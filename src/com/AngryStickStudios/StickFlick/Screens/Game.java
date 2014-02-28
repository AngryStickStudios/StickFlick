package com.AngryStickStudios.StickFlick.Screens;

import java.util.Vector;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
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
import com.AngryStickStudios.StickFlick.Entities.Entity;
import com.AngryStickStudios.StickFlick.Entities.Player;
import com.AngryStickStudios.StickFlick.Entities.WalkingEnemy;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;

public class Game implements Screen, GestureListener {

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
    private long coinageTotal = 0; // Keeps track of money (coinage) earned in-game - Alex
    private int freeze = 0;
    private float freezeTime = 10;
    private int healthRegen = 7500;
	
	StickFlick game;
	SpriteBatch batch;
	Texture gameBackground, castleOnly;
	Stage stage, pauseStage, deathStage;
	Group bg, fg;
	Skin skin;
	BitmapFont white;
	GestureDetector gd;
	TextureAtlas atlas;
	InputMultiplexer im;
	TextButton pauseButton, resumeButton, mainMenuButton, mainMenuButton2;
	LabelStyle labelStyle, labelStyleCoinage, labelStyleDeath; // Added labelStyleCoinage to test coinage - Alex
	Label timer, coinageDisplay, deathMessage;              // Added coinageDisplay to test coinage - Alex
	Vector<WalkingEnemy> enemyList;
	Player player;
	OrthographicCamera camera;
	ShapeRenderer sp;
	Button freezePow, explodePow, healthPow;
	Timer spawnTimer, freezeTimer;
	double sumSpawn = 0;
	double timeSpawn = 0;
	
	
	public Game(StickFlick game){
		this.game = game;
		
		spawnTimer.schedule(new Task() {
			@Override
			public void run() {
				spawn();
			}
		}, 0, 5);
		
		freezeTimer.schedule(new Task() {
			@Override
			public void run() {
				freezeCheck();
			}
		}, 0, 1);
		
		
		
		player = new Player("testPlayer", 30000);
		enemyList = new Vector<WalkingEnemy>();
	
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
		}
				
		if (gameStatus == GAME_RUNNING) {
			stage.act(Gdx.graphics.getDeltaTime());
			for(int i = 0; i < enemyList.size(); i++) {
				if(enemyList.get(i).getIsAlive())
					enemyList.get(i).Update(delta);
				else{
					bg.removeActor(enemyList.get(i).getImage());
					
					enemyList.remove(i);
				}
			}
			
			int enemiesAtWall = 0;
			
			for(int i = 0; i < enemyList.size(); i++){
				
				if(enemyList.get(i).getImage().getY() < Gdx.graphics.getHeight() * 0.11f){
					enemiesAtWall++;
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
				increaseCoinage(80);
				
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
		healthPow = new Button(skin.getDrawable("ExplosionPowerupButtonLight"), skin.getDrawable("ExplosionPowerupButtonDark"));
		healthPow.setWidth(Gdx.graphics.getWidth() / 16);
		healthPow.setHeight(Gdx.graphics.getWidth() / 16);
		healthPow.setX(Gdx.graphics.getWidth() * 0.005f);
		healthPow.setY(Gdx.graphics.getHeight() * 0.50f);
		fg.addActor(healthPow);
			
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
		deathMessage = new Label("You Died!!!", labelStyleDeath);
		deathMessage.setX(Gdx.graphics.getWidth() / 2 - deathMessage.getWidth()/2);
		deathMessage.setY(Gdx.graphics.getHeight() / 2 - deathMessage.getHeight());
		deathStage.addActor(deathMessage);

		mainMenuButton2 = new TextButton("Main Menu", buttonStyle);
		mainMenuButton2.setWidth(Gdx.graphics.getWidth() / 6);
		mainMenuButton2.setHeight(Gdx.graphics.getHeight() / 12);
		mainMenuButton2.setX(Gdx.graphics.getWidth()/2 - mainMenuButton2.getWidth()/2);
		mainMenuButton2.setY(Gdx.graphics.getHeight()/2 + mainMenuButton2.getHeight()/2);
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
		Gdx.input.setInputProcessor(pauseStage);
	}
	
	public void resumeGame() {
		gameStatus = GAME_RUNNING;
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

	/*******************
	 * Spawning
	 *******************/

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


	public void spawn() {
		Random generator = new Random();
		int x;
		int rate;

		timeSpawn = timeSpawn + 5;
		
		if(timeSpawn <= 10){
			rate = 1;
		} else if(timeSpawn > 10 && timeSpawn <= 30){
			rate = 2;
		} else if(timeSpawn > 30 && timeSpawn <= 90){
			rate = 3;
		} else if(timeSpawn > 90 && timeSpawn <= 180){
			rate = 4;
		} else{
			rate = 5;
		}

		if(freeze == 0){
			for(int i = 0; i < rate; i++){
				x = generator.nextInt((int)(Gdx.graphics.getWidth()*4/5)) + (int)(Gdx.graphics.getWidth()/10);
				enemyList.add(new WalkingEnemy("basic", 100, x, (int) (Gdx.graphics.getHeight() / 1.75)));		
				bg.addActor(enemyList.get((enemyList.size())-1).getShadow());
				bg.addActor(enemyList.get((enemyList.size())-1).getImage());
			}
		}
	}	

	/*********************************
	 * Coinage Generation & Management
	 *********************************/

	// Public methods for getting and setting private long coinageTotal
	public void setCoinage(long coinageTotal) {
		this.coinageTotal = coinageTotal;
	}

	public long getCoinage() {
		return coinageTotal;
	}	 

	// Methods for modifying totalCoinage
	public void increaseCoinage(long coinageAcquired){ // adds coins to wallet
		setCoinage(getCoinage() + coinageAcquired);
	}

	public void decreaseCoinage(long coinageSpent){
		setCoinage(getCoinage() - coinageSpent);
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
		// UNUSED
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
