package cz.cuni.mff.xcars;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Vector;

public class Scenario implements java.io.Externalizable {
	public Vector<Level> levels;
	public String name;
	
	@SuppressWarnings("unchecked")
	@Override
	public void readExternal(ObjectInput in) throws IOException,
			ClassNotFoundException {
		this.levels = (Vector<Level>)in.readObject();
		this.name = (String)in.readUTF();
	}
	
	@Override
	public void writeExternal(ObjectOutput out) throws IOException {
		out.writeObject(this.levels);
		out.writeUTF(this.name);
	}
	
	
}
