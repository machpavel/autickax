package cz.mff.cuni.autickax.dialogs;

import cz.mff.cuni.autickax.Autickax;
import cz.mff.cuni.autickax.constants.Constants;
import cz.mff.cuni.autickax.gamelogic.SubLevel;
import cz.mff.cuni.autickax.scene.GameScreen;
import cz.mff.cuni.autickax.screenObjects.ScreenAdaptiveButton;

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
				Autickax.getInstance().assets
						.getGraphics(Constants.dialog.DECISION_DIALOG_BUTTON_CONTINUE_TEXTURE),
				Autickax.getInstance().assets
						.getGraphics(Constants.dialog.DECISION_DIALOG_BUTTON_CONTINUE_OVER_TEXTURE)) {

			@Override
			public void action() {
				playButtonSound();
				status = DialogAbstractStatus.FINISHED;
				decision = DecisionType.CONTINUE;				
				parent.onDialogEnded();
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
				Autickax.getInstance().assets
						.getGraphics(Constants.dialog.DECISION_DIALOG_BUTTON_RESTART_TEXTURE),
				Autickax.getInstance().assets
						.getGraphics(Constants.dialog.DECISION_DIALOG_BUTTON_RESTART_OVER_TEXTURE)) {

			@Override
			public void action() {
				playButtonSound();
				status = DialogAbstractStatus.FINISHED;
				decision = DecisionType.RESTART;				
				parent.onDialogEnded();
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
				Autickax.getInstance().assets
						.getGraphics(Constants.dialog.DECISION_DIALOG_BUTTON_GO_TO_MAIN_MENU_TEXTURE),
				Autickax.getInstance().assets
						.getGraphics(Constants.dialog.DECISION_DIALOG_BUTTON_GO_TO_MAIN_MENU_OVER_TEXTURE)) {
			@Override
			public void action() {
				playButtonSound();
				status = DialogAbstractStatus.FINISHED;
				decision = DecisionType.GO_TO_MAIN_MENU;
				parent.onDialogEnded();
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
	public void render() {
		// TODO Auto-generated method stub

	}

	@Override
	public DecisionType getDecision() {
		return this.decision;
	}

	@Override
	public void onDialogEnded() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onMinigameEnded() {
		// TODO Auto-generated method stub

	}

}
