package com.AngryStickStudios.StickFlick.Screens;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.TweenEquations;
import aurelienribon.tweenengine.TweenManager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.AngryStickStudios.StickFlick.StickFlick;
import com.AngryStickStudios.StickFlick.TweenAccessors.SpriteTween;

public class SplashScreen implements Screen{

	Texture splashTexture;
	Sprite splashSprite;
	SpriteBatch batch;
	StickFlick game;
	TweenManager manager;
	Sound swoosh;
	
	public SplashScreen(StickFlick game){
		this.game = game;
	}
	
	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		manager.update(delta);
		batch.begin();
		splashSprite.draw(batch);
		batch.end();
	}

	@Override
	public void resize(int width, int height) {
		
	}

	@Override
	public void show() {
		swoosh = Gdx.audio.newSound(Gdx.files.internal("data/sounds/splashSwoosh.mp3"));
		swoosh.play();
		
		splashTexture = new Texture("data/SplashScreen1.png");
		splashTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
	
		splashSprite = new Sprite(splashTexture);
		splashSprite.setColor(1, 1, 1, 0);
		
		splashSprite.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		
		batch = new SpriteBatch();
		
		Tween.registerAccessor(Sprite.class,  new SpriteTween());
		
		manager = new TweenManager();
		
		TweenCallback cb = new TweenCallback(){
			@Override
			public void onEvent(int type, BaseTween<?> source) {
				tweenCompleted();
				
			}	
		};
		
		Tween.to(splashSprite, SpriteTween.ALPHA, 1f).target(1).ease(TweenEquations.easeInQuad).repeatYoyo(1, 1.7f).setCallback(cb).setCallbackTriggers(TweenCallback.COMPLETE).start(manager);
	}
	
	private void tweenCompleted(){
		game.setScreen(new SplashScreen2(game));
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


