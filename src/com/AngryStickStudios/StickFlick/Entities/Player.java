package com.AngryStickStudios.StickFlick.Entities;

import com.AngryStickStudios.StickFlick.Controller.AnimationLoader;

public class Player extends Entity {
	private int numEnAtWall;
	
	public Player(String name, int health, AnimationLoader anims){
		super(name,health, anims);
		super.setCastleMaxHealth(health);
		numEnAtWall = 0;
	}
	
	public void setEnAtWall(int num){
		numEnAtWall = num;
	}
	
	public void addEnToWall(){
		numEnAtWall++;
	}
	
	public void removeEnFromWall(int x){
		if(numEnAtWall - x <= 0)
			numEnAtWall = 0;
		else
			numEnAtWall = numEnAtWall - x;
	}
	
	public void Update(){
		float WallDmg = getCastleMaxHealth() * ((float)numEnAtWall / 10000); //int of numEnAtWall made to be 0.1% of castle health
		if(getHealthCurrent() - WallDmg <= 0){
			setHealthCurrent(0);
			setIsAlive(false);
		}
		else{
			setHealthCurrent(getHealthCurrent() - WallDmg);
		}
	}
}
