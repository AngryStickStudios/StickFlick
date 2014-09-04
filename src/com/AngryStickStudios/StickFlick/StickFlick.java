package com.AngryStickStudios.StickFlick;

import com.AngryStickStudios.StickFlick.Controller.AnimationLoader;
import com.AngryStickStudios.StickFlick.Screens.MainMenu;
import com.AngryStickStudios.StickFlick.Screens.SplashScreen;
import com.badlogic.gdx.Game;

public class StickFlick extends Game {

	public static final String version = "0.12 Beta (Bug Fixes and Balancing)";
	public static final String LOG = "StickFlick";
	public AnimationLoader anims;
	
	@Override
	public void create() {
		anims = new AnimationLoader();
		setScreen(new SplashScreen(this));
		//setScreen(new MainMenu(this));
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

