package cz.mff.cuni.autickax.screenObjects;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Button;

import cz.mff.cuni.autickax.input.Input;

public abstract class ScreenAdaptiveButton extends Button {
	
	public ScreenAdaptiveButton(TextureRegion image, TextureRegion imageHover) {
		super(new ScreenAdaptiveButtonStyle(image, imageHover));
		
		this.addListener(new InputListener() {
			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int button) {
				return true;
			}

			public void touchUp(InputEvent event, float x, float y,
					int pointer, int button) {
				action();
			}
		});
	}
	
	public void setStyle(TextureRegion image, TextureRegion imageHover) {
		this.setStyle(new ScreenAdaptiveButtonStyle(image, imageHover));
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
			x * Input.xStretchFactorInv - this.getWidth() / 2,
			y * Input.yStretchFactorInv - this.getHeight() / 2
		);
	}
	
	@Override
	public void setPosition(float x, float y) {
		super.setPosition(x * Input.xStretchFactorInv, y * Input.yStretchFactorInv);
	}
	
	public abstract void action();
}
