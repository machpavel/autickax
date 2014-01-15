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
	
	//DISTANCE LIMITS
	/** Determines maximal total distance from pathway (in pixels). Beyond it is no pathway.*/
	public static final int MAX_DISTANCE_FROM_PATHWAY = 60;
	
	/** Determines maximal distance of proper surface  from pathway (in pixels)*/
	//public static final int MAX_SURFACE_DISTANCE_FROM_PATHWAY = 30;
	public static final int MAX_SURFACE_DISTANCE_FROM_PATHWAY_DEFAULT = 30;
	
	public static final int MAX_SURFACE_DISTANCE_FROM_PATHWAY_KIDDIE = 60;
	
	public static final int MAX_SURFACE_DISTANCE_FROM_PATHWAY_BEGINNER = 50;
	
	public static final int MAX_SURFACE_DISTANCE_FROM_PATHWAY_NORMAL = 40;
	
	public static final int MAX_SURFACE_DISTANCE_FROM_PATHWAY_HARD = 30;
	
	public static final int MAX_SURFACE_DISTANCE_FROM_PATHWAY_EXTREME = 20;
	
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
	public static final float SOUND_GAME_OBJECT_INTERACTION_DEFAULT_VOLUME = 0.5f;
	
	//OBJECTS WITH NO SOUND INTERACTION
	public static final String SOUND_NO_SOUND = "no_sound";
	//MUD
	public static final String SOUND_MUD = "mud";
	public static final String SOUND_MUD_PATH = "sfx/collisions/mud02.wav";
	//HOLE
	public static final String SOUND_HOLE = "hole";
	public static final String SOUND_HOLE_PATH = "sfx/collisions/vehicle_crash_small_hole.mp3";
	//STONE
	public static final String SOUND_STONE = "stone";
	public static final String SOUND_STONE_PATH = "sfx/collisions/vehicle_crash_large_stone.mp3";
	//TREE
	public static final String SOUND_TREE = "tree";
	public static final String SOUND_TREE_PATH = "sfx/collisions/vehicle_crash_large_tree.mp3";
	
	//CAR IGNITION SOUND
	public static final String SOUND_ENGINE_START = "engine_start";
	public static final String SOUND_ENGINE_START_PATH = "sfx/car-ignition-1.wav";
	public static final float SOUND_ENGINE_VOLUME = 1;
	
	//EDITOR SOUND
	public static final String SOUND_EDITOR = "editor";
	public static final String SOUND_EDITOR_PATH = "sfx/editorItemAdded.mp3";
	
	//MUSIC
	//MENU
	public static final String MUSIC_MENU_PATH = "sfx/music/exciting_race_tune.mp3";
	//RACE
	public static final String MUSIC_RACE_PATH = "sfx/music/sports_card.mp3";
	
	
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
	/** Determines dialog x shift. */ 
	public final static int DIALOG_WORLD_X_OFFSET = 100;
	/** Determines dialog y shift. */
	public final static int DIALOG_WORLD_Y_OFFSET = 60;
	
	
	//
	// DIALOG POSITIONING AND TEXTURES
	//
	
	public static final String DIALOG_BACKGROUND_TEXTURE = "DialogBackground";	
	
	
	public static final float DIALOG_MESSAGE_POSITION_X = 400;
	public static final float DIALOG_MESSAGE_POSITION_Y = 240;
	
	public static final String MESSAGE_DIALOG_BUTTON_OK_TEXTURE = "popOk";
	public static final String MESSAGE_DIALOG_BUTTON_OK_OVER_TEXTURE = "popOkOver";
	public static final float MESSAGE_DIALOG_BUTTON_OK_POSITION_X = 400;
	public static final float MESSAGE_DIALOG_BUTTON_OK_POSITION_Y = 120;
	
	public static final String DECISION_DIALOG_BUTTON_CONTINUE_TEXTURE = "popNext";
	public static final String DECISION_DIALOG_BUTTON_CONTINUE_OVER_TEXTURE = "popNextOver";
	public static final float DECISION_DIALOG_BUTTON_CONTINUE_POSITION_X = 600;
	public static final float DECISION_DIALOG_BUTTON_CONTINUE_POSITION_Y = 120;
	
	public static final String DECISION_DIALOG_BUTTON_RESTART_TEXTURE = "popRestart";
	public static final String DECISION_DIALOG_BUTTON_RESTART_OVER_TEXTURE = "popRestartOver";
	public static final float DECISION_DIALOG_BUTTON_RESTART_POSITION_X = 400;
	public static final float DECISION_DIALOG_BUTTON_RESTART_POSITION_Y = 120;
	
	public static final String DECISION_DIALOG_BUTTON_GO_TO_MAIN_MENU_TEXTURE = "popMenu";
	public static final String DECISION_DIALOG_BUTTON_GO_TO_MAIN_MENU_OVER_TEXTURE = "popMenuOver";
	public static final float DECISION_DIALOG_BUTTON_GO_TO_MAIN_MENU_POSITION_X = 200;
	public static final float DECISION_DIALOG_BUTTON_GO_TO_MAIN_MENU_POSITION_Y = 120;
	
	
	//
	// MINIGAMES
	//
	
	// Avoid obstacles
	public static final String AVOID_OBSTACLES_MINIGAME_BACKGROUND_TEXTURE = "AvoidObstaclesMinigameBackground";	
	public static final float AVOID_OBSTACLES_CAR_START_POSITION_X = DIALOG_WORLD_X_OFFSET + 30;
	public static final float AVOID_OBSTACLES_FINISH_START_POSITION_X = WORLD_WIDTH - DIALOG_WORLD_X_OFFSET - 20;
	public static final int AVOID_OBSTACLES_FINISH_TYPE = 1;
	public static final float AVOID_OBSTACLES_MINIMAL_DISTANCE_BETWEEN_OBSTACLES_KIDDIE = 150;
	public static final float AVOID_OBSTACLES_MINIMAL_DISTANCE_BETWEEN_OBSTACLES_BEGINNER = 120;
	public static final float AVOID_OBSTACLES_MINIMAL_DISTANCE_BETWEEN_OBSTACLES_NORMAL = 90;
	public static final float AVOID_OBSTACLES_MINIMAL_DISTANCE_BETWEEN_OBSTACLES_HARD = 60;
	public static final float AVOID_OBSTACLES_MINIMAL_DISTANCE_BETWEEN_OBSTACLES_EXTREME = 50;
	public static final float AVOID_OBSTACLES_MINIMAL_DISTANCE_BETWEEN_OBSTACLES_DEFAULT = 20;
	public static final float AVOID_OBSTACLES_MINIMAL_DISTANCE_BETWEEN_CAR = 100;
	public static final float AVOID_OBSTACLES_MINIMAL_DISTANCE_BETWEEN_FINISH = 100;
	public static final int AVOID_OBSTACLES_NUMBER_OF_TRIES_TO_GENERATE_OBSTACLE = 20;
	public static final float AVOID_HOLES_FAIL_VALUE = 0.5f;
	
	// Gear shift
	public static final String GEAR_SHIFT_MINIGAME_BACKGROUND_TEXTURE = "GearShiftMinigameBackground";
	
	//
	// STRINGS
	//
	public static final String TOOLTIP_PHASE_1_WHAT_TO_DO = "Follow the pathway\nwith your finger,\nhurry to finish\nand avoid nasty \nobjects!";
	public static final String TOOLTIP_MINIGAME_CRASHED_WHAT_TO_DO = "Your can't avoid trees";
	public static final String TOOLTIP_MINIGAME_AVOID_OBSTACLES_WHAT_TO_DO = "Avoid obstacles and\nreach finish";
	public static final String TOOLTIP_MINIGAME_GEAR_SHIFT_WHAT_TO_DO = "Move the gearshift lever\nto the right position";
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
	public static final String 	CAR_TYPE_1_POSITION_0_TEXTURE_NAME = "car0";
	public static final String 	CAR_TYPE_1_POSITION_1_TEXTURE_NAME = "car1";
	public static final String 	CAR_TYPE_1_POSITION_2_TEXTURE_NAME = "car2";
	public static final String 	CAR_TYPE_1_POSITION_3_TEXTURE_NAME = "car3";
	public static final String 	CAR_TYPE_1_POSITION_4_TEXTURE_NAME = "car4";
	public static final String 	CAR_TYPE_1_POSITION_5_TEXTURE_NAME = "car5";
	public static final String 	CAR_TYPE_1_POSITION_6_TEXTURE_NAME = "car6";
	public static final String 	CAR_TYPE_1_POSITION_7_TEXTURE_NAME = "car7";
	
	public static final int FINISH_TYPES_COUNT = 2;
	public static final String 	FINISH_TEXTURE_PREFIX = "finish";
	public static final int FINISH_BOUNDING_RADIUS = 50;
	
	public static final int MUD_TYPES_COUNT = 3;
	public static final String 	MUD_TEXTURE_PREFIX = "mud";
	
	public static final int START_TYPES_COUNT = 3;
	public static final String 	START_TEXTURE_PREFIX = "start";
	
	public static final int STONE_TYPES_COUNT = 5;
	public static final String 	STONE_TEXTURE_PREFIX = "stone";
	
	public static final int TREE_TYPES_COUNT = 5;
	public static final String 	TREE_TEXTURE_PREFIX = "tree";
	
	public static final int HOLE_TYPES_COUNT = 2;
	public static final String 	HOLE_TEXTURE_NAME_PREFIX = "hole";
	
	public static final int AVOID_STONE_TYPES_COUNT = 15;
	public static final String 	AVOID_STONE_TEXTURE_NAME_PREFIX = "avoidstone";
	
	public static final int AVOID_HOLES_TYPES_COUNT = 2;
	public static final String 	AVOID_HOLES_TEXTURE_NAME_PREFIX = "avoidhole";
}
