package cz.cuni.mff.autickax.myInputListener;

import com.badlogic.gdx.scenes.scene2d.InputEvent;

import cz.cuni.mff.autickax.AutickaxEditor;
import cz.cuni.mff.autickax.EditorScreen;
import cz.cuni.mff.autickax.drawing.LevelTextureBackground;

public class TexturedBackgroundInputListener extends MyInputListener {
	String name;
	public TexturedBackgroundInputListener(String name, EditorScreen screen) {
		super(screen);
		this.name = name;
	}
	
	
	@Override
	public void touchUp(InputEvent event, float x, float y,
			int pointer, int button) {
		this.screen.SetBackground(new LevelTextureBackground(name, AutickaxEditor.getInstance().assets.getGraphics(name)));
	}

}
