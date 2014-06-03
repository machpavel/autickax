package cz.cuni.mff.autickax.miniGames;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;

import cz.cuni.mff.autickax.Autickax;
import cz.cuni.mff.autickax.Difficulty;
import cz.cuni.mff.autickax.constants.Constants;
import cz.cuni.mff.autickax.dialogs.MessageDialog;
import cz.cuni.mff.autickax.exceptions.IllegalDifficultyException;
import cz.cuni.mff.autickax.gamelogic.SubLevel;
import cz.cuni.mff.autickax.miniGames.support.BoostMinigameButton;
import cz.cuni.mff.autickax.scene.GameScreen;

public final class BoostMinigame extends Minigame {

	private final float RESULT_WIN_VALUE = Constants.minigames.BOOST_MINIGAME_WIN_VALUE;
	private final float TIME_GENERATION_LIMIT = Constants.minigames.BOOST_MINIGAME_TIME_GENERATION_LIMIT;

	private States state = States.BEGINNING_STATE;
	private int columnCount;
	private int rowsCount;
	private float remainingTime;
	private float timeLimit;
	private int hitCounter = 0;
	private int hitsLimit;

	private BoostMinigameButton[] buttonsWaiting;
	private BoostMinigameButton[] buttonsHighlighted;
	private boolean buttonsAreEnabled = true;

	public BoostMinigame(GameScreen screen, SubLevel parent) {
		super(screen, parent);
		setDifficulty(this.level.getDifficulty());
		this.backgroundTexture = new NinePatchDrawable(
				Autickax.getInstance().assets
						.getNinePatch(Constants.minigames.BOOST_MINIGAME_BACKGROUND_TEXTURE));

		if (Autickax.settings.isShowTooltips())
			this.parent.setDialog(new MessageDialog(screen, parent,
					Constants.strings.TOOLTIP_MINIGAME_BOOST_WHAT_TO_DO));

		createButtons();
	}

	public void createButtons() {
		buttonsWaiting = new BoostMinigameButton[rowsCount * columnCount];
		buttonsHighlighted = new BoostMinigameButton[rowsCount * columnCount];
		Vector2 midPosition = new Vector2(Constants.WORLD_WIDTH / 2, Constants.WORLD_HEIGHT / 2);
		Vector2 originPosition = null;
		for (int row = 0; row < this.rowsCount; row++) {
			for (int column = 0; column < this.columnCount; column++) {
				BoostMinigameButton newWaitingButton = new BoostMinigameButton(
						this.columnCount * row + column,
						this,
						Autickax.getInstance().assets
								.getGraphics(Constants.minigames.BOOST_MINIGAME_WAITING_BUTTON_TEXTURE));
				buttonsWaiting[this.columnCount * row + column] = newWaitingButton;
				this.stage.addActor(newWaitingButton);

				BoostMinigameButton newHighlightedButton = new BoostMinigameButton(
						this.columnCount * row + column,
						this,
						Autickax.getInstance().assets
								.getGraphics(Constants.minigames.BOOST_MINIGAME_HIGHLIGHTED_BUTTON_TEXTURE));
				buttonsHighlighted[this.columnCount * row + column] = newHighlightedButton;
				newHighlightedButton.setVisible(false);
				this.stage.addActor(newHighlightedButton);

				if (originPosition == null) {
					originPosition = new Vector2(midPosition.x - newWaitingButton.getWidth()
							* (float) columnCount / 2, midPosition.y - newWaitingButton.getHeight()
							* (float) rowsCount / 2);
				}
				newHighlightedButton.setPosition(
						originPosition.x + column * newWaitingButton.getWidth(), originPosition.y
								+ row * newHighlightedButton.getHeight());
				newWaitingButton.setPosition(newHighlightedButton.getX(),
						newHighlightedButton.getY());
			}
		}
	}

	public void buttonPressed(int index) {
		if (buttonsAreEnabled) {
			if (buttonsHighlighted[index].isVisible()) {
				this.state = States.SET_PREPARATION_TIME_STATE;
				reverseButton(index);
				this.hitCounter++;
				if (this.hitCounter >= this.hitsLimit)
					win();

			} else {
				fail(Constants.strings.TOOLTIP_MINIGAME_BOOST_WRONG_HIT_FAIL);
			}
		}
	}

	private void reverseButton(int index) {
		buttonsHighlighted[index].setVisible(!buttonsHighlighted[index].isVisible());
		buttonsWaiting[index].setVisible(!buttonsWaiting[index].isVisible());
	}

	@Override
	public void update(float delta) {
		switch (state) {
		case BEGINNING_STATE:
			updateInBeginnigState(delta);
			break;
		case SET_PREPARATION_TIME_STATE:
			setPreparationTime();
			break;
		case PREPARATION_STATE:
			updateInPreparationState(delta);
			break;
		case SET_NORMAL_TIME_STATE:
			setNormalTime();
			break;
		case NORMAL_STATE:
			updateInDrivingState(delta);
			break;
		case LEAVING_STATE:
			updateInLeavingState(delta);
			break;
		default:
			throw new IllegalStateException(state.toString());
		}

	}

	private void setPreparationTime() {
		this.remainingTime = this.TIME_GENERATION_LIMIT;
		this.state = States.PREPARATION_STATE;
	}

	private void setNormalTime() {
		this.remainingTime = this.timeLimit;
		this.state = States.NORMAL_STATE;
	}

	private void updateInBeginnigState(float delta) {
		this.takeFocus();
		this.state = States.SET_PREPARATION_TIME_STATE;
	}

	private void updateInPreparationState(float delta) {
		this.remainingTime -= delta;
		if (this.remainingTime < 0) {
			int index = MathUtils.random(buttonsWaiting.length - 1);
			reverseButton(index);
			this.state = States.SET_NORMAL_TIME_STATE;
		}
	}

	private void updateInDrivingState(float delta) {
		this.remainingTime -= delta;
		if (this.remainingTime < 0) {
			fail(Constants.strings.TOOLTIP_MINIGAME_BOOST_TIME_LIMIT_FAIL);
		}
	}

	private void fail(String message) {
		this.resultMessage = message;
		this.result = ResultType.FAILED_WITH_VALUE;
		this.resultValue = Constants.minigames.RESULT_VALUE_NOTHING;
		leave();
	}

	private void win() {
		this.resultMessage = Constants.strings.TOOLTIP_MINIGAME_BOOST_SUCCESS;
		this.result = ResultType.PROCEEDED_WITH_VALUE;
		this.resultValue = RESULT_WIN_VALUE;
		leave();
	}

	private void leave() {
		this.playResultSound();
		this.buttonsAreEnabled = false;
		this.state = States.LEAVING_STATE;
	}

	private void setDifficulty(Difficulty difficulty) {
		switch (difficulty) {
		case Kiddie:
			this.rowsCount = Constants.minigames.BOOST_MINIGAME_ROWS_KIDDIE;
			this.columnCount = Constants.minigames.BOOST_MINIGAME_COLUMNS_KIDDIE;
			this.timeLimit = Constants.minigames.BOOST_MINIGAME_TIME_LIMIT_KIDDIE;
			this.hitsLimit = Constants.minigames.BOOST_MINIGAME_HITS_LIMIT_KIDDIE;
			break;
		case Beginner:
			this.rowsCount = Constants.minigames.BOOST_MINIGAME_ROWS_BEGINNER;
			this.columnCount = Constants.minigames.BOOST_MINIGAME_COLUMNS_BEGINNER;
			this.timeLimit = Constants.minigames.BOOST_MINIGAME_TIME_LIMIT_BEGINNER;
			this.hitsLimit = Constants.minigames.BOOST_MINIGAME_HITS_LIMIT_BEGINNER;
			break;
		case Normal:
			this.rowsCount = Constants.minigames.BOOST_MINIGAME_ROWS_NORMAL;
			this.columnCount = Constants.minigames.BOOST_MINIGAME_COLUMNS_NORMAL;
			this.timeLimit = Constants.minigames.BOOST_MINIGAME_TIME_LIMIT_NORMAL;
			this.hitsLimit = Constants.minigames.BOOST_MINIGAME_HITS_LIMIT_NORMAL;
			break;
		case Hard:
			this.rowsCount = Constants.minigames.BOOST_MINIGAME_ROWS_HARD;
			this.columnCount = Constants.minigames.BOOST_MINIGAME_COLUMNS_HARD;
			this.timeLimit = Constants.minigames.BOOST_MINIGAME_TIME_LIMIT_HARD;
			this.hitsLimit = Constants.minigames.BOOST_MINIGAME_HITS_LIMIT_HARD;
			break;
		case Extreme:
			this.rowsCount = Constants.minigames.BOOST_MINIGAME_ROWS_EXTREME;
			this.columnCount = Constants.minigames.BOOST_MINIGAME_COLUMNS_EXTREME;
			this.timeLimit = Constants.minigames.BOOST_MINIGAME_TIME_LIMIT_EXTREME;
			this.hitsLimit = Constants.minigames.BOOST_MINIGAME_HITS_LIMIT_EXTREME;
			break;
		default:
			throw new IllegalDifficultyException(difficulty.toString());
		}
		return;
	}

	private enum States {
		BEGINNING_STATE, SET_PREPARATION_TIME_STATE, PREPARATION_STATE, SET_NORMAL_TIME_STATE, NORMAL_STATE, LEAVING_STATE;
	}
}
