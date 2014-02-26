package com.AngryStickStudios.StickFlick.Screens;



import java.util.Vector;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.input.GestureDetector.GestureListener;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
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
import com.AngryStickStudios.StickFlick.Entities.WalkingEnemy;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.Timer.Task;
import java.util.*;

public class Game implements Screen, GestureListener {

	public static final int GAME_RUNNING = 1;
    public static final int GAME_PAUSED = 0;
    private int gameStatus = 1;
    private float timeTrack = 0;
    private int seconds = 0;
    private int minutes = 0;
    private String formattedTime = "0:00";
    private boolean enemyGrabbed = false;
    private int grabbedNumber = -1;
    
	StickFlick game;
	SpriteBatch batch;
	Texture gameBackground;
	Stage stage, pauseStage;
	Skin skin;
	BitmapFont white;
	GestureDetector gd;
	TextureAtlas atlas;
	InputMultiplexer im;
	TextButton pauseButton, resumeButton, mainMenuButton;
	LabelStyle labelStyle;
	Label timer;
	Vector<WalkingEnemy> enemyList;
	Timer spawnTimer;
	double sumSpawn = 0;
	double timeSpawn = 0;
	
	
	public Game(StickFlick game){
		this.game = game;
		
		enemyList = new Vector<WalkingEnemy>();
		spawnTimer.schedule(new Task() {
			@Override
			public void run() {
				spawn();
			}
		}, 1, 1);
	}
	
	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
				
		if (gameStatus == 1) {
			stage.act(Gdx.graphics.getDeltaTime());
			for(int i = 0; i < enemyList.size(); i++) {
				enemyList.get(i).Update(delta);
			}
			batch.begin();
			stage.draw();	
			
			if(enemyGrabbed && !Gdx.input.isTouched())
			{
				enemyGrabbed = false;
				enemyList.get(grabbedNumber).Released(new Vector2(0,0));
			}
			
			timeTrack += Gdx.graphics.getDeltaTime();
			if (timeTrack >= 1f) {
				
				timeTrack = timeTrack - 1f;
				seconds++;
				
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
			}
			
			batch.end();
		}
		
		else {
			pauseStage.act(Gdx.graphics.getDeltaTime());
			batch.begin();
			pauseStage.draw();
			batch.end();
		} 	
	}
	

	@Override
	public void resize(int width, int height) {
		stage = new Stage(width, height, true);
		stage.clear();
		
		pauseStage = new Stage(width, height, true);
		pauseStage.clear();
		
		if(gameStatus == 1) {
			im = new InputMultiplexer(new GestureDetector(this), stage);
			Gdx.input.setInputProcessor(im);
		}
		
		else {
			Gdx.input.setInputProcessor(pauseStage);
		}
		
		TextButtonStyle buttonStyle = new TextButtonStyle();
		buttonStyle.up = skin.getDrawable("LightButton");
		buttonStyle.down = skin.getDrawable("DarkButton");
		buttonStyle.font = white;
		
		Texture gameBackground = new Texture("data/gameBackground.png");
		Image backgroundImage = new Image(gameBackground);
		backgroundImage.setWidth(Gdx.graphics.getWidth());
		backgroundImage.setHeight(Gdx.graphics.getHeight());
		stage.addActor(backgroundImage);
		
		pauseButton = new TextButton("Pause", buttonStyle);
		pauseButton.setWidth(Gdx.graphics.getWidth() / 6);
		pauseButton.setHeight(Gdx.graphics.getHeight() / 12);
		pauseButton.setX(Gdx.graphics.getWidth() * 0.80f);
		pauseButton.setY(Gdx.graphics.getHeight() * 0.90f);
		stage.addActor(pauseButton);
		
		labelStyle = new LabelStyle(white, Color.BLACK);
		timer = new Label(formattedTime, labelStyle);
		timer.setHeight(Gdx.graphics.getHeight() / 24);
		timer.setX(Gdx.graphics.getWidth() * 0.025f);
		timer.setY(Gdx.graphics.getHeight() * 0.95f);
		stage.addActor(timer);
	
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
		
	}

	/*******************
	* Spawning
	*******************/
	public void spawn() {
		Random generator = new Random();
		int x;
		int rate;
		
		timeSpawn++;
		double minuteSpawn = (timeSpawn)/60;
		
		if(timeSpawn <= 30) {
			sumSpawn += .5;
		}
		else if(timeSpawn > 30 && minuteSpawn <= 1) {
			sumSpawn += 2*minuteSpawn;
		}
		else if(minuteSpawn > 1 && minuteSpawn <= 3) {
			sumSpawn += Math.pow(2, minuteSpawn);
		}
		else if(minuteSpawn > 3) {
			sumSpawn += 2*(minuteSpawn - 3) + 8;
		}
		
		rate = (int) Math.floor(sumSpawn);
		sumSpawn -= rate;
		
		for(int i = 0; i < rate; i++) {
			x = generator.nextInt((int)(Gdx.graphics.getWidth()*4/5)) + (int)(Gdx.graphics.getWidth()/10);
			enemyList.add(new WalkingEnemy("basic", 100, x, (int) (Gdx.graphics.getHeight() / 1.75)));		
			stage.addActor(enemyList.get((enemyList.size())-1).getShadow());
			stage.addActor(enemyList.get((enemyList.size())-1).getImage());
		}
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
				}
			}
		}
		return false;
	}

	@Override
	public boolean tap(float x, float y, int count, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean longPress(float x, float y) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean fling(float velocityX, float velocityY, int button) {
		if(enemyGrabbed == true)
		{
			//enemyList.get(grabbedNumber).FindDestOnWall();
			enemyGrabbed = false;
			enemyList.get(grabbedNumber).Released(new Vector2(velocityX / 100, velocityY / -100));
		}
		return false;
	}

	@Override
	public boolean pan(float x, float y, float deltaX, float deltaY) {
		// TODO Auto-generated method stub
		/*
		y = Gdx.graphics.getHeight() - y;
		
		if(enemyGrabbed == true){
			enemyList.get(grabbedNumber).setPosition(x, y);
		}
		*/
		
		
		/* AIDAN'S CODE
		WalkingEnemy closest = null;
		float closest_i = -1;
		for(int i = 0; i < enemyList.size(); i++)	// Searches through enemy list
		{
			Vector2 size = enemyList.get(i).getSize();
			Vector2 pos = enemyList.get(i).getPosition();
			if((pos.x - size.x / 2 <= x && x <= pos.x + size.x / 2) && (pos.y - size.y / 2 <= y && y < pos.y + size.y / 2)){
				
			}
			
			
			float distance = enemyList.get(i).getPosition().dst(x, Gdx.graphics.getHeight() - y);	// gets distance of mouse pointer from enemy
			if(distance <= 100)
			{
				if(closest == null)
				{
					closest = enemyList.get(i);
					closest_i = enemyList.get(i).getPosition().dst(x, y);
				}
				else
				{
					if(distance < closest_i)
					{
						closest = enemyList.get(i);
						closest_i = enemyList.get(i).getPosition().dst(x, y);
					}
				}
			}
		}
		
		if(closest != null)
		{
			closest.setPosition(x, Gdx.graphics.getHeight() - y);
			closest.FindDestOnWall();
		}
		*/
		return false;
	}

	@Override
	public boolean panStop(float x, float y, int pointer, int button) {
		// TODO Auto-generated method stub
		//enemyList.get(grabbedNumber).FindDestOnWall();
		//enemyGrabbed = false;
		return false;
	}

	@Override
	public boolean zoom(float initialDistance, float distance) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2,
			Vector2 pointer1, Vector2 pointer2) {
		// TODO Auto-generated method stub
		return false;
	}

}
