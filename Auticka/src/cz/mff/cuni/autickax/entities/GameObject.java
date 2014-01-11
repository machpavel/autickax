package cz.mff.cuni.autickax.entities;

import java.io.IOException;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.XmlWriter;

import cz.mff.cuni.autickax.Autickax;
import cz.mff.cuni.autickax.scene.GameScreen;
import cz.mff.cuni.autickax.input.Input;

/**
 * Base class for all game entities
 */
abstract public class GameObject {

	/** Position of the center of GameObject. */
	protected Vector2 position;
	protected int width;
	protected int height;
	protected float rotation;
	protected Vector2 scale = new Vector2(1, 1);
	protected int boundingCircleRadius;

	private boolean toDispose;
	protected TextureRegion texture;


	protected Autickax game;
	protected GameScreen gameScreen;
	protected int type;

	public GameObject(float startX, float startY, GameScreen gameScreen) {
		this.position = new Vector2(startX, startY);
		this.game = Autickax.getInstance();
		this.gameScreen = gameScreen;
		this.rotation = 0;
	}

	/** Returns position of the objects center. */
	public Vector2 getPosition() {
		return this.position;
	}

	/** Returns x-coordinate of the objects center. */
	public float getX() {
		return this.position.x;
	}

	/** Returns y-coordinate of the objects center. */
	public float getY() {
		return this.position.y;
	}

	public boolean isToDispose() {
		return toDispose;
	}

	public void setToDispose(boolean toDispose) {
		this.toDispose = toDispose;
	}

	/**
	 * Moves object center to the specified position.
	 * 
	 * @param newX
	 *            New position of the center x-coordinate.
	 * @param newY
	 *            New position of the center y-coordinate.
	 */	
	public void move(Vector2 newPos) {
		this.position = newPos;
	}

	abstract public void update(float delta);

	abstract public String getName();

	public void draw(SpriteBatch batch) {
		batch.draw(this.texture, (this.position.x - this.width / 2)
				* Input.xStretchFactorInv, (this.position.y - this.height / 2)
				* Input.yStretchFactorInv, (this.width / 2)
				* Input.xStretchFactorInv, (this.height / 2)
				* Input.yStretchFactorInv,
				this.width * Input.xStretchFactorInv, this.height
						* Input.yStretchFactorInv, scale.x, scale.y, this.rotation);
	}

	public String toString() {
		return getName() + " " + this.position.x + " " + this.position.y;
	}

	public void toXml(XmlWriter writer) throws IOException {
		writer.element(this.getName());
		writer.attribute("X", this.position.x);
		writer.attribute("Y", this.position.y);
		writer.attribute("type", this.type);
		aditionalsToXml(writer);
		writer.pop();
	}

	public void setMeasurements(int width, int height) {
		this.width = width;
		this.height = height;		
		this.boundingCircleRadius = width > height ? width : height;
	}

	public void setTexture(String name) {		
		// TODO: This condition is temporary hack due to loading levels in
		// AssetsProcessor. REWRITE!
		if (this.game != null) {
			this.texture = this.game.assets.getGraphics(name);
		}

		
	}

	/**
	 * Determines when two object hit each other.
	 * 
	 * @param object2
	 * @return
	 */
	public boolean collides(GameObject object2) {
		
		float middlesDistance = new Vector2(this.position).sub(object2.position).len(); 
		return  middlesDistance < (this.boundingCircleRadius + object2.boundingCircleRadius);
	}

	/**
	 * Determines when position of the middle of the object is in the other
	 * object
	 * 
	 * @param object2
	 * @return
	 */
	public boolean positionCollides(GameObject object2) {
		float middlesDistance = new Vector2(this.position).sub(object2.position).len(); 
		return  middlesDistance < object2.boundingCircleRadius;		
	}
	
	/**
	 * Determinate when object is intersected with a position
	 * @param position
	 * @return
	 */
	public boolean includePosition(Vector2 position) {
		float middlesDistance = new Vector2(this.position).sub(position).len(); 
		return  middlesDistance < this.boundingCircleRadius;		
	}

	abstract void aditionalsToXml(XmlWriter writer) throws IOException;

	/**
	 * Sets the rotation of object in degrees (counterclockwise).
	 * 
	 * @param rotation
	 */
	public void setRotation(float rotation) {
		this.rotation = rotation;
	}
	
	/**
	 * Gets the rotation of object in degrees (counterclockwise).
	 * @return 
	 */
	public float getRotation() {
		return this.rotation;
	}	
}