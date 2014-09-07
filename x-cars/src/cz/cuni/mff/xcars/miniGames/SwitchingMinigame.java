package cz.cuni.mff.xcars.miniGames;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Slider.SliderStyle;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

import cz.cuni.mff.xcars.Xcars;
import cz.cuni.mff.xcars.Difficulty;
import cz.cuni.mff.xcars.constants.Constants;
import cz.cuni.mff.xcars.dialogs.MessageDialog;
import cz.cuni.mff.xcars.exceptions.IllegalDifficultyException;
import cz.cuni.mff.xcars.gamelogic.SubLevel;
import cz.cuni.mff.xcars.miniGames.support.SwitchingMinigameButton;
import cz.cuni.mff.xcars.scene.GameScreen;

public final class SwitchingMinigame extends Minigame {

	private static final float progressBarXPosition = 400;
	private static final float progressBarYPosition = 420;
	private static final float progressStep = 5.f;

	private static final float RESULT_WIN_VALUE = Constants.minigames.SWITCHING_MINIGAME_WIN_VALUE;
	private static final float RESULT_FAIL_VALUE = Constants.minigames.SWITCHING_MINIGAME_FAIL_VALUE;

	private States state = States.BEGINNING_STATE;
	private Button[] buttons = new Button[2];
	private ProgressBar progressBar;
	private float opponentSpeed;

	public SwitchingMinigame(GameScreen screen, SubLevel parent) {
		super(screen, parent);
		setDifficulty(this.level.getDifficulty());
		this.backgroundTexture = new NinePatchDrawable(
				Xcars.getInstance().assets
						.getNinePatch(Constants.minigames.SWITCHING_MINIGAME_BACKGROUND_TEXTURE));

		if (Xcars.settings.isShowTooltips())
			this.parent.setDialog(new MessageDialog(screen, parent,
					Constants.strings.TOOLTIP_MINIGAME_SWITCHING_WHAT_TO_DO));
		createButtons();
		createProgressBar();
	}

	private void createProgressBar() {
		SliderStyle style = new SliderStyle();
		// TODO Figure out how to show the progress
		style.background = new TextureRegionDrawable(
				Xcars.getInstance().assets
						.getGraphics(Constants.minigames.SWITCHING_MINIGAME_SLIDER_BACKGROUND_TEXTURE));
		style.knob = new TextureRegionDrawable(
				Xcars.getInstance().assets
						.getGraphics(Constants.minigames.SWITCHING_MINIGAME_SLIDER_KNOB_TEXTURE));
		progressBar = new ProgressBar(0, 100, 0.001f, false, style);
		progressBar.setPosition(progressBarXPosition - progressBar.getWidth() / 2,
				progressBarYPosition);
		progressBar.setValue(50);
		this.stage.addActor(progressBar);
	}

	private void createButtons() {
		for (int i = 0; i < buttons.length; i++) {
			ButtonStyle style = new ButtonStyle();
			style.up = new TextureRegionDrawable(
					Xcars.getInstance().assets
							.getGraphics(Constants.minigames.SWITCHING_MINIGAME_BUTTON_TEXTURE + i));
			style.disabled = new TextureRegionDrawable(
					Xcars.getInstance().assets
							.getGraphics(Constants.minigames.SWITCHING_MINIGAME_DISABLED_BUTTON_TEXTURE + i));
			
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
			win();
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
		case LEAVING_STATE:
			updateInLeavingState(delta);
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
		this.resultValue = RESULT_FAIL_VALUE;
		leave();
	}

	private void win() {
		this.resultMessage = Constants.strings.TOOLTIP_MINIGAME_SWITCHING_SUCCESS;
		this.result = ResultType.PROCEEDED;
		this.resultValue = RESULT_WIN_VALUE;
		leave();
	}

	private void leave() {
		this.playResultSound();
		for (int i = 0; i < buttons.length; i++) {
			buttons[i].setDisabled(true);
		}
		this.state = States.LEAVING_STATE;
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
		BEGINNING_STATE, NORMAL_STATE, LEAVING_STATE;
	}
}
