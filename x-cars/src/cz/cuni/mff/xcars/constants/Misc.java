package cz.cuni.mff.xcars.constants;

import com.badlogic.gdx.graphics.Color;

public final class Misc {

	//
	// PATHWAY
	//

	
	/** Helping arrow texture */
	public final String ARROW_NAME = "arrow";	
	
	/** Determines the color of a pathway */
	public final Color PATHWAY_COLOR = new Color(0.75f, 0.7f, 0.6f, 1f);
	/** How many pictures should be blended from the border of pathway */
	public final int PATHWAY_BORDER_BLEND_DISTANCE = 3;
	/**
	 * How big should be the circle in front of start and behind finish in the
	 * picture of pathway
	 */
	public final int PATHWAY_START_AND_FINISH_CIRCLE_RADIUS = 30;

	/** Determines where is located start on the curve in percents */
	public final float START_POSITION_IN_CURVE = 0;
	/** Determines where is located finish on the curve in percents */
	public final float FINISH_POSITION_IN_CURVE = 1;

	/** Determines how far should be the car ahead of the start line */
	public final float CAR_DISTANCE_FROM_START = 30;

	// DISTANCE LIMITS
	/**
	 * Determines maximal total distance from pathway (in pixels). Beyond it is
	 * no pathway.
	 */
	public final int MAX_DISTANCE_FROM_PATHWAY = 65;

	/**
	 * Determines the distance from user's finger that the car can be
	 * controlled.
	 */
	public final int SHIFTABLE_OBJECT_MAX_CAPABLE_DISTANCE = 60;

	/** Determines maximal distance of proper surface from pathway (in pixels) */
	// public final int MAX_SURFACE_DISTANCE_FROM_PATHWAY = 30;	

	public final int MAX_SURFACE_DISTANCE_FROM_PATHWAY_KIDDIE = 60;

	public final int MAX_SURFACE_DISTANCE_FROM_PATHWAY_BEGINNER = 50;

	public final int MAX_SURFACE_DISTANCE_FROM_PATHWAY_NORMAL = 40;

	public final int MAX_SURFACE_DISTANCE_FROM_PATHWAY_HARD = 30;

	public final int MAX_SURFACE_DISTANCE_FROM_PATHWAY_EXTREME = 20;
	
	/** Number of waypoints that check if the player raced through all the track */
	public final int WAYPOINTS_COUNT = 40;

	/**
	 * Amount of parts used between two points during the counting of distances
	 * in DistanceMap
	 */
	public final int LINE_SEGMENTATION = 100;

	//
	// SPEED REGULATION
	//

	/**
	 * The speed is effected by this constant when the car is out of proper
	 * surface of the pathway
	 */
	public final float OUT_OF_SURFACE_PENALIZATION_FACTOR = 1.8f;

	/**
	 * Global speed. It can be used for slowing or accelerating the phase two
	 * game.
	 */
	public final float GLOBAL_SPEED_REGULATOR = 0.75f;

	/** Pathway texture */
	public final String PATHWAY_TEXTURE_TYPE_1 = "pathway";
	public final String TEMPORARY_PATHWAY_TEXTURE_STORAGE_NAME = "pathwayTexture";

	// GOLDEN STARS
	// Defines the max multiply of time limit which a player must not cross in
	// order to gain the stars
	public final byte STARS_MAX = 3;
	public final float STARS_ONE_TIME_THRESHOLD = 1.5f;
	public final float STARS_TWO_TIME_THRESHOLD = 0.7f;
	public final float STARS_THREE_TIME_THRESHOLD = 0.3f;

	// SCORE CONSTANT
	public final int SCORE_MULTIPLIER = 1000000;
	

}
