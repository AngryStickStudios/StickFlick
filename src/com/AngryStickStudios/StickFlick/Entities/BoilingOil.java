package com.AngryStickStudios.StickFlick.Entities;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.AngryStickStudios.StickFlick.Controller.AnimationLoader;

public class BoilingOil extends Entity {
	float scale, mscale;

	private Animation currentanim;
	private TextureRegion currentframe;
	private TextureRegionDrawable powerUpDrawable;
	float animationStateTime;

	Image bOil;

	public BoilingOil(AnimationLoader anims, float posX, float posY){
		super("boiling_oil", 0, anims);
		scale = 0.5f;
		mscale = 1f;

		animationStateTime = 0;
		currentanim = anims.getAnim("boilingOil");
		currentframe = currentanim.getKeyFrame(animationStateTime, true);
		powerUpDrawable = new TextureRegionDrawable(currentframe);

		// Create enemy Image/Actor
		bOil = new Image(powerUpDrawable);
		bOil.setX(posX);
		bOil.setY(posY);
		bOil.setScale(scale);

	}

	public Image getImage(){
		return bOil;
	}

	public void setPosition(float x, float y){
		bOil.setX(x - ((bOil.getWidth() / 2) * (scale / 2)));
		bOil.setY(y - ((bOil.getHeight() / 2) * scale));
	}

	public Vector2 getPosition()
	{
		return new Vector2(bOil.getX() + ((bOil.getWidth() / 2) * (scale / 2)), bOil.getY() + ((bOil.getHeight() / 2) * scale));
	}


	public Vector2 getSize(){
		return new Vector2(bOil.getWidth() * (scale / 2), bOil.getHeight() * scale);
	}



	public void Anim(float delta)
	{
		currentframe = currentanim.getKeyFrame(animationStateTime += delta, true);
		powerUpDrawable.setRegion(currentframe);
		bOil.setDrawable(powerUpDrawable);
	}
}
