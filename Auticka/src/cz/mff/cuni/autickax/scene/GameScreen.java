package cz.mff.cuni.autickax.scene;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Vector;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import cz.mff.cuni.autickax.Autickax;
import cz.mff.cuni.autickax.Constants;
import cz.mff.cuni.autickax.Difficulty;
import cz.mff.cuni.autickax.LevelLoading;
import cz.mff.cuni.autickax.PlayedLevel;
import cz.mff.cuni.autickax.drawing.LevelBackground;
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
	private LevelBackground levelBackground;
	private TextureRegion pathwayTexture;


	// Rendering
	protected OrthographicCamera camera;	
	
	// Levels
	private SubLevel currentPhase;
	private LevelLoading level;
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
		
	public Pathway getPathWay()
	{
		return pathway;
	}
	
	
	public GameScreen(int levelIndex, Difficulty difficulty) {
		super();
		
		this.levelDifficulty = difficulty;
		this.levelIndex = levelIndex;
		this.loadLevels(levelIndex, this.levelDifficulty.getAvailableLevels(), this.levelDifficulty.getPlayedLevels());
	}
	
	public void initializeDistanceMap() {
		level.calculateDistanceMap();
	}
	
	public void initializeGameScreen() {
		camera = new OrthographicCamera();
		camera.setToOrtho(false, stageWidth, stageHeight);
		
		
		level.calculateDistanceMap();
		level.setGameScreen(this);
								
		this.pathwayTexture = level.getPathway().getDistanceMap().generateTexture(this.levelDifficulty);
		
		this.pathway = level.getPathway();
		this.gameObjects = level.getGameObjects();
		
		this.levelBackground = new LevelBackground();
		this.levelBackground.SetType(level.getBackgroundType());
		this.car = level.getCar();		
		
		
		this.start = level.getStart();
		Vector2 startDirection = new Vector2(this.pathway.GetPosition(Constants.START_POSITION_IN_CURVE + EPSILON_F)).sub(this.start.getPosition()).nor();
		this.start.setShift(startDirection.scl(Constants.CAR_DISTANCE_FROM_START));
		this.start.setRotation((startDirection.angle() + 270) % 360);
		
		this.finish = level.getFinish();
		Vector2 finishDirection = new Vector2(this.pathway.GetPosition(Constants.FINISH_POSITION_IN_CURVE - EPSILON_F)).sub(this.finish.getPosition()).nor();
		this.finish.setShift(finishDirection.scl(Constants.CAR_DISTANCE_FROM_START));
		this.finish.setRotation((finishDirection.angle() + 90) % 360);
		
		this.currentPhase = new SubLevel1(this, level.getTimeLimit());
		
		// Start Music!
		if (Autickax.settings.playMusic) {
			getGame().assets.soundAndMusicManager.playRaceMusic();
		}
	}

	/**
	 * Load level information and playedLevel information.
	 * If the playedLevel information is not preset, adds a blank one.
	 * @param levelIndex
	 */
	private void loadLevels(int levelIndex, Vector<LevelLoading> levels, Vector<PlayedLevel> playedLevels) {
		this.level = levels.get(levelIndex);		
		this.playedLevel = playedLevels.get(levelIndex);
	}
	
	public Start getStart(){
		return this.start;
	}
	
	public Finish getFinish(){
		return this.finish;
	}
	
	public Car getCar()
	{
		return this.car;
	}
	
	public void switchToPhase(SubLevel1 phase1) {		
		phase1.reset();
		this.currentPhase = phase1;
	}
	public void switchToPhase2(LinkedList<CheckPoint> checkpoints, DistanceMap map, SubLevel1 lastPhase, GameStatistics stats) {
		this.currentPhase = new SubLevel2(this, checkpoints, map, lastPhase, stats);
	}
	

	
	public void reset() {
		// TODO: reseting of all game objects
	}
	
	public void unproject(Vector3 vector) {
		this.camera.unproject(vector);
	}

	protected void update(float delta) {
		this.currentPhase.update(delta);
	}

	@Override
	public void render(float delta) {		
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT); // This cryptic line clears the screen

		camera.update();
		batch.setProjectionMatrix(camera.combined);  // see https://github.com/libgdx/libgdx/wiki/Orthographic-camera
		
		batch.begin();		
		batch.disableBlending(); //performance boost		
		// background
		this.levelBackground.draw(batch, stageWidth, stageHeight);
		batch.enableBlending(); //don't forget to enabled this for alpha channel
		batch.draw(this.pathwayTexture, 0, 0, stageWidth, stageHeight);
		batch.end();
				
		
		this.currentPhase.update(delta);
		this.currentPhase.render();
		//batch.begin();
		this.currentPhase.draw(batch);		
		//batch.end();
		
		
	}

	public ArrayList<GameObject> getGameObjects() {
		return gameObjects;
	}

	@Override
	protected void onBackKeyPressed() {
		getGame().assets.soundAndMusicManager.playSound(Constants.SOUND_MENU_CLOSE, Constants.SOUND_DEFAULT_VOLUME);
		this.getGame().assets.soundAndMusicManager.stopRaceMusic();
		this.getGame().assets.soundAndMusicManager.playMenuMusic();
		Autickax.levelSelectScreen.dispose();
		Autickax.levelSelectScreen = new LevelSelectScreen(this.levelDifficulty);
		this.getGame().setScreen(Autickax.levelSelectScreen);
		Gdx.input.setInputProcessor(Autickax.levelSelectScreen.getStage());
	}
	
	public void goToMainScreen(){
		this.onBackKeyPressed();
	}

	@Override
	public void dispose() {
		super.dispose();
		
		this.level.deleteDistanceMap();
	}
	
	public Difficulty getDifficulty()
	{
		return this.levelDifficulty;
	}

	public PlayedLevel getPlayedLevel() {
		return this.playedLevel;
	}


	public int getLevelIndex() {
		return this.levelIndex;
	}

}
