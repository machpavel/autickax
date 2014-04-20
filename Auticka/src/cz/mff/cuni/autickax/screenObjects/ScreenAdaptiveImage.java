package cz.mff.cuni.autickax.screenObjects;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

import cz.mff.cuni.autickax.input.Input;

public class ScreenAdaptiveImage extends Image {
	
	public ScreenAdaptiveImage(TextureRegion graphics) {
		super(graphics);
	}
	
	@Override
	public float getPrefHeight() {
		return super.getPrefHeight() * Input.yStretchFactorInv;
	}
	@Override
	public float getPrefWidth() {
		return super.getPrefWidth() * Input.xStretchFactorInv;
	}
	
	public float getActualWidth() {
		return super.getWidth() * Input.xStretchFactorInv;
	}
	
	public float getActualHeight() {
		return super.getHeight() * Input.yStretchFactorInv;
	}
	
	public void setCenterPosition(float x, float y) {
		super.setPosition (
			x * Input.xStretchFactorInv - this.getActualWidth() / 2,
			y * Input.yStretchFactorInv - this.getActualHeight() / 2
		);
	}
	
	@Override
	public void setPosition(float x, float y) {
		super.setPosition(x * Input.xStretchFactorInv, y * Input.yStretchFactorInv);
	}
}
