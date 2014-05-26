package cz.mff.cuni.autickax.screenObjects;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

import cz.mff.cuni.autickax.Autickax;
import cz.mff.cuni.autickax.input.Input;

public abstract class ScreenAdaptiveTextButton extends TextButton {

	private final String text;

	public ScreenAdaptiveTextButton(String text, TextureRegion image, TextureRegion imageHover,
			TextureRegion disabled, BitmapFont font, boolean hasListener) {
		super(text, new ScreenAdaptiveButtonStyle(image, imageHover, disabled, font));

		this.text = text;

		if (hasListener) {
			this.addListener(new InputListener() {
				public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
					return true;
				}

				public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
					action();
				}
			});
		}
	}

	public ScreenAdaptiveTextButton(String text, TextureRegion image, TextureRegion imageHover,
			BitmapFont font, TextureRegion disabled) {
		this(text, image, imageHover, disabled, font, true);
	}

	public ScreenAdaptiveTextButton(String text, TextureRegion image, TextureRegion imageHover,
			TextureRegion disabled) {
		this(text, image, imageHover, disabled, Autickax.getInstance().assets.getMenuFont(), true);
	}

	public ScreenAdaptiveTextButton(String text, TextureRegion image, TextureRegion imageHover,
			BitmapFont font) {
		this(text, image, imageHover, null, font, true);
	}

	public ScreenAdaptiveTextButton(String text, TextureRegion image, TextureRegion imageHover) {
		this(text, image, imageHover, null, Autickax.getInstance().assets.getMenuFont(), true);
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

	public void setCenterPosition(float x, float y) {
		super.setPosition(x * Input.xStretchFactorInv - this.getWidth() / 2, y
				* Input.yStretchFactorInv - this.getHeight() / 2);
	}

	@Override
	public void setPosition(float x, float y) {
		super.setPosition(x * Input.xStretchFactorInv, y * Input.yStretchFactorInv);
	}

	@Override
	public void setDisabled(boolean isDisabled) {
		super.setDisabled(isDisabled);

		if (isDisabled) {
			this.setText("");
		} else {
			this.setText(this.text);
		}
	}

	public abstract void action();
}
