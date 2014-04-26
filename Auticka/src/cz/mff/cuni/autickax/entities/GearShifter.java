package cz.mff.cuni.autickax.entities;

import java.io.Externalizable;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;

import cz.mff.cuni.autickax.constants.Constants;
import cz.mff.cuni.autickax.input.Input;

public final class GearShifter extends ShiftableGameObject implements Externalizable {
	private boolean isDragged = false;

	public GearShifter(float x, float y) {
		super(x, y, 0);
	}

	public GearShifter(GameObject object) {
		super(object);
	}

	/** Parameterless constructor for the externalization */
	public GearShifter() {
	}

	public boolean isDragged() {
		return this.isDragged;
	}

	public void setDragged(boolean value) {
		this.isDragged = value;
	}

	@Override
	public String getName() {
		return "gearShifter";
	}

	@Override
	public void update(float delta) {
		if (this.isDragged()) {
			if (Gdx.input.isTouched()) {
				Vector2 touchPos = new Vector2(Input.getX(), Input.getY());
				super.move(touchPos);
			}
		}

		if (!Gdx.input.isTouched())
			setDragged(false);
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
