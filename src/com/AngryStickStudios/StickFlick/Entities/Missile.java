package com.AngryStickStudios.StickFlick.Entities;

import com.AngryStickStudios.StickFlick.Controller.AnimationLoader;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class Missile extends Entity{
	
	Entity target;
	protected Animation currentanim;
    protected TextureRegion currentframe;
    protected TextureRegionDrawable enemyDrawable;
    float animationStateTime;
    float scale;
    Image enemy;
    Vector2 tpos;;
	
	public Missile(String name, int health, AnimationLoader anims, float posX, float posY, Entity tg){
		super(name, health,anims);
		scale = 0.5f;
		
		target = tg;
		tpos = target.getPosition();
		currentanim = anims.getAnim("spell");
        currentframe = currentanim.getKeyFrame(animationStateTime, true);
        enemyDrawable = new TextureRegionDrawable(currentframe);
        animationStateTime = 0;
		
		// Create enemy Image/Actor
		enemy = new Image(enemyDrawable);
		enemy.setX(posX);
		enemy.setY(posY);
		
		scale = ((Gdx.graphics.getHeight() - getPosition().y) / 1000) * 0.5f;
		enemy.setScale(scale);
	}
	
	public Image getImage(){
        return enemy;
	}
	
	public void setPosition(float x, float y){
		enemy.setX(x - ((enemy.getWidth() / 2) * scale));
		enemy.setY(y - ((enemy.getHeight() / 2) * scale));
	}
	
	public Vector2 getPosition()
	{
		return new Vector2(enemy.getX() + ((enemy.getWidth() / 2) * scale), enemy.getY() + ((enemy.getHeight() / 2) * scale));
	}
	
	public void setTarget(Entity tg)
	{
		target = tg;
	}
	
	public Entity getTarget()
	{
		return target;
	}
	
	public void Update(float delta){
		if(getIsAlive() == false) return;
		
		Vector2 destination;
		destination = tpos;
		
		if(Math.abs(destination.y - getPosition().y) < (Gdx.graphics.getHeight() * 0.01f))
        {
                if(Math.abs(destination.x - getPosition().x) < (Gdx.graphics.getWidth() * 0.02f))
                {
                	 if(target != null && target.getIsAlive())
                	 {
                		 if(Math.abs(tpos.y - target.getPosition().y) < (Gdx.graphics.getHeight() * 0.05f))
                	     {
                	                if(Math.abs(tpos.x - target.getPosition().x) < (Gdx.graphics.getWidth() * 0.06f))
                	                {
                	                	target.decreaseHealth(150);
                		 
                	                	if(target.getIsAlive() == false)
                	                	{
                	                		target.setState(0);
                	                		target.setSplatting(1);
                	                	}
                	                }
                	      }
                	 }
                	 
                	 setIsAlive(false);
                }
        }
		
		Vector2 compVec = new Vector2(destination.x - getPosition().x, destination.y - getPosition().y);
        Vector2 normVec = compVec.nor();
        Vector2 walkVec = normVec.scl(250 * delta * Gdx.graphics.getHeight() * 0.002f);
               
        scale = ((Gdx.graphics.getHeight() - getPosition().y) / 1000) * 0.5f;
        enemy.setScale(scale);

        setPosition(getPosition().x + walkVec.x, getPosition().y + walkVec.y);
		
	}
	
	public void Anim(float delta)
	{
		currentframe = currentanim.getKeyFrame(animationStateTime += delta, true);
		enemyDrawable.setRegion(currentframe);
		enemy.setDrawable(enemyDrawable);
	}
}