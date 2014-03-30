package com.AngryStickStudios.StickFlick.Entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.AngryStickStudios.StickFlick.StickFlick;

public class Champion extends Entity {
	float scale, mscale;
	Entity target;
	float life, attackdelay, attackdelay2;
	
	private Texture shadowTex;
	private Animation currentanim, walk_d, walk_u, walk_l, walk_r, attack_r, attack_l, attack_u, attack_d;
	private TextureRegion currentframe;
	private TextureRegionDrawable enemyDrawable;
	float animationStateTime;
	
	Image enemy, shadow;

	public Champion(String name, int health, int posX, int posY){
		super(name, health);
		scale = 0.1f;
		mscale = 1f;
		shadowTex = new Texture("data/enemyTextures/shadow.png");
		
		animationStateTime = 0;
		walk_d = setupAnim("data/enemyTextures/hotc_walkf.png", 6, 5, (float) 0.025);
		walk_u = setupAnim("data/enemyTextures/hotc_walkb.png", 6, 5, (float) 0.025);
		walk_l = setupAnim("data/enemyTextures/hotc_walkl.png", 6, 5, (float) 0.025);
		walk_r = setupAnim("data/enemyTextures/hotc_walkr.png", 6, 5, (float) 0.025);
		attack_d = setupAnim("data/enemyTextures/hotc_attackf.png", 10, 5, (float) 0.025);
		attack_u = setupAnim("data/enemyTextures/hotc_attackb.png", 10, 5, (float) 0.025);
		attack_l = setupAnim("data/enemyTextures/hotc_attackl.png", 10, 5, (float) 0.025);
		attack_r = setupAnim("data/enemyTextures/hotc_attackr.png", 10, 5, (float) 0.025);
		currentanim = walk_d;
		currentframe = walk_d.getKeyFrame(animationStateTime, true);
		enemyDrawable = new TextureRegionDrawable(currentframe);
		
		// Create enemy Image/Actor
		enemy = new Image(enemyDrawable);
		enemy.setX(posX);
		enemy.setY(posY);
		enemy.setScale(scale);
		
		shadow = new Image(shadowTex);
		shadow.setX(posX);
		shadow.setY(posY);
		shadow.setScale(scale*4);
		
		life = 45f;
		attackdelay = 0;
		attackdelay2 = 0;
		target = null;
	}
	
	public Entity getTarget(){
		return target;
	}
	
	public void setTarget(Entity inp){
		target = inp;
	}
	
	public Image getImage(){
		return enemy;
	}

	
	public Image getShadow(){
		return shadow;
	}
	
	public void setPosition(float x, float y){
		enemy.setX(x - ((enemy.getWidth() / 2) * (scale / 2)));
		enemy.setY(y - ((enemy.getHeight() / 2) * scale));
	}
	
	public Vector2 getPosition()
	{
		return new Vector2(enemy.getX() + ((enemy.getWidth() / 2) * (scale / 2)), enemy.getY() + ((enemy.getHeight() / 2) * scale));
	}
	
	public Vector2 getGroundPosition()
	{
		return new Vector2(enemy.getX() + ((enemy.getWidth() / 2) * (scale / 2)), enemy.getY());
	}
	
	public Vector2 getSize(){
		return new Vector2(enemy.getWidth() * (scale / 2), enemy.getHeight() * scale);
	}
	
	public void Update(float delta){
		//life calculations
		if(life <= 0)
		{
			setIsAlive(false);
			return;
		}
		else
		{
			life -= Gdx.graphics.getDeltaTime();
		}
		
		if(target == null || target.getIsAlive() == false)
		{
			return;
		}
		
		if(attackdelay > 0)
		{
			attackdelay -= Gdx.graphics.getDeltaTime();
			if(attackdelay <= 0 && target.getIsAlive())
			{
				target.decreaseHealth(200);
				target.setState(0);
				target.setSplatting(1);
			}
			return;
		}
		
		if(attackdelay2 > 0)
		{
			attackdelay2 -= Gdx.graphics.getDeltaTime();
			return;
		}
		
		if(Math.abs(target.getGroundPosition().y - getGroundPosition().y) < (Gdx.graphics.getHeight() * 0.03f))
		{
			if(Math.abs(target.getGroundPosition().x - getGroundPosition().x) < (Gdx.graphics.getWidth() * 0.04f))
			{
				Vector2 compVec = new Vector2(target.getGroundPosition().x - getGroundPosition().x, target.getGroundPosition().y - getGroundPosition().y);
				Vector2 normVec = compVec.nor();
				Vector2 walkVec = normVec.scl(100 * delta);
				
				if(Math.abs(walkVec.x) >= Math.abs(walkVec.y))
				{
					if(walkVec.x < 0)
						currentanim = attack_l;
					else
						currentanim = attack_r;
				}
				else
				{
					if(walkVec.y < 0)
						currentanim = attack_d;
					else
						currentanim = attack_u;
				}
				
				attackdelay = 0.5f;
				attackdelay2 = 1.0f;
				animationStateTime = 0;
				return;
			}
		}
			
		Vector2 compVec;
		if(!target.onGround())
		{
			if(Math.abs(target.getGroundPosition().x - getGroundPosition().x) < (Gdx.graphics.getWidth() * 0.03f))
			{
				if(Math.abs(target.getLastPos().y - getGroundPosition().y) < (Gdx.graphics.getHeight() * 0.02f))
				{
					return;
				}
			}
			compVec = new Vector2(target.getGroundPosition().x - getGroundPosition().x, target.getLastPos().y - getGroundPosition().y);
		}
		else
		{
			compVec = new Vector2(target.getGroundPosition().x - getGroundPosition().x, target.getGroundPosition().y - getGroundPosition().y);
		}
		Vector2 normVec = compVec.nor();
		Vector2 walkVec = normVec.scl(150 * delta);
		//Vector2 walkVec = normVec.scl(600 * delta);
		
		if(Math.abs(walkVec.x) >= Math.abs(walkVec.y))
		{
			if(walkVec.x < 0)
				currentanim = walk_l;
			else
				currentanim = walk_r;
		}
		else
		{
			if(walkVec.y < 0)
				currentanim = walk_d;
			else
				currentanim = walk_u;
		}
			
		scale = ((Gdx.graphics.getHeight() - getPosition().y) / 1000) * mscale;
		enemy.setScale(scale);
		shadow.setScale((float) ((scale / mscale) * 2.5));

		setPosition(getPosition().x + walkVec.x, getPosition().y + walkVec.y);

		shadow.setX(getGroundPosition().x - ((shadow.getWidth() / 2) * (scale)));
		shadow.setY(-4 + getPosition().y - ((enemy.getHeight() / 2) * (scale) - ((shadow.getHeight() / 2) * (scale))));
		return;
	}
	
	public void Anim(float delta)
	{
		if(attackdelay2 <= 0)
		{
			currentframe = currentanim.getKeyFrame(animationStateTime += delta, true);
			enemyDrawable.setRegion(currentframe);
			enemy.setDrawable(enemyDrawable);
		}
		else
		{
			currentframe = currentanim.getKeyFrame(animationStateTime += delta, false);
			enemyDrawable.setRegion(currentframe);
			enemy.setDrawable(enemyDrawable);
		}
		
	}
}
