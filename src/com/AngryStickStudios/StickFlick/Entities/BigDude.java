package com.AngryStickStudios.StickFlick.Entities;

import com.AngryStickStudios.StickFlick.Controller.AnimationLoader;

public class BigDude extends WalkingEnemy{
	
	public BigDude(String name, int health, AnimationLoader anims, int posX, int posY){
		super(name, health,anims,posX,posY,"bigdude_walk", 3f);
		
	}

}
