package cz.cuni.mff.xcars;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;

public class BaseScreenEditor implements Screen {

	protected XcarsEditor game;
	protected float stageWidth;
	protected float stageHeight;
	protected Stage stage;
	
	/*public float getStageWidth() {
		return this.stageWidth;
	}
	
	public float getStageHeight() {
		return this.stageHeight;
	}*/

	public BaseScreenEditor() {
		game = XcarsEditor.getInstance();
		stageWidth = Gdx.graphics.getWidth();
		stageHeight = Gdx.graphics.getHeight();
	}

	public void printHepl(){};
	
	@Override
	public void render(float delta) {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		stage.act(delta); // don't forget to advance the stage ( input + actions
							// )
		stage.draw(); // and also display it :)
	}

	@Override
	public void resize(int width, int height) {
		// stage.setViewport(width, height, true);
	}

	@Override
	public void show() {

	}

	@Override
	public void hide() {

	}

	@Override
	public void pause() {

	}

	@Override
	public void resume() {

	}

	@Override
	public void dispose() {
		stage.dispose();
	}

}
