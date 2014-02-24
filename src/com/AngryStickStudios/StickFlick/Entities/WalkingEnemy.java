package com.AngryStickStudios.StickFlick.Entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.AngryStickStudios.StickFlick.StickFlick;

public class WalkingEnemy extends Entity {
	private boolean held, floating;
	private Vector2 lastPos, destination, flySpeed;
	
	private Texture entTex, shadowTex;
	Image enemy, shadow;

	public WalkingEnemy(String name, int health, int posX, int posY){
		super(name, health);
		
		// Set enemy texture depending on type
		if(name == "basic" || name == "Basic"){
			entTex = new Texture("data/enemyTextures/basicEnemy.png");
		} else{
			entTex = new Texture("data/enemyTextures/error.png");
		}
		
		shadowTex = new Texture("data/enemyTextures/error.png");
		
		// Create enemy Image/Actor
		enemy = new Image(entTex);
		enemy.setX(posX);
		enemy.setY(posY);
		enemy.setScale(0.5f);
		
		shadow = new Image(shadowTex);
		shadow.setX(posX);
		shadow.setY(posY);
		shadow.setScale(0.5f);
		
		held = false;
		floating = false;
		FindDestOnWall();
		
	}
	
	public Image getImage(){
		return enemy;
	}
	
	public void setPosition(float x, float y){
		enemy.setX(x);
		enemy.setY(y);
	}
	
	public Vector2 getPosition()
	{
		return new Vector2(enemy.getX(), enemy.getY());
	}
	
	public void Update(float delta){
		if(held)
		{
			return;
		}

		if(floating)
		{
			Vector2 newPos = new Vector2(0,0);
			newPos.x = enemy.getX() + flySpeed.x;
			if(newPos.x < 8) newPos.x = 8;
			if(newPos.x > Gdx.graphics.getWidth() - 8) newPos.x = Gdx.graphics.getWidth() - 8;

			if(lastPos.y <= enemy.getY() + flySpeed.y)
			{
				newPos.y = lastPos.y;
				floating = false;
				setPosition(newPos.x, newPos.y);
				FindDestOnWall();
			}
			else
			{
				newPos.y = enemy.getY() + flySpeed.y;
				setPosition(newPos.x, newPos.y);
			}

			flySpeed.y += 1;
			shadow.setX(enemy.getX());
			shadow.setY(lastPos.y);
			return;
		}
		
		//walk and shit
		Vector2 compVec = new Vector2(destination.x - enemy.getX(), destination.y - enemy.getY());
		Vector2 normVec = compVec.nor();
		Vector2 walkVec = normVec.scl(50 * delta);
		//System.out.println("destination = " + destination.x + " " + destination.y);
		enemy.setX(enemy.getX() + walkVec.x);
		enemy.setY(enemy.getY() + walkVec.y);
		
		if(enemy.getY() < 20) enemy.setY(20);
		
		//walk towards destination
		shadow.setX(enemy.getX());
		shadow.setY(enemy.getY());
	}

	public void FindDestOnWall() {
		Vector2 straightDown = new Vector2(enemy.getX(), 20);
		int adjAmt = (int) Math.round(((Math.random() * (Gdx.graphics.getWidth() / 5)) - (Gdx.graphics.getWidth() / 10)));
		System.out.println(adjAmt);
		straightDown.x += adjAmt;
		destination = straightDown;
	}

	public void pickedUp() {
		held = true;
		lastPos = new Vector2(enemy.getX(), enemy.getY());
	}

	public void Released(Vector2 speed) {
		held = false;
		floating = true;
		flySpeed = speed;
	}
}
