package com.AngryStickStudios.StickFlick.Screens;

import java.util.Hashtable;
import java.util.Vector;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Preferences;
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
import com.AngryStickStudios.StickFlick.StickFlick;
import com.AngryStickStudios.StickFlick.Controller.GestureDetection;
import com.AngryStickStudios.StickFlick.Controller.AnimationLoader;
import com.AngryStickStudios.StickFlick.Entities.BigDude;
import com.AngryStickStudios.StickFlick.Entities.Champion;
import com.AngryStickStudios.StickFlick.Entities.DemoDude;
import com.AngryStickStudios.StickFlick.Entities.Entity;
import com.AngryStickStudios.StickFlick.Entities.Player;
import com.AngryStickStudios.StickFlick.Entities.Priest;
import com.AngryStickStudios.StickFlick.Entities.StickDude;
import com.AngryStickStudios.StickFlick.Entities.WalkingEnemy;

import java.util.Random;

import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;

public class Game implements Screen{

	//Stores currency and high scores
	Preferences prefs = Gdx.app.getPreferences("Preferences");
		
	public static final int GAME_LOST = 3;
	public static final int POWERUP_PAUSE = 2;
	public static final int GAME_PAUSED = 1;
	public static final int GAME_RUNNING = 0;
	private int gameStatus = GAME_RUNNING;
	private float timeTrack = 0;
	private int seconds = 0;
	private int minutes = 0;
	private String formattedTime = "0:00";
	private boolean enemyGrabbed = false;
	private int grabbedNumber = -1;
	private long coinageTotal = prefs.getLong("currency", 0); 
	private int explodeCDTimer = 0;
	private int healthCDTimer = 0;
	private int freeze = 0;
	private float freezeTime = 10;
	private int freezeCDTimer = 0;
	private int healthRegen = 7500;
	private boolean god = false;
	private int godTime = 5;
	private int godCDTimer = 0;
	private int score = 0;
	private int[] scores = {prefs.getInteger("score1", 0), prefs.getInteger("score2", 0), prefs.getInteger("score3", 0)};
	private double[] spawnLocation = new double[101];
    
	StickFlick game;
	SpriteBatch batch;
	Texture gameBackground, castleOnly, gameHills;
	Stage stage, pauseStage, powerupStage, deathStage;
	Group bg, hg, fg;
	Skin skin;
	BitmapFont white;
	GestureDetection gd;
	TextureAtlas atlas;
	InputMultiplexer im;
	TextButton pauseButton, powerupButton, resumeButton, mainMenuButton, mainMenuButton2;
	LabelStyle labelStyle, labelStyleCoinage, labelStyleDeath, labelStyleScore; 
	Label timer, coinageDisplay, deathMessage, finalScore;
	Vector<Entity> enemyList;
	Champion curChamp;
	Player player;
	OrthographicCamera camera;
	ShapeRenderer sp;
	Button freezePow, explodePow, healthPow, godPow, championPow, freezeCD, godCD, healthCD, explodeCD, championCD;
	Timer spawnTimer, spawnTimerOuter, spawnTimerInner, freezeTimer, godTimer, coolDownTimer;
	double timeSpawn, timeEquation, timeSetSpawn = 0;
	final double DEATHTIME = .25;
	boolean justUnfrozen = false, priestButtonDown = false;
	AnimationLoader anims;
	
	
	public Game(StickFlick game){
		this.game = game;
		anims = new AnimationLoader();
		
		generateSpawnLocations();
		
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
		
		coolDownTimer.schedule(new Task() {
			@Override
			public void run() {
				coolDownCheck();
			}
		}, 0, 1);
		
		player = new Player("testPlayer", 30000, anims);
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
		
		// GAME RUNNING
		if (gameStatus == GAME_RUNNING) {
			stage.act(Gdx.graphics.getDeltaTime());
			for(int i = 0; i < enemyList.size(); i++) {
				if(enemyList.get(i).getIsAlive())
				{
					enemyList.get(i).Update(delta);
					if(enemyList.get(i).getName() == "Priest")
					{
						if(enemyList.size() > 1 && (enemyList.get(i).getTarget() == null || enemyList.get(i).getTarget().getIsAlive() == false))
						{
							enemyList.get(i).setTarget(PriorityHeal(enemyList.get(i)));
						}
					}
				}
				else
				{
					hg.removeActor(enemyList.get(i).getShadow());
                    if(enemyList.get(i).getSplatting() != 1)
                    {
                            hg.removeActor(enemyList.get(i).getImage());
                            enemyList.remove(i);
                    }
				}	
			}
			
			if(curChamp != null)
			{
				if(curChamp.getIsAlive())
				{
					curChamp.Update(delta);

					if(enemyList.size() > 0 && (curChamp.getTarget() == null || curChamp.getTarget().getIsAlive() == false))
					{
						curChamp.setTarget(PriorityTarget());
					}
				}
				else
				{
					hg.removeActor(curChamp.getImage());
					hg.removeActor(curChamp.getShadow());
					curChamp = null;
				}
			}
		
			if(Gdx.input.isKeyPressed(Keys.C) && curChamp == null)
			{
				curChamp = new Champion("champ", 45, anims, Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY());
				hg.addActor(curChamp.getImage());
				hg.addActor(curChamp.getShadow());
			}
			
			/*
			if(Gdx.input.isKeyPressed(Keys.P) && priestButtonDown == false)
			{
				Entity newPriest = new Priest("priest", 100, Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY());
				enemyList.add(newPriest);
				hg.addActor(newPriest.getImage());
				hg.addActor(newPriest.getShadow());
				newPriest.setChanged(true);
				newPriest.setPeak(0);
				priestButtonDown = true;
			}
			else
			{
				priestButtonDown = false;
			}
			*/

			int enemiesAtWall = 0;
			
			for(int i = 0; i < enemyList.size(); i++){
				
				if(enemyList.get(i).getImage().getY() < Gdx.graphics.getHeight() * 0.11f){
					if((enemyList.get(i).getName()).equals("Basic") || (enemyList.get(i).getName()).equals("Archer") || (enemyList.get(i).getName()).equals("Flier")){
						enemiesAtWall++;
					}
					else if((enemyList.get(i).getName()).equals("HeavyFlier")){
						enemiesAtWall += 2;
					}
					else if((enemyList.get(i).getName()).equals("BigDude")){
						enemiesAtWall += 40; //4% of castle health/second accounting for 0.1% = one enemy at wall
					}
					else if((enemyList.get(i).getName()).equals("Demo")){
						//immediately decreases castle health by 1.2%
						player.decreaseHealth((int)(player.getCastleMaxHealth() * (1.2 / 100)));
						enemyList.get(i).setIsAlive(false);
					}
				}
			}
			
			player.setEnAtWall(enemiesAtWall);
			
			player.Update();
			for(int i = 0; i < enemyList.size(); i++) {
				if(enemyList.get(i).getIsAlive() && enemyList.get(i).getPeak() <= 0)
				{
					if(enemyList.get(i).getChanged() == false)
					{
						bg.removeActor(enemyList.get(i).getImage());
						bg.removeActor(enemyList.get(i).getShadow());
						hg.addActor(enemyList.get(i).getImage());
						hg.addActor(enemyList.get(i).getShadow());
						enemyList.get(i).setChanged(true);
					}
				}
				enemyList.get(i).Anim(delta);
			}
			
			batch.begin();
			if(curChamp != null)
			{
				curChamp.Anim(delta);
			}
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
			
			if (explodeCDTimer != 0) fg.addActor(explodeCD); else fg.removeActor(explodeCD);
			if (freezeCDTimer != 0) fg.addActor(freezeCD); else fg.removeActor(freezeCD);
			if (healthCDTimer != 0) fg.addActor(healthCD); else fg.removeActor(healthCD);
			if (godCDTimer != 0) fg.addActor(godCD); else fg.removeActor(godCD);
			if (curChamp != null) fg.addActor(championCD); else fg.removeActor(championCD);
			
			batch.end();
		} else if(gameStatus == GAME_PAUSED) {
			pauseStage.act(Gdx.graphics.getDeltaTime());
			batch.begin();
			pauseStage.draw();
			batch.end();
		} else if(gameStatus == POWERUP_PAUSE){
			powerupStage.act(Gdx.graphics.getDeltaTime());
			batch.begin();
			powerupStage.draw();
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
	
	public Entity PriorityTarget()
	{
		int priority;
		int lPriority = -1;
		Entity lEnt = null;
		boolean BIGDUDE = false;

		for(int i = 0; i < enemyList.size(); i++) {
			if(enemyList.get(i).getIsAlive() && enemyList.get(i).getSplatting() == 0 && enemyList.get(i).getChanged()) {
				if(!enemyList.get(i).onGround())
				{
					priority = (int) Math.sqrt(sqr(enemyList.get(i).getGroundPosition().x - curChamp.getGroundPosition().x) + sqr(enemyList.get(i).getLastPos().y - curChamp.getGroundPosition().y));
					priority += enemyList.get(i).getLastPos().y;
				}
				else
				{
					priority = (int) Math.sqrt(sqr(enemyList.get(i).getGroundPosition().x - curChamp.getGroundPosition().x) + sqr(enemyList.get(i).getGroundPosition().y - curChamp.getGroundPosition().y));
					priority += enemyList.get(i).getGroundPosition().y;
				}

				if(lPriority == -1)
				{
					lPriority = priority;
					lEnt = enemyList.get(i);
				}
				else
				{
					if(enemyList.get(i).getName() != "BigDude")
					{
						if(priority < lPriority && BIGDUDE == false)
						{
							lPriority = priority;
							lEnt = enemyList.get(i);
						}
					}
					else
					{
						if(BIGDUDE == false)
						{
							lPriority = priority;
							lEnt = enemyList.get(i);
							BIGDUDE = true;
						}
						else
						{
							if(priority < lPriority)
							{
								lPriority = priority;
								lEnt = enemyList.get(i);
							}
						}
					}
				}
			}
		}

		return lEnt;
	}
	
	public Entity PriorityHeal(Entity curPriest)
	{
		int priority;
		int lPriority = -1;
		Entity lEnt = null;

		for(int i = 0; i < enemyList.size(); i++) {
			if(enemyList.get(i).getIsAlive() && enemyList.get(i) != curPriest && enemyList.get(i).getHealthCurrent() < enemyList.get(i).getHealthMax() && enemyList.get(i).getSplatting() == 0 && enemyList.get(i).getChanged()) {
				if(!enemyList.get(i).onGround())
				{
					priority = (int) Math.sqrt(sqr(enemyList.get(i).getGroundPosition().x - curPriest.getGroundPosition().x) + sqr(enemyList.get(i).getLastPos().y - curPriest.getGroundPosition().y));
					priority += (enemyList.get(i).getHealthCurrent() * 4);
				}
				else
				{
					priority = (int) Math.sqrt(sqr(enemyList.get(i).getGroundPosition().x - curPriest.getGroundPosition().x) + sqr(enemyList.get(i).getGroundPosition().y - curPriest.getGroundPosition().y));
					priority += (enemyList.get(i).getHealthCurrent() * 4);
				}

				if(lPriority == -1)
				{
					lPriority = priority;
					lEnt = enemyList.get(i);
				}
				else
				{
					if(priority < lPriority)
					{
						lPriority = priority;
						lEnt = enemyList.get(i);
					}
				}
			}
		}

		return lEnt;
	}
	
	public float sqr(float num)
	{
		return num*num;
	}

	@Override
	public void resize(int width, int height) {
		gd = new GestureDetection(this);
		
		stage = new Stage(width, height, true);
		stage.clear();
		
		pauseStage = new Stage(width, height, true);
		pauseStage.clear();
		
		powerupStage = new Stage(width, height, true);
		powerupStage.clear();
		
		deathStage = new Stage(width, height, true);
		deathStage.clear();
		
		bg = new Group();
		hg = new Group();
		fg = new Group();
		
		if(gameStatus == GAME_RUNNING) {
			im = new InputMultiplexer(new GestureDetector(gd) , stage);
			Gdx.input.setInputProcessor(im);
		} else if(gameStatus == GAME_PAUSED){
			Gdx.input.setInputProcessor(pauseStage);
		} else if(gameStatus == POWERUP_PAUSE){
			Gdx.input.setInputProcessor(powerupStage);
		} else if(gameStatus == GAME_LOST){
			Gdx.input.setInputProcessor(deathStage);
		} else{
			System.out.println("How did you do this?");
		}
		
		stage.addActor(bg);
		stage.addActor(hg);
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
		
		gameHills = new Texture("data/gameHills.png");
		Image hillsImage = new Image(gameHills);
		hillsImage.setZIndex(100000);
		hillsImage.setWidth(Gdx.graphics.getWidth());
		hillsImage.setHeight(Gdx.graphics.getHeight());
		hg.addActor(hillsImage);
		
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
		
		powerupButton = new TextButton("Powerups", buttonStyle);
		powerupButton.setWidth(Gdx.graphics.getWidth() / 6);
		powerupButton.setHeight(Gdx.graphics.getHeight() / 12);
		powerupButton.setX(Gdx.graphics.getWidth() * 0.01f);
		powerupButton.setY(Gdx.graphics.getHeight() * 0.01f);
		fg.addActor(powerupButton);
		
		//Explosion button, kills everyone!
		explodePow = new Button(skin.getDrawable("ExplosionPowerupButtonLight"), skin.getDrawable("ExplosionPowerupButtonDark"));
		explodePow.setWidth(Gdx.graphics.getWidth() / 6);
		explodePow.setHeight(Gdx.graphics.getWidth() / 6);
		explodePow.setX((Gdx.graphics.getWidth() * 0.2f) - (explodePow.getWidth() / 2));
		explodePow.setY((Gdx.graphics.getHeight() * 0.5f) - (explodePow.getHeight() / 2));
		powerupStage.addActor(explodePow);
		
		//Explosion button cool down
		explodeCD = new Button(skin.getDrawable("ExplosionPowerupButtonCD"));
		explodeCD.setWidth(Gdx.graphics.getWidth() / 16);
		explodeCD.setHeight(Gdx.graphics.getWidth() / 16);
		explodeCD.setX(Gdx.graphics.getWidth() * 0.005f);
		explodeCD.setY(Gdx.graphics.getHeight() * 0.8f);
			
		//Freeze powerup button
		freezePow = new Button(skin.getDrawable("IcePowerupButtonLight"), skin.getDrawable("IcePowerupButtonDark"));
		freezePow.setWidth(Gdx.graphics.getWidth() / 6);
		freezePow.setHeight(Gdx.graphics.getWidth() / 6);
		freezePow.setX((Gdx.graphics.getWidth() * 0.4f) - (freezePow.getWidth() / 2));
		freezePow.setY((Gdx.graphics.getHeight() * 0.5f) - (freezePow.getHeight() / 2));
		powerupStage.addActor(freezePow);
		
		//Freeze powerup cooldown button
		freezeCD = new Button(skin.getDrawable("IcePowerupButtonCD"));
		freezeCD.setWidth(Gdx.graphics.getWidth() / 16);
		freezeCD.setHeight(Gdx.graphics.getWidth() / 16);
		freezeCD.setX(Gdx.graphics.getWidth() * 0.005f);
		freezeCD.setY(Gdx.graphics.getHeight() * 0.65f);
		
		//Health button, restores certain percent of castle health
		healthPow = new Button(skin.getDrawable("HealPowerupButtonLight"), skin.getDrawable("HealPowerupButtonDark"));
		healthPow.setWidth(Gdx.graphics.getWidth() / 6);
		healthPow.setHeight(Gdx.graphics.getWidth() / 6);
		healthPow.setX((Gdx.graphics.getWidth() * 0.6f) - (healthPow.getWidth() / 2));
		healthPow.setY((Gdx.graphics.getHeight() * 0.5f) - (healthPow.getHeight() / 2));
		powerupStage.addActor(healthPow);
		
		//Health cool down button
		healthCD = new Button(skin.getDrawable("HealPowerupButtonCD"));
		healthCD.setWidth(Gdx.graphics.getWidth() / 16);
		healthCD.setHeight(Gdx.graphics.getWidth() / 16);
		healthCD.setX(Gdx.graphics.getWidth() * 0.005f);
		healthCD.setY(Gdx.graphics.getHeight() * 0.50f);
		
		//Finger of God button, tap to kill!
		godPow = new Button(skin.getDrawable("GodPowerupButtonLight"), skin.getDrawable("GodPowerupButtonDark"));
		godPow.setWidth(Gdx.graphics.getWidth() / 6);
		godPow.setHeight(Gdx.graphics.getWidth() / 6);
		godPow.setX((Gdx.graphics.getWidth() * 0.8f) - (godPow.getWidth() / 2));
		godPow.setY((Gdx.graphics.getHeight() * 0.5f) - (godPow.getHeight() / 2));
		powerupStage.addActor(godPow);
		
		//Finger of God cooldown button
		godCD = new Button(skin.getDrawable("GodPowerupButtonCD"));
		godCD.setWidth(Gdx.graphics.getWidth() / 16);
		godCD.setHeight(Gdx.graphics.getWidth() / 16);
		godCD.setX(Gdx.graphics.getWidth() * 0.005f);
		godCD.setY(Gdx.graphics.getHeight() * 0.35f);
		
		//Horn of Champion Powerup
		championPow = new Button(skin.getDrawable("HornPowerupButtonLight"), skin.getDrawable("HornPowerupButtonDark"));
		championPow.setWidth(Gdx.graphics.getWidth() / 6);
		championPow.setHeight(Gdx.graphics.getWidth() / 6);
		championPow.setX((Gdx.graphics.getWidth() * 0.5f) - (godPow.getWidth() / 2));
		championPow.setY((Gdx.graphics.getHeight() * 0.25f) - (godPow.getHeight() / 2));
		powerupStage.addActor(championPow);
		
		//Horn of Champion cooldown button
		championCD = new Button(skin.getDrawable("HornPowerupButtonCD"));
		championCD.setWidth(Gdx.graphics.getWidth() / 16);
		championCD.setHeight(Gdx.graphics.getWidth() / 16);
		championCD.setX(Gdx.graphics.getWidth() * 0.005f);
		championCD.setY(Gdx.graphics.getHeight() * 0.2f);
			
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
			if(enemyList.get(i).getPeak() > 0)
			{
				bg.addActor(enemyList.get(i).getShadow());
				bg.addActor(enemyList.get(i).getImage());
			}
			else
			{
				hg.addActor(enemyList.get(i).getShadow());
				hg.addActor(enemyList.get(i).getImage());
			}
		}
		
		if(curChamp != null)
		{
			hg.addActor(curChamp.getImage());
			hg.addActor(curChamp.getShadow());
		}
		
		stage.addAction(Actions.sequence(Actions.alpha(0), Actions.fadeIn(1)));
		pauseStage.addAction(Actions.sequence(Actions.alpha(0), Actions.fadeIn(1)));
		
		pauseButton.addListener(new InputListener(){
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				return true;
			}
			public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
				
				stage.addAction(Actions.sequence(Actions.fadeOut(.3f), Actions.run(new Runnable() {
					@Override
					public void run() {
						pauseGame();
					}
				})));
			}
		});
		
		powerupButton.addListener(new InputListener(){
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				return true;
			}
			public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
				
				stage.addAction(Actions.sequence(Actions.fadeOut(.3f), Actions.run(new Runnable() {
					@Override
					public void run() {
						powerupPause();
					}
				})));
			}
		});
		
		freezePow.addListener(new InputListener(){
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				return true;
			}
			public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
				resumeGame();
				if (freezeCDTimer == 0) {
					freezeCDTimer = 15;
					fg.addActor(freezeCD);
					for(int i = 0; i < enemyList.size(); i++){
						enemyList.get(i).freeze();
					}
					freeze = 1;
				}
			}
		});
		
		explodePow.addListener(new InputListener(){
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				return true;
			}
			public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
				resumeGame();
				if (explodeCDTimer == 0) {
					explodeCDTimer = 30;
					fg.addActor(explodeCD);
					for(int i = 0; i < enemyList.size(); i++){
						if(enemyList.get(i).getChanged() == true && enemyList.get(i).getIsAlive() && enemyList.get(i).getSplatting() == 0)
						{
							Random generator = new Random();
							int test = generator.nextInt(10) - 5;
							Vector2 explode = new Vector2(test, 20);
							enemyList.get(i).pickedUp();
							enemyList.get(i).Released(explode);
						}
					}
				}
			}
		});
		
		healthPow.addListener(new InputListener(){
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				return true;
			}
			public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
				resumeGame();
				if (healthCDTimer == 0) {
					healthCDTimer = 20;
					fg.addActor(healthCD);
					float newHealth = player.getHealthCurrent() + healthRegen;
				
					if(newHealth > player.getHealthMax()) {
						player.setHealthCurrent(player.getHealthMax());
					}
					else {
						player.setHealthCurrent(newHealth);
					}	
				}	 
			}
		});
		
		godPow.addListener(new InputListener(){
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				return true;
			}
			public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
				resumeGame();
				if (godCDTimer == 0) {
					godCDTimer = 20;
					fg.addActor(godCD);
					god = true;	
				}
			}
		});
		
		championPow.addListener(new InputListener(){
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				return true;
			}
			public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
				resumeGame();
				curChamp = new Champion("champ", 45, anims, Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY());
				hg.addActor(curChamp.getImage());
				hg.addActor(curChamp.getShadow());
			}
		});
		
		resumeButton.addListener(new InputListener(){
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				return true;
			}
			public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
				
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
				return true;
			}
			public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
				
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
				return true;
			}
			public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
				
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
	
	public void powerupPause() {
		gameStatus = POWERUP_PAUSE;
		Gdx.input.setInputProcessor(powerupStage);
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

	//If the freeze powerup is enabled, spawn will not be called
	public void freezeCheck() {

		if (freeze == 0) {
			
		} else if ((freeze == 1) && (freezeTime != 0) ) {
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
	
	public void coolDownCheck() {
		if (freezeCDTimer != 0) {
			freezeCDTimer--;
			if (freezeCDTimer == 0) {
				fg.removeActor(freezeCD);
			}
		}
		
		if (godCDTimer != 0) {
			godCDTimer--;
			if (godCDTimer == 0) {
				fg.removeActor(godCD);
			}
		}
		
		if (explodeCDTimer != 0) {
			explodeCDTimer--;
			if (explodeCDTimer == 0) {
				fg.removeActor(explodeCD);
			}
		}
		
		if (healthCDTimer != 0) {
			healthCDTimer--;
			if (healthCDTimer == 0) {
				fg.removeActor(healthCD);
			}
		}
	}
	
	private void generateSpawnLocations() {
		for(int x = 0; x <= 100; x++) {
			
			double per_y = 22.07*Math.exp(-Math.pow(((x-28.38)/13.46), 2))
					+ 57.74*Math.exp(-Math.pow(((x-107.2)/82.91), 2))
					+ 2.657*Math.exp(-Math.pow(((x-15.37)/7.31), 2))
					+ 42.57*Math.exp(-Math.pow(((x-2.339)/22.12), 2))
					+ 4.368*Math.exp(-Math.pow(((x-48.16)/5.225), 2))
					+ 8.194*Math.exp(-Math.pow(((x-51.9)/21.79), 2))
					+ 9.983*Math.exp(-Math.pow(((x-41.27)/8.756), 2))
					+ 1.574*Math.exp(-Math.pow(((x-57.25)/5.172), 2));
			 
			 spawnLocation[x] = per_y;
		}
		
	}

	public void spawn() {
        if(freeze == 0) {
        	Random generator = new Random();
	           
        	int x = generator.nextInt((int)(Gdx.graphics.getWidth()*4/5) + 1) + (int)(Gdx.graphics.getWidth()/10);
        	int y = (int)((spawnLocation[(int)(((float)x / (float)Gdx.graphics.getWidth()) * 100)] / 100) * Gdx.graphics.getHeight());
	            
	        Entity newEnemy = null;
	            
	        int yourFate = generator.nextInt(100) + 1;
	        if(yourFate > 0 && yourFate < 6) {
	        	newEnemy = new BigDude("BigDude", 600, anims, x, y);
	        } else if(yourFate > 5 && yourFate < 11) {
	        	newEnemy = new Priest("Priest", 100, anims, x, y);
	        } else if(yourFate > 10 && yourFate < 21) {
	        	newEnemy = new DemoDude("Demo", 100, anims, x, y);
	        } else {
	        	newEnemy = new StickDude("Basic", 100, anims, x, y);
	        }
	       
            bg.addActor(newEnemy.getShadow());
            bg.addActor(newEnemy.getImage());
            
            newEnemy.setPosition(newEnemy.getPosition().x, Math.round(newEnemy.getPosition().y - (.05 * Gdx.graphics.getHeight())));
	        enemyList.add(newEnemy);
            newEnemy = null;
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
	
	public boolean getGrabbed(){
		return enemyGrabbed;
	}
	
	public void setGrabbed(Boolean grabbed){
		enemyGrabbed = grabbed;
	}
	
	public int getGrabbedNumber(){
		return grabbedNumber;
	}

	public void setGrabbedNumber(int grabbedNumber){
		this.grabbedNumber = grabbedNumber;
	}
	
	public Vector<Entity> getEnemyList(){
		return enemyList;
	}
	
	public Boolean getGodStatus(){
		return god;
	}

}
