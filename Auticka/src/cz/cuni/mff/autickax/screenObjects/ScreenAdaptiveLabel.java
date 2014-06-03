package cz.cuni.mff.autickax.screenObjects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

import cz.cuni.mff.autickax.Autickax;

public class ScreenAdaptiveLabel extends Label {

	private ScreenAdaptiveLabel(String text, BitmapFont font) {
		super(text, new LabelStyle(font, Color.WHITE));
	}

	static public ScreenAdaptiveLabel getDialogLabel(String text) {
		return new ScreenAdaptiveLabel(text, Autickax.getInstance().assets.getDialogFont());
	}

	static public ScreenAdaptiveLabel getMenuLabel(String text) {
		return new ScreenAdaptiveLabel(text, Autickax.getInstance().assets.getMenuFont());
	}

	static public ScreenAdaptiveLabel getCompleteLevelDialogLabel(String text) {
		return new ScreenAdaptiveLabel(text, Autickax.getInstance().assets.getFinishDialogFont());
	}

	public void setCenterPosition(float x, float y) {
		super.setPosition(x - this.getWidth() / 2, y - this.getHeight() / 2);
	}

}
