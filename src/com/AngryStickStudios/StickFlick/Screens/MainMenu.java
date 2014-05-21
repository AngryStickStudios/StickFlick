package com.AngryStickStudios.StickFlick.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
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
import com.AngryStickStudios.StickFlick.StickFlick;
import com.AngryStickStudios.StickFlick.Controller.TextButton2;

public class MainMenu implements Screen{

	StickFlick game;
	Stage stage;
	BitmapFont white;
	TextureAtlas atlas;
	Skin skin;
	SpriteBatch batch;
	TextButton2 playButton, storeButton, tutorialButton, optionsButton, scoreButton;
	Sound menuTheme;
	Sound buttonClick;
	float screenWidth, screenHeight;
	
	public MainMenu(StickFlick game){
		this.game = game;
	}
	
	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		stage.act(Gdx.graphics.getDeltaTime());
	
		batch.begin();
		stage.draw();
		batch.end();
	}

	@Override
	public void resize(int width, int height) {
		stage = new Stage(width, height, true);
		stage.clear();
		screenWidth = Gdx.graphics.getWidth();
		screenHeight = Gdx.graphics.getHeight();
		
		Gdx.input.setInputProcessor(stage);
		
		TextButtonStyle buttonStyle = new TextButtonStyle();
		buttonStyle.up = skin.getDrawable("LightButton");
		buttonStyle.down = skin.getDrawable("DarkButton");
		buttonStyle.font = white;
		
		// Background Image
		Texture backgroundTexture = new Texture("data/menubackground.png");
		Image backgroundImage = new Image(backgroundTexture);
		backgroundImage.setWidth(screenWidth);
		backgroundImage.setHeight(screenHeight);
		stage.addActor(backgroundImage);

		// Label
		LabelStyle versionText = new LabelStyle(white, Color.WHITE);
		Label gameVersion = new Label(StickFlick.version, versionText);
		gameVersion.setFontScale(screenHeight/650);
		stage.addActor(gameVersion);
		
		// Button creation
		playButton = new TextButton2("Play", buttonStyle, screenWidth * 0.113f, screenHeight * 0.57f, screenWidth * 0.17f, screenHeight * 0.09f);
		storeButton = new TextButton2("Store", buttonStyle, screenWidth * 0.213f, screenHeight * 0.45f, screenWidth * 0.17f, screenHeight * 0.09f);
		tutorialButton = new TextButton2("Tutorial", buttonStyle, screenWidth * 0.8f, screenHeight * 0.9f, screenWidth * 0.17f, screenHeight * 0.09f);
		optionsButton = new TextButton2("Options", buttonStyle, screenWidth * 0.413f, screenHeight * 0.21f, screenWidth * 0.17f, screenHeight * 0.09f);
		scoreButton = new TextButton2("High Scores", buttonStyle, screenWidth * 0.313f, screenHeight * 0.33f, screenWidth * 0.17f, screenHeight * 0.09f);

		// Add buttons to stage
		stage.addActor(playButton);
		stage.addActor(storeButton);
		stage.addActor(tutorialButton);
		stage.addActor(optionsButton);
		stage.addActor(scoreButton);
		
		stage.addAction(Actions.sequence(Actions.alpha(0), Actions.fadeIn(1)));

		// Play the game
		playButton.addListener(new InputListener(){
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				buttonClick.stop();
				buttonClick.play();
				return true;
			}
			public void touchUp (InputEvent event, float x, float y, int pointer, int button) {

				stage.addAction(Actions.sequence(Actions.fadeOut(.3f), Actions.run(new Runnable() {
					@Override
					public void run() {
						menuTheme.stop();
						((StickFlick) Gdx.app.getApplicationListener()).setScreen(new Game(game));
					}
				})));
			}
		});
		
		storeButton.addListener(new InputListener(){
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				buttonClick.stop();
				buttonClick.play();
				return true;
			}
			public void touchUp (InputEvent event, float x, float y, int pointer, int button) {

				stage.addAction(Actions.sequence(Actions.fadeOut(.3f), Actions.run(new Runnable() {
					@Override
					public void run() {
						menuTheme.stop();
						((StickFlick) Gdx.app.getApplicationListener()).setScreen(new Store(game));
					}
				})));
			}
		});
		
		// Go to options menu
		optionsButton.addListener(new InputListener(){
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				buttonClick.stop();
				buttonClick.play();
				return true;
			}
			public void touchUp (InputEvent event, float x, float y, int pointer, int button) {

				stage.addAction(Actions.sequence(Actions.fadeOut(.3f), Actions.run(new Runnable() {
					@Override
					public void run() {
						menuTheme.stop();
						((StickFlick) Gdx.app.getApplicationListener()).setScreen(new Options(game));
					}
				})));
			}
		});
		
		scoreButton.addListener(new InputListener(){
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				buttonClick.stop();
				buttonClick.play();
				return true;
			}
			public void touchUp (InputEvent event, float x, float y, int pointer, int button) {

				stage.addAction(Actions.sequence(Actions.fadeOut(.3f), Actions.run(new Runnable() {
					@Override
					public void run() {
						menuTheme.stop();
						((StickFlick) Gdx.app.getApplicationListener()).setScreen(new Scores(game));
					}
				})));
			}
		});
		
	}

	@Override
	public void show() {	
		menuTheme = Gdx.audio.newSound(Gdx.files.internal("data/sounds/menuTheme.mp3"));
		buttonClick = Gdx.audio.newSound(Gdx.files.internal("data/sounds/button2.mp3"));
		menuTheme.play();

		batch = new SpriteBatch();
		atlas = new TextureAtlas("data/Textures.atlas");
		skin = new Skin();
		skin.addRegions(atlas);
		white = new BitmapFont(Gdx.files.internal("data/whiteFont.fnt"), false);
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
		menuTheme.dispose();
		buttonClick.dispose();
	}
	

}
