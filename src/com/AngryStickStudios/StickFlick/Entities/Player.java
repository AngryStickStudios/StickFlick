package com.AngryStickStudios.StickFlick.Entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.AngryStickStudios.StickFlick.StickFlick;

public class Player extends Entity {
	private int numEnAtWall;
	
	public Player(String name, int health){
		super(name,health);
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
		int WallDmg = numEnAtWall;
		
		if(getHealthCurrent() - WallDmg <= 0){
			setHealthCurrent(0);
			setIsAlive(false);
		}
		else{
			setHealthCurrent(getHealthCurrent() - WallDmg);
		}
		//System.out.println("Health after EnAtWall: " + getHealthCurrent());
	}
}
