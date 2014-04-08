package com.AngryStickStudios.StickFlick.Entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.AngryStickStudios.StickFlick.StickFlick;
import com.AngryStickStudios.StickFlick.Controller.AnimationLoader;

public class WalkingEnemy extends Entity {
	float scale, scaleMultiplier;
	private boolean held, floating, frozen;
	private Vector2 lastPos, destination, flySpeed;
	private int moveBackSpeed;
	private int splatting;
	float peakamt =  .05f * Gdx.graphics.getHeight();
	
	private Texture shadowTex;
    private Animation currentanim;
    private String walkName;
    private TextureRegion currentframe;
    private TextureRegionDrawable enemyDrawable;
    float animationStateTime;
	
    Image enemy, shadow;

	public WalkingEnemy(String name, int health, AnimationLoader anims, int posX, int posY){
		super(name, health, anims);
		lastPos = new Vector2(posX, posY);
		
		// Set enemy texture depending on type
		if(name == "Basic"){
			//entTex = new Texture("data/enemyTextures/basicEnemy.png");
			walkName = "dude_walk";
			scaleMultiplier = 1f;
		} else if(name == "Demo"){
			//entTex = new Texture("data/enemyTextures/basicEnemy.png");
			walkName = "demo_walk";
			scaleMultiplier = 0.5f;
		} else if(name == "BigDude"){
			//entTex = new Texture("data/enemyTextures/basicEnemy.png");
			walkName = "bigdude_walk";
			scaleMultiplier = 2.5f;
			setHealthMax(health * 10);
			setHealthCurrent(getHealthMax());
		} else{
			//entTex = new Texture("data/enemyTextures/error.png");
			walkName = "dude_walk";
			scaleMultiplier = 5f;
		}
		
		currentanim = anims.getAnim(walkName);
        currentframe = currentanim.getKeyFrame(animationStateTime, true);
        enemyDrawable = new TextureRegionDrawable(currentframe);
		shadowTex = new Texture("data/enemyTextures/shadow.png");
		
		// Create enemy Image/Actor
		enemy = new Image(enemyDrawable);
		enemy.setX(posX);
		enemy.setY(posY);
		enemy.setScale(scale);
		
		shadow = new Image(shadowTex);
		shadow.setX(posX);
		shadow.setY(posY);
		shadow.setScale(scale);
		
		scale = ((Gdx.graphics.getHeight() - getPosition().y) / 1000) * scaleMultiplier;
		enemy.setScale(scale);
		shadow.setScale(scale);
			
		held = false;
		floating = false;
		frozen = false;
		moveBackSpeed = 0;
		FindDestOnWall();
	}
	
	public void freeze(){
		frozen = true;
	}
	
    public void setSplatting(int val)
    {
            splatting = val;
    }
   
    public void setState(float sta)
    {
            animationStateTime = sta;
    }
   
    public int getSplatting()
    {
            return splatting;
    }
	
	public void unfreeze(){
		frozen = false;
	}
	
	public Image getImage(){
		return enemy;
	}
	
	public Image getShadow(){
		return shadow;
	}
	
	public Vector2 getLastPos(){
		return lastPos;
	}
	
	public boolean onGround(){
		if(held || floating)
		{
			return false;
		}
		return true;
	}
	
	public void setPosition(float x, float y){
		enemy.setX(x - ((enemy.getWidth() / 2) * scale));
		enemy.setY(y - ((enemy.getHeight() / 2) * scale));
	}
	
	public Vector2 getPosition()
	{
		return new Vector2(enemy.getX() + ((enemy.getWidth() / 2) * scale), enemy.getY() + ((enemy.getHeight() / 2) * scale));
	}
	
	public Vector2 getGroundPosition()
	{
		return new Vector2(enemy.getX() + ((enemy.getWidth() / 2) * scale), enemy.getY());
	}
	
	public Vector2 getSize(){
		return new Vector2(enemy.getWidth() * scale, enemy.getHeight() * scale);
	}
	
	public float getPeak(){
		return peakamt;
	}
	
	public void FindDestOnWall() {

		Vector2 straightDown = getPosition();
		straightDown.y = Gdx.graphics.getHeight() * 0.04f;

		int adjAmt = (int) Math.round(((Math.random() * (Gdx.graphics.getWidth() / 5)) - (Gdx.graphics.getWidth() / 10)));
		System.out.println("Adjust Amount: " + adjAmt);
		straightDown.x += adjAmt;
		
		int tempWidth = Gdx.graphics.getWidth();
		if(straightDown.x > tempWidth * 0.98f){
			straightDown.x = tempWidth * 0.98f;
		}
		if(straightDown.x < tempWidth * 0.02f){
			straightDown.x = tempWidth * 0.02f;
		}
		destination = straightDown;
	}

	public void pickedUp() {
		held = true;
		if(floating == false)
		{
			lastPos.x = getPosition().x;
			lastPos.y = getPosition().y;
		}
	}

	public void Released(Vector2 speed) {
		held = false;
		floating = true;
		flySpeed = new Vector2(speed);
		moveBackSpeed = Math.round(flySpeed.y / 10);
		System.out.println("MoveBackSpeed: " + moveBackSpeed);
	}
	
	public void Update(float delta){
		if(getIsAlive() == false) return;
		
		if(held)
		{
			setPosition(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY());
			shadow.setX(enemy.getX());
			shadow.setY(lastPos.y - ((enemy.getHeight() / 2) * scale) - ((shadow.getHeight() / 2) * scale));
			
			if(getPosition().y <= lastPos.y)
			{
				setPosition(Gdx.input.getX(), lastPos.y);
			}
			return;
		}

		if(floating)
		{
			Vector2 newPos = new Vector2(0,0);
			newPos.x = getPosition().x + flySpeed.x;
			
			if(lastPos.y < Gdx.graphics.getHeight() / 1.8f ){
				lastPos.y = lastPos.y + ((Gdx.graphics.getHeight() / 500) * moveBackSpeed);
			}
			
			scale = ((Gdx.graphics.getHeight() - lastPos.y) / 1000) * scaleMultiplier;
			enemy.setScale(scale);
			shadow.setScale(scale);
			
			if(newPos.x < Gdx.graphics.getWidth() * 0.01f){
				newPos.x = Gdx.graphics.getWidth() * 0.01f;
			}
			if(newPos.x > Gdx.graphics.getWidth() * 0.99f){
				newPos.x = Gdx.graphics.getWidth() * 0.99f;
			}

			//has landed back on the homeland
			if(lastPos.y >= getPosition().y + flySpeed.y)
			{
				newPos.y = lastPos.y;
				floating = false;
				//landed = true;
				setPosition(newPos.x, newPos.y);
				
				Damage(-flySpeed.y);
				FindDestOnWall();
			}
			else
			{
				newPos.y = getPosition().y + (flySpeed.y * 0.5f);
				setPosition(newPos.x, newPos.y);
			}

			flySpeed.y -= 0.3f;
			shadow.setX(enemy.getX());
			shadow.setY(lastPos.y - ((enemy.getHeight() / 2) * scale) - ((shadow.getHeight() / 2) * scale));
			return;
		}
		
		//walk up... and shit
		if(peakamt > 0 && frozen == false)
		{
			scale = ((Gdx.graphics.getHeight() - getPosition().y) / 1000) * scaleMultiplier;
			enemy.setScale(scale);
			shadow.setWidth(enemy.getWidth());
			shadow.setScale(scale);
			
			setPosition(getPosition().x, getPosition().y + (20 * delta));
			peakamt -= (20*delta);
			shadow.setX(enemy.getX());
			shadow.setY(getPosition().y + (Gdx.graphics.getHeight() / 100) - ((enemy.getHeight() / 2) * scale) - ((shadow.getHeight() / 2) * scale));
			return;
		}
		
		//walk and shit
		if(getPosition().y > Gdx.graphics.getHeight() * 0.1f && frozen == false){
			Vector2 compVec = new Vector2(destination.x - getPosition().x, destination.y - getPosition().y);
			Vector2 normVec = compVec.nor();
			Vector2 walkVec = normVec.scl(8 * delta);
			
			scale = ((Gdx.graphics.getHeight() - getPosition().y) / 1000) * scaleMultiplier;
			enemy.setScale(scale);
			shadow.setScale(scale);

			setPosition(getPosition().x + walkVec.x, getPosition().y + walkVec.y);

			shadow.setX(enemy.getX());
			shadow.setY(getPosition().y + (Gdx.graphics.getHeight() / 100) - ((enemy.getHeight() / 2) * scale) - ((shadow.getHeight() / 2) * scale));
		}
	}

	public void Damage(float fallingVelocity){
		//can change the dmgAmt ratio to whatever
		int dmgAmt = (int)fallingVelocity * 4;
		decreaseHealth(dmgAmt);
		System.out.println("Falling Velocity: " + fallingVelocity);
		System.out.println("Damage Amount: " + dmgAmt);
		System.out.println("Stickman Health: " + getHealthCurrent());
		if(getIsAlive() != true){
			System.out.println("An enemy reached zero heath! Victory dance!");
			splatting = 1;
			animationStateTime = 0;
		}
	}

	public void Anim(float delta)
	{
		if(splatting == 0)
		{
			currentframe = currentanim.getKeyFrame(animationStateTime += delta, true);
			enemyDrawable.setRegion(currentframe);
			enemy.setDrawable(enemyDrawable);
		}
		else
		{
			currentframe = anims.getAnim("splat").getKeyFrame(animationStateTime += delta, false);
			enemyDrawable.setRegion(currentframe);
			enemy.setDrawable(enemyDrawable);

			if(animationStateTime >= 1)
			{
				splatting = 2;
			}
		}

	}
}
