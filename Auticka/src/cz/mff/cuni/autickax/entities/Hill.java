package cz.mff.cuni.autickax.entities;

import java.io.Externalizable;

import cz.mff.cuni.autickax.constants.Constants;
import cz.mff.cuni.autickax.gamelogic.SubLevel;
import cz.mff.cuni.autickax.miniGames.GearShiftMinigame;
import cz.mff.cuni.autickax.miniGames.Minigame;
import cz.mff.cuni.autickax.scene.GameScreen;

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
		return Constants.gameObjects.GAME_OBJECTS_TEXTURE_PREFIX
				+ Constants.gameObjects.HILL_NAME + type;

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
