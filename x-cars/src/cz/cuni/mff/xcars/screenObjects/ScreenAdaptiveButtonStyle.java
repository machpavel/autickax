package cz.cuni.mff.xcars.screenObjects;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

import cz.cuni.mff.xcars.Xcars;

public class ScreenAdaptiveButtonStyle extends TextButtonStyle {
	ScreenAdaptiveButtonStyle(TextureRegion image, TextureRegion imagePressed, TextureRegion disabled, BitmapFont font) {
		this.up = new TextureRegionDrawable(image);
		// this.over = new TextureRegionDrawable(imageHover);
		// Since the target device is mobile, there is no "over".
		this.over = null;
		this.down = new TextureRegionDrawable(imagePressed);
		if (disabled != null) {
			this.disabled = new TextureRegionDrawable(disabled);
		}

		this.font = font;
	}

	ScreenAdaptiveButtonStyle(TextureRegion image, TextureRegion imagePressed) {
		this(image, imagePressed, null, Xcars.getInstance().assets.getMenuFont());
	}

	ScreenAdaptiveButtonStyle(TextureRegion image, TextureRegion imagePressed, TextureRegion disabled) {
		this(image, imagePressed, disabled, Xcars.getInstance().assets.getMenuFont());
	}
}
