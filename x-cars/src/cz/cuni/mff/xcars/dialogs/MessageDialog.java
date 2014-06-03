package cz.cuni.mff.xcars.dialogs;

import cz.cuni.mff.xcars.Xcars;
import cz.cuni.mff.xcars.constants.Constants;
import cz.cuni.mff.xcars.gamelogic.SubLevel;
import cz.cuni.mff.xcars.scene.GameScreen;
import cz.cuni.mff.xcars.screenObjects.ScreenAdaptiveButton;

public class MessageDialog extends Dialog {

	private ScreenAdaptiveButton buttonOk;

	public MessageDialog(GameScreen gameScreen, SubLevel subLevel,
			String message) {
		super(gameScreen, subLevel, message);
		buttonOk = new ScreenAdaptiveButton(
				Xcars.getInstance().assets
						.getGraphics(Constants.dialog.MESSAGE_DIALOG_BUTTON_OK_TEXTURE),
				Xcars.getInstance().assets
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
