package cz.cuni.mff.xcars.miniGames.support;

import java.io.Externalizable;

import cz.cuni.mff.xcars.constants.Constants;
import cz.cuni.mff.xcars.entities.GameObject;
import cz.cuni.mff.xcars.entities.ShiftableGameObject;

public final class GearShifter extends ShiftableGameObject implements Externalizable {

	public GearShifter(float x, float y) {
		super(x, y, 0);
		this.setCanBeDragged(true);
	}

	public GearShifter(GameObject object) {
		super(object);
		this.setCanBeDragged(true);
	}
	
	@Override
	public void update(float delta) {
		this.updateDragging(delta);
	}

	@Override
	public String getName() {
		return "gearShifter";
	}

	@Override
	public GameObject copy() {
		return new GearShifter(this);
	}

	@Override
	public void setTexture(int type) {
		super.setTexture(Constants.minigames.GEAR_SHIFTER_TEXTURE);
	}

}
