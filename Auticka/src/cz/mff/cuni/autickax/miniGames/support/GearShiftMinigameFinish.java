package cz.mff.cuni.autickax.miniGames.support;

import java.io.Externalizable;

import cz.mff.cuni.autickax.constants.Constants;
import cz.mff.cuni.autickax.entities.GameObject;

public class GearShiftMinigameFinish extends GameObject implements Externalizable {
	public static final String name = Constants.minigames.GEAR_SHIFT_MINIGAME_FINISH_TEXTURE;

	public GearShiftMinigameFinish(float x, float y) {
		super(x, y, 0);
		this.boundingCircleRadius = Constants.minigames.GEAR_SHIFT_MINIGAME_FINISH_RADIUS;
	}


	public GearShiftMinigameFinish(GameObject object) {
		super(object);
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void setTexture(int type) {
		super.setTexture(Constants.minigames.GEAR_SHIFT_MINIGAME_FINISH_TEXTURE);
	}

	@Override
	public GameObject copy() {
		return new GearShiftMinigameFinish(this);
	}

}
