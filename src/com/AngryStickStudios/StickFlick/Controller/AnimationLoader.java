package com.AngryStickStudios.StickFlick.Controller;
 
import java.util.Vector;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.AngryStickStudios.StickFlick.Controller.AnimationLoaderClass;
 
public class AnimationLoader {
	
		Vector<AnimationLoaderClass> list;
 
        public AnimationLoader(){
        	list = new Vector<AnimationLoaderClass>();
        	
        	//all
        	setupAnim("splat", "data/enemyTextures/splatSheet.png", 4, 4, (float) 0.025);
        	
        	//champion
        	setupAnim("champ_walk_f", "data/enemyTextures/hotc_walkf.png", 6, 5, (float) 0.025);
            setupAnim("champ_walk_b", "data/enemyTextures/hotc_walkb.png", 6, 5, (float) 0.025);
            setupAnim("champ_walk_l", "data/enemyTextures/hotc_walkl.png", 6, 5, (float) 0.025);
            setupAnim("champ_walk_r", "data/enemyTextures/hotc_walkr.png", 6, 5, (float) 0.025);
            setupAnim("champ_attack_f", "data/enemyTextures/hotc_attackf.png", 10, 5, (float) 0.025);
            setupAnim("champ_attack_b", "data/enemyTextures/hotc_attackb.png", 10, 5, (float) 0.025);
            setupAnim("champ_attack_l", "data/enemyTextures/hotc_attackl.png", 10, 5, (float) 0.025);
            setupAnim("champ_attack_r", "data/enemyTextures/hotc_attackr.png", 10, 5, (float) 0.025);
            
            //priest
            setupAnim("priest_walk_f", "data/enemyTextures/priest_front.png", 6, 5, (float) 0.025);
            setupAnim("priest_walk_b", "data/enemyTextures/priest_back.png", 6, 5, (float) 0.025);
            setupAnim("priest_walk_l", "data/enemyTextures/priest_left.png", 6, 5, (float) 0.025);
            setupAnim("priest_walk_r", "data/enemyTextures/priest_right.png", 6, 5, (float) 0.025);
            
            //normal dude
            setupAnim("dude_walk", "data/enemyTextures/stickdude_run.png", 6, 5, (float) 0.04);
            
            //demo
            setupAnim("demo_walk", "data/enemyTextures/demo_run.png", 6, 5, (float) 0.025);
            
            //big dude
            setupAnim("bigdude_walk", "data/enemyTextures/stickdude_run.png", 6, 5, (float) 0.06);
        }
       
        public void setupAnim(String name, String file, int cols, int rows, float speed)
        {
                Texture sheet = new Texture(Gdx.files.internal(file)); // #9
                TextureRegion[][] tmp = TextureRegion.split(sheet, sheet.getWidth()/cols, sheet.getHeight()/rows);
            	TextureRegion[] frames = new TextureRegion[cols * rows];
            	int index = 0;
            	for (int i = 0; i < rows; i++) {
            		for (int j = 0; j < cols; j++) {
            				frames[index++] = tmp[i][j];
            		}
            	}
            	
            	Animation anim = new Animation(speed, frames);
            	
            	list.add(new AnimationLoaderClass(name, anim));
        }
        
        public Animation getAnim(String name)
        {
        	for(int i = 0; i < list.size(); i++)
        	{
        		if(list.get(i).getName() == name)
        		{
        			return list.get(i).getAnim();
        		}
        	}
        	
        	return null;
        }
}