package cz.mff.cuni.autickax.scene;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;

import cz.mff.cuni.autickax.Autickax;
import cz.mff.cuni.autickax.drawing.Font;


public class LoadingScreen extends BaseScreen {

	public LoadingScreen() {
		super();
		
		getGame().assets.load();
	}

	@Override
	public void render(float delta) {		
		//TODO: loading animation
		Gdx.gl.glClearColor(0f, 1f, 0f, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		if (getGame().assets.update()) {
			Autickax.font = new Font(getGame().assets.getFont());
			Autickax.mainMenuScreen = new MainMenuScreen(); // we know that it is null, no need for check
			getGame().setScreen(Autickax.mainMenuScreen);						
			return;
		}
		//trace("loading progress:" + game.assets.getProgress()); //TODO visualize

	}

	@Override
	protected void onBackKeyPressed() {
		// TODO Auto-generated method stub
		
	}

}
