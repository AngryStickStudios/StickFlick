package com.AngryStickStudios.StickFlick.Entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.AngryStickStudios.StickFlick.StickFlick;

public class WalkingEnemy extends Entity {
	float scale;
	private boolean held, floating;
	private Vector2 lastPos, destination, flySpeed;
	
	private Texture entTex, shadowTex;
	Image enemy, shadow;

	public WalkingEnemy(String name, int health, int posX, int posY){
		super(name, health);
		scale = 0.5f;
		
		// Set enemy texture depending on type
		if(name == "basic" || name == "Basic"){
			entTex = new Texture("data/enemyTextures/basicEnemy.png");
		} else{
			entTex = new Texture("data/enemyTextures/error.png");
		}
		
		shadowTex = new Texture("data/enemyTextures/shadow.png");
		
		// Create enemy Image/Actor
		enemy = new Image(entTex);
		enemy.setX(posX);
		enemy.setY(posY);
		enemy.setScale(scale);
		
		shadow = new Image(shadowTex);
		shadow.setX(posX);
		shadow.setY(posY);
		shadow.setScale(scale);
		
		held = false;
		floating = false;
		FindDestOnWall();
	}
	
	public Image getImage(){
		return enemy;
	}
	
	public Image getShadow(){
		return shadow;
	}
	
	public void setPosition(float x, float y){
		enemy.setX(x - ((enemy.getWidth() / 2) * scale));
		enemy.setY(y - ((enemy.getHeight() / 2) * scale));
	}
	
	public Vector2 getPosition()
	{
		return new Vector2(enemy.getX() + ((enemy.getWidth() / 2) * scale), enemy.getY() + ((enemy.getHeight() / 2) * scale));
	}
	
	public Vector2 getSize(){
		return new Vector2(enemy.getWidth() * scale, enemy.getHeight() * scale);
	}
	
	public void FindDestOnWall() {
		Vector2 straightDown = getPosition();
		straightDown.y = Gdx.graphics.getHeight() * 0.04f;
		
		int adjAmt = (int) Math.round(((Math.random() * (Gdx.graphics.getWidth() / 5)) - (Gdx.graphics.getWidth() / 10)));
		System.out.println(adjAmt);
		straightDown.x += adjAmt;
		if(straightDown.x > Gdx.graphics.getWidth() * 0.98f){
			straightDown.x = Gdx.graphics.getWidth() * 0.98f;
		}
		if(straightDown.x < Gdx.graphics.getWidth() * 0.02f){
			straightDown.x = Gdx.graphics.getWidth() * 0.02f;
		}
		destination = straightDown;
	}

	public void pickedUp() {
		held = true;
		lastPos = new Vector2(getPosition().x, getPosition().y);
	}

	public void Released(Vector2 speed) {
		held = false;
		floating = true;
		flySpeed = new Vector2(speed);
		System.out.println("->> " + flySpeed.x);
	}
	
	public void Update(float delta){
		if(held)
		{
			setPosition(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY());
			shadow.setX(enemy.getX());
			shadow.setY(lastPos.y - ((enemy.getHeight() / 8) * scale));
			
			if(getPosition().y <= lastPos.y)
			{
				setPosition(Gdx.input.getX(), lastPos.y + ((enemy.getHeight() / 2) * scale));
			}
			return;
		}

		if(floating)
		{
			Vector2 newPos = new Vector2(0,0);
			newPos.x = getPosition().x + flySpeed.x;
			
			if(newPos.x < 8) newPos.x = 8;
			if(newPos.x > Gdx.graphics.getWidth() - 16) newPos.x = Gdx.graphics.getWidth() - 16;

			if(lastPos.y >= getPosition().y + flySpeed.y)
			{
				newPos.y = lastPos.y;
				floating = false;
				setPosition(newPos.x, newPos.y);

				FindDestOnWall();
			}
			else
			{
				newPos.y = getPosition().y + flySpeed.y;
				setPosition(newPos.x, newPos.y);
			}

			flySpeed.y -= 1;
			shadow.setX(enemy.getX());
			shadow.setY(lastPos.y - ((enemy.getHeight() / 8) * scale));
			return;
		}
		
		//walk and shit
		if(getPosition().y > Gdx.graphics.getHeight() * 0.05f){
			Vector2 compVec = new Vector2(destination.x - getPosition().x, destination.y - getPosition().y);
			Vector2 normVec = compVec.nor();
			Vector2 walkVec = normVec.scl(50 * delta);


			//System.out.println("destination = " + destination.x + " " + destination.y);
			setPosition(getPosition().x + walkVec.x, getPosition().y + walkVec.y);


			if(getPosition().y < 20){
				setPosition(getPosition().x, 20);
			}

			shadow.setX(enemy.getX());
			shadow.setY(enemy.getY() - ((enemy.getHeight() / 8) * scale));
		}
	}
}
