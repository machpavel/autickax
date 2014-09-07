package cz.cuni.mff.xcars.miniGames;

import java.util.Deque;
import java.util.LinkedList;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
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

	private String LINE_TEXTURE = Constants.minigames.RACE_MINIGAME_LINE_TEXTURE;
	private String ENVIRONMENT_BACKGROUND_TEXTURE = Constants.minigames.ENVIRONMENT_BACKGROUND_TEXTURE;
	private String ENVIRONMENT_FOREGROUND_TEXTURE = Constants.minigames.ENVIRONMENT_FOREGROUND_TEXTURE;
	private float ENVIRONMENT_FOREGROUND_OFFSET = Constants.minigames.ENVIRONMENT_FOREGROUND_OFFSET;

	private static final float MIN_SPEED_ADDITION = Constants.minigames.MIN_SPEED_ADDITION;
	private static final float MAX_SPEED_ADDITION = Constants.minigames.MAX_SPEED_ADDITION;
	private static final float MIN_CAR_DISTANCE = Constants.minigames.MIN_CAR_DISTANCE;
	private static final float MAX_CAR_DISTANCE = Constants.minigames.MAX_CAR_DISTANCE;
	private static final float DISTANCE_RAISER = Constants.minigames.DISTANCE_RAISER;

	private float remainingTime;
	private float speed;
	private static final float environmentForegroundSpeedModifier = 1.3f;
	private static final float environmentBackgroundSpeedModifier = 0.8f;

	private float zoneWidth;
	private int zonesCount;
	private float[] zonesCenters;
	private float[] zonesSpeeds;
	private float[] minDistances;
	private float[] maxDistances;
	private ScreenAdaptiveImage[][] lineImages;
	private ScreenAdaptiveImage[] environmentForeground;
	private ScreenAdaptiveImage[] environmentBackground;

	private Deque<RaceMinigameCar>[] cars;

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
		generateEnvironmentBackground();
		generateEnvironmentForeground();

		initializeMainCar(gameScreen);
	}

	private void initializeMainCar(GameScreen gameScreen) {
		this.car = new RaceMinigameCar(CAR_START_POSITION_X,
				zonesCenters[MathUtils.random(zonesCount - 1)], 0);
		this.car.setCanBeDragged(true);
		this.car.setDragged(false);
	}

	@SuppressWarnings("unchecked")
	private void initializeArrays() {
		zonesCenters = new float[zonesCount];
		zonesSpeeds = new float[zonesCount];
		minDistances = new float[zonesCount];
		maxDistances = new float[zonesCount];
		lineImages = new ScreenAdaptiveImage[zonesCount - 1][2];
		environmentBackground = new ScreenAdaptiveImage[2];
		environmentForeground = new ScreenAdaptiveImage[2];

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
				lineImages[offsetX][i].setWidth(400
						+ 400
						% line.getRegion().getRegionWidth());
				this.stage.addActor(lineImages[offsetX][i]);
			}
			//lineImages[offsetX][0].setVisible(false);
			lineImages[offsetX][0].setPosition(0, positionY);
			lineImages[offsetX][1].setPosition(
					lineImages[offsetX][0].getRight(), positionY);
			positionY += zoneWidth;
		}
	}

	private void generateEnvironmentForeground() {
		TextureRegionDrawable foreground = new TextureRegionDrawable(
				Xcars.getInstance().assets
						.getGraphics(ENVIRONMENT_FOREGROUND_TEXTURE));
		float positionY = Constants.dialog.DIALOG_WORLD_Y_OFFSET
				- foreground.getRegion().getRegionHeight()
				+ ENVIRONMENT_FOREGROUND_OFFSET;

		for (int i = 0; i < environmentForeground.length; i++) {
			environmentForeground[i] = new ScreenAdaptiveImage(foreground);
			// this.stage.addActor(environmentForeground[i]); It is done
			// manually in draw function
		}
		environmentForeground[0].setPosition(0, positionY);
		environmentForeground[1].setPosition(
				environmentForeground[0].getWidth(), positionY);
	}

	private void generateEnvironmentBackground() {

		TextureRegionDrawable background = new TextureRegionDrawable(
				Xcars.getInstance().assets
						.getGraphics(ENVIRONMENT_BACKGROUND_TEXTURE));
		float positionY = Constants.dialog.DIALOG_WORLD_Y_OFFSET
				+ Constants.dialog.DIALOG_WORLD_HEIGHT;

		for (int i = 0; i < environmentBackground.length; i++) {
			environmentBackground[i] = new ScreenAdaptiveImage(background);
			this.stage.addActor(environmentBackground[i]);
		}
		environmentBackground[0].setPosition(0, positionY);
		environmentBackground[1].setPosition(
				environmentBackground[0].getWidth(), positionY);
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
		// //This is waiting part for first input. But we want to play instantly
		// //so it only switches the state to drive
		// if (Gdx.input.justTouched()) {
		// Vector2 touchPos = new Vector2(Input.getX(), Input.getY());
		//
		// Vector2 shift = new Vector2(this.car.getPosition()).sub(touchPos.x,
		// touchPos.y);
		// this.car.setDragged(true);
		// this.car.setShift(shift);
		// this.state = States.DRIVING_STATE;
		// }
		//updateEnvironment(delta);
		this.state = States.DRIVING_STATE;
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

			this.car.setDragged(true);
			this.car.setShift(shift);
		}

		updateEnvironment(delta);
		updateCars(delta);

		// Car is out of borders
		if (!isInWorld(this.car.getPosition())) {
			fail(Constants.strings.TOOLTIP_MINIGAME_CAR_OUT_OF_BOUNDS);
		} else {
			// Collision detection
			for (int zoneIndex = 0; zoneIndex < zonesCount; zoneIndex++) {
				for (RaceMinigameCar car : cars[zoneIndex]) {
					if (this.car.collidesRectangulary(car)) {
						fail(null);
						this.soundsManager
								.playSound(Constants.sounds.SOUND_MINIGAME_RACE_CRASH);
					}
				}
			}
		}
	}

	private void updateCars(float delta) {
		for (int zoneIndex = 0; zoneIndex < zonesCount; zoneIndex++) {
			// Moves cars
			for (RaceMinigameCar car : cars[zoneIndex]) {
				float x = car.getX();
				x -= zonesSpeeds[zoneIndex] * delta;
				car.setX(x);
			}

			// Removes cars which are gone
			RaceMinigameCar firstCar = cars[zoneIndex].peekFirst();
			if (firstCar != null
					&& firstCar.getLeftBottomCorner().x < -firstCar.getWidth()) {
				cars[zoneIndex].poll();

			}

			// Adds new car
			RaceMinigameCar lastCar = cars[zoneIndex].peekLast();
			if (lastCar == null
					|| lastCar.getLeftBottomCorner().x < Constants.WORLD_WIDTH) {
				int type = MathUtils.random(1,
						Constants.minigames.RACE_MINIGAME_CAR_TYPE_COUNT);
				RaceMinigameCar car = new RaceMinigameCar(0, 0, type);

				car.setX(Constants.WORLD_WIDTH + car.getWidth() / 2
						+ +(maxDistances[zoneIndex] - minDistances[zoneIndex])
						* MathUtils.random(1.f) + minDistances[zoneIndex]);

				car.setY(zonesCenters[zoneIndex]
						+ MathUtils.random(-1, 1)
						* (zoneWidth / 2 - car.getHeight() / 2 - lineImages[0][0]
								.getHeight() / 2));

				cars[zoneIndex].add(car);
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
		for (int i = 0; i < environmentForeground.length; i++) {
			environmentForeground[i].draw(batch, 1);
		}
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

	private void updateEnvironment(float delta) {
		updateLines(delta);
		updateEnvironmentForeground(delta);
		updateEnvironmentBackground(delta);
	}

	private void updateLines(float delta) {
		for (int offsetX = 0; offsetX < lineImages.length; offsetX++) {
			for (int i = 0; i < lineImages[offsetX].length; i++) {
				lineImages[offsetX][i].setX(lineImages[offsetX][i].getX()
						- speed * delta);
			}
		}
		for (int offsetX = 0; offsetX < lineImages.length; offsetX++) {
			for (int i = 0; i < lineImages[offsetX].length; i++) {
				if (lineImages[offsetX][i].getX() < -lineImages[offsetX][i]
						.getWidth()) {
					lineImages[offsetX][i].setX(lineImages[offsetX][(i + 1)
							% lineImages[offsetX].length].getRight());
				}
			}
		}
	}

	private void updateEnvironmentForeground(float delta) {
		for (int i = 0; i < environmentForeground.length; i++) {
			environmentForeground[i].setX(environmentForeground[i].getX()
					- speed * environmentForegroundSpeedModifier * delta);
		}
		for (int i = 0; i < environmentForeground.length; i++) {
			if (environmentForeground[i].getX() < -environmentForeground[i]
					.getWidth()) {
				environmentForeground[i].setX(environmentForeground[(i + 1)
						% environmentForeground.length].getRight());
			}
		}
	}

	private void updateEnvironmentBackground(float delta) {
		for (int i = 0; i < environmentBackground.length; i++) {
			environmentBackground[i].setX(environmentBackground[i].getX()
					- speed * environmentBackgroundSpeedModifier * delta);
		}
		for (int i = 0; i < environmentBackground.length; i++) {
			if (environmentBackground[i].getX() < -environmentBackground[i]
					.getWidth()) {
				environmentBackground[i].setX(environmentBackground[(i + 1)
						% environmentBackground.length].getRight());
			}
		}
	}
}
