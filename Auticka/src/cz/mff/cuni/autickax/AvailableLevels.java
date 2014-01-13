package cz.mff.cuni.autickax;

import java.util.Vector;

public class AvailableLevels implements java.io.Serializable {
	
	private static final long serialVersionUID = 1L;
	
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
}
