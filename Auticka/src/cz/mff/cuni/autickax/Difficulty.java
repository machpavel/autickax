package cz.mff.cuni.autickax;

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
			distSurface = Constants.MAX_SURFACE_DISTANCE_FROM_PATHWAY_KIDDIE;
			break;
		case Beginner:
			distSurface = Constants.MAX_SURFACE_DISTANCE_FROM_PATHWAY_BEGINNER;
			break;
		case Normal:
			distSurface = Constants.MAX_SURFACE_DISTANCE_FROM_PATHWAY_NORMAL;
			break;
		case Hard:
			distSurface = Constants.MAX_SURFACE_DISTANCE_FROM_PATHWAY_HARD;
			break;
		case Extreme:
			distSurface = Constants.MAX_SURFACE_DISTANCE_FROM_PATHWAY_EXTREME;
			break;
		default:
			distSurface = Constants.MAX_SURFACE_DISTANCE_FROM_PATHWAY_DEFAULT;
		}
		return distSurface;
	}
}
