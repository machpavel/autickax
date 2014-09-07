package cz.cuni.mff.xcars.entities;

import java.io.Externalizable;

import cz.cuni.mff.xcars.constants.Constants;
import cz.cuni.mff.xcars.gamelogic.SubLevel;
import cz.cuni.mff.xcars.miniGames.GearShiftMinigame;
import cz.cuni.mff.xcars.miniGames.Minigame;
import cz.cuni.mff.xcars.scene.GameScreen;

public final class Hill extends GameObject implements Externalizable {
	public static final String name = Constants.gameObjects.HILL_NAME;

	public Hill(float x, float y, int type) {
		super(x, y, type);
	}

	public Hill(GameObject object) {
		super(object);
	}

	/** Parameterless constructor for the externalization */
	public Hill() {
	}

	@Override
	public String getName() {
		return name;
	}

	/** Gets the texture name according to a type */
	public static String GetTextureName(int type) {
		return Constants.gameObjects.GAME_OBJECTS_TEXTURE_PREFIX + name + '/'
				+ name + type;

	}

	@Override
	public GameObject copy() {
		return new Hill(this);
	}

	@Override
	public void setTexture(int type) {
		super.setTexture(Hill.GetTextureName(type));
	}

	@Override
	public Minigame getMinigame(GameScreen gameScreen, SubLevel parent) {
		return new GearShiftMinigame(gameScreen, parent);
	}
}
