package cz.mff.cuni.autickax;

import java.util.Vector;

public class AvailableLevels implements java.io.Serializable {
	
	private static final long serialVersionUID = 1L;
	
	public Vector<Level> kiddieLevels;
	public Vector<Level> beginnerLevels;
	public Vector<Level> normalLevels;
	public Vector<Level> hardLevels;
	public Vector<Level> extremeLevels;
	
	public AvailableLevels() {
		this.kiddieLevels = new Vector<>();
		this.beginnerLevels = new Vector<>();
		this.normalLevels = new Vector<>();
		this.hardLevels = new Vector<>();
		this.extremeLevels = new Vector<>();
	}
}
