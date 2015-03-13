package cz.cuni.mff.xcars.entities;

import java.io.Externalizable;

import cz.cuni.mff.xcars.constants.Constants;

public class Start extends VisuallyShiftableGameObject implements Externalizable {
	public static final String name = Constants.gameObjects.START_NAME;

	public Start(float x, float y, int type) {
		super(x, y, type);
	}

	/** Parameterless constructor for the externalization */
	public Start() {
	}

	public Start(GameObject object) {
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
	public void setTexture(int type) {
		super.setTexture(Start.GetTextureName(type));
	}

	@Override
	public GameObject copy() {
		return new Start(this);
	}
}
