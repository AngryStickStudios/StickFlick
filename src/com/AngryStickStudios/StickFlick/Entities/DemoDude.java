package com.AngryStickStudios.StickFlick.Entities;

import com.AngryStickStudios.StickFlick.Controller.AnimationLoader;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;

public class DemoDude extends WalkingEnemy{

	public DemoDude(String name, int health, AnimationLoader anims, int posX, int posY){
		super(name, health,anims,posX,posY,"demo_walk", 0.5f);
		
	}
	
	public void Update(float delta){
		if(getIsAlive() == false) return;
		
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
			
			scale = ((Gdx.graphics.getHeight() - lastPos.y) / 1000) * scaleMultiplier;
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
		if(peakamt > 0 && frozen == false)
		{
			scale = ((Gdx.graphics.getHeight() - getPosition().y) / 1000) * scaleMultiplier;
			enemy.setScale(scale);
			shadow.setWidth(enemy.getWidth());
			shadow.setScale(scale);
			
			setPosition(getPosition().x, getPosition().y + (20 * delta));
			peakamt -= (20*delta);
			shadow.setX(enemy.getX());
			shadow.setY(getPosition().y + (Gdx.graphics.getHeight() / 100) - ((enemy.getHeight() / 2) * scale) - ((shadow.getHeight() / 2) * scale));
			return;
		}
		
		//walk and shit
		if(getPosition().y > Gdx.graphics.getHeight() * 0.1f && frozen == false){
			Vector2 compVec = new Vector2(destination.x - getPosition().x, destination.y - getPosition().y);
			Vector2 normVec = compVec.nor();
			Vector2 walkVec = normVec.scl(8 * delta);
			
			scale = ((Gdx.graphics.getHeight() - getPosition().y) / 1000) * scaleMultiplier;
			enemy.setScale(scale);
			shadow.setScale(scale);

			setPosition(getPosition().x + walkVec.x, getPosition().y + walkVec.y);

			shadow.setX(enemy.getX());
			shadow.setY(getPosition().y + (Gdx.graphics.getHeight() / 100) - ((enemy.getHeight() / 2) * scale) - ((shadow.getHeight() / 2) * scale));
		}
	}

}
