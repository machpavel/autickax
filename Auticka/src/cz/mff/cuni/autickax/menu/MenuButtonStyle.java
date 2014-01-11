package cz.mff.cuni.autickax.menu;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class MenuButtonStyle extends ButtonStyle {
	MenuButtonStyle(TextureRegion image, TextureRegion imageHover) {
		this.up = new TextureRegionDrawable(image);
		this.over = new TextureRegionDrawable(imageHover);
		this.down = new TextureRegionDrawable(imageHover);
	}
}
