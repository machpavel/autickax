package cz.mff.cuni.autickax.entities;

import java.io.Externalizable;

import cz.mff.cuni.autickax.constants.Constants;

public final class Wall extends GameTerminatingObject implements Externalizable {
	public static final String name = Constants.gameObjects.WALL_NAME;

	public Wall(float x, float y, int type) {
		super(x, y, type);
	}

	public Wall(GameObject object) {
		super(object);
	}

	/** Parameterless constructor for the externalization */
	public Wall() {
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public GameObject copy() {
		return new Wall(this);
	}

	@Override
	public String getSoundName() {
		// TODO add custom sounds!
		return Constants.sounds.SOUND_TREE;
	}

	@Override
	protected String getResultMessage() {
		return Constants.gameObjects.GAME_OBJECTS_TEXTURE_PREFIX
				+ Constants.strings.TOOLTIP_MINIGAME_CRASHED_WALL_RESULT_MESSAGE;
	}

	/** Gets the texture name according to a type */
	@Override
	public String GetStaticTextureName(int type) {
		return Constants.gameObjects.WALL_NAME + type;
	}
}
