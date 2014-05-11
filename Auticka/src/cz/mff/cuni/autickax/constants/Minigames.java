package cz.mff.cuni.autickax.constants;

public final class Minigames {

	//
	// MINIGAMES
	//
	
	public final float LEAVING_DELAY = 0.5f; // Delay in the end of game if there is no message
	public final float RESULT_VALUE_NOTHING = 1.f;

	// Avoid obstacles
	public final String AVOID_OBSTACLES_MINIGAME_BACKGROUND_TEXTURE = "AvoidObstaclesMinigameBackground";
	public final float AVOID_OBSTACLES_CAR_START_POSITION_X = Constants.dialog.DIALOG_WORLD_X_OFFSET + 30;
	public final float AVOID_OBSTACLES_FINISH_START_POSITION_X = Constants.WORLD_WIDTH
			- Constants.dialog.DIALOG_WORLD_X_OFFSET - 20;
	public final int AVOID_OBSTACLES_FINISH_TYPE = 1;
	public final float AVOID_OBSTACLES_MINIMAL_DISTANCE_BETWEEN_OBSTACLES_KIDDIE = 150;
	public final float AVOID_OBSTACLES_MINIMAL_DISTANCE_BETWEEN_OBSTACLES_BEGINNER = 120;
	public final float AVOID_OBSTACLES_MINIMAL_DISTANCE_BETWEEN_OBSTACLES_NORMAL = 90;
	public final float AVOID_OBSTACLES_MINIMAL_DISTANCE_BETWEEN_OBSTACLES_HARD = 60;
	public final float AVOID_OBSTACLES_MINIMAL_DISTANCE_BETWEEN_OBSTACLES_EXTREME = 50;
	public final float AVOID_OBSTACLES_MINIMAL_DISTANCE_BETWEEN_OBSTACLES_DEFAULT = 20;
	public final float AVOID_OBSTACLES_MINIMAL_DISTANCE_BETWEEN_CAR = 100;
	public final float AVOID_OBSTACLES_MINIMAL_DISTANCE_BETWEEN_FINISH = 100;
	public final int AVOID_OBSTACLES_NUMBER_OF_TRIES_TO_GENERATE_OBSTACLE = 20;
	public final float AVOID_OBSTACLES_FAIL_VALUE = 0.6f;

	// Gear shift
	public final String GEAR_SHIFT_MINIGAME_BACKGROUND_TEXTURE = "GearShiftMinigameBackground";
	public final String GEAR_SHIFTER_TEXTURE = "gearShifter";	
	public final String GEAR_SHIFT_MINIGAME_FINISH_TEXTURE = "gearShiftMinigameFinish";
	public final float GEAR_SHIFT_MINIGAME_FINISH_RADIUS = 50;
	public final float GEAR_SHIFT_MINIGAME_ROW_1 = 180;
	public final float GEAR_SHIFT_MINIGAME_ROW_2 = 245;
	public final float GEAR_SHIFT_MINIGAME_ROW_3 = 310;
	public final float GEAR_SHIFT_MINIGAME_COLUMN_1 = 320;
	public final float GEAR_SHIFT_MINIGAME_COLUMN_2 = 400;
	public final float GEAR_SHIFT_MINIGAME_COLUMN_3 = 480;
	public final float GEAR_SHIFT_MINIGAME_MAX_DISTANCE_FROM_LINE_KIDDIE = 50;
	public final float GEAR_SHIFT_MINIGAME_MAX_DISTANCE_FROM_LINE_BEGINNER = 40;
	public final float GEAR_SHIFT_MINIGAME_MAX_DISTANCE_FROM_LINE_NORMAL = 30;
	public final float GEAR_SHIFT_MINIGAME_MAX_DISTANCE_FROM_LINE_HARD = 20;
	public final float GEAR_SHIFT_MINIGAME_MAX_DISTANCE_FROM_LINE_EXTREME = 15;
	public final float GEAR_SHIFT_FAIL_VALUE = 0.7f;

	// Boost
	public final String BOOST_MINIGAME_BACKGROUND_TEXTURE = "BoostMinigameBackground";
	public final String BOOST_MINIGAME_WAITING_BUTTON_TEXTURE = "BoostMinigameWaitingButton";
	public final String BOOST_MINIGAME_HIGHLIGHTED_BUTTON_TEXTURE = "BoostMinigameHighlightButton";
	public final float BOOST_MINIGAME_BUTTONS_DISTANCE = 60;
	public final int BOOST_MINIGAME_ROWS_KIDDIE = 1;
	public final int BOOST_MINIGAME_ROWS_BEGINNER = 2;
	public final int BOOST_MINIGAME_ROWS_NORMAL = 2;
	public final int BOOST_MINIGAME_ROWS_HARD = 3;
	public final int BOOST_MINIGAME_ROWS_EXTREME = 3;
	public final int BOOST_MINIGAME_COLUMNS_KIDDIE = 1;
	public final int BOOST_MINIGAME_COLUMNS_BEGINNER = 2;
	public final int BOOST_MINIGAME_COLUMNS_NORMAL = 4;
	public final int BOOST_MINIGAME_COLUMNS_HARD = 4;
	public final int BOOST_MINIGAME_COLUMNS_EXTREME = 5;
	public final float BOOST_MINIGAME_TIME_LIMIT_KIDDIE = 6;
	public final float BOOST_MINIGAME_TIME_LIMIT_BEGINNER = 4;
	public final float BOOST_MINIGAME_TIME_LIMIT_NORMAL = 2;
	public final float BOOST_MINIGAME_TIME_LIMIT_HARD = 1;
	public final float BOOST_MINIGAME_TIME_LIMIT_EXTREME = 0.5f;
	public final int BOOST_MINIGAME_HITS_LIMIT_KIDDIE = 2;
	public final int BOOST_MINIGAME_HITS_LIMIT_BEGINNER = 3;
	public final int BOOST_MINIGAME_HITS_LIMIT_NORMAL = 5;
	public final int BOOST_MINIGAME_HITS_LIMIT_HARD = 10;
	public final int BOOST_MINIGAME_HITS_LIMIT_EXTREME = 15;	
	public final float BOOST_MINIGAME_TIME_GENERATION_LIMIT = 0.5f;
	public final float BOOST_MINIGAME_WIN_VALUE = 2;

	// Switching
	public final String SWITCHING_MINIGAME_BACKGROUND_TEXTURE = "SwitchingMinigameBackground";
	public final String SWITCHING_MINIGAME_BUTTON_TEXTURE = "SwitchingMinigameButton";
	public final String SWITCHING_MINIGAME_DISABLED_BUTTON_TEXTURE = "SwitchingMinigameButtonDisabled";
	public final String SWITCHING_MINIGAME_SLIDER_BACKGROUND_TEXTURE = "SwitchingSliderBackground";
	public final String SWITCHING_MINIGAME_SLIDER_KNOB_TEXTURE = "SwitchingSliderKnob";
	public final float SWITCHING_MINIGAME_OPPONENT_SPEED_KIDDIE = 1;
	public final float SWITCHING_MINIGAME_OPPONENT_SPEED_BEGINNER = 5;
	public final float SWITCHING_MINIGAME_OPPONENT_SPEED_NORMAL = 10;
	public final float SWITCHING_MINIGAME_OPPONENT_SPEED_HARD = 20;
	public final float SWITCHING_MINIGAME_OPPONENT_SPEED_EXTREME = 30;
	public final float SWITCHING_MINIGAME_WIN_VALUE = 1.5f;
	public final float SWITCHING_MINIGAME_FAIL_VALUE = 0.8f;

	// Anglicak
	public final String ANGLICAK_MINIGAME_BACKGROUND_TEXTURE = "AnglicakMinigameBackground";
	public final String ANGLICAK_MINIGAME_TARGET_TEXTURE = "AnglicakMinigameTarget";
	public final float ANGLICAK_MINIGAME_CAR_START_POSITION_X = Constants.dialog.DIALOG_WORLD_X_OFFSET + 30;
	public final int ANGLICAK_MINIGAME_TARGET_POSITION_X = 550;
	public final int ANGLICAK_MINIGAME_TARGET_POSITION_Y = 240; 
	public final float ANGLICAK_MINIGAME_CONTROL_LINE_POSITION_X = 240;
	public final float ANGLICAK_MINIGAME_FINISH_START_POSITION_X = Constants.WORLD_WIDTH - Constants.dialog.DIALOG_WORLD_X_OFFSET - 20;
	public final float ANGLICAK_MINIGAME_TARGET_RADIUS_KIDDIE = 150;
	public final float ANGLICAK_MINIGAME_TARGET_RADIUS_BEGINNER = 125;
	public final float ANGLICAK_MINIGAME_TARGET_RADIUS_NORMAL = 75;
	public final float ANGLICAK_MINIGAME_TARGET_RADIUS_HARD = 50;
	public final float ANGLICAK_MINIGAME_TARGET_RADIUS_EXTREME = 25;
	public final float ANGLICAK_MINIGAME_UNIFORM_DECELERATION = 5000;	
	public final float ANGLICAK_MINIGAME_MAX_WIN_VALUE = 3.f;
	public float ANGLICAK_MINIGAME_TRIES_COUNT = 3;
	
	// Race
	public final String RACE_MINIGAME_BACKGROUND_TEXTURE = "RaceMinigameBackground";
	public final int RACE_MINIGAME_CAR_TYPE_COUNT = 2;
	public final String RACE_MINIGAME_CAR_TEXTURE = "RaceMinigameCar";
	public float RACE_CAR_START_POSITION_X = 400;
	public String LINE_TEXTURE = "roadLine";	
	public final float MIN_SPEED_ADDITION = 100;
	public final float MAX_SPEED_ADDITION = 300;
	public final float MIN_CAR_DISTANCE = 100;
	public final float MAX_CAR_DISTANCE = 500;
	public final float DISTANCE_RAISER = 1.2f; // Change in distance
	public final int RACE_MINIMAL_DISTANCE_BETWEEN_CAR = 2;
	public final int RACE_MINIGAME_ZONES_COUNT_KIDDIE = 1;
	public final int RACE_MINIGAME_ZONES_COUNT_BEGINNER = 2;
	public final int RACE_MINIGAME_ZONES_COUNT_NORMAL = 3;
	public final int RACE_MINIGAME_ZONES_COUNT_HARD = 4;
	public final int RACE_MINIGAME_ZONES_COUNT_EXTREME = 5;
	public final int RACE_MINIGAME_TIME_KIDDIE = 20;
	public final int RACE_MINIGAME_TIME_BEGINNER = 20;
	public final int RACE_MINIGAME_TIME_NORMAL = 25;
	public final int RACE_MINIGAME_TIME_HARD = 30;
	public final int RACE_MINIGAME_TIME_EXTREME = 35;
	public final int RACE_MINIGAME_SPEED_KIDDIE = 250;
	public final int RACE_MINIGAME_SPEED_BEGINNER = 300;
	public final int RACE_MINIGAME_SPEED_NORMAL = 350;
	public final int RACE_MINIGAME_SPEED_HARD = 400;
	public final int RACE_MINIGAME_SPEED_EXTREME = 450;
	
	// Repairing
	public final String REPAIRING_MINIGAME_BACKGROUND_TEXTURE = "RepairingMinigameBackground";
	public final String REPAIRING_MINIGAME_HAND_JACK_TEXTURE = "RepairingMinigameHandJack";	
	public final String REPAIRING_MINIGAME_SPANNER_TEXTURE = "RepairingMinigameSpanner";
	public final String REPAIRING_MINIGAME_DAMAGED_TIRE_TEXTURE = "RepairingMinigameDamagedTire";
	public final String REPAIRING_MINIGAME_NEW_TIRE_TEXTURE = "RepairingMinigameNewTire";
	public final String REPAIRING_MINIGAME_OBJECT= "RepairingMinigameObject";
	public final float REPAIRING_MINIGAME_TARGET_RADIUS = 80.f;
}
