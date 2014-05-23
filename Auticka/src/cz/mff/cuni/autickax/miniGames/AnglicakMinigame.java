package cz.mff.cuni.autickax.miniGames;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;

import cz.mff.cuni.autickax.Autickax;
import cz.mff.cuni.autickax.Difficulty;
import cz.mff.cuni.autickax.constants.Constants;
import cz.mff.cuni.autickax.dialogs.MessageDialog;
import cz.mff.cuni.autickax.entities.Car;
import cz.mff.cuni.autickax.exceptions.IllegalDifficultyException;
import cz.mff.cuni.autickax.gamelogic.SubLevel;
import cz.mff.cuni.autickax.input.Input;
import cz.mff.cuni.autickax.miniGames.support.AnglicakMinigameTarget;
import cz.mff.cuni.autickax.scene.GameScreen;

public final class AnglicakMinigame extends Minigame {
	private static final float CAR_START_POSITION_X = Constants.minigames.ANGLICAK_MINIGAME_CAR_START_POSITION_X;
	private static final float CONTROL_LINE_POSITION_X = Constants.minigames.ANGLICAK_MINIGAME_CONTROL_LINE_POSITION_X;
	private static final float MAX_WIN_COEF = Constants.minigames.ANGLICAK_MINIGAME_MAX_WIN_VALUE;

	private float numberOfTries = Constants.minigames.ANGLICAK_MINIGAME_TRIES_COUNT;
	private float uniformDeceleration = Constants.minigames.ANGLICAK_MINIGAME_UNIFORM_DECELERATION;

	// TODO Modify the wind randomly and show the wind.
	private Vector2 wind = new Vector2(0, 2000);

	// From how many changes should be counted a speed
	private static final int averageCount = 5;

	// Target
	private float targetRadius;
	private static final int targetLocationX = Constants.minigames.ANGLICAK_MINIGAME_TARGET_POSITION_X;
	private static final int targetLocationY = Constants.minigames.ANGLICAK_MINIGAME_TARGET_POSITION_Y;;
	private AnglicakMinigameTarget target;

	// Car
	private Car car;
	private boolean carWasDragged = false;
	private Vector2 lastCarPosition;
	private Vector2 carSpeed;
	// Field for counting a speed
	private Vector2[] carSpeeds = new Vector2[averageCount];
	// Field for counting a normalized speed
	private float[] carSpeedsDeltas = new float[averageCount];
	// Index in speed fields for next save of Speed and Delta
	private int carSpeedsIndex = 0;

	States state;

	public AnglicakMinigame(GameScreen gameScreen, SubLevel parent) {
		super(gameScreen, parent);

		setDifficulty(this.level.getDifficulty());

		this.backgroundTexture = new NinePatchDrawable(
				Autickax.getInstance().assets
						.getNinePatch(Constants.minigames.ANGLICAK_MINIGAME_BACKGROUND_TEXTURE));

		if (Autickax.settings.isShowTooltips())
			this.parent.setDialog(new MessageDialog(gameScreen, parent,
					Constants.strings.TOOLTIP_MINIGAME_ANGLICAK_WHAT_TO_DO));

		this.target = new AnglicakMinigameTarget(targetLocationX, targetLocationY, targetRadius);
		this.stage.addActor(this.target);

		this.car = new Car(0, 0, 1);

		restartForNewTry();
	}

	private void restartForNewTry() {
		this.car.reset();
		this.car.setPosition(new Vector2(CAR_START_POSITION_X, Constants.WORLD_HEIGHT / 2));
		carWasDragged = false;

		this.state = States.BEGINNING_STATE;

		carSpeeds = new Vector2[5];
		for (int i = 0; i < carSpeeds.length; i++) {
			carSpeeds[i] = new Vector2(0, 0);
		}

		for (int i = 0; i < carSpeedsDeltas.length; i++) {
			carSpeedsDeltas[i] = 0.f;
		}
	}

	@Override
	public void update(float delta) {
		this.car.update(delta);
		switch (state) {
		case BEGINNING_STATE:
			updateInBeginnigState(delta);
			break;
		case DRIVING_STATE:
			updateInDrivingState(delta);
			break;
		case LEAVING_STATE:
			updateInLeavingState(delta);
			break;
		default:
			throw new IllegalStateException(state.toString());
		}
	}

	private void updateInBeginnigState(float delta) {
		if (Gdx.input.justTouched()) {
			Vector2 touchPos = new Vector2(Input.getX(), Input.getY());
			Vector2 shift = new Vector2(this.car.getPosition()).sub(touchPos.x, touchPos.y);
			if (shift.len() <= Constants.misc.CAR_CAPABLE_DISTANCE) {
				this.carWasDragged = true;
				this.car.setDragged(true);
				this.car.setShift(shift);
				lastCarPosition = this.car.getPosition();
			}
		} else if (carWasDragged) {
			// Car is dragged
			if (Gdx.input.isTouched()) {
				Vector2 newPosition = new Vector2(this.car.getPosition());

				if (newPosition.x > CONTROL_LINE_POSITION_X) {
					freeCar();
					return;
				}

				carSpeeds[carSpeedsIndex] = new Vector2(newPosition).sub(lastCarPosition);
				carSpeedsDeltas[carSpeedsIndex] = delta;

				// Counts average speed
				carSpeed = new Vector2(Vector2.Zero);
				float averageDelta = 0;
				for (int i = 0; i < carSpeeds.length; i++) {
					carSpeed.add(carSpeeds[i]);
					averageDelta += carSpeedsDeltas[i];
				}
				averageDelta /= carSpeeds.length;
				carSpeed.scl(1.f / carSpeeds.length);

				carSpeed.scl(1.f / averageDelta); // Normalization

				lastCarPosition = newPosition;
				carSpeedsIndex = carSpeedsIndex % carSpeeds.length;
			}
			// Just released
			else {
				freeCar();
			}
		}
	}

	private void freeCar() {
		this.car.setDragged(false);
		this.car.setShift(Vector2.Zero);
		this.state = States.DRIVING_STATE;
	}

	private void updateInDrivingState(float delta) {
		Vector2 a = new Vector2(carSpeed).nor().scl(-uniformDeceleration).add(wind);
		Vector2 at = new Vector2(a).scl(delta);
		Vector2 v = new Vector2(carSpeed).add(at);
		Vector2 s = new Vector2(carSpeed).add(v).scl(delta / 2);
		Vector2 newCarPosition = new Vector2(this.car.getPosition()).add(s);
		carSpeed = v;

		// To low speed
		if (at.len2() > v.len2() || s.len2() < 0.0001) {
			checkEnd();
		}
		// In the world
		else if (isInWorld(newCarPosition)) {
			this.car.move(newCarPosition);
		}
		// Out of the world
		else {
			checkEnd();
		}
	}

	public void checkEnd() {
		float distanceFromTargetInPercents = this.target.distanceInPerc(this.car.getPosition());
		if (distanceFromTargetInPercents > 0) {
			this.setResultValue(distanceFromTargetInPercents);
			win();
		} else {
			numberOfTries--;
			if (numberOfTries <= 0) {
				this.setResultValue(distanceFromTargetInPercents);
				fail();
			}
			restartForNewTry();
		}

	}

	private boolean isInWorld(Vector2 position) {
		return position.x > Constants.dialog.DIALOG_WORLD_X_OFFSET
				&& position.x < Constants.dialog.DIALOG_WORLD_X_OFFSET
						+ Constants.dialog.DIALOG_WORLD_WIDTH
				&& position.y > Constants.dialog.DIALOG_WORLD_Y_OFFSET
				&& position.y < Constants.dialog.DIALOG_WORLD_Y_OFFSET
						+ Constants.dialog.DIALOG_WORLD_HEIGHT;
	}

	private void setResultValue(float succesInPerc) {
		if (succesInPerc <= 0) {
			this.resultValue = Constants.minigames.RESULT_VALUE_NOTHING;
		} else {
			this.resultValue = succesInPerc * (MAX_WIN_COEF - 1) + 1;
		}
	}

	private void fail() {
		this.resultMessage = Constants.strings.TOOLTIP_MINIGAME_ANGLICAK_FAIL;
		this.result = ResultType.FAILED;
		leave();
	}

	private void win() {
		this.resultMessage = Constants.strings.TOOLTIP_MINIGAME_ANGLICAK_SUCCESS;
		this.result = ResultType.PROCEEDED_WITH_VALUE; // value is already set
		leave();
	}

	private void leave() {
		this.state = States.LEAVING_STATE;
		this.playResultSound();
	}

	@Override
	public void draw(SpriteBatch batch) {
		super.draw(batch);
		batch.begin();
		this.car.draw(batch);
		batch.end();
	}

	public enum States {
		BEGINNING_STATE, DRIVING_STATE, LEAVING_STATE;
	}

	private void setDifficulty(Difficulty difficulty) {
		switch (difficulty) {
		case Kiddie:
			this.targetRadius = Constants.minigames.ANGLICAK_MINIGAME_TARGET_RADIUS_KIDDIE;
			break;
		case Beginner:
			this.targetRadius = Constants.minigames.ANGLICAK_MINIGAME_TARGET_RADIUS_BEGINNER;
			break;
		case Normal:
			this.targetRadius = Constants.minigames.ANGLICAK_MINIGAME_TARGET_RADIUS_NORMAL;
			break;
		case Hard:
			this.targetRadius = Constants.minigames.ANGLICAK_MINIGAME_TARGET_RADIUS_HARD;
			break;
		case Extreme:
			this.targetRadius = Constants.minigames.ANGLICAK_MINIGAME_TARGET_RADIUS_EXTREME;
			break;
		default:
			throw new IllegalDifficultyException(difficulty.toString());
		}
		return;
	}

}
