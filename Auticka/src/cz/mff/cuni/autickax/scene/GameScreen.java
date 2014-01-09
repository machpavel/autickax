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
import cz.mff.cuni.autickax.Constants;
import cz.mff.cuni.autickax.Level;
import cz.mff.cuni.autickax.entities.Car;
import cz.mff.cuni.autickax.entities.Finish;
import cz.mff.cuni.autickax.entities.GameObject;
import cz.mff.cuni.autickax.entities.Mud;
import cz.mff.cuni.autickax.entities.Start;
import cz.mff.cuni.autickax.entities.Stone;
import cz.mff.cuni.autickax.entities.Tree;
import cz.mff.cuni.autickax.gamelogic.CheckPoint;
import cz.mff.cuni.autickax.gamelogic.SubLevel;
import cz.mff.cuni.autickax.gamelogic.SubLevel1;
import cz.mff.cuni.autickax.gamelogic.SubLevel2;
import cz.mff.cuni.autickax.pathway.DistanceMap;
import cz.mff.cuni.autickax.pathway.Pathway;

public class GameScreen extends BaseScreen {
	// Textures
	private TextureRegion backgroundTexture;
	private TextureRegion pathwayTexture;
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
	protected Start start;
	protected Finish finish;
	
		
	// Cached font
	private BitmapFont font;
	public BitmapFont getFont() {
		// TODO: consider loading font elsewhere, once for the whole game
		return font;
	}
	
	// Pathway
	protected Pathway pathway;
		
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
			this.start = level.getStart();
			this.finish = level.getFinish();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		this.pathwayTexture = game.assets.getGraphics(name);
			
		// Start Music!
		game.assets.music.setLooping(true);
		game.assets.music.setVolume(Constants.MUSIC_DEFAULT_VOLUME);
		game.assets.music.play();
		
		this.currentPhase = new SubLevel1(this);	
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
	
	public void switchToPhase2(LinkedList<CheckPoint> checkpoints, DistanceMap map) {
		this.currentPhase = new SubLevel2(this, checkpoints, map);
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
		batch.draw(this.backgroundTexture, 0, 0, stageWidth, stageHeight);
		batch.enableBlending(); //don't forget to enabled this for alpha channel
		batch.draw(this.pathwayTexture, 0, 0, stageWidth, stageHeight);
		batch.end();
				
		this.currentPhase.render();
		
		batch.begin();
		this.currentPhase.draw(batch);		
		batch.end();
		
		
	}

	public ArrayList<GameObject> getGameObjects() {
		return gameObjects;
	}


}
