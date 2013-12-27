package cz.mff.cuni.autickax.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import cz.mff.cuni.autickax.MyGdxGame;

/**
 * Base class for all game entities
 */
abstract public class GameObject {

	protected float x;
	protected float y;
	private boolean toDispose;
	protected TextureRegion texture;
	protected MyGdxGame game;
	
	public GameObject(float startX, float startY) {
		this.x = startX;
		this.y = startY;
		this.game = MyGdxGame.getInstance();
	}
	
	public float getX() { return x;}
	public float getY() { return y;}

	public boolean isToDispose() {
		return toDispose;
	}

	public void setToDispose(boolean toDispose) {
		this.toDispose = toDispose;
	}
	
	public void move(float newX, float newY)
	{
		this.x = newX;
		this.y = newY;
	}
	
	abstract public void update(float delta);
	
	public void draw(SpriteBatch batch, float delta)
	{
		batch.draw(this.texture, this.getX(), this.getY());
	}
}
