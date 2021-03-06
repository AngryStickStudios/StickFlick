package com.AngryStickStudios.StickFlick.Screens;

import com.AngryStickStudios.StickFlick.StickFlick;
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
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;

public class Store implements Screen{

	Preferences prefs = Gdx.app.getPreferences("Preferences");
	
	private final long bombCatapultPrice = 400;
	private final long archersPrice = 500;
	private final long magesPrice = 400;
	private final long serfsPrice = 150;
	private final long fingerOfGodPrice = 200;
	private final long hornOfChampPrice = 350;
	private final long blizzardPrice = 180;
	
	private boolean bombCatapultSelected, magesSelected, archersSelected, blizzardSelected, serfsSelected, fingerOfGodSelected, hornOfChampSelected;
	Label coinLabel, desTextLabel, priceLabel, desTitleLabel, ownedLabel;
	
	StickFlick app;
	Stage stage;
	Sound purchase, buttonClick;
	float SFXVolume;
	
	BitmapFont titleFont, descriptionFont, textFont, desTextFont, totalFont;

	TextureAtlas atlas;
	Skin skin;
	SpriteBatch batch;
	Button buyButton, backButton, bombCatapultButton, fingerOfGodButton, hornOfChampButton, blizzardButton, archersButton, magesButton, serfsButton, powerUpSelection;
	Texture storeBackground, transBackground, desBackground;
	
	String title, description, price, owned;

	
	//Window popup = new Window("Note", skin);
	
	
	public Store(StickFlick game){
		app = game;
		
		//popup.setPosition(0, 0);
		
		description = "";
		price = "";
		title = "";
		owned = "";
		
	}
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		stage.act(delta);
		
		coinLabel.setText("Spendable Coins:  " + prefs.getLong("currency", 0));
		desTextLabel.setText(description);
		priceLabel.setText(price);
		desTitleLabel.setText(title);
		ownedLabel.setText(owned);
		
		
		batch.begin();
		stage.draw();
		batch.end();
		
	}

	@Override
	public void resize(int width, int height) {
		stage = new Stage(width, height, true);
		stage.clear();
		
		Gdx.input.setInputProcessor(stage);	
		
		storeBackground = new Texture("data/gamebackground.png");
		Image backgroundImage = new Image(storeBackground);
		backgroundImage.setZIndex(100000);
		backgroundImage.setWidth(Gdx.graphics.getWidth());
		backgroundImage.setHeight(Gdx.graphics.getHeight());
		stage.addActor(backgroundImage);
		
		transBackground = new Texture("data/store_background.png");
		Image transImage = new Image(transBackground);
		transImage.setZIndex(100000);
		transImage.setWidth(Gdx.graphics.getWidth());
		transImage.setHeight(Gdx.graphics.getHeight());
		stage.addActor(transImage);
		
		desBackground = new Texture("data/powerup_description.png");
		Image desImage = new Image(desBackground);
		desImage.setZIndex(100000);
		desImage.setWidth(Gdx.graphics.getWidth()/3);
		desImage.setHeight(Gdx.graphics.getHeight());
		desImage.setX(Gdx.graphics.getWidth()/2 + desImage.getWidth()/2);
		stage.addActor(desImage);
		
		titleFont.setScale(width * 0.0007f);
		LabelStyle titleStyle = new LabelStyle(titleFont, Color.BLACK);
		Label powerUpLabel = new Label("Power Ups", titleStyle);
		powerUpLabel.setPosition(Gdx.graphics.getWidth()/2 - Gdx.graphics.getWidth()/4, Gdx.graphics.getHeight() - Gdx.graphics.getHeight()/8);
		stage.addActor(powerUpLabel);
		
		descriptionFont.setScale(width * 0.0004f);
		LabelStyle desTitleStyle = new LabelStyle(descriptionFont, Color.BLACK);
		desTitleLabel = new Label(title, desTitleStyle);
		desTitleLabel.setX(Gdx.graphics.getWidth()/2 + Gdx.graphics.getWidth()/5);
		desTitleLabel.setY(Gdx.graphics.getHeight()- Gdx.graphics.getHeight()/8);
		stage.addActor(desTitleLabel);
		
		textFont.setScale(width * 0.0004f);
		LabelStyle textStyle = new LabelStyle(textFont, Color.BLACK);
		coinLabel = new Label("Spendable Coins: " + prefs.getLong("currency", 0), textStyle);
		coinLabel.setX(Gdx.graphics.getWidth()/20);
		coinLabel.setY(Gdx.graphics.getHeight()/20);
		stage.addActor(coinLabel);
		
		desTextFont.setScale(width * 0.0003f);
		LabelStyle desTextStyle = new LabelStyle(desTextFont, Color.BLACK);
		desTextLabel = new Label(description, desTextStyle);
		desTextLabel.setX(Gdx.graphics.getWidth()/2 + Gdx.graphics.getWidth()/5);
		desTextLabel.setY(Gdx.graphics.getHeight()- Gdx.graphics.getHeight()/3f);
		stage.addActor(desTextLabel);

		priceLabel = new Label(price, desTextStyle);
		priceLabel.setX(Gdx.graphics.getWidth()/2 + Gdx.graphics.getWidth()/5);
		priceLabel.setY(Gdx.graphics.getHeight()- Gdx.graphics.getHeight()/2);
		stage.addActor(priceLabel);
		
		ownedLabel = new Label(owned, textStyle);
		ownedLabel.setX(Gdx.graphics.getWidth()/2 + Gdx.graphics.getWidth()/5);
		ownedLabel.setY(Gdx.graphics.getHeight() - Gdx.graphics.getHeight() / 1.5f);
		stage.addActor(ownedLabel);
		
		buyButton = new Button(skin.getDrawable("Buy Button"), skin.getDrawable("Buy Button Pressed"));
		buyButton.setWidth(Gdx.graphics.getWidth() / 10);
		buyButton.setHeight(Gdx.graphics.getWidth() / 24);
		buyButton.setPosition(Gdx.graphics.getWidth()/2 + Gdx.graphics.getWidth()/4.5f, Gdx.graphics.getHeight()/20);
		stage.addActor(buyButton);
		
		backButton = new Button(skin.getDrawable("BackButton"), skin.getDrawable("Back Button Pressed"));
		backButton.setWidth(Gdx.graphics.getWidth() / 10);
		backButton.setHeight(Gdx.graphics.getWidth() / 24);
		backButton.setPosition(Gdx.graphics.getWidth()/2 + Gdx.graphics.getWidth()/2.8f, Gdx.graphics.getHeight()/20);
		stage.addActor(backButton);
		
		blizzardButton = new Button(skin.getDrawable("IcePowerupButtonLight"),skin.getDrawable("IcePowerupButtonDark"));
		blizzardButton.setWidth(Gdx.graphics.getWidth() / 16);
		blizzardButton.setHeight(Gdx.graphics.getWidth() / 16);
		blizzardButton.setPosition(Gdx.graphics.getWidth()/4 - 2*blizzardButton.getWidth(),Gdx.graphics.getHeight()/2 + blizzardButton.getHeight()/2);
		stage.addActor(blizzardButton);
		
		serfsButton = new Button(skin.getDrawable("HealPowerupButtonLight"),skin.getDrawable("HealPowerupButtonDark"));
		serfsButton.setWidth(Gdx.graphics.getWidth() / 16);
		serfsButton.setHeight(Gdx.graphics.getWidth() / 16);
		serfsButton.setPosition(Gdx.graphics.getWidth()/2 - 3*serfsButton.getWidth(), Gdx.graphics.getHeight()/2 + serfsButton.getHeight()/2);
		stage.addActor(serfsButton);
		
		bombCatapultButton = new Button(skin.getDrawable("ExplosionPowerupButtonLight"),skin.getDrawable("ExplosionPowerupButtonDark"));
		bombCatapultButton.setWidth(Gdx.graphics.getWidth() / 16);
		bombCatapultButton.setHeight(Gdx.graphics.getWidth() / 16);
		bombCatapultButton.setPosition(Gdx.graphics.getWidth()/2 ,Gdx.graphics.getHeight()/2 + bombCatapultButton.getHeight()/2);
		stage.addActor(bombCatapultButton);
		
		fingerOfGodButton = new Button(skin.getDrawable("GodPowerupButtonLight"),skin.getDrawable("GodPowerupButtonDark"));
		fingerOfGodButton.setWidth(Gdx.graphics.getWidth() / 16);
		fingerOfGodButton.setHeight(Gdx.graphics.getWidth() / 16);
		fingerOfGodButton.setPosition(Gdx.graphics.getWidth()/4 - 2*fingerOfGodButton.getWidth(),Gdx.graphics.getHeight()/2 - fingerOfGodButton.getHeight());
		stage.addActor(fingerOfGodButton);
		
		hornOfChampButton = new Button(skin.getDrawable("HornPowerupButtonLight"),skin.getDrawable("HornPowerupButtonDark"));
		hornOfChampButton.setWidth(Gdx.graphics.getWidth() / 16);
		hornOfChampButton.setHeight(Gdx.graphics.getWidth() / 16);
		hornOfChampButton.setPosition(Gdx.graphics.getWidth()/2 - 3*hornOfChampButton.getWidth(),Gdx.graphics.getHeight()/2 - hornOfChampButton.getHeight());
		stage.addActor(hornOfChampButton);
		
		magesButton = new Button(skin.getDrawable("MagePowerupButtonLight"),skin.getDrawable("MagePowerupButtonDark"));
		magesButton.setWidth(Gdx.graphics.getWidth() / 16);
		magesButton.setHeight(Gdx.graphics.getWidth() / 16);
		magesButton.setPosition(Gdx.graphics.getWidth()/2,Gdx.graphics.getHeight()/2 - magesButton.getHeight());
		stage.addActor(magesButton);
		
		archersButton = new Button(skin.getDrawable("ArcherPowerupButtonLight"),skin.getDrawable("ArcherPowerupButtonDark"));
		archersButton.setWidth(Gdx.graphics.getWidth() / 16);
		archersButton.setHeight(Gdx.graphics.getWidth() / 16);
		archersButton.setPosition(Gdx.graphics.getWidth()/2 - 3*archersButton.getWidth(),Gdx.graphics.getHeight()/2 - 5*archersButton.getHeight()/2);
		stage.addActor(archersButton);
		
		
		stage.addAction(Actions.sequence(Actions.alpha(0), Actions.fadeIn(1)));
		
		buyButton.addListener(new InputListener(){
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				buttonClick.stop();
				buttonClick.play(SFXVolume);
				return true;
			}
			public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
				
				buyPowerUps();
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
						((StickFlick) Gdx.app.getApplicationListener()).setScreen(new MainMenu(app));
					}
				})));
			}
		});
		
		blizzardButton.addListener(new InputListener(){
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				buttonClick.stop();
				buttonClick.play(SFXVolume);
				return true;
			}
			public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
				deselectPowerups();
				blizzardSelected = true;
				title = "Blizzard";
				description = "Freezes the stick dudes\ngiving you 10 seconds\nto kill as many as you can.";
				price = "Price:  180 coinage";
				owned = "Owned: " + prefs.getBoolean("blizzard");
			}
		});
		
		serfsButton.addListener(new InputListener(){
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				buttonClick.stop();
				buttonClick.play(SFXVolume);
				return true;
			}
			public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
				deselectPowerups();
				serfsSelected = true;
				title = "Serfs";
				description = "Brings little dudes\nto help retore the health\nof your castle.";
				price = "Price:  150 coinage";
				owned = "Owned: " + prefs.getBoolean("serfs");
			}
		});
		
		bombCatapultButton.addListener(new InputListener(){
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				buttonClick.stop();
				buttonClick.play(SFXVolume);
				return true;
			}
			public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
				deselectPowerups();
				bombCatapultSelected = true;
				title = "Bomb Catapult";
				description = "Throws bombs that kills\nall the enemy dudes on the\nscreen.";
				price = "Price:  400 coinage";
				owned = "Owned: " + prefs.getBoolean("bomb");
			}
		});
		
		fingerOfGodButton.addListener(new InputListener(){
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				buttonClick.stop();
				buttonClick.play(SFXVolume);
				return true;
			}
			public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
				deselectPowerups();
				fingerOfGodSelected = true;
				title = "Finger Of God";
				description = "Gives you the ability to\ntap the dudes in order to\nkill them.";
				price = "Price:  200 coinage";
				owned = "Owned: " + prefs.getBoolean("fingerOfGod");
			}
		});
		
		hornOfChampButton.addListener(new InputListener(){
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				buttonClick.stop();
				buttonClick.play(SFXVolume);
				return true;
			}
			public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
				deselectPowerups();
				hornOfChampSelected = true;
				title = "Horn Of The Champ";
				description = "Specifically targets Big\nDudes if any exist during\nthe game. (Last 10secs)";
				price = "Price:  350 coinage";
				owned = "Owned: " + prefs.getBoolean("hornOfChamp");
			}
		});
		
		
		magesButton.addListener(new InputListener(){
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				buttonClick.stop();
				buttonClick.play(SFXVolume);
				return true;
			}
			public void touchUp (InputEvent event, float x, float y, int pointer, int button) {	
				deselectPowerups();
				magesSelected = true;
				title = "Mages";
				description = "Reduces all ability\ncooldowns by 5%.";
				price = "Price:  400 coinage";
				owned = "Owned: " + Integer.toString(prefs.getInteger("mages", 0)) + " (Max: 2)";
			}
		});
		
		
		archersButton.addListener(new InputListener(){
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				buttonClick.stop();
				buttonClick.play(SFXVolume);
				return true;
			}
			public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
				deselectPowerups();
				archersSelected = true;
				title = "Archers";
				description = "Raw damage per\nsecond increase.";	
				price = "Price:  500 coinage";
				owned = "Owned: " + Integer.toString(prefs.getInteger("archers", 0)) + " (Max: 2)";
			}
		});
		
	}
	

	@Override
	public void show() {
		batch = new SpriteBatch();
		atlas = new TextureAtlas("data/Textures.atlas");
		skin = new Skin();
		skin.addRegions(atlas);
		titleFont = new BitmapFont(Gdx.files.internal("data/whiteFont.fnt"), false);
		descriptionFont = new BitmapFont(Gdx.files.internal("data/whiteFont.fnt"), false);
		textFont = new BitmapFont(Gdx.files.internal("data/whiteFont.fnt"), false);
		totalFont = new BitmapFont(Gdx.files.internal("data/whiteFont.fnt"), false);
		desTextFont = new BitmapFont(Gdx.files.internal("data/whiteFont.fnt"), false);
		purchase = Gdx.audio.newSound(Gdx.files.internal("data/sounds/coins.mp3"));	
		buttonClick = Gdx.audio.newSound(Gdx.files.internal("data/sounds/button2.mp3"));
		
		//Set Volumes
		SFXVolume = prefs.getInteger("SFXVolume") * 0.01f;
	}

	public void deselectPowerups(){
		bombCatapultSelected = false;
		magesSelected = false;
		archersSelected = false;
		blizzardSelected = false;
		serfsSelected = false;
		fingerOfGodSelected = false;
		hornOfChampSelected = false;
	}
	
	public void buyPowerUps(){
		
		if(magesPrice <= prefs.getLong("currency", 0) && magesSelected && prefs.getInteger("mages", 0) <= 2){
			
			long temp = prefs.getLong("currency", 0) - magesPrice;
			prefs.putLong("currency", temp);
			prefs.flush();
			prefs.putInteger("mages", prefs.getInteger("mages") + 1);
			prefs.flush();
			purchase.play(SFXVolume);
			magesSelected = false;
		}
		
		if(archersPrice <= prefs.getLong("currency", 0) && archersSelected && prefs.getInteger("archers", 0) <= 2){
			
			long temp = prefs.getLong("currency", 0) - archersPrice;
			prefs.putLong("currency", temp);
			prefs.flush();
			prefs.putInteger("archers", prefs.getInteger("archers") + 1);
			prefs.flush();
			purchase.play(SFXVolume);
			archersSelected = false;
		}
		
		if(bombCatapultPrice <= prefs.getLong("currency", 0) && bombCatapultSelected && !prefs.getBoolean("bomb")){
			
			long temp = prefs.getLong("currency", 0) - bombCatapultPrice;
			prefs.putLong("currency", temp);
			prefs.flush();
			prefs.putBoolean("bomb", true);
			prefs.flush();
			purchase.play(SFXVolume);
			bombCatapultSelected = false;
		}
		
		if(blizzardPrice <= prefs.getLong("currency", 0) && blizzardSelected && !prefs.getBoolean("blizzard")){
			
			long temp = prefs.getLong("currency", 0) - blizzardPrice;
			prefs.putLong("currency", temp);
			prefs.flush();
			prefs.putBoolean("blizzard", true);
			prefs.flush();
			purchase.play(SFXVolume);
			blizzardSelected = false;
		}
		
		if(fingerOfGodPrice <= prefs.getLong("currency", 0) && fingerOfGodSelected && !prefs.getBoolean("fingerOfGod")){
			
			long temp = prefs.getLong("currency", 0) - fingerOfGodPrice;
			prefs.putLong("currency", temp);
			prefs.flush();
			prefs.putBoolean("fingerOfGod", true);
			prefs.flush();
			purchase.play(SFXVolume);
			fingerOfGodSelected = false;
		}
		
		if(hornOfChampPrice <= prefs.getLong("currency", 0) && hornOfChampSelected && !prefs.getBoolean("hornOfChamp")){
			
			long temp = prefs.getLong("currency", 0) - hornOfChampPrice;
			prefs.putLong("currency", temp);
			prefs.flush();
			prefs.putBoolean("hornOfChamp", true);
			prefs.flush();
			purchase.play(SFXVolume);
			hornOfChampSelected = false;
		}
		
		if(serfsPrice <= prefs.getLong("currency", 0) && serfsSelected && !prefs.getBoolean("serfs")){
			
			long temp = prefs.getLong("currency", 0) - serfsPrice;
			prefs.putLong("currency", temp);
			prefs.flush();
			prefs.putBoolean("serfs", true);
			prefs.flush();
			purchase.play(SFXVolume);
			serfsSelected = false;
		}	
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
		titleFont.dispose();
		textFont.dispose();
		descriptionFont.dispose();
		desTextFont.dispose();
		stage.dispose();
		totalFont.dispose();
		buttonClick.dispose();
		purchase.dispose();
		
	}

}
