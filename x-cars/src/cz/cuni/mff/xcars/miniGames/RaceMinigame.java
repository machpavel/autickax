package cz.cuni.mff.xcars.miniGames;

import java.util.Deque;
import java.util.LinkedList;
import java.util.Queue;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TiledDrawable;

import cz.cuni.mff.xcars.Difficulty;
import cz.cuni.mff.xcars.Xcars;
import cz.cuni.mff.xcars.constants.Constants;
import cz.cuni.mff.xcars.dialogs.MessageDialog;
import cz.cuni.mff.xcars.entities.GameObject;
import cz.cuni.mff.xcars.exceptions.IllegalDifficultyException;
import cz.cuni.mff.xcars.gamelogic.SubLevel;
import cz.cuni.mff.xcars.input.Input;
import cz.cuni.mff.xcars.miniGames.support.RaceMinigameCar;
import cz.cuni.mff.xcars.scene.GameScreen;
import cz.cuni.mff.xcars.screenObjects.ScreenAdaptiveImage;

public final class RaceMinigame extends Minigame {
	private final float CAR_START_POSITION_X = Constants.minigames.RACE_CAR_START_POSITION_X;

	private String LINE_TEXTURE = Constants.minigames.LINE_TEXTURE;

	private static final float MIN_SPEED_ADDITION = Constants.minigames.MIN_SPEED_ADDITION;
	private static final float MAX_SPEED_ADDITION = Constants.minigames.MAX_SPEED_ADDITION;
	private static final float MIN_CAR_DISTANCE = Constants.minigames.MIN_CAR_DISTANCE;
	private static final float MAX_CAR_DISTANCE = Constants.minigames.MAX_CAR_DISTANCE;
	private static final float DISTANCE_RAISER = Constants.minigames.DISTANCE_RAISER;

	private float remainingTime;
	private float speed;

	private float zoneWidth;
	private int zonesCount;
	private float[] zonesCenters;
	private float[] zonesSpeeds;
	private float[] minDistances;
	private float[] maxDistances;
	private ScreenAdaptiveImage[][] lineImages;

	private Deque<RaceMinigameCar>[] cars;
	// How much a car can move horizontally in a zone
	private float carsMaxYShift;
	// To come from the scene
	private float carsXShift;

	private RaceMinigameCar car;
	States state = States.BEGINNING_STATE;

	public RaceMinigame(GameScreen gameScreen, SubLevel parent) {
		super(gameScreen, parent);

		setDifficulty(this.level.getDifficulty());

		this.backgroundTexture = new NinePatchDrawable(
				Xcars.getInstance().assets
						.getNinePatch(Constants.minigames.RACE_MINIGAME_BACKGROUND_TEXTURE));

		if (Xcars.settings.isShowTooltips())
			this.parent.setDialog(new MessageDialog(gameScreen, parent,
					Constants.strings.TOOLTIP_MINIGAME_RACE_WHAT_TO_DO));

		zoneWidth = (float) Constants.dialog.DIALOG_WORLD_HEIGHT / zonesCount;

		initializeArrays(); // it has to be initialized first
		generateLines();
		createCars(gameScreen);
		generateObstacles(cars);

	}

	private void createCars(GameScreen gameScreen) {
		this.car = new RaceMinigameCar(CAR_START_POSITION_X,
				zonesCenters[MathUtils.random(zonesCount - 1)], 0);
		this.car.setCanBeDragged(true);
		this.car.setDragged(false);

		RaceMinigameCar raceCar = new RaceMinigameCar(0, 0, 1);
		carsMaxYShift = zoneWidth / 2 - raceCar.getHeight() / 2
				- lineImages[0][0].getHeight() / 2;
		carsXShift = raceCar.getWidth() / 2;
	}

	@SuppressWarnings("unchecked")
	private void initializeArrays() {
		zonesCenters = new float[zonesCount];
		zonesSpeeds = new float[zonesCount];
		minDistances = new float[zonesCount];
		maxDistances = new float[zonesCount];
		lineImages = new ScreenAdaptiveImage[zonesCount - 1][2];

		cars = new LinkedList[zonesCount];

		for (int zoneIndex = 0; zoneIndex < zonesCount; zoneIndex++) {
			zonesCenters[zoneIndex] = (float) Constants.dialog.DIALOG_WORLD_Y_OFFSET
					+ zoneWidth / 2 + zoneWidth * zoneIndex;
			zonesSpeeds[zoneIndex] = speed
					- (MAX_SPEED_ADDITION - MIN_SPEED_ADDITION) * zoneIndex
					/ zonesCount - MIN_SPEED_ADDITION;
			cars[zoneIndex] = new LinkedList<RaceMinigameCar>();
		}

		minDistances[zonesCount - 1] = MIN_CAR_DISTANCE;
		maxDistances[zonesCount - 1] = MAX_CAR_DISTANCE;
		for (int zoneIndex = zonesCount - 2; zoneIndex >= 0; zoneIndex--) {
			minDistances[zoneIndex] = minDistances[zoneIndex + 1]
					* DISTANCE_RAISER;
			maxDistances[zoneIndex] = maxDistances[zoneIndex + 1]
					* DISTANCE_RAISER;
		}
	}

	private void generateLines() {
		TiledDrawable line = new TiledDrawable(
				Xcars.getInstance().assets.getGraphics(LINE_TEXTURE));
		float positionY = Constants.dialog.DIALOG_WORLD_Y_OFFSET + zoneWidth
				- line.getRegion().getRegionHeight() / 2;
		for (int offsetX = 0; offsetX < lineImages.length; offsetX++) {
			for (int i = 0; i < lineImages[offsetX].length; i++) {
				lineImages[offsetX][i] = new ScreenAdaptiveImage(line);
				lineImages[offsetX][i].setWidth(Constants.WORLD_WIDTH);
				this.stage.addActor(lineImages[offsetX][i]);
			}
			lineImages[offsetX][0].setPosition(0, positionY);
			lineImages[offsetX][1].setPosition(-Constants.WORLD_WIDTH,
					positionY);
			positionY += zoneWidth;
		}
	}

	private void generateObstacles(Queue<RaceMinigameCar>[] gameObjects) {
		// Here can be some starting configuration
	}

	@Override
	public void update(float delta) {
		stage.act(delta);
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

			Vector2 shift = new Vector2(this.car.getPosition()).sub(touchPos.x,
					touchPos.y);
			if (shift.len() <= Constants.misc.SHIFTABLE_OBJECT_MAX_CAPABLE_DISTANCE) {
				this.car.setDragged(true);
				this.car.setShift(shift);
				state = States.DRIVING_STATE;
			}
		}
	}

	private void updateInDrivingState(float delta) {
		remainingTime -= delta;

		// The time has gone, so player wins
		if (remainingTime < 0) {
			win();
			return;
		}

		// Taking focus of the car again
		if (Gdx.input.justTouched()) {
			Vector2 touchPos = new Vector2(Input.getX(), Input.getY());
			Vector2 shift = new Vector2(this.car.getPosition()).sub(touchPos.x,
					touchPos.y);
			if (shift.len() <= Constants.misc.SHIFTABLE_OBJECT_MAX_CAPABLE_DISTANCE) {
				this.car.setDragged(true);
				this.car.setShift(shift);
			}
		}

		moveLines(delta);
		carsUpdate(delta);

		// Car is out of borders
		if (!isInWorld(this.car.getPosition())) {
			fail(Constants.strings.TOOLTIP_MINIGAME_CAR_OUT_OF_BOUNDS);
		} else {
			// Collision detection
			for (int zoneIndex = 0; zoneIndex < zonesCount; zoneIndex++) {
				for (RaceMinigameCar car : cars[zoneIndex]) {
					if (this.car.collides(car)) {
						fail(null);
					}
				}
			}
		}
	}

	private void carsUpdate(float delta) {
		float rightBorder = Constants.WORLD_WIDTH + carsXShift;
		for (int zoneIndex = 0; zoneIndex < zonesCount; zoneIndex++) {
			// Moves cars
			for (RaceMinigameCar car : cars[zoneIndex]) {
				float x = car.getX();
				x -= zonesSpeeds[zoneIndex] * delta;
				car.setX(x);
			}

			// Removes cars which are gone
			RaceMinigameCar firstCar = cars[zoneIndex].peekFirst();
			if (firstCar != null && firstCar.getX() < -carsXShift) {
				cars[zoneIndex].poll();

			}

			// Adds new car
			RaceMinigameCar lastCar = cars[zoneIndex].peekLast();
			if (lastCar == null || lastCar.getX() < rightBorder) {
				float x = rightBorder
						+ (maxDistances[zoneIndex] - minDistances[zoneIndex])
						* MathUtils.random(1.f) + minDistances[zoneIndex];
				float y = zonesCenters[zoneIndex] + MathUtils.random(-1, 1)
						* carsMaxYShift;
				int type = MathUtils.random(1,
						Constants.minigames.RACE_MINIGAME_CAR_TYPE_COUNT);
				cars[zoneIndex].add(new RaceMinigameCar(x, y, type));
			}
		}

	}

	private boolean isInWorld(Vector2 position) {
		return position.x > 0
				&& position.x < Constants.WORLD_WIDTH
				&& position.y > Constants.dialog.DIALOG_WORLD_Y_OFFSET
				&& position.y < Constants.dialog.DIALOG_WORLD_Y_OFFSET
						+ Constants.dialog.DIALOG_WORLD_HEIGHT;
	}

	private void fail(String primaryMessage) {
		if (primaryMessage != null)
			this.resultMessage = primaryMessage;
		else
			this.resultMessage = Constants.strings.TOOLTIP_MINIGAME_RACE_FAIL;
		this.result = ResultType.FAILED;
		leave();
	}

	private void win() {
		this.resultMessage = Constants.strings.TOOLTIP_MINIGAME_RACE_SUCCESS;
		this.result = ResultType.PROCEEDED;
		leave();
	}

	private void leave() {
		this.playResultSound();
		this.car.setCanBeDragged(false);
		this.car.setDragged(false);
		this.state = States.LEAVING_STATE;
	}

	@Override
	public void draw(SpriteBatch batch) {
		super.draw(batch);
		batch.begin();
		for (int i = 0; i < cars.length; i++) {
			for (GameObject car : cars[i]) {
				car.draw(batch);
			}
		}
		this.car.draw(batch);
		batch.end();
	}

	public enum States {
		BEGINNING_STATE, DRIVING_STATE, LEAVING_STATE;
	}

	private void setDifficulty(Difficulty difficulty) {
		switch (difficulty) {
		case Kiddie:
			zonesCount = Constants.minigames.RACE_MINIGAME_ZONES_COUNT_KIDDIE;
			remainingTime = Constants.minigames.RACE_MINIGAME_TIME_KIDDIE;
			speed = Constants.minigames.RACE_MINIGAME_SPEED_KIDDIE;
			break;
		case Beginner:
			zonesCount = Constants.minigames.RACE_MINIGAME_ZONES_COUNT_BEGINNER;
			remainingTime = Constants.minigames.RACE_MINIGAME_TIME_BEGINNER;
			speed = Constants.minigames.RACE_MINIGAME_SPEED_BEGINNER;
			break;
		case Normal:
			zonesCount = Constants.minigames.RACE_MINIGAME_ZONES_COUNT_NORMAL;
			remainingTime = Constants.minigames.RACE_MINIGAME_TIME_NORMAL;
			speed = Constants.minigames.RACE_MINIGAME_SPEED_NORMAL;
			break;
		case Hard:
			zonesCount = Constants.minigames.RACE_MINIGAME_ZONES_COUNT_HARD;
			remainingTime = Constants.minigames.RACE_MINIGAME_TIME_HARD;
			speed = Constants.minigames.RACE_MINIGAME_SPEED_HARD;
			break;
		case Extreme:
			zonesCount = Constants.minigames.RACE_MINIGAME_ZONES_COUNT_EXTREME;
			remainingTime = Constants.minigames.RACE_MINIGAME_TIME_EXTREME;
			speed = Constants.minigames.RACE_MINIGAME_SPEED_EXTREME;
			break;
		default:
			throw new IllegalDifficultyException(difficulty.toString());
		}
		return;
	}

	public void moveLines(float delta) {
		for (int offsetX = 0; offsetX < lineImages.length; offsetX++) {
			for (int i = 0; i < lineImages[offsetX].length; i++) {
				lineImages[offsetX][i].setX(lineImages[offsetX][i].getX()
						- speed * delta);
				if (lineImages[offsetX][i].getX() < -Constants.WORLD_WIDTH) {
					lineImages[offsetX][i].setX(Constants.WORLD_WIDTH);
				}

			}
		}
	}
}
