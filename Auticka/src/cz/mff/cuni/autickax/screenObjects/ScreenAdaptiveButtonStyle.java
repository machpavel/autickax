package cz.mff.cuni.autickax.screenObjects;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

import cz.mff.cuni.autickax.Autickax;

public class ScreenAdaptiveButtonStyle extends TextButtonStyle {
	ScreenAdaptiveButtonStyle(TextureRegion image, TextureRegion imageHover, TextureRegion disabled, BitmapFont font) {
		this.up = new TextureRegionDrawable(image);
		this.over = new TextureRegionDrawable(imageHover);
		this.down = new TextureRegionDrawable(imageHover);
		
		if (disabled != null) {
			this.disabled = new TextureRegionDrawable(disabled);
		}
		
		this.font = font;		
	}
	
	ScreenAdaptiveButtonStyle(TextureRegion image, TextureRegion imageHover) {
		this(image, imageHover, null, Autickax.getInstance().assets.getMenuFont());
	}
}
