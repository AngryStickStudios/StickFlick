package com.AngryStickStudios.StickFlick.Controller;

import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;


public class Image2 extends Image{
		public Image2(Drawable image, float xPos, float yPos, float width, float height){
		super(image);
		this.setX(xPos);
		this.setY(yPos);
		this.setWidth(width);
		this.setHeight(height);
	}
}

