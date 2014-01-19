package cz.mff.cuni.autickax;

import com.badlogic.gdx.graphics.Color;

public final class Constants {

	
	//
	// PATHWAY
	//
	
	/** Determines the color of a pathway*/
	public static final Color PATHWAY_COLOR = new Color( 0.75f, 0.7f, 0.6f, 0.9f );
	/** How many pictures shoud be blended from the border of pathway*/
	public static final float PATHWAY_BORDER_BLEND_DISTANCE = 2;
	/** How big should be the circle in front of start and behind finish in the picture of pathway*/ 
	public static final int PATHWAY_START_AND_FINISH_CIRCLE_RADIUS = 20;
	
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
	public static final float GLOBAL_SPEED_REGULATOR = 0.75f;
	
	
	
	//
	// Menu textures
	//
	
	public static final String 	MAIN_MENU_BACKGROUND = "mainMenuBackground";
	public static final String 	DIFFICULTY_MENU_BACKGROUND = "difficultyMenuBackground";
	public static final String 	LOADING_LEVEL_MENU_BACKGROUND = "menuLoadingLevelBackground";
	
	public static final String 	LOADING_SCREEN_BACKGROUND = "loadingScreenBackground";
	public static final String 	LOADING_SCREEN_CAR = "loadingScreenCar";
	public static final String 	LOADING_SCREEN_FUME = "loadingScreenFume";
	
	public static final float 	LEVELS_MENU_RED = (float)169 / 255;
	public static final float 	LEVELS_MENU_GREEN = (float)207 / 255;
	public static final float 	LEVELS_MENU_BLUE = (float)56 / 255;
	
	public static final String 	BUTTON_MENU_PLAY = "menuStart";
	public static final String 	BUTTON_MENU_PLAY_HOVER = "menuStartHover";
	
	public static final String 	BUTTON_MENU_EXIT = "menuExit";
	public static final String 	BUTTON_MENU_EXIT_HOVER = "menuExitHover";
	
	public static final String 	BUTTON_MENU_LEVEL_DISABLED = "menuLevelDisabled";
	public static final String 	BUTTON_MENU_LEVEL_NO_STAR = "menuLevelNoStar";
	public static final String 	BUTTON_MENU_LEVEL_NO_STAR_HOVER = "menuLevelNoStarHover";
	public static final String 	BUTTON_MENU_LEVEL_ONE_STAR = "menuLevelOneStar";
	public static final String 	BUTTON_MENU_LEVEL_ONE_STAR_HOVER = "menuLevelOneStarHover";
	public static final String 	BUTTON_MENU_LEVEL_TWO_STARS = "menuLevelTwoStars";
	public static final String 	BUTTON_MENU_LEVEL_TWO_STARS_HOVER = "menuLevelTwoStarsHover";
	public static final String 	BUTTON_MENU_LEVEL_THREE_STARS = "menuLevelThreeStars";
	public static final String 	BUTTON_MENU_LEVEL_THREE_STARS_HOVER = "menuLevelThreeStarsHover";
	
	public static final String 	BUTTON_MENU_DIFFICULTY = "menuDifficulty";
	public static final String 	BUTTON_MENU_DIFFICULTY_HOVER = "menuDifficultyHover";
	
	public static final String 	BUTTON_MENU_TOOLTIPS_ON = "menuTipsOn";
	public static final String 	BUTTON_MENU_TOOLTIPS_ON_HOVER = "menuTipsOnHover";
	public static final String 	BUTTON_MENU_TOOLTIPS_OFF = "menuTipsOff";
	public static final String 	BUTTON_MENU_TOOLTIPS_OFF_HOVER = "menuTipsOffHover";
	
	public static final String 	BUTTON_MENU_SOUNDS_ON = "menuSoundsOn";
	public static final String 	BUTTON_MENU_SOUNDS_ON_HOVER = "menuSoundsOnHover";
	public static final String 	BUTTON_MENU_SOUNDS_OFF = "menuSoundsOff";
	public static final String 	BUTTON_MENU_SOUNDS_OFF_HOVER = "menuSoundsOffHover";
	
	public static final String 	BUTTON_MENU_MUSIC_ON = "menuMusicOn";
	public static final String 	BUTTON_MENU_MUSIC_ON_HOVER = "menuMusicOnHover";
	public static final String 	BUTTON_MENU_MUSIC_OFF = "menuMusicOff";
	public static final String 	BUTTON_MENU_MUSIC_OFF_HOVER = "menuMusicOffHover";
	
	
	/** Pathway texture */
	public static final String 	PATHWAY_TEXTURE_TYPE_1 = "pathway";
	public static final String 	TEMPORARY_PATHWAY_TEXTURE_STORAGE_NAME = "pathwayTexture";
	
	/** Background texture in levels*/
	public static final int LEVEL_BACKGROUND_TEXTURE_TYPES_COUNT = 21;
	public static final String 	LEVEL_BACKGROUND_TEXTURE_PREFIX = "textury";
	public static final String 	LEVEL_SMALL_BACKGROUND_TEXTURE_POSTFIX = "small";

	
	//
	// SERIALIZATION
	//
	public static final String PREFERENCES_FILENAME = "cz.cuni.mff.autickax.preferences";
	public static final String PLAYED_LEVELS_FILENAME = "cz.cuni.mff.autickax.playedLevels";
	public static final String PLAYED_LEVELS_KIDDIE_NAME = "kiddieLevels";
	public static final String PLAYED_LEVELS_BEGINNER_NAME = "beginnerLevels";
	public static final String PLAYED_LEVELS_NORMAL_NAME = "normalLevels";
	public static final String PLAYED_LEVELS_HARD_NAME = "hardLevels";
	public static final String PLAYED_LEVELS_EXTREME_NAME = "extremeLevels";
	
	
	
	//
	// SOUNDS & MUSIC
	//
	public static final float SOUND_DEFAULT_VOLUME = 0.5f;
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
	

	
	//EDITOR SOUND
	public static final String SOUND_EDITOR = "editor";
	public static final String SOUND_EDITOR_PATH = "sfx/editorItemAdded.mp3";
	
	//MUSIC MENU
	public static final String MUSIC_MENU_PATH = "sfx/music/exciting_race_tune.mp3";
	
	//SOUNDS MENU
	public static final String SOUND_MENU_OPEN_PATH = "sfx/menu/car_door_open.mp3";
	public static final String SOUND_MENU_OPEN = "menuOpen";
	
	public static final String SOUND_MENU_CLOSE_PATH = "sfx/menu/car_door_close_6.mp3";
	public static final String SOUND_MENU_CLOSE = "menuClose";
	
	//MUSIC RACE
	public static final String MUSIC_RACE_PATH = "sfx/music/sports_card.mp3";
	
	
	//SOUNDS MINIGAME
	public static final String SOUND_MINIGAME_SUCCESS_PATH = "sfx/minigame/small_crowd_says_yes.mp3";
	public static final String SOUND_MINIGAME_SUCCESS = "minisucc";
	
	public static final String SOUND_MINIGAME_FAIL_PATH = "sfx/minigame/wizard_says_no.mp3";
	public static final String SOUND_MINIGAME_FAIL = "minifail";
	
	//SOUNDS SUBLEVELS
	public static final String SOUND_ENGINE_START = "engine_start";
	public static final String SOUND_ENGINE_START_PATH = "sfx/sublevels/car-ignition-1.mp3";
	public static final float SOUND_ENGINE_VOLUME = 1;
	
	
	
	public static final String SOUND_SUB1_CHEER = "cheer_sub1_small";
	public static final String SOUND_SUB1_CHEER_PATH = "sfx/sublevels/smallCheer.mp3";
	
	public static final String SOUND_SUB2_CHEER = "cheer_sub1_big";
	public static final String SOUND_SUB2_CHEER_PATH = "sfx/sublevels/bigCheer.mp3";
	public static final float SOUND_BIG_CHEER_VOLUME = 0.8f;
	
	public static final String SOUND_SUB1_FAIL = "cheer_sub1_fail";
	public static final String SOUND_SUB1_FAIL_PATH = "sfx/sublevels/sports_arena_oh.mp3";
	
	public static final String SOUND_SUB2_START = "sub2_start";
	public static final String SOUND_SUB2_START_PATH = "sfx/sublevels/auto_car_rev_up_squeal_away.mp3";
	public static final float SOUNDS_ENGINE_DELAY = 1.8f;
	
	public static final String SOUND_BUTTON_DIALOG_SOUND = SOUND_MENU_CLOSE;
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
	public static final float DIALOG_MESSAGE_POSITION_Y = 270;
	
	public static final String MESSAGE_DIALOG_BUTTON_OK_TEXTURE = "popOk";
	public static final String MESSAGE_DIALOG_BUTTON_OK_OVER_TEXTURE = "popOkOver";
	public static final float MESSAGE_DIALOG_BUTTON_OK_POSITION_X = 400;
	public static final float MESSAGE_DIALOG_BUTTON_OK_POSITION_Y = 110;
	
	public static final String DECISION_DIALOG_BUTTON_CONTINUE_TEXTURE = "popNext";
	public static final String DECISION_DIALOG_BUTTON_CONTINUE_OVER_TEXTURE = "popNextOver";
	public static final float DECISION_DIALOG_BUTTON_CONTINUE_POSITION_X = 600;
	public static final float DECISION_DIALOG_BUTTON_CONTINUE_POSITION_Y = 110;
	
	public static final String DECISION_DIALOG_BUTTON_RESTART_TEXTURE = "popRestart";
	public static final String DECISION_DIALOG_BUTTON_RESTART_OVER_TEXTURE = "popRestartOver";
	public static final float DECISION_DIALOG_BUTTON_RESTART_POSITION_X = 400;
	public static final float DECISION_DIALOG_BUTTON_RESTART_POSITION_Y = 110;
	
	public static final String DECISION_DIALOG_BUTTON_GO_TO_MAIN_MENU_TEXTURE = "popMenu";
	public static final String DECISION_DIALOG_BUTTON_GO_TO_MAIN_MENU_OVER_TEXTURE = "popMenuOver";
	public static final float DECISION_DIALOG_BUTTON_GO_TO_MAIN_MENU_POSITION_X = 200;
	public static final float DECISION_DIALOG_BUTTON_GO_TO_MAIN_MENU_POSITION_Y = 110;
	
	public static final float COMPLETE_DIALOG_MESSAGE_POSITION_X = 360;
	public static final float COMPLETE_DIALOG_MESSAGE_POSITION_Y = 270;
	public static final String COMPLETE_DIALOG_STAR_FILLED_TEXTURE = "endOfLevelFullStar";
	public static final String COMPLETE_DIALOG_STAR_EMPTY_TEXTURE = "endOfLevelEmptyStar";
	public static final int COMPLETE_DIALOG_STAR_POSITION_X = 600;
	public static final int COMPLETE_DIALOG_STAR_POSITION_Y = 155;
	
	
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
	public static final float AVOID_OBSTACLES_FAIL_VALUE = 0.6f;
	
	// Gear shift
	public static final String GEAR_SHIFT_MINIGAME_BACKGROUND_TEXTURE = "GearShiftMinigameBackground";
	public static final String GEAR_SHIFTER_TEXTURE = "gearShifter";
	public static final float GEAR_SHIFT_FAIL_VALUE = 0.7f;
	public static final String GEAR_SHIFT_MINIGAME_FINISH_TEXTURE = "gearShiftMinigameFinish";
	public static final float GEAR_SHIFT_MINIGAME_FINISH_RADIUS = 30;
	public static final float GEAR_SHIFT_MINIGAME_ROW_1 = 180;
	public static final float GEAR_SHIFT_MINIGAME_ROW_2 = 245;
	public static final float GEAR_SHIFT_MINIGAME_ROW_3 = 310;
	public static final float GEAR_SHIFT_MINIGAME_COLUMN_1 = 320;
	public static final float GEAR_SHIFT_MINIGAME_COLUMN_2 = 400;
	public static final float GEAR_SHIFT_MINIGAME_COLUMN_3 = 480;	
	public static final float GEAR_SHIFT_MINIGAME_MAX_DISTANCE_FROM_LINE_KIDDIE = 40;
	public static final float GEAR_SHIFT_MINIGAME_MAX_DISTANCE_FROM_LINE_BEGINNER = 30;
	public static final float GEAR_SHIFT_MINIGAME_MAX_DISTANCE_FROM_LINE_NORMAL = 20;
	public static final float GEAR_SHIFT_MINIGAME_MAX_DISTANCE_FROM_LINE_HARD = 10;
	public static final float GEAR_SHIFT_MINIGAME_MAX_DISTANCE_FROM_LINE_EXTREME = 5;
	
	//
	// STRINGS
	//
	public static final String TOOLTIP_PHASE_1_WHAT_TO_DO = "Follow the pathway\n  with your finger,\n    hurry to finish\n   and avoid nasty \n          objects!";
	public static final String TOOLTIP_MINIGAME_CRASHED_WHAT_TO_DO = "Your can't avoid trees";
	public static final String TOOLTIP_MINIGAME_AVOID_OBSTACLES_WHAT_TO_DO = "Avoid obstacles and\n      reach finish";
	public static final String TOOLTIP_MINIGAME_GEAR_SHIFT_WHAT_TO_DO = "Move the gearshift lever\n   to the right position";
	public static final String TOOLTIP_MINIGAME_AVOID_HOLES_FAIL = "  You felt into a hole.\nYou will be punised by\n     speed reduction";
	public static final String TOOLTIP_MINIGAME_AVOID_STONES_FAIL = "You crashed into a stone.\n           Level failed.";
	public static final String TOOLTIP_MINIGAME_GEAR_SHIFT_FAIL = "You were not enough\naccurate. You will be\n punished by speed\n        reduction";
	public static final String PHASE_1_FINISH_REACHED = "Finish reached";
	public static final String PHASE_1_FINISH_REACHED_BUT_NOT_CHECKPOINTS = "You have to drive\n     along whole\n    the pathway.";
	public static final String PHASE_1_OUT_OF_LINE = "  You went out\nof the pathway.";
	public static final String PHASE_1_FINISH_NOT_REACHED = "You have not\n reached the\n      finish";
	public static final String PHASE_1_TIME_EXPIRED = "Time limit exceeded.";
	
	public static final String PHASE_2_MINIGAME_FAILED = "You failed.";
	
	public static final String GAME_PAUSED = "Game paused"; 
	
	
	
	//
	// GAME OBJECTS
	//
	
	public static final float CAR_MINIMAL_DISTANCE_TO_SHOW_ROTATION = 30;
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
	public static final float FINISH_BOUNDING_RADIUS = 30;
	
	public static final int MUD_TYPES_COUNT = 18;
	public static final String 	MUD_TEXTURE_PREFIX = "mud";
	
	public static final int START_TYPES_COUNT = 4;
	public static final String 	START_TEXTURE_PREFIX = "start";
	
	public static final int STONE_TYPES_COUNT = 6;
	public static final String 	STONE_TEXTURE_PREFIX = "stone";
	
	public static final int TREE_TYPES_COUNT = 23;
	public static final String 	TREE_TEXTURE_PREFIX = "tree";
	
	public static final int HOLE_TYPES_COUNT = 3;
	public static final String 	HOLE_TEXTURE_NAME_PREFIX = "hole";
	
	public static final int AVOID_STONE_TYPES_COUNT = 15;
	public static final String 	AVOID_STONE_TEXTURE_NAME_PREFIX = "avoidstone";
	
	
	public static final int AVOID_HOLES_TYPES_COUNT = 18;
	public static final String 	AVOID_HOLES_TEXTURE_NAME_PREFIX = "avoidhole";
	
	//GOLDEN STARS
	//Defines the max multiply of time limit which a player must not cross in order to gain the stars
	public static final byte STARS_MAX = 3;
	public static final float STARS_ONE_TIME_THRESHOLD = 1.5f;
	public static final float STARS_TWO_TIME_THRESHOLD = 0.7f;
	public static final float STARS_THREE_TIME_THRESHOLD = 0.3f;
	
	//SCORE CONSTANT
	public static final int SCORE_MULTIPLIER = 1000000;
	
}
