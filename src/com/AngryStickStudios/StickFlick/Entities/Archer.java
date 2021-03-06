package com.AngryStickStudios.StickFlick.Entities;

import com.AngryStickStudios.StickFlick.Controller.AnimationLoader;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class Archer extends Entity{
	
	Preferences prefs = Gdx.app.getPreferences("Preferences");
	
	float scale;
	float lastShot = 0;
	boolean firedShot = false;
	Entity target = null;
	protected Animation currentanim;
    protected TextureRegion currentframe;
    protected TextureRegionDrawable enemyDrawable;
    float animationStateTime, SFXVolume;
    Sound arrowShot;
    
    Image enemy;
	
	public Archer(String name, int health, AnimationLoader anims, int posX, int posY){
		super(name, health,anims);
        
        arrowShot= Gdx.audio.newSound(Gdx.files.internal("data/sounds/arrow_shot.mp3"));
        SFXVolume = prefs.getInteger("SFXVolume") * 0.01f;
		
		currentanim = anims.getAnim("archerright");
        currentframe = currentanim.getKeyFrame(animationStateTime, true);
        enemyDrawable = new TextureRegionDrawable(currentframe);
		
		// Create enemy Image/Actor
		enemy = new Image(enemyDrawable);
		enemy.setX(posX);
		enemy.setY(posY);
		
		scale = ((Gdx.graphics.getHeight() - getPosition().y) / 2000);
		enemy.setScale(scale);
	}
	
	public Entity getTarget()
	{
		return target;
	}
	
	public Image getImage(){
        return enemy;
	}
	
	public void setTarget(Entity tg)
	{
		target = tg;
		animationStateTime = 0;
		return;
	}
	
	public void Update(float delta){
		if(getIsAlive() == false) return;
		
		if(target != null && target.getIsAlive())
		{
			if(target.getPosition().x < getPosition().x)
			{
				currentanim = anims.getAnim("archerleft");
			}
			else
			{
				currentanim = anims.getAnim("archerright");
			}
		}
		
		if(!firedShot)
		{
			if(lastShot > 1 && target != null && target.getIsAlive())
			{
				arrowShot.play(SFXVolume * 0.25f);
				lastShot = 0;
				firedShot = true;
				animationStateTime = 0;
			}
			else
			{
				lastShot += delta;
			}
		}
	}
	
	public void setPosition(float x, float y){
        enemy.setX(x - ((enemy.getWidth() / 2) * scale));
        enemy.setY(y - ((enemy.getHeight() / 2) * scale));
}

	public Vector2 getPosition()
	{
        	return new Vector2(enemy.getX() + ((enemy.getWidth() / 2) * scale), enemy.getY() + ((enemy.getHeight() / 2) * scale));
	}
	
	public Vector2 getSize(){
        return new Vector2(enemy.getWidth() * scale, enemy.getHeight() * scale);
	}
	
	public String getProjFired()
    {
    	if(firedShot)
    	{
    		firedShot = false;
    		return "arrow";
    	}
    	return "null";
    }
	
	public void Anim(float delta)
	{
		currentframe = currentanim.getKeyFrame(animationStateTime += delta, false);
		enemyDrawable.setRegion(currentframe);
		enemy.setDrawable(enemyDrawable);
	}
}