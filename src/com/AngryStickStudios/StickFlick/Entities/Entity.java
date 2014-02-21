package com.AngryStickStudios.StickFlick.Entities;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.AngryStickStudios.StickFlick.StickFlick;

public abstract class Entity {
	private int health;
	private String name;
	

	public Entity(String name, int health){
		this.name = name;
		this.health = health;
				
	}


	public int getHealth() {
		return health;
	}


	public void setHealth(int health) {
		this.health = health;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public Texture getEntTex() {
		return entTex;
	}


	public void setEntTex(Texture entTex) {
		this.entTex = entTex;
	}


}


