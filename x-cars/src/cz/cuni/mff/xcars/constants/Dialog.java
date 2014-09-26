package cz.cuni.mff.xcars.constants;

import com.badlogic.gdx.graphics.Color;

public final class Dialog {
	/** Determines ideal dialog world width. */
	public final int DIALOG_WORLD_WIDTH = 600;
	/** Determines ideal dialog world height. */
	public final int DIALOG_WORLD_HEIGHT = 360;
	/** Determines dialog x shift. */
	public final int DIALOG_WORLD_X_OFFSET = 100;
	/** Determines dialog y shift. */
	public final int DIALOG_WORLD_Y_OFFSET = 60;

	//
	// DIALOG POSITIONING AND TEXTURES
	//

	public final String DIALOG_BACKGROUND_TEXTURE = "dialog/dialogBackground";
	public final Color DIALOG_BACKGROUND_COLOR = new Color(0.7f, 0.7f, 0.7f, 0.5f);

	public final float DIALOG_MESSAGE_POSITION_X = 400;
	public final float DIALOG_MESSAGE_POSITION_Y = 270;
	public final float DIALOG_MESSAGE_WIDTH = 600;	

	public final String MESSAGE_DIALOG_BUTTON_OK_TEXTURE = "dialog/popOk";
	public final String MESSAGE_DIALOG_BUTTON_OK_OVER_TEXTURE = "dialog/popOkOver";
	public final float MESSAGE_DIALOG_BUTTON_OK_POSITION_X = 400;
	public final float MESSAGE_DIALOG_BUTTON_OK_POSITION_Y = 110;

	public final String DECISION_DIALOG_BUTTON_CONTINUE_TEXTURE = "dialog/popNext";
	public final String DECISION_DIALOG_BUTTON_CONTINUE_OVER_TEXTURE = "dialog/popNextOver";
	public final float DECISION_DIALOG_BUTTON_CONTINUE_POSITION_X = 600;
	public final float DECISION_DIALOG_BUTTON_CONTINUE_POSITION_Y = 110;

	public final String DECISION_DIALOG_BUTTON_RESTART_TEXTURE = "dialog/popRestart";
	public final String DECISION_DIALOG_BUTTON_RESTART_OVER_TEXTURE = "dialog/popRestartOver";
	public final float DECISION_DIALOG_BUTTON_RESTART_POSITION_X = 400;
	public final float DECISION_DIALOG_BUTTON_RESTART_POSITION_Y = 110;

	public final String DECISION_DIALOG_BUTTON_GO_TO_MAIN_MENU_TEXTURE = "dialog/popMenu";
	public final String DECISION_DIALOG_BUTTON_GO_TO_MAIN_MENU_OVER_TEXTURE = "dialog/popMenuOver";
	public final float DECISION_DIALOG_BUTTON_GO_TO_MAIN_MENU_POSITION_X = 200;
	public final float DECISION_DIALOG_BUTTON_GO_TO_MAIN_MENU_POSITION_Y = 110;

	public final float COMPLETE_DIALOG_MESSAGE_POSITION_X = 360;
	public final float COMPLETE_DIALOG_MESSAGE_POSITION_Y = 230;
	public final float COMPLETE_DIALOG_MESSAGE_WIDTH = 450;
	public final float COMPLETE_DIALOG_MESSAGE_HEIGHT = 250;
	public final String COMPLETE_DIALOG_STAR_FILLED_TEXTURE = "dialog/endOfLevelFullStar";
	public final String COMPLETE_DIALOG_STAR_EMPTY_TEXTURE = "dialog/endOfLevelEmptyStar";
	public final int COMPLETE_DIALOG_STAR_POSITION_X = 600;
	public final int COMPLETE_DIALOG_STAR_POSITION_Y = 155;

	public final int PAUSE_DIALOG_TOOLTIPS_POSITION_X = 100;
	public final int PAUSE_DIALOG_TOOLTIPS_POSITION_Y = 290;
	public final int PAUSE_DIALOG_SOUNDS_POSITION_X = 320;
	public final int PAUSE_DIALOG_SOUNDS_POSITION_Y = 315;
	public final int PAUSE_DIALOG_MUSIC_POSITION_X = 550;
	public final int PAUSE_DIALOG_MUSIC_POSITION_Y = 315;

}
