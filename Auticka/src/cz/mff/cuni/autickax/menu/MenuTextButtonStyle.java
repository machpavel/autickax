package cz.mff.cuni.autickax.menu;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

import cz.mff.cuni.autickax.Autickax;
import cz.mff.cuni.autickax.input.Input;

public class MenuTextButtonStyle extends TextButtonStyle {
	MenuTextButtonStyle(TextureRegion image, TextureRegion imageHover, TextureRegion disabled, BitmapFont font) {
		this.up = new TextureRegionDrawable(image);
		this.over = new TextureRegionDrawable(imageHover);
		this.down = new TextureRegionDrawable(imageHover);
		
		if (disabled != null) {
			this.disabled = new TextureRegionDrawable(disabled);
		}
		
		this.font = font;
		this.font.setScale(Input.xStretchFactorInv, Input.yStretchFactorInv);
	}
	
	MenuTextButtonStyle(TextureRegion image, TextureRegion imageHover) {
		this(image, imageHover, null, Autickax.getInstance().assets.getMenuFont());
	}
}
