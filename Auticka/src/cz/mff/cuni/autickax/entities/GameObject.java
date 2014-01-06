package cz.mff.cuni.autickax.entities;

import java.io.IOException;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.XmlWriter;

import cz.mff.cuni.autickax.Autickax;
import cz.mff.cuni.autickax.scene.GameScreen;

/**
 * Base class for all game entities
 */
abstract public class GameObject {

	protected float x;
	protected float y;
	private boolean toDispose;
	protected TextureRegion texture;
	protected String textureName;
	protected Autickax game;
	protected GameScreen gameScreen;

	private GameObject(float startX, float startY) {
		this.x = startX;
		this.y = startY;
		this.game = Autickax.getInstance();
	}
		
	
	public GameObject(float startX, float startY, GameScreen gameScreen, String textureName) {
		this(startX, startY);
		this.gameScreen = gameScreen;
		
		this.textureName = textureName;
		this.texture = this.game.assets.getGraphics(textureName);
	}
		

	public float getX() {
		return x;
	}

	public float getY() {
		return y;
	}

	public boolean isToDispose() {
		return toDispose;
	}

	public void setToDispose(boolean toDispose) {
		this.toDispose = toDispose;
	}

	public void move(float newX, float newY) {
		this.x = newX;
		this.y = newY;
	}

	abstract public void update(float delta);
	abstract public String getName();
	

	public void draw(SpriteBatch batch) {
		batch.draw(this.texture, this.getX(), this.getY());
	}
	
	
	public String toString(){
		return getName() + " " + x + " " + y;
	}
	
	public void toXml(XmlWriter writer) throws IOException{
		writer.element(this.getName());
			writer.attribute("X", x);
			writer.attribute("Y", y);
			writer.attribute("textureName", textureName);
			aditionalsToXml(writer);
		writer.pop();
	}
	
	abstract void aditionalsToXml(XmlWriter writer) throws IOException;	
}