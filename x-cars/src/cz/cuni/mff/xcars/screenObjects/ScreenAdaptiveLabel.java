package cz.cuni.mff.xcars.screenObjects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

import cz.cuni.mff.xcars.Xcars;

public class ScreenAdaptiveLabel extends Label {

	private ScreenAdaptiveLabel(String text, BitmapFont font) {
		super(text, new LabelStyle(font, Color.WHITE));
	}

	static public ScreenAdaptiveLabel getDialogLabel(String text) {
		return new ScreenAdaptiveLabel(text, Xcars.getInstance().assets.getDialogFont());
	}

	static public ScreenAdaptiveLabel getMenuLabel(String text) {
		return new ScreenAdaptiveLabel(text, Xcars.getInstance().assets.getMenuFont());
	}

	static public ScreenAdaptiveLabel getCompleteLevelDialogLabel(String text) {
		return new ScreenAdaptiveLabel(text, Xcars.getInstance().assets.getFinishDialogFont());
	}

	public void setCenterPosition(float x, float y) {
		super.setPosition(x - this.getWidth() / 2, y - this.getHeight() / 2);
	}

}
