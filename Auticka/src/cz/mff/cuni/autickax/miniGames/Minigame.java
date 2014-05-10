package cz.mff.cuni.autickax.miniGames;

import cz.mff.cuni.autickax.Autickax;
import cz.mff.cuni.autickax.constants.Constants;
import cz.mff.cuni.autickax.dialogs.Comunicator;
import cz.mff.cuni.autickax.gamelogic.SubLevel;
import cz.mff.cuni.autickax.scene.GameScreen;

public abstract class Minigame extends Comunicator {

	protected ResultType result = ResultType.UNASIGNED;
	protected float resultValue = Constants.minigames.RESULT_VALUE_NOTHING;
	protected String resultMessage = null;
	private float leavingDelay = Constants.minigames.LEAVING_DELAY;

	public Minigame(GameScreen gameScreen, SubLevel parent) {
		super(gameScreen, parent);
	}

	public ResultType getResult() {
		return this.result;
	}

	public float getResultValue() {
		return this.resultValue;
	}

	public String getResultMessage() {
		return resultMessage;
	}

	@Override
	protected void endCommunication() {
		this.parent.onMinigameEnded();
		super.endCommunication();
	}

	protected void updateInLeavingState(float delta) {

		// Delays the end if there is no message in the end of a game
		if (!Autickax.settings.showTooltips) {
			leavingDelay -= delta;
			if (leavingDelay < 0) {
				this.endCommunication();
			}
		} else {
			this.endCommunication();
		}
	}

	public enum ResultType {
		FAILED, FAILED_WITH_VALUE, PROCEEDED_WITH_VALUE, PROCEEDED, UNASIGNED;
	}
}
