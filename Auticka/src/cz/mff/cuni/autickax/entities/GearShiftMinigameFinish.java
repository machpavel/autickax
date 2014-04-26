package cz.mff.cuni.autickax.entities;

import java.io.Externalizable;

import cz.mff.cuni.autickax.constants.Constants;

public class GearShiftMinigameFinish extends GameObject implements Externalizable {

	public GearShiftMinigameFinish(float x, float y) {
		super(x, y, 0);
		this.boundingCircleRadius = Constants.minigames.GEAR_SHIFT_MINIGAME_FINISH_RADIUS;
	}

	/** Parameterless constructor for the externalization */
	public GearShiftMinigameFinish() {
	}

	public GearShiftMinigameFinish(GameObject object) {
		super(object);
	}

	@Override
	public String getName() {
		return "gearShiftMinigameFinish";
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
