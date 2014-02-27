package com.AngryStickStudios.StickFlick.Entities;


public abstract class Entity {
	private String name;
	private int healthMax;
	private int healthCurrent;
	private boolean isAlive;

	public Entity(String name, int healthMax){
		this.name = name;
		this.healthMax = healthMax;
		this.healthCurrent = healthMax;
		this.isAlive = true;
				
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}
	
	
	public int getHealthMax() {
		return healthMax;
	}


	public void setHealthMax(int healthMax) {
		this.healthMax = healthMax;
	}
	
	public int getHealthCurrent() {
		return healthCurrent;
	}


	public void setHealthCurrent(int healthCurrent) {
		this.healthCurrent = healthCurrent;
	}
	
	
	public boolean getIsAlive(){
		return isAlive;	
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


