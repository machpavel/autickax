package cz.cuni.mff.autickax.screenObjects;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

public abstract class ScreenAdaptiveButton extends Button {

	public ScreenAdaptiveButton(TextureRegion image, TextureRegion imageHover) {
		super(new ScreenAdaptiveButtonStyle(image, imageHover));

		this.addListener(new InputListener() {
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				return true;
			}

			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				action();
			}
		});
	}

	public ScreenAdaptiveButton(Drawable up) {
		super(up);

		this.addListener(new InputListener() {
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				return true;
			}

			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				action();
			}
		});
	}

	public void setStyle(TextureRegion image, TextureRegion imageHover) {
		this.setStyle(new ScreenAdaptiveButtonStyle(image, imageHover));
	}

	public void setCenterPosition(float x, float y) {
		super.setPosition(x - this.getWidth() / 2, y - this.getHeight() / 2);
	}

	public abstract void action();
}
