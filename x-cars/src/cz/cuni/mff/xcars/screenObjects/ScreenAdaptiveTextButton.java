package cz.cuni.mff.xcars.screenObjects;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

import cz.cuni.mff.xcars.Xcars;

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
		this(text, image, imageHover, disabled, Xcars.getInstance().assets.getMenuFont(), true);
	}

	public ScreenAdaptiveTextButton(String text, TextureRegion image, TextureRegion imageHover,
			BitmapFont font) {
		this(text, image, imageHover, null, font, true);
	}

	public ScreenAdaptiveTextButton(String text, TextureRegion image, TextureRegion imageHover) {
		this(text, image, imageHover, null, Xcars.getInstance().assets.getMenuFont(), true);
	}

	public void setCenterPosition(float x, float y) {
		super.setPosition(x - this.getWidth() / 2, y - this.getHeight() / 2);
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
