package cz.mff.cuni.autickax.menu;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

import cz.mff.cuni.autickax.Autickax;

public class MenuTextButtonStyle extends TextButtonStyle {
	MenuTextButtonStyle(TextureRegion image, TextureRegion imageHover) {
		this.up = new TextureRegionDrawable(image);
		this.over = new TextureRegionDrawable(imageHover);
		this.down = new TextureRegionDrawable(imageHover);
		this.font = Autickax.getInstance().assets.getMenuFont();
	}
}
