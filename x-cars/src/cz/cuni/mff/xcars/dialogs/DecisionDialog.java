package cz.cuni.mff.xcars.dialogs;

import cz.cuni.mff.xcars.Xcars;
import cz.cuni.mff.xcars.constants.Constants;
import cz.cuni.mff.xcars.gamelogic.SubLevel;
import cz.cuni.mff.xcars.scene.GameScreen;
import cz.cuni.mff.xcars.screenObjects.ScreenAdaptiveButton;

public class DecisionDialog extends Dialog {

	protected ScreenAdaptiveButton buttonContinue;
	protected ScreenAdaptiveButton buttonRestart;
	protected ScreenAdaptiveButton buttonGoToMainMenu;
	protected DecisionType decision;

	public DecisionDialog(GameScreen gameScreen, SubLevel subLevel,
			String message, boolean enableContinueButton) {
		super(gameScreen, subLevel, message);
		if (enableContinueButton)
			CreateButtonContinue();
		CreateButtonRestart();
		CreateCuttonGoToMainMenu();
	}

	protected void CreateButtonContinue() {
		buttonContinue = new ScreenAdaptiveButton(
				Xcars.getInstance().assets
						.getGraphics(Constants.dialog.DECISION_DIALOG_BUTTON_CONTINUE_TEXTURE),
				Xcars.getInstance().assets
						.getGraphics(Constants.dialog.DECISION_DIALOG_BUTTON_CONTINUE_OVER_TEXTURE)) {

			@Override
			public void action() {
				playButtonSound();				
				decision = DecisionType.CONTINUE;								
				endCommunication();
			}
		};
		buttonContinue.setCenterPosition(
			Constants.dialog.DECISION_DIALOG_BUTTON_CONTINUE_POSITION_X,
			Constants.dialog.DECISION_DIALOG_BUTTON_CONTINUE_POSITION_Y);
		this.stage.addActor(buttonContinue);
	}

	private void CreateButtonRestart() {
		buttonRestart = new ScreenAdaptiveButton(
				Xcars.getInstance().assets
						.getGraphics(Constants.dialog.DECISION_DIALOG_BUTTON_RESTART_TEXTURE),
				Xcars.getInstance().assets
						.getGraphics(Constants.dialog.DECISION_DIALOG_BUTTON_RESTART_OVER_TEXTURE)) {

			@Override
			public void action() {
				playButtonSound();
				decision = DecisionType.RESTART;				
				endCommunication();
			}
		};
		buttonRestart.setCenterPosition(
			Constants.dialog.DECISION_DIALOG_BUTTON_RESTART_POSITION_X,
			Constants.dialog.DECISION_DIALOG_BUTTON_RESTART_POSITION_Y);
		this.stage.addActor(buttonRestart);
	}

	private void CreateCuttonGoToMainMenu() {
		buttonGoToMainMenu = new ScreenAdaptiveButton(
				Xcars.getInstance().assets
						.getGraphics(Constants.dialog.DECISION_DIALOG_BUTTON_GO_TO_MAIN_MENU_TEXTURE),
				Xcars.getInstance().assets
						.getGraphics(Constants.dialog.DECISION_DIALOG_BUTTON_GO_TO_MAIN_MENU_OVER_TEXTURE)) {
			@Override
			public void action() {
				playButtonSound();
				decision = DecisionType.GO_TO_MAIN_MENU;
				endCommunication();
			}
		};
		buttonGoToMainMenu.setCenterPosition(
			Constants.dialog.DECISION_DIALOG_BUTTON_GO_TO_MAIN_MENU_POSITION_X,
			Constants.dialog.DECISION_DIALOG_BUTTON_GO_TO_MAIN_MENU_POSITION_Y
		);
		this.stage.addActor(buttonGoToMainMenu);
	}

	@Override
	public DecisionType getDecision() {
		return this.decision;
	}
}
