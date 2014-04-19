package cz.mff.cuni.autickax.screenObjects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

import cz.mff.cuni.autickax.Autickax;
import cz.mff.cuni.autickax.input.Input;

public class ScreenAdaptiveLabel extends Label {
	
	private ScreenAdaptiveLabel(String text) {
		super(text, new LabelStyle(Autickax.getInstance().assets.getMenuFont(), Color.WHITE));
	}
	
	private ScreenAdaptiveLabel(String text, BitmapFont font) {
		super(text, new LabelStyle(font, Color.WHITE));
	}
	
	static public ScreenAdaptiveLabel getDialogLabel(String text) {
		return new ScreenAdaptiveLabel(text, Autickax.getInstance().assets.getDialogFont());
	}
	
	static public ScreenAdaptiveLabel getMenuLabel(String text) {
		return new ScreenAdaptiveLabel(text, Autickax.getInstance().assets.getMenuFont());
	}
	
	static public ScreenAdaptiveLabel getCompleteLevelDialogLabel(String text) {
		return new ScreenAdaptiveLabel(text, Autickax.getInstance().assets.getFinishDialogFont());
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
