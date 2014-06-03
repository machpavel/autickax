package cz.cuni.mff.autickax.dialogs;

import cz.cuni.mff.autickax.Autickax;
import cz.cuni.mff.autickax.constants.Constants;
import cz.cuni.mff.autickax.gamelogic.SubLevel;
import cz.cuni.mff.autickax.scene.GameScreen;
import cz.cuni.mff.autickax.screenObjects.ScreenAdaptiveButton;

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
