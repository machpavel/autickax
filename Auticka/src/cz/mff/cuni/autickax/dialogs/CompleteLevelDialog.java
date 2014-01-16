package cz.mff.cuni.autickax.dialogs;

import cz.mff.cuni.autickax.Autickax;
import cz.mff.cuni.autickax.Constants;
import cz.mff.cuni.autickax.gamelogic.GameStatistics;
import cz.mff.cuni.autickax.gamelogic.SubLevel2;
import cz.mff.cuni.autickax.scene.GameScreen;

public class CompleteLevelDialog extends DecisionDialog {
	GameStatistics stats;
	SubLevel2 subLevel2;
	public CompleteLevelDialog(GameScreen gameScreen, SubLevel2 subLevel2, GameStatistics stats) {
		super(gameScreen, subLevel2, "", true);
		this.stats = stats;
		this.subLevel2 = subLevel2;
	}	
	
	@Override
	protected void CreateButtonContinue() {
		buttonContinue = new DialogButton(
				Autickax.getInstance().assets
						.getGraphics(Constants.DECISION_DIALOG_BUTTON_CONTINUE_TEXTURE),
				Autickax.getInstance().assets
						.getGraphics(Constants.DECISION_DIALOG_BUTTON_CONTINUE_OVER_TEXTURE)) {

			@Override
			public void action() {
				status = DialogAbstractStatus.FINISHED;
				decision = DecisionType.CONTINUE;				
				subLevel2.onLevelComplete();
				endCommunication();
			}
		};
		buttonContinue.setPosition(
				Constants.DECISION_DIALOG_BUTTON_CONTINUE_POSITION_X
						- buttonContinue.getWidth() / 2,
				Constants.DECISION_DIALOG_BUTTON_CONTINUE_POSITION_Y
						- buttonContinue.getHeight() / 2);
		this.stage.addActor(buttonContinue);
	}
}
