package cz.mff.cuni.autickax.dialogs;

import java.text.DecimalFormat;

import cz.mff.cuni.autickax.Autickax;
import cz.mff.cuni.autickax.constants.Constants;
import cz.mff.cuni.autickax.gamelogic.GameStatistics;
import cz.mff.cuni.autickax.gamelogic.SubLevel2;
import cz.mff.cuni.autickax.scene.GameScreen;
import cz.mff.cuni.autickax.screenObjects.ScreenAdaptiveButton;
import cz.mff.cuni.autickax.screenObjects.ScreenAdaptiveImage;
import cz.mff.cuni.autickax.screenObjects.ScreenAdaptiveLabel;

public class CompleteLevelDialog extends DecisionDialog {

	GameStatistics stats;
	SubLevel2 subLevel2;

	public CompleteLevelDialog(GameScreen gameScreen, SubLevel2 subLevel2, GameStatistics stats,
			boolean isNextLevelAvailible) {
		super(gameScreen, subLevel2, null, isNextLevelAvailible);
		this.stats = stats;
		this.subLevel2 = subLevel2;

		createLabels();

		drawStars(stats.getNumberOfStars(), Constants.dialog.COMPLETE_DIALOG_STAR_POSITION_X,
				Constants.dialog.COMPLETE_DIALOG_STAR_POSITION_Y);
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
		ScreenAdaptiveImage gainedStar = new ScreenAdaptiveImage(
				Autickax.getInstance().assets.getGraphics(textureName));
		gainedStar.setPosition(x, (y + j * offset));
		stage.addActor(gainedStar);
	}

	private void createLabels() {
		ScreenAdaptiveLabel[][] labels = new ScreenAdaptiveLabel[6][2];

		labels[0][0] = ScreenAdaptiveLabel.getCompleteLevelDialogLabel("Difficulty:");
		labels[0][1] = ScreenAdaptiveLabel.getCompleteLevelDialogLabel(stats.getDifficulty()
				.toString());

		labels[1][0] = ScreenAdaptiveLabel.getCompleteLevelDialogLabel("Succeeded collisions:");
		labels[1][1] = ScreenAdaptiveLabel.getCompleteLevelDialogLabel(String.valueOf(stats
				.getSucceeded()));

		labels[2][0] = ScreenAdaptiveLabel.getCompleteLevelDialogLabel("Failed collisions:");
		labels[2][1] = ScreenAdaptiveLabel.getCompleteLevelDialogLabel(String.valueOf(stats
				.getFailed()));

		DecimalFormat decimalFormat = new DecimalFormat("0.#");
		labels[3][0] = ScreenAdaptiveLabel.getCompleteLevelDialogLabel("Time in first part:");
		labels[3][1] = ScreenAdaptiveLabel.getCompleteLevelDialogLabel(decimalFormat.format(stats
				.getPhase1ElapsedTime()) + '/' + decimalFormat.format(stats.getPhase1TimeLimit()));

		labels[4][0] = ScreenAdaptiveLabel.getCompleteLevelDialogLabel("Time in second part:");
		labels[4][1] = ScreenAdaptiveLabel.getCompleteLevelDialogLabel(decimalFormat.format(stats
				.getPhase2ElapsedTime()));

		labels[5][0] = ScreenAdaptiveLabel.getCompleteLevelDialogLabel("Score:");
		labels[5][1] = ScreenAdaptiveLabel.getCompleteLevelDialogLabel(Integer.toString(stats
				.getScoreFromTime()));

		for (int row = 0; row < labels.length; row++) {
			labels[row][0]
					.setPosition(Constants.dialog.COMPLETE_DIALOG_MESSAGE_POSITION_X
							- Constants.dialog.COMPLETE_DIALOG_MESSAGE_WIDTH / 2,
							Constants.dialog.COMPLETE_DIALOG_MESSAGE_POSITION_Y
									+ Constants.dialog.COMPLETE_DIALOG_MESSAGE_HEIGHT / 2
									- Constants.dialog.COMPLETE_DIALOG_MESSAGE_HEIGHT
									/ labels.length * row);
			this.stage.addActor(labels[row][0]);

			labels[row][1]
					.setPosition(
							Constants.dialog.COMPLETE_DIALOG_MESSAGE_POSITION_X
									+ Constants.dialog.COMPLETE_DIALOG_MESSAGE_WIDTH / 2
									- labels[row][1].getWidth(),
							Constants.dialog.COMPLETE_DIALOG_MESSAGE_POSITION_Y
									+ Constants.dialog.COMPLETE_DIALOG_MESSAGE_HEIGHT / 2
									- Constants.dialog.COMPLETE_DIALOG_MESSAGE_HEIGHT
									/ labels.length * row);
			this.stage.addActor(labels[row][1]);
		}

	}

	@Override
	protected void CreateButtonContinue() {
		buttonContinue = new ScreenAdaptiveButton(
				Autickax.getInstance().assets
						.getGraphics(Constants.dialog.DECISION_DIALOG_BUTTON_CONTINUE_TEXTURE),
				Autickax.getInstance().assets
						.getGraphics(Constants.dialog.DECISION_DIALOG_BUTTON_CONTINUE_OVER_TEXTURE)) {

			@Override
			public void action() {
				status = DialogAbstractStatus.FINISHED;
				decision = DecisionType.CONTINUE;
				subLevel2.onLevelComplete();				
				dispose();
			}
		};
		buttonContinue.setCenterPosition(
				Constants.dialog.DECISION_DIALOG_BUTTON_CONTINUE_POSITION_X,
				Constants.dialog.DECISION_DIALOG_BUTTON_CONTINUE_POSITION_Y);
		this.stage.addActor(buttonContinue);
	}
}
