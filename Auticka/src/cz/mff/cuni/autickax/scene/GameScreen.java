package cz.mff.cuni.autickax.scene;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlReader.Element;

import cz.mff.cuni.autickax.Assets;
import cz.mff.cuni.autickax.Autickax;
import cz.mff.cuni.autickax.Level;
import cz.mff.cuni.autickax.entities.Car;
import cz.mff.cuni.autickax.entities.GameObject;
import cz.mff.cuni.autickax.entities.Mud;
import cz.mff.cuni.autickax.entities.Stone;
import cz.mff.cuni.autickax.entities.Tree;
import cz.mff.cuni.autickax.gamelogic.CheckPoint;
import cz.mff.cuni.autickax.gamelogic.SubLevel;
import cz.mff.cuni.autickax.gamelogic.SubLevel1;
import cz.mff.cuni.autickax.gamelogic.SubLevel2;
import cz.mff.cuni.autickax.pathway.DistanceMap;
import cz.mff.cuni.autickax.pathway.Pathway;
/**
 * This is the screen where the game happens
 * @author Ondrej Paska
 */

public class GameScreen extends BaseScreen {
	// Textures
	private TextureRegion backgroundTexture;
	private String backGroundTextureString;

	// Rendering
	protected OrthographicCamera camera;
	protected SpriteBatch batch;
	protected ShapeRenderer shapeRenderer;
	
	// Levels
	private SubLevel currentPhase;

	// Entities
	private ArrayList<GameObject> gameObjects;
	protected Car car;
	public Car getCar()
	{
		return this.car;
	}
	
	// Cached font
	private BitmapFont font;
	public BitmapFont getFont() {
		// TODO: consider loading font elsewhere, once for the whole game
		return font;
	}
	
	// Pathway
	protected Pathway pathway;
	
	public GameScreen() {
		super();

		camera = new OrthographicCamera();
		camera.setToOrtho(false, 800, 480);

		batch = new SpriteBatch();
		shapeRenderer = new ShapeRenderer();
		
		// TODO: don't load phase 1 here, but after initialization. because of editor
		this.currentPhase = new SubLevel1(this);

		Assets assets = Autickax.getInstance().assets;
		this.backGroundTextureString = "sky";
		this.backgroundTexture = assets.getGraphics(backGroundTextureString);
		
		// dummy code ------------------------>		
		this.gameObjects = new ArrayList<GameObject>();
				
		// <------------------------ dummy code
		
		this.font = game.assets.getFont();
		
		// Pathway
		pathway = new Pathway();
		
		// Car
		car = new Car(0, 0, 100, 63, this, "car");

		
		// Start Music!
		game.assets.music.setLooping(true);
		game.assets.music.setVolume(0.3f);
		game.assets.music.play();
	}
	
	public Pathway getPathWay()
	{
		return pathway;
	}
	
	// Creates instance according to a given xml file
	public GameScreen(String name) {
		super();
		
		camera = new OrthographicCamera();
		camera.setToOrtho(false, stageWidth, stageHeight);

		//graphics
		batch = new SpriteBatch();
		shapeRenderer = new ShapeRenderer();
					

		font = game.assets.getFont();
			
		try {
			Level level = new Level(game.assets.loadLevel(name));		
			level.parseLevel(this);
			this.pathway = level.getPathway();
			this.gameObjects = level.getGameObjects();
			this.backGroundTextureString = level.getBackgroundTextureName();
			this.backgroundTexture = game.assets.getGraphics(this.backGroundTextureString);
			this.car = level.getCar();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
			
		// Start Music!
		game.assets.music.setLooping(true);
		game.assets.music.setVolume(0.3f);
		game.assets.music.play();
		
		this.currentPhase = new SubLevel1(this);	
	}
	
	public void switchToPhase2(LinkedList<CheckPoint> checkpoints, DistanceMap map, double pathFollowingTime) {
		this.currentPhase = new SubLevel2(this, checkpoints, map, pathFollowingTime);
	}
	
	public void reset() {
		// TODO: reseting of all game objects
	}
	
	public void unproject(Vector3 vector) {
		this.camera.unproject(vector);
	}

	private void update(float delta) {		
		// TODO: Consider moving this into sublevels
		for (GameObject gameObject : this.getGameObjects()) {
			gameObject.update(delta);
		}
		
		this.currentPhase.update(delta);
	}

	@Override
	public void render(float delta) {
		
		update(delta); //Update our world
		
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT); // This cryptic line clears the screen

		camera.update();
		batch.setProjectionMatrix(camera.combined);  // see https://github.com/libgdx/libgdx/wiki/Orthographic-camera
		
		batch.begin();
		
		batch.disableBlending(); //performance boost
		
		// background
		batch.draw(backgroundTexture, 0, 0, stageWidth, stageHeight);
		
		batch.enableBlending(); //don't forget to enabled this for alpha channel
		

		
		this.currentPhase.draw(batch);

		// TODO: Consider moving this into sublevels
		/*for (int i = 0; i < this.gameObjects.size(); ++i) {
			this.gameObjects.get(i).draw(this.batch);
		}*/
		// When Michal repairs it, uncomment
		
		batch.end();
		
		this.currentPhase.render();
	}

	public ArrayList<GameObject> getGameObjects() {
		return gameObjects;
	}


}
