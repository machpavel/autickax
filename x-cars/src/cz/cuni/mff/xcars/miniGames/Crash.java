package cz.cuni.mff.xcars.miniGames;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import cz.cuni.mff.xcars.gamelogic.SubLevel;
import cz.cuni.mff.xcars.scene.GameScreen;

public class Crash extends Minigame {

	public Crash(String resultMessage, GameScreen gameScreen, SubLevel parent){
		this(resultMessage, ResultType.FAILED, 0, gameScreen, parent);
	}
	
	public Crash(String resultMessage, ResultType result, float resultValue, GameScreen gameScreen, SubLevel parent) {
		super(gameScreen, parent);

		this.resultMessage = resultMessage;
		this.result = result;
		this.resultValue = resultValue;

		// if (Xcars.settings.showTooltips) {
		// this.parent.setDialog(new MessageDialog(gameScreen, parent,
		// Constants.strings.TOOLTIP_MINIGAME_CRASHED_WHAT_TO_DO));
		// }
	}

	@Override
	public void draw(SpriteBatch batch) {
		// Don't draw anything	
	}
	
	private void fail() {
		this.playResultSound();
		this.endCommunication();
	}

	@Override
	public void update(float delta) {
		fail();
	}
}
