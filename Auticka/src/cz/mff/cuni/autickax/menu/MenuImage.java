package cz.mff.cuni.autickax.menu;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

import cz.mff.cuni.autickax.input.Input;

public class MenuImage extends Image {
	
	public MenuImage(TextureRegion graphics) {
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
	
	@Override
	public void setPosition(float x, float y) {
		super.setPosition(x * Input.xStretchFactorInv, y * Input.yStretchFactorInv);
	}
}
