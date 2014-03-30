package com.AngryStickStudios.StickFlick.Screens;

import java.util.Vector;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.input.GestureDetector.GestureListener;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.AngryStickStudios.StickFlick.StickFlick;
import com.AngryStickStudios.StickFlick.Controller.GestureDetection;
import com.AngryStickStudios.StickFlick.Entities.Champion;
import com.AngryStickStudios.StickFlick.Entities.Entity;
import com.AngryStickStudios.StickFlick.Entities.Player;
import com.AngryStickStudios.StickFlick.Entities.WalkingEnemy;
import com.AngryStickStudios.StickFlick.Entities.FlyingEnemy;

import java.util.Random;
import java.util.concurrent.TimeUnit;

import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;

public class Game implements Screen, GestureListener {

	//Stores currency and high scores
	Preferences prefs = Gdx.app.getPreferences("Preferences");
	
	public static final int GAME_LOST = 2;
	public static final int GAME_RUNNING = 1;
    public static final int GAME_PAUSED = 0;
    private int gameStatus = 1;
    private float timeTrack = 0;
    private int seconds = 0;
    private int minutes = 0;
    private String formattedTime = "0:00";
    private boolean enemyGrabbed = false;
    private int grabbedNumber = -1;
    private long coinageTotal = prefs.getLong("currency", 0); 
    private int freeze = 0;
    private float freezeTime = 10;
    private int healthRegen = 7500;
    private boolean god = false;
    private int godTime = 5;
    private int score = 0;
    private int[] scores = {prefs.getInteger("score1", 0), prefs.getInteger("score2", 0), prefs.getInteger("score3", 0)};
    
    
	StickFlick game;
	SpriteBatch batch;
	Texture gameBackground, castleOnly, serfTex;
	Stage stage, pauseStage, deathStage;
	Group bg, fg;
	Skin skin;
	BitmapFont white;
	GestureDetector gd;
	TextureAtlas atlas;
	InputMultiplexer im;
	TextButton pauseButton, resumeButton, mainMenuButton, mainMenuButton2;
	LabelStyle labelStyle, labelStyleCoinage, labelStyleDeath, labelStyleScore; 
	Label timer, coinageDisplay, deathMessage, finalScore;             
	Vector<Entity> enemyList;
	Champion curChamp;
	Player player;
	OrthographicCamera camera;
	ShapeRenderer sp;
	Button freezePow, explodePow, healthPow, godPow, serfPow;
	Timer spawnTimerOuter, spawnTimerInner, freezeTimer, godTimer;
	double timeSpawn, timeEquation, timeSetSpawn = 0;
	final double DEATHTIME = .25;
	boolean justUnfrozen = false;
	
	
	public Game(StickFlick game){
		this.game = game;
		
		spawnTimerOuter.schedule(new Task() {
			@Override
			public void run() {
				if(timeSpawn == 0) {
					timeEquation = 2.625 - 0.009375*timeSpawn;
					
					spawnTimerInner.schedule(new Task() {
						@Override
						public void run() {
							spawn();
						}
					}, (float)(timeEquation), 0, 0);
				}
				else if(timeSetSpawn >= timeEquation) {
					timeSetSpawn = 0;
					timeEquation = 2.625 - 0.009375*timeSpawn;
					
					spawnTimerInner.schedule(new Task() {
						@Override
						public void run() {
							spawn();
						}
					}, (float)(timeEquation), 0, 0);
				}
				else if(timeSpawn > 240) {
					spawnTimerInner.schedule(new Task() {
						@Override
						public void run() {
							spawn();
						}
					}, 0, 0, 0);
				}
				
				timeSpawn += .25;
				timeSetSpawn += .25;
			}
		}, 0, .25f);
		
		freezeTimer.schedule(new Task() {
			@Override
			public void run() {
				freezeCheck();
			}
		}, 0, 1);
		
		godTimer.schedule(new Task() {
			@Override
			public void run() {
				godCheck();
			}
		}, 0, 1);
		
		player = new Player("testPlayer", 30000);
		curChamp = null;
		enemyList = new Vector<Entity>();
	
		/* Health initialization */
		camera= new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
	    camera.setToOrtho(true, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
	    camera.update();
	    sp = new ShapeRenderer(); 
    
	}
	
	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		
		if(player.getIsAlive() == false){
			gameStatus = GAME_LOST;
			Gdx.input.setInputProcessor(deathStage);
			
			//Saves currency count when game is over
			prefs.putLong("currency", getCoinage());
			prefs.flush();
			
			//Saves potential high score
			finalScore.setText("Score: " + ((60 * minutes) + seconds));	
			score = (60 * minutes) + seconds;
			if (score > scores[0]) {
				prefs.putInteger("score1", score);
				prefs.putInteger("score2", scores[0]);
				prefs.putInteger("score3", scores[1]);
				prefs.flush();
			}
			
			else if (score > scores[1]) {
				prefs.putInteger("score2", score);
				prefs.putInteger("score3", scores[1]);
				prefs.flush();
			}
			
			else if (score > scores[2]){
				prefs.putInteger("score3", score);
				prefs.flush();
			}
		}
				
		if (gameStatus == GAME_RUNNING) {
			stage.act(Gdx.graphics.getDeltaTime());
			for(int i = 0; i < enemyList.size(); i++) {
				if(enemyList.get(i).getIsAlive())
					enemyList.get(i).Update(delta*.4f);
				else{
					bg.removeActor(enemyList.get(i).getImage());
					bg.removeActor(enemyList.get(i).getShadow());
					enemyList.remove(i);
				}
				
				if(curChamp != null)
				{
					if(curChamp.getIsAlive())
					{
						curChamp.Update(delta);
						
						if(enemyList.size() > 0 && curChamp.getTarget() == null)
						{
							curChamp.setTarget(enemyList.get((int) Math.round(((Math.random() * (enemyList.size()-1))))));
						}
					}
					else
					{
						bg.removeActor(curChamp.getImage());
						bg.removeActor(curChamp.getShadow());
						curChamp = null;
					}
				}
			}
			
		
			if(Gdx.input.isKeyPressed(Keys.C) && curChamp == null)
			{
				curChamp = new Champion("champ", 45, Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY());
				bg.addActor(curChamp.getImage());
				bg.addActor(curChamp.getShadow());
			}
		
			
			int enemiesAtWall = 0;
			
			for(int i = 0; i < enemyList.size(); i++){
				
				if((enemyList.get(i).getImage().getY() < Gdx.graphics.getHeight() * 0.11f) && !(enemyList.get(i).getName()).equals("HeavyFlier") && !(enemyList.get(i).getName()).equals("Flier")){
					if((enemyList.get(i).getName()).equals("Basic") || (enemyList.get(i).getName()).equals("Archer")){
						enemiesAtWall++;
					}
					else if((enemyList.get(i).getName()).equals("BigDude")){
						enemiesAtWall += 40; //4% of castle health/second accounting for 0.1% = one enemy at wall
					}
					else if((enemyList.get(i).getName()).equals("Demo")){
						//immediately decreases castle health by 1.2%
						player.decreaseHealth((int)(player.getCastleMaxHealth() * (1.2 / 100)));
					}
				}
				else if(enemyList.get(i).getImage().getY() < Gdx.graphics.getHeight() * 0.25f){ // USED TO BE 0.11f
					if((enemyList.get(i).getName()).equals("Flier")){
						enemiesAtWall++;
					}
					else if((enemyList.get(i).getName()).equals("HeavyFlier")){
						enemiesAtWall += 2;
					}
				}
			
			}
			
			player.setEnAtWall(enemiesAtWall);
			
			player.Update();
			
			batch.begin();
			stage.draw();	
			
			/* Drawing health bar and decreasing health when needed */
			batch.end();
			sp.setProjectionMatrix(camera.combined);
			sp.begin(ShapeType.Filled);
			sp.setColor(Color.RED);
				
			float healthBarSize = Gdx.graphics.getWidth()/2 * (player.getHealthCurrent()/player.getHealthMax());
				
			sp.rect(Gdx.graphics.getWidth()/4,Gdx.graphics.getHeight()*.025f, healthBarSize, Gdx.graphics.getHeight()/24);
			sp.end();
			batch.begin();
			
			if(enemyGrabbed && !Gdx.input.isTouched())
			{
				enemyGrabbed = false;
				enemyList.get(grabbedNumber).Released(new Vector2(0,0));
			}
			
			timeTrack += Gdx.graphics.getDeltaTime();
			if (timeTrack >= 1f) {
				
				timeTrack = timeTrack - 1f;
				seconds++;
				
				// Increase coinage by 80 each second
				increaseCoinage(8);
				
				if (seconds >= 60) {
					seconds = seconds - 60;
					minutes++;
				}
				
				if (seconds < 10) {
					formattedTime = Integer.toString(minutes) + ":0" + Integer.toString(seconds);
				}
				
				else {
					formattedTime = Integer.toString(minutes) + ":" + Integer.toString(seconds);
				}
				
				timer.setText(formattedTime);
				coinageDisplay.setText("$" + String.valueOf(getCoinage())); // To display coinage
			}
			
			batch.end();
		} else if(gameStatus == GAME_PAUSED) {
			pauseStage.act(Gdx.graphics.getDeltaTime());
			batch.begin();
			pauseStage.draw();
			batch.end();
		} else if(gameStatus == GAME_LOST){
			deathStage.act(Gdx.graphics.getDeltaTime());
			batch.begin();
			deathStage.draw();
			batch.end();
		} else{
			System.out.println("Kudos to you... you reached a secret impossible game status?");
		}
	}
	

	@Override
	public void resize(int width, int height) {
		stage = new Stage(width, height, true);
		stage.clear();
		
		pauseStage = new Stage(width, height, true);
		pauseStage.clear();
		
		deathStage = new Stage(width, height, true);
		deathStage.clear();
		
		bg = new Group();
		fg = new Group();
		
		if(gameStatus == GAME_RUNNING) {
			im = new InputMultiplexer(new GestureDetector(this), stage);
			Gdx.input.setInputProcessor(im);
		} else if(gameStatus == GAME_PAUSED){
			Gdx.input.setInputProcessor(pauseStage);
		} else if(gameStatus == GAME_LOST){
			Gdx.input.setInputProcessor(deathStage);
		} else{
			System.out.println("How did you do this?");
		}
		
		stage.addActor(bg);
		stage.addActor(fg);
		
		TextButtonStyle buttonStyle = new TextButtonStyle();
		buttonStyle.up = skin.getDrawable("LightButton");
		buttonStyle.down = skin.getDrawable("DarkButton");
		buttonStyle.font = white;
		
		gameBackground = new Texture("data/gameBackground.png");
		Image backgroundImage = new Image(gameBackground);
		backgroundImage.setZIndex(100000);
		backgroundImage.setWidth(Gdx.graphics.getWidth());
		backgroundImage.setHeight(Gdx.graphics.getHeight());
		bg.addActor(backgroundImage);
		
		castleOnly = new Texture("data/castleOnly.png");
		Image castleImage = new Image(castleOnly);
		castleImage.setWidth(Gdx.graphics.getWidth());
		castleImage.setHeight(Gdx.graphics.getHeight());
		fg.addActor(castleImage);
		
		pauseButton = new TextButton("Pause", buttonStyle);
		pauseButton.setWidth(Gdx.graphics.getWidth() / 6);
		pauseButton.setHeight(Gdx.graphics.getHeight() / 12);
		pauseButton.setX(Gdx.graphics.getWidth() * 0.80f);
		pauseButton.setY(Gdx.graphics.getHeight() * 0.90f);
		fg.addActor(pauseButton);
		
		//Explosion button, kills everyone!
		explodePow = new Button(skin.getDrawable("ExplosionPowerupButtonLight"), skin.getDrawable("ExplosionPowerupButtonDark"));
		explodePow.setWidth(Gdx.graphics.getWidth() / 16);
		explodePow.setHeight(Gdx.graphics.getWidth() / 16);
		explodePow.setX(Gdx.graphics.getWidth() * 0.005f);
		explodePow.setY(Gdx.graphics.getHeight() * 0.8f);
		fg.addActor(explodePow);
		
		//Freeze powerup button
		freezePow = new Button(skin.getDrawable("IcePowerupButtonLight"), skin.getDrawable("IcePowerupButtonDark"));
		freezePow.setWidth(Gdx.graphics.getWidth() / 16);
		freezePow.setHeight(Gdx.graphics.getWidth() / 16);
		freezePow.setX(Gdx.graphics.getWidth() * 0.005f);
		freezePow.setY(Gdx.graphics.getHeight() * 0.65f);
		fg.addActor(freezePow);
		
		//Health button, restores certain percent of castle health
		healthPow = new Button(skin.getDrawable("HealPowerupButtonLight"), skin.getDrawable("HealPowerupButtonDark"));
		healthPow.setWidth(Gdx.graphics.getWidth() / 16);
		healthPow.setHeight(Gdx.graphics.getWidth() / 16);
		healthPow.setX(Gdx.graphics.getWidth() * 0.005f);
		healthPow.setY(Gdx.graphics.getHeight() * 0.50f);
		fg.addActor(healthPow);
		
		//Finger of God button, tap to kill!
		godPow = new Button(skin.getDrawable("HealPowerupButtonLight"), skin.getDrawable("HealPowerupButtonDark"));
		godPow.setWidth(Gdx.graphics.getWidth() / 16);
		godPow.setHeight(Gdx.graphics.getWidth() / 16);
		godPow.setX(Gdx.graphics.getWidth() * 0.005f);
		godPow.setY(Gdx.graphics.getHeight() * 0.35f);
		fg.addActor(godPow);
		
		// Serf "Powerup": Gradually heals (increases castle health) in the style of Health Button
		serfPow = new Button(skin.getDrawable("HealPowerupButtonLight"), skin.getDrawable("HealPowerupButtonDark"));
		serfPow.setWidth(Gdx.graphics.getWidth() / 16);
		serfPow.setHeight(Gdx.graphics.getWidth() / 16);
		serfPow.setX(Gdx.graphics.getWidth() * 0.005f);
		serfPow.setY(Gdx.graphics.getHeight() * 0.20f);
		fg.addActor(serfPow);
		
		labelStyle = new LabelStyle(white, Color.BLACK);
		timer = new Label(formattedTime, labelStyle);
		timer.setHeight(Gdx.graphics.getHeight() / 24);
		timer.setX(Gdx.graphics.getWidth() * 0.025f);
		timer.setY(Gdx.graphics.getHeight() * 0.95f);
		fg.addActor(timer);
		
		// Making Label for Coinage (for testing purposes) - Alex 
	    labelStyleCoinage = new LabelStyle(white, Color.ORANGE);
		coinageDisplay = new Label(String.valueOf(getCoinage()), labelStyleCoinage);
		coinageDisplay.setHeight(Gdx.graphics.getHeight() / 24);
		coinageDisplay.setX(Gdx.graphics.getWidth() * 0.085f);
		coinageDisplay.setY(Gdx.graphics.getHeight() * 0.95f);
		stage.addActor(coinageDisplay);
		
		//Pause button stage, not main stage
		resumeButton = new TextButton("Resume", buttonStyle);
		resumeButton.setWidth(Gdx.graphics.getWidth() / 6);
		resumeButton.setHeight(Gdx.graphics.getHeight() / 12);
		resumeButton.setX(Gdx.graphics.getWidth()/2 - resumeButton.getWidth()/2);
		resumeButton.setY(Gdx.graphics.getHeight()/2 - resumeButton.getHeight()/2);
		pauseStage.addActor(resumeButton);
		
		mainMenuButton = new TextButton("Main Menu", buttonStyle);
		mainMenuButton.setWidth(Gdx.graphics.getWidth() / 6);
		mainMenuButton.setHeight(Gdx.graphics.getHeight() / 12);
		mainMenuButton.setX(Gdx.graphics.getWidth()/2 - resumeButton.getWidth()/2);
		mainMenuButton.setY(Gdx.graphics.getHeight()/2 - resumeButton.getHeight()*2);
		pauseStage.addActor(mainMenuButton);
		
		//Death message for Lost Stage
		labelStyleDeath = new LabelStyle(white, Color.RED);
		deathMessage = new Label("Game Over...", labelStyleDeath);
		deathMessage.setX(Gdx.graphics.getWidth() / 2 - deathMessage.getWidth()/2);
		deathMessage.setY(Gdx.graphics.getHeight() / 2 + deathMessage.getHeight() * 3);
		deathStage.addActor(deathMessage);
		
		//Final score
		labelStyleScore = new LabelStyle(white, Color.ORANGE);
		finalScore = new Label("Score: 999", labelStyleScore);
		finalScore.setX(Gdx.graphics.getWidth() / 2 - finalScore.getWidth()/2);
		finalScore.setY(Gdx.graphics.getHeight() / 2);
		deathStage.addActor(finalScore);
		
		//Return to main menu
		mainMenuButton2 = new TextButton("Main Menu", buttonStyle);
		mainMenuButton2.setWidth(Gdx.graphics.getWidth() / 6);
		mainMenuButton2.setHeight(Gdx.graphics.getHeight() / 12);
		mainMenuButton2.setX(Gdx.graphics.getWidth()/2 - mainMenuButton2.getWidth()/2);
		mainMenuButton2.setY(Gdx.graphics.getHeight()/2 - mainMenuButton2.getHeight() * 2);
		deathStage.addActor(mainMenuButton2);
		
		for(int i = 0; i < enemyList.size(); i++) {
			bg.addActor(enemyList.get(i).getShadow());
			bg.addActor(enemyList.get(i).getImage());
		}
		
		stage.addAction(Actions.sequence(Actions.alpha(0), Actions.fadeIn(1)));
		pauseStage.addAction(Actions.sequence(Actions.alpha(0), Actions.fadeIn(1)));
		
		pauseButton.addListener(new InputListener(){
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				System.out.println("down");
				return true;
			}
			public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
				System.out.println("up");
				
				stage.addAction(Actions.sequence(Actions.fadeOut(.3f), Actions.run(new Runnable() {
					@Override
					public void run() {
						pauseGame();
					}
				})));
			}
		});
		
		freezePow.addListener(new InputListener(){
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				System.out.println("down");
				return true;
			}
			public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
				System.out.println("up");
				for(int i = 0; i < enemyList.size(); i++){
					enemyList.get(i).freeze();
				}
				freeze = 1;
			}
		});
		
		explodePow.addListener(new InputListener(){
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				System.out.println("down");
				return true;
			}
			public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
				System.out.println("up");
				for(int i = 0; i < enemyList.size(); i++){
					Random generator = new Random();
					int test = generator.nextInt(10) - 5;
					Vector2 explode = new Vector2(test, 20);
					enemyList.get(i).pickedUp();
					enemyList.get(i).Released(explode);
				}
			}
		});
		
		healthPow.addListener(new InputListener(){
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				System.out.println("down");
				return true;
			}
			public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
				System.out.println("up");
				
				float newHealth = player.getHealthCurrent() + healthRegen;
				
				if(newHealth > player.getHealthMax()) {
					player.setHealthCurrent(player.getHealthMax());
				}
				
				else {
					player.setHealthCurrent(newHealth);
				}	 
			}
		});
		
		godPow.addListener(new InputListener(){
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				System.out.println("down");
				return true;
			}
			public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
				System.out.println("up"); 
				god = true;	
			}
		});
		
		serfPow.addListener(new InputListener(){ 
			
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				System.out.println("down");
				return true;
			}
			public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
				System.out.println("up");
				//serf = Gdx.audio.newSound(Gdx.files.internal("data/serf.wav"));
				//serf.play();
				// Place the serf on the castle
				// Put a number below the serf to indicate serfs currently in play
				//
				Image serfImage;
				serfTex = new Texture("data/serf.png");
				serfImage = new Image(serfTex);
				serfImage.setX(Gdx.graphics.getWidth() * 0.85f); 
				serfImage.setY(Gdx.graphics.getHeight() * 0.04f);
				serfImage.setScale(0.5f);
				fg.addActor(serfImage);
				// Reset this number to zero during next game?
				float newHealth = player.getHealthCurrent() + healthRegen;
				
				if(newHealth > player.getHealthMax()) {
					player.setHealthCurrent(player.getHealthMax());
				}
				
				else {
					player.setHealthCurrent(newHealth);
				}	 
			}
		});
		
		resumeButton.addListener(new InputListener(){
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				System.out.println("down");
				return true;
			}
			public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
				System.out.println("up");
				
				pauseStage.addAction(Actions.sequence(Actions.fadeOut(.3f), Actions.run(new Runnable() {
					@Override
					public void run() {
						resumeGame();
					}
				})));
				
				freeze = 0;
			}
		});
		
		mainMenuButton.addListener(new InputListener(){
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				System.out.println("down");
				return true;
			}
			public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
				System.out.println("up");
				
				pauseStage.addAction(Actions.sequence(Actions.fadeOut(.3f), Actions.run(new Runnable() {
					@Override
					public void run() {
						((StickFlick) Gdx.app.getApplicationListener()).setScreen(new MainMenu(game));
					}
				})));
			}
		});
		
		mainMenuButton2.addListener(new InputListener(){
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				System.out.println("down");
				return true;
			}
			public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
				System.out.println("up");
				
				deathStage.addAction(Actions.sequence(Actions.fadeOut(.3f), Actions.run(new Runnable() {
					@Override
					public void run() {
						((StickFlick) Gdx.app.getApplicationListener()).setScreen(new MainMenu(game));
					}
				})));
			}
		});
	}

	@Override
	public void show() {
		
		batch = new SpriteBatch();
		
		atlas = new TextureAtlas("data/Textures.atlas");
		skin = new Skin();
		skin.addRegions(atlas);
		
		white = new BitmapFont(Gdx.files.internal("data/whiteFont.fnt"), false);
	}

	@Override
	public void hide() {
		
	}

	@Override
	public void pause() {
		pauseGame();
	}
	
	public void pauseGame() {
		gameStatus = GAME_PAUSED;
		spawnTimerInner.instance().stop();
		spawnTimerOuter.instance().stop();
		Gdx.input.setInputProcessor(pauseStage);
	}
	
	public void resumeGame() {
		gameStatus = GAME_RUNNING;
		spawnTimerInner.instance().start();
		spawnTimerOuter.instance().start();
		resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
	}

	@Override
	public void resume() {

	}

	@Override
	public void dispose() {
		batch.dispose();
		game.dispose();
		gameBackground.dispose();
		stage.dispose();
		pauseStage.dispose();
		skin.dispose();	
	}

	//If the freeze powerup is enabled, spawn will not be called
	public void freezeCheck() {

		if (freeze == 0) {
			
		} else if ((freeze == 1) && (freezeTime != 0) ) {
			System.out.println(freezeTime);
			freezeTime--;
		} else {
			freezeTime = 10;
			for(int i = 0; i < enemyList.size(); i++){
				enemyList.get(i).unfreeze();
			}
			freeze = 0;
		}
	}
	
	public void godCheck() {
		if (god == true && (godTime != 0)) {
			godTime--;
		}
		else {
			godTime = 5;
			god = false;
		}
	}

	/*******************
	 * Spawning
	 *******************/
	
	public void spawn() {
		if(freeze == 0){
			Random generator = new Random();
			
			int x = generator.nextInt((int)(Gdx.graphics.getWidth()*4/5)) + (int)(Gdx.graphics.getWidth()/10);
			int x2 = generator.nextInt((int)(Gdx.graphics.getWidth()*4/5)) + (int)(Gdx.graphics.getWidth()/10);
			
			double per_x = ((float)x / (float)Gdx.graphics.getWidth()) * 100;
			
			double per_y = (int)((2.58167567540614 * Math.pow(10, -16) * Math.pow(per_x,10)) 
					+ (-1.95342140280605 * Math.pow(10, -13) * Math.pow(per_x,9)) 
					+ (5.67913824173503 * Math.pow(10, -11) * Math.pow(per_x,8)) 
					+ (-8.57596416430226 * Math.pow(10, -9) * Math.pow(per_x,7)) 
					+ (7.44412734903002 * Math.pow(10, -7) * Math.pow(per_x,6)) 
					+ (-0.0000381433485700688 * Math.pow(per_x,5)) 
					+ (0.00112983288812486 * Math.pow(per_x,4)) 
					+ (-0.0179778462008673 * Math.pow(per_x,3)) 
					+ (0.120431228628006 * Math.pow(per_x,2)) 
					+ (0.227287085911332 * per_x) 
					+ (53.4555698252754));
			
			int y = (int)((per_y / 100) * Gdx.graphics.getHeight());
			
			System.out.println("y = " + y);
			
			//enemyList.add(new WalkingEnemy("Basic", 100, x, (int) (Gdx.graphics.getHeight() / 1.75)));
			//enemyList.add(new WalkingEnemy("Basic", 100, x, y));
			//Testing the spawning of flying units (we can balance this and make it happen only in later rounds in Sprint 3)
			enemyList.add(new FlyingEnemy("Flier", 100, x2, y));
			bg.addActor(enemyList.get((enemyList.size())-1).getShadow());
			bg.addActor(enemyList.get((enemyList.size())-1).getImage());
			//bg.addActor(enemyList.get((enemyList.size())-2).getShadow());
			//bg.addActor(enemyList.get((enemyList.size())-2).getImage());
		}
	}	

	/*********************************
	 * Coinage Generation & Management
	 *********************************/

	// Public methods for getting and setting private long coinageTotal
	public void setCoinage(long coinageTotal) {
		this.coinageTotal = coinageTotal;
		//prefs.putLong("currency", getCoinage());
		//prefs.flush();
	}

	public long getCoinage() {
		return coinageTotal;
	}	 

	// Methods for modifying totalCoinage
	public void increaseCoinage(long coinageAcquired){ // adds coins to wallet
		setCoinage(getCoinage() + coinageAcquired);
		//prefs.putLong("currency", getCoinage());
		//prefs.flush();
	}

	public void decreaseCoinage(long coinageSpent){
		setCoinage(getCoinage() - coinageSpent);
		//prefs.putLong("currency", getCoinage());
		//prefs.flush();
	}

	/*******************
	 * Gesture Detection
	 *******************/
	@Override
	public boolean touchDown(float x, float y, int pointer, int button) {
		y = Gdx.graphics.getHeight() - y;
		if(enemyGrabbed == false){
			for(int i = 0; i < enemyList.size(); i++)	// Searches through enemy list
			{
				Vector2 size = enemyList.get(i).getSize();
				Vector2 pos = enemyList.get(i).getPosition();
				if((pos.x - size.x <= x && x <= pos.x + size.x) && (pos.y - size.y<= y && y < pos.y + size.y)){
					grabbedNumber = i;
					enemyGrabbed = true;
					enemyList.get(grabbedNumber).pickedUp();
					break;
				}
			}
		}
		return false;
	}

	@Override
	public boolean tap(float x, float y, int count, int button) {
	
		//tap to kill
		if(enemyGrabbed == true && god)
		{
			enemyGrabbed = false;
			enemyList.get(grabbedNumber).setIsAlive(false);
		}
		
		return false;
	}

	@Override
	public boolean longPress(float x, float y) {
		// UNUSED
		return false;
	}

	@Override
	public boolean fling(float velocityX, float velocityY, int button) {
		if(enemyGrabbed == true)
		{
			enemyGrabbed = false;
			enemyList.get(grabbedNumber).Released(new Vector2(velocityX / 1000, velocityY / -1000));
		}
		return false;
	}

	@Override
	public boolean pan(float x, float y, float deltaX, float deltaY) {
			// UNUSED
		return false;
	}

	@Override
	public boolean panStop(float x, float y, int pointer, int button) {
			// UNUSED
		return false;
	}

	@Override
	public boolean zoom(float initialDistance, float distance) {
			// UNUSED
		return false;
	}

	@Override
	public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2) {
			// UNUSED
		return false;
	}

}
