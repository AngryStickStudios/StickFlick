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

public class MainMenu implements Screen{

	StickFlick game;
	Stage stage;
	BitmapFont white;
	TextureAtlas atlas;
	Skin skin;
	SpriteBatch batch;
	TextButton playButton, storeButton, tutorialButton, optionsButton, scoreButton;
	Sound menuTheme;
	
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
		
		Gdx.input.setInputProcessor(stage);
		
		TextButtonStyle buttonStyle = new TextButtonStyle();
		buttonStyle.up = skin.getDrawable("LightButton");
		buttonStyle.down = skin.getDrawable("DarkButton");
		buttonStyle.font = white;
		
		// Background Image
		Texture backgroundTexture = new Texture("data/menuBackground.png");
		Image backgroundImage = new Image(backgroundTexture);
		backgroundImage.setWidth(Gdx.graphics.getWidth());
		backgroundImage.setHeight(Gdx.graphics.getHeight());
		stage.addActor(backgroundImage);

		// Label
		LabelStyle versionText = new LabelStyle(white, Color.WHITE);
		Label gameVersion = new Label(StickFlick.version, versionText);
		stage.addActor(gameVersion);
		
		// BUTTON INITIATION
		//New Game Button
		playButton = new TextButton("Play", buttonStyle);
		playButton.setWidth(Gdx.graphics.getWidth() / 6);
		playButton.setHeight(Gdx.graphics.getHeight() / 12);
		playButton.setX(Gdx.graphics.getWidth() / 2 - playButton.getWidth() / 2 - Gdx.graphics.getWidth() / 3);
		playButton.setY(Gdx.graphics.getHeight() /2 + playButton.getHeight());
		stage.addActor(playButton);
		//Load Game Button
		storeButton = new TextButton("Store", buttonStyle);
		storeButton.setWidth(Gdx.graphics.getWidth() / 6);
		storeButton.setHeight(Gdx.graphics.getHeight() / 12);
		storeButton.setX(Gdx.graphics.getWidth() / 2 - storeButton.getWidth() / 2 - Gdx.graphics.getWidth() / 4);
		storeButton.setY(Gdx.graphics.getHeight() /2 - storeButton.getHeight() / 2);
		stage.addActor(storeButton);
		//Settings Button
		tutorialButton = new TextButton("Tutorial", buttonStyle);
		tutorialButton.setWidth(Gdx.graphics.getWidth() / 6);
		tutorialButton.setHeight(Gdx.graphics.getHeight() / 12);
		tutorialButton.setX(Gdx.graphics.getWidth() / 2 - tutorialButton.getWidth() / 2 - Gdx.graphics.getWidth() / 8);
		tutorialButton.setY(Gdx.graphics.getHeight() /2 - tutorialButton.getHeight() * 2);
		stage.addActor(tutorialButton);
		//Credits Button
		optionsButton = new TextButton("Options", buttonStyle);
		optionsButton.setWidth(Gdx.graphics.getWidth() / 6);
		optionsButton.setHeight(Gdx.graphics.getHeight() / 12);
		optionsButton.setX(Gdx.graphics.getWidth() / 2 - optionsButton.getWidth() / 2);
		optionsButton.setY(Gdx.graphics.getHeight() /2 - optionsButton.getHeight() * 3.5f);
		stage.addActor(optionsButton);
		//High Scores
		scoreButton = new TextButton("High Scores", buttonStyle);
		scoreButton.setWidth(Gdx.graphics.getWidth() / 6);
		scoreButton.setHeight(Gdx.graphics.getHeight() / 12);
		scoreButton.setX(Gdx.graphics.getWidth() * 0.80f);
		scoreButton.setY(Gdx.graphics.getHeight() * 0.90f);
		stage.addActor(scoreButton);
		
		stage.addAction(Actions.sequence(Actions.alpha(0), Actions.fadeIn(1)));

		// Play the game
		playButton.addListener(new InputListener(){
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				System.out.println("down");
				return true;
			}
			public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
				System.out.println("up");

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
				System.out.println("down");
				return true;
			}
			public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
				System.out.println("up");

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
				System.out.println("down");
				return true;
			}
			public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
				System.out.println("up");

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
				System.out.println("down");
				return true;
			}
			public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
				System.out.println("up");

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
		menuTheme = Gdx.audio.newSound(Gdx.files.internal("data/menuTheme.wav"));
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
	}
	

}
