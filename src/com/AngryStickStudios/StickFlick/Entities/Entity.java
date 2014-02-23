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
		//Will add
	}
	
	public void decreaseHealth(int x){
		//Will add
	}

}


