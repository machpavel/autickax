package cz.cuni.mff.xcars.miniGames;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;

import cz.cuni.mff.xcars.Difficulty;
import cz.cuni.mff.xcars.Xcars;
import cz.cuni.mff.xcars.constants.Constants;
import cz.cuni.mff.xcars.debug.Debug;
import cz.cuni.mff.xcars.dialogs.MessageDialog;
import cz.cuni.mff.xcars.exceptions.IllegalDifficultyException;
import cz.cuni.mff.xcars.gamelogic.SubLevel;
import cz.cuni.mff.xcars.input.Input;
import cz.cuni.mff.xcars.miniGames.support.GearShifter;
import cz.cuni.mff.xcars.scene.GameScreen;
import cz.cuni.mff.xcars.screenObjects.ScreenAdaptiveImage;

public final class GearShiftMinigame extends Minigame {
	private static final float FAIL_VALUE = Constants.minigames.GEAR_SHIFT_FAIL_VALUE;
	private static final float ROW_1 = Constants.minigames.GEAR_SHIFT_MINIGAME_ROW_1;
	private static final float ROW_2 = Constants.minigames.GEAR_SHIFT_MINIGAME_ROW_2;
	private static final float ROW_3 = Constants.minigames.GEAR_SHIFT_MINIGAME_ROW_3;
	private static final float COLUMN_1 = Constants.minigames.GEAR_SHIFT_MINIGAME_COLUMN_1;
	private static final float COLUMN_2 = Constants.minigames.GEAR_SHIFT_MINIGAME_COLUMN_2;
	private static final float COLUMN_3 = Constants.minigames.GEAR_SHIFT_MINIGAME_COLUMN_3;
	private static float MAX_DISTANCE_FROM_LINE;
	private static float FINISH_RADIUS = Constants.minigames.GEAR_SHIFT_FINISH_RADIUS;

	private States state = States.BEGINNING_STATE;
	private GearShifter gearShifter;
	private Vector2 finishPosition;

	public GearShiftMinigame(GameScreen screen, SubLevel parent) {
		super(screen, parent);
		setDifficulty(this.level.getDifficulty());
		this.backgroundTexture = new NinePatchDrawable(
				Xcars.getInstance().assets.getNinePatch(Constants.minigames.GEAR_SHIFT_MINIGAME_BACKGROUND_TEXTURE));

		if (Xcars.settings.isShowTooltips())
			this.parent.setDialog(new MessageDialog(screen, parent,
					Constants.strings.TOOLTIP_MINIGAME_GEAR_SHIFT_WHAT_TO_DO));

		float[] rows = new float[] { ROW_1, ROW_2, ROW_3 };
		float[] columns = new float[] { COLUMN_1, COLUMN_2, COLUMN_3 };

		// Generates position of finish
		int finishPositionIndex = MathUtils.random(5);
		this.finishPosition = new Vector2(columns[finishPositionIndex % 3],
				rows[(((finishPositionIndex / 3) + 1) % 2) * 2]);

		// Generates position of gear shifter
		int gearShifterIndex = 0;
		do {
			gearShifterIndex = MathUtils.random(5);
		} while (finishPositionIndex == gearShifterIndex);
		this.gearShifter = new GearShifter(columns[gearShifterIndex % 3], rows[(((gearShifterIndex / 3) + 1) % 2) * 2]);

		// Generates gears numbers
		float numbersPositionOffset = Constants.minigames.GEAR_SHIFT_MINIGAME_GEAR_NUMBER_VERTICAL_OFFSET;
		String numbersTexturePrefix = Constants.minigames.GEAR_SHIFT_MINIGAME_GEAR_NUMBER_TEXTURE_PREFIX;
		for (int i = 0; i < 6; i++) {
			TextureRegion numberTexture;
			if (i == finishPositionIndex) {
				numberTexture = Xcars.getInstance().assets.getGraphics(numbersTexturePrefix + (i + 1) + "e");
			} else {
				numberTexture = Xcars.getInstance().assets.getGraphics(numbersTexturePrefix + (i + 1) + "d");
			}
			ScreenAdaptiveImage gearNumberImage = new ScreenAdaptiveImage(numberTexture);
			float yOffset = i < 3 ? numbersPositionOffset : -numbersPositionOffset;
			gearNumberImage.setCenterPosition(columns[i % 3], rows[(((i / 3) + 1) % 2) * 2] + yOffset);
			stage.addActor(gearNumberImage);
		}

		// Generates axis image
		ScreenAdaptiveImage gearsAxisImage = new ScreenAdaptiveImage(
				Xcars.getInstance().assets.getGraphics(Constants.minigames.GEAR_SHIFT_MINIGAME_GEARS_AXIS_TEXTURE));
		gearsAxisImage.setCenterPosition(Constants.WORLD_WIDTH / 2, Constants.WORLD_HEIGHT / 2);
		stage.addActor(gearsAxisImage);
	}

	@Override
	public void update(float delta) {
		this.gearShifter.update(delta);

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
			Vector2 shift = new Vector2(this.gearShifter.getPosition()).sub(touchPos.x, touchPos.y);
			float maxDistance = Constants.misc.SHIFTABLE_OBJECT_MAX_CAPABLE_DISTANCE;

			if (shift.len() <= maxDistance) {
				this.gearShifter.setDragged(true);
				this.gearShifter.setShift(shift);
				state = States.DRIVING_STATE;
			}
		}

	}

	private void updateInDrivingState(float delta) {
		// Focus was lost
		if (!this.gearShifter.isDragged()) {
			fail(Constants.strings.TOOLTIP_MINIGAME_GEAR_SHIFT_WAS_RELEASED);
		}
		// Finish reached
		else if (this.gearShifter.getPosition().sub(finishPosition).len() < FINISH_RADIUS) {
			win();
		}
		// Out of pathway
		else if (!isInGrid(this.gearShifter.getPosition().x, this.gearShifter.getPosition().y)) {
			fail(null);
		}
	}

	private void fail(String primaryMessage) {
		if (primaryMessage != null)
			this.resultMessage = primaryMessage;
		else
			this.resultMessage = Constants.strings.TOOLTIP_MINIGAME_GEAR_SHIFT_FAIL;
		this.result = ResultType.FAILED_WITH_VALUE;
		this.resultValue = FAIL_VALUE;
		leave();
	}

	private void win() {
		this.gearShifter.setPosition(finishPosition.x, finishPosition.y);
		this.resultMessage = Constants.strings.TOOLTIP_MINIGAME_GEAR_SHIFT_SUCCESS;
		this.result = ResultType.PROCEEDED;
		leave();
	}

	private void leave() {
		this.playResultSound();
		this.gearShifter.setCanBeDragged(false);
		this.gearShifter.setDragged(false);
		this.state = States.LEAVING_STATE;
	}

	@Override
	public void draw(SpriteBatch batch) {
		super.draw(batch);
		batch.begin();
		this.gearShifter.draw(batch);
		batch.end();
	}

	private boolean isInGrid(float x, float y) {
		// Vertical limits
		if (y > ROW_3 + MAX_DISTANCE_FROM_LINE || y < ROW_1 - MAX_DISTANCE_FROM_LINE)
			return false;
		// In first column
		if (x > COLUMN_1 - MAX_DISTANCE_FROM_LINE && x < COLUMN_1 + MAX_DISTANCE_FROM_LINE)
			return true;
		// In second column
		if (x > COLUMN_2 - MAX_DISTANCE_FROM_LINE && x < COLUMN_2 + MAX_DISTANCE_FROM_LINE)
			return true;
		// In second column
		if (x > COLUMN_3 - MAX_DISTANCE_FROM_LINE && x < COLUMN_3 + MAX_DISTANCE_FROM_LINE)
			return true;
		// Middle row
		if (x > COLUMN_1 - MAX_DISTANCE_FROM_LINE && x < COLUMN_3 + MAX_DISTANCE_FROM_LINE
				&& y > ROW_2 - MAX_DISTANCE_FROM_LINE && y < ROW_2 + MAX_DISTANCE_FROM_LINE)
			return true;
		return false;
	}

	private void setDifficulty(Difficulty difficulty) {
		switch (difficulty) {
		case Kiddie:
			MAX_DISTANCE_FROM_LINE = Constants.minigames.GEAR_SHIFT_MINIGAME_MAX_DISTANCE_FROM_LINE_KIDDIE;
			break;
		case Beginner:
			MAX_DISTANCE_FROM_LINE = Constants.minigames.GEAR_SHIFT_MINIGAME_MAX_DISTANCE_FROM_LINE_BEGINNER;
			break;
		case Normal:
			MAX_DISTANCE_FROM_LINE = Constants.minigames.GEAR_SHIFT_MINIGAME_MAX_DISTANCE_FROM_LINE_NORMAL;
			break;
		case Hard:
			MAX_DISTANCE_FROM_LINE = Constants.minigames.GEAR_SHIFT_MINIGAME_MAX_DISTANCE_FROM_LINE_HARD;
			break;
		case Extreme:
			MAX_DISTANCE_FROM_LINE = Constants.minigames.GEAR_SHIFT_MINIGAME_MAX_DISTANCE_FROM_LINE_EXTREME;
			break;
		default:
			throw new IllegalDifficultyException(difficulty.toString());
		}
		return;
	}

	private enum States {
		BEGINNING_STATE, DRIVING_STATE, LEAVING_STATE;
	}

	@Override
	public void DrawDiagnostics() {
		if (Debug.gearShiftMinigame) {
			// Max distance from skeleton lines
			shapeRenderer.begin(ShapeType.Filled);
			float width = 1.f;
			shapeRenderer.setColor(0.5f, 0, 0, 1);
			shapeRenderer.rectLine(COLUMN_1 - MAX_DISTANCE_FROM_LINE, ROW_2 + MAX_DISTANCE_FROM_LINE, COLUMN_3
					+ MAX_DISTANCE_FROM_LINE, ROW_2 + MAX_DISTANCE_FROM_LINE, width);
			shapeRenderer.rectLine(COLUMN_1 - MAX_DISTANCE_FROM_LINE, ROW_2 - MAX_DISTANCE_FROM_LINE, COLUMN_3
					+ MAX_DISTANCE_FROM_LINE, ROW_2 - MAX_DISTANCE_FROM_LINE, width);
			shapeRenderer.rectLine(COLUMN_1 + MAX_DISTANCE_FROM_LINE, ROW_1 - MAX_DISTANCE_FROM_LINE, COLUMN_1
					+ MAX_DISTANCE_FROM_LINE, ROW_3 + MAX_DISTANCE_FROM_LINE, width);
			shapeRenderer.rectLine(COLUMN_1 - MAX_DISTANCE_FROM_LINE, ROW_1 - MAX_DISTANCE_FROM_LINE, COLUMN_1
					- MAX_DISTANCE_FROM_LINE, ROW_3 + MAX_DISTANCE_FROM_LINE, width);
			shapeRenderer.rectLine(COLUMN_2 + MAX_DISTANCE_FROM_LINE, ROW_1 - MAX_DISTANCE_FROM_LINE, COLUMN_2
					+ MAX_DISTANCE_FROM_LINE, ROW_3 + MAX_DISTANCE_FROM_LINE, width);
			shapeRenderer.rectLine(COLUMN_2 - MAX_DISTANCE_FROM_LINE, ROW_1 - MAX_DISTANCE_FROM_LINE, COLUMN_2
					- MAX_DISTANCE_FROM_LINE, ROW_3 + MAX_DISTANCE_FROM_LINE, width);
			shapeRenderer.rectLine(COLUMN_3 + MAX_DISTANCE_FROM_LINE, ROW_1 - MAX_DISTANCE_FROM_LINE, COLUMN_3
					+ MAX_DISTANCE_FROM_LINE, ROW_3 + MAX_DISTANCE_FROM_LINE, width);
			shapeRenderer.rectLine(COLUMN_3 - MAX_DISTANCE_FROM_LINE, ROW_1 - MAX_DISTANCE_FROM_LINE, COLUMN_3
					- MAX_DISTANCE_FROM_LINE, ROW_3 + MAX_DISTANCE_FROM_LINE, width);

			// Skeleton lines
			width = 3.0f;
			shapeRenderer.setColor(1, 0, 0, 1);
			shapeRenderer.rectLine(COLUMN_1, ROW_2, COLUMN_3, ROW_2, width);
			shapeRenderer.rectLine(COLUMN_1, ROW_1, COLUMN_1, ROW_3, width);
			shapeRenderer.rectLine(COLUMN_2, ROW_1, COLUMN_2, ROW_3, width);
			shapeRenderer.rectLine(COLUMN_3, ROW_1, COLUMN_3, ROW_3, width);
			shapeRenderer.end();

			// Finish bounding circle
			Debug.drawCircle(finishPosition, FINISH_RADIUS, new Color(1, 0, 0, 1), 1);
		}
	}

	@Override
	public void DrawMaxTouchableArea() {
		if (!this.gearShifter.isDragged()) {
			float maxDistance = Constants.misc.SHIFTABLE_OBJECT_MAX_CAPABLE_DISTANCE;
			Debug.drawCircle(this.gearShifter.getPosition(), maxDistance, new Color(1, 1, 0, 1), 1);
		}
	}
}
