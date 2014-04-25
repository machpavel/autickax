package cz.mff.cuni.autickax.entities;

import java.io.Externalizable;

import cz.mff.cuni.autickax.constants.Constants;

public final class ParkingCar extends GameTerminatingObject implements Externalizable {

	public ParkingCar(float x, float y, int type) {
		super(x, y, type);
	}

	public ParkingCar(GameObject object) {
		super(object);
	}

	/** Parameterless constructor for the externalization */
	public ParkingCar() {
	}

	@Override
	public String getName() {
		return "parkingCar";
	}

	@Override
	public GameObject copy() {
		return new ParkingCar(this);
	}

	@Override
	public String getSoundName() {
		assert(false); // add custom sounds!
		return Constants.sounds.SOUND_TREE;
	}

	@Override
	protected String getResultMessage() {
		return Constants.strings.TOOLTIP_MINIGAME_CRASHED_PARKING_CAR_RESULT_MESSAGE;
	}
}
