package cz.cuni.mff.xcars.entities;

import java.io.Externalizable;

import cz.cuni.mff.xcars.constants.Constants;

public final class House extends GameTerminatingObject implements
		Externalizable {
	public static final String name = Constants.gameObjects.HOUSE_NAME;

	public House(float x, float y, int type) {
		super(x, y, type);
	}

	public House(GameObject object) {
		super(object);
	}

	/** Parameterless constructor for the externalization */
	public House() {
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public GameObject copy() {
		return new House(this);
	}

	@Override
	public String getSoundName() {
		// TODO add custom sounds!
		return Constants.sounds.SOUND_TREE;
	}

	@Override
	protected String getResultMessage() {
		return Constants.strings.TOOLTIP_MINIGAME_CRASHED_HOUSE_RESULT_MESSAGE;
	}

	/** Gets the texture name according to a type */
	public static String GetStaticTextureName(int type) {
		return Constants.gameObjects.GAME_OBJECTS_TEXTURE_PREFIX + name + '/'
				+ name + type;
	}

	@Override
	public String GetTextureName(int type) {
		return GetStaticTextureName(type);
	}
}
