package cz.cuni.mff.autickax.dialogs;

import java.text.DecimalFormat;

import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

import cz.cuni.mff.autickax.Autickax;
import cz.cuni.mff.autickax.constants.Constants;
import cz.cuni.mff.autickax.gamelogic.GameStatistics;
import cz.cuni.mff.autickax.gamelogic.SubLevel2;
import cz.cuni.mff.autickax.scene.GameScreen;
import cz.cuni.mff.autickax.screenObjects.ScreenAdaptiveButton;
import cz.cuni.mff.autickax.screenObjects.ScreenAdaptiveImage;
import cz.cuni.mff.autickax.screenObjects.ScreenAdaptiveLabel;

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

	public CompleteLevelDialog(GameScreen gameScreen, SubLevel2 subLevel2, GameStatistics stats,
			boolean isNextLevelAvailible) {
		super(gameScreen, subLevel2, null, isNextLevelAvailible);
		this.stats = stats;
		this.subLevel2 = subLevel2;
		createLabels();

		initializeStars(Constants.dialog.COMPLETE_DIALOG_STAR_POSITION_X,
				Constants.dialog.COMPLETE_DIALOG_STAR_POSITION_Y);
	}

	private void initializeStars(int x, int y) {
		this.starsCount = stats.getNumberOfStars();
		filledStarDrawable = new TextureRegionDrawable(
				Autickax.getInstance().assets
						.getGraphics(Constants.dialog.COMPLETE_DIALOG_STAR_FILLED_TEXTURE));
		for (byte j = 0; j < 3; ++j) {
			ScreenAdaptiveImage gainedStar = new ScreenAdaptiveImage(
					Autickax.getInstance().assets
							.getGraphics(Constants.dialog.COMPLETE_DIALOG_STAR_EMPTY_TEXTURE));
			gainedStar.setPosition(x, (y + j * offset));
			this.stars[j] = gainedStar;
			stage.addActor(gainedStar);
		}
	}

	float starsXStart = 600;
	float starsY = 200;
	float offset = 80;

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

		labels[3][0] = ScreenAdaptiveLabel.getCompleteLevelDialogLabel("Time in first part:");
		labels[3][1] = ScreenAdaptiveLabel.getCompleteLevelDialogLabel(decimalFormat.format(stats
				.getPhase1ElapsedTime()) + '/' + decimalFormat.format(stats.getPhase1TimeLimit()));

		labels[4][0] = ScreenAdaptiveLabel.getCompleteLevelDialogLabel("Time in second part:");
		timeLabel = labels[4][1] = ScreenAdaptiveLabel.getCompleteLevelDialogLabel(decimalFormat2
				.format(stats.getPhase2ElapsedTime()));

		labels[5][0] = ScreenAdaptiveLabel.getCompleteLevelDialogLabel("Score:");
		this.score = stats.getScoreFromTime();
		scoreLabel = labels[5][1] = ScreenAdaptiveLabel.getCompleteLevelDialogLabel(Integer
				.toString(this.score));

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

	float dialogElapsedTime = 0;
	int starIndex = 1;

	@Override
	public void update(float delta) {
		super.update(delta);

		this.dialogElapsedTime += delta;

		if (dialogElapsedTime <= ANIMATION_DURATION) {
			float progress = this.dialogElapsedTime / ANIMATION_DURATION;

			float elapsedTime = progress * stats.getPhase2ElapsedTime();
			this.timeLabel.setText(decimalFormat2.format(elapsedTime));

			this.scoreLabel.setText(Integer.toString((int) (progress * this.score)));

			if (progress > (float) this.starIndex / (this.starsCount + 1)) {
				Autickax.getInstance().assets.soundAndMusicManager.playSound(
						Constants.sounds.SOUND_FINISH_DIALOG_STAR_PREFIX + this.starIndex, 1);
				this.stars[this.starIndex - 1].setDrawable(this.filledStarDrawable);
				this.starIndex++;
			}
		}

	}
}
