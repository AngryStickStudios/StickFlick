package com.AngryStickStudios.StickFlick.Controller;

import com.badlogic.gdx.graphics.Texture;

public class TextureLoaderClass {
		private String name;
		private Texture anim;
		
		public TextureLoaderClass(String gname, Texture ganim)
		{
			name = gname;
			anim = ganim;
		}
		
		public String getName()
		{
			return name;
		}
		
		public Texture getAnim()
		{
			return anim;
		}
}
