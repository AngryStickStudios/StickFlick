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
import com.AngryStickStudios.StickFlick.StickFlick;

public class Champion extends Entity {
	float scale;
	Entity target;
	float life, attackdelay;
	
	private Texture entTex, shadowTex;
	Image enemy, shadow;
	Animation           enemyanim;      // #3
    Texture             walkTex;      // #4
    TextureRegion[]         walkframes;     // #5
    SpriteBatch         spriteBatch;        // #6
    TextureRegion           currentFrame;
    float stateTime;

	public Champion(String name, int health, int posX, int posY){
		super(name, health);
		scale = 0.1f;
		
		entTex = new Texture("data/enemyTextures/champion.png");
		shadowTex = new Texture("data/enemyTextures/shadow.png");
		
		Texture walkTex = new Texture("data/enemyTextures/champion_walkf.png");
		TextureRegion[][] tmp = TextureRegion.split(walkTex, walkTex.getWidth()/4, walkTex.getHeight()/5);
		TextureRegion[] walkframes = new TextureRegion[4 * 5];
		int index = 0;
		for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 4; j++) {
                walkframes[index++] = tmp[i][j];
            }
        }
        
		// Create enemy Image/Actor
		enemy = new Image(entTex);
		enemy.setX(posX);
		enemy.setY(posY);
		enemy.setScale(scale);
		
		enemyanim = new Animation(0.025f, walkframes);
		spriteBatch = new SpriteBatch();
		stateTime = 0f;
		
		shadow = new Image(shadowTex);
		shadow.setX(posX);
		shadow.setY(posY);
		shadow.setScale(scale*4);
		
		life = 45f;
		attackdelay = 0;
		target = null;
	}
	
	public void Anim(float delta)
	{
		stateTime += Gdx.graphics.getDeltaTime();           // #15
		currentFrame = enemyanim.getKeyFrame(stateTime, true);  // #16
		spriteBatch.begin();
		spriteBatch.draw(currentFrame, 50, 50);             // #17
		spriteBatch.end();
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
	
	public Animation getAnim(){
		return enemyanim;
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
			return;
		}
		
		if(Math.abs(target.getGroundPosition().y - getGroundPosition().y) < (Gdx.graphics.getHeight() * 0.03f))
		{
			if(Math.abs(target.getGroundPosition().x - getGroundPosition().x) < (Gdx.graphics.getWidth() * 0.04f))
			{
				target.decreaseHealth(200);
				attackdelay = 1f;
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
			
		scale = (Gdx.graphics.getHeight() - getPosition().y) / 1000;
		enemy.setScale(scale / 2);
		shadow.setScale(scale * 2);

		setPosition(getPosition().x + walkVec.x, getPosition().y + walkVec.y);

		shadow.setX(getGroundPosition().x - ((shadow.getWidth() / 2) * (scale*2)));
		shadow.setY(getPosition().y - ((enemy.getHeight() / 2) * scale) - ((shadow.getHeight() / 2) * (scale * 2)));
		return;
	}
}
