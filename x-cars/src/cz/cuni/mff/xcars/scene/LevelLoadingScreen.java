package cz.cuni.mff.xcars.scene;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import cz.cuni.mff.xcars.Xcars;
import cz.cuni.mff.xcars.Difficulty;
import cz.cuni.mff.xcars.constants.Constants;
import cz.cuni.mff.xcars.debug.Debug;

public class LevelLoadingScreen extends BaseScreen {

	private final TextureRegion background;
	private final TextureRegion grayLight;
	private final TextureRegion greenLight;
	private final TextureRegion redLight;

	private static final int lightsCount = 6;
	private int shiningLightsCount = 0;
	private static final int lightsXmargin = 100;
	private static final int lightsYmargin = 60;
	private static final int lightsYposition = 220;
	private final int lightsXposition;

	// Helping variables to stop a loading
	private final int levelIndex;
	private final Difficulty levelDifficulty;
	private boolean isInitializationStopped = false;

	public LevelLoadingScreen(final int levelIndex,
			final Difficulty levelDifficulty) {
		super();
		this.levelIndex = levelIndex;
		this.levelDifficulty = levelDifficulty;

		if (Xcars.adsHandler != null) {
			Xcars.adsHandler.showBanner(true);
		}

		// setup a game screen
		if (Xcars.gameScreen != null) {
			Xcars.gameScreen.dispose();
			Xcars.gameScreen = null;
		}

		Xcars.gameScreen = new GameScreen(levelIndex, levelDifficulty, false);

		Thread distanceMapLoader = new Thread(new Runnable() {
			@Override
			public void run() {
				Xcars.gameScreen.initializeGameScreen();
				Gdx.app.postRunnable(new Runnable() {
					@Override
					public void run() {
						if (!LevelLoadingScreen.this.isInitializationStopped) {
							Xcars.gameScreen.initializeTextures();
							Xcars.getInstance().setScreen(Xcars.gameScreen);
							Xcars.gameScreen.takeFocus();

							if (Xcars.adsHandler != null) {
								Xcars.adsHandler.showBanner(false);
							}
						}
					}
				});
			}
		});

		distanceMapLoader.start();

		this.background = Xcars.getInstance().assets
				.getGraphics(Constants.menu.LOADING_LEVEL_MENU_BACKGROUND);
		this.greenLight = Xcars.getInstance().assets
				.getGraphics(Constants.menu.LOADING_LEVEL_MENU_GREEN);
		this.redLight = Xcars.getInstance().assets
				.getGraphics(Constants.menu.LOADING_LEVEL_MENU_RED);
		this.grayLight = Xcars.getInstance().assets
				.getGraphics(Constants.menu.LOADING_LEVEL_MENU_GRAY);

		this.lightsXposition = (int) ((this.getStage().getWidth() / 2) - ((LevelLoadingScreen.lightsCount / 2) * LevelLoadingScreen.lightsXmargin));
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0f, 1f, 0f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		camera.update();
		batch.setProjectionMatrix(camera.combined);

		this.batch.begin();

		this.batch.draw(this.background, 0, 0, this.stageWidth,
				this.stageHeight);

		for (int i = 0; i < LevelLoadingScreen.lightsCount; ++i) {
			TextureRegion drawnLight;
			if (this.shiningLightsCount >= LevelLoadingScreen.lightsCount - 1) {
				drawnLight = this.greenLight;
			} else if (i > this.shiningLightsCount) {
				drawnLight = this.grayLight;
			} else {
				drawnLight = this.redLight;
			}
			this.batch.draw(drawnLight, this.lightsXposition + i
					* LevelLoadingScreen.lightsXmargin,
					LevelLoadingScreen.lightsYposition
							+ LevelLoadingScreen.lightsYmargin,
					drawnLight.getRegionWidth(), drawnLight.getRegionHeight());

			this.batch.draw(drawnLight, this.lightsXposition + i
					* LevelLoadingScreen.lightsXmargin,
					LevelLoadingScreen.lightsYposition
							- LevelLoadingScreen.lightsYmargin,
					drawnLight.getRegionWidth(), drawnLight.getRegionHeight());
		}

		this.batch.end();

		this.shiningLightsCount = (int) (Xcars.gameScreen
				.getDistanceMapProgress() * LevelLoadingScreen.lightsCount);
		Debug.SetValue(Xcars.gameScreen.getDistanceMapProgress());
		renderDebug(batch, delta);
	}

	@Override
	protected void onBackKeyPressed() {
		this.isInitializationStopped = true;
		Xcars.gameScreen.stopInitialization();

		Debug.Log("Exit loading screen");

		Xcars.getInstance().assets.soundAndMusicManager.playSound(
				Constants.sounds.SOUND_MENU_CLOSE,
				Constants.sounds.SOUND_DEFAULT_VOLUME);
		Xcars.getInstance().assets.soundAndMusicManager.stopRaceMusic();
		Xcars.getInstance().assets.soundAndMusicManager.playMenuMusic();
		Xcars.levelSelectScreen.dispose();
		Xcars.levelSelectScreen = new LevelSelectScreen(this.levelDifficulty,
				levelIndex);
		Xcars.getInstance().setScreen(Xcars.levelSelectScreen);
		Xcars.levelSelectScreen.takeFocus();
	}
}
