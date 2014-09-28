package cz.cuni.mff.xcars.screenObjects;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class ScreenAdaptiveImage extends Image {

	public ScreenAdaptiveImage(TextureRegion texture) {
		super(texture);
	}

	public ScreenAdaptiveImage(TextureRegionDrawable texture) {
		super(texture);
	}

	public ScreenAdaptiveImage() {
		super();
	}

	public void setCenterPosition(float x, float y) {
		super.setPosition(x - this.getWidth() / 2, y - this.getHeight() / 2);
	}
}
