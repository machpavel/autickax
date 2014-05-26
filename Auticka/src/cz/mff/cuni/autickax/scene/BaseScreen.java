package cz.mff.cuni.autickax.scene;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import cz.mff.cuni.autickax.Debug;

public abstract class BaseScreen implements Screen {
	protected float stageWidth;
	protected float stageHeight;
	protected final Stage stage;
	protected SpriteBatch batch;

	protected OrthographicCamera camera;
	protected InputProcessor inputProcessor;

	public BaseScreen() {
		this(true);
	}

	public BaseScreen(boolean takeFocus) {
		stageWidth = Gdx.graphics.getWidth();
		stageHeight = Gdx.graphics.getHeight();
		batch = new SpriteBatch();

		// Camera
		camera = new OrthographicCamera();
		camera.setToOrtho(false, stageWidth, stageHeight);

		stage = new Stage(new ScreenViewport(camera), batch);

		this.stage.addListener(new ScreenInputListener(this));

		this.inputProcessor = createInputProcessor();

		if (takeFocus)
			takeFocus();
	}

	protected InputProcessor createInputProcessor() {
		return this.stage;
	}

	public void takeFocus() {
		Gdx.input.setInputProcessor(inputProcessor);
		Gdx.input.setCatchBackKey(true);
	}

	protected void clearScreenWithColor() {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
	}

	public Stage getStage() {
		return this.stage;
	}

	@Override
	public void render(float delta) {
		this.clearScreenWithColor();

		camera.update();
		// see https://github.com/libgdx/libgdx/wiki/Orthographic-camera
		batch.setProjectionMatrix(camera.combined);

		stage.act(delta);
		stage.draw();

		renderDebug(batch);
	}

	protected void renderDebug(SpriteBatch batch) {
		batch.begin();
		Debug.draw(batch);
		batch.end();
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

	protected abstract void onBackKeyPressed();
}
