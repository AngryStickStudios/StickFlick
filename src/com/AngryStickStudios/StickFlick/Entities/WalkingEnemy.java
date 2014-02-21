package com.AngryStickStudios.StickFlick.Entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.AngryStickStudios.StickFlick.StickFlick;

public class WalkingEnemy extends Entity {
	boolean held;
	boolean floating;
	Rectangle bounds;
	Vector2 pos;
	Vector2 lastPos;
	Vector2 destination;
	Vector2 flySpeed;
	Shadow shadowent;
	private Texture entTex;

	public WalkingEnemy(String name, int health, int posX, int posY){
		super(name, health);

		pos = new Vector2(posX, posY);
		held = false;
		floating = false;
		lastPos = new Vector2(pos);
		flySpeed = new Vector2(0,0);
		destination = FindDestOnWall();
		shadowent = new Shadow();
		shadowent.pos = pos;
		
		
		if(name == "basic" || name == "Basic"){
			entTex = new Texture("data/basicEnemy.png");
		}
	}
	
	public void Update(float delta){
		if(held)
		{
			return;
		}

		if(floating)
		{
			Vector2 newPos = new Vector2(0,0);
			newPos.x = pos.x + flySpeed.x;
			if(newPos.x < 8) newPos.x = 8;
			if(newPos.x > Gdx.graphics.getWidth() - 8) newPos.x = Gdx.graphics.getWidth() - 8;

			if(lastPos.y <= pos.y + flySpeed.y)
			{
				newPos.y = lastPos.y;
				floating = false;
				pos = newPos;
				destination = FindDestOnWall();
			}
			else
			{
				newPos.y = pos.y + flySpeed.y;
				pos = newPos;
			}

			flySpeed.y += 1;
			shadowent.pos.x = pos.x;
			shadowent.pos.y = lastPos.y;
			return;
		}
		
		//walk towards destination
		shadowent.pos.x = pos.x;
		shadowent.pos.y = pos.y;
	}

	public Vector2 FindDestOnWall() {
		return new Vector2();
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
