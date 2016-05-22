package cz.cuni.mff.xcars.constants;

import java.util.HashMap;

public final class Menu {
	//
	// Menu textures
	//

	//Backgrounds
	public final String MAIN_MENU_BACKGROUND = "menu/mainMenuBackground";
	public final String DIFFICULTY_MENU_BACKGROUND = "menu/difficultyMenuBackground";
	public final String LOADING_LEVEL_MENU_BACKGROUND = "menu/menuLoadingLevelBackground";
	
	private static final String CITY_LEVEL_SELECT_BACKGROUND = "menu/backgroundCity3";
	private static final String DESERT_LEVEL_SELECT_BACKGROUND = "menu/backgroundDesert6";
	private static final String FOREST_LEVEL_SELECT_BACKGROUND = "menu/backgroundforest1";
	private static final String ROOM_LEVEL_SELECT_BACKGROUND = "menu/backgroundRoom3";
	private static final String SPACE_LEVEL_SELECT_BACKGROUND = "menu/backgroundSpace4";
	private static final String WINTER_LEVEL_SELECT_BACKGROUND = "menu/backgroundWinter4";
	
	public static final HashMap<String, String> SCENARIO_TO_BG_MAPPING;
	static
	{
		SCENARIO_TO_BG_MAPPING = new HashMap<String, String>();
		SCENARIO_TO_BG_MAPPING.put("city", CITY_LEVEL_SELECT_BACKGROUND);
		SCENARIO_TO_BG_MAPPING.put("desert", DESERT_LEVEL_SELECT_BACKGROUND);
		SCENARIO_TO_BG_MAPPING.put("forest", FOREST_LEVEL_SELECT_BACKGROUND);
		SCENARIO_TO_BG_MAPPING.put("room", ROOM_LEVEL_SELECT_BACKGROUND);
		SCENARIO_TO_BG_MAPPING.put("space", SPACE_LEVEL_SELECT_BACKGROUND);
		SCENARIO_TO_BG_MAPPING.put("winter", WINTER_LEVEL_SELECT_BACKGROUND);
	}
	
	public final String LOADING_LEVEL_MENU_GRAY = "menu/menuLoadingLevelGrey";
	public final String LOADING_LEVEL_MENU_GREEN = "menu/menuLoadingLevelGreen";
	public final String LOADING_LEVEL_MENU_RED = "menu/menuLoadingLevelRed";

	public final String LOADING_SCREEN_BACKGROUND = "loadingScreenBackground";
	public final String LOADING_SCREEN_CAR = "loadingScreenCar";
	public final String LOADING_SCREEN_FUME = "loadingScreenFume";

	public final float LEVELS_MENU_RED = (float) 169 / 255;
	public final float LEVELS_MENU_GREEN = (float) 207 / 255;
	public final float LEVELS_MENU_BLUE = (float) 56 / 255;

	public final String BUTTON_MENU_PLAY = "menu/menuStart";
	public final String BUTTON_MENU_PLAY_HOVER = "menu/menuStartHover";

	public final String BUTTON_MENU_EXIT = "menu/menuExit";
	public final String BUTTON_MENU_EXIT_HOVER = "menu/menuExitHover";

	public final String BUTTON_MENU_LEVEL_DISABLED = "menu/menuLevelDisabled";
	public final String BUTTON_MENU_LEVEL = "menu/menuLevel";
	public final String BUTTON_MENU_LEVEL_HOVER = "menu/menuLevelHover";
	public final String LEVEL_STAR_FULL = "menu/menuLevelStarFull";
	public final String LEVEL_STAR_EMPTY = "menu/menuLevelStarEmpty";
	
	public final String SLIDER_MENU_LEVEL_BACKGROUND = "menu/menuSelectScreenProgressBar";
	public final String SLIDER_MENU_LEVEL_KNOB = "menu/menuSelectScreenKnob";
	public final String SLIDER_MENU_LEVEL_ANCHOR = "menu/menuSelectScreenPageAnchor";
	
	public final String SCENARIOS_FOLDER_NAME = "scenarioThemes";

	public final String BUTTON_MENU_DIFFICULTY = "menu/menuDifficulty";
	public final String BUTTON_MENU_DIFFICULTY_HOVER = "menu/menuDifficultyHover";

	public final String BUTTON_MENU_TOOLTIPS_ON = "menu/menuTipsOn";
	public final String BUTTON_MENU_TOOLTIPS_ON_HOVER = "menu/menuTipsOnHover";
	public final String BUTTON_MENU_TOOLTIPS_OFF = "menu/menuTipsOff";
	public final String BUTTON_MENU_TOOLTIPS_OFF_HOVER = "menu/menuTipsOffHover";

	public final String BUTTON_MENU_SOUNDS_ON = "menu/menuSoundsOn";
	public final String BUTTON_MENU_SOUNDS_ON_HOVER = "menu/menuSoundsOnHover";
	public final String BUTTON_MENU_SOUNDS_OFF = "menu/menuSoundsOff";
	public final String BUTTON_MENU_SOUNDS_OFF_HOVER = "menu/menuSoundsOffHover";

	public final String BUTTON_MENU_MUSIC_ON = "menu/menuMusicOn";
	public final String BUTTON_MENU_MUSIC_ON_HOVER = "menu/menuMusicOnHover";
	public final String BUTTON_MENU_MUSIC_OFF = "menu/menuMusicOff";
	public final String BUTTON_MENU_MUSIC_OFF_HOVER = "menu/menuMusicOffHover";
	
	public final String BUTTON_MENU_LEFT_SHIFTER = "menu/leftShifter";
	public final String BUTTON_MENU_LEFT_SHIFTER_HOVER = "menu/leftShifterHover";
	public final String BUTTON_MENU_RIGHT_SHIFTER = "menu/rightShifter";
	public final String BUTTON_MENU_RIGHT_SHIFTER_HOVER = "menu/rightShifterHover";
	
	public final int DISPLAYED_LEVELS_MAX_COUNT = 15;
	public final int DISPLAYED_SCENARIOS_MAX_COUNT = 4;
	
	public final float LEVEL_SELECT_SCREEN_buttonsMinXPosition = 20;
	public final float LEVEL_SELECT_SCREEN_buttonsMinYPosition = 50;
	public final float LEVEL_SELECT_SCREEN_buttonsMaxYPosition = 450;
	public final int LEVEL_SELECT_SCREEN_buttonsXOffset = 60;
	public final int LEVEL_SELECT_SCREEN_buttonsYOffset = 60;
	
	public final float SCENARIO_SELECT_SCREEN_buttonsMinXPosition = 20;
	public final float SCENARIO_SELECT_SCREEN_buttonsMinYPosition = 50;
	public final float SCENARIO_SELECT_SCREEN_buttonsMaxYPosition = 450;
	public final int SCENARIO_SELECT_SCREEN_buttonsXOffset = 15;
	public final int SCENARIO_SELECT_SCREEN_buttonsYOffset = 50;

}
