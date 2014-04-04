package com.AngryStickStudios.StickFlick.Controller;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.utils.BaseDrawable;

public class AnimationDrawable extends BaseDrawable {
	public final Animation anim;

	private float stateTime = 0;

	public AnimationDrawable(Animation anim)
	{
	    this.anim = anim;
	    setMinWidth(anim.getKeyFrame(0).getRegionWidth());
	    setMinHeight(anim.getKeyFrame(0).getRegionHeight());
	}

	public void act(float delta)
	{
	    stateTime += delta;
	    System.out.println("stateTime = " + stateTime);
	}

	public void reset()
	{
	    stateTime = 0;
	}

	public void draw(SpriteBatch batch, float x, float y, float width, float height)
	{
	    batch.draw(anim.getKeyFrame(stateTime, true), x, y, width, height);
	}
}
