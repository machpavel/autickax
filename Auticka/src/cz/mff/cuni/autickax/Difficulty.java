package cz.mff.cuni.autickax;

import java.util.Vector;

import cz.mff.cuni.autickax.constants.Constants;

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
			distSurface = Constants.misc.MAX_SURFACE_DISTANCE_FROM_PATHWAY_DEFAULT;
		}
		return distSurface;
	}
	

	public Vector<Level> getAvailableLevels() {
		switch(this)
		{
		case Kiddie: 
			return Autickax.getInstance().assets.getAvailableLevels().kiddieLevels;
		case Beginner:
			return Autickax.getInstance().assets.getAvailableLevels().beginnerLevels;
		case Normal:
			return Autickax.getInstance().assets.getAvailableLevels().normalLevels;
		case Hard:
			return Autickax.getInstance().assets.getAvailableLevels().hardLevels;
		case Extreme:
			return Autickax.getInstance().assets.getAvailableLevels().extremeLevels;
		default:
			assert true;
			return null;
		}
	}
	

	public Vector<PlayedLevel> getPlayedLevels() {
		switch(this)
		{
		case Kiddie: 
			return Autickax.playedLevels.kiddieLevels;
		case Beginner:
			return Autickax.playedLevels.beginnerLevels;
		case Normal:
			return Autickax.playedLevels.normalLevels;
		case Hard:
			return Autickax.playedLevels.hardLevels;
		case Extreme:
			return Autickax.playedLevels.extremeLevels;
		default:
			assert true;
			return null;
		}
	}
}
