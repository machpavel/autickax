package cz.mff.cuni.autickax.screenObjects;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

public class ScreenAdaptiveImage extends Image {

	public ScreenAdaptiveImage(TextureRegion graphics) {
		super(graphics);
	}

	public ScreenAdaptiveImage(Drawable drawableGraphics) {
		super(drawableGraphics);
	}

	public void setCenterPosition(float x, float y) {
		super.setPosition(x - this.getWidth() / 2, y - this.getHeight() / 2);
	}
}
