package cz.cuni.mff.xcars;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;

import cz.cuni.mff.xcars.Xcars;


public class LoadingScreenEditor extends BaseScreenEditor {

	public LoadingScreenEditor() {
		super();		
		new Xcars();
		Xcars.getInstance().assets.load();
		game.assets.load();
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0f, 0f, 0f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		if (game.assets.update() && Xcars.getInstance().assets.update()) { // Keep calling this, until returns true
			game.setScreen(new EditorScreen());						
			return;
		}
	}

}
