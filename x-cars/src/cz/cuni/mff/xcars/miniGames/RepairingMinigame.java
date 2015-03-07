package cz.cuni.mff.xcars.miniGames;

import java.text.DecimalFormat;
import java.util.ArrayList;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;

import cz.cuni.mff.xcars.Difficulty;
import cz.cuni.mff.xcars.Xcars;
import cz.cuni.mff.xcars.constants.Constants;
import cz.cuni.mff.xcars.debug.Debug;
import cz.cuni.mff.xcars.dialogs.MessageDialog;
import cz.cuni.mff.xcars.exceptions.IllegalDifficultyException;
import cz.cuni.mff.xcars.gamelogic.SubLevel;
import cz.cuni.mff.xcars.miniGames.support.RepairingMinigameObject;
import cz.cuni.mff.xcars.scene.GameScreen;
import cz.cuni.mff.xcars.screenObjects.ScreenAdaptiveLabel;

public final class RepairingMinigame extends Minigame {
	private static final float FAIL_VALUE = Constants.minigames.REPAIRING_MINIGAME_FAIL_VALUE;

	private States state = States.JACK_TO_SPANNER;

	private RepairingMinigameObject handJack;
	private RepairingMinigameObject spanner;
	private RepairingMinigameObject damagedTire;
	private RepairingMinigameObject newTire;
	private ArrayList<RepairingMinigameObject> gameObjects = new ArrayList<RepairingMinigameObject>();

	private float timeLimit;
	private float timeLabelXPosition = 20, timeLabelYPosition = 420;
	private ScreenAdaptiveLabel timeLabel;
	DecimalFormat timeFormat = new DecimalFormat("0.0");

	public RepairingMinigame(GameScreen screen, SubLevel parent) {
		super(screen, parent);
		this.leavingDelay = Constants.minigames.REPAIRING_MINIGAME_LEAVING_DELAY;
		setDifficulty(this.level.getDifficulty());
		this.backgroundTexture = new NinePatchDrawable(
				Xcars.getInstance().assets.getNinePatch(Constants.minigames.REPAIRING_MINIGAME_BACKGROUND_TEXTURE));

		if (Xcars.settings.isShowTooltips())
			this.parent.setDialog(new MessageDialog(screen, parent,
					Constants.strings.TOOLTIP_MINIGAME_REPAIRING_WHAT_TO_DO));

		this.timeLabel = ScreenAdaptiveLabel.getDialogLabel(this.timeFormat.format(this.timeLimit));
		// TODO remove and refactor
		this.timeLabel.setColor(1, 0, 0, 1);
		this.timeLabel.setPosition(timeLabelXPosition, timeLabelYPosition);
		this.stage.addActor(this.timeLabel);

		initializeGameObjects();
	}

	private void initializeGameObjects() {
		handJack = new RepairingMinigameObject(600, 400, 90, 80,
				Constants.minigames.REPAIRING_MINIGAME_HAND_JACK_TEXTURE, this, false);
		handJack.setIsActive(true);
		gameObjects.add(handJack);

		spanner = new RepairingMinigameObject(600, 300, 90, 80, Constants.minigames.REPAIRING_MINIGAME_SPANNER_TEXTURE,
				this, false);
		gameObjects.add(spanner);

		damagedTire = new RepairingMinigameObject(90, 80, 600, 200,
				Constants.minigames.REPAIRING_MINIGAME_DAMAGED_TIRE_TEXTURE, this, true);
		gameObjects.add(damagedTire);

		newTire = new RepairingMinigameObject(600, 100, 90, 80,
				Constants.minigames.REPAIRING_MINIGAME_NEW_TIRE_TEXTURE, this, true);
		gameObjects.add(newTire);

		// gameObjects.add(new RepairingMinigameObject(400, 400, 1));
		// gameObjects.add(new RepairingMinigameObject(200, 300, 1));
		// gameObjects.add(new RepairingMinigameObject(500, 100, 2));
	}

	public void switchToNextState() {
		if (Debug.DEBUG && Debug.repairingMinigame) {
			System.out.println(this.state.toString());
		}
		switch (this.state) {
		case JACK_TO_SPANNER:
			this.state = States.SPANNER_TO_TIRE;
			handJack.setIsActive(false);
			spanner.setIsActive(true);
			this.soundsManager.playSound(Constants.sounds.SOUND_MINIGAME_REPAIRING_JACK);
			break;
		case SPANNER_TO_TIRE:
			this.state = States.TIRE_TO_TIRE;
			spanner.setIsActive(false);
			damagedTire.setIsActive(true);
			this.soundsManager.playSound(Constants.sounds.SOUND_MINIGAME_REPAIRING_SPANNER);
			break;
		case TIRE_TO_TIRE:
			this.state = States.TIRE_TO_SPANNER;
			damagedTire.setIsActive(false);
			newTire.setIsActive(true);
			this.soundsManager.playSound(Constants.sounds.SOUND_MINIGAME_REPAIRING_TIRE_OUT);
			break;
		case TIRE_TO_SPANNER:
			this.state = States.SPANNER_TO_JACK;
			newTire.setIsActive(false);
			spanner.setIsActive(true);
			this.soundsManager.playSound(Constants.sounds.SOUND_MINIGAME_REPAIRING_TIRE_IN);
			break;
		case SPANNER_TO_JACK:
			this.state = States.JACK_TO_LEAVING;
			spanner.setIsActive(false);
			handJack.setIsActive(true);
			this.soundsManager.playSound(Constants.sounds.SOUND_MINIGAME_REPAIRING_SPANNER);
			break;
		case JACK_TO_LEAVING:
			handJack.setIsActive(false);
			this.soundsManager.playSound(Constants.sounds.SOUND_MINIGAME_REPAIRING_JACK);
			win();
			break;
		default:
			throw new IllegalStateException(state.toString());
		}
	}

	@Override
	public void update(float delta) {
		switch (this.state) {
		case JACK_TO_SPANNER:
		case SPANNER_TO_TIRE:
		case TIRE_TO_TIRE:
		case TIRE_TO_SPANNER:
		case SPANNER_TO_JACK:
		case JACK_TO_LEAVING:
			updateInNormalState(delta);
			break;
		case LEAVING:
			updateInLeavingState(delta);
			break;
		default:
			throw new IllegalStateException(state.toString());
		}

	}

	private void updateInNormalState(float delta) {
		for (RepairingMinigameObject gameObject : gameObjects) {
			gameObject.update(delta);
		}
		// this.timeLimit -= delta;
		if (this.timeLimit > 0)
			this.timeLabel.setText(this.timeFormat.format(this.timeLimit));
		else
			fail(null);
	}

	RepairingMinigameObject objectToDrag;

	public void setDraggedObject(RepairingMinigameObject object, Vector2 shift) {
		if (objectToDrag == null || shift.len2() < objectToDrag.getShift().len2()) {
			if (objectToDrag != null) {
				objectToDrag.setDragged(false);
			}
			object.setDragged(true);
			object.setShift(shift);
			objectToDrag = object;
		}
	}

	public void resetDraggedObject() {
		objectToDrag = null;
	}

	public boolean draggedObjectExist() {
		return this.objectToDrag != null;
	}

	private void fail(String primaryMessage) {
		if (primaryMessage != null)
			this.resultMessage = primaryMessage;
		else
			this.resultMessage = Constants.strings.TOOLTIP_MINIGAME_REPAIRING_FAIL;
		this.result = ResultType.FAILED_WITH_VALUE;
		this.resultValue = FAIL_VALUE;
		this.soundsManager.playSound(Constants.sounds.SOUND_MINIGAME_REPAIRING_FAIL);
		leave();
	}

	private void win() {
		this.resultMessage = Constants.strings.TOOLTIP_MINIGAME_REPAIRING_SUCCESS;
		this.result = ResultType.PROCEEDED;
		this.soundsManager.playSound(Constants.sounds.SOUND_MINIGAME_REPAIRING_SUCCESS);
		leave();
	}

	private void leave() {
		this.state = States.LEAVING;
	}

	@Override
	public void draw(SpriteBatch batch) {
		super.draw(batch);
		batch.begin();
		for (RepairingMinigameObject gameObject : gameObjects) {
			gameObject.draw(batch);
		}
		batch.end();
	}

	private void setDifficulty(Difficulty difficulty) {
		switch (difficulty) {
		case Kiddie:
			this.timeLimit = Constants.minigames.REPAIRING_MINIGAME_TIME_KIDDIE;
			break;
		case Beginner:
			this.timeLimit = Constants.minigames.REPAIRING_MINIGAME_TIME_BEGINNER;
			break;
		case Normal:
			this.timeLimit = Constants.minigames.REPAIRING_MINIGAME_TIME_NORMAL;
			break;
		case Hard:
			this.timeLimit = Constants.minigames.REPAIRING_MINIGAME_TIME_HARD;
			break;
		case Extreme:
			this.timeLimit = Constants.minigames.REPAIRING_MINIGAME_TIME_EXTREME;
			break;
		default:
			throw new IllegalDifficultyException(difficulty.toString());
		}
		return;
	}

	private enum States {
		JACK_TO_SPANNER, SPANNER_TO_TIRE, TIRE_TO_TIRE, TIRE_TO_SPANNER, SPANNER_TO_JACK, JACK_TO_LEAVING, LEAVING;
	}

	@Override
	protected void drawBackGroundTexture(SpriteBatch batch) {
		batch.begin();
		this.backgroundTexture.draw(batch, 0, 0, Constants.WORLD_WIDTH, Constants.WORLD_HEIGHT);
		batch.end();
	}

	@Override
	public void DrawMaxTouchableArea() {
		super.DrawMaxTouchableArea();
		for (RepairingMinigameObject gameObject : gameObjects) {
			gameObject.DrawMaxTouchableArea();
		}
	}

	@Override
	public void DrawDiagnostics() {
		if (Debug.repairingMinigame) {
			super.DrawDiagnostics();
			for (RepairingMinigameObject gameObject : gameObjects) {
				gameObject.DrawTarget();
				gameObject.DrawActiveObject();
			}
		}
	}
}
