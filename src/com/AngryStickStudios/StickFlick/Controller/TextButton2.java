package com.AngryStickStudios.StickFlick.Controller;

import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

public class TextButton2 extends TextButton {
	
	public TextButton2(String text, TextButtonStyle style, float xPos, float yPos, float width, float height){
		super(text, style);
		this.setWidth(width);
		this.setHeight(height);
		this.setX(xPos);
		this.setY(yPos);
	}
}
