package cz.mff.cuni.autickax;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

import cz.mff.cuni.autickax.myInputListener.MyInputListenerForBackground;

public class EditorBackgroundButton extends Button {
	EditorScreen screen;
	int i;
	public EditorBackgroundButton(TextureRegion image, int i, EditorScreen screen) {
		super(new TextureRegionDrawable(image));
		this.screen = screen;
		this.i = i;
		this.addListener(new MyInputListenerForBackground(i, screen));
	}
		
	@Override
	public float getPrefHeight() {
		return 18;
	}
	@Override
	public float getPrefWidth() {
		return 30;
	}
}
