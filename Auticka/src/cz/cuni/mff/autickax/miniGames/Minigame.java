package cz.cuni.mff.autickax.miniGames;

import cz.cuni.mff.autickax.Autickax;
import cz.cuni.mff.autickax.constants.Constants;
import cz.cuni.mff.autickax.dialogs.Comunicator;
import cz.cuni.mff.autickax.gamelogic.SubLevel;
import cz.cuni.mff.autickax.scene.GameScreen;

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
	public void endCommunication() {
		this.parent.onMinigameEnded();
		super.endCommunication();
	}

	protected void playResultSound() {
		switch (result) {
		case FAILED:
			Autickax.getInstance().assets.soundAndMusicManager.playSound(
					Constants.sounds.SOUND_MINIGAME_FAIL, Constants.sounds.SOUND_DEFAULT_VOLUME);
			break;
		case FAILED_WITH_VALUE:
			Autickax.getInstance().assets.soundAndMusicManager.playSound(
					Constants.sounds.SOUND_MINIGAME_FAIL, Constants.sounds.SOUND_DEFAULT_VOLUME);
			break;
		case PROCEEDED:
			Autickax.getInstance().assets.soundAndMusicManager.playSound(
					Constants.sounds.SOUND_MINIGAME_SUCCESS, Constants.sounds.SOUND_DEFAULT_VOLUME);
			break;
		case PROCEEDED_WITH_VALUE:
			Autickax.getInstance().assets.soundAndMusicManager.playSound(
					Constants.sounds.SOUND_MINIGAME_SUCCESS, Constants.sounds.SOUND_DEFAULT_VOLUME);
			break;
		default:
			throw new IllegalStateException(result.toString());
		}
	}

	protected void updateInLeavingState(float delta) {
		// Delays the leaving if there is no message in the end of a game
		if (!Autickax.settings.isShowTooltips()) {
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
