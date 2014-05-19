package cz.mff.cuni.autickax;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.ArrayList;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlReader.Element;

import cz.mff.cuni.autickax.drawing.LevelBackground;
import cz.mff.cuni.autickax.entities.Car;
import cz.mff.cuni.autickax.entities.Finish;
import cz.mff.cuni.autickax.entities.GameObject;
import cz.mff.cuni.autickax.entities.Start;
import cz.mff.cuni.autickax.pathway.Pathway;

public class Level implements java.io.Externalizable {

	private static final byte MAGIC_LEVEL_END = (byte) 255;

	private Pathway pathway;
	private ArrayList<GameObject> gameObjects;
	private Car car;
	private Start start;
	private Finish finish;
	private float timeLimit;
	private LevelBackground background;

	public Pathway getPathway() {
		return this.pathway;
	}

	public Car getCar() {
		return this.car;
	}

	public ArrayList<GameObject> getGameObjects() {
		return this.gameObjects;
	}

	public Start getStart() {
		return this.start;
	}

	public Finish getFinish() {
		return this.finish;
	}

	public float getTimeLimit() {
		return timeLimit;
	}

	public LevelBackground getLevelBackground() {
		return this.background;
	}

	public int getPathwayTextureType() {
		return this.pathway.getTextureType();
	}

	public void parseLevel(FileHandle file) throws Exception {

		Element root = new XmlReader().parse(file);

		this.pathway = Pathway.parsePathway(root);

		// Loading game objects
		Element entities = root.getChildByName("entities");

		this.gameObjects = new ArrayList<GameObject>();
		for (int i = 0; i < entities.getChildCount(); i++) {
			Element gameObject = entities.getChild(i);

			this.gameObjects.add(GameObject.parseGameObject(gameObject));
		}

		// Loading car
		Element car = root.getChildByName(Car.name);
		this.car = Car.parseCar(car);

		// Loading start
		Element start = root.getChildByName(Start.name);
		this.start = Start.parseStart(start);

		// Loading finish
		Element finish = root.getChildByName(Finish.name);
		this.finish = Finish.parseFinish(finish);

		// Background
		Element background = root.getChildByName("background");
		this.background = LevelBackground.parseLevelBackground(background);

		// Time limit
		this.timeLimit = root.getFloat("timeLimit");

		System.out.println("Loading level \"" + file.name() + "\" done.");
	}
	
	public float getDistanceMapProgress() {
		if (this.pathway.getDistanceMap() != null) {
			return this.pathway.getDistanceMap().getProgress();
		} else {
			return 0;
		}
	}

	public void setTextures() {
		for (GameObject gameObject : this.gameObjects) {			
			if(gameObject.getTexture() == null)
				gameObject.setTexture();
		}		
		if(this.car.getTexture() == null)
			this.car.setTexture();
		if(this.start.getTexture() == null)
			this.start.setTexture();
		if(this.finish.getTexture() == null)
			this.finish.setTexture();
	}

	@SuppressWarnings("unchecked")
	@Override
	public void readExternal(ObjectInput in) throws IOException,
			ClassNotFoundException {

		this.pathway = (Pathway) in.readObject();

		this.gameObjects = (ArrayList<GameObject>) in.readObject();

		this.car = (Car) in.readObject();
		this.background = (LevelBackground) in.readObject();
		this.start = (Start) in.readObject();
		this.finish = (Finish) in.readObject();
		this.timeLimit = in.readFloat();

		byte check = in.readByte();
		if(check != Level.MAGIC_LEVEL_END)
			throw new RuntimeException("Level wasn't read correctly");
	}

	@Override
	public void writeExternal(ObjectOutput out) throws IOException {

		out.writeObject(this.pathway);

		out.writeObject(this.gameObjects);

		out.writeObject(this.car);
		out.writeObject(this.background);
		out.writeObject(this.start);
		out.writeObject(this.finish);
		out.writeFloat(this.timeLimit);

		out.writeByte(Level.MAGIC_LEVEL_END);
	}
}
