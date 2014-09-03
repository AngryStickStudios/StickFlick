package com.AngryStickStudios.StickFlick.Screens;

import com.AngryStickStudios.StickFlick.StickFlick;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;

public class Credits implements Screen {
	
	Preferences prefs = Gdx.app.getPreferences("Preferences");
	
	private int[] scores = {prefs.getInteger("score1", 0), prefs.getInteger("score2", 0), prefs.getInteger("score3", 0)};
	
	String creditsString, creditsTString, creditsHString;
	StickFlick game;
	Stage stage;
	BitmapFont white;
	TextureAtlas atlas;
	Skin skin;
	SpriteBatch batch;
	TextButton backButton, creditsButton;
	Sound buttonClick;
	
	
	public Credits(StickFlick game){
		this.game = game;
	}
	
	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		stage.act(delta);
		
		batch.begin();
		stage.draw();
		batch.end();
	}

	@Override
	public void resize(int width, int height) {
		stage = new Stage(width, height, true);
		stage.clear();
		
		white.setScale(width * 0.0003f);
		Gdx.input.setInputProcessor(stage);
		
		// Styles for text buttons, slider and labels
		TextButtonStyle style = new TextButtonStyle();
		style.up = skin.getDrawable("LightButton");
		style.down = skin.getDrawable("DarkButton");
		style.font = white;
		
		//Credits Title
		LabelStyle creditsTitle = new LabelStyle(white, Color.WHITE);
		Label creditsT = new Label(null,creditsTitle);
		
		
		creditsTString = "Credits:  \n";	
		creditsT.setText(creditsTString);
		
		creditsT.setX(Gdx.graphics.getWidth() *0.5f);
		creditsT.setY(Gdx.graphics.getHeight() * 0.90f);
		creditsT.setAlignment(0,2);
		creditsT.setFontScale(width * 0.0004f);
		stage.addActor(creditsT);
		
		//Credits Header
		LabelStyle creditsHText = new LabelStyle(white, Color.WHITE);
		Label creditsH = new Label(null,creditsHText);
		
		
		creditsString = "---Coding and Development---\n";	
		creditsH.setText(creditsString);
		
		creditsH.setX(Gdx.graphics.getWidth() *0.5f);
		creditsH.setY(Gdx.graphics.getHeight() * 0.77f);
		creditsH.setAlignment(0,2);
		creditsH.setFontScale(width * 0.0004f);
		stage.addActor(creditsH);

		//Credits Test
		LabelStyle creditsText = new LabelStyle(white, Color.WHITE);
		Label credits = new Label(null,creditsText);
		
		
		creditsString = "Jeremy Baker, Angela Evans, Seth Paskert,\n" + "Irene Freire, Aidan Pace, Valentin Caleon\n" + "Alex Ciccone, Andres Hernandez";	
		credits.setText(creditsString);
		
		credits.setX(Gdx.graphics.getWidth() *0.5f);
		credits.setY(Gdx.graphics.getHeight() * 0.65f);
		credits.setAlignment(0,2);
		credits.setFontScale(width * 0.0003f);
		stage.addActor(credits);
		
		//Back Button
		backButton = new TextButton("Options Menu", style);
		backButton.setWidth(Gdx.graphics.getWidth() / 6);
		backButton.setHeight(Gdx.graphics.getWidth() / 24);
		backButton.setX(Gdx.graphics.getWidth() / 2 - backButton.getWidth() / 2);
		backButton.setY(Gdx.graphics.getHeight() / 2 - backButton.getWidth() / 2);
		stage.addActor(backButton);
		
		stage.addAction(Actions.sequence(Actions.alpha(0), Actions.fadeIn(1)));
		
		backButton.addListener(new InputListener(){
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				System.out.println("down");
				buttonClick.stop();
				buttonClick.play();
				return true;
			}
			public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
				System.out.println("up");
				stage.addAction(Actions.sequence(Actions.fadeOut(.3f), Actions.run(new Runnable() {
					@Override
					public void run() {
						((StickFlick) Gdx.app.getApplicationListener()).setScreen(new Options(game));
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
		buttonClick = Gdx.audio.newSound(Gdx.files.internal("data/sounds/button2.mp3"));
	}

	@Override
	public void hide() {
		dispose();
		
	}

	@Override
	public void pause() {
		
	}

	@Override
	public void resume() {
		
	}

	@Override
	public void dispose() {
		batch.dispose();
		skin.dispose();
		atlas.dispose();
		white.dispose();
		stage.dispose();
		buttonClick.dispose();
	}
	
}
