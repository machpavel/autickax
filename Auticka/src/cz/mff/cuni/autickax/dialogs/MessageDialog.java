package cz.mff.cuni.autickax.dialogs;

import cz.mff.cuni.autickax.Autickax;
import cz.mff.cuni.autickax.constants.Constants;
import cz.mff.cuni.autickax.gamelogic.SubLevel;
import cz.mff.cuni.autickax.scene.GameScreen;
import cz.mff.cuni.autickax.screenObjects.ScreenAdaptiveButton;

public class MessageDialog extends Dialog {

	private ScreenAdaptiveButton buttonOk;

	public MessageDialog(GameScreen gameScreen, SubLevel subLevel,
			String message) {
		super(gameScreen, subLevel, message);
		buttonOk = new ScreenAdaptiveButton(
				Autickax.getInstance().assets
						.getGraphics(Constants.dialog.MESSAGE_DIALOG_BUTTON_OK_TEXTURE),
				Autickax.getInstance().assets
						.getGraphics(Constants.dialog.MESSAGE_DIALOG_BUTTON_OK_OVER_TEXTURE)) {
			@Override
			public void action() {
				playButtonSound();
				status = DialogAbstractStatus.FINISHED;				
				parent.onDialogEnded();
				endCommunication();
			}
		};
		buttonOk.setCenterPosition(
			Constants.dialog.MESSAGE_DIALOG_BUTTON_OK_POSITION_X,
			Constants.dialog.MESSAGE_DIALOG_BUTTON_OK_POSITION_Y
		);
		this.stage.addActor(buttonOk);
	}

	@Override
	public DecisionType getDecision() {
		return DecisionType.CONTINUE;
	}
}
