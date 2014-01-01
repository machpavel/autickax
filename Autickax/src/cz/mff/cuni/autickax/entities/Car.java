package cz.mff.cuni.autickax.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector3;

import cz.mff.cuni.autickax.scene.GameScreen;


public class Car extends GameObject {

	private boolean isDragged = false;
	
	private GameScreen level;
	
	public Car( float x, float y, GameScreen level) {
		super(x, y);
		super.texture = super.game.assets.getGraphics("car");
		this.level = level;
	}
	
	public boolean isDragged() {
		return this.isDragged;
	}
	
	public void setDragged(boolean value){
		this.isDragged = value;		
	}

	@Override
	public void update(float delta) {
		if( Gdx.input.justTouched())
		{
			Vector3 touchPos = new Vector3();
			touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
			this.level.unproject(touchPos);
			
			this.setDragged(true);
		}
		
		if (this.isDragged())
		{
			if(Gdx.input.isTouched()) {
				Vector3 touchPos = new Vector3();
				touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
				this.level.unproject(touchPos);
				this.move(touchPos.x,  touchPos.y);
			}
		}		
	}
	
}
