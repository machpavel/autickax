package cz.mff.cuni.autickax.entities;

import java.io.Externalizable;

import cz.mff.cuni.autickax.constants.Constants;

public final class House extends GameTerminatingObject implements Externalizable {

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
		return "house";
	}

	@Override
	public GameObject copy() {
		return new House(this);
	}

	@Override
	public String getSoundName() {
		assert(false); // add custom sounds!
		return Constants.sounds.SOUND_TREE;
	}

	@Override
	protected String getResultMessage() {
		return Constants.strings.TOOLTIP_MINIGAME_CRASHED_HOUSE_RESULT_MESSAGE;
	}
}
