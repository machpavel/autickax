package cz.mff.cuni.autickax.miniGames.support;

import cz.mff.cuni.autickax.constants.Constants;
import cz.mff.cuni.autickax.entities.GameObject;
import cz.mff.cuni.autickax.entities.ShiftableGameObject;

public final class RaceMinigameCar extends ShiftableGameObject {
	public static final String name = "RaceMinigameCar";

	public RaceMinigameCar(float x, float y, int type) {
		super(x, y, type);
	}

	public RaceMinigameCar(GameObject object) {
		super(object);
	}

	public void reset() {
		super.reset();
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public GameObject copy() {
		return new RaceMinigameCar(this);
	}

	/** Gets the texture name according to a type */
	public static String GetTextureName(int type) {
		return Constants.minigames.RACE_MINIGAME_CAR_TEXTURE + Integer.toString(type);
	}

	@Override
	public void setTexture(int type) {
		super.setTexture(RaceMinigameCar.GetTextureName(type));
	}
}
