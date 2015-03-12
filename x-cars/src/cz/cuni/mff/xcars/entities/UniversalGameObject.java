package cz.cuni.mff.xcars.entities;

import java.io.Externalizable;

import cz.cuni.mff.xcars.constants.Constants;

public class UniversalGameObject extends GameObject implements Externalizable {
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

	@Override
	public void scaleBy(float scaleX, float scaleY) {
		float epsilon = 0.0000001f;
		if (this.getScaleX() >= 0) {
			if (this.getScaleX() + scaleX < 0)
				scaleX = -(this.getScaleX() - epsilon);
		} else if (this.getScaleX() < 0) {
			scaleX *= -1;
			if (this.getScaleX() + scaleX > 0)
				scaleX = -(this.getScaleX() + epsilon);
		}

		if (this.getScaleY() > 0) {
			if (this.getScaleY() + scaleY < 0)
				scaleY = -(this.getScaleY() - epsilon);
		} else if (this.getScaleY() < 0) {
			scaleY *= -1;
			if (this.getScaleY() + scaleY > 0)
				scaleY = -(this.getScaleY() + epsilon);
		}
		super.scaleBy(scaleX, scaleY);
	}
	
}
