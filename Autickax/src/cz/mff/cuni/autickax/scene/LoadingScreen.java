package cz.mff.cuni.autickax.scene;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;

import cz.mff.cuni.autickax.MyGdxGame;

/**
 * In this screen we load all our assets and then switch to TitleScreen
 * 
 * @author Ondrej Paska
 */
public class LoadingScreen extends BaseScreen {

	public LoadingScreen() {
		super();

		game.assets.load();
	}

	@Override
	public void render(float delta) {
		//TODO: loading animation
		Gdx.gl.glClearColor(0f, 1f, 0f, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		if (game.assets.update()) { // Keep calling this, until returns true			
			MyGdxGame.getInstance().setScreen(new TitleScreen());
			return;
		}
		// trace("loading progress:"+Assets.getProgress()); //TODO visualize
		// progress
	}

}
