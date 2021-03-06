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
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Slider.SliderStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

public class Options implements Screen {
	
	Preferences prefs = Gdx.app.getPreferences("Preferences");
	
	StickFlick game;
	Stage stage;
	BitmapFont white;
	TextureAtlas atlas;
	Skin skin;
	SpriteBatch batch;
	TextButton backButton, creditsButton, leftyButton;
	Sound buttonClick;
	Label musicVolumeLabel, musicVolumeLabelPercent, SFXVolumeLabel, SFXVolumeLabelPercent;
	Slider musicVolumeSlider, SFXVolumeSlider;
	float SFXVolume;
	
	public Options(StickFlick game){
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
		
		white.setScale(width * 0.0004f);
		Gdx.input.setInputProcessor(stage);
		
		// Styles for text buttons, slider and labels
		TextButtonStyle style = new TextButtonStyle();
		style.up = skin.getDrawable("LightButton");
		style.down = skin.getDrawable("DarkButton");
		style.font = white;
		SliderStyle slidStyle = new SliderStyle(skin.getDrawable("Slider"), skin.getDrawable("SliderKnob"));
		LabelStyle labelStyle = new LabelStyle(white, Color.WHITE);
		
		// Sliders
		// Music Slider
		musicVolumeLabel = new Label("Music Volume", labelStyle);
		musicVolumeLabel.setX(Gdx.graphics.getWidth() * 0.25f);
		musicVolumeLabel.setY(Gdx.graphics.getHeight() * 0.33f);
		
		musicVolumeLabelPercent = new Label(Integer.toString(prefs.getInteger("musicVolume")) + "%", labelStyle);
		musicVolumeLabelPercent.setX(Gdx.graphics.getWidth() * 0.75f);
		musicVolumeLabelPercent.setY(Gdx.graphics.getHeight() * 0.33f);
		
		musicVolumeSlider = new Slider(1, 100, 1, false, slidStyle);
		musicVolumeSlider.setWidth(Gdx.graphics.getWidth() * 0.2f);
		musicVolumeSlider.setX(Gdx.graphics.getWidth() * 0.5f);
		musicVolumeSlider.setY(Gdx.graphics.getHeight() * 0.34f);
		
		//Listeners and setting in preferences
		musicVolumeSlider.setValue(prefs.getInteger("musicVolume"));
		musicVolumeSlider.addCaptureListener( new ChangeListener() {
			public void changed(ChangeEvent event, Actor actor) {
				prefs.putInteger("musicVolume", (int) musicVolumeSlider.getValue());
				prefs.flush();
				updateVolumeLabels();
			}
		});
		
		stage.addActor(musicVolumeLabel);
		stage.addActor(musicVolumeLabelPercent);
		stage.addActor(musicVolumeSlider);
		
		
		//SFX Slider
		SFXVolumeLabel = new Label("SFX Volume", labelStyle);
		SFXVolumeLabel.setX(Gdx.graphics.getWidth() * 0.25f);
		SFXVolumeLabel.setY(Gdx.graphics.getHeight() * 0.25f);
		
		SFXVolumeLabelPercent = new Label(Integer.toString(prefs.getInteger("SFXVolume")) + "%", labelStyle);
		SFXVolumeLabelPercent.setX(Gdx.graphics.getWidth() * 0.75f);
		SFXVolumeLabelPercent.setY(Gdx.graphics.getHeight() * 0.25f);
		
		SFXVolumeSlider = new Slider(1, 100, 1, false, slidStyle);
		SFXVolumeSlider.setWidth(Gdx.graphics.getWidth() * 0.2f);
		SFXVolumeSlider.setX(Gdx.graphics.getWidth() * 0.5f);
		SFXVolumeSlider.setY(Gdx.graphics.getHeight() * 0.26f);
		
		//Listeners and setting in preferences
		SFXVolumeSlider.setValue(prefs.getInteger("SFXVolume"));
		SFXVolumeSlider.addCaptureListener( new ChangeListener() {
			public void changed(ChangeEvent event, Actor actor) {
				prefs.putInteger("SFXVolume", (int) SFXVolumeSlider.getValue());
				prefs.flush();
				updateVolumeLabels();
			}
		});
		
		stage.addActor(SFXVolumeLabel);
		stage.addActor(SFXVolumeLabelPercent);
		stage.addActor(SFXVolumeSlider);
		
		// BUTTON INITIATION
		//Lefty Button
		leftyButton = new TextButton("Lefty?", style);
		leftyButton.setWidth(Gdx.graphics.getWidth() / 6);
		leftyButton.setHeight(Gdx.graphics.getWidth() / 24);
		leftyButton.setX(Gdx.graphics.getWidth() / 2 - leftyButton.getWidth() / 2 - Gdx.graphics.getWidth() / 4);
		leftyButton.setY(Gdx.graphics.getHeight() /2 - leftyButton.getHeight() / 2);
		stage.addActor(leftyButton);

		//Credits Button
		creditsButton = new TextButton("Credits", style);
		creditsButton.setWidth(Gdx.graphics.getWidth() / 6);
		creditsButton.setHeight(Gdx.graphics.getWidth() / 24);
		creditsButton.setX(Gdx.graphics.getWidth() / 2 - creditsButton.getWidth() / 2);
		creditsButton.setY(Gdx.graphics.getHeight() /2 - creditsButton.getHeight() / 2);
		stage.addActor(creditsButton);
		
		//Back Button
		backButton = new TextButton("Main Menu", style);
		backButton.setWidth(Gdx.graphics.getWidth() / 6);
		backButton.setHeight(Gdx.graphics.getWidth() / 24);
		backButton.setX(Gdx.graphics.getWidth() / 2 - backButton.getWidth() / 2 + Gdx.graphics.getWidth() / 4);
		backButton.setY(Gdx.graphics.getHeight() /2 - backButton.getHeight() / 2);
		stage.addActor(backButton);
		
		stage.addAction(Actions.sequence(Actions.alpha(0), Actions.fadeIn(1)));
		
		creditsButton.addListener(new InputListener(){
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				buttonClick.stop();
				buttonClick.play(SFXVolume);
				return true;
			}
			public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
				stage.addAction(Actions.sequence(Actions.fadeOut(.3f), Actions.run(new Runnable() {
					@Override
					public void run() {
						((StickFlick) Gdx.app.getApplicationListener()).setScreen(new Credits(game));
					}
				})));
			}
		});
		
		backButton.addListener(new InputListener(){
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				buttonClick.stop();
				buttonClick.play(SFXVolume);
				return true;
			}
			public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
				stage.addAction(Actions.sequence(Actions.fadeOut(.3f), Actions.run(new Runnable() {
					@Override
					public void run() {
						((StickFlick) Gdx.app.getApplicationListener()).setScreen(new MainMenu(game));
					}
				})));
			}
		});
		
		leftyButton.addListener(new InputListener(){
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				buttonClick.stop();
				buttonClick.play(SFXVolume);	
				return true;
			}
			public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
				if (prefs.getBoolean("lefty", false) == false) {
					prefs.putBoolean("lefty", true);
					System.out.println(prefs.getBoolean("lefty", false));
				}
				else {
					prefs.putBoolean("lefty", false);
					System.out.println(prefs.getBoolean("lefty", false));
				}
				
				prefs.flush();
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
		
		//Set Volumes
		SFXVolume = prefs.getInteger("SFXVolume") * 0.01f;
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
	
    private void updateVolumeLabels()
    {
        musicVolumeLabelPercent.setText(Integer.toString(prefs.getInteger("musicVolume")) + "%");
        SFXVolumeLabelPercent.setText(Integer.toString(prefs.getInteger("SFXVolume")) + "%");
    }
	
}
