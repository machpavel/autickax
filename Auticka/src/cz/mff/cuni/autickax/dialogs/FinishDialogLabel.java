package cz.mff.cuni.autickax.dialogs;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

import cz.mff.cuni.autickax.Autickax;
import cz.mff.cuni.autickax.input.Input;

public class FinishDialogLabel extends Label {
	
	public FinishDialogLabel(String text) {
		super(text, new LabelStyle(Autickax.getInstance().assets.getFinishDialogFont(), Color.WHITE));
	}
	
	@Override
	public float getPrefHeight() {
		return super.getPrefHeight() * Input.yStretchFactorInv;
	}
	@Override
	public float getPrefWidth() {
		return super.getPrefWidth() * Input.xStretchFactorInv;
	}
	
	@Override
	public void setPosition(float x, float y) {
		super.setPosition(x * Input.xStretchFactorInv, y * Input.yStretchFactorInv);
	}
}
