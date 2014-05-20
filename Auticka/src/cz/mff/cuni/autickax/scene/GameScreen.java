package cz.mff.cuni.autickax.scene;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Vector;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.PixmapIO;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import cz.mff.cuni.autickax.Autickax;
import cz.mff.cuni.autickax.Debug;
import cz.mff.cuni.autickax.Difficulty;
import cz.mff.cuni.autickax.Level;
import cz.mff.cuni.autickax.PlayedLevel;
import cz.mff.cuni.autickax.constants.Constants;
import cz.mff.cuni.autickax.drawing.TimeStatusBar;
import cz.mff.cuni.autickax.entities.Car;
import cz.mff.cuni.autickax.entities.Finish;
import cz.mff.cuni.autickax.entities.GameObject;
import cz.mff.cuni.autickax.entities.Start;
import cz.mff.cuni.autickax.gamelogic.CheckPoint;
import cz.mff.cuni.autickax.gamelogic.GameStatistics;
import cz.mff.cuni.autickax.gamelogic.SubLevel;
import cz.mff.cuni.autickax.gamelogic.SubLevel1;
import cz.mff.cuni.autickax.gamelogic.SubLevel2;
import cz.mff.cuni.autickax.pathway.DistanceMap;
import cz.mff.cuni.autickax.pathway.Pathway;

public class GameScreen extends BaseScreen {
	public static final float EPSILON_F = 0.01f;

	// Textures
	private TextureRegion pathwayTexture;

	// Rendering
	protected OrthographicCamera camera;

	// Levels
	private SubLevel currentPhase;
	private Level level;
	private PlayedLevel playedLevel;
	private final int levelIndex;
	private final Difficulty levelDifficulty;

	// Entities
	private ArrayList<GameObject> gameObjects;
	protected Car car;
	protected Start start;
	protected Finish finish;

	// Pathway
	protected Pathway pathway;

	private final TimeStatusBar timeStatusBar;

	public Pathway getPathWay() {
		return pathway;
	}

	public GameScreen(int levelIndex, Difficulty difficulty) {
		this(levelIndex, difficulty, true);
	}

	public GameScreen(int levelIndex, Difficulty difficulty, boolean takeFocus) {
		super(takeFocus);

		this.levelDifficulty = difficulty;
		this.levelIndex = levelIndex;
		this.loadLevels(levelIndex, this.levelDifficulty.getAvailableLevels(),
				this.levelDifficulty.getPlayedLevels());

		this.timeStatusBar = new TimeStatusBar(this.level.getTimeLimit());

		// Add actors - note that start and finish should be added first
		this.stage.addActor(this.level.getFinish());
		this.stage.addActor(this.level.getStart());
		for (GameObject gameObject : this.level.getGameObjects()) {
			this.stage.addActor(gameObject);
		}
		this.stage.addActor(this.level.getCar());
		this.stage.addActor(this.timeStatusBar);
	}

	public TimeStatusBar getTimeStatusBar() {
		return timeStatusBar;
	}

	public float getDistanceMapProgress() {
		return this.level.getDistanceMapProgress();
	}

	/**
	 * Initializes textures. For more details see initializeGameScreen function.
	 */
	public void initializeTextures() {
		level.setTextures();
		this.pathwayTexture = level.getPathway().getDistanceMap()
				.generateTexture(this.levelDifficulty);
	}

	/**
	 * Initializes main game screen objects except for textures. Textures have
	 * to be attached from render thread or in postRunnable way because of
	 * OpenGL context. This is the reason initializeTextures function exist and
	 * has to be called separately.
	 */
	public void initializeGameScreen() {
		// Camera
		camera = new OrthographicCamera();
		camera.setToOrtho(false, stageWidth, stageHeight);

		// Pathway
		this.pathway = level.getPathway();
		this.pathway.CreateDistances();

		// Game objects
		this.gameObjects = level.getGameObjects();
		this.car = level.getCar();

		this.start = level.getStart();
		Vector2 startDirection = new Vector2(
				this.pathway.GetPosition(Constants.misc.START_POSITION_IN_CURVE + EPSILON_F)).sub(
				this.start.getPosition()).nor();
		this.start.setShift(startDirection.scl(Constants.misc.CAR_DISTANCE_FROM_START));
		this.start.setRotation((startDirection.angle() + 270) % 360);

		this.finish = level.getFinish();
		Vector2 finishDirection = new Vector2(
				this.pathway.GetPosition(Constants.misc.FINISH_POSITION_IN_CURVE - EPSILON_F)).sub(
				this.finish.getPosition()).nor();
		this.finish.setShift(finishDirection.scl(Constants.gameObjects.FINISH_BOUNDING_RADIUS));
		this.finish.setRotation((finishDirection.angle() + 90) % 360);

		// Sublevel
		this.currentPhase = new SubLevel1(this, level.getTimeLimit());

		// Start Music!
		if (Autickax.settings.playMusic) {
			Autickax.getInstance().assets.soundAndMusicManager.playRaceMusic();
		}
	}

	/**
	 * Load level information and playedLevel information. If the playedLevel
	 * information is not preset, adds a blank one.
	 * 
	 * @param levelIndex
	 */
	private void loadLevels(int levelIndex, Vector<Level> levels, Vector<PlayedLevel> playedLevels) {
		this.level = levels.get(levelIndex);
		this.playedLevel = playedLevels.get(levelIndex);
	}

	public Start getStart() {
		return this.start;
	}

	public Finish getFinish() {
		return this.finish;
	}

	public Car getCar() {
		return this.car;
	}

	public void switchToPhase(SubLevel1 phase1) {
		this.currentPhase.dispose(true);
		phase1.reset();
		this.currentPhase = phase1;
	}

	public void switchToPhase2(LinkedList<CheckPoint> checkpoints, DistanceMap map,
			SubLevel1 lastPhase, GameStatistics stats) {
		this.currentPhase = new SubLevel2(this, checkpoints, map, lastPhase, stats);
		this.timeStatusBar.setPhase2();
	}

	public void unproject(Vector3 vector) {
		this.camera.unproject(vector);
	}

	protected void update(float delta) {
		this.currentPhase.update(delta);
	}

	@Override
	public void render(float delta) {
		clearScreenWithColor(); // This cryptic line clears the screen

		camera.update();
		batch.setProjectionMatrix(camera.combined); // see
													// https://github.com/libgdx/libgdx/wiki/Orthographic-camera

		batch.begin();
		batch.disableBlending();
		this.level.getLevelBackground().draw(batch, stageWidth, stageHeight);
		batch.enableBlending();

		batch.draw(this.pathwayTexture, 0, 0, stageWidth, stageHeight);
		batch.end();

		this.currentPhase.update(delta);

		this.stage.act(delta);
		this.stage.draw();

		if (!this.currentPhase.getDialogStack().isEmpty()) {
			this.currentPhase.getDialogStack().peek().draw(batch);
		} else if (currentPhase.getMiniGame() != null) {
			this.currentPhase.getMiniGame().draw(batch);
		}

		renderDebug(batch);
	}

	public ArrayList<GameObject> getGameObjects() {
		return gameObjects;
	}

	public void goToMainScreen() {
		Autickax.getInstance().assets.soundAndMusicManager.playSound(
				Constants.sounds.SOUND_MENU_CLOSE, Constants.sounds.SOUND_DEFAULT_VOLUME);
		Autickax.getInstance().assets.soundAndMusicManager.stopRaceMusic();
		Autickax.getInstance().assets.soundAndMusicManager.playMenuMusic();
		Autickax.levelSelectScreen.dispose();
		Autickax.levelSelectScreen = new LevelSelectScreen(this.levelDifficulty,
				(levelIndex / Constants.menu.DISPLAYED_LEVELS_MAX_COUNT)
						* Constants.menu.DISPLAYED_LEVELS_MAX_COUNT);
		Autickax.getInstance().setScreen(Autickax.levelSelectScreen);
		Gdx.input.setInputProcessor(Autickax.levelSelectScreen.getStage());

	}

	@Override
	protected void onBackKeyPressed() {
		if (this.currentPhase.isPaused()) {
			this.currentPhase.resume();
		} else {
			this.currentPhase.pause();
		}
	}

	@Override
	public void dispose() {
		super.dispose();
		this.pathwayTexture.getTexture().dispose();
		this.pathway.deleteDistanceMap();
	}

	public Difficulty getDifficulty() {
		return this.levelDifficulty;
	}

	public PlayedLevel getPlayedLevel() {
		return this.playedLevel;
	}

	public int getLevelIndex() {
		return this.levelIndex;
	}

	@Override
	public void pause() {
		this.currentPhase.pause();
		this.pathwayTexture.getTexture().dispose();
	}

	@Override
	public void resume() {
		FileHandle textureFile = null;
		if (Gdx.files.isLocalStorageAvailable()) {
			textureFile = Gdx.files.local(Constants.misc.TEMPORARY_PATHWAY_TEXTURE_STORAGE_NAME
					+ ".cim");
			// System.out.println("Texture of pathway is loaded from local memory.");
		} else {
			textureFile = Gdx.files.internal(Constants.misc.TEMPORARY_PATHWAY_TEXTURE_STORAGE_NAME
					+ ".cim");
			// System.out.println("Texture of pathway is loaded from internal memory.");
		}

		if (textureFile.exists()) {
			Pixmap pixmap = PixmapIO.readCIM(textureFile);
			this.pathwayTexture = new TextureRegion(new Texture(pixmap), Constants.WORLD_WIDTH,
					Constants.WORLD_HEIGHT);
			pixmap.dispose();
			// System.out.println("The pathway texture was loaded succesfully.");
		} else {
			// System.out.println("Loading of pathway texture failed..");
		}
	}

	@Override
	public void takeFocus() {
		if (this.currentPhase != null) {
			if (!this.currentPhase.isDialogStackEmpty()) {
				this.currentPhase.takeDialogFocus();
			} else if (this.currentPhase.isMinigameRunning()) {
				this.currentPhase.takeMinigameFocus();
			} else {
				this.currentPhase.takeFocus();
			}
		} else {
			super.takeFocus();
		}
	}
}
