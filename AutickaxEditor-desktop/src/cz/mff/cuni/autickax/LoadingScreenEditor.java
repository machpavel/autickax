package cz.mff.cuni.autickax;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;

import cz.mff.cuni.autickax.scene.BaseScreen;


public class LoadingScreenEditor extends BaseScreenEditor {

	public LoadingScreenEditor() {
		super();		
		new Autickax().getInstance().assets.load();
		game.assets.load();
	}

	@Override
	public void render(float delta) {
		//TODO: loading animation
		Gdx.gl.glClearColor(0f, 1f, 0f, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		if (game.assets.update() && Autickax.getInstance().assets.update()) { // Keep calling this, until returns true
			game.setScreen(new EditorScreen());						
			return;
		}
		// trace("loading progress:"+Assets.getProgress()); //TODO visualize
		// progress
	}

}
