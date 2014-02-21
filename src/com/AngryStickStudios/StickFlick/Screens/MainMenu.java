package com.AngryStickStudios.StickFlick.Screens;

import com.badlogic.gdx.Gdx;
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
	TextButton newGameButton, loadGameButton, optionsButton;
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
		newGameButton = new TextButton("New Game", buttonStyle);
		newGameButton.setWidth(Gdx.graphics.getWidth() / 6);
		newGameButton.setHeight(Gdx.graphics.getWidth() / 24);
		newGameButton.setX(Gdx.graphics.getWidth() / 2 - newGameButton.getWidth() / 2 - Gdx.graphics.getWidth() / 3);
		newGameButton.setY(Gdx.graphics.getHeight() /2 + newGameButton.getHeight());
		stage.addActor(newGameButton);
		//Load Game Button
		loadGameButton = new TextButton("Load Game", buttonStyle);
		loadGameButton.setWidth(Gdx.graphics.getWidth() / 6);
		loadGameButton.setHeight(Gdx.graphics.getWidth() / 24);
		loadGameButton.setX(Gdx.graphics.getWidth() / 2 - loadGameButton.getWidth() / 2 - Gdx.graphics.getWidth() / 4);
		loadGameButton.setY(Gdx.graphics.getHeight() /2 - loadGameButton.getHeight() / 2);
		stage.addActor(loadGameButton);
		//Settings Button
		optionsButton = new TextButton("Settings", buttonStyle);
		optionsButton.setWidth(Gdx.graphics.getWidth() / 6);
		optionsButton.setHeight(Gdx.graphics.getWidth() / 24);
		optionsButton.setX(Gdx.graphics.getWidth() / 2 - optionsButton.getWidth() / 2 - Gdx.graphics.getWidth() / 10);
		optionsButton.setY(Gdx.graphics.getHeight() /2 - optionsButton.getHeight() * 2);
		stage.addActor(optionsButton);
		
		stage.addAction(Actions.sequence(Actions.alpha(0), Actions.fadeIn(1)));

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
		
		newGameButton.addListener(new InputListener(){
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
