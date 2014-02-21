package com.AngryStickStudios.StickFlick.Entities;

import com.badlogic.gdx.math.Vector2;
import com.AngryStickStudios.StickFlick.StickFlick;

public class Shadow implements Entity {
	Vector2 pos;

	@Override
	public void create() {
		entTex = new Texture("data/shadow.png");
		entSprite = new Sprite(entTex);
		batch = new SpriteBatch();
		pos = new Vector2(0,0);
	}
	
	@Override
	public void render(float delta) {
		batch.begin();
		entSprite.draw(batch, pos.x, pos.y);
		batch.end();
	}
}
