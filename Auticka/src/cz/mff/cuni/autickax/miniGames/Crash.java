package cz.mff.cuni.autickax.miniGames;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import cz.mff.cuni.autickax.Autickax;
import cz.mff.cuni.autickax.Constants;
import cz.mff.cuni.autickax.dialogs.MessageDialog;
import cz.mff.cuni.autickax.gamelogic.SubLevel;
import cz.mff.cuni.autickax.scene.GameScreen;

public class Crash extends Minigame {

	public Crash(GameScreen gameScreen, SubLevel parent) {
		super(gameScreen, parent);
		if(Autickax.showTooltips)
			this.parent.setDialog(new MessageDialog(gameScreen, parent, Constants.TOOLTIP_MINIGAME_CRASHED_WHAT_TO_DO));
	}

	@Override
	public void update(float delta) {
		this.result = ResultType.FAILED;
		this.resultValue = 0;
		parent.onMinigameEnded();		
	}

	@Override
	public void draw(SpriteBatch batch) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void render() {
		// TODO Auto-generated method stub
		
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
