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
	private boolean held, floating, frozen/*, landed*/;
	private Vector2 lastPos, destination, flySpeed;
	private int moveBackSpeed, maxHeight = 2;
	private float peakamt =  .05f * Gdx.graphics.getHeight();
	
	private Texture entTex, shadowTex;
	Image enemy, shadow;

	public WalkingEnemy(String name, int health, int posX, int posY){
		super(name, health);
		scale = 0.5f;
		lastPos = new Vector2(posX, posY);
		
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
		frozen = false;
		moveBackSpeed = 0;
		FindDestOnWall();
	}
	
	public void freeze(){
		frozen = true;
	}
	
	public void unfreeze(){
		frozen = false;
	}
	
	public Image getImage(){
		return enemy;
	}
	
	public Image getShadow(){
		return shadow;
	}
	
	public Vector2 getLastPos(){
		return lastPos;
	}
	
	public boolean onGround(){
		if(held || floating)
		{
			return false;
		}
		return true;
	}
	
	public void setPosition(float x, float y){
		enemy.setX(x - ((enemy.getWidth() / 2) * scale));
		enemy.setY(y - ((enemy.getHeight() / 2) * scale));
	}
	
	public Vector2 getPosition()
	{
		return new Vector2(enemy.getX() + ((enemy.getWidth() / 2) * scale), enemy.getY() + ((enemy.getHeight() / 2) * scale));
	}
	
	public Vector2 getGroundPosition()
	{
		return new Vector2(enemy.getX() + ((enemy.getWidth() / 2) * scale), enemy.getY());
	}
	
	public Vector2 getSize(){
		return new Vector2(enemy.getWidth() * scale, enemy.getHeight() * scale);
	}
	
	public void FindDestOnWall() {

		Vector2 straightDown = getPosition();
		straightDown.y = Gdx.graphics.getHeight() * 0.04f;

		int adjAmt = (int) Math.round(((Math.random() * (Gdx.graphics.getWidth() / 5)) - (Gdx.graphics.getWidth() / 10)));
		System.out.println("Adjust Amount: " + adjAmt);
		straightDown.x += adjAmt;
		
		int tempWidth = Gdx.graphics.getWidth();
		if(straightDown.x > tempWidth * 0.98f){
			straightDown.x = tempWidth * 0.98f;
		}
		if(straightDown.x < tempWidth * 0.02f){
			straightDown.x = tempWidth * 0.02f;
		}
		destination = straightDown;
	}

	public void pickedUp() {
		held = true;
		if(floating == false)
		{
			lastPos.x = getPosition().x;
			lastPos.y = getPosition().y;
		}
	}

	public void Released(Vector2 speed) {
		held = false;
		floating = true;
		flySpeed = new Vector2(speed);
		moveBackSpeed = Math.round(flySpeed.y / 10);
		System.out.println("MoveBackSpeed: " + moveBackSpeed);
	}
	
	public void Update(float delta){
		if(held)
		{
			setPosition(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY());
			shadow.setX(enemy.getX());
			shadow.setY(lastPos.y - ((enemy.getHeight() / 2) * scale) - ((shadow.getHeight() / 2) * scale));
			
			if(getPosition().y <= lastPos.y)
			{
				setPosition(Gdx.input.getX(), lastPos.y);
			}
			return;
		}

		if(floating)
		{
			Vector2 newPos = new Vector2(0,0);
			newPos.x = getPosition().x + flySpeed.x;
			
			if(lastPos.y < Gdx.graphics.getHeight() / 1.8f ){
				lastPos.y = lastPos.y + ((Gdx.graphics.getHeight() / 500) * moveBackSpeed);
			}
			
			scale = (Gdx.graphics.getHeight() - lastPos.y) / 1000;
			enemy.setScale(scale);
			shadow.setScale(scale);
			
			if(newPos.x < Gdx.graphics.getWidth() * 0.01f){
				newPos.x = Gdx.graphics.getWidth() * 0.01f;
			}
			if(newPos.x > Gdx.graphics.getWidth() * 0.99f){
				newPos.x = Gdx.graphics.getWidth() * 0.99f;
			}

			//has landed back on the homeland
			if(lastPos.y >= getPosition().y + flySpeed.y)
			{
				newPos.y = lastPos.y;
				floating = false;
				//landed = true;
				setPosition(newPos.x, newPos.y);
				
				Damage(-flySpeed.y);
				FindDestOnWall();
			}
			else
			{
				newPos.y = getPosition().y + (flySpeed.y * 0.5f);
				setPosition(newPos.x, newPos.y);
			}

			flySpeed.y -= 0.3f;
			shadow.setX(enemy.getX());
			shadow.setY(lastPos.y - ((enemy.getHeight() / 2) * scale) - ((shadow.getHeight() / 2) * scale));
			return;
		}
		
		//walk up... and shit
		if(peakamt > 0)
		{
			scale = (Gdx.graphics.getHeight() - getPosition().y) / 1000;
			enemy.setScale(scale);
			shadow.setScale(scale);
			
			setPosition(getPosition().x, getPosition().y + (20 * delta));
			peakamt -= (20*delta);
			shadow.setX(enemy.getX());
			shadow.setY(getPosition().y - ((enemy.getHeight() / 2) * scale) - ((shadow.getHeight() / 2) * scale));
			return;
		}
		
		//walk and shit
		if(getPosition().y > Gdx.graphics.getHeight() * 0.1f && frozen == false){
			Vector2 compVec = new Vector2(destination.x - getPosition().x, destination.y - getPosition().y);
			Vector2 normVec = compVec.nor();
			Vector2 walkVec = normVec.scl(8 * delta);
			
			scale = (Gdx.graphics.getHeight() - getPosition().y) / 1000;
			enemy.setScale(scale);
			shadow.setScale(scale);

			setPosition(getPosition().x + walkVec.x, getPosition().y + walkVec.y);

			shadow.setX(enemy.getX());
			shadow.setY(getPosition().y - ((enemy.getHeight() / 2) * scale) - ((shadow.getHeight() / 2) * scale));
		}
	}

	public void Damage(float fallingVelocity){
		//can change the dmgAmt ratio to whatever
		int dmgAmt = (int)fallingVelocity * 4;
		decreaseHealth(dmgAmt);
		System.out.println("Falling Velocity: " + fallingVelocity);
		System.out.println("Damage Amount: " + dmgAmt);
		System.out.println("Stickman Health: " + getHealthCurrent());
		if(getIsAlive() != true)
			System.out.println("An enemy reached zero heath! Victory dance!");
	}
}
