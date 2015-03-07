package cz.cuni.mff.xcars.miniGames;

import java.util.Deque;
import java.util.LinkedList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Slider.SliderStyle;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

import cz.cuni.mff.xcars.Difficulty;
import cz.cuni.mff.xcars.Xcars;
import cz.cuni.mff.xcars.constants.Constants;
import cz.cuni.mff.xcars.debug.Debug;
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
	private static final float environmentForegroundSpeedModifier = 1.3f;
	private static final float environmentBackgroundSpeedModifier = 0.8f;
	private static int carsTypeCount;

	private float zoneWidth;
	private int zonesCount;
	private float[] zonesCenters;
	private float[] zonesSpeeds;
	private float[] minDistances;
	private float[] maxDistances;
	private Deque<ScreenAdaptiveImage>[] lineImages;
	private Deque<ScreenAdaptiveImage> environmentForeground;
	private Deque<ScreenAdaptiveImage> environmentBackground;
	private Deque<RaceMinigameCar>[] cars;

	private RaceMinigameCar car;
	States state = States.BEGINNING_STATE;

	private float remainingTime;
	private float timeLimit;
	private float speed;

	// TODO how to show progress?
	private static final float progressBarXPosition = 400;
	private static final float progressBarYPosition = 440;
	private ProgressBar progressBar;

	public RaceMinigame(GameScreen gameScreen, SubLevel parent) {
		super(gameScreen, parent);

		setDifficulty(this.level.getDifficulty());

		this.backgroundTexture = new NinePatchDrawable(
				Xcars.getInstance().assets.getNinePatch(Constants.minigames.RACE_MINIGAME_BACKGROUND_TEXTURE));

		if (Xcars.settings.isShowTooltips())
			this.parent.setDialog(new MessageDialog(gameScreen, parent,
					Constants.strings.TOOLTIP_MINIGAME_RACE_WHAT_TO_DO));

		zoneWidth = (float) Constants.dialog.DIALOG_WORLD_HEIGHT / zonesCount;
		carsTypeCount = getCarTypesCount();

		initializeArrays(); // it has to be initialized first

		generateLines();
		generateEnvironmentBackground();
		generateEnvironmentForeground();

		initializeMainCar(gameScreen);
		createProgressBar();
	}

	private void createProgressBar() {
		SliderStyle style = new SliderStyle();
		// TODO Figure out how to show the progress
		style.background = new TextureRegionDrawable(
				Xcars.getInstance().assets.getGraphics(Constants.minigames.RACE_MINIGAME_SLIDER_BACKGROUND_TEXTURE));
		style.knob = new TextureRegionDrawable(
				Xcars.getInstance().assets.getGraphics(Constants.minigames.RACE_MINIGAME_SLIDER_KNOB_TEXTURE));
		progressBar = new ProgressBar(0, 1, 0.001f, false, style);
		progressBar.setPosition(progressBarXPosition - progressBar.getWidth() / 2, progressBarYPosition);
		progressBar.setValue(50);
		this.stage.addActor(progressBar);
	}

	private int getCarTypesCount() {
		// Index 0 is reserved for player car
		int count = 1;
		while (Xcars.getInstance().assets.graphicExist(RaceMinigameCar.GetTextureName(count))) {
			++count;
		}
		return count - 1;
	}

	private void initializeMainCar(GameScreen gameScreen) {
		this.car = new RaceMinigameCar(CAR_START_POSITION_X, zonesCenters[MathUtils.random(zonesCount - 1)], 0);
		this.car.setCanBeDragged(true);
		this.car.setDragged(false);
	}

	@SuppressWarnings("unchecked")
	private void initializeArrays() {
		// Zones stuffs (except for distances)
		zonesCenters = new float[zonesCount];
		zonesSpeeds = new float[zonesCount];
		cars = new LinkedList[zonesCount];
		for (int zoneIndex = 0; zoneIndex < zonesCount; zoneIndex++) {
			zonesCenters[zoneIndex] = (float) Constants.dialog.DIALOG_WORLD_Y_OFFSET + zoneWidth / 2 + zoneWidth
					* zoneIndex;
			zonesSpeeds[zoneIndex] = speed - (MAX_SPEED_ADDITION - MIN_SPEED_ADDITION) * zoneIndex / zonesCount
					- MIN_SPEED_ADDITION;
			cars[zoneIndex] = new LinkedList<RaceMinigameCar>();
		}

		// Zones distances
		minDistances = new float[zonesCount];
		maxDistances = new float[zonesCount];
		minDistances[zonesCount - 1] = MIN_CAR_DISTANCE;
		maxDistances[zonesCount - 1] = MAX_CAR_DISTANCE;
		for (int zoneIndex = zonesCount - 2; zoneIndex >= 0; zoneIndex--) {
			minDistances[zoneIndex] = minDistances[zoneIndex + 1] * DISTANCE_RAISER;
			maxDistances[zoneIndex] = maxDistances[zoneIndex + 1] * DISTANCE_RAISER;
		}

		// Lines
		lineImages = new LinkedList[zonesCount - 1];
		for (int zoneIndex = 0; zoneIndex < zonesCount - 1; zoneIndex++) {
			lineImages[zoneIndex] = new LinkedList<ScreenAdaptiveImage>();
		}
	}

	private void generateDeque(Deque<ScreenAdaptiveImage> deque, TextureRegion texture, float positionY,
			boolean addActors) {
		float offsetX = 0;
		int count = 0;
		do {
			ScreenAdaptiveImage image = new ScreenAdaptiveImage(texture);
			image.setPosition(offsetX, positionY);
			deque.addLast(image);
			if (addActors)
				this.stage.addActor(image);
			offsetX = image.getRight();
			count++;
		} while (offsetX <= Constants.WORLD_WIDTH || count < 2);
	}

	private void generateLines() {
		TextureRegion lineTexture = Xcars.getInstance().assets.getGraphics(LINE_TEXTURE);
		float positionY = Constants.dialog.DIALOG_WORLD_Y_OFFSET + zoneWidth - lineTexture.getRegionHeight() / 2;
		for (int i = 0; i < lineImages.length; i++) {
			generateDeque(this.lineImages[i], lineTexture, positionY, true);
			positionY += zoneWidth;
		}
	}

	private void generateEnvironmentForeground() {
		this.environmentForeground = new LinkedList<ScreenAdaptiveImage>();
		TextureRegion foregroundTexture = Xcars.getInstance().assets.getGraphics(ENVIRONMENT_FOREGROUND_TEXTURE);
		float positionY = Constants.dialog.DIALOG_WORLD_Y_OFFSET - foregroundTexture.getRegionHeight()
				+ ENVIRONMENT_FOREGROUND_OFFSET;
		generateDeque(this.environmentForeground, foregroundTexture, positionY, false);
	}

	private void generateEnvironmentBackground() {
		this.environmentBackground = new LinkedList<ScreenAdaptiveImage>();
		TextureRegion backgroundTexture = Xcars.getInstance().assets.getGraphics(ENVIRONMENT_BACKGROUND_TEXTURE);
		float positionY = Constants.dialog.DIALOG_WORLD_Y_OFFSET + Constants.dialog.DIALOG_WORLD_HEIGHT;
		generateDeque(this.environmentBackground, backgroundTexture, positionY, true);
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
		// updateEnvironment(delta);
		this.state = States.DRIVING_STATE;
	}

	private void updateInDrivingState(float delta) {
		remainingTime -= delta;

		// The time has gone, so player wins
		if (remainingTime < 0) {
			win();
			return;
		}

		updateProgress();

		// Taking focus of the car again
		if (Gdx.input.justTouched()) {
			Vector2 touchPos = new Vector2(Input.getX(), Input.getY());
			Vector2 shift = new Vector2(this.car.getPosition()).sub(touchPos.x, touchPos.y);

			this.car.setDragged(true);
			this.car.setShift(shift);
		}

		updateLines(delta);
		updateEnvironmentForeground(delta);
		updateEnvironmentBackground(delta);
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
						this.soundsManager.playSound(Constants.sounds.SOUND_MINIGAME_RACE_CRASH);
					}
				}
			}
		}
	}

	private void updateProgress() {
		float nextProgressBarValue = 1 - this.remainingTime / this.timeLimit;
		this.progressBar.setValue(nextProgressBarValue);
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
			while (firstCar != null && firstCar.getLeftBottomCorner().x < -firstCar.getWidth()) {
				cars[zoneIndex].poll();
				firstCar = cars[zoneIndex].peekFirst();
			}

			// Adds new car
			RaceMinigameCar lastCar = cars[zoneIndex].peekLast();
			if (lastCar == null || lastCar.getLeftBottomCorner().x < Constants.WORLD_WIDTH) {
				// Index 0 is reserved for player car
				int type = MathUtils.random(1, carsTypeCount);
				RaceMinigameCar car = new RaceMinigameCar(0, 0, type);

				car.setX(Constants.WORLD_WIDTH + car.getWidth() / 2
						+ (maxDistances[zoneIndex] - minDistances[zoneIndex]) * MathUtils.random(1.f)
						+ minDistances[zoneIndex]);

				car.setY(zonesCenters[zoneIndex] + MathUtils.random(-1, 1)
						* (zoneWidth / 2 - car.getHeight() / 2 - lineImages[0].peek().getHeight() / 2));

				cars[zoneIndex].add(car);
			}
		}
	}

	private boolean isInWorld(Vector2 position) {
		return position.x > 0 && position.x < Constants.WORLD_WIDTH
				&& position.y > Constants.dialog.DIALOG_WORLD_Y_OFFSET
				&& position.y < Constants.dialog.DIALOG_WORLD_Y_OFFSET + Constants.dialog.DIALOG_WORLD_HEIGHT;
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
		// Draws foreground manually to be up
		for (ScreenAdaptiveImage foreground : environmentForeground) {
			foreground.draw(batch, 1);
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
		this.timeLimit = remainingTime;
		return;
	}

	// Moves items which are off the screen to the second side
	private void updateDeque(Deque<ScreenAdaptiveImage> deque, float speed, float delta) {
		// Moves all items
		for (ScreenAdaptiveImage item : deque) {
			item.setX(item.getX() - delta * speed);
		}

		// Items out off screen are moved to the second size
		while (deque.peekFirst().getRight() < 0) {
			deque.peekFirst().setX(deque.peekLast().getRight());
			deque.addLast(deque.pollFirst());
		}

	}

	private void updateLines(float delta) {
		for (int i = 0; i < this.lineImages.length; i++) {
			updateDeque(this.lineImages[i], this.speed, delta);
		}
	}

	private void updateEnvironmentForeground(float delta) {
		updateDeque(this.environmentForeground, this.speed * environmentForegroundSpeedModifier, delta);
	}

	private void updateEnvironmentBackground(float delta) {
		updateDeque(this.environmentBackground, this.speed * environmentBackgroundSpeedModifier, delta);
	}

	@Override
	protected void drawBackGroundTexture(SpriteBatch batch) {
		batch.begin();
		this.backgroundTexture.draw(batch, 0, 0, Constants.WORLD_WIDTH, Constants.WORLD_HEIGHT);
		batch.end();
	}

	@Override
	public void DrawDiagnostics() {
		if (Debug.raceMinigameMinigame) {
			Debug.SetValue(remainingTime);

			super.DrawDiagnostics();

			shapeRenderer.begin(ShapeType.Line);
			float width = 2.f;
			shapeRenderer.setColor(0.5f, 0, 0, 1);

			// Out of world
			shapeRenderer.rectLine(0, Constants.dialog.DIALOG_WORLD_Y_OFFSET + Constants.dialog.DIALOG_WORLD_HEIGHT,
					Constants.WORLD_WIDTH, Constants.dialog.DIALOG_WORLD_Y_OFFSET
							+ Constants.dialog.DIALOG_WORLD_HEIGHT, width);
			shapeRenderer.rectLine(0, Constants.dialog.DIALOG_WORLD_Y_OFFSET, Constants.WORLD_WIDTH,
					Constants.dialog.DIALOG_WORLD_Y_OFFSET, width);

			shapeRenderer.end();
		}
	}
}
