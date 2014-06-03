package cz.cuni.mff.xcars.miniGames;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;

import cz.cuni.mff.xcars.Xcars;
import cz.cuni.mff.xcars.Difficulty;
import cz.cuni.mff.xcars.constants.Constants;
import cz.cuni.mff.xcars.dialogs.MessageDialog;
import cz.cuni.mff.xcars.entities.Car;
import cz.cuni.mff.xcars.entities.Finish;
import cz.cuni.mff.xcars.entities.GameObject;
import cz.cuni.mff.xcars.exceptions.IllegalDifficultyException;
import cz.cuni.mff.xcars.gamelogic.SubLevel;
import cz.cuni.mff.xcars.input.Input;
import cz.cuni.mff.xcars.miniGames.support.AvoidHole;
import cz.cuni.mff.xcars.miniGames.support.AvoidStone;
import cz.cuni.mff.xcars.scene.GameScreen;

public final class AvoidObstaclesMinigame extends Minigame {
	private final float CAR_START_POSITION_X = Constants.minigames.AVOID_OBSTACLES_CAR_START_POSITION_X;
	private final float FINISH_START_POSITION_X = Constants.minigames.AVOID_OBSTACLES_FINISH_START_POSITION_X;
	private final int FINISH_TYPE = Constants.minigames.AVOID_OBSTACLES_FINISH_TYPE;
	private float MINIMAL_DISTANCE_BETWEEN_OBSTACLES;
	private final float MINIMAL_DISTANCE_BETWEEN_CAR = Constants.minigames.AVOID_OBSTACLES_MINIMAL_DISTANCE_BETWEEN_CAR;
	private final float MINIMAL_DISTANCE_BETWEEN_FINISH = Constants.minigames.AVOID_OBSTACLES_MINIMAL_DISTANCE_BETWEEN_FINISH;
	private final int NUMBER_OF_TRIES_TO_GENERATE_OBSTACLE = Constants.minigames.AVOID_OBSTACLES_NUMBER_OF_TRIES_TO_GENERATE_OBSTACLE;

	private final float FAIL_VALUE = Constants.minigames.AVOID_OBSTACLES_FAIL_VALUE;

	private ObstaclesType obstaclesType;
	private ArrayList<GameObject> gameObjects;
	private Car car;
	private Finish finish;
	States state = States.BEGINNING_STATE;

	public AvoidObstaclesMinigame(GameScreen gameScreen, SubLevel parent, ObstaclesType obstacleType) {
		super(gameScreen, parent);
		this.obstaclesType = obstacleType;

		setDifficulty(this.level.getDifficulty());

		this.backgroundTexture = new NinePatchDrawable(
				Xcars.getInstance().assets
						.getNinePatch(Constants.minigames.AVOID_OBSTACLES_MINIGAME_BACKGROUND_TEXTURE));

		if (Xcars.settings.isShowTooltips())
			this.parent.setDialog(new MessageDialog(gameScreen, parent,
					Constants.strings.TOOLTIP_MINIGAME_AVOID_OBSTACLES_WHAT_TO_DO));

		this.gameObjects = new ArrayList<GameObject>();

		this.finish = new Finish(FINISH_START_POSITION_X, Constants.WORLD_HEIGHT / 2, FINISH_TYPE);

		this.car = new Car(CAR_START_POSITION_X, Constants.WORLD_HEIGHT / 2, 1);
		this.car.setDragged(false);

		generateObstacles(gameObjects);
	}

	private void generateObstacles(ArrayList<GameObject> gameObjects) {
		int tryCount = 0;
		boolean limitReached = false;
		do {
			tryCount = 0;
			float xPosition = 0;
			float yPosition = 0;

			// Finds new position. It has to be farer from another obstacles.
			boolean positionIsCorrect = true;
			do {
				positionIsCorrect = true;

				xPosition = MathUtils.random(Constants.dialog.DIALOG_WORLD_WIDTH - 20)
						+ Constants.dialog.DIALOG_WORLD_X_OFFSET + 10;
				yPosition = MathUtils.random(Constants.dialog.DIALOG_WORLD_HEIGHT - 20)
						+ Constants.dialog.DIALOG_WORLD_Y_OFFSET + 10;
				Vector2 position = new Vector2(xPosition, yPosition);

				if (position.dst(car.getPosition()) < MINIMAL_DISTANCE_BETWEEN_CAR) {
					positionIsCorrect = false;
				} else if (position.dst(finish.getPosition()) < MINIMAL_DISTANCE_BETWEEN_FINISH) {
					positionIsCorrect = false;
				} else {
					for (GameObject gameObject : gameObjects) {

						if (position.dst(gameObject.getPosition()) < MINIMAL_DISTANCE_BETWEEN_OBSTACLES) {
							positionIsCorrect = false;
							break;
						}
					}
				}
				++tryCount;
				limitReached = tryCount > NUMBER_OF_TRIES_TO_GENERATE_OBSTACLE;
			} while (!positionIsCorrect && !limitReached);

			if (!limitReached) {
				switch (obstaclesType) {
				case HOLES:
					int avoidHoleType = MathUtils.random(1,
							Constants.minigames.AVOID_HOLES_TYPES_COUNT);
					AvoidHole avoidHoleObstacle = new AvoidHole(xPosition, yPosition, avoidHoleType);
					gameObjects.add(avoidHoleObstacle);
					break;
				case STONES:
					int avoidStoneType = MathUtils.random(1,
							Constants.minigames.AVOID_STONE_TYPES_COUNT);
					AvoidStone avoidStoneObstacle = new AvoidStone(xPosition, yPosition,
							avoidStoneType);
					gameObjects.add(avoidStoneObstacle);
					break;
				default:
					throw new IllegalStateException(obstaclesType.toString());
				}
			}
		} while (!limitReached);
	}

	@Override
	public void update(float delta) {
		for (GameObject gameObject : gameObjects) {
			gameObject.update(delta);
		}
		this.car.update(delta);
		this.finish.update(delta);

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
			if (shift.len() <= Constants.misc.SHIFTABLE_OBJECT_MAX_CAPABLE_DISTANCE) {
				this.car.setDragged(true);
				this.car.setShift(shift);
				state = States.DRIVING_STATE;
			}
		}

	}

	private void updateInDrivingState(float delta) {
		// Taking focus of the car again
		if (Gdx.input.justTouched()) {
			Vector2 touchPos = new Vector2(Input.getX(), Input.getY());
			Vector2 shift = new Vector2(this.car.getPosition()).sub(touchPos.x, touchPos.y);
			if (shift.len() <= Constants.misc.SHIFTABLE_OBJECT_MAX_CAPABLE_DISTANCE) {
				this.car.setDragged(true);
				this.car.setShift(shift);
			}
		}
		// Car is out of borders
		else if (!isInWorld(this.car.getPosition())) {
			fail(Constants.strings.TOOLTIP_MINIGAME_CAR_OUT_OF_BOUNDS);
		}
		// Finish reached
		else if (this.car.positionCollides(finish)) {
			win();
		}
		// Collision detection
		else {
			for (GameObject gameObject : gameObjects) {
				if (this.car.collides(gameObject)) {
					fail(null);
					break;
				}
			}
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

	private void fail(String primaryMessage) {
		switch (obstaclesType) {
		case STONES:
			this.resultMessage = Constants.strings.TOOLTIP_MINIGAME_AVOID_STONES_FAIL;
			this.result = ResultType.FAILED;
			break;
		case HOLES:
			this.resultMessage = Constants.strings.TOOLTIP_MINIGAME_AVOID_HOLES_FAIL;
			this.result = ResultType.FAILED_WITH_VALUE;
			this.resultValue = FAIL_VALUE;
			break;
		default:
			throw new IllegalStateException(obstaclesType.toString());
		}
		if (primaryMessage != null)
			this.resultMessage = primaryMessage;
		leave();
	}

	private void win() {
		this.resultMessage = Constants.strings.TOOLTIP_MINIGAME_AVOID_SUCCESS;
		this.result = ResultType.PROCEEDED;
		leave();
	}

	private void leave() {
		this.car.setDragged(false);
		this.car.setCanBeDragged(false);
		this.playResultSound();
		this.state = States.LEAVING_STATE;
	}

	@Override
	public void draw(SpriteBatch batch) {
		super.draw(batch);
		batch.begin();
		for (GameObject gameObject : gameObjects) {
			gameObject.draw(batch, 0);
		}
		this.finish.draw(batch);
		this.car.draw(batch);
		batch.end();
	}

	public enum States {
		BEGINNING_STATE, DRIVING_STATE, LEAVING_STATE;
	}

	public enum ObstaclesType {
		STONES, HOLES
	}

	private void setDifficulty(Difficulty difficulty) {
		switch (difficulty) {
		case Kiddie:
			MINIMAL_DISTANCE_BETWEEN_OBSTACLES = Constants.minigames.AVOID_OBSTACLES_MINIMAL_DISTANCE_BETWEEN_OBSTACLES_KIDDIE;
			break;
		case Beginner:
			MINIMAL_DISTANCE_BETWEEN_OBSTACLES = Constants.minigames.AVOID_OBSTACLES_MINIMAL_DISTANCE_BETWEEN_OBSTACLES_BEGINNER;
			break;
		case Normal:
			MINIMAL_DISTANCE_BETWEEN_OBSTACLES = Constants.minigames.AVOID_OBSTACLES_MINIMAL_DISTANCE_BETWEEN_OBSTACLES_NORMAL;
			break;
		case Hard:
			MINIMAL_DISTANCE_BETWEEN_OBSTACLES = Constants.minigames.AVOID_OBSTACLES_MINIMAL_DISTANCE_BETWEEN_OBSTACLES_HARD;
			break;
		case Extreme:
			MINIMAL_DISTANCE_BETWEEN_OBSTACLES = Constants.minigames.AVOID_OBSTACLES_MINIMAL_DISTANCE_BETWEEN_OBSTACLES_EXTREME;
			break;
		default:
			throw new IllegalDifficultyException(difficulty.toString());
		}
		return;
	}

}
