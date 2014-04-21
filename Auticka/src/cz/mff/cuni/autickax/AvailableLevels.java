package cz.mff.cuni.autickax;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Vector;

public class AvailableLevels implements java.io.Externalizable {
	
	public Vector<LevelLoading> kiddieLevels;
	public Vector<LevelLoading> beginnerLevels;
	public Vector<LevelLoading> normalLevels;
	public Vector<LevelLoading> hardLevels;
	public Vector<LevelLoading> extremeLevels;
	
	public AvailableLevels() {
		this.kiddieLevels = new Vector<LevelLoading>();
		this.beginnerLevels = new Vector<LevelLoading>();
		this.normalLevels = new Vector<LevelLoading>();
		this.hardLevels = new Vector<LevelLoading>();
		this.extremeLevels = new Vector<LevelLoading>();
	}

	@SuppressWarnings("unchecked")
	@Override
	public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
		this.kiddieLevels = (Vector<LevelLoading>) in.readObject();
		this.beginnerLevels = (Vector<LevelLoading>) in.readObject();
		this.normalLevels = (Vector<LevelLoading>) in.readObject();
		this.hardLevels = (Vector<LevelLoading>) in.readObject();
		this.extremeLevels = (Vector<LevelLoading>) in.readObject();
	}

	@Override
	public void writeExternal(ObjectOutput out) throws IOException {
		out.writeObject(this.kiddieLevels);
		out.writeObject(this.beginnerLevels);
		out.writeObject(this.normalLevels);
		out.writeObject(this.hardLevels);
		out.writeObject(this.extremeLevels);
	}
}
