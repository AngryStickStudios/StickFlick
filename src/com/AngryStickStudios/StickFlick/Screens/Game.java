package com.AngryStickStudios.StickFlick.Screens;

import java.util.Hashtable;
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
import com.badlogic.gdx.math.MathUtils;
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
import com.AngryStickStudios.StickFlick.Controller.Button2;
import com.AngryStickStudios.StickFlick.Controller.GestureDetection;
import com.AngryStickStudios.StickFlick.Controller.AnimationLoader;
import com.AngryStickStudios.StickFlick.Controller.Image2;
import com.AngryStickStudios.StickFlick.Controller.TextButton2;
import com.AngryStickStudios.StickFlick.Entities.Archer;
import com.AngryStickStudios.StickFlick.Entities.ArcherDude;
import com.AngryStickStudios.StickFlick.Entities.Arrow;
import com.AngryStickStudios.StickFlick.Entities.BigDude;
import com.AngryStickStudios.StickFlick.Entities.BoilingOil;
import com.AngryStickStudios.StickFlick.Entities.Champion;
import com.AngryStickStudios.StickFlick.Entities.DemoDude;
import com.AngryStickStudios.StickFlick.Entities.Entity;
import com.AngryStickStudios.StickFlick.Entities.Mage;
import com.AngryStickStudios.StickFlick.Entities.Missile;
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

	boolean developerMode = false;
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
	private Entity grabbed;
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
	private boolean lefty = prefs.getBoolean("lefty", false);
    
	StickFlick game;
	SpriteBatch batch;
	Texture gameBackground, castleOnly, gameHills;
	Stage stage, pauseStage, powerupStage, deathStage;
	Group bg, hg, fg;
	Skin skin;
	Sound buttonClick;
	BitmapFont white;
	GestureDetection gd;
	TextureAtlas atlas;
	InputMultiplexer im;
	TextButton pauseButton, powerupButton, resumeButton, powerupResumeButton, mainMenuButton, mainMenuButton2;
	LabelStyle labelStyle, labelStyleCoinage, labelStyleDeath, labelStyleScore; 
	Label timer, coinageDisplay, deathMessage, finalScore;
	Vector<Entity> enemyList;
	Vector<Entity> projlist;
	Vector<Entity> friendlylist;
	Champion curChamp;
	BoilingOil boilingOil;
	Player player;
	OrthographicCamera camera;
	ShapeRenderer sp;
	Button freezePow, explodePow, godPow, championPow, boilingOilPow, serfsPow;
	Image explodeCD, freezeCD, godCD, serfsCD, championCD, boilingOilCD;
	Timer spawnTimerOuter, spawnTimerInner, freezeTimer, godTimer, coolDownTimer;
	double timeSpawn, timeEquation, timeSetSpawn = 0;
	final double DEATHTIME = .25;
	boolean justUnfrozen = false, priestButtonDown = false;
	AnimationLoader anims;
	int screenWidth, screenHeight;
	boolean bombUsed = false, blizzardUsed = false, fogUsed = false, hocUsed = false, serfsUsed = false;
	
	
	public Game(StickFlick game){
		this.game = game;
		anims = game.anims;
		atlas = new TextureAtlas("data/Textures.atlas");
		
		screenWidth = Gdx.graphics.getWidth();
		screenHeight = Gdx.graphics.getHeight();
		
		generateSpawnLocations();
		
		spawnTimerOuter = new Timer();
		spawnTimerInner = new Timer();
		freezeTimer = new Timer();
		godTimer = new Timer();
		coolDownTimer = new Timer();
		
		
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
		projlist = new Vector<Entity>();
		friendlylist = new Vector<Entity>();
	
		/* Health initialization */
		camera= new OrthographicCamera(screenWidth, screenHeight);
	    camera.setToOrtho(true, screenWidth, screenHeight);
	    camera.update();
	    sp = new ShapeRenderer();
	    
	    if(prefs.getBoolean("mages") || developerMode == true)
		{
			prefs.putBoolean("mages", false);
			
			Mage m1 = new Mage("mage", 100, anims, (int) Math.round(screenWidth * .2), (int) Math.round(screenHeight * .2));
	    	Mage m2 = new Mage("mage", 100, anims, (int) Math.round(screenWidth * .75), (int) Math.round(screenHeight * .2));
	    	
	    	friendlylist.add(m1);
	    	friendlylist.add(m2);
		}
		
		if(prefs.getBoolean("archers") || developerMode == true)
		{
			prefs.putBoolean("archers", false);
			
			Archer a1 = new Archer("archer", 100, anims, (int) Math.round(screenWidth * .3), (int) Math.round(screenHeight * .2));
	    	Archer a2 = new Archer("archer", 100, anims, (int) Math.round(screenWidth * .65), (int) Math.round(screenHeight * .2));
	    	
	    	
	    	
	    	friendlylist.add(a1);
	    	friendlylist.add(a2);
		}
	}
	
	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		
		if(player.getIsAlive() == false){
			if(bombUsed == true) prefs.putBoolean("bomb", false);
			if(blizzardUsed == true) prefs.putBoolean("blizzard", false);
			if(fogUsed == true) prefs.putBoolean("fingerOfGod", false);
			if(hocUsed == true) prefs.putBoolean("hornOfChamp", false);
			if(serfsUsed == true) prefs.putBoolean("serfs", false);
			
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
			
			for(int i = 0; i < projlist.size(); i++) {
				if(projlist.get(i).getIsAlive())
				{
					projlist.get(i).Update(delta);
				}
				else
				{
					hg.removeActor(projlist.get(i).getImage());
					projlist.remove(i);
				}
			}
			
			for(int i = 0; i < friendlylist.size(); i++) {
				if(friendlylist.get(i).getIsAlive())
				{
					friendlylist.get(i).Update(delta);
					
					if(enemyList.size() > 0 && (friendlylist.get(i).getTarget() == null || friendlylist.get(i).getTarget().getIsAlive() == false))
					{
						friendlylist.get(i).setTarget(PriorityTarget2(friendlylist.get(i)));
					}
					
					String projFired = friendlylist.get(i).getProjFired();
					if(projFired != "null")
					{
						if(projFired == "arrow")
						{
							Arrow arr = new Arrow("arrow", 100, anims, friendlylist.get(i).getPosition().x, friendlylist.get(i).getPosition().y, friendlylist.get(i).getTarget());
							projlist.add(arr);
							hg.addActor(arr.getImage());
						}
						
						if(projFired == "spell")
						{
							Missile arr = new Missile("spell", 100, anims, friendlylist.get(i).getPosition().x, friendlylist.get(i).getPosition().y, friendlylist.get(i).getTarget());
							projlist.add(arr);
							hg.addActor(arr.getImage());
						}
					}
				}
				else
				{
					fg.removeActor(friendlylist.get(i).getImage());
					friendlylist.remove(i);
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
				curChamp = new Champion("champ", 45, anims, Gdx.input.getX(), screenHeight - Gdx.input.getY());
				hg.addActor(curChamp.getImage());
				hg.addActor(curChamp.getShadow());
			}

			int enemiesAtWall = 0;
			
			for(int i = 0; i < enemyList.size(); i++){
				
				if(enemyList.get(i).getImage().getY() < screenHeight * 0.11f){
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
			
			for(int i = 0; i < projlist.size(); i++) {
				projlist.get(i).Anim(delta);
			}
			
			for(int i = 0; i < friendlylist.size(); i++) {
				friendlylist.get(i).Anim(delta);
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
				
			float healthBarSize = screenWidth/2 * (player.getHealthCurrent()/player.getHealthMax());
				
			sp.rect(screenWidth/4,screenHeight*.025f, healthBarSize, screenHeight/24);
			sp.end();
			batch.begin();
			
			if(enemyGrabbed && !Gdx.input.isTouched())
			{
				enemyGrabbed = false;
				grabbed.Released(new Vector2(0,0));
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
			if (healthCDTimer != 0) fg.addActor(serfsCD); else fg.removeActor(serfsCD);
			if (godCDTimer != 0) fg.addActor(godCD); else fg.removeActor(godCD);
			if (curChamp != null) fg.addActor(championCD); else fg.removeActor(championCD);
			
			batch.end();
		} else if(gameStatus == GAME_PAUSED) {
			pauseStage.act(Gdx.graphics.getDeltaTime());
			batch.begin();
			stage.draw();
			pauseStage.draw();
			batch.end();
		} else if(gameStatus == POWERUP_PAUSE){
			powerupStage.act(Gdx.graphics.getDeltaTime());
			batch.begin();
			stage.draw();
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
	
	public boolean Targeted(Entity enemy)
	{
		if(enemy.getName() == "BigDude")
		{
			return false;
		}
		
		for(int i = 0; i < friendlylist.size(); i++)
		{
			if(friendlylist.get(i).getIsAlive())
			{
				if(friendlylist.get(i).getTarget() == enemy)
				{
					return true;
				}
			}
		}
		
		return false;
	}
	
	public Entity PriorityTarget2(Entity friend)
	{
		int priority;
		int lPriority = -1;
		Entity lEnt = null;

		for(int i = 0; i < enemyList.size(); i++) {
			if(enemyList.get(i).getIsAlive() && enemyList.get(i).getSplatting() == 0 && enemyList.get(i).getChanged() && !Targeted(enemyList.get(i))) {
				
				priority = (int) Math.sqrt(sqr(enemyList.get(i).getPosition().x - friend.getPosition().x) + sqr(enemyList.get(i).getPosition().y - friend.getPosition().y));
				priority += enemyList.get(i).getGroundPosition().y;

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
		
		if(MathUtils.randomBoolean())
		{
			return lEnt;
		}
		else
		{
			return null;
		}
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
		
		screenWidth = Gdx.graphics.getWidth();
		screenHeight = Gdx.graphics.getHeight();
		
		stage.addActor(bg);
		stage.addActor(hg);
		stage.addActor(fg);
		
		TextButtonStyle buttonStyle = new TextButtonStyle();
		buttonStyle.up = skin.getDrawable("LightButton");
		buttonStyle.down = skin.getDrawable("DarkButton");
		buttonStyle.font = white;
		
		gameBackground = anims.getTex("gameBG");
		Image backgroundImage = new Image(gameBackground);
		backgroundImage.setZIndex(100000);
		backgroundImage.setWidth(screenWidth);
		backgroundImage.setHeight(screenHeight);
		bg.addActor(backgroundImage);
		
		gameHills = anims.getTex("gameHills");
		Image hillsImage = new Image(gameHills);
		hillsImage.setZIndex(100000);
		hillsImage.setWidth(screenWidth);
		hillsImage.setHeight(screenHeight);
		hg.addActor(hillsImage);
		
		castleOnly = anims.getTex("gameCastle");
		Image castleImage = new Image(castleOnly);
		castleImage.setWidth(screenWidth);
		castleImage.setHeight(screenHeight);
		fg.addActor(castleImage);
		
		/***************************************
		* GAME BUTTONS
		***************************************/
		float position;
		
		//PAUSE BUTTON
		if (!lefty) position = 0.80f; else position = 0.01f;
		pauseButton = new TextButton2("Pause", buttonStyle, screenWidth * position, screenHeight * 0.90f, screenWidth * 0.17f, screenHeight * 0.09f);
		fg.addActor(pauseButton);
		
		//POWERUPS BUTTON
		if (!lefty) position = 0.01f; else position = 0.825f;
		powerupButton = new TextButton2("Powerups", buttonStyle, screenWidth * position, screenHeight * 0.0125f, screenWidth * 0.17f, screenHeight * 0.09f);
		fg.addActor(powerupButton);
		
		//EXPLOSION BUTTON
		explodePow = new Button2(skin.getDrawable("ExplosionPowerupButtonLight"), skin.getDrawable("ExplosionPowerupButtonDark"), screenWidth * 0.1f, screenHeight * 0.68f, screenWidth * 0.17f, screenWidth * 0.17f);
		if(prefs.getBoolean("bomb") || developerMode == true){
			powerupStage.addActor(explodePow);
		}
		explodeCD = new Image2(skin.getDrawable("ExplosionPowerupButtonCD"), screenWidth * 0.005f, screenHeight * 0.83f, screenWidth * 0.0625f, screenWidth * 0.0625f);
		
		//FREEZE BUTTON
		freezePow = new Button2(skin.getDrawable("IcePowerupButtonLight"), skin.getDrawable("IcePowerupButtonDark"), screenWidth * 0.4f, screenHeight * 0.68f, screenWidth * 0.17f, screenWidth * 0.17f);
		if(prefs.getBoolean("blizzard") || developerMode == true){
			powerupStage.addActor(freezePow);
		}
		freezeCD = new Image2(skin.getDrawable("IcePowerupButtonCD"), screenWidth * 0.005f, screenHeight * 0.7f, screenWidth * 0.0625f, screenWidth * 0.0625f);
		
		//FINGER OF GOD BUTTON
		godPow = new Button2(skin.getDrawable("GodPowerupButtonLight"), skin.getDrawable("GodPowerupButtonDark"), screenWidth * 0.7f, screenHeight * 0.68f, screenWidth * 0.17f, screenWidth * 0.17f);
		if(prefs.getBoolean("fingerOfGod") || developerMode == true){
			powerupStage.addActor(godPow);
		}
		godCD = new Image2(skin.getDrawable("GodPowerupButtonCD"), screenWidth * 0.005f, screenHeight * 0.57f, screenWidth * 0.0625f, screenWidth * 0.0625f);
		
		//HORN OF CHAMPION POWERUP BUTTON
		championPow = new Button2(skin.getDrawable("HornPowerupButtonLight"), skin.getDrawable("HornPowerupButtonDark"), screenWidth * 0.1f, screenHeight * 0.35f, screenWidth * 0.17f, screenWidth * 0.17f);
		if(prefs.getBoolean("hornOfChamp") || developerMode == true){
			powerupStage.addActor(championPow);
		}
		championCD = new Image2(skin.getDrawable("HornPowerupButtonCD"), screenWidth * 0.005f, screenHeight * 0.44f, screenWidth * 0.0625f, screenWidth * 0.0625f);
		
		//SERFS BUTTON
		serfsPow = new Button2(skin.getDrawable("HealPowerupButtonLight"), skin.getDrawable("HealPowerupButtonDark"), screenWidth * 0.4f, screenHeight * 0.35f, screenWidth * 0.17f, screenWidth * 0.17f);
		if(prefs.getBoolean("serfs") || developerMode == true){
			powerupStage.addActor(serfsPow);
		}
		serfsCD = new Image2(skin.getDrawable("HealPowerupButtonCD"), screenWidth * 0.005f, screenHeight * 0.31f, screenWidth * 0.0625f, screenWidth * 0.0625f);
		
		//BOILING OIL BUTTON
		boilingOilPow = new Button2(skin.getDrawable("BoilingOilPowerupButtonLight"), skin.getDrawable("BoilingOilPowerupButtonDark"), screenWidth * 0.7f, screenHeight * 0.35f, screenWidth * 0.17f, screenWidth * 0.17f);
		powerupStage.addActor(boilingOilPow);
		boilingOilCD = new Image2(skin.getDrawable("BoilingOilPowerupButtonCD"), screenWidth * 0.005f, screenHeight * 0.18f, screenWidth * 0.0625f, screenWidth * 0.0625f);
		
		//POWERUP SCREEN RESUME BUTTON
		powerupResumeButton = new TextButton2("Resume", buttonStyle, screenWidth * 0.4f, screenHeight * 0.15f, screenWidth * 0.17f, screenHeight * 0.09f);
		powerupStage.addActor(powerupResumeButton);
		
		//PAUSE SCREEN RESUME BUTTON
		resumeButton = new TextButton2("Resume", buttonStyle, screenWidth/2 - (screenWidth * 0.17f) / 2, screenHeight/2 - (screenHeight * 0.09f) / 2, screenWidth * 0.17f, screenHeight * 0.09f);
		pauseStage.addActor(resumeButton);
		
		//PAUSE SCREEN MAIN MENU BUTTON
		mainMenuButton = new TextButton2("Main Menu", buttonStyle, screenWidth/2 - (screenWidth * 0.17f) / 2, screenHeight/2 - (screenHeight * 0.09f) * 2, screenWidth * 0.17f, screenHeight * 0.09f);
		pauseStage.addActor(mainMenuButton);
		
		//DEATH MESSAGE
		labelStyleDeath = new LabelStyle(white, Color.RED);
		deathMessage = new Label("Game Over...", labelStyleDeath);
		deathMessage.setX(screenWidth / 2 - deathMessage.getWidth()/2);
		deathMessage.setY(screenHeight / 2 + deathMessage.getHeight() * 3);
		deathStage.addActor(deathMessage);
		
		//FINAL SCORE`
		labelStyleScore = new LabelStyle(white, Color.ORANGE);
		finalScore = new Label("Score: 999", labelStyleScore);
		finalScore.setX(screenWidth / 2 - finalScore.getWidth()/2);
		finalScore.setY(screenHeight / 2);
		deathStage.addActor(finalScore);
		
		//Return to main menu
		mainMenuButton2 = new TextButton2("Main Menu", buttonStyle, screenWidth/2 - (screenWidth * 0.17f) / 2, screenHeight/2 - (screenHeight * 0.09f) * 2, screenWidth * 0.17f, screenHeight * 0.09f);
		deathStage.addActor(mainMenuButton2);
		
		//TIMER
		if (!lefty) position = 0.025f; else position = 0.95f;
		labelStyle = new LabelStyle(white, Color.BLACK);
		timer = new Label(formattedTime, labelStyle);
		timer.setHeight(screenHeight / 24);
		timer.setX(screenWidth * position);
		timer.setY(screenHeight * 0.95f);
		fg.addActor(timer);

		//COINAGE
		if (!lefty) position = 0.085f; else position = 0.875f;
	    labelStyleCoinage = new LabelStyle(white, Color.ORANGE);
		coinageDisplay = new Label(String.valueOf(getCoinage()), labelStyleCoinage);
		coinageDisplay.setHeight(screenHeight / 24);
		coinageDisplay.setX(screenWidth * position);
		coinageDisplay.setY(screenHeight * 0.95f);
		stage.addActor(coinageDisplay);
		
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
		
		for(int i = 0; i < projlist.size(); i++) {
			hg.addActor(projlist.get(i).getImage());
		}
		
		for(int i = 0; i < friendlylist.size(); i++) {
			fg.addActor(friendlylist.get(i).getImage());
		}
		
		if(curChamp != null)
		{
			hg.addActor(curChamp.getImage());
			hg.addActor(curChamp.getShadow());
		}
		
		pauseStage.addAction(Actions.sequence(Actions.fadeIn(1)));
		
		
		//LISTENERS
		pauseButton.addListener(new InputListener(){
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				buttonClick.stop();
				buttonClick.play();
				return true;
			}
			public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
				
				stage.addAction(Actions.sequence(Actions.alpha(.3f, .3f), Actions.run(new Runnable() {
					@Override
					public void run() {
						pauseGame();
					}
				})));
			}
		});
		
		powerupButton.addListener(new InputListener(){
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				buttonClick.stop();
				buttonClick.play();
				return true;
			}
			public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
				
				stage.addAction(Actions.sequence(Actions.alpha(.3f, .3f), Actions.run(new Runnable() {
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
				blizzardUsed = true;
				resumeGame();
				
				if (freezeCDTimer == 0) {
					if(prefs.getBoolean("mages"))
					{
						freezeCDTimer = 52;
					}
					else
					{
						freezeCDTimer = 60;
					}
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
				bombUsed = true;
				resumeGame();
				if (explodeCDTimer == 0) {
					if(prefs.getBoolean("mages"))
					{
						explodeCDTimer = 52;
					}
					else
					{
						explodeCDTimer = 60;
					}
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
		
		serfsPow.addListener(new InputListener(){
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				return true;
			}
			public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
				serfsUsed = true;
				resumeGame();
				if (healthCDTimer == 0) {
					if(prefs.getBoolean("mages"))
					{
						healthCDTimer = 18;
					}
					else
					{
						healthCDTimer = 20;
					}
					fg.addActor(serfsCD);
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
				fogUsed = true;
				resumeGame();
				if (godCDTimer == 0) {
					if(prefs.getBoolean("mages"))
					{
						godCDTimer = 18;
					}
					else
					{
						godCDTimer = 20;
					}
					fg.addActor(godCD);
					god = true;	
				}
				
			}
		});
		
		boilingOilPow.addListener(new InputListener(){
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button){
				return true;
			}
			public void touchUp(InputEvent event, float x, float y, int pointer, int button){
				resumeGame();
				boilingOilPU();
				boilingOil = new BoilingOil(anims, screenWidth*0.45f, screenHeight*0.02f);
				fg.addActor(boilingOil.getImage());
			}
		});
		
		championPow.addListener(new InputListener(){
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				return true;
			}
			public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
				hocUsed = true;
				resumeGame();
				if(curChamp == null)
				{
					curChamp = new Champion("champ", 45, anims, Gdx.input.getX(), screenHeight - Gdx.input.getY());
					hg.addActor(curChamp.getImage());
					hg.addActor(curChamp.getShadow());
				}
			}
		});
		
		resumeButton.addListener(new InputListener(){
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				buttonClick.stop();
				buttonClick.play();
				return true;
			}
			public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
				
				pauseStage.addAction(Actions.sequence(Actions.run(new Runnable() {
					@Override
					public void run() {
						resumeGame();
					}
				})));
			}
		});
		
		powerupResumeButton.addListener(new InputListener(){
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				buttonClick.stop();
				buttonClick.play();
				return true;
			}
			public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
				resumeGame();
			}
		});
		
		mainMenuButton.addListener(new InputListener(){
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				if(bombUsed == true) prefs.putBoolean("bomb", false);
				if(blizzardUsed == true) prefs.putBoolean("blizzard", false);
				if(fogUsed == true) prefs.putBoolean("fingerOfGod", false);
				if(hocUsed == true) prefs.putBoolean("hornOfChamp", false);
				if(serfsUsed == true) prefs.putBoolean("serfs", false);
				prefs.putLong("currency", getCoinage());
				prefs.flush();
				
				buttonClick.stop();
				buttonClick.play();
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
				buttonClick.stop();
				buttonClick.play();
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

	//SHOW METHOD
	@Override
	public void show() {
		
		batch = new SpriteBatch();
		
		//atlas = new TextureAtlas("data/Textures.atlas");
		skin = new Skin();
		skin.addRegions(atlas);
		
		white = new BitmapFont(Gdx.files.internal("data/whiteFont.fnt"), false);
		buttonClick = Gdx.audio.newSound(Gdx.files.internal("data/sounds/button2.mp3"));
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
		resize(screenWidth, screenHeight);
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
		buttonClick.dispose();
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
				fg.removeActor(serfsCD);
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
        if(freeze == 0 && gameStatus == GAME_RUNNING) {
        	Random generator = new Random();
	           
        	int x = generator.nextInt((int)(screenWidth*4/5) + 1) + (int)(screenWidth/10);
        	int y = (int)((spawnLocation[(int)(((float)x / (float)screenWidth) * 100)] / 100) * screenHeight);
	            
	        Entity newEnemy = null;
	            
	        int yourFate = generator.nextInt(100) + 1;
	        if(yourFate > 0 && yourFate < 6) {
	        	newEnemy = new BigDude("BigDude", 1200, anims, x, y);
	        } else if(yourFate > 5 && yourFate < 11) {
	        	newEnemy = new Priest("Priest", 200, anims, x, y);
	        } else if(yourFate > 10 && yourFate < 21) {
	        	newEnemy = new DemoDude("Demo", 200, anims, x, y);
	        } else if(yourFate > 20 && yourFate < 26){
	        	newEnemy = new ArcherDude("Archer",200, anims, x, y);
	        }
	        else {
	        	newEnemy = new StickDude("Basic", 200, anims, x, y);
	        }
	       
            bg.addActor(newEnemy.getShadow());
            bg.addActor(newEnemy.getImage());
            
            newEnemy.setPosition(newEnemy.getPosition().x, Math.round(newEnemy.getPosition().y - (.05 * screenHeight)));
	        enemyList.add(newEnemy);
            newEnemy = null;
        }
	}
	
	/*********************************
	 * BOILING OIL
	 *********************************/
	
	public void boilingOilPU(){
		int removeEnemyAtWall = 0;
		for(int i = 0; i<enemyList.size(); i++){
			if(enemyList.get(i).getPosition().y <= screenHeight * 0.37f){
				//System.out.println("Enemy at wall TRUE");
				hg.removeActor(enemyList.get(i).getImage());
				hg.removeActor(enemyList.get(i).getShadow());
				enemyList.remove(i);
				removeEnemyAtWall++;
			}
			//System.out.println("Not at wall");
		}
		player.removeEnFromWall(removeEnemyAtWall);
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
	
	public Entity getGrabbedEnt(){
		return grabbed;
	}

	public void setGrabbedEnt(Entity gotGrabbed){
		this.grabbed = gotGrabbed;
	}
	
	public Vector<Entity> getEnemyList(){
		return enemyList;
	}
	
	public Boolean getGodStatus(){
		return god;
	}

}
