package cz.mff.cuni.autickax;

import java.io.IOException;
import java.util.ArrayList;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlReader.Element;

import cz.mff.cuni.autickax.entities.Car;
import cz.mff.cuni.autickax.entities.Finish;
import cz.mff.cuni.autickax.entities.Start;
import cz.mff.cuni.autickax.entities.GameObject;
import cz.mff.cuni.autickax.entities.Mud;
import cz.mff.cuni.autickax.entities.Stone;
import cz.mff.cuni.autickax.entities.Tree;
import cz.mff.cuni.autickax.pathway.Pathway;
import cz.mff.cuni.autickax.scene.GameScreen;

public class Level {
	private final FileHandle file;
	private Pathway pathway = new Pathway();
	private ArrayList<GameObject> gameObjects = new ArrayList<GameObject>();
	private Car car;
	private String backgroundTextureName;
	private Start start;
	private Finish finish;
	private int pathwayTextureType;
	private float timeLimit;
	
	public Level (FileHandle file) {
		this.file = file;
	}
	
	public Pathway getPathway() {
		return this.pathway;
	}
	
	public Car getCar() {
		return this.car;
	}
	
	public ArrayList<GameObject> getGameObjects() {
		return this.gameObjects;
	}
	
	public String getBackgroundTextureName() {
		return this.backgroundTextureName;
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
	public void parseLevel(GameScreen gameScreen) throws Exception {
	
		System.out.println("Loading level...");
		
		Element root = new XmlReader().parse(this.file);			
		
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
		this.pathway.CreateDistances();
		
		//TODO correct positions
		this.start = new Start(pathway.GetPosition(Constants.START_POSITION_IN_CURVE).x, pathway.GetPosition(0).y, gameScreen, 0);
		this.finish = new Finish(pathway.GetPosition(Constants.FINISH_POSITION_IN_CURVE).x, pathway.GetPosition(1).y, gameScreen, 0);
		
		
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
			else throw new IOException("Loading object failed: Unknown type");
		}
		for (GameObject gameObject : gameObjects) {
			System.out.println(gameObject.toString());
		}
		
		// Loading car
		Element car = root.getChildByName("car");
		this.car = new Car(car.getFloat("X"), car.getFloat("Y"), gameScreen, car.getInt("type", 0));
		System.out.println(car.toString());
		
		// Background
		Element backgroundTexture = root.getChildByName("backgroundTexture");
		this.backgroundTextureName = backgroundTexture.get("textureName");
		System.out.println(this.backgroundTextureName);
		
		this.timeLimit = root.getFloat("timeLimit");
		
		System.out.println("Loading done...");
	}
}
