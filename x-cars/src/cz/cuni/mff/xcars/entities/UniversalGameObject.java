package cz.cuni.mff.xcars.entities;

import java.io.Externalizable;

import cz.cuni.mff.xcars.constants.Constants;

public final class UniversalGameObject extends GameObject implements Externalizable {
	public static final String name = Constants.gameObjects.UNIVERSAL_NAME;

	public UniversalGameObject(float x, float y, int type) {
		super(x, y, type);
	}

	public UniversalGameObject(GameObject object) {
		super(object);
	}

	/** Parameterless constructor for the externalization */
	public UniversalGameObject() {
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
		return new UniversalGameObject(this);
	}

	@Override
	public void setTexture(int type) {
		super.setTexture(UniversalGameObject.GetTextureName(type));
	}
}
