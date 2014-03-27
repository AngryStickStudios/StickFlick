package com.AngryStickStudios.StickFlick.Entities;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

public abstract class Entity {
	private String name;
	private float healthMax;
	private float healthCurrent;
	private boolean isAlive;

	public Entity(String name, int healthMax){
		this.name = name;
		this.healthMax = healthMax;
		this.healthCurrent = healthMax;
		this.isAlive = true;
				
	}
	
	public void Released(Vector2 speed) {
	}
	
	public void freeze(){
	}
	
	public void unfreeze(){
	}
	
	public void pickedUp() {
	}
	
	public Vector2 getPosition()
	{
		return new Vector2();
	}
	
	public Vector2 getSize(){
		return new Vector2();
	}

	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}
	
	
	public float getHealthMax() {
		return healthMax;
	}


	public void setHealthMax(float healthMax) {
		this.healthMax = healthMax;
	}
	
	public float getHealthCurrent() {
		return healthCurrent;
	}


	public void setHealthCurrent(float healthCurrent) {
		this.healthCurrent = healthCurrent;
	}
	
	
	public boolean getIsAlive(){
		return isAlive;	
	}
	
	public void Update(float delta){
	}
	
	public Image getImage(){
		return new Image();
	}
	
	public Image getShadow(){
		return new Image();
	}
	
	public void setIsAlive(boolean isAlive){
		this.isAlive = isAlive;
	}
	
	public void increaseHealth(int x){
		if(healthCurrent + x >= healthMax)
			healthCurrent = healthMax;
		else
			healthCurrent = healthCurrent + x;
			
	}
	
	public void decreaseHealth(int x){
		if(healthCurrent - x <= 0){
			healthCurrent = 0;
			isAlive = false;
		}
		else
			healthCurrent = healthCurrent - x;
	}

}


