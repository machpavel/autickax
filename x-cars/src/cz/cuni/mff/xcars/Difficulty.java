package cz.cuni.mff.xcars;

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
}
