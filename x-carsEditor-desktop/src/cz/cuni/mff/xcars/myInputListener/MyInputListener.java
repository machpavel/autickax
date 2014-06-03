package cz.cuni.mff.xcars.myInputListener;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;

import cz.cuni.mff.xcars.EditorScreen;

public class MyInputListener extends InputListener {
	protected EditorScreen screen;

	public MyInputListener(EditorScreen screen) {
		this.screen = screen;
	}

	@Override
	public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
		this.screen.SetAnyButtonTouched(true);
		return true;
	}

}
