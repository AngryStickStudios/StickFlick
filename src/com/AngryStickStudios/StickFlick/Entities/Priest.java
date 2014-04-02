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
 
public class Priest extends Entity {
        float scale, mscale;
 
        private boolean held, floating, frozen/*, landed*/;
        private Vector2 lastPos, destination, flySpeed;
        private int moveBackSpeed;
        private int splatting;
        Entity target;
        float healdelay;
        float peakamt =  .05f * Gdx.graphics.getHeight();
       
        private Texture shadowTex;
        private Animation currentanim;
        private TextureRegion currentframe;
        private TextureRegionDrawable enemyDrawable;
        float animationStateTime;
       
        Image enemy, shadow;
 
        public Priest(String name, int health, AnimationLoader anims, int posX, int posY){
                super(name, health, anims);
                scale = 0.5f;
                mscale = 0.5f;
               
                lastPos = new Vector2(posX, posY);
               
                animationStateTime = 0;
                currentanim = anims.getAnim("priest_walk_f");
                currentframe = currentanim.getKeyFrame(animationStateTime, true);
                enemyDrawable = new TextureRegionDrawable(currentframe);
                   
                splatting = 0;
               
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
               
                healdelay = 0;
                target = null;
                held = false;
                floating = false;
                frozen = false;
                moveBackSpeed = 0;
                RandomDest();
        }
       
        public int getSplatting()
        {
                return splatting;
        }
       
        public void setSplatting(int val)
        {
                splatting = val;
        }
       
        public void setState(float sta)
        {
                animationStateTime = sta;
        }
       
        public float getPeak(){
                return peakamt;
        }
       
        public void setPeak(float newamt){
                peakamt = newamt;
        }
       
        public Entity getTarget(){
                return target;
        }
       
        public void setTarget(Entity inp){
                target = inp;
        }
       
        public void freeze(){
                frozen = true;
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
       
        public void RandomDest(){
                destination = new Vector2(Math.round(Math.random() * Gdx.graphics.getWidth()), Math.round(Math.random() * (Gdx.graphics.getHeight() / 2)));
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
                        currentanim = anims.getAnim("priest_walk_f");
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
                		currentanim = anims.getAnim("priest_walk_f");
                        Vector2 newPos = new Vector2(0,0);
                        newPos.x = getPosition().x + flySpeed.x;
                       
                        if(lastPos.y < Gdx.graphics.getHeight() / 1.8f ){
                                lastPos.y = lastPos.y + ((Gdx.graphics.getHeight() / 500) * moveBackSpeed);
                        }
                       
                        scale = ((Gdx.graphics.getHeight() - lastPos.y) / 1000) * mscale;
                        enemy.setScale(scale);
                        shadow.setScale(scale / mscale);
                       
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
                                        currentanim = anims.getAnim("priest_walk_f");
                                        scale = ((Gdx.graphics.getHeight() - getPosition().y) / 1000) * mscale;
                                        enemy.setScale(scale);
                                        shadow.setScale(scale / mscale);
                                       
                                        setPosition(getPosition().x, getPosition().y + (20 * delta));
                                        peakamt -= (20*delta);
                                        shadow.setX(enemy.getX());
                                        shadow.setY(getPosition().y - ((enemy.getHeight() / 2) * scale) - ((shadow.getHeight() / 2) * scale));
                                        return;
                                }
               
                if(healdelay > 0)
                {
                        healdelay -= Gdx.graphics.getDeltaTime();
                        return;
                }
               
                if(frozen)
                {
                        return;
                }
               
               
               
                if(target == null || target.getIsAlive() == false || target.getHealthCurrent() == target.getHealthMax())
                {
                        if(Math.abs(destination.y - getPosition().y) < (Gdx.graphics.getHeight() * 0.04f))
                        {
                                if(Math.abs(destination.x - getPosition().x) < (Gdx.graphics.getWidth() * 0.05f))
                                {
                                        RandomDest();
                                        return;
                                }
                        }
                        Vector2 compVec = new Vector2(destination.x - getPosition().x, destination.y - getPosition().y);
                        Vector2 normVec = compVec.nor();
                        Vector2 walkVec = normVec.scl(120 * delta);
                       
                        if(Math.abs(walkVec.x) >= Math.abs(walkVec.y))
                        {
                                if(walkVec.x < 0)
                                	currentanim = anims.getAnim("priest_walk_l");
                                else
                                	currentanim = anims.getAnim("priest_walk_r");
                        }
                        else
                        {
                                if(walkVec.y < 0)
                                	currentanim = anims.getAnim("priest_walk_f");
                                else
                                	currentanim = anims.getAnim("priest_walk_b");
                        }
                               
                        scale = ((Gdx.graphics.getHeight() - getPosition().y) / 1000) * mscale;
                        enemy.setScale(scale);
                        shadow.setScale(scale / mscale);
 
                        setPosition(getPosition().x + walkVec.x, getPosition().y + walkVec.y);
 
                        shadow.setX(enemy.getX());
                        shadow.setY(getPosition().y - ((enemy.getHeight() / 2) * scale) - ((shadow.getHeight() / 2) * scale));
                        return;
                }
               
                if(Math.abs(target.getGroundPosition().y - getGroundPosition().y) < (Gdx.graphics.getHeight() * 0.08f))
                {
                        if(Math.abs(target.getGroundPosition().x - getGroundPosition().x) < (Gdx.graphics.getWidth() * 0.1f))
                        {
                                target.increaseHealth(100);
                                healdelay = 1f;
                                target = null;
                                RandomDest();
                                return;
                        }
                }
               
                Vector2 compVec;
                if(!target.onGround())
                {
                        if(Math.abs(target.getGroundPosition().x - getGroundPosition().x) < (Gdx.graphics.getWidth() * 0.09f))
                        {
                                if(Math.abs(target.getLastPos().y - getGroundPosition().y) < (Gdx.graphics.getHeight() * 0.07f))
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
                Vector2 walkVec = normVec.scl(120 * delta);
               
                if(Math.abs(walkVec.x) >= Math.abs(walkVec.y))
                {
                        if(walkVec.x < 0)
                        	currentanim = anims.getAnim("priest_walk_l");
                        else
                        	currentanim = anims.getAnim("priest_walk_r");
                }
                else
                {
                        if(walkVec.y < 0)
                        	currentanim = anims.getAnim("priest_walk_f");
                        else
                        	currentanim = anims.getAnim("priest_walk_b");
                }
                       
                scale = ((Gdx.graphics.getHeight() - getPosition().y) / 1000) * mscale;
                enemy.setScale(scale);
                shadow.setScale(scale / mscale);
 
                setPosition(getPosition().x + walkVec.x, getPosition().y + walkVec.y);
 
                shadow.setX(enemy.getX());
                shadow.setY(getPosition().y - ((enemy.getHeight() / 2) * scale) - ((shadow.getHeight() / 2) * scale));
                return;
        }
 
        public void Damage(float fallingVelocity){
                //can change the dmgAmt ratio to whatever
                int dmgAmt = (int)fallingVelocity * 4;
                decreaseHealth(dmgAmt);
                System.out.println("Falling Velocity: " + fallingVelocity);
                System.out.println("Damage Amount: " + dmgAmt);
                System.out.println("Stickman Health: " + getHealthCurrent());
                if(getIsAlive() != true)
                {
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