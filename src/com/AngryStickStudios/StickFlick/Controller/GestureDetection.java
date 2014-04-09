package com.AngryStickStudios.StickFlick.Controller;

import com.AngryStickStudios.StickFlick.Screens.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.input.GestureDetector.GestureListener;
import com.badlogic.gdx.math.Vector2;

public class GestureDetection implements GestureListener{
	
	Game game;
	
	public GestureDetection(Game game){
		this.game = game;
	}

	public boolean touchDown(float x, float y, int pointer, int button) {
		y = Gdx.graphics.getHeight() - y;
		if(game.getGrabbed() == false){
			for(int i = 0; i < game.getEnemyList().size(); i++)       // Searches through enemy list
			{
				Vector2 size = game.getEnemyList().get(i).getSize();
				Vector2 pos = game.getEnemyList().get(i).getPosition();
				if((pos.x - size.x <= x && x <= pos.x + size.x) && (pos.y - size.y<= y && y < pos.y + size.y)){
					if(game.getEnemyList().get(i).getChanged() && game.getEnemyList().get(i).getSplatting() == 0 && game.getEnemyList().get(i).getIsAlive())
					{
						game.setGrabbedNumber(i);
						game.setGrabbed(true);
						game.getEnemyList().get(game.getGrabbedNumber()).pickedUp();
						break;
					}
				}
			}
		}
		return false;
	}

	@Override
	public boolean tap(float x, float y, int count, int button) {

		//tap to kill
		if(game.getGrabbed() == true && game.getGodStatus())
		{
			game.setGrabbed(false);
			game.getEnemyList().get(game.getGrabbedNumber()).decreaseHealth(100);
			
			if(game.getEnemyList().get(game.getGrabbedNumber()).getIsAlive() == false)
			{
				game.getEnemyList().get(game.getGrabbedNumber()).setState(0);
				game.getEnemyList().get(game.getGrabbedNumber()).setSplatting(1);
			}
			else
			{
				game.getEnemyList().get(game.getGrabbedNumber()).Released(new Vector2(0, 0));
			}
		}

		return false;
	}

	@Override
	public boolean longPress(float x, float y) {
		// UNUSED
		return false;
	}

	@Override
	public boolean fling(float velocityX, float velocityY, int button) {
		if(game.getGrabbed() == true)
		{
			game.setGrabbed(false);
			game.getEnemyList().get(game.getGrabbedNumber()).Released(new Vector2(velocityX / 1000, velocityY / -1000));
		}
		return false;
	}

	@Override
	public boolean pan(float x, float y, float deltaX, float deltaY) {
			// UNUSED
		return false;
	}

	@Override
	public boolean panStop(float x, float y, int pointer, int button) {
			// UNUSED
		return false;
	}

	@Override
	public boolean zoom(float initialDistance, float distance) {
			// UNUSED
		return false;
	}

	@Override
	public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2) {
			// UNUSED
		return false;
	}
}
