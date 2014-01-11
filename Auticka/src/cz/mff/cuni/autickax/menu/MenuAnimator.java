package cz.mff.cuni.autickax.menu;

import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;

import cz.mff.cuni.autickax.input.Input;

public class MenuAnimator {
	static public MoveToAction moveTo (float x, float y, float duration, Interpolation interpolation) {
		MoveToAction action = Actions.action(MoveToAction.class);
		action.setPosition(x * Input.xStretchFactorInv, y * Input.yStretchFactorInv);
		action.setDuration(duration);
		action.setInterpolation(interpolation);
		return action;
	}
}
