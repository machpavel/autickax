package cz.cuni.mff.xcars.scene;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Vector;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.PixmapIO;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;

import cz.cuni.mff.xcars.Difficulty;
import cz.cuni.mff.xcars.Level;
import cz.cuni.mff.xcars.PlayedLevel;
import cz.cuni.mff.xcars.Scenario;
import cz.cuni.mff.xcars.Xcars;
import cz.cuni.mff.xcars.constants.Constants;
import cz.cuni.mff.xcars.debug.Debug;
import cz.cuni.mff.xcars.drawing.TimeStatusBar;
import cz.cuni.mff.xcars.entities.Car;
import cz.cuni.mff.xcars.entities.Finish;
import cz.cuni.mff.xcars.entities.GameObject;
import cz.cuni.mff.xcars.entities.Start;
import cz.cuni.mff.xcars.gamelogic.CheckPoint;
import cz.cuni.mff.xcars.gamelogic.GameStatistics;
import cz.cuni.mff.xcars.gamelogic.SubLevel;
import cz.cuni.mff.xcars.gamelogic.SubLevel1;
import cz.cuni.mff.xcars.gamelogic.SubLevel2;
import cz.cuni.mff.xcars.pathway.DistanceMap;
import cz.cuni.mff.xcars.pathway.Pathway;

public class GameScreen extends BaseScreen {
	// Textures
	private TextureRegion pathwayTexture;

	// Levels
	private SubLevel currentPhase;
	private Level level;
	private PlayedLevel playedLevel;
	private final int levelIndex;
	private final Difficulty levelDifficulty;
	private final Scenario scenario;

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

	public GameScreen(int levelIndex, Scenario levelsSet) {
		this(levelIndex, levelsSet, true);
	}

	public GameScreen(int levelIndex, Scenario levelsSet, boolean takeFocus) {
		super(takeFocus);

		this.scenario = levelsSet;
		this.level = scenario.levels.get(levelIndex);
		this.levelDifficulty = this.level.getDifficulty();
		this.levelIndex = levelIndex;

		Vector<PlayedLevel> playedLevels = Xcars.playedLevels.levels.containsKey(levelsSet.name) ? Xcars.playedLevels.levels
				.get(levelsSet.name) : new Vector<PlayedLevel>();

		this.playedLevel = playedLevels.get(levelIndex);

		this.timeStatusBar = new TimeStatusBar(this.level.getTimeLimit());

		// Add actors - note that start and finish should be added first
		this.stage.addActor(this.level.getFinish());
		this.stage.addActor(this.level.getStart());
		for (GameObject universalObject : this.level.getUniversalObjects()) {
			this.stage.addActor(universalObject);
		}
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
	 * Initializes stuffs with textures. For more details see
	 * initializeGameScreen function.
	 */
	public void initializeTextures() {
		level.setTextures();
		this.pathwayTexture = level.getPathway().getDistanceMap().generateTexture(this.levelDifficulty);
		// If tooltips are turned on, texture in dialog is generated
		this.currentPhase = new SubLevel1(this, level.getTimeLimit());
	}

	/**
	 * Initializes main game screen objects except for textures. Textures have
	 * to be attached from render thread or in postRunnable way because of
	 * OpenGL context. This is the reason initializeTextures function exist and
	 * has to be called separately.
	 */
	public void initializeGameScreen() {
		// Pathway
		this.pathway = level.getPathway();
		this.pathway.CreateDistances();

		// Game objects
		this.gameObjects = level.getGameObjects();
		this.car = level.getCar();
		this.start = level.getStart();
		this.finish = level.getFinish();
	}

	public void stopInitialization() {
		if (this.pathway != null)
			this.pathway.stopInitialization();
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

	public Scenario getScenario() {
		return this.scenario;
	}

	public void switchToPhase(SubLevel1 phase1) {
		this.currentPhase.dispose(true);
		phase1.reset();
		this.currentPhase = phase1;
	}

	public void switchToPhase2(LinkedList<CheckPoint> checkpoints, DistanceMap map, SubLevel1 lastPhase,
			GameStatistics stats) {
		this.currentPhase = new SubLevel2(this, checkpoints, map, lastPhase, stats);
		this.timeStatusBar.setPhase2();
	}

	public void unproject(Vector3 vector) {
		this.camera.unproject(vector);
	}

	@SuppressWarnings("unused")
	@Override
	public void render(float delta) {
		long time, lastTime;
		lastTime = time = System.currentTimeMillis();

		if (Debug.DEBUG && Debug.drawFPSDistribution) {
			Debug.clear();
		}

		clearScreenWithColor(); // This cryptic line clears the screen

		if (Debug.DEBUG && Debug.drawFPSDistribution) {
			time = System.currentTimeMillis();
			Debug.Log("Clearing map: " + Long.toString(time - lastTime));
			lastTime = time;
		}

		camera.update();
		batch.setProjectionMatrix(camera.combined);

		batch.begin();
		batch.disableBlending();
		this.level.getLevelBackground().draw(batch, stageWidth, stageHeight);
		batch.enableBlending();

		if (Debug.DEBUG && Debug.drawFPSDistribution) {
			time = System.currentTimeMillis();
			Debug.Log("Background: " + Long.toString(time - lastTime));
			lastTime = time;
		}

		batch.draw(this.pathwayTexture, 0, 0, stageWidth, stageHeight);
		batch.end();

		if (Debug.DEBUG && Debug.drawFPSDistribution) {
			time = System.currentTimeMillis();
			Debug.Log("Pathway: " + Long.toString(time - lastTime));
			lastTime = time;
		}

		this.currentPhase.update(delta);

		if (Debug.DEBUG && Debug.drawFPSDistribution) {
			time = System.currentTimeMillis();
			Debug.Log("Main + minigame update: " + Long.toString(time - lastTime));
			lastTime = time;
		}

		this.stage.act(delta);

		if (Debug.DEBUG && Debug.drawFPSDistribution) {
			time = System.currentTimeMillis();
			Debug.Log("Main stage update: " + Long.toString(time - lastTime));
			lastTime = time;
		}

		this.stage.draw();

		if (Debug.DEBUG && Debug.drawFPSDistribution) {
			time = System.currentTimeMillis();
			Debug.Log("Main stage draw: " + Long.toString(time - lastTime));
			lastTime = time;
		}

		// Touchable area for car
		if (Debug.DEBUG && Debug.drawMaxTouchableArea) {
			if (this.currentPhase != null && this.currentPhase instanceof SubLevel1) {
				SubLevel1 sl1 = (SubLevel1) this.currentPhase;
				sl1.DrawMaxTouchableArea();
			}
		}

		if (!this.currentPhase.getDialogStack().isEmpty()) {
			this.currentPhase.getDialogStack().peek().draw(batch);
		} else if (currentPhase.getMiniGame() != null) {
			this.currentPhase.getMiniGame().draw(batch);
		}

		if (Debug.DEBUG && Debug.drawFPSDistribution) {
			time = System.currentTimeMillis();
			Debug.Log("Dialogs draw: " + Long.toString(time - lastTime));
			lastTime = time;
		}

		renderDebug(batch, delta);
	}

	public ArrayList<GameObject> getGameObjects() {
		return gameObjects;
	}

	public void goToMainScreen() {
		this.goToMainScreen(this.scenario);
	}

	public void goToMainScreen(Scenario displayedScenario) {
		this.goToMainScreen(displayedScenario, this.levelIndex);
	}

	public void goToMainScreen(Scenario displayedScenario, int levelIndex) {
		Xcars.getInstance().assets.soundAndMusicManager.playSound(Constants.sounds.SOUND_MENU_CLOSE,
				Constants.sounds.SOUND_DEFAULT_VOLUME);
		Xcars.getInstance().assets.soundAndMusicManager.pauseRaceMusic();
		Xcars.getInstance().assets.soundAndMusicManager.playMenuMusic();
		Xcars.levelSelectScreen.dispose();
		Xcars.levelSelectScreen = new LevelSelectScreen(levelIndex, displayedScenario);
		Xcars.getInstance().setScreen(Xcars.levelSelectScreen);
		Xcars.levelSelectScreen.takeFocus();

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
		if (this.pathwayTexture != null)
			this.pathwayTexture.getTexture().dispose();
		if (this.pathway != null)
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
			textureFile = Gdx.files.local(Constants.misc.TEMPORARY_PATHWAY_TEXTURE_STORAGE_NAME + ".cim");
			// Gdx.app.log("TEXTURE",
			// "Texture of pathway is loaded from local memory.");
			// Debug.Log("Texture of pathway is loaded from local memory.");
		} else {
			textureFile = Gdx.files.internal(Constants.misc.TEMPORARY_PATHWAY_TEXTURE_STORAGE_NAME + ".cim");
			// Gdx.app.log("TEXTURE",
			// "Texture of pathway is loaded from internal memory.");
			// Debug.Log("Texture of pathway is loaded from internal memory.");
		}

		if (textureFile.exists()) {
			Pixmap pixmap = PixmapIO.readCIM(textureFile);
			this.pathwayTexture = new TextureRegion(new Texture(pixmap), Constants.WORLD_WIDTH, Constants.WORLD_HEIGHT);
			pixmap.dispose();
			// Gdx.app.log("TEXTURE",
			// "The pathway texture was loaded succesfully.");
			// Debug.Log("The pathway texture was loaded succesfully.");
		} else {
			// Gdx.app.log("TEXTURE", "Reading of saved");
			// Debug.Log("Reading of saved ");
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
