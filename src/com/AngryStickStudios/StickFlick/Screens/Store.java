package com.AngryStickStudios.StickFlick.Screens;

import com.AngryStickStudios.StickFlick.StickFlick;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;

public class Store implements Screen{

	Preferences prefs = Gdx.app.getPreferences("Preferences");
	
	private final long bombCatapultPrice = 14000;
	private final long archersPrice = 2000;
	private final long magesPrice = 2000;
	private final long serfsPrice = 2000;
	private final long fingerOfGodPrice = 9000;
	private final long hornOfChampPrice = 14000;
	private final long blizzardPrice = 7000;
	
	private boolean bombCatapultSelected;
	private boolean magesSelected;
	private boolean archersSelected;
	private boolean blizzardSelected;
	private boolean serfsSelected;
	private boolean fingerOfGodSelected;
	private boolean hornOfChampSelected;
	
	Label coinLabel;
	Label desTextLabel;
	Label priceLabel;
	Label desTitleLabel;
	
	StickFlick app;
	Stage stage;
	
	
	BitmapFont titleFont;
	BitmapFont descriptionFont;
	BitmapFont textFont;
	BitmapFont desTextFont;
	BitmapFont totalFont;

	TextureAtlas atlas;
	Skin skin;
	SpriteBatch batch;
	Button buyButton, backButton, bombCatapultButton, fingerOfGodButton, hornOfChampButton, blizzardButton, archersButton, magesButton, serfsButton, powerUpSelection;
	Texture storeBackground, transBackground, desBackground;
	
	String title;
	String description;
	String price;
	
	public Store(StickFlick game){
		app = game;
		
		description = "";
		price = "";
		title = "";
		
	}
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		stage.act(delta);
		
		coinLabel.setText("Spendable Coins:  " + prefs.getLong("currency", 0));
		desTextLabel.setText(description);
		priceLabel.setText(price);
		desTitleLabel.setText(title);
		
		
		batch.begin();
		stage.draw();
		batch.end();
		
	}

	@Override
	public void resize(int width, int height) {
		stage = new Stage(width, height, true);
		stage.clear();
		
		Gdx.input.setInputProcessor(stage);
		
		storeBackground = new Texture("data/menubackground.png");
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
		
		titleFont.setScale(2f);
		LabelStyle titleStyle = new LabelStyle(titleFont, Color.BLACK);
		Label powerUpLabel = new Label("Power Ups", titleStyle);
		powerUpLabel.setPosition(Gdx.graphics.getWidth()/2 - Gdx.graphics.getWidth()/4, Gdx.graphics.getHeight() - Gdx.graphics.getHeight()/8);
		powerUpLabel.setFontScale(Gdx.graphics.getWidth()*0.0015f,Gdx.graphics.getHeight()*0.003f);
		stage.addActor(powerUpLabel);
		
		descriptionFont.setScale(1.3f);
		LabelStyle desTitleStyle = new LabelStyle(descriptionFont, Color.BLACK);
		desTitleLabel = new Label(title, desTitleStyle);
		desTitleLabel.setX(Gdx.graphics.getWidth()/2 + Gdx.graphics.getWidth()/5);
		desTitleLabel.setY(Gdx.graphics.getHeight()- Gdx.graphics.getHeight()/8);
		desTitleLabel.setFontScale(Gdx.graphics.getWidth()*0.0009f,Gdx.graphics.getHeight()*0.0025f);
		stage.addActor(desTitleLabel);
		
		textFont.setScale(1.3f);
		LabelStyle textStyle = new LabelStyle(textFont, Color.BLACK);
		coinLabel = new Label("Spendable Coins: " + prefs.getLong("currency", 0), textStyle);
		coinLabel.setX(Gdx.graphics.getWidth()/20);
		coinLabel.setY(Gdx.graphics.getHeight()/20);
		coinLabel.setFontScale(Gdx.graphics.getWidth()*0.00075f,Gdx.graphics.getHeight()*0.0015f);
		stage.addActor(coinLabel);
		
		desTextFont.setScale(0.95f);
		LabelStyle desTextStyle = new LabelStyle(desTextFont, Color.BLACK);
		desTextLabel = new Label(description, desTextStyle);
		desTextLabel.setX(Gdx.graphics.getWidth()/2 + Gdx.graphics.getWidth()/5);
		desTextLabel.setY(Gdx.graphics.getHeight()- Gdx.graphics.getHeight()/3f);
		desTextLabel.setFontScale(Gdx.graphics.getWidth()*0.00075f,Gdx.graphics.getHeight()*0.0015f);
		stage.addActor(desTextLabel);

		priceLabel = new Label(price, desTextStyle);
		priceLabel.setX(Gdx.graphics.getWidth()/2 + Gdx.graphics.getWidth()/5);
		priceLabel.setY(Gdx.graphics.getHeight()- Gdx.graphics.getHeight()/2);
		priceLabel.setFontScale(Gdx.graphics.getWidth()*0.00075f,Gdx.graphics.getHeight()*0.0015f);
		stage.addActor(priceLabel);
		
		
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
		/*
		if(blizzard2 == 2){
			powerUpSelection = new Button(skin.getDrawable("Powerup Selection"));
			powerUpSelection.setWidth(Gdx.graphics.getWidth() / 16);
			powerUpSelection.setHeight(Gdx.graphics.getWidth() / 16);
			powerUpSelection.setPosition(Gdx.graphics.getWidth()/4 - 2*powerUpSelection.getWidth(),Gdx.graphics.getHeight()/2 + powerUpSelection.getHeight()/2);
			stage.addActor(powerUpSelection);
		}*/
		
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
		
		magesButton = new Button(skin.getDrawable("ExplosionPowerupButtonLight"),skin.getDrawable("ExplosionPowerupButtonDark"));
		magesButton.setWidth(Gdx.graphics.getWidth() / 16);
		magesButton.setHeight(Gdx.graphics.getWidth() / 16);
		magesButton.setPosition(Gdx.graphics.getWidth()/2,Gdx.graphics.getHeight()/2 - magesButton.getHeight());
		stage.addActor(magesButton);
		
		archersButton = new Button(skin.getDrawable("IcePowerupButtonLight"),skin.getDrawable("IcePowerupButtonDark"));
		archersButton.setWidth(Gdx.graphics.getWidth() / 16);
		archersButton.setHeight(Gdx.graphics.getWidth() / 16);
		archersButton.setPosition(Gdx.graphics.getWidth()/2 - 3*archersButton.getWidth(),Gdx.graphics.getHeight()/2 - 5*archersButton.getHeight()/2);
		stage.addActor(archersButton);
		
		
		stage.addAction(Actions.sequence(Actions.alpha(0), Actions.fadeIn(1)));
		
		buyButton.addListener(new InputListener(){
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				return true;
			}
			public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
				
				buyPowerUps();
			}
		});
		
		backButton.addListener(new InputListener(){
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
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
				return true;
			}
			public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
				blizzardSelected = true;
				title = "Blizzard";
				description = "Freezes the stick dudes\ngiving you 10 seconds\nto kill as many as you can.";
				price = "Price:  7,000";
			}
		});
		
		serfsButton.addListener(new InputListener(){
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				return true;
			}
			public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
				serfsSelected = true;
				title = "Serfs";
				description = "Brings little dudes\nto help retore the health\nof your castle.";
				price = "Price:  2,000";
			}
		});
		
		bombCatapultButton.addListener(new InputListener(){
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				return true;
			}
			public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
				bombCatapultSelected = true;
				title = "Bomb Catapult";
				description = "Throws bombs that kills\nall the enemy dudes on the\nscreen.";
				price = "Price:  14,000";
			}
		});
		
		fingerOfGodButton.addListener(new InputListener(){
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				return true;
			}
			public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
				fingerOfGodSelected = true;
				title = "Finger Of God";
				description = "Gives you the ability to\ntap the dudes in order to\nkill them.";
				price = "Price:  9,000";
			}
		});
		
		hornOfChampButton.addListener(new InputListener(){
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				return true;
			}
			public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
				hornOfChampSelected = true;
				title = "Horn Of The Champ";
				description = "Specifically targets Big\nDudes if any exist during\nthe game. (Last 10secs)";
				price = "Price:  14,000";
			}
		});
		
		
		magesButton.addListener(new InputListener(){
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				return true;
			}
			public void touchUp (InputEvent event, float x, float y, int pointer, int button) {	
				magesSelected = true;
				title = "Mages";
				description = "Reduces all ability\ncooldowns by 5%.";
				price = "Price:  2,000";
			}
		});
		
		
		archersButton.addListener(new InputListener(){
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				return true;
			}
			public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
				archersSelected = true;
				title = "Archers";
				description = "Raw damage per\nsecond increase.";	
				price = "Price:  2,000";
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
		
		
		
	}

	public void buyPowerUps(){
		
		if(magesPrice <= prefs.getLong("currency", 0) && magesSelected){
			prefs.putBoolean("mages", true);
			prefs.flush();
			long temp = prefs.getLong("currency", 0) - magesPrice;
			prefs.putLong("currency", temp);
			prefs.flush();
			magesSelected = false;
		}
		
		if(archersPrice <= prefs.getLong("currency", 0) && archersSelected){
			prefs.putBoolean("archers", true);
			prefs.flush();
			long temp = prefs.getLong("currency", 0) - archersPrice;
			prefs.putLong("currency", temp);
			prefs.flush();
			archersSelected = false;
		}
		
		if(bombCatapultPrice <= prefs.getLong("currency", 0) && bombCatapultSelected){
			prefs.putBoolean("bomb", true);
			prefs.flush();
			long temp = prefs.getLong("currency", 0) - bombCatapultPrice;
			prefs.putLong("currency", temp);
			prefs.flush();
			bombCatapultSelected = false;
		}
		
		if(blizzardPrice <= prefs.getLong("currency", 0) && blizzardSelected){
			prefs.putBoolean("blizzard", true);
			prefs.flush();
			long temp = prefs.getLong("currency", 0) - blizzardPrice;
			prefs.putLong("currency", temp);
			prefs.flush();
			blizzardSelected = false;
		}
		
		if(fingerOfGodPrice <= prefs.getLong("currency", 0) && fingerOfGodSelected){
			prefs.putBoolean("fingerOfGod", true);
			prefs.flush();
			long temp = prefs.getLong("currency", 0) - fingerOfGodPrice;
			prefs.putLong("currency", temp);
			prefs.flush();
			fingerOfGodSelected = false;
		}
		
		if(hornOfChampPrice <= prefs.getLong("currency", 0) && hornOfChampSelected){
			prefs.putBoolean("hornOfChamp", true);
			prefs.flush();
			long temp = prefs.getLong("currency", 0) - hornOfChampPrice;
			prefs.putLong("currency", temp);
			prefs.flush();
			hornOfChampSelected = false;
		}
		
		if(serfsPrice <= prefs.getLong("currency", 0) && serfsSelected){
			prefs.putBoolean("serfs", true);
			prefs.flush();
			long temp = prefs.getLong("currency", 0) - serfsPrice;
			prefs.putLong("currency", temp);
			prefs.flush();
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
		
	}

}
