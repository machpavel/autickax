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
	public GameScreen(FileHandle file) {
		super();
		try {
			camera = new OrthographicCamera();
			camera.setToOrtho(false, stageWidth, stageHeight);

			//graphics
			batch = new SpriteBatch();
			shapeRenderer = new ShapeRenderer();
						
			// Init entities
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
			pathway.setType(pathwayElement.getAttribute("pathwayType"));
			Element controlPoints = pathwayElement.getChildByName("controlPoints");						
			for(int i = 0; i < controlPoints.getChildCount(); i++){
				Element controlPoint = controlPoints.getChild(i);
				pathway.getControlPoints().add(new Vector2(controlPoint.getFloat("X"), controlPoint.getFloat("Y")));
			}			
			for (Vector2 point : pathway.getControlPoints()) {
				System.out.println("point " + point);
			}
			pathway.CreateDistances();
			
			//TODO correct positions
			start = new Start(pathway.GetPosition(SubLevel.START).x, pathway.GetPosition(0).y, this, 0);
			finish = new Finish(pathway.GetPosition(SubLevel.FINISH).x, pathway.GetPosition(1).y, this, 0);
			
			
			// Loading game objects
			Element entities = root.getChildByName("entities");
			for(int i = 0; i < entities.getChildCount() ; i++){
				Element gameObject = entities.getChild(i);
				String name = gameObject.getName(); 
				if(name.equals("mud")){
					gameObjects.add(new Mud(gameObject.getFloat("X"), gameObject.getFloat("Y"), this, gameObject.getInt("type", 0)));
				}
				else if (name.equals("stone")){
					gameObjects.add(new Stone(gameObject.getFloat("X"), gameObject.getFloat("Y"), this, gameObject.getInt("type", 0)));
				}
				else if (name.equals("tree")){
					gameObjects.add(new Tree(gameObject.getFloat("X"), gameObject.getFloat("Y"), this, gameObject.getInt("type", 0)));
				}
				else throw new IOException("Loading object failed: Unknown type");
			}
			for (GameObject gameObject : gameObjects) {
				System.out.println(gameObject.toString());
			}
			
			// Loading car
			Element car = root.getChildByName("car");
			this.car = new Car(car.getFloat("X"), car.getFloat("Y"), this, car.getInt("type", 0));			
			System.out.println(this.car.toString());
			
			// Background
			Element backgroundTexture = root.getChildByName("backgroundTexture");
			this.backGroundTextureString = backgroundTexture.get("textureName");
			this.backgroundTexture = game.assets.getGraphics(backGroundTextureString);
			System.out.println(backGroundTextureString);
			
			this.currentPhase = new SubLevel1(this);					
			System.out.println("Loading done...");
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
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
		
		batch.end();
		
		this.currentPhase.render();
	}

	public ArrayList<GameObject> getGameObjects() {
		return gameObjects;
	}


}
