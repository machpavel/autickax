package cz.mff.cuni.autickax.dialogs;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.sun.accessibility.internal.resources.accessibility_zh_HK;

import cz.mff.cuni.autickax.Autickax;
import cz.mff.cuni.autickax.Constants;
import cz.mff.cuni.autickax.gamelogic.GameStatistics;
import cz.mff.cuni.autickax.gamelogic.SubLevel;
import cz.mff.cuni.autickax.gamelogic.SubLevel2;
import cz.mff.cuni.autickax.menu.MenuImage;
import cz.mff.cuni.autickax.scene.GameScreen;

public class CompleteLevelDialog extends DecisionDialog {

	GameStatistics stats;
	SubLevel2 subLevel2;

		
	public CompleteLevelDialog(GameScreen gameScreen, SubLevel2 subLevel2, GameStatistics stats, boolean isNextLevelAvailible) {
		super(gameScreen, subLevel2, "", isNextLevelAvailible);
		this.stats = stats;
		this.subLevel2 = subLevel2;
				
		
		this.messageLabel = new DialogLabel(createResultString());
		this.messageLabel.setPosition( Constants.DIALOG_MESSAGE_POSITION_X  - messageLabel.getWidth() / 2, Constants.DIALOG_MESSAGE_POSITION_Y - messageLabel.getHeight() / 2);
		this.stage.addActor(this.messageLabel);
		
		drawStars(stats.getNumberOfStars(), 600, 200);
	}	
	
	private void drawStars(byte stars, int x, int y) {
		byte j = 0;
		for (; j < stars; ++j) {
			drawStar(x, y, j, Constants.COMPLETE_DIALOG_STAR_FILLED_TEXTURE);
		}
		for (; j < 3; ++j) {
			drawStar(x, y, j, Constants.COMPLETE_DIALOG_STAR_EMPTY_TEXTURE);
		}
	}
	
	float starsXStart = 600;
	float starsY = 200;
	private void drawStar(int x, int y, byte j, String textureName) {
		MenuImage gainedStar = new MenuImage(Autickax.getInstance().assets.getGraphics(textureName));
		gainedStar.setPosition(x + j * gainedStar.getWidth(), y);
		stage.addActor(gainedStar);
	}
	
	private String createResultString(){
		StringBuilder str = new StringBuilder();
		str.append("Difficulty ... ");
		str.append(stats.getDifficulty());
		str.append('\n');
				
			
		str.append("Collisions: ");
		str.append(String.valueOf(stats.getCollisions()));
		str.append(" : ");
		str.append(String.valueOf(stats.getSucceeded()));
		str.append("succeded and ");		
		str.append(String.valueOf(stats.getFailed()));
		str.append("failed");
		str.append('\n');
		
		str.append("Stars: ");
		str.append(stats.getNumberOfStars());
		str.append('\n');
		
		str.append("Time in phase 1: ");
		str.append(String.format("%1$,.2f", stats.getPhase1ElapsedTime()));
		str.append('/');
		str.append(String.format("%1$,.2f", stats.getPhase1TimeLimit()));
		str.append('\n');
						
		str.append("Time in phase 1: ");
		str.append(stats.getPhase2ElapsedTime());
		str.append('\n');
		
		str.append("Score: ");
		str.append(stats.getScoreFromTime());
		str.append('\n');
		
		return str.toString();
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
