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

import cz.mff.cuni.autickax.entities.Car;
import cz.mff.cuni.autickax.entities.GameObject;
import cz.mff.cuni.cz.autickax.pathway.Pathway;

/**
 * This is the screen where the game happens
 * 
 * @author Ondrej Paska
 */

public class GameScreen extends BaseScreen {
	// Textures
	protected TextureRegion skyTexture;

	// Rendering
	protected OrthographicCamera camera;
	protected SpriteBatch batch;
	protected ShapeRenderer shapeRenderer;

	// Entities
	protected Car car;
	protected ArrayList<GameObject> gameObjects;

	// Score
	private int score = 0;
	// Time
	private float timeElapsed = 0;

	// Cached font
	private BitmapFont font;
	
	// Pathway
	protected Pathway pathway;

	public GameScreen() {
		super();

		camera = new OrthographicCamera();
		camera.setToOrtho(false, 800, 480);

		//graphics
		batch = new SpriteBatch();
		shapeRenderer = new ShapeRenderer();
		
		this.skyTexture = game.assets.getGraphics("sky");

		// Init entities
		this.car = new Car(stageWidth * 0.25f, 70, this);
		this.gameObjects = new ArrayList<GameObject>();

		font = game.assets.getFont();
		
		// Pathway
		pathway = new Pathway();

		// Start Music!
		game.assets.music.setLooping(true);
		game.assets.music.setVolume(0.3f);
		game.assets.music.play();			
	}
	
	// Creates instance according to a given xml file
	public GameScreen(FileHandle file){
		super();
		try {
			camera = new OrthographicCamera();
			camera.setToOrtho(false, 800, 480);

			//graphics
			batch = new SpriteBatch();
			shapeRenderer = new ShapeRenderer();
			
			this.skyTexture = game.assets.getGraphics("sky");

			// Init entities
			//this.car = new Car(stageWidth * 0.25f, 70, this);
			this.gameObjects = new ArrayList<GameObject>();

			font = game.assets.getFont();
			
			// Pathway
			pathway = new Pathway();

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
				pathway.controlPoints.add(new Vector2(controlPoint.getFloat("X"), controlPoint.getFloat("Y")));
			}			
			for (Vector2 point : pathway.controlPoints) {
				System.out.println("point " + point);
			}
			
			// Loading game objects
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
			}
			
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

	public void unproject(Vector3 vector) {
		this.camera.unproject(vector);
	}

	private void update(float delta) {

		timeElapsed += delta;

		this.car.update(delta);
	}

	@Override
	public void render(float delta) {

		update(delta); // Update our world

		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT); // This cryptic line clears
													// the screen

		camera.update();
		batch.setProjectionMatrix(camera.combined); // see
													// https://github.com/libgdx/libgdx/wiki/Orthographic-camera

		batch.begin();

		batch.disableBlending(); // performance boost

		// SKY
		batch.draw(skyTexture, 0, 0, stageWidth, stageHeight);

		batch.enableBlending(); // don't forget to enabled this for alpha
								// channel

		this.car.draw(batch, delta);

		// Draw score
		font.draw(batch, "score: " + score, 10, (int) stageHeight - 32);
		// Draw time
		font.draw(batch, "time: " + ((int) timeElapsed), (int) stageWidth / 2,
				(int) stageHeight - 32);

		batch.end();
	}
	
	

}
