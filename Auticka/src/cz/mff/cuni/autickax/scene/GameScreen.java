package cz.mff.cuni.autickax.scene;

import java.util.ArrayList;
import java.util.LinkedList;

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
import cz.mff.cuni.autickax.drawing.LevelBackground;
import cz.mff.cuni.autickax.entities.Car;
import cz.mff.cuni.autickax.entities.Finish;
import cz.mff.cuni.autickax.entities.GameObject;
import cz.mff.cuni.autickax.entities.Start;
import cz.mff.cuni.autickax.gamelogic.CheckPoint;
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
	
	private final Difficulty difficulty;
	
	
	
	// Creates instance according to a given xml file
	public GameScreen(LevelLoading level, Difficulty difficulty) {
		super();
		
		this.level = level;
		this.difficulty = difficulty;
		
		camera = new OrthographicCamera();
		camera.setToOrtho(false, stageWidth, stageHeight);
		

		
		
		level.calculateDistanceMap();
		level.setGameScreen(this);
								
		this.pathwayTexture = level.getPathway().getDistanceMap().generateTexture(this.difficulty);
		
		this.pathway = level.getPathway();
		this.gameObjects = level.getGameObjects();
		
		this.levelBackground = new LevelBackground();
		this.levelBackground.SetType(level.getBackgroundType());
		this.car = level.getCar();		
		
		
		this.start = level.getStart();
		Vector2 startDirection = new Vector2(this.pathway.GetPosition(Constants.START_POSITION_IN_CURVE + EPSILON_F)).sub(this.start.getPosition()).nor();
		this.start.setRotation((startDirection.angle() + 270) % 360);
		
		this.finish = level.getFinish();
		Vector2 finishDirection = new Vector2(this.pathway.GetPosition(Constants.FINISH_POSITION_IN_CURVE - EPSILON_F)).sub(this.finish.getPosition()).nor();
		this.finish.setRotation((finishDirection.angle() + 90) % 360);
		
		this.currentPhase = new SubLevel1(this, level.getTimeLimit());
		// Start Music!
		getGame().assets.raceMusic.setLooping(true);
		getGame().assets.raceMusic.setVolume(Constants.MUSIC_DEFAULT_VOLUME);
		getGame().assets.raceMusic.play();
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
		this.currentPhase = phase1;
	}
	public void switchToPhase2(LinkedList<CheckPoint> checkpoints, DistanceMap map, SubLevel1 lastPhase) {
		this.currentPhase = new SubLevel2(this, checkpoints, map, lastPhase);
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
		this.getGame().assets.raceMusic.stop();
		this.getGame().assets.menuMusic.play();
		Autickax.levelSelectScreen.dispose();
		Autickax.levelSelectScreen = new LevelSelectScreen(this.difficulty);
		this.getGame().setScreen(Autickax.levelSelectScreen);
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
		return this.difficulty;
	}

}
