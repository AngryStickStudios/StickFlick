package com.AngryStickStudios.StickFlick.Controller;

import com.badlogic.gdx.graphics.g2d.Animation;

public class AnimationLoaderClass {
		private String name;
		private Animation anim;
		
		public AnimationLoaderClass(String gname, Animation ganim)
		{
			name = gname;
			anim = ganim;
		}
		
		public String getName()
		{
			return name;
		}
		
		public Animation getAnim()
		{
			return anim;
		}
}
