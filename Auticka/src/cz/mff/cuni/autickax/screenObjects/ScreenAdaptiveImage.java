package cz.mff.cuni.autickax.screenObjects;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

import cz.mff.cuni.autickax.input.Input;

public class ScreenAdaptiveImage extends Image {
	
	public ScreenAdaptiveImage(TextureRegion graphics) {
		super(graphics);
	}
	
	public ScreenAdaptiveImage(Drawable drawableGraphics) {
		super(drawableGraphics);
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
		return super.getWidth() * Input.xStretchFactor;
	}
	
	public float getActualHeight() {
		return super.getHeight() * Input.yStretchFactor;
	}
	
	public float getActualX() {
		return super.getX() * Input.xStretchFactor;
	}
	public float getActualY() {
		return super.getY() * Input.yStretchFactor;
	}
	
	public void setWidth(float width){
		super.setWidth(width * Input.xStretchFactorInv);
	}
	
	public void setHeight(float height){
		super.setHeight(height * Input.yStretchFactorInv);
	}
	
	public void setX(float x){
		super.setX(x * Input.xStretchFactorInv);
	}
	
	public void setY(float y){
		super.setY(y * Input.yStretchFactorInv);
	}
	
	public void setCenterPosition(float x, float y) {
		super.setPosition (
			x * Input.xStretchFactorInv - this.getWidth() / 2,
			y * Input.yStretchFactorInv - this.getHeight() / 2
		);
	}
	
	@Override
	public void setPosition(float x, float y) {
		super.setPosition(x * Input.xStretchFactorInv, y * Input.yStretchFactorInv);
	}
}
