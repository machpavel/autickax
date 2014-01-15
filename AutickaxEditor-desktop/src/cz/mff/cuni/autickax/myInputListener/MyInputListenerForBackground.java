package cz.mff.cuni.autickax.myInputListener;

import com.badlogic.gdx.scenes.scene2d.InputEvent;

import cz.mff.cuni.autickax.EditorScreen;

public class MyInputListenerForBackground extends MyInputListener {
	int i;
	public MyInputListenerForBackground(int i, EditorScreen screen) {
		super(screen);
		this.i = i;
	}
	
	
	@Override
	public void touchUp(InputEvent event, float x, float y,
			int pointer, int button) {
		this.screen.GetBackground().SetType(i);
	}

}
