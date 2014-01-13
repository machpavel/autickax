package cz.mff.cuni.autickax;

import java.io.IOException;
import java.util.ArrayList;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlReader.Element;

import cz.mff.cuni.autickax.entities.Car;
import cz.mff.cuni.autickax.entities.Finish;
import cz.mff.cuni.autickax.entities.Hole;
import cz.mff.cuni.autickax.entities.Start;
import cz.mff.cuni.autickax.entities.GameObject;
import cz.mff.cuni.autickax.entities.Mud;
import cz.mff.cuni.autickax.entities.Stone;
import cz.mff.cuni.autickax.entities.Tree;
import cz.mff.cuni.autickax.pathway.Pathway;
import cz.mff.cuni.autickax.scene.GameScreen;

public class LevelLoading implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	
	private Pathway pathway = new Pathway();
	private ArrayList<GameObject> gameObjects = new ArrayList<GameObject>();
	private Car car;
	private int backgroundType;
	private Start start;
	private Finish finish;
	private int pathwayTextureType;
	private float timeLimit;
	
	public Pathway getPathway() {
		return this.pathway;
	}
	
	public Car getCar() {
		return this.car;
	}
	
	public ArrayList<GameObject> getGameObjects() {
		return this.gameObjects;
	}
	
	public int getBackgroundType() {
		return this.backgroundType;
	}
	
	public Start getStart() {
		return this.start;
	}
	
	public Finish getFinish() {
		return this.finish;
	}
	
	public float getTimeLimit()
	{
		return timeLimit;
	}
	
	public int getPathwayTextureType() {
		return this.pathwayTextureType;
	}
	
	//TODO add TIMELIMIT to XML
	public void parseLevel(GameScreen gameScreen, FileHandle file) throws Exception {
	
		System.out.println("Loading level...");
		
		Element root = new XmlReader().parse(file);			
		
		// Loading pathway
		Element pathwayElement = root.getChildByName("pathway");
		pathway.setType(pathwayElement.getAttribute("pathwayType"));
		pathway.setTypeOfInterpolation(pathwayElement.getAttribute("typeOfInterpolation"));
		pathwayTextureType = pathwayElement.getInt("textureType");
		Element controlPoints = pathwayElement.getChildByName("controlPoints");
		int controlPointsCount = controlPoints.getChildCount(); 
		for(int i = 0; i < controlPointsCount; i++){			
			Element controlPoint = controlPoints.getChild(i);
			Vector2 controlPointPosition = new Vector2(controlPoint.getFloat("X"), controlPoint.getFloat("Y"));
			this.pathway.getControlPoints().add(controlPointPosition);			
		}			
		for (Vector2 point : this.pathway.getControlPoints()) {
			System.out.println("point " + point);
		}
		
				
		// Loading game objects
		Element entities = root.getChildByName("entities");
		for(int i = 0; i < entities.getChildCount() ; i++){
			Element gameObject = entities.getChild(i);
			String objectName = gameObject.getName(); 
			if(objectName.equals("mud")){
				gameObjects.add(new Mud(gameObject.getFloat("X"), gameObject.getFloat("Y"), gameScreen, gameObject.getInt("type", 0)));
			}
			else if (objectName.equals("stone")){
				gameObjects.add(new Stone(gameObject.getFloat("X"), gameObject.getFloat("Y"), gameScreen, gameObject.getInt("type", 0)));
			}
			else if (objectName.equals("tree")){
				gameObjects.add(new Tree(gameObject.getFloat("X"), gameObject.getFloat("Y"), gameScreen, gameObject.getInt("type", 0)));
			}
			else if (objectName.equals("hole")){
				gameObjects.add(new Hole(gameObject.getFloat("X"), gameObject.getFloat("Y"), gameScreen, gameObject.getInt("type", 0)));
			}
			else throw new IOException("Loading object failed: Unknown type");
		}
		for (GameObject gameObject : gameObjects) {
			System.out.println(gameObject.toString());
		}
		
		// Loading car
		Element car = root.getChildByName("car");
		this.car = new Car(car.getFloat("X"), car.getFloat("Y"), gameScreen, car.getInt("type", 1));
		System.out.println(car.toString());
		
		//Loading start
		Element start = root.getChildByName("start");
		this.start = new Start(start.getFloat("X"), start.getFloat("Y"), gameScreen, start.getInt("type", 1));
		System.out.println(start.toString());
		
		//Loading finish
		Element finish = root.getChildByName("finish");
		this.finish = new Finish(finish.getFloat("X"), finish.getFloat("Y"), gameScreen, finish.getInt("type", 1));
		System.out.println(finish.toString());
				
		
		// Background
		this.backgroundType = root.getInt("levelBackgroundType");		
		
		// Time limit
		this.timeLimit = root.getFloat("timeLimit");
		
		System.out.println("Loading done...");
	}
	
	public void calculateDistanceMap() {
		this.pathway.CreateDistances();
	}
	
	public void deleteDistanceMap() {
		this.pathway.deleteDistanceMap();
	}
	
	public void setGameScreen(GameScreen screen) {
		for (GameObject gameObject : this.gameObjects) {
			gameObject.setScreen(screen);
			gameObject.setTexture();
		}
		this.car.setScreen(screen);
		this.start.setScreen(screen);
		this.finish.setScreen(screen);
		this.car.setTexture();
		this.start.setTexture();
		this.finish.setTexture();
	}
}
