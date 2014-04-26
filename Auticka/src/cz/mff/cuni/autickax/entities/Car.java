package cz.mff.cuni.autickax.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.XmlReader.Element;

import cz.mff.cuni.autickax.Autickax;
import cz.mff.cuni.autickax.constants.Constants;
import cz.mff.cuni.autickax.exceptions.IllegalGameObjectException;
import cz.mff.cuni.autickax.input.Input;

public final class Car extends ShiftableGameObject {

	private boolean isDragged = false;
	private TextureRegion[] positionTextures;
	private Vector2 lastCarPosition;
	private float lastRotationDistance = 0;

	/** Parameterless constructor for the externalization */
	public Car() {
	}

	public Car(float x, float y, int type) {
		super(x, y, type);
		this.lastCarPosition = new Vector2(x, y);
	}

	public static Car parseCar(Element car) {
		return new Car(car.getFloat("X"), car.getFloat("Y"), car.getInt("type", 1));
	}

	public Car(GameObject object) {
		super(object);
	}

	public void reset() {
		super.reset();
		this.isDragged = false;
		this.lastCarPosition = new Vector2(0, 0);
		this.lastRotationDistance = 0;
	}

	public boolean isDragged() {
		return this.isDragged;
	}

	public void setDragged(boolean value) {
		this.isDragged = value;
	}

	@Override
	public String getName() {
		return "car";
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
	public void update(float delta) {
		if (this.isDragged()) {
			if (Gdx.input.isTouched()) {
				Vector2 touchPos = new Vector2(Input.getX(), Input.getY());
				this.move(touchPos);
			}
		}

		if (!Gdx.input.isTouched())
			setDragged(false);
	}

	@Override
	public void draw(SpriteBatch batch) {
		while (this.rotation < 0)
			this.rotation += 360;
		if (this.rotation > 360)
			this.rotation = this.rotation % 360;

		if (this.rotation >= 22.5 && this.rotation < 67.5)
			this.setTexture(positionTextures[1]);
		else if (this.rotation >= 67.5 && this.rotation < 112.5)
			this.setTexture(positionTextures[2]);
		else if (this.rotation >= 112.5 && this.rotation < 157.5)
			this.setTexture(positionTextures[3]);
		else if (this.rotation >= 157.5 && this.rotation < 202.5)
			this.setTexture(positionTextures[4]);
		else if (this.rotation >= 202.5 && this.rotation < 247.5)
			this.setTexture(positionTextures[5]);
		else if (this.rotation >= 247.5 && this.rotation < 292.5)
			this.setTexture(positionTextures[6]);
		else if (this.rotation >= 292.5 && this.rotation < 337.5)
			this.setTexture(positionTextures[7]);
		else if ((this.rotation >= 337.5 && this.rotation < 360)
				|| (this.rotation >= 0 && this.rotation < 22.5))
			this.setTexture(positionTextures[0]);

		super.draw(batch);
	}

	@Override
	public GameObject copy() {
		return new Car(this);
	}

	@Override
	public void setTexture(int type) {
		if (Autickax.getInstance() == null)
			return;
		if (this.positionTextures == null)
			this.positionTextures = new TextureRegion[8];
		switch (type) {
		case 1:
			this.positionTextures[0] = Autickax.getInstance().assets
					.getGraphics(Constants.gameObjects.CAR_TYPE_1_POSITION_0_TEXTURE_NAME);
			this.positionTextures[1] = Autickax.getInstance().assets
					.getGraphics(Constants.gameObjects.CAR_TYPE_1_POSITION_1_TEXTURE_NAME);
			this.positionTextures[2] = Autickax.getInstance().assets
					.getGraphics(Constants.gameObjects.CAR_TYPE_1_POSITION_2_TEXTURE_NAME);
			this.positionTextures[3] = Autickax.getInstance().assets
					.getGraphics(Constants.gameObjects.CAR_TYPE_1_POSITION_3_TEXTURE_NAME);
			this.positionTextures[4] = Autickax.getInstance().assets
					.getGraphics(Constants.gameObjects.CAR_TYPE_1_POSITION_4_TEXTURE_NAME);
			this.positionTextures[5] = Autickax.getInstance().assets
					.getGraphics(Constants.gameObjects.CAR_TYPE_1_POSITION_5_TEXTURE_NAME);
			this.positionTextures[6] = Autickax.getInstance().assets
					.getGraphics(Constants.gameObjects.CAR_TYPE_1_POSITION_6_TEXTURE_NAME);
			this.positionTextures[7] = Autickax.getInstance().assets
					.getGraphics(Constants.gameObjects.CAR_TYPE_1_POSITION_7_TEXTURE_NAME);
			super.texture = this.positionTextures[0];
			super.setMeasurements(texture.getRegionWidth(), texture.getRegionHeight());
			break;
		default:
			throw new IllegalGameObjectException("Car type" + Integer.toString(type));
		}
	}
}
