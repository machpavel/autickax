package cz.mff.cuni.autickax;

public final class Constants {

	
	//
	// PATHWAY
	//
	
	/** Determines where is located start on the curve in percents*/
	public static final float START_POSITION_IN_CURVE = 0;
	/** Determines where is located finish on the curve in percents*/
	public static final float FINISH_POSITION_IN_CURVE = 1;
	
	/** Determines how far should be the car ahead of the start line*/
	public static final float CAR_DISTANCE_FROM_START = 30; 
	
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
	// Menu textures
	//
	
	public static final String 	BUTTON_MENU_BACKGROUND = "menuBackground";
	public static final String 	BUTTON_MENU_PLAY = "menuPlay";
	public static final String 	BUTTON_MENU_PLAY_HOVER = "menuPlayHover";
	public static final String 	BUTTON_MENU_EXIT = "menuExit";
	public static final String 	BUTTON_MENU_EXIT_HOVER = "menuExitHover";
	public static final String 	BUTTON_MENU_LEVEL = "menuLevel";
	public static final String 	BUTTON_MENU_LEVEL_HOVER = "menuLevelHover";
	public static final String 	BUTTON_MENU_DIFFICULTY = "menuDifficulty";
	public static final String 	BUTTON_MENU_DIFFICULTY_HOVER = "menuDifficultyHover";
	
	
	/** Pathway texture */
	public static final String 	PATHWAY_TEXTURE_TYPE_1 = "pathway";
	
	/** Background texture in levels*/
	public static final int LEVEL_BACKGROUND_TEXTURE_TYPES_COUNT = 4;
	public static final String 	LEVEL_BACKGROUND_TEXTURE_TYPE_1 = "levelBackground1";
	public static final String 	LEVEL_BACKGROUND_TEXTURE_TYPE_2 = "levelBackground2";
	public static final String 	LEVEL_BACKGROUND_TEXTURE_TYPE_3 = "levelBackground3";
	public static final String 	LEVEL_BACKGROUND_TEXTURE_TYPE_4 = "levelBackground4";
	public static final String 	LEVEL_BACKGROUND_TEXTURE_TYPE_1_SMALL = "levelBackground1small";
	public static final String 	LEVEL_BACKGROUND_TEXTURE_TYPE_2_SMALL = "levelBackground2small";
	public static final String 	LEVEL_BACKGROUND_TEXTURE_TYPE_3_SMALL = "levelBackground3small";
	public static final String 	LEVEL_BACKGROUND_TEXTURE_TYPE_4_SMALL = "levelBackground4small";
	
	
	
	
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
	/** Determines ideal dialog world width. */ 
	public final static int DIALOG_WORLD_WIDTH = 600;
	/** Determines ideal dialog world height. */
	public final static int DIALOG_WORLD_HEIGHT = 360; 
	
	
	//
	// DIALOG POSITIONING AND TEXTURES
	//
	
	public static final String DIALOG_BACKGROUND_TEXTURE = "DialogBackground";
	public static final float DIALOG_MESSAGE_POSITION_X = 400;
	public static final float DIALOG_MESSAGE_POSITION_Y = 240;
	
	public static final String MESSAGE_DIALOG_BUTTON_OK_TEXTURE = "stone";
	public static final float MESSAGE_DIALOG_BUTTON_OK_POSITION_X = 400;
	public static final float MESSAGE_DIALOG_BUTTON_OK_POSITION_Y = 120;
	
	public static final String DECISION_DIALOG_BUTTON_CONTINUE_TEXTURE = "stone";
	public static final float DECISION_DIALOG_BUTTON_CONTINUE_POSITION_X = 600;
	public static final float DECISION_DIALOG_BUTTON_CONTINUE_POSITION_Y = 120;
	
	public static final String DECISION_DIALOG_BUTTON_RESTART_TEXTURE = "stone";
	public static final float DECISION_DIALOG_BUTTON_RESTART_POSITION_X = 400;
	public static final float DECISION_DIALOG_BUTTON_RESTART_POSITION_Y = 120;
	
	public static final String DECISION_DIALOG_BUTTON_GO_TO_MAIN_MENU_TEXTURE = "stone";
	public static final float DECISION_DIALOG_BUTTON_GO_TO_MAIN_MENU_POSITION_X = 200;
	public static final float DECISION_DIALOG_BUTTON_GO_TO_MAIN_MENU_POSITION_Y = 120;
	
	//
	// STRINGS
	//
	public static final String TOOLTIP_PHASE_1_WHAT_TO_DO = "Follow the pathway\nwith your finger,\nhurry to finish\nand avoid nasty \nobjects!";
	public static final String PHASE_1_FINISH_REACHED = "Finish\nreached";
	public static final String PHASE_1_OUT_OF_LINE = "You have gone\nout of the\npathway.";
	public static final String PHASE_1_FINISH_NOT_REACHED = "You have not\nreached the\nfinish";
	public static final String PHASE_1_TIME_EXPIRED = "Time limit\nexceeded.";
	
	public static final String PHASE_2_MINIGAME_FAILED = "You failed.";
	public static final String PHASE_2_FINISH_REACHED = "Finish reached\nin time: ";
	
	//
	// GAME OBJECTS
	//
	public static final int CAR_TYPES_COUNT = 1;
	public static final int FINISH_TYPES_COUNT = 1;
	public static final int MUD_TYPES_COUNT = 3;
	public static final int START_TYPES_COUNT = 3;
	public static final int STONE_TYPES_COUNT = 5;
	public static final int TREE_TYPES_COUNT = 5;
	public static final int HOLE_TYPES_COUNT = 2;
	
	
	public static final String 	CAR_TYPE_1_POSITION_0_TEXTURE_NAME = "car0";
	public static final String 	CAR_TYPE_1_POSITION_1_TEXTURE_NAME = "car1";
	public static final String 	CAR_TYPE_1_POSITION_2_TEXTURE_NAME = "car2";
	public static final String 	CAR_TYPE_1_POSITION_3_TEXTURE_NAME = "car3";
	public static final String 	CAR_TYPE_1_POSITION_4_TEXTURE_NAME = "car4";
	public static final String 	CAR_TYPE_1_POSITION_5_TEXTURE_NAME = "car5";
	public static final String 	CAR_TYPE_1_POSITION_6_TEXTURE_NAME = "car6";
	public static final String 	CAR_TYPE_1_POSITION_7_TEXTURE_NAME = "car7";
	public static final int  	CAR_TYPE_1_WIDTH = 65;
	public static final int 	CAR_TYPE_1_HEIGHT = 35;
	
	public static final String 	FINISH_TYPE_1_TEXTURE_NAME = "finish1";
	public static final int  	FINISH_TYPE_1_WIDTH = 33;
	public static final int 	FINISH_TYPE_1_HEIGHT = 52;
	
	public static final String 	FINISH_TYPE_2_TEXTURE_NAME = "finish2";
	public static final int  	FINISH_TYPE_2_WIDTH = 60;
	public static final int 	FINISH_TYPE_2_HEIGHT = 16;
	
	public static final String 	MUD_TYPE_1_TEXTURE_NAME = "mud1";
	public static final int  	MUD_TYPE_1_WIDTH = 48;
	public static final int 	MUD_TYPE_1_HEIGHT = 34;
	
	public static final String 	MUD_TYPE_2_TEXTURE_NAME = "mud2";
	public static final int  	MUD_TYPE_2_WIDTH = 29;
	public static final int 	MUD_TYPE_2_HEIGHT = 27;
	
	public static final String 	MUD_TYPE_3_TEXTURE_NAME = "mud3";
	public static final int  	MUD_TYPE_3_WIDTH = 18;
	public static final int 	MUD_TYPE_3_HEIGHT = 18;
	
	public static final String 	START_TYPE_1_TEXTURE_NAME = "start1";
	public static final int  	START_TYPE_1_WIDTH = 72;
	public static final int 	START_TYPE_1_HEIGHT = 52;
	
	public static final String 	START_TYPE_2_TEXTURE_NAME = "start2";
	public static final int  	START_TYPE_2_WIDTH = 33;
	public static final int 	START_TYPE_2_HEIGHT = 52;
	
	public static final String 	START_TYPE_3_TEXTURE_NAME = "start3";
	public static final int  	START_TYPE_3_WIDTH = 33;
	public static final int 	START_TYPE_3_HEIGHT = 52;
	
	public static final String 	START_TYPE_4_TEXTURE_NAME = "start4";
	public static final int  	START_TYPE_4_WIDTH = 41;
	public static final int 	START_TYPE_4_HEIGHT = 24;
	
	public static final String 	STONE_TYPE_1_TEXTURE_NAME = "stone1";
	public static final int  	STONE_TYPE_1_WIDTH = 15;
	public static final int 	STONE_TYPE_1_HEIGHT = 14;
	
	public static final String 	STONE_TYPE_2_TEXTURE_NAME = "stone2";
	public static final int  	STONE_TYPE_2_WIDTH = 22;
	public static final int 	STONE_TYPE_2_HEIGHT = 21;
	
	public static final String 	STONE_TYPE_3_TEXTURE_NAME = "stone3";
	public static final int  	STONE_TYPE_3_WIDTH = 50;
	public static final int 	STONE_TYPE_3_HEIGHT = 43;
	
	public static final String 	STONE_TYPE_4_TEXTURE_NAME = "stone4";
	public static final int  	STONE_TYPE_4_WIDTH = 50;
	public static final int 	STONE_TYPE_4_HEIGHT = 43;
	
	public static final String 	STONE_TYPE_5_TEXTURE_NAME = "stone5";
	public static final int  	STONE_TYPE_5_WIDTH = 50;
	public static final int 	STONE_TYPE_5_HEIGHT = 32;
	
	public static final String 	TREE_TYPE_1_TEXTURE_NAME = "tree1";
	public static final int  	TREE_TYPE_1_WIDTH = 29;
	public static final int 	TREE_TYPE_1_HEIGHT = 42;
	
	public static final String 	TREE_TYPE_2_TEXTURE_NAME = "tree2";
	public static final int  	TREE_TYPE_2_WIDTH = 28;
	public static final int 	TREE_TYPE_2_HEIGHT = 42;
	
	public static final String 	TREE_TYPE_3_TEXTURE_NAME = "tree3";
	public static final int  	TREE_TYPE_3_WIDTH = 28;
	public static final int 	TREE_TYPE_3_HEIGHT = 42;
	
	public static final String 	TREE_TYPE_4_TEXTURE_NAME = "tree4";
	public static final int  	TREE_TYPE_4_WIDTH = 28;
	public static final int 	TREE_TYPE_4_HEIGHT = 42;
	
	public static final String 	TREE_TYPE_5_TEXTURE_NAME = "tree5";
	public static final int  	TREE_TYPE_5_WIDTH = 29;
	public static final int 	TREE_TYPE_5_HEIGHT = 42;
	
	public static final String 	HOLE_TYPE_1_TEXTURE_NAME = "hole1";
	public static final int  	HOLE_TYPE_1_WIDTH = 24;
	public static final int 	HOLE_TYPE_1_HEIGHT = 11;
	
	public static final String 	HOLE_TYPE_2_TEXTURE_NAME = "hole2";
	public static final int  	HOLE_TYPE_2_WIDTH = 13;
	public static final int 	HOLE_TYPE_2_HEIGHT = 9;
}
