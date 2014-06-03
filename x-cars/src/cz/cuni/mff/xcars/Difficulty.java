package cz.cuni.mff.xcars;

import java.util.Vector;

import cz.cuni.mff.xcars.constants.Constants;
import cz.cuni.mff.xcars.exceptions.IllegalDifficultyException;

/** Difficulty of the level played. */
public enum Difficulty {
	Kiddie,
	Beginner,
	Normal,
	Hard,
	Extreme;
	
	
	public int getMaxDistanceFromSurface()
	{
		int distSurface;
		switch(this)
		{
		case Kiddie: 
			distSurface = Constants.misc.MAX_SURFACE_DISTANCE_FROM_PATHWAY_KIDDIE;
			break;
		case Beginner:
			distSurface = Constants.misc.MAX_SURFACE_DISTANCE_FROM_PATHWAY_BEGINNER;
			break;
		case Normal:
			distSurface = Constants.misc.MAX_SURFACE_DISTANCE_FROM_PATHWAY_NORMAL;
			break;
		case Hard:
			distSurface = Constants.misc.MAX_SURFACE_DISTANCE_FROM_PATHWAY_HARD;
			break;
		case Extreme:
			distSurface = Constants.misc.MAX_SURFACE_DISTANCE_FROM_PATHWAY_EXTREME;
			break;
		default:
			throw new IllegalDifficultyException(this.toString());
		}
		return distSurface;
	}
	

	public Vector<Level> getAvailableLevels() {
		switch(this)
		{
		case Kiddie: 
			return Xcars.getInstance().assets.getAvailableLevels().kiddieLevels;
		case Beginner:
			return Xcars.getInstance().assets.getAvailableLevels().beginnerLevels;
		case Normal:
			return Xcars.getInstance().assets.getAvailableLevels().normalLevels;
		case Hard:
			return Xcars.getInstance().assets.getAvailableLevels().hardLevels;
		case Extreme:
			return Xcars.getInstance().assets.getAvailableLevels().extremeLevels;
		default:
			throw new IllegalDifficultyException(this.toString());
		}
	}
	

	public Vector<PlayedLevel> getPlayedLevels() {
		switch(this)
		{
		case Kiddie: 
			return Xcars.playedLevels.kiddieLevels;
		case Beginner:
			return Xcars.playedLevels.beginnerLevels;
		case Normal:
			return Xcars.playedLevels.normalLevels;
		case Hard:
			return Xcars.playedLevels.hardLevels;
		case Extreme:
			return Xcars.playedLevels.extremeLevels;
		default:
			throw new IllegalDifficultyException(this.toString());
		}
	}
}
