package cz.mff.cuni.autickax.entities;

import java.io.Externalizable;

import cz.mff.cuni.autickax.constants.Constants;

public final class ParkingCar extends GameTerminatingObject implements Externalizable {
	public static final String name = Constants.gameObjects.PARKING_CAR_NAME;

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
		return name;
	}

	@Override
	public GameObject copy() {
		return new ParkingCar(this);
	}

	@Override
	public String getSoundName() {
		// TODO add custom sounds!
		return Constants.sounds.SOUND_TREE;
	}

	@Override
	protected String getResultMessage() {
		return Constants.strings.TOOLTIP_MINIGAME_CRASHED_PARKING_CAR_RESULT_MESSAGE;
	}

	/** Gets the texture name according to a type */
	public static String GetStaticTextureName(int type) {
		return Constants.gameObjects.GAME_OBJECTS_TEXTURE_PREFIX
				+ Constants.gameObjects.PARKING_CAR_NAME + type;
	}

	@Override
	public String GetTextureName(int type) {
		return GetStaticTextureName(type);
	}
}
