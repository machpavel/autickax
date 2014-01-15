package cz.mff.cuni.autickax.entities;

import java.io.IOException;
import java.io.Serializable;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.XmlWriter;

import cz.mff.cuni.autickax.Autickax;
import cz.mff.cuni.autickax.scene.GameScreen;
import cz.mff.cuni.autickax.gamelogic.SubLevel;
import cz.mff.cuni.autickax.input.Input;
import cz.mff.cuni.autickax.miniGames.Minigame;

/**
 * Base class for all game entities
 */
abstract public class GameObject implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/** Position of the center of GameObject. */
	protected Vector2 position;
	protected int width;
	protected int height;
	protected float rotation;
	protected Vector2 scale = new Vector2(1, 1);
	protected float boundingCircleRadius;

	private boolean toDispose;
	protected transient TextureRegion texture;

	protected GameScreen gameScreen;
	protected int type;
	protected boolean isActive = true;
	
	

	public GameObject(float startX, float startY, GameScreen gameScreen) {
		this.position = new Vector2(startX, startY);
		this.gameScreen = gameScreen;
		this.rotation = 0;
		
		if (this.gameScreen != null) { // caused by asset processor
			this.setTexture();
		}
	}

	public GameObject(GameObject object) {
		this.position = object.position;
		this.width = object.width;
		this.height = object.height;
		this.rotation = object.rotation;
		this.scale = object.scale;
		this.boundingCircleRadius = object.boundingCircleRadius;
		this.toDispose = object.toDispose;
		this.texture = object.texture;
		this.gameScreen = object.gameScreen;
		this.type = object.type;
	}
	
	public void reset(){
		this.isActive = true;
		this.rotation = 0;
		this.scale = new Vector2(1, 1);
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
						* Input.yStretchFactorInv, scale.x, scale.y,
				this.rotation);
	}

	public String toString() {
		return getName() + " PosX: " + this.position.x + " PosY: "
				+ this.position.y + " Width: " + this.width + " Height: "
				+ this.height + " Bounding: " + this.boundingCircleRadius;
	}

	public void toXml(XmlWriter writer) throws IOException {
		writer.element(this.getName());
		writer.attribute("X", this.position.x);
		writer.attribute("Y", this.position.y);
		writer.attribute("type", this.type);
		aditionalsToXml(writer);
		writer.pop();
	}

	protected void setMeasurements(int width, int height) {
		this.width = width;
		this.height = height;
		//TODO: which method of choosin bounding rectangle??
		//this.boundingCircleRadius = width > height ? width / 2 : height / 2;
		this.boundingCircleRadius = width > height ? height / 2 : width / 2;
		//this.boundingCircleRadius = (width + height) / 4;		
	}

	public void setTexture(String name) {
		// TODO: This condition is temporary hack due to loading levels in
		// AssetsProcessor. REWRITE!
//		if (this.gameScreen != null && this.gameScreen.getGame() != null) {
			this.texture = Autickax.getInstance().assets.getGraphics(name);
			setMeasurements(this.texture.getRegionWidth(), this.texture.getRegionHeight());
//		}
	}

	/**
	 * Determines when two object hit each other.
	 * 
	 * @param object2
	 * @return
	 */
	public boolean collides(GameObject object2) {
		float objectsDistance = new Vector2(this.position).sub(object2.position).len();
		float minimalDistance = this.boundingCircleRadius + object2.boundingCircleRadius;
		return objectsDistance < minimalDistance;
	}

	/**
	 * Determines when position of the middle of the object is in the other
	 * object
	 * 
	 * @param object2
	 * @return
	 */
	public boolean positionCollides(GameObject object2) {
		return object2.includePosition(this.position);
	}

	/**
	 * Determinate when object is intersected with a position
	 * 
	 * @param position
	 * @return
	 */
	public boolean includePosition(Vector2 position) {
		float middlesDistance = new Vector2(this.position).sub(position).len();
		return middlesDistance < this.boundingCircleRadius;
	}

	abstract void aditionalsToXml(XmlWriter writer) throws IOException;

	/**
	 * Sets the rotation of object in degrees (counterclockwise).
	 * 
	 * @param rotation
	 */
	public void setRotation(float rotation) {
		this.rotation = rotation % 360;
	}

	/**
	 * Gets the rotation of object in degrees (counterclockwise).
	 * 
	 * @return
	 */
	public float getRotation() {
		return this.rotation;
	}

	public void setPosition(Vector2 position) {
		this.position = position;

	}

	public abstract GameObject copy();
	
	public abstract void setTexture();
	
	public void setScreen(GameScreen screen) {
		this.gameScreen = screen;
	}
	
	public abstract Minigame getMinigame(GameScreen gameScreen, SubLevel parent);
	
	public abstract String getSoundName();
	
	public boolean isActive(){
		return this.isActive;
	}
	public void deactivate(){
		this.isActive = false;
	}
}