package cz.mff.cuni.autickax;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;

public class BaseScreenEditor implements Screen {

	protected AutickaxEditor game;
	protected float stageWidth;
	protected float stageHeight;
	protected final Stage stage;
	
	/*public float getStageWidth() {
		return this.stageWidth;
	}
	
	public float getStageHeight() {
		return this.stageHeight;
	}*/

	public BaseScreenEditor() {
		game = AutickaxEditor.getInstance();
		stageWidth = Gdx.graphics.getWidth();
		stageHeight = Gdx.graphics.getHeight();

		stage = new Stage(); // https://github.com/libgdx/libgdx/wiki/Scene2d
		Gdx.input.setInputProcessor(stage);
	}

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
