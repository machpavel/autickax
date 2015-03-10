package cz.cuni.mff.xcars.entities;

import java.io.Externalizable;

import cz.cuni.mff.xcars.constants.Constants;

public class Arrow extends UniversalGameObject implements Externalizable {
	public static final String name = Constants.gameObjects.ARROW_NAME;
	float length = -1;

	public Arrow(float x, float y, int type) {
		super(x, y, type);
	}

	public Arrow(GameObject object) {
		super(object);
	}

	/** Parameterless constructor for the externalization */
	public Arrow() {
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
		return new Arrow(this);
	}

	@Override
	public void setTexture(int type) {
		this.setTexture(Arrow.GetTextureName(type));
	}
}
