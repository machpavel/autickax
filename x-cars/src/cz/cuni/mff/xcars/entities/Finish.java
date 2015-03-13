package cz.cuni.mff.xcars.entities;

import java.io.Externalizable;

import com.badlogic.gdx.math.Vector2;

import cz.cuni.mff.xcars.constants.Constants;

public class Finish extends VisuallyShiftableGameObject implements Externalizable {
	public static final String name = Constants.gameObjects.FINISH_NAME;
	Vector2 visualShift = new Vector2(0, 0);

	public Finish(float x, float y, int type) {
		super(x, y, type);
		this.boundingCircleRadius = Constants.gameObjects.FINISH_BOUNDING_RADIUS;
	}

	/** Parameterless constructor for the externalization */
	public Finish() {
	}

	public Finish(GameObject object) {
		super(object);
	}

	@Override
	public String getName() {
		return name;
	}

	/** Gets the texture name according to a type */
	public static String GetTextureName(int type) {
		return Constants.gameObjects.GAME_OBJECTS_TEXTURE_PREFIX + name + '/' + name + type;
	}

	@Override
	public GameObject copy() {
		return new Finish(this);
	}

	@Override
	public void setTexture(int type) {
		super.setTexture(Finish.GetTextureName(type));
	}

	public float getBoundingRadius() {
		return this.boundingCircleRadius;
	}

}
