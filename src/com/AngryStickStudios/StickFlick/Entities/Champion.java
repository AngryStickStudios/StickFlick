package com.AngryStickStudios.StickFlick.Entities;
 
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
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
import com.AngryStickStudios.StickFlick.Controller.AnimationLoader;
 
public class Champion extends Entity {
        float scale, mscale;
        Entity target;
        float life, attackdelay, attackdelay2;
       
        private Texture shadowTex;
        private Animation currentanim;
        private TextureRegion currentframe;
        private TextureRegionDrawable enemyDrawable;
        float animationStateTime;
       
        Image enemy, shadow;
        Sound spawn, attack;
 
        public Champion(String name, int health, AnimationLoader anims, int posX, int posY){
                super(name, health, anims);
                scale = 0.1f;
                mscale = 1f;
                shadowTex = anims.getTex("shadow");
               
                animationStateTime = 0;
                currentanim = anims.getAnim("champ_walk_f");
                currentframe = currentanim.getKeyFrame(animationStateTime, true);
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
                
                attack = Gdx.audio.newSound(Gdx.files.internal("data/champion_slash.mp3"));
                spawn = Gdx.audio.newSound(Gdx.files.internal("data/champion_arriving.mp3"));
                spawn.stop();
                spawn.play();
               
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
                        		attack.stop();
                        		attack.play();
                        		target.decreaseHealth(200);
                                if(target.getIsAlive() == false)
                                {
                                	target.setState(0);
                                	target.setSplatting(1);
                                }
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
                                                currentanim = anims.getAnim("champ_attack_l");
                                        else
                                        	currentanim = anims.getAnim("champ_attack_r");
                                }
                                else
                                {
                                        if(walkVec.y < 0)
                                        	currentanim = anims.getAnim("champ_attack_f");
                                        else
                                        	currentanim = anims.getAnim("champ_attack_b");
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
                        	currentanim = anims.getAnim("champ_walk_l");
                        else
                        	currentanim = anims.getAnim("champ_walk_r");
                }
                else
                {
                        if(walkVec.y < 0)
                        	currentanim = anims.getAnim("champ_walk_f");
                        else
                        	currentanim = anims.getAnim("champ_walk_b");
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
