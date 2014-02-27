package com.AngryStickStudios.StickFlick.Entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.AngryStickStudios.StickFlick.StickFlick;

public class Castle extends Entity {
	private int numEnAtWall;
	
	Image castleOnly;
	
	public Castle(String name, int health){
		super(name,health);
		numEnAtWall = 0;
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
	
	public void Update(Castle castle){
		int currentH = castle.getHealthCurrent();
		
		int WallDmg = numEnAtWall * 100;
		
		castle.setHealthCurrent(currentH - WallDmg);
	}
}
