package com.AngryStickStudios.StickFlick.Entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.AngryStickStudios.StickFlick.StickFlick;

public class WalkingEnemy extends Entity {
	private boolean held;
	private boolean floating;
	private Vector2 pos;
	private Vector2 shadowPos;
	private Vector2 lastPos;
	private Vector2 destination;
	private Vector2 flySpeed;
	
	private Texture entTex;
	Image enemy;

	public WalkingEnemy(String name, int health, int posX, int posY){
		super(name, health);
		
		// Set enemy texture depending on type
		if(name == "basic" || name == "Basic"){
			entTex = new Texture("data/enemyTextures/basicEnemy.png");
		} else{
			entTex = new Texture("data/enemyTextures/error.png");
		}
		
		// Create enemy Image/Actor
		enemy = new Image(entTex);
		enemy.setX(posX);
		enemy.setY(posY);
		enemy.setScale(0.5f);

		
		pos = new Vector2(posX, posY);
		held = false;
		floating = false;
		lastPos = new Vector2(pos);
		flySpeed = new Vector2(0,0);
		destination = FindDestOnWall();
		
		//shadowPos.x = pos.x;
		//shadowPos.y = pos.y;

		
		
	}
	
	public Image getImage(){
		return enemy;
	}
	
	public void setPosition(int posX, int posY){
		pos.x = posX;
		enemy.setX(posX);
		pos.y = posY;
		enemy.setY(posY);
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
			shadowPos.x = pos.x;
			shadowPos.y = lastPos.y;
			return;
		}
		
		//walk towards destination
		shadowPos.x = pos.x;
		shadowPos.y = pos.y;
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
