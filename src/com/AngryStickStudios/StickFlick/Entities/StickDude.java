package com.AngryStickStudios.StickFlick.Entities;

import com.AngryStickStudios.StickFlick.Controller.AnimationLoader;

public class StickDude extends WalkingEnemy{
	
	public StickDude(String name, int health, AnimationLoader anims, int posX, int posY){
		super(name, health,anims,posX,posY,"dude_walk", 0.5f);
		
	}
}
