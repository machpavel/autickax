package cz.mff.cuni.autickax.miniGames;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Slider.SliderStyle;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

import cz.mff.cuni.autickax.Autickax;
import cz.mff.cuni.autickax.Difficulty;
import cz.mff.cuni.autickax.constants.Constants;
import cz.mff.cuni.autickax.dialogs.MessageDialog;
import cz.mff.cuni.autickax.exceptions.IllegalDifficultyException;
import cz.mff.cuni.autickax.gamelogic.SubLevel;
import cz.mff.cuni.autickax.input.Input;
import cz.mff.cuni.autickax.miniGames.support.SwitchingMinigameButton;
import cz.mff.cuni.autickax.scene.GameScreen;

public final class SwitchingMinigame extends Minigame {

	private static final float progressStep = 5.f;

	private States state = States.BEGINNING_STATE;
	private Button[] buttons = new Button[2];
	private Slider progressBar;
	private float opponentSpeed;

	public SwitchingMinigame(GameScreen screen, SubLevel parent) {
		super(screen, parent);
		setDifficulty(this.level.getDifficulty());
		this.backgroundTexture = new TextureRegionDrawable(
				Autickax.getInstance().assets
						.getGraphics(Constants.minigames.SWITCHING_MINIGAME_BACKGROUND_TEXTURE));

		if (Autickax.settings.showTooltips)
			this.parent.setDialog(new MessageDialog(screen, parent,
					Constants.strings.TOOLTIP_MINIGAME_SWITCHING_WHAT_TO_DO));
		createButtons();
		createProgressBar();
	}

	private void createProgressBar() {
		SliderStyle style = new SliderStyle();
		// TODO Figure out how to show the progress
		style.background = new TextureRegionDrawable(
				Autickax.getInstance().assets
						.getGraphics(Constants.minigames.SWITCHING_MINIGAME_SLIDER_BACKGROUND_TEXTURE));
		style.knob = new TextureRegionDrawable(
				Autickax.getInstance().assets
						.getGraphics(Constants.minigames.SWITCHING_MINIGAME_SLIDER_KNOB_TEXTURE));
		progressBar = new Slider(0, 100, 0.001f, false, style);
		progressBar.setDisabled(true);
		progressBar.setPosition(400 * Input.xStretchFactorInv - progressBar.getWidth() / 2,
				420 * Input.yStretchFactorInv);
		progressBar.setValue(50);
		this.stage.addActor(progressBar);
	}

	private void createButtons() {
		ButtonStyle style = new ButtonStyle();
		style.up = new TextureRegionDrawable(
				Autickax.getInstance().assets
						.getGraphics(Constants.minigames.SWITCHING_MINIGAME_BUTTON_TEXTURE));
		style.disabled = new TextureRegionDrawable(
				Autickax.getInstance().assets
						.getGraphics(Constants.minigames.SWITCHING_MINIGAME_DISABLED_BUTTON_TEXTURE));
		for (int i = 0; i < buttons.length; i++) {
			SwitchingMinigameButton newButton = new SwitchingMinigameButton(style, this);
			newButton.setDisabled(true);
			newButton.setCenterPosition(Constants.dialog.DIALOG_WORLD_X_OFFSET
					+ Constants.dialog.DIALOG_WORLD_WIDTH / 4 + Constants.dialog.DIALOG_WORLD_WIDTH
					* i / 2, Constants.dialog.DIALOG_WORLD_Y_OFFSET
					+ Constants.dialog.DIALOG_WORLD_HEIGHT / 2);
			stage.addActor(newButton);
			buttons[i] = newButton;
		}
		buttons[MathUtils.random(buttons.length - 1)].setDisabled(false);
	}

	public void switchButtons() {
		for (int i = 0; i < buttons.length; i++) {
			buttons[i].setDisabled(!buttons[i].isDisabled());
		}
		float nextProgressBarValue = this.progressBar.getValue() + progressStep;
		if (nextProgressBarValue >= 100)
			this.state = States.FINISH_STATE;
		this.progressBar.setValue(nextProgressBarValue);
	}

	@Override
	public void update(float delta) {
		switch (state) {
		case BEGINNING_STATE:
			updateInBeginnigState(delta);
			break;
		case NORMAL_STATE:
			updateInNormalState(delta);
			break;
		case FINISH_STATE:
			updateInFinishState(delta);
			break;
		default:
			throw new IllegalStateException(state.toString());
		}

	}

	private void updateInBeginnigState(float delta) {
		this.takeFocus();
		this.state = States.NORMAL_STATE;
	}

	private void updateInNormalState(float delta) {
		float nextProgressBarValue = this.progressBar.getValue() - delta * opponentSpeed;
		if (nextProgressBarValue <= 0)
			fail();
		else
			this.progressBar.setValue(nextProgressBarValue);

	}

	private void fail() {
		this.resultMessage = Constants.strings.TOOLTIP_MINIGAME_SWITCHING_FAIL;
		this.result = ResultType.FAILED;
		// this.resultValue = 1; // nothing happens
		parent.onMinigameEnded();
		this.endCommunication();
	}

	private void updateInFinishState(float delta) {
		this.resultMessage = Constants.strings.TOOLTIP_MINIGAME_SWITCHING_SUCCESS;
		this.status = DialogAbstractStatus.FINISHED;
		this.result = ResultType.PROCEEDED;
		// this.resultValue = RESULT_WIN_VALUE;
		parent.onMinigameEnded();
		this.endCommunication();
	}

	@Override
	public void draw(SpriteBatch batch) {
		super.draw(batch);
	}

	private void setDifficulty(Difficulty difficulty) {
		switch (difficulty) {
		case Kiddie:
			opponentSpeed = Constants.minigames.SWITCHING_MINIGAME_OPPONENT_SPEED_KIDDIE;
			break;
		case Beginner:
			opponentSpeed = Constants.minigames.SWITCHING_MINIGAME_OPPONENT_SPEED_BEGINNER;
			break;
		case Normal:
			opponentSpeed = Constants.minigames.SWITCHING_MINIGAME_OPPONENT_SPEED_NORMAL;
			break;
		case Hard:
			opponentSpeed = Constants.minigames.SWITCHING_MINIGAME_OPPONENT_SPEED_HARD;
			break;
		case Extreme:
			opponentSpeed = Constants.minigames.SWITCHING_MINIGAME_OPPONENT_SPEED_EXTREME;
			break;
		default:
			throw new IllegalDifficultyException(difficulty.toString());
		}
		return;
	}

	private enum States {
		BEGINNING_STATE, NORMAL_STATE, FINISH_STATE;
	}
}
