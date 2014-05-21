package cz.mff.cuni.autickax.miniGames;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

import cz.mff.cuni.autickax.Autickax;
import cz.mff.cuni.autickax.Difficulty;
import cz.mff.cuni.autickax.constants.Constants;
import cz.mff.cuni.autickax.dialogs.MessageDialog;
import cz.mff.cuni.autickax.exceptions.IllegalDifficultyException;
import cz.mff.cuni.autickax.gamelogic.SubLevel;
import cz.mff.cuni.autickax.miniGames.support.RepairingMinigameObject;
import cz.mff.cuni.autickax.scene.GameScreen;

public final class RepairingMinigame extends Minigame {
	private static final float FAIL_VALUE = Constants.minigames.GEAR_SHIFT_FAIL_VALUE;

	private States state = States.JACK_TO_SPANNER;

	private RepairingMinigameObject handJack;
	private RepairingMinigameObject spanner;
	private RepairingMinigameObject damagedTire;
	private RepairingMinigameObject newTire;
	private ArrayList<RepairingMinigameObject> gameObjects = new ArrayList<RepairingMinigameObject>();
		

	public RepairingMinigame(GameScreen screen, SubLevel parent) {
		super(screen, parent);
		setDifficulty(this.level.getDifficulty());
		this.backgroundTexture = new TextureRegionDrawable(
				Autickax.getInstance().assets
						.getGraphics(Constants.minigames.REPAIRING_MINIGAME_BACKGROUND_TEXTURE));

		if (Autickax.settings.isShowTooltips())
			this.parent.setDialog(new MessageDialog(screen, parent,
					Constants.strings.TOOLTIP_MINIGAME_REPAIRING_WHAT_TO_DO));

		initializeGameObjects();
	}

	private void initializeGameObjects() {
		handJack = new RepairingMinigameObject(600, 400, 90, 80,
				Constants.minigames.REPAIRING_MINIGAME_HAND_JACK_TEXTURE, this, false);
		handJack.setIsActive(true);
		gameObjects.add(handJack);

		spanner = new RepairingMinigameObject(600, 300, 90, 80,
				Constants.minigames.REPAIRING_MINIGAME_SPANNER_TEXTURE, this, false);
		gameObjects.add(spanner);

		damagedTire = new RepairingMinigameObject(90, 80, 600, 200,
				Constants.minigames.REPAIRING_MINIGAME_DAMAGED_TIRE_TEXTURE, this, true);
		gameObjects.add(damagedTire);

		newTire = new RepairingMinigameObject(600, 100, 90, 80,
				Constants.minigames.REPAIRING_MINIGAME_NEW_TIRE_TEXTURE, this, true);
		gameObjects.add(newTire);

		//gameObjects.add(new RepairingMinigameObject(400, 400, 1));
		//gameObjects.add(new RepairingMinigameObject(200, 300, 1));
		//gameObjects.add(new RepairingMinigameObject(500, 100, 2));
	}

	public void switchToNextState() {
		System.out.println(this.state.toString());
		switch (this.state) {
		case JACK_TO_SPANNER:
			this.state = States.SPANNER_TO_TIRE;
			handJack.setIsActive(false);
			spanner.setIsActive(true);
			break;
		case SPANNER_TO_TIRE:
			this.state = States.TIRE_TO_TIRE;
			spanner.setIsActive(false);
			damagedTire.setIsActive(true);
			break;
		case TIRE_TO_TIRE:
			this.state = States.TIRE_TO_SPANNER;
			damagedTire.setIsActive(false);
			newTire.setIsActive(true);
			break;
		case TIRE_TO_SPANNER:
			this.state = States.SPANNER_TO_JACK;
			newTire.setIsActive(false);
			spanner.setIsActive(true);
			break;
		case SPANNER_TO_JACK:
			this.state = States.JACK_TO_LEAVING;
			spanner.setIsActive(false);
			handJack.setIsActive(true);
			break;
		case JACK_TO_LEAVING:
			handJack.setIsActive(false);
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
	}

	private void fail(String primaryMessage) {
		if (primaryMessage != null)
			this.resultMessage = primaryMessage;
		else
			this.resultMessage = Constants.strings.TOOLTIP_MINIGAME_REPAIRING_FAIL;
		this.result = ResultType.FAILED_WITH_VALUE;
		this.resultValue = FAIL_VALUE;
		leave();
	}

	private void win() {
		this.resultMessage = Constants.strings.TOOLTIP_MINIGAME_REPAIRING_SUCCESS;
		this.result = ResultType.PROCEEDED;		
		leave();
	}
	
	private void leave() {
		this.playResultSound();		
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
			break;
		case Beginner:
			break;
		case Normal:
			break;
		case Hard:
			break;
		case Extreme:
			break;
		default:
			throw new IllegalDifficultyException(difficulty.toString());
		}
		return;
	}

	private enum States {
		JACK_TO_SPANNER, SPANNER_TO_TIRE, TIRE_TO_TIRE, TIRE_TO_SPANNER, SPANNER_TO_JACK, JACK_TO_LEAVING, LEAVING;
	}
}
