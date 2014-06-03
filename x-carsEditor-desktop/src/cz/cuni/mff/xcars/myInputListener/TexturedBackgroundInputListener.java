package cz.cuni.mff.xcars.myInputListener;

import com.badlogic.gdx.scenes.scene2d.InputEvent;

import cz.cuni.mff.xcars.AutickaxEditor;
import cz.cuni.mff.xcars.EditorScreen;
import cz.cuni.mff.xcars.drawing.LevelTextureBackground;

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
