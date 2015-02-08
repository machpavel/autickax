package cz.cuni.mff.xcars;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Vector;

/*
 * Class containing available levels.
 */

public class AvailableLevels implements java.io.Externalizable {
	
	/** Map is not used, since we want to have control over level sets ordering. */
	public Vector<Scenario> scenarios;
	
	public AvailableLevels() {
		this.scenarios = new Vector<Scenario>();
	}

	@SuppressWarnings("unchecked")
	@Override
	public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
		this.scenarios = (Vector<Scenario>)in.readObject();
	}

	@Override
	public void writeExternal(ObjectOutput out) throws IOException {
		out.writeObject(this.scenarios);
	}
}
