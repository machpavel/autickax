package cz.mff.cuni.autickax.myInputListener;

import com.badlogic.gdx.scenes.scene2d.InputEvent;

import cz.mff.cuni.autickax.AutickaxEditor;
import cz.mff.cuni.autickax.EditorScreen;
import cz.mff.cuni.autickax.drawing.LevelTextureBackground;

public class MyInputListenerForBackground extends MyInputListener {
	String name;
	public MyInputListenerForBackground(String name, EditorScreen screen) {
		super(screen);
		this.name = name;
	}
	
	
	@Override
	public void touchUp(InputEvent event, float x, float y,
			int pointer, int button) {
		this.screen.SetBackground(new LevelTextureBackground(name, AutickaxEditor.getInstance().assets.getGraphics(name)));
	}

}
