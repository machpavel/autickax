package cz.mff.cuni.autickax.menu;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import cz.mff.cuni.autickax.Autickax;
import cz.mff.cuni.autickax.input.Input;

public class MenuLabel extends Label {
	
	public MenuLabel(String text) {
		super(text, new LabelStyle(Autickax.getInstance().assets.getMenuFont(), Color.WHITE));
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
