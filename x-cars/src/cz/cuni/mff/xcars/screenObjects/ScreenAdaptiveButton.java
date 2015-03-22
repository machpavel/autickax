package cz.cuni.mff.xcars.screenObjects;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Button;

public abstract class ScreenAdaptiveButton extends Button {

	public ScreenAdaptiveButton(TextureRegion image, TextureRegion imagePressed, TextureRegion disabled,
			boolean hasListener) {
		super(new ScreenAdaptiveButtonStyle(image, imagePressed, disabled));

		if (hasListener) {
			this.addListener(new InputListener() {
				public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
					return true;
				}

				public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
					if (x >= 0 && x < getWidth() && y >= 0 && y < getHeight()) {
						action();
					}
				}
			});
		}
	}

	public ScreenAdaptiveButton(TextureRegion image, TextureRegion imagePressed) {
		this(image, imagePressed, null, true);
	}

	public void setStyle(TextureRegion image, TextureRegion imageHover) {
		this.setStyle(new ScreenAdaptiveButtonStyle(image, imageHover));
	}

	public void setCenterPosition(float x, float y) {
		super.setPosition(x - this.getWidth() / 2, y - this.getHeight() / 2);
	}

	public abstract void action();
}
