package com.AngryStickStudios.StickFlick.Controller;

import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

public class Button2 extends Button{
	public Button2(Drawable up, Drawable down, float xPos, float yPos, float width, float height){
		super(up, down);
		this.setX(xPos);
		this.setY(yPos);
		this.setWidth(width);
		this.setHeight(height);
	}
}
