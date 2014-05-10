package cz.mff.cuni.autickax.miniGames;

import cz.mff.cuni.autickax.Autickax;
import cz.mff.cuni.autickax.constants.Constants;
import cz.mff.cuni.autickax.dialogs.MessageDialog;
import cz.mff.cuni.autickax.gamelogic.SubLevel;
import cz.mff.cuni.autickax.scene.GameScreen;

public class Crash extends Minigame {

	public Crash(String resultMessage, GameScreen gameScreen, SubLevel parent){
		this(resultMessage, ResultType.FAILED, 0, gameScreen, parent);
	}
	
	public Crash(String resultMessage, ResultType result, float resultValue, GameScreen gameScreen, SubLevel parent) {
		super(gameScreen, parent);

		this.resultMessage = resultMessage;
		this.result = result;
		this.resultValue = resultValue;

		if (Autickax.settings.showTooltips) {
			this.parent.setDialog(new MessageDialog(gameScreen, parent,
					Constants.strings.TOOLTIP_MINIGAME_CRASHED_WHAT_TO_DO));
		}
	}

	private void fail() {
		this.endCommunication();
	}

	@Override
	public void update(float delta) {
		fail();
	}
}
