package cz.mff.cuni.autickax;

import java.io.IOException;
import java.util.ArrayList;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlReader.Element;

import cz.mff.cuni.autickax.entities.Car;
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
	
	public void parseLevel(GameScreen gameScreen) throws IOException {
	
		System.out.println("Loading level...");
		
		Element root = new XmlReader().parse(this.file);			
		
		// Loading pathway
		Element pathwayElement = root.getChildByName("pathway");									
		Element controlPoints = pathwayElement.getChildByName("controlPoints");						
		for(int i = 0; i < controlPoints.getChildCount(); i++){
			Element controlPoint = controlPoints.getChild(i);
			this.pathway.getControlPoints().add(new Vector2(controlPoint.getFloat("X"), controlPoint.getFloat("Y")));
		}			
		for (Vector2 point : this.pathway.getControlPoints()) {
			System.out.println("point " + point);
		}
		this.pathway.CreateDistances();
		
		
		// Loading game objects
		Element entities = root.getChildByName("entities");
		for(int i = 0; i < entities.getChildCount() ; i++){
			Element gameObject = entities.getChild(i);
			String objectName = gameObject.getName(); 
			if(objectName.equals("mud")){
				gameObjects.add(new Mud(gameObject, gameScreen));
			}
			else if (objectName.equals("stone")){
				gameObjects.add(new Stone(gameObject, gameScreen));
			}
			else if (objectName.equals("tree")){
				gameObjects.add(new Tree(gameObject, gameScreen));
			}
			else throw new IOException("Loading object failed: Unknown type");
		}
		for (GameObject gameObject : gameObjects) {
			System.out.println(gameObject.toString());
		}
		
		// Loading car
		Element car = root.getChildByName("car");
		this.car = new Car(car, gameScreen);
		System.out.println(car.toString());
		
		// Background
		Element backgroundTexture = root.getChildByName("backgroundTexture");
		this.backgroundTextureName = backgroundTexture.get("textureName");
		System.out.println(this.backgroundTextureName);
		
		System.out.println("Loading done...");
	}
}
