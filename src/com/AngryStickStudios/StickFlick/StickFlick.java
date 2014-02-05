package com.AngryStickStudios.StickFlick;

import com.AngryStickStudios.StickFlick.Screens.SplashScreen;
import com.badlogic.gdx.Game;

public class StickFlick extends Game {

	public static final String version = "0.0.04 Pre-Alpha (Yay There Is Music!!)";
	public static final String LOG = "StickFlick";
	
	@Override
	public void create() {		
		setScreen(new SplashScreen(this));
	}

	@Override
	public void dispose() {
		super.dispose();
	}

	@Override
	public void render() {		
		super.render();
	}

	@Override
	public void resize(int width, int height) {
		super.resize(width, height);
	}

	@Override
	public void pause() {
		super.pause();
	}

	@Override
	public void resume() {
		super.resume();
	}
}

