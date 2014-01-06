package cz.mff.cuni.autickax.entities;

import java.io.IOException;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.XmlReader.Element;
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
	
	private boolean toDispose;
	protected TextureRegion texture;

	protected final int width;
	protected final int height;
	protected String textureName;
	protected Autickax game;
	protected GameScreen gameScreen;

	public GameObject(float startX, float startY, int width, int height) {
		this.position = new Vector2(startX, startY);
		this.game = Autickax.getInstance();
		this.width = width;
		this.height = height;
	}
		
	
	public GameObject(float startX, float startY, int width, int height, GameScreen gameScreen, String textureName) {
		this(startX, startY, width, height);
		this.gameScreen = gameScreen;
		
		this.textureName = textureName;
		this.texture = this.game.assets.getGraphics(textureName);
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
	 * @param newX New position of the center x-coordinate.
	 * @param newY New position of the center y-coordinate.
	 */
	public void move(float newX, float newY) {
		this.position = new Vector2(newX, newY);
	}

	abstract public void update(float delta);
	abstract public String getName();
	

	public void draw(SpriteBatch batch) {
		batch.draw (
		    this.texture,
		    (this.position.x - this.width / 2) / Input.xStretchFactor,
		    (this.position.y - this.height / 2) / Input.yStretchFactor,
		    this.width / Input.xStretchFactor,
		    this.height / Input.yStretchFactor
		);
	}
	
	
	public String toString(){
		return getName() + " " + this.position.x + " " + this.position.y;
	}
	
	public void toXml(XmlWriter writer) throws IOException{
		writer.element(this.getName());
			writer.attribute("X", this.position.x);
			writer.attribute("Y", this.position.y);
			writer.attribute("textureName", textureName);
			aditionalsToXml(writer);
		writer.pop();
	}
	
	abstract void aditionalsToXml(XmlWriter writer) throws IOException;	
}