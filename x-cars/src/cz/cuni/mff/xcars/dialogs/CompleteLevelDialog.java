package cz.cuni.mff.xcars.dialogs;

import java.text.DecimalFormat;

import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

import cz.cuni.mff.xcars.Xcars;
import cz.cuni.mff.xcars.constants.Constants;
import cz.cuni.mff.xcars.gamelogic.GameStatistics;
import cz.cuni.mff.xcars.gamelogic.SubLevel2;
import cz.cuni.mff.xcars.scene.GameScreen;
import cz.cuni.mff.xcars.screenObjects.ScreenAdaptiveButton;
import cz.cuni.mff.xcars.screenObjects.ScreenAdaptiveImage;
import cz.cuni.mff.xcars.screenObjects.ScreenAdaptiveLabel;

public class CompleteLevelDialog extends DecisionDialog {

	GameStatistics stats;
	SubLevel2 subLevel2;
	ScreenAdaptiveLabel scoreLabel, timeLabel;

	float ANIMATION_DURATION = 4;
	DecimalFormat decimalFormat = new DecimalFormat("0.#");
	DecimalFormat decimalFormat2 = new DecimalFormat("0.00");
	int score;
	ScreenAdaptiveImage[] stars = new ScreenAdaptiveImage[3];
	Drawable filledStarDrawable;
	byte starsCount;

	public CompleteLevelDialog(GameScreen gameScreen, SubLevel2 subLevel2,
			GameStatistics stats, boolean isNextLevelAvailible) {
		super(gameScreen, subLevel2, null, isNextLevelAvailible);
		this.stats = stats;
		this.subLevel2 = subLevel2;
		createLabels();

		initializeStars(Constants.dialog.COMPLETE_DIALOG_STAR_POSITION_X,
				Constants.dialog.COMPLETE_DIALOG_STAR_POSITION_Y);

		initializeCar(Constants.dialog.COMPLETE_DIALOG_CAR_POSITION_X,
				Constants.dialog.COMPLETE_DIALOG_CAR_POSITION_Y);
	}

	private void initializeStars(int x, int y) {
		this.starsCount = stats.getNumberOfStars();
		filledStarDrawable = new TextureRegionDrawable(
				Xcars.getInstance().assets
						.getGraphics(Constants.dialog.COMPLETE_DIALOG_STAR_FILLED_TEXTURE));
		for (byte j = 0; j < 3; ++j) {
			ScreenAdaptiveImage gainedStar = new ScreenAdaptiveImage(
					Xcars.getInstance().assets
							.getGraphics(Constants.dialog.COMPLETE_DIALOG_STAR_EMPTY_TEXTURE));
			gainedStar.setPosition(x + (j * offset), y);
			this.stars[j] = gainedStar;
			stage.addActor(gainedStar);
		}
	}

	private void initializeCar(int x, int y) {
		int tresholds[] = Constants.dialog.COMPLETE_DIALOG_CAR_RESULT_TRESHOLDS;
		int carIndex = 0;
		for (int i = 0; i < tresholds.length; i++) {
			if (stats.getFailed() >= tresholds[i])
				carIndex = i + 1;
			else
				break;
		}
		ScreenAdaptiveImage carImage = new ScreenAdaptiveImage(
				Xcars.getInstance().assets
						.getGraphics(Constants.dialog.COMPLETE_DIALOG_CAR_RESULT_TEXTURE_PREFIX
								+ String.valueOf(carIndex)));
		carImage.setCenterPosition(x, y);
		stage.addActor(carImage);
	}

	float offset = 80;

	private void createLabels() {
		final int LABELS_ROW_COUNT = 5;
		final int LABELS_COLUMN_COUNT = 2;
		
		ScreenAdaptiveLabel[][] labels = new ScreenAdaptiveLabel[LABELS_ROW_COUNT][LABELS_COLUMN_COUNT];

		labels[0][0] = ScreenAdaptiveLabel.getCompleteLevelDialogLabel(stats
				.getDifficulty().toString());
		labels[0][1] = null;

		labels[1][0] = ScreenAdaptiveLabel.getCompleteLevelDialogLabel("Level "
				+ String.valueOf(stats.getLevelIndex() + 1));
		labels[1][1] = null;

		labels[2][0] = ScreenAdaptiveLabel
				.getCompleteLevelDialogLabel("Collisions:");
		labels[2][1] = ScreenAdaptiveLabel.getCompleteLevelDialogLabel("+"
				+ String.valueOf(stats.getSucceeded()) + "/-"
				+ String.valueOf(stats.getFailed()));

		labels[3][0] = ScreenAdaptiveLabel.getCompleteLevelDialogLabel("Time:");
		timeLabel = labels[3][1] = ScreenAdaptiveLabel
				.getCompleteLevelDialogLabel(decimalFormat2.format(stats
						.getPhase2ElapsedTime())
						+ " ("
						+ decimalFormat.format(stats.getPhase1TimeLimit())
						+ ')');

		labels[4][0] = ScreenAdaptiveLabel
				.getCompleteLevelDialogLabel("Score:");
		this.score = stats.getScoreFromTime();
		scoreLabel = labels[4][1] = ScreenAdaptiveLabel
				.getCompleteLevelDialogLabel(Integer.toString(this.score));

		for (int row = 0; row < LABELS_ROW_COUNT; ++row) {
			for (int column = 0; column < LABELS_COLUMN_COUNT; ++column) {
				if (labels[row][column] != null) {
					labels[row][column].setColor(Constants.dialog.DIALOG_FONT_COLOR);
				}
			}
		}
		
		for (int row = 0; row < LABELS_ROW_COUNT; ++row) {
			labels[row][0].setPosition(
					Constants.dialog.COMPLETE_DIALOG_MESSAGE_POSITION_X
							- Constants.dialog.COMPLETE_DIALOG_MESSAGE_WIDTH
							/ 2,
					Constants.dialog.COMPLETE_DIALOG_MESSAGE_POSITION_Y
							+ Constants.dialog.COMPLETE_DIALOG_MESSAGE_HEIGHT
							/ 2
							- Constants.dialog.COMPLETE_DIALOG_MESSAGE_HEIGHT
							/ labels.length * row);
			this.stage.addActor(labels[row][0]);

			if (labels[row][1] != null) {
				labels[row][1]
						.setPosition(
								Constants.dialog.COMPLETE_DIALOG_MESSAGE_POSITION_X
										+ Constants.dialog.COMPLETE_DIALOG_MESSAGE_WIDTH
										/ 2 - labels[row][1].getWidth(),
								Constants.dialog.COMPLETE_DIALOG_MESSAGE_POSITION_Y
										+ Constants.dialog.COMPLETE_DIALOG_MESSAGE_HEIGHT
										/ 2
										- Constants.dialog.COMPLETE_DIALOG_MESSAGE_HEIGHT
										/ labels.length * row);
				this.stage.addActor(labels[row][1]);
			}
		}

	}

	@Override
	protected void CreateButtonContinue() {
		buttonContinue = new ScreenAdaptiveButton(
				Xcars.getInstance().assets
						.getGraphics(Constants.dialog.DECISION_DIALOG_BUTTON_CONTINUE_TEXTURE),
				Xcars.getInstance().assets
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

	float dialogElapsedTime = 0;
	int starIndex = 1;

	@Override
	public void update(float delta) {
		super.update(delta);

		this.dialogElapsedTime += delta;

		if (dialogElapsedTime <= ANIMATION_DURATION) {
			float progress = this.dialogElapsedTime / ANIMATION_DURATION;

			float elapsedTime = progress * stats.getPhase2ElapsedTime();
			this.timeLabel.setText(decimalFormat2.format(elapsedTime) + " ("
					+ decimalFormat.format(stats.getPhase1TimeLimit()) + ')');

			this.scoreLabel.setText(Integer
					.toString((int) (progress * this.score)));

			if (progress > (float) this.starIndex / (this.starsCount + 1)) {
				this.soundsManager
						.playSound(Constants.sounds.SOUND_FINISH_DIALOG_STAR_PREFIX
								+ this.starIndex
								+ Constants.sounds.SOUND_FINISH_DIALOG_STAR_POSTFIX);
				this.stars[this.starIndex - 1]
						.setDrawable(this.filledStarDrawable);
				this.starIndex++;
			}
		}

	}
}
