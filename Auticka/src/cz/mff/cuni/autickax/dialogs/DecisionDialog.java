package cz.mff.cuni.autickax.dialogs;

import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

import cz.mff.cuni.autickax.Autickax;
import cz.mff.cuni.autickax.Constants;
import cz.mff.cuni.autickax.gamelogic.SubLevel;
import cz.mff.cuni.autickax.scene.GameScreen;

public class DecisionDialog extends Dialog {

	private Button buttonContinue;
	private Button buttonRestart;
	private DialogButton buttonGoToMainMenu;
	private DecisionType decision;

	public DecisionDialog(GameScreen gameScreen, SubLevel subLevel, String message, boolean enableContinueButton) {
		super(gameScreen, subLevel, message);
		if (enableContinueButton)
			CreateButtonContinue();
		CreateButtonRestart();
		CreateCuttonGoToMainMenu();
	}

	public enum DecisionType {
		CONTINUE, RESTART, GO_TO_MAIN_MENU;
	}

	private void CreateButtonContinue() {
		buttonContinue = new DialogButton(
				new TextureRegionDrawable(
						Autickax.getInstance().assets
								.getGraphics(Constants.DECISION_DIALOG_BUTTON_CONTINUE_TEXTURE))) {

			@Override
			public void action() {
				status = DialogAbstractStatus.FINISHED;				
				decision = DecisionType.CONTINUE;	
				parent.onDialogEnded();
			}
		};
		buttonContinue.setPosition(
				Constants.DECISION_DIALOG_BUTTON_CONTINUE_POSITION_X
						- buttonContinue.getWidth() / 2,
				Constants.DECISION_DIALOG_BUTTON_CONTINUE_POSITION_Y
						- buttonContinue.getHeight() / 2);
		this.stage.addActor(buttonContinue);
	}

	private void CreateButtonRestart() {
		buttonRestart = new DialogButton(
				new TextureRegionDrawable(
						Autickax.getInstance().assets
								.getGraphics(Constants.DECISION_DIALOG_BUTTON_RESTART_TEXTURE))) {

			@Override
			public void action() {
				status = DialogAbstractStatus.FINISHED;
				decision = DecisionType.RESTART;
				parent.onDialogEnded();
			}
		};
		buttonRestart.setPosition(
				Constants.DECISION_DIALOG_BUTTON_RESTART_POSITION_X
						- buttonRestart.getWidth() / 2,
				Constants.DECISION_DIALOG_BUTTON_RESTART_POSITION_Y
						- buttonRestart.getHeight() / 2);
		this.stage.addActor(buttonRestart);
	}

	private void CreateCuttonGoToMainMenu() {
		buttonGoToMainMenu = new DialogButton(
				new TextureRegionDrawable(
						Autickax.getInstance().assets
								.getGraphics(Constants.DECISION_DIALOG_BUTTON_GO_TO_MAIN_MENU_TEXTURE))) {
			@Override
			public void action() {
				status = DialogAbstractStatus.FINISHED;
				decision = DecisionType.GO_TO_MAIN_MENU;
				parent.onDialogEnded();
			}
		};
		buttonGoToMainMenu.setPosition(
				Constants.DECISION_DIALOG_BUTTON_GO_TO_MAIN_MENU_POSITION_X
						- buttonGoToMainMenu.getWidth() / 2,
				Constants.DECISION_DIALOG_BUTTON_GO_TO_MAIN_MENU_POSITION_Y
						- buttonGoToMainMenu.getHeight() / 2);
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