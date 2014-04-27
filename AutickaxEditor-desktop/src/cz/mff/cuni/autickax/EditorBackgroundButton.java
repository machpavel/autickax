package cz.mff.cuni.autickax;

import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

import cz.mff.cuni.autickax.myInputListener.MyInputListenerForBackground;

public class EditorBackgroundButton extends Button {
	EditorScreen screen;
	int i;

	public EditorBackgroundButton(String name, EditorScreen screen) {
		super(new TextureRegionDrawable(AutickaxEditor.getInstance().assets.getGraphics(name)));
		this.screen = screen;		
		this.addListener(new MyInputListenerForBackground(name, screen));
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
