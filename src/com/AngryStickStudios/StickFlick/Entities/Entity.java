package com.AngryStickStudios.StickFlick.Entities;

import com.badlogic.gdx.Game;
import com.AngryStickStudios.StickFlick.StickFlick;

public class Entity {
	Int health;
	String type;
	Texture entTex;
	Sprite entSprite;
	SpriteBatch batch;

	public Entity(StickFlick game){
		this.game = game;
	}

	@Override
	public void create() {
		health = 10;
		type = "stick_w_n";
		entTex = new Texture("data/stick_w_n.png");
		entSprite = new Sprite(entTex);
		batch = new SpriteBatch();
	}
	
	@Override
	public void render(float delta) {
		batch.begin();
		entSprite.draw(batch);
		batch.end();
	}

}
