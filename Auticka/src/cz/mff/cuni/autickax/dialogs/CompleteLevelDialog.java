package cz.mff.cuni.autickax.dialogs;

import com.badlogic.gdx.scenes.scene2d.ui.Button;

import cz.mff.cuni.autickax.Autickax;
import cz.mff.cuni.autickax.Constants;
import cz.mff.cuni.autickax.gamelogic.GameStatistics;
import cz.mff.cuni.autickax.gamelogic.SubLevel;
import cz.mff.cuni.autickax.scene.GameScreen;

public class CompleteLevelDialog extends DecisionDialog {
	GameStatistics stats;
	public CompleteLevelDialog(GameScreen gameScreen, SubLevel subLevel, GameStatistics stats) {
		super(gameScreen, subLevel, "", true);
		this.stats = stats;
	}
	
	

}
