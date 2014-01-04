package cz.mff.cuni.autickax.scene;

import java.io.IOException;
import java.util.ArrayList;

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
import cz.mff.cuni.autickax.MyGdxGame;
import cz.mff.cuni.autickax.entities.Car;
import cz.mff.cuni.autickax.entities.GameObject;
import cz.mff.cuni.autickax.gamelogic.CheckPoint;
import cz.mff.cuni.autickax.gamelogic.SubLevel;
import cz.mff.cuni.autickax.gamelogic.SubLevel1;
import cz.mff.cuni.autickax.gamelogic.SubLevel2;
import cz.mff.cuni.autickax.pathway.Pathway;
/**
 * This is the screen where the game happens
 * @author Ondrej Paska
 */

public class GameScreen extends BaseScreen {
	// Textures
	private TextureRegion backgroundTexture;

	// Rendering
	protected OrthographicCamera camera;
	protected SpriteBatch batch;
	protected ShapeRenderer shapeRenderer;
	
	// Levels
	private SubLevel currentPhase;

	// Entities
	protected ArrayList<GameObject> gameObjects;
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
		
		this.currentPhase = new SubLevel1(this);

		Assets assets = MyGdxGame.getInstance().assets;
		this.backgroundTexture = assets.getGraphics("sky");
		
		// dummy code ------------------------>
		
		this.gameObjects = new ArrayList<GameObject>();
		this.gameObjects.add(new Car(stageWidth * 0.25f, 70, this));
		
		// <------------------------ dummy code
		
		this.font = game.assets.getFont();
		
		// Pathway
		pathway = new Pathway();
		
		// Start Music!
		game.assets.music.setLooping(true);
		game.assets.music.setVolume(0.3f);
		game.assets.music.play();
	}
	
	// Creates instance according to a given xml file
	public GameScreen(FileHandle file) {
		super();
		try {
			camera = new OrthographicCamera();
			camera.setToOrtho(false, stageWidth, stageHeight);

			//graphics
			batch = new SpriteBatch();
			shapeRenderer = new ShapeRenderer();
			
			this.currentPhase = new SubLevel1(this);
			
			this.backgroundTexture = game.assets.getGraphics("sky");

			// Init entities
			//this.car = new Car(stageWidth * 0.25f, 70, this);
			this.gameObjects = new ArrayList<GameObject>();

			font = game.assets.getFont();
			
			// Pathway
			this.pathway = new Pathway();

			// Start Music!
			game.assets.music.setLooping(true);
			game.assets.music.setVolume(0.3f);
			game.assets.music.play();				
			
			System.out.println("Loading level...");
			Element root = new XmlReader().parse(file);			
			
			// Loading pathway
			Element pathwayElement = root.getChildByName("pathway");									
			Element controlPoints = pathwayElement.getChildByName("controlPoints");						
			for(int i = 0; i < controlPoints.getChildCount(); i++){
				Element controlPoint = controlPoints.getChild(i);
				pathway.getControlPoints().add(new Vector2(controlPoint.getFloat("X"), controlPoint.getFloat("Y")));
			}			
			for (Vector2 point : pathway.getControlPoints()) {
				System.out.println("point " + point);
			}
			
			/*// Loading game objects
			Element entities = root.getChildByName("entities");
			for(int i = 0; i < entities.getChildCount() ; i++){
				Element gameObject = entities.getChild(i);
				gameObjects.add(new GameObject(gameObject.getFloat("X"),gameObject.getFloat("Y")){
					@Override
					public void update(float delta) {}					
					@Override
					public String getName() {						
						return "blabla";
					}
				});
			}
			for (GameObject gameObject : gameObjects) {
				System.out.println(gameObject.toString());
			}*/
			
			// Loading car
			Element car = root.getChildByName("car");
			this.car = new Car(car.getFloat("X"), car.getFloat("Y"), this);
			System.out.println(car.toString());
						
			System.out.println("Loading done...");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
	}
	
	public void switchToPhase2(CheckPoint[] checkpoints, double pathFollowingAccuracy, double pathFollowingTime) {
		this.currentPhase = new SubLevel2(this, checkpoints, pathFollowingAccuracy, pathFollowingTime);
	}
	
	public void reset() {
		// TODO: reseting of all game objects
	}
	
	public void unproject(Vector3 vector) {
		this.camera.unproject(vector);
	}

	private void update(float delta) {		
		// TODO: Consider moving this into sublevels
		/*for (int i = 0; i < this.gameObjects.size(); ++i) {
			this.gameObjects.get(i).update(delta);
		}*/
		// When Michal repairs it, uncomment
		
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
	}

}
