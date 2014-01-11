package cz.mff.cuni.autickax.myInputListener;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;

import cz.mff.cuni.autickax.EditorScreen;

public class MyInputListener extends InputListener {
	protected EditorScreen screen;
	
	public MyInputListener(EditorScreen screen){
		this.screen = screen;
	}
	
	public boolean touchDown(InputEvent event, float x, float y,
			int pointer, int button) {
		this.screen.SetAnyButtonTouched(true);
		return true;
	}

}
