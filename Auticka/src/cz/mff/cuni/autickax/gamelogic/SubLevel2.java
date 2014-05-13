package cz.mff.cuni.autickax.gamelogic;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Vector;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

import cz.mff.cuni.autickax.Autickax;
import cz.mff.cuni.autickax.Difficulty;
import cz.mff.cuni.autickax.Level;
import cz.mff.cuni.autickax.PlayedLevel;
import cz.mff.cuni.autickax.constants.Constants;
import cz.mff.cuni.autickax.dialogs.CompleteLevelDialog;
import cz.mff.cuni.autickax.dialogs.DecisionDialog;
import cz.mff.cuni.autickax.dialogs.Dialog;
import cz.mff.cuni.autickax.dialogs.MessageDialog;
import cz.mff.cuni.autickax.drawing.TimeStatusBar;
import cz.mff.cuni.autickax.entities.GameObject;
import cz.mff.cuni.autickax.exceptions.IllegalCommandException;
import cz.mff.cuni.autickax.miniGames.Minigame;
import cz.mff.cuni.autickax.pathway.DistanceMap;
import cz.mff.cuni.autickax.scene.GameScreen;
import cz.mff.cuni.autickax.scene.LevelLoadingScreen;

public class SubLevel2 extends SubLevel {

	private LinkedList<CheckPoint> checkpoints;
	private SubLevel1 phase1;

	private DistanceMap distMap;

	private CheckPoint from;
	private CheckPoint to;

	private Vector2 velocity;
	private float velocityMagnitude;
	private float penalizationFactor = 1f;
	private LinkedList<Float> speedModifiers = new LinkedList<Float>();
	private float speedModifierValue = 1f;

	private LinkedList<Vector2> points = new LinkedList<Vector2>();

	// Objects which were used in collision but the car haven't left them yet.
	private ArrayList<GameObject> objectsInCollision = new ArrayList<GameObject>();

	private SubLevel2States state = SubLevel2States.ENGINE_RAGING_STATE;
	private float elapsedFromEngine = 0.0f;

	private Difficulty difficulty;

	// It is for decision in final state. If should be played next level or go
	// to main screen;
	private boolean playNextLevel = false;

	private TimeStatusBar timeStatusBar;
	private GameStatistics stats;
	
	//class supporting calculating and rendering tyre tracks on the road
	private TyreTracks tyreTracks;
	
	public enum SubLevel2States {
		ENGINE_RAGING_STATE, BEGINNING_STATE, DRIVING_STATE, FINISH_STATE, MISTAKE_STATE
	}

	public SubLevel2(GameScreen gameScreen, LinkedList<CheckPoint> checkpoints, DistanceMap map,
			SubLevel1 lastPhase, GameStatistics stats) {

		super(gameScreen);

		this.timeStatusBar = new TimeStatusBar(gameScreen);

		this.checkpoints = checkpoints;
		this.phase1 = lastPhase;
		this.distMap = map;
		this.stats = stats;

		this.difficulty = gameScreen.getDifficulty();
		this.from = this.checkpoints.removeFirst();
		this.to = this.checkpoints.removeFirst();

		speedModifiers.add(Constants.misc.GLOBAL_SPEED_REGULATOR);
		computeSpeedModifierValue();
		computeVelocity();
		Autickax.getInstance().assets.soundAndMusicManager.playSound(
				Constants.sounds.SOUND_SUB2_START, 1);

		// Car positioning
		this.level.getCar().reset();
		this.level.getCar().move(this.from.position);
		this.level.getCar().setNextPositionIsDirection();
		this.level.getCar().move(this.to.position);
		
		this.tyreTracks = new TyreTracks(this.from.position);
		this.tyreTracks.addPoint(this.to.position);
	}

	@Override
	public void onDialogEnded() {
		Dialog dialogLocal = this.dialogStack.peek();
		eraseDialog();
		switch (dialogLocal.getDecision()) {
		case CONTINUE:
			break;
		case RESTART:
			this.level.switchToPhase(this.phase1);
			this.phase1.reset();
			break;
		case GO_TO_MAIN_MENU:
			this.level.goToMainScreen();
			break;
		default:
			throw new IllegalCommandException(dialogLocal.getDecision().toString());
		}

	}

	@Override
	public void onMinigameEnded() {
		Minigame miniGameLocal = this.miniGame;
		eraseMinigame();
		switch (miniGameLocal.getResult()) {
		case FAILED:
			this.dialogStack.push(new DecisionDialog(this.level, this, miniGameLocal
					.getResultMessage(), false));
			stats.increaseFailed();
			break;
		case FAILED_WITH_VALUE:
			if (Autickax.settings.showTooltips && miniGameLocal.getResultMessage() != null)
				this.dialogStack.push(new MessageDialog(this.level, this, miniGameLocal
						.getResultMessage()));
			stats.increaseFailed();
			float failResult = miniGameLocal.getResultValue();
			speedModifiers.add(failResult);
			computeSpeedModifierValue();
			break;
		case PROCEEDED:
			if (Autickax.settings.showTooltips && miniGameLocal.getResultMessage() != null)
				this.dialogStack.push(new MessageDialog(this.level, this, miniGameLocal
						.getResultMessage()));
			stats.increaseSucceeded();
			break;
		case PROCEEDED_WITH_VALUE:
			if (Autickax.settings.showTooltips && miniGameLocal.getResultMessage() != null)
				this.dialogStack.push(new MessageDialog(this.level, this, miniGameLocal
						.getResultMessage()));
			stats.increaseSucceeded();
			float winResult = miniGameLocal.getResultValue();
			speedModifiers.add(winResult);
			computeSpeedModifierValue();
			break;
		default:
			throw new IllegalStateException(miniGameLocal.getResult().toString());
		}
	}

	@Override
	public void update(float delta) {
		// do not count time in this state
		if (state != SubLevel2States.ENGINE_RAGING_STATE)
			timeStatusBar.update(this.stats.getPhase2ElapsedTime());

		if (!this.dialogStack.isEmpty()) {
			this.dialogStack.peek().update(delta);
		} else if (this.miniGame != null) {
			this.miniGame.update(delta);
		} else {
			for (GameObject gameObject : this.level.getGameObjects()) {
				gameObject.update(delta);
			}
			this.level.getCar().update(delta);
			this.level.getStart().update(delta);
			this.level.getFinish().update(delta);

			switch (state) {
			case ENGINE_RAGING_STATE:
				updateInEngineRagingState(delta);
				break;
			case BEGINNING_STATE:
				updateInBeginnigState(delta);
				break;
			case DRIVING_STATE:
				updateInDrivingState(delta);
				break;
			case FINISH_STATE:
				updateInFinishState(delta);
				break;
			case MISTAKE_STATE:
				updateInMistakeState(delta);
				break;
			default:
				throw new IllegalStateException(state.toString());
			}
		}
	}

	private void updateInEngineRagingState(float delta) {
		elapsedFromEngine += delta;
		if (elapsedFromEngine >= Constants.sounds.SOUNDS_ENGINE_DELAY)
			state = SubLevel2States.BEGINNING_STATE;
	}

	private void updateInBeginnigState(float delta) {
		state = SubLevel2States.DRIVING_STATE;
	}

	private void updateInDrivingState(float delta) {

		this.stats.increasePhase2ElapsedTime(delta);
		// finish reached
		if (checkpoints.isEmpty()) {
			state = SubLevel2States.FINISH_STATE;
			this.updateScore();
			this.unlockNewLevel();
			Autickax.getInstance().assets.soundAndMusicManager.playSound(
					Constants.sounds.SOUND_SUB2_CHEER, Constants.sounds.SOUND_BIG_CHEER_VOLUME);
			dialogStack.push(new CompleteLevelDialog(this.level, this, this.stats, true));
		} else {
			Vector2 newPos = moveCarToNewPosition(delta);

			for (int i = 0; i < objectsInCollision.size(); i++) {
				if (!this.level.getCar().collides(objectsInCollision.get(i))) {
					objectsInCollision.get(i).setIsActive(true);
					objectsInCollision.remove(i);
					i--;
				}
			}

			for (GameObject gameObject : this.level.getGameObjects()) {
				if (gameObject.getIsActive() && this.level.getCar().collides(gameObject)) {
					Autickax.getInstance().assets.soundAndMusicManager
							.playCollisionSound(gameObject);
					gameObject.setIsActive(false);
					this.objectsInCollision.add(gameObject);
					stats.increaseCollisions();
					this.miniGame = gameObject.getMinigame(this.level, this);
					break;
				}
			}

			points.add(new Vector2(newPos));
		}

	}

	private void updateInFinishState(float delta) {
		if (this.playNextLevel)
			this.playNextLevel();
		else
			this.level.goToMainScreen();
	}

	private void updateInMistakeState(float delta) {
		this.dialogStack.push(new MessageDialog(this.level, this, "MISTAKE"));
	}

	@Override
	public void draw(SpriteBatch batch) {
		batch.begin();
		for (GameObject gameObject : this.level.getGameObjects()) {
			gameObject.draw(batch);
		}

		this.level.getStart().draw(batch);
		this.level.getFinish().draw(batch);
		this.level.getCar().draw(batch);

		batch.end();

		timeStatusBar.draw(batch);

		if (!dialogStack.isEmpty()) {
			dialogStack.peek().draw(batch);
		} else if (miniGame != null) {
			miniGame.draw(batch);
		}
	}

	public void render() {
		/*shapeRenderer.begin(ShapeType.Filled);
		shapeRenderer.setColor(Color.RED);
		for (CheckPoint ce : checkpoints) {
			shapeRenderer.circle((float) ce.position.x * Input.xStretchFactorInv,
					(float) ce.position.y * Input.yStretchFactorInv, 2);
		}

		shapeRenderer.setColor(Color.BLUE);
		for (Vector2 vec : points) {
			shapeRenderer.circle(vec.x * Input.xStretchFactorInv, vec.y * Input.yStretchFactorInv,
					2);
		}
		shapeRenderer.end();*/
		tyreTracks.render(shapeRenderer);

	}

	private Vector2 moveCarToNewPosition(float time) {
		float timeAvailable = time;
		Vector2 newPosition = null;

		while (timeAvailable > 0 && !this.checkpoints.isEmpty()) {
			// TODO assert car is always between TO AND FROM && timeAvailable
			// >=0
			Vector2 carPosition = this.level.getCar().getPosition();
			// compute time necessary to reach checkpoint To
			float distToTo = new Vector2(carPosition).sub(this.to.position).len();
			float timeNecessaire = distToTo
					/ (this.velocityMagnitude * this.penalizationFactor * this.speedModifierValue);

			// move only as much as you can
			if (timeNecessaire > timeAvailable) {
				float dist = this.velocityMagnitude * timeAvailable * this.penalizationFactor
						* this.speedModifierValue;
				Vector2 traslationVec = new Vector2(this.velocity).nor().scl(dist);
				newPosition = new Vector2(carPosition);
				newPosition.add(traslationVec);
				timeAvailable = 0;
			}
			// move to checkpoint to and subtract the time, recalculate
			// velocity, and checkpoints
			else {
				this.level.getCar().move(this.to.position);
				newPosition = to.position;
				this.from = this.to;
				this.to = this.checkpoints.removeFirst();
				computeVelocity();
				timeAvailable -= timeNecessaire;
			}
		}
		this.tyreTracks.addPoint(newPosition);
		this.level.getCar().move(newPosition);
		return newPosition;
	}

	/**
	 * Computes exact velocity from sublevel1
	 */
	private void computeVelocity() {
		float time = this.to.time - this.from.time;
		velocity = new Vector2(this.to.position).sub(this.from.position).div(time);
		velocityMagnitude = velocity.len();

		float distanceFromCurveCenter = distMap.At(this.level.getCar().getPosition());
		if (distanceFromCurveCenter > difficulty.getMaxDistanceFromSurface()) {
			this.penalizationFactor = Constants.misc.OUT_OF_SURFACE_PENALIZATION_FACTOR
					/ (float) Math.log(distanceFromCurveCenter + 2);
		} else
			this.penalizationFactor = 1f;

	}

	/**
	 * Combine all speed modifiers into a single value - penalizationFactor
	 */
	private void computeSpeedModifierValue() {
		float mod = 1.0f;
		for (Float fl : speedModifiers)
			mod *= fl;
		this.speedModifierValue = mod;
	}

	public void onLevelComplete() {
		Dialog dialogLocal = this.dialogStack.peek();
		eraseDialog();

		updateScore();

		unlockNewLevel();

		switch (dialogLocal.getDecision()) {
		case CONTINUE:
			if (this.isNextLevelAvaible())
				this.playNextLevel = true;
			else
				this.dialogStack.push(new MessageDialog(this.level, this, this.level
						.getDifficulty().toString() + Constants.strings.DIFFICULTY_FINISHED));
			break;
		case RESTART:
			this.level.switchToPhase(this.phase1);
			break;
		case GO_TO_MAIN_MENU:
			this.level.goToMainScreen();
			break;
		default:
			throw new IllegalCommandException(dialogLocal.getDecision().toString());
		}
	}

	private void unlockNewLevel() {
		if (this.isNextLevelAvaible()
				&& this.level.getLevelIndex() == this.level.getDifficulty().getPlayedLevels()
						.size() - 1) {
			this.level.getDifficulty().getPlayedLevels().add(new PlayedLevel(0, (byte) 0));
			if (Autickax.settings.showTooltips) {
				this.dialogStack.push(new MessageDialog(this.level, this,
						Constants.strings.NEW_LEVEL_UNLOCK));
			}
		}
	}

	private void updateScore() {
		byte numberOfStars = this.stats.getNumberOfStars();
		if (numberOfStars > this.level.getPlayedLevel().starsNumber)
			this.level.getPlayedLevel().starsNumber = numberOfStars;

		float score = this.stats.getScoreFromTime();
		if (score > this.level.getPlayedLevel().score)
			this.level.getPlayedLevel().score = score;
		Autickax.playedLevels.storeLevels();
	}

	public void playNextLevel() {
		if (Autickax.levelLoadingScreen != null) {
			Autickax.levelLoadingScreen.dispose();
			Autickax.levelLoadingScreen = null;
		}

		Autickax.levelLoadingScreen = new LevelLoadingScreen(this.level.getLevelIndex() + 1,
				difficulty);

		Autickax.getInstance().setScreen(Autickax.levelLoadingScreen);
	}

	private boolean isNextLevelAvaible() {
		Vector<Level> availableLevels = this.level.getDifficulty().getAvailableLevels();
		Vector<PlayedLevel> playedLevels = this.level.getDifficulty().getPlayedLevels();

		return this.level.getLevelIndex() < playedLevels.size()
				&& this.level.getLevelIndex() < availableLevels.size() - 1;
	}

}
