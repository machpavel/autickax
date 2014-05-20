package cz.mff.cuni.autickax.scene;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import cz.mff.cuni.autickax.Autickax;
import cz.mff.cuni.autickax.Debug;
import cz.mff.cuni.autickax.Difficulty;
import cz.mff.cuni.autickax.constants.Constants;
import cz.mff.cuni.autickax.input.Input;

public class LevelLoadingScreen extends BaseScreen {

	private final TextureRegion background;
	private final TextureRegion grayLight;
	private final TextureRegion greenLight;
	private final TextureRegion redLight;

	private static final int lightsCount = 6;
	private int shiningLightsCount = 0;
	private static final int lightsXmargin = (int) (100 * Input.xStretchFactorInv);
	private static final int lightsYmargin = (int) (60 * Input.xStretchFactorInv);
	private static final int lightsYposition = (int) (220 * Input.yStretchFactorInv);
	private final int lightsXposition;

	public LevelLoadingScreen(final int levelIndex, final Difficulty levelDifficulty) {
		super();

		// setup a game screen
		if (Autickax.gameScreen != null) {
			Autickax.gameScreen.dispose();
			Autickax.gameScreen = null;
		}

		Autickax.gameScreen = new GameScreen(levelIndex, levelDifficulty, false);

		Thread distanceMapLoader = new Thread(new Runnable() {
			@Override
			public void run() {
				Autickax.gameScreen.initializeGameScreen();
				Gdx.app.postRunnable(new Runnable() {
					@Override
					public void run() {
						Autickax.gameScreen.initializeTextures();
						Autickax.getInstance().setScreen(Autickax.gameScreen);
						Autickax.gameScreen.takeFocus();
					}
				});
			}
		});

		distanceMapLoader.start();

		this.background = Autickax.getInstance().assets
				.getGraphics(Constants.menu.LOADING_LEVEL_MENU_BACKGROUND);
		this.greenLight = Autickax.getInstance().assets
				.getGraphics(Constants.menu.LOADING_LEVEL_MENU_GREEN);
		this.redLight = Autickax.getInstance().assets
				.getGraphics(Constants.menu.LOADING_LEVEL_MENU_RED);
		this.grayLight = Autickax.getInstance().assets
				.getGraphics(Constants.menu.LOADING_LEVEL_MENU_GRAY);

		this.lightsXposition = (int) ((this.getStage().getWidth() / 2) - ((LevelLoadingScreen.lightsCount / 2) * LevelLoadingScreen.lightsXmargin));
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0f, 1f, 0f, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

		this.batch.begin();

		this.batch.draw(this.background, 0, 0, this.stageWidth, this.stageHeight);

		for (int i = 0; i < LevelLoadingScreen.lightsCount; ++i) {
			TextureRegion drawnLight;
			if (this.shiningLightsCount >= LevelLoadingScreen.lightsCount - 1) {
				drawnLight = this.greenLight;
			} else if (i > this.shiningLightsCount) {
				drawnLight = this.grayLight;
			} else {
				drawnLight = this.redLight;
			}
			this.batch.draw(drawnLight,
					this.lightsXposition + i * LevelLoadingScreen.lightsXmargin,
					LevelLoadingScreen.lightsYposition + LevelLoadingScreen.lightsYmargin,
					drawnLight.getRegionWidth() * Input.xStretchFactorInv,
					drawnLight.getRegionHeight() * Input.yStretchFactorInv);

			this.batch.draw(drawnLight,
					this.lightsXposition + i * LevelLoadingScreen.lightsXmargin,
					LevelLoadingScreen.lightsYposition - LevelLoadingScreen.lightsYmargin,
					drawnLight.getRegionWidth() * Input.xStretchFactorInv,
					drawnLight.getRegionHeight() * Input.yStretchFactorInv);
		}

		this.batch.end();

		this.shiningLightsCount = (int) (Autickax.gameScreen.getDistanceMapProgress() * LevelLoadingScreen.lightsCount);
		Debug.SetValue(Autickax.gameScreen.getDistanceMapProgress());
		renderDebug(batch);
	}

	@Override
	protected void onBackKeyPressed() {
		// Rewrites the base method with doing nothing
	}
}
