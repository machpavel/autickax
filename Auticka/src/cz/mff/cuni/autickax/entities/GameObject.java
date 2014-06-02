package cz.mff.cuni.autickax.entities;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.utils.XmlReader.Element;
import com.badlogic.gdx.utils.XmlWriter;

import cz.mff.cuni.autickax.Autickax;
import cz.mff.cuni.autickax.constants.Constants;
import cz.mff.cuni.autickax.gamelogic.SubLevel;
import cz.mff.cuni.autickax.input.Input;
import cz.mff.cuni.autickax.miniGames.Minigame;
import cz.mff.cuni.autickax.scene.GameScreen;

/**
 * Base class for all game entities
 */
public abstract class GameObject extends Actor implements Externalizable {

	private static final byte MAGIC_GAME_OBJECT_END = 127;

	protected float boundingCircleRadius;

	protected transient TextureRegion texture;

	protected int type;
	protected boolean isActive = true;

	protected transient boolean canBeDragged = false;

	public boolean canBeDragged() {
		return this.canBeDragged;
	}

	public void setCanBeDragged(boolean canBeDragged) {
		this.canBeDragged = canBeDragged;
	}

	protected transient boolean isDragged = false;

	/** Parameterless constructor for the externalization */
	public GameObject() {
		this.setRotation(0);
		this.type = 0;
		this.texture = null;
	}

	public GameObject(float startX, float startY, int type) {
		this.setPosition(startX, startY);
		this.setRotation(0);
		this.type = type;
		setTexture(type);
	}

	public GameObject(GameObject object) {
		this.setPosition(object.getX(), object.getY());
		this.setWidth(object.getWidth());
		this.setHeight(object.getHeight());
		this.setRotation(object.getRotation());
		this.setScale(object.getScaleX(), object.getScaleY());
		this.boundingCircleRadius = object.boundingCircleRadius;
		this.setTexture(object.getTexture());
		this.type = object.type;
	}

	public static GameObject parseGameObject(Element gameObject)
			throws IOException {
		GameObject retval;

		float x = gameObject.getFloat("X");
		float y = gameObject.getFloat("Y");
		int type = gameObject.getInt("type", 0);

		String objectName = gameObject.getName();
		if (objectName.equals(Mud.name)) {
			retval = new Mud(x, y, type);
		} else if (objectName.equals(Stone.name)) {
			retval = new Stone(x, y, type);
		} else if (objectName.equals(Tree.name)) {
			retval = new Tree(x, y, type);
		} else if (objectName.equals(Fence.name)) {
			retval = new Fence(x, y, type);
		} else if (objectName.equals(ParkingCar.name)) {
			retval = new ParkingCar(x, y, type);
		} else if (objectName.equals(Wall.name)) {
			retval = new Wall(x, y, type);
		} else if (objectName.equals(House.name)) {
			retval = new House(x, y, type);
		} else if (objectName.equals(Hole.name)) {
			retval = new Hole(x, y, type);
		} else if (objectName.equals(Booster.name)) {
			retval = new Booster(x, y, type);
		} else if (objectName.equals(Hill.name)) {
			retval = new Hill(x, y, type);
		} else if (objectName.equals(Tornado.name)) {
			retval = new Tornado(x, y, type);
		} else if (objectName.equals(UniversalGameObject.name)) {
			retval = new UniversalGameObject(x, y, type);
		} else if (objectName.equals(Pneu.name)) {
			retval = new Pneu(x, y, type);
		} else if (objectName.equals(RacingCar.name)) {
			retval = new RacingCar(x, y, type);
		} else {
			throw new IOException("Loading object failed: Unknown type "
					+ " \"" + objectName + "\"");
		}
		return retval;
	}

	public void reset() {
		this.isActive = true;
		this.setRotation(0);
		this.isDragged = false;
		this.setScale(1);
	}

	/** Returns position of the objects center. */
	public Vector2 getPosition() {
		return new Vector2(this.getX(), this.getY());
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
		this.setPosition(newPos.x, newPos.y);
	}

	public void update(float delta) {
	}
	
	protected void updateDragging(float delta) {
		if (this.canBeDragged) {
			if (this.isDragged) {
				if (Gdx.input.isTouched()) {
					Vector2 touchPos = new Vector2(Input.getX(), Input.getY());
					this.move(touchPos);
				} else {
					this.isDragged = false;
				}
			}
		}
	}

	abstract public String getName();

	public void draw(Batch batch) {
		this.draw(batch, 0);
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		batch.draw(this.getTexture(), (this.getX() - this.getWidth() / 2),
				(this.getY() - this.getHeight() / 2), (this.getWidth() / 2),
				(this.getHeight() / 2), this.getWidth(), this.getHeight(),
				this.getScaleX(), this.getScaleY(), this.getRotation());
	}

	public String toString() {
		return getName() + " PosX: " + this.getX() + " PosY: " + this.getY()
				+ " Width: " + this.getWidth() + " Height: " + this.getHeight()
				+ " Bounding: " + this.boundingCircleRadius;
	}

	public void toXml(XmlWriter writer) throws IOException {
		writer.element(this.getName());
		writer.attribute("X", this.getX());
		writer.attribute("Y", this.getY());
		writer.attribute("type", this.type);
		aditionalsToXml(writer);
		writer.pop();
	}

	protected void setMeasurements(int width, int height) {
		this.setWidth(width);
		this.setHeight(height);

		// Counts bounding radius only if it wasn't assigned before
		if (this.boundingCircleRadius == 0)
			this.boundingCircleRadius = width > height ? height / 2 : width / 2;
	}

	/**
	 * Sets texture from main game texture atlas.
	 * 
	 * @param name
	 * @return True when texture was set.
	 */
	public boolean setTexture(String name) {
		if (Autickax.getInstance() != null) {
			this.texture = Autickax.getInstance().assets.getGraphics(name);
			setMeasurements(this.texture.getRegionWidth(),
					this.texture.getRegionHeight());
			return true;
		} else
			return false;

	}

	public void setTexture(TextureRegion texture) {
		this.texture = texture;
	}

	public abstract void setTexture(int type);

	public void setTexture() {
		setTexture(this.type);
	}

	/**
	 * Determines when two object hit each other.
	 * 
	 * @param object2
	 * @return
	 */
	public boolean collides(GameObject object2) {
		float objectsDistance = this.getPosition().sub(object2.getPosition())
				.len();
		float minimalDistance = this.boundingCircleRadius
				+ object2.boundingCircleRadius;
		return objectsDistance < minimalDistance;
	}

	/**
	 * This method test whether this (a moving game object) collided with
	 * another object. As a result of possible high velocity it is necessary to
	 * interpolate positions between current location and last recorded
	 * location. Each of this position is test for collision against given
	 * object. Number of tested positions depends on the bounding radius of this
	 * object
	 * 
	 * @param obstacle
	 *            Source of possible collision
	 * @param formerPosition
	 *            Last known position of this object before the current one
	 * @return true if there is a collision
	 */
	public boolean collidesWithinLineSegment(GameObject obstacle,
			Vector2 formerPosition) {
		GameObject movingObject = copy();
		movingObject.setPosition(formerPosition.x, formerPosition.y);
		Vector2 dirVec = this.getPosition().sub(formerPosition);
		float dist = dirVec.len();
		Vector2 norm = new Vector2(dirVec).nor();
		Vector2 lineCenter = new Vector2(formerPosition).add(new Vector2(norm)
				.scl(dist / 2));

		return movingObject.collides(obstacle)
				|| collides(obstacle)
				|| intersects(obstacle.getPosition(),
						obstacle.boundingCircleRadius, lineCenter,
						2 * this.boundingCircleRadius, dist);
	}

	private boolean intersects(Vector2 circleCenter, float circleRadius,
			Vector2 rectCenter, float width, float height) {
		Vector2 circleDistance = new Vector2(Math.abs(circleCenter.x
				- rectCenter.x), Math.abs(circleCenter.y - rectCenter.y));

		if (circleDistance.x > (width / 2 + circleRadius)) {
			return false;
		}
		if (circleDistance.y > (height / 2 + circleRadius)) {
			return false;
		}

		if (circleDistance.x <= (width / 2)) {
			return true;
		}
		if (circleDistance.y <= (height / 2)) {
			return true;
		}

		double cornerDistance_sq = Math.pow((circleDistance.x - width / 2), 2)
				+ Math.pow((circleDistance.y - height / 2), 2);

		return (cornerDistance_sq <= Math.pow(circleRadius, 2));
	}

	/**
	 * Determines when position of the middle of the object is in the other
	 * object
	 * 
	 * @param object2
	 * @return
	 */
	public boolean positionCollides(GameObject object2) {
		return object2.includePosition(this.getPosition());
	}

	/**
	 * Determinate when object is intersected with a position
	 * 
	 * @param position
	 * @return
	 */
	public boolean includePosition(Vector2 position) {
		float middlesDistance = new Vector2(this.getPosition()).sub(position)
				.len();
		return middlesDistance < this.boundingCircleRadius;
	}

	void aditionalsToXml(XmlWriter writer) throws IOException {
		// Every object can write its own values in writer
	}

	@Override
	public Actor hit(float x, float y, boolean touchable) {
		if (touchable && this.getTouchable() != Touchable.enabled)
			return null;
		Vector2 point = new Vector2(x * Input.xStretchFactor, y
				* Input.yStretchFactor);
		return point.dst(Vector2.Zero) < this.boundingCircleRadius ? this
				: null;
	}

	public abstract GameObject copy();

	public Minigame getMinigame(GameScreen gameScreen, SubLevel parent) {
		return null;
	}

	public String getSoundName() {
		return Constants.sounds.SOUND_NO_SOUND;
	}

	public void setIsActive(boolean value) {
		this.isActive = value;
	}

	public boolean getIsActive() {
		return this.isActive;
	}

	public TextureRegion getTexture() {
		return texture;
	}

	@Override
	public void readExternal(ObjectInput in) throws IOException,
			ClassNotFoundException {
		float x = in.readFloat();
		float y = in.readFloat();
		this.setPosition(x, y);
		this.setWidth(in.readInt());
		this.setHeight(in.readInt());
		this.setRotation(in.readFloat());
		this.setScaleX(in.readFloat());
		this.setScaleY(in.readFloat());
		this.boundingCircleRadius = in.readFloat();
		this.type = in.readInt();

		byte check = in.readByte();
		if (check != GameObject.MAGIC_GAME_OBJECT_END)
			throw new RuntimeException("Game object wasn't read correctly");
	}

	@Override
	public void writeExternal(ObjectOutput out) throws IOException {
		out.writeFloat(this.getX());
		out.writeFloat(this.getY());
		out.writeFloat(this.getWidth());
		out.writeFloat(this.getHeight());
		out.writeFloat(this.getRotation());
		out.writeFloat(this.getScaleX());
		out.writeFloat(this.getScaleY());
		out.writeFloat(this.boundingCircleRadius);
		out.writeInt(this.type);
		out.writeByte(GameObject.MAGIC_GAME_OBJECT_END);
	}

	public boolean isDragged() {
		return isDragged;
	}

	public void setDragged(boolean isDragged) {
		this.isDragged = isDragged;
	}
}