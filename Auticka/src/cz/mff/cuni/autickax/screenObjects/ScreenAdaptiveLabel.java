package cz.mff.cuni.autickax.screenObjects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

import cz.mff.cuni.autickax.Autickax;
import cz.mff.cuni.autickax.input.Input;

public class ScreenAdaptiveLabel extends Label {

	private ScreenAdaptiveLabel(String text, BitmapFont font) {
		super(text, new LabelStyle(font, Color.WHITE));
		this.setFontScale(Input.xStretchFactorInv, Input.yStretchFactorInv);
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
	
	/*@Override
	public float getPrefHeight() {
		return super.getPrefHeight() * Input.yStretchFactorInv;
	}
	@Override
	public float getPrefWidth() {
		return super.getPrefWidth() * Input.xStretchFactorInv;
	}*/
	
	public float getActualWidth() {
		return super.getWidth() * Input.xStretchFactorInv;
	}
	
	public float getActualHeight() {
		return super.getHeight() * Input.yStretchFactorInv;
	}
	
	public void setCenterPosition(float x, float y) {
		super.setPosition (
			x * Input.xStretchFactorInv - this.getActualWidth() / 2,
			y * Input.yStretchFactorInv - this.getHeight() / 2
			// Strange different behavior in the axes x and y;
			// Propably bug in the libgdx?
		);
	}
		
	@Override
	public float getX() {
		return super.getX() * Input.xStretchFactor; 
	}
	
	@Override
	public float getY() {
		return super.getY() * Input.yStretchFactor; 
	}
	// TODO why the fuck does it work only if x position is square scaled???? 
	@Override
	public void setPosition(float x, float y) {
		super.setPosition(x * Input.xStretchFactorInv * Input.xStretchFactorInv, y * Input.yStretchFactorInv);
	}
}
