package cz.cuni.mff.autickax;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Vector;

public class AvailableLevels implements java.io.Externalizable {
	
	public Vector<Level> kiddieLevels;
	public Vector<Level> beginnerLevels;
	public Vector<Level> normalLevels;
	public Vector<Level> hardLevels;
	public Vector<Level> extremeLevels;
	
	public AvailableLevels() {
		this.kiddieLevels = new Vector<Level>();
		this.beginnerLevels = new Vector<Level>();
		this.normalLevels = new Vector<Level>();
		this.hardLevels = new Vector<Level>();
		this.extremeLevels = new Vector<Level>();
	}

	@SuppressWarnings("unchecked")
	@Override
	public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
		this.kiddieLevels = (Vector<Level>) in.readObject();
		this.beginnerLevels = (Vector<Level>) in.readObject();
		this.normalLevels = (Vector<Level>) in.readObject();
		this.hardLevels = (Vector<Level>) in.readObject();
		this.extremeLevels = (Vector<Level>) in.readObject();
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
