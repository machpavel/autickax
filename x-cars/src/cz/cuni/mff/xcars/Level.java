package cz.cuni.mff.xcars;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.ArrayList;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlReader.Element;

import cz.cuni.mff.xcars.drawing.LevelBackground;
import cz.cuni.mff.xcars.entities.Car;
import cz.cuni.mff.xcars.entities.Finish;
import cz.cuni.mff.xcars.entities.GameObject;
import cz.cuni.mff.xcars.entities.Start;
import cz.cuni.mff.xcars.pathway.Pathway;

public class Level implements java.io.Externalizable {

	private static final byte MAGIC_LEVEL_END = (byte) 255;

	private Pathway pathway;
	private ArrayList<GameObject> gameObjects;
	private ArrayList<GameObject> universalObjects;
	private Car car;
	private Start start;
	private Finish finish;
	private float timeLimit;
	private LevelBackground background;
	private Difficulty difficulty;

	public Pathway getPathway() {
		return this.pathway;
	}

	public Car getCar() {
		return this.car;
	}

	public ArrayList<GameObject> getGameObjects() {
		return this.gameObjects;
	}

	public ArrayList<GameObject> getUniversalObjects() {
		return this.universalObjects;
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
		if (entities != null) {
			for (int i = 0; i < entities.getChildCount(); i++) {
				Element gameObject = entities.getChild(i);
				this.gameObjects.add(GameObject.parseGameObject(gameObject));
			}
		}

		// Loading universal objects
		Element universalObjects = root.getChildByName("drawings");
		this.universalObjects = new ArrayList<GameObject>();
		if (universalObjects != null) {
			for (int i = 0; i < universalObjects.getChildCount(); i++) {
				Element universalObject = universalObjects.getChild(i);
				this.universalObjects.add(GameObject.parseGameObject(universalObject));
			}
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

		Element difficulty = root.getChildByName("difficulty");
		this.setDifficulty(Difficulty.valueOf(difficulty.getText()));

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
			if (gameObject.getTexture() == null)
				gameObject.setTexture();
		}

		for (GameObject universalObject : this.universalObjects) {
			universalObject.setTexture();
		}
		if (this.car.getTexture() == null)
			this.car.setTexture();
		if (this.start.getTexture() == null)
			this.start.setTexture();
		if (this.finish.getTexture() == null)
			this.finish.setTexture();
	}

	@SuppressWarnings("unchecked")
	@Override
	public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {

		this.pathway = (Pathway) in.readObject();

		this.gameObjects = (ArrayList<GameObject>) in.readObject();
		this.universalObjects = (ArrayList<GameObject>) in.readObject();

		this.car = (Car) in.readObject();
		this.background = (LevelBackground) in.readObject();
		this.start = (Start) in.readObject();
		this.finish = (Finish) in.readObject();
		this.timeLimit = in.readFloat();
		this.difficulty = (Difficulty) in.readObject();

		byte check = in.readByte();
		if (check != Level.MAGIC_LEVEL_END)
			throw new RuntimeException("Level wasn't read correctly");
	}

	@Override
	public void writeExternal(ObjectOutput out) throws IOException {

		out.writeObject(this.pathway);

		out.writeObject(this.gameObjects);
		out.writeObject(this.universalObjects);

		out.writeObject(this.car);
		out.writeObject(this.background);
		out.writeObject(this.start);
		out.writeObject(this.finish);
		out.writeFloat(this.timeLimit);
		out.writeObject(this.difficulty);

		out.writeByte(Level.MAGIC_LEVEL_END);
	}

	public Difficulty getDifficulty() {
		return difficulty;
	}

	public void setDifficulty(Difficulty difficulty) {
		this.difficulty = difficulty;
	}
}
