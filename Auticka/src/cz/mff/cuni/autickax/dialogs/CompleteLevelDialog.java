package cz.mff.cuni.autickax.dialogs;

import java.text.DecimalFormat;

import cz.mff.cuni.autickax.Autickax;
import cz.mff.cuni.autickax.constants.Constants;
import cz.mff.cuni.autickax.gamelogic.GameStatistics;
import cz.mff.cuni.autickax.gamelogic.SubLevel2;
import cz.mff.cuni.autickax.input.Input;
import cz.mff.cuni.autickax.menu.MenuImage;
import cz.mff.cuni.autickax.scene.GameScreen;

public class CompleteLevelDialog extends DecisionDialog {

	GameStatistics stats;
	SubLevel2 subLevel2;

		
	public CompleteLevelDialog(GameScreen gameScreen, SubLevel2 subLevel2, GameStatistics stats, boolean isNextLevelAvailible) {
		super(gameScreen, subLevel2, null, isNextLevelAvailible);
		this.stats = stats;
		this.subLevel2 = subLevel2;
				
		createLabels();

		drawStars(stats.getNumberOfStars(), Constants.dialog.COMPLETE_DIALOG_STAR_POSITION_X, Constants.dialog.COMPLETE_DIALOG_STAR_POSITION_Y);
	}	
	


	private void drawStars(byte stars, int x, int y) {
		byte j = 0;
		for (; j < stars; ++j) {
			drawStar(x, y, j, Constants.dialog.COMPLETE_DIALOG_STAR_FILLED_TEXTURE);
		}
		for (; j < 3; ++j) {
			drawStar(x, y, j, Constants.dialog.COMPLETE_DIALOG_STAR_EMPTY_TEXTURE);
		}
	}
	
	float starsXStart = 600;
	float starsY = 200;
	float offset = 80;
	private void drawStar(int x, int y, byte j, String textureName) {
		MenuImage gainedStar = new MenuImage(Autickax.getInstance().assets.getGraphics(textureName));
		gainedStar.setPosition(x * Input.xStretchFactor , (y + j * offset) * Input.yStretchFactor);
		stage.addActor(gainedStar);
	}
		
	private void createLabels() {
		FinishDialogLabel[][] labels = new FinishDialogLabel[6][2];

		// TODO Auto-generated method stub		
		labels[0][0] = new FinishDialogLabel("Difficulty:");
		labels[0][1] = new FinishDialogLabel(stats.getDifficulty().toString());
		
		labels[1][0] = new FinishDialogLabel("Succeeded collisions:");
		labels[1][1] = new FinishDialogLabel(String.valueOf(stats.getSucceeded()));
		
		labels[2][0] = new FinishDialogLabel("Failed collisions:");
		labels[2][1] = new FinishDialogLabel(String.valueOf(stats.getFailed()));
		
		DecimalFormat decimalFormat = new DecimalFormat("0.#");
		labels[3][0] = new FinishDialogLabel("Time in first part:");
		labels[3][1] = new FinishDialogLabel(decimalFormat.format(stats.getPhase1ElapsedTime()) + '/' + decimalFormat.format(stats.getPhase1TimeLimit()));
				
		labels[4][0] = new FinishDialogLabel("Time in second part:");
		labels[4][1] = new FinishDialogLabel(decimalFormat.format(stats.getPhase2ElapsedTime()));

		labels[5][0] = new FinishDialogLabel("Score:");
		labels[5][1] = new FinishDialogLabel(Integer.toString(stats.getScoreFromTime()));
		
		FinishDialogLabel foo = new FinishDialogLabel(Integer.toString(stats.getScoreFromTime()));
		System.out.println(foo.getWidth());
		
		for (int row = 0; row < labels.length; row++) {
			labels[row][0].setPosition( Constants.dialog.COMPLETE_DIALOG_MESSAGE_POSITION_X  - Constants.dialog.COMPLETE_DIALOG_MESSAGE_WIDTH / 2, Constants.dialog.COMPLETE_DIALOG_MESSAGE_POSITION_Y + Constants.dialog.COMPLETE_DIALOG_MESSAGE_HEIGHT / 2 - Constants.dialog.COMPLETE_DIALOG_MESSAGE_HEIGHT / labels.length * row);
			this.stage.addActor(labels[row][0]);
			
			labels[row][1].setPosition( Constants.dialog.COMPLETE_DIALOG_MESSAGE_POSITION_X  + Constants.dialog.COMPLETE_DIALOG_MESSAGE_WIDTH / 2 - labels[row][1].getWidth(), Constants.dialog.COMPLETE_DIALOG_MESSAGE_POSITION_Y + Constants.dialog.COMPLETE_DIALOG_MESSAGE_HEIGHT / 2 - Constants.dialog.COMPLETE_DIALOG_MESSAGE_HEIGHT / labels.length * row);
			this.stage.addActor(labels[row][1]);						
		}
		
	}
	
	
	@Override
	protected void CreateButtonContinue() {
		buttonContinue = new DialogButton(
				Autickax.getInstance().assets
						.getGraphics(Constants.dialog.DECISION_DIALOG_BUTTON_CONTINUE_TEXTURE),
				Autickax.getInstance().assets
						.getGraphics(Constants.dialog.DECISION_DIALOG_BUTTON_CONTINUE_OVER_TEXTURE)) {

			@Override
			public void action() {
				status = DialogAbstractStatus.FINISHED;
				decision = DecisionType.CONTINUE;				
				subLevel2.onLevelComplete();
				endCommunication();
			}
		};
		buttonContinue.setPosition(
				Constants.dialog.DECISION_DIALOG_BUTTON_CONTINUE_POSITION_X
						- buttonContinue.getWidth() / 2,
				Constants.dialog.DECISION_DIALOG_BUTTON_CONTINUE_POSITION_Y
						- buttonContinue.getHeight() / 2);
		this.stage.addActor(buttonContinue);
	}
}
