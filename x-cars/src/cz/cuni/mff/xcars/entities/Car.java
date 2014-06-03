package cz.cuni.mff.xcars.entities;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.XmlReader.Element;

import cz.cuni.mff.xcars.Xcars;
import cz.cuni.mff.xcars.constants.Constants;
import cz.cuni.mff.xcars.exceptions.IllegalGameObjectException;

public class Car extends ShiftableGameObject  {
	public static final String name = Constants.gameObjects.CAR_NAME;

	private TextureRegion[] positionTextures;
	private Vector2 lastCarPosition;
	private float lastRotationDistance = 0;

	/** Parameterless constructor for the externalization */
	public Car() {
		this.setCanBeDragged(true);
	}

	public Car(float x, float y, int type) {
		super(x, y, type);
		this.lastCarPosition = new Vector2(x, y);
		setCanBeDragged(true);
	}

	public static Car parseCar(Element car) {
		return new Car(car.getFloat("X"), car.getFloat("Y"), car.getInt("type", 1));
	}

	public Car(GameObject object) {
		super(object);
		setCanBeDragged(true);
	}
	
	@Override
	public void update(float delta) {
		this.updateDragging(delta);
	}

	public void reset() {
		super.reset();
		this.setDragged(false);
		this.lastCarPosition = new Vector2(0, 0);
		this.lastRotationDistance = 0;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void move(Vector2 newPos) {
		super.move(newPos);

		lastRotationDistance += newPos.dst(lastCarPosition);
		if (lastRotationDistance > Constants.gameObjects.CAR_MINIMAL_DISTANCE_TO_SHOW_ROTATION) {
			lastRotationDistance = 0;
			this.setRotation(new Vector2(this.lastCarPosition).sub(newPos).scl(-1).angle());
			this.lastCarPosition = newPos;
		}
	}

	public void setNextPositionIsDirection() {
		this.lastRotationDistance = Constants.gameObjects.CAR_MINIMAL_DISTANCE_TO_SHOW_ROTATION;
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		float rotation = this.getRotation();
		while (rotation < 0) {
			this.setRotation(rotation + 360);
			rotation = this.getRotation();
		}
		if (rotation > 360)
			this.setRotation(rotation % 360);

		if (rotation >= 22.5 && rotation < 67.5)
			this.setTexture(positionTextures[1]);
		else if (rotation >= 67.5 && rotation < 112.5)
			this.setTexture(positionTextures[2]);
		else if (rotation >= 112.5 && rotation < 157.5)
			this.setTexture(positionTextures[3]);
		else if (rotation >= 157.5 && rotation < 202.5)
			this.setTexture(positionTextures[4]);
		else if (rotation >= 202.5 && rotation < 247.5)
			this.setTexture(positionTextures[5]);
		else if (rotation >= 247.5 && rotation < 292.5)
			this.setTexture(positionTextures[6]);
		else if (rotation >= 292.5 && rotation < 337.5)
			this.setTexture(positionTextures[7]);
		else if ((rotation >= 337.5 && rotation < 360) || (rotation >= 0 && rotation < 22.5))
			this.setTexture(positionTextures[0]);

		super.draw(batch, parentAlpha);
	}

	@Override
	public GameObject copy() {
		// return new Car(this);
		Car copied = new Car(this.getX(), this.getY(), this.type);
		return copied;
	}

	@Override
	public void setTexture(int type) {
		if (Xcars.getInstance() == null)
			return;
		if (this.positionTextures == null)
			this.positionTextures = new TextureRegion[8];
		switch (type) {
		case 1:
			this.positionTextures[0] = Xcars.getInstance().assets
					.getGraphics(Constants.gameObjects.CAR_TYPE_1_POSITION_0_TEXTURE_NAME);
			this.positionTextures[1] = Xcars.getInstance().assets
					.getGraphics(Constants.gameObjects.CAR_TYPE_1_POSITION_1_TEXTURE_NAME);
			this.positionTextures[2] = Xcars.getInstance().assets
					.getGraphics(Constants.gameObjects.CAR_TYPE_1_POSITION_2_TEXTURE_NAME);
			this.positionTextures[3] = Xcars.getInstance().assets
					.getGraphics(Constants.gameObjects.CAR_TYPE_1_POSITION_3_TEXTURE_NAME);
			this.positionTextures[4] = Xcars.getInstance().assets
					.getGraphics(Constants.gameObjects.CAR_TYPE_1_POSITION_4_TEXTURE_NAME);
			this.positionTextures[5] = Xcars.getInstance().assets
					.getGraphics(Constants.gameObjects.CAR_TYPE_1_POSITION_5_TEXTURE_NAME);
			this.positionTextures[6] = Xcars.getInstance().assets
					.getGraphics(Constants.gameObjects.CAR_TYPE_1_POSITION_6_TEXTURE_NAME);
			this.positionTextures[7] = Xcars.getInstance().assets
					.getGraphics(Constants.gameObjects.CAR_TYPE_1_POSITION_7_TEXTURE_NAME);
			super.texture = this.positionTextures[0];
			super.setMeasurements(texture.getRegionWidth(), texture.getRegionHeight());
			break;
		default:
			throw new IllegalGameObjectException("Car type" + Integer.toString(type));
		}
	}
}
