package com.AngryStickStudios.StickFlick.Entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.AngryStickStudios.StickFlick.StickFlick;

public class Champion extends Entity {
	float scale;
	Entity target;
	float life;
	
	private Texture entTex, shadowTex;
	Image enemy, shadow;

	public Champion(String name, int health, int posX, int posY){
		super(name, health);
		scale = 0.2f;
		
		entTex = new Texture("data/enemyTextures/champion.png");
		shadowTex = new Texture("data/enemyTextures/shadow.png");
		
		// Create enemy Image/Actor
		enemy = new Image(entTex);
		enemy.setX(posX);
		enemy.setY(posY);
		enemy.setScale(scale);
		
		shadow = new Image(shadowTex);
		shadow.setX(posX);
		shadow.setY(posY);
		shadow.setScale(scale*4);
		
		life = 45;
		target = null;
	}
	
	public Entity getTarget(){
		return target;
	}
	
	public void setTarget(Entity inp){
		target = inp;
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
	
	public void Update(float delta){
		//life calculations
		if(life <= 0)
		{
			setIsAlive(false);
			return;
		}
		else
		{
			life -= delta;
		}
		
		if(target == null)
		{
			return;
		}
		
		if((Math.abs(target.getPosition().y) - Math.abs(getPosition().y) < (Gdx.graphics.getHeight() * 0.02f)) && (Math.abs(target.getPosition().x) - Math.abs(getPosition().x) < (Gdx.graphics.getWidth() * 0.04f)))
		{
			//attack
			return;
		}
		else
		{
			Vector2 compVec = new Vector2(target.getPosition().x - getPosition().x, target.getPosition().y - getPosition().y);
			Vector2 normVec = compVec.nor();
			Vector2 walkVec = normVec.scl(30 * delta);
			
			scale = (Gdx.graphics.getHeight() - getPosition().y) / 1000;
			enemy.setScale(scale);
			shadow.setScale(scale);

			setPosition(getPosition().x + walkVec.x, getPosition().y + walkVec.y);

			shadow.setX(enemy.getX());
			shadow.setY(getPosition().y - ((enemy.getHeight() / 2) * scale) - ((shadow.getHeight() / 2) * scale));
		}
		return;
	}
}
