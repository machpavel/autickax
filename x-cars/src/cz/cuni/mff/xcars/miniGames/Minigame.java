package cz.cuni.mff.xcars.miniGames;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

import cz.cuni.mff.xcars.Xcars;
import cz.cuni.mff.xcars.constants.Constants;
import cz.cuni.mff.xcars.dialogs.Comunicator;
import cz.cuni.mff.xcars.gamelogic.SubLevel;
import cz.cuni.mff.xcars.scene.GameScreen;

public abstract class Minigame extends Comunicator {
	protected ResultType result = ResultType.UNASIGNED;
	protected float resultValue = Constants.minigames.RESULT_VALUE_NOTHING;
	protected String resultMessage = null;
	protected float leavingDelay = Constants.minigames.LEAVING_DELAY;
	protected TextureRegionDrawable backgroundTexture;

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
			this.soundsManager.playSound(Constants.sounds.SOUND_MINIGAME_FAIL, Constants.sounds.SOUND_DEFAULT_VOLUME);
			break;
		case FAILED_WITH_VALUE:
			this.soundsManager.playSound(Constants.sounds.SOUND_MINIGAME_FAIL, Constants.sounds.SOUND_DEFAULT_VOLUME);
			break;
		case PROCEEDED:
			this.soundsManager
					.playSound(Constants.sounds.SOUND_MINIGAME_SUCCESS, Constants.sounds.SOUND_DEFAULT_VOLUME);
			break;
		case PROCEEDED_WITH_VALUE:
			this.soundsManager
					.playSound(Constants.sounds.SOUND_MINIGAME_SUCCESS, Constants.sounds.SOUND_DEFAULT_VOLUME);
			break;
		default:
			throw new IllegalStateException(result.toString());
		}
	}

	protected void updateInLeavingState(float delta) {
		// Delays the leaving if there is no message in the end of a game
		if (!Xcars.settings.isShowTooltips()) {
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

	@Override
	protected void drawBackGroundTexture(SpriteBatch batch) {
		batch.begin();
		this.backgroundTexture.draw(batch, 0, 0, Constants.WORLD_WIDTH, Constants.WORLD_HEIGHT);
		batch.end();
	}
}
