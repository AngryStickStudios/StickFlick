package com.AngryStickStudios.StickFlick.Screens;

import Controllers.GestureDetection;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.input.GestureDetector.GestureListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.AngryStickStudios.StickFlick.StickFlick;
import com.AngryStickStudios.StickFlick.Entities.WalkingEnemy;

public class Game implements Screen{

	StickFlick game;
	SpriteBatch batch;
	Texture gameBackground;
	Stage stage;
	GestureDetector gd;
	InputMultiplexer im;
	WalkingEnemy testEnemy;
	
	public Game(StickFlick game){
		this.game = game;
		testEnemy = new WalkingEnemy("basic", 100, Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2);
	}
	
	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		//enemy1.update(delta);
		System.out.println("");
		
		stage.act(Gdx.graphics.getDeltaTime());
		
		batch.begin();
		stage.draw();
		batch.end();
	}

	@Override
	public void resize(int width, int height) {
		stage = new Stage(width, height, true);
		stage.clear();
		
		gd = new GestureDetector(new GestureDetection(stage));
		im = new InputMultiplexer(gd, stage);
		Gdx.input.setInputProcessor(im);
		
		Texture gameBackground = new Texture("data/gameBackground.png");
		Image backgroundImage = new Image(gameBackground);
		backgroundImage.setWidth(Gdx.graphics.getWidth());
		backgroundImage.setHeight(Gdx.graphics.getHeight());
		stage.addActor(backgroundImage);
		
		stage.addActor(testEnemy.getImage());
		testEnemy.getImage().addCaptureListener(new ActorGestureListener());
		
		stage.addAction(Actions.sequence(Actions.alpha(0), Actions.fadeIn(1)));
		
	}

	@Override
	public void show() {
		batch = new SpriteBatch();
	}

	@Override
	public void hide() {
		
	}

	@Override
	public void pause() {
		
	}

	@Override
	public void resume() {
		
	}

	@Override
	public void dispose() {
		
	}

}
