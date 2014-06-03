package cz.cuni.mff.autickax.scene;

import java.util.Vector;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import cz.cuni.mff.autickax.Autickax;
import cz.cuni.mff.autickax.constants.Constants;

public class LoadingScreen extends BaseScreen {

	private final TextureRegion background;
	private final TextureRegion car;
	private final TextureRegion fume;

	private final int carYPosition;
	private final int fumeYPositon;
	private final int fumeLaunchInterval = 100;

	private int carFumePosition = 0;
	private Vector<Integer> fumeXPositions = new Vector<Integer>();

	private int carXPosition = 0;

	public LoadingScreen() {
		super();

		this.carYPosition = 50;
		this.fumeYPositon = 70;

		Autickax.getInstance().assets.loadLoadingScreenGraphics();
		while (!Autickax.getInstance().assets.update())
			; // wait until it is loaded

		this.background = new TextureRegion(
				Autickax.getInstance().assets
						.getLoadingScreenGraphics(Constants.menu.LOADING_SCREEN_BACKGROUND));
		this.car = new TextureRegion(
				Autickax.getInstance().assets
						.getLoadingScreenGraphics(Constants.menu.LOADING_SCREEN_CAR));
		this.fume = new TextureRegion(
				Autickax.getInstance().assets
						.getLoadingScreenGraphics(Constants.menu.LOADING_SCREEN_FUME));

		Autickax.getInstance().assets.load();
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0f, 1f, 0f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		camera.update();
		batch.setProjectionMatrix(camera.combined);

		if (Autickax.getInstance().assets.update()) {
			Autickax.getInstance().assets.disposeLoadingScreenGraphics();

			Autickax.mainMenuScreen = new MainMenuScreen(); // we know that it
															// is null, no need
															// for check
			Autickax.getInstance().setScreen(Autickax.mainMenuScreen);
		} else {
			this.batch.begin();

			this.batch.draw(this.background, 0, 0, this.stageWidth, this.stageHeight);
			this.batch.draw(this.car, this.carXPosition, this.carYPosition,
					this.car.getRegionWidth(), this.car.getRegionHeight());

			for (int x : this.fumeXPositions) {
				this.batch.draw(this.fume, x, this.fumeYPositon, this.fume.getRegionWidth(),
						this.fume.getRegionHeight());
			}

			this.batch.end();

			int newCarPosition = (int) (Autickax.getInstance().assets.getProgress() * Constants.WORLD_WIDTH);
			this.carFumePosition += newCarPosition - this.carXPosition;
			this.carXPosition = newCarPosition;

			if (this.carFumePosition > this.fumeLaunchInterval) {
				this.fumeXPositions.add(this.carXPosition - this.fumeLaunchInterval / 2);
				this.carFumePosition = 0;
			}
		}

		renderDebug(batch);
	}

	@Override
	protected void onBackKeyPressed() {
		// Rewrites the base method with doing nothing
	}

}
