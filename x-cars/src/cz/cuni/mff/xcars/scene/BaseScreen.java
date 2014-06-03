package cz.cuni.mff.xcars.scene;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ScalingViewport;

import cz.cuni.mff.xcars.Debug;
import cz.cuni.mff.xcars.constants.Constants;

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
		this.stageWidth = Constants.WORLD_WIDTH;
		this.stageHeight = Constants.WORLD_HEIGHT;
		this.batch = new SpriteBatch();

		// Camera
		this.camera = new OrthographicCamera();
		this.camera.setToOrtho(false, this.stageWidth, this.stageHeight);

		this.stage = new Stage(new ScalingViewport(Scaling.stretch, this.stageWidth,
				this.stageHeight, this.camera), batch);

		this.stage.addListener(new ScreenInputListener(this));

		this.inputProcessor = createInputProcessor();

		if (takeFocus)
			takeFocus();
	}

	public SpriteBatch getBatch() {
		return this.batch;
	}

	protected InputProcessor createInputProcessor() {
		return this.stage;
	}

	public void takeFocus() {
		Gdx.input.setInputProcessor(inputProcessor);
		Gdx.input.setCatchBackKey(true);
	}

	public Stage getStage() {
		return this.stage;
	}

	protected void clearScreenWithColor() {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
	}

	@Override
	public void render(float delta) {
		clearScreenWithColor();
		camera.update();
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
