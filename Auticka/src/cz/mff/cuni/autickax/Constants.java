package cz.mff.cuni.autickax;

public final class Constants {

	
	//
	// PATHWAY
	//
	
	/** Determines where is located start on the curve in percents*/
	public static final float START_POSITION_IN_CURVE = 0;
	/** Determines where is located finish on the curve in percents*/
	public static final float FINISH_POSITION_IN_CURVE = 1;
	
	/** Determines maximal total distance from pathway (in pixels). Beyond it is no pathway.*/
	public static final int MAX_DISTANCE_FROM_PATHWAY = 60;
	
	/** Determines maximal distance of proper surface  from pathway (in pixels)*/
	public static final int MAX_SURFACE_DISTANCE_FROM_PATHWAY = 30;
	
	/** Number of waypoints that check if the player raced through all the track */
	public static final int WAYPOINTS_COUNT = 40;
	
	/** Amount of parts used between two points during the counting of distances in DistanceMap*/
	public static final int LINE_SEGMENTATION = 100; 
	
	
	
	//
	// SPEED REGULATION
	//
	
	/** The speed is effected by this constant when the car is out of proper surface of the pathway */
	public static final float OUT_OF_SURFACE_PENALIZATION_FACTOR = 1.8f;
	
	/** Global speed. It can be used for slowing or accelerating the phase two game.*/ 
	public static final float GLOBAL_SPEED_REGULATOR = 0.5f;
	
	
	
	//
	// DRAWING PLACEMENTS
	//
	
	//TODO: dont use this
	public static final String 	BUTTON_PLAY = "play";
	
	/** Background pathway texture */
	public static final String 	PATHWAY_TEXTURE_TYPE_1 = "pathway";
	
	
	
	//
	// SOUNDS & MUSIC
	//
	
	//TODO: dont use this
	public static final String 	SOUND_JUMP = "jump";
	//TODO: dont use this
	public static final String 	SOUND_HIT = "hit";	
	
	/** Default value of volume */
	public static final float 	MUSIC_DEFAULT_VOLUME = 0.5f;
	
	
	
	//
	// INPUT & COORDINATES
	//
	/** Determines ideal world width. If the resolution is different, coordinates are stretched. */ 
	public final static int WORLD_WIDTH = 800;
	/** Determines ideal world height. If the resolution is different, coordinates are stretched. */
	public final static int WORLD_HEIGHT = 480; 

	

	//
	// GAME OBJECTS
	//
	public static final String 	CAR_TYPE_0_POSITION_0_TEXTURE_NAME = "car0";
	public static final String 	CAR_TYPE_0_POSITION_1_TEXTURE_NAME = "car0";
	public static final String 	CAR_TYPE_0_POSITION_2_TEXTURE_NAME = "car1";
	public static final String 	CAR_TYPE_0_POSITION_3_TEXTURE_NAME = "car2";
	public static final String 	CAR_TYPE_0_POSITION_4_TEXTURE_NAME = "car2";
	public static final String 	CAR_TYPE_0_POSITION_5_TEXTURE_NAME = "car2";
	public static final String 	CAR_TYPE_0_POSITION_6_TEXTURE_NAME = "car1";
	public static final String 	CAR_TYPE_0_POSITION_7_TEXTURE_NAME = "car0";
	public static final int  	CAR_TYPE_0_WIDTH = 62;
	public static final int 	CAR_TYPE_0_HEIGHT = 39;
	
	public static final String 	FINISH_TYPE_1_TEXTURE_NAME = "finish";
	public static final int  	FINISH_TYPE_1_WIDTH = 91;
	public static final int 	FINISH_TYPE_1_HEIGHT = 91;
	
	public static final String 	MUD_TYPE_1_TEXTURE_NAME = "mud";
	public static final int  	MUD_TYPE_1_WIDTH = 30;
	public static final int 	MUD_TYPE_1_HEIGHT = 30;
	
	public static final String 	START_TYPE_1_TEXTURE_NAME = "start";
	public static final int  	START_TYPE_1_WIDTH = 91;
	public static final int 	START_TYPE_1_HEIGHT = 91;
	
	public static final String 	STONE_TYPE_1_TEXTURE_NAME = "stone";
	public static final int  	STONE_TYPE_1_WIDTH = 30;
	public static final int 	STONE_TYPE_1_HEIGHT = 30;
	
	public static final String 	TREE_TYPE_1_TEXTURE_NAME = "tree";
	public static final int  	TREE_TYPE_1_WIDTH = 30;
	public static final int 	TREE_TYPE_1_HEIGHT = 30;
}
