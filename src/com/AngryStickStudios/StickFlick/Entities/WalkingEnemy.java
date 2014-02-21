package com.AngryStickStudios.StickFlick.Entities;

import com.badlogic.gdx.math.Vector2;
import com.AngryStickStudios.StickFlick.StickFlick;

public class WalkingEnemy implements Entity {
	Bool held;
	Bool floating;
	Vector2 pos;
	Vector2 lastPos;
	Vector2 destination;
	Vector2 flySpeed;
	Shadow shadowent;

	public void WalkingEnemy(Int posX, Int posY) {
		pos = new Vector2(posX, posY);
	}

	@Override
	public void create() {
		health = 10;
		type = "stick_w_n";
		entTex = new Texture("data/stick_w_n.png");
		entSprite = new Sprite(entTex);
		batch = new SpriteBatch();

		held = false;
		self.floating = false;
		self.lastPos = new Vector2(pos);
		self.flySpeed = new Vector2(0,0);
		self.destination = self.FindDestOnWall();

		shadowent = new Shadow();
		shadowent.pos = self.pos;
	}
	
	@Override
	public void render(float delta) {
		if(self.held)
		{
			batch.begin();
			entSprite.draw(batch, pos.x, pos.y);
			batch.end();
			return;
		}

		if(self.floating)
		{
			Vector2 newPos = new Vector2(0,0);
			newPos.x = self.pox.x + self.flySpeed.x;
			if(newPos.x < 8) newPos.x = 8;
			if(newPos.x > Game.width - 8) newPos.x = Game.width - 8;

			if(self.lastPos.y <= self.pos.y + self.flySpeed.y)
			{
				newPos.y = self.lastPos.y;
				floating = false;
				self.pos = newPos;
				self.destination = self.FindDestOnWall();
			}
			else
			{
				newPos.y = pos.y + flySpeed.y;
				pos = NewPos;
			}

			flySpeed.y += 1;
			shadowent.pos.x = self.pos.x;
			shadowet.pos.y = self.lastPos.y;
			batch.begin();
			entSprite.draw(batch, pos.x, pos.y);
			batch.end();
			return;
		}
		
		//walk towards destination
		shadowent.pos.x = self.pos.x;
		shadowet.pos.y = self.pos.y;
		batch.begin();
		entSprite.draw(batch, pos.x, pos.y);
		batch.end();
	}

	public Vector2 FindDestOnWall() {
		//stuff
	}

	public void pickedUp() {
		held = true;
		lastPos = pos;
	}

	public void Released(Vector2 speed) {
		held = false;
		floating = true;
		flySpeed = speed;
	}
}
