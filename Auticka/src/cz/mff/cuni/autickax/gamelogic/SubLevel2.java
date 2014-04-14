package cz.mff.cuni.autickax.gamelogic;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Vector;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;

import cz.mff.cuni.autickax.Autickax;
import cz.mff.cuni.autickax.Difficulty;
import cz.mff.cuni.autickax.LevelLoading;
import cz.mff.cuni.autickax.PlayedLevel;
import cz.mff.cuni.autickax.constants.Constants;
import cz.mff.cuni.autickax.dialogs.CompleteLevelDialog;
import cz.mff.cuni.autickax.dialogs.DecisionDialog;
import cz.mff.cuni.autickax.dialogs.Dialog;
import cz.mff.cuni.autickax.dialogs.MessageDialog;
import cz.mff.cuni.autickax.drawing.TimeStatusBar;
import cz.mff.cuni.autickax.entities.GameObject;
import cz.mff.cuni.autickax.input.Input;
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
	
	private ArrayList<GameObject> objectsInCollision = new ArrayList<GameObject>(); // Objects which were used in collision but the car haven't left them yet.

	private SubLevel2States state = SubLevel2States.ENGINE_RAGING_STATE;
	private float elapsedFromEngine = 0.0f;
	
	private Difficulty difficulty;
	
	private TimeStatusBar timeStatusBar;
	private GameStatistics stats;

	public enum SubLevel2States {
		ENGINE_RAGING_STATE,
		BEGINNING_STATE, DRIVING_STATE, FINISH_STATE, MISTAKE_STATE
	}

	public SubLevel2(GameScreen gameScreen, LinkedList<CheckPoint> checkpoints,
			DistanceMap map, SubLevel1 lastPhase, GameStatistics stats) {
		
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
		this.level.getGame().assets.soundAndMusicManager.playSound(Constants.sounds.SOUND_SUB2_START, 1);
		
		// Car positioning
		this.level.getCar().reset();
		this.level.getCar().move(this.from.position);
		this.level.getCar().setNextPositionIsDirection();
		this.level.getCar().move(this.to.position);

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
			break;
		}

	}

	@Override
	public void onMinigameEnded() {
		Minigame miniGameLocal = this.miniGame;
		eraseMinigame();
		switch (miniGameLocal.getResult()) {
		case FAILED:
			if (Autickax.settings.showTooltips && miniGameLocal.getResultMessage() != null)
				this.dialogStack.push(new DecisionDialog(this.level, this, miniGameLocal.getResultMessage(), false));
			stats.increaseFailed();
			this.level.getGame().assets.soundAndMusicManager.playSound(Constants.sounds.SOUND_MINIGAME_FAIL, Constants.sounds.SOUND_DEFAULT_VOLUME);
			break;
		case PROCEEDED:
			if (Autickax.settings.showTooltips && miniGameLocal.getResultMessage() != null)
				this.dialogStack.push(new MessageDialog(this.level, this, miniGameLocal.getResultMessage()));
			this.level.getGame().assets.soundAndMusicManager.playSound(Constants.sounds.SOUND_MINIGAME_SUCCESS, Constants.sounds.SOUND_DEFAULT_VOLUME);

			stats.increaseSucceeded();
			break;
		case PROCEEDED_WITH_VALUE:
			if (Autickax.settings.showTooltips && miniGameLocal.getResultMessage() != null)
				this.dialogStack.push(new MessageDialog(this.level, this, miniGameLocal.getResultMessage()));

			stats.increaseSucceeded();
			this.level.getGame().assets.soundAndMusicManager.playSound(Constants.sounds.SOUND_MINIGAME_SUCCESS, Constants.sounds.SOUND_DEFAULT_VOLUME);
			float winResult = miniGameLocal.getResultValue();
				speedModifiers.add(winResult);
				computeSpeedModifierValue();
			break;
		case FAILED_WITH_VALUE:
			if (Autickax.settings.showTooltips && miniGameLocal.getResultMessage() != null)
				this.dialogStack.push(new MessageDialog(this.level, this, miniGameLocal.getResultMessage()));

			stats.increaseFailed();
			this.level.getGame().assets.soundAndMusicManager.playSound(Constants.sounds.SOUND_MINIGAME_FAIL, Constants.sounds.SOUND_DEFAULT_VOLUME);
			float failResult = miniGameLocal.getResultValue();
				speedModifiers.add(failResult);
				computeSpeedModifierValue();
			break;
		default:
			// TODO assert state
			break;
		}
	}
	

	
	

	@Override
	public void update(float delta) {
		//do not count time in this state
		if (state != SubLevel2States.ENGINE_RAGING_STATE)
			timeStatusBar.update(this.stats.getPhase2ElapsedTime());
		
		if (!this.dialogStack.isEmpty()) {
			this.dialogStack.peek().update(delta);
		} else if (this.miniGame != null) {
			this.miniGame.update(delta);

			this.stats.increasePhase2ElapsedTime(delta);
		}		
		else{
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
				// TODO implementation of exception
				break;
			}			
		}
	}
	
	private void updateInEngineRagingState(float delta)
	{
		elapsedFromEngine += delta;
		if (elapsedFromEngine >= Constants.sounds.SOUNDS_ENGINE_DELAY)
			state = SubLevel2States.BEGINNING_STATE;
	}
	
	private void updateInBeginnigState(float delta) {
		// TODO Maybe some delay and countdown animation

		state = SubLevel2States.DRIVING_STATE;

	}

	private void playSound(GameObject collisionOrigin)
	{
		if (Autickax.settings.playSounds) {
			String soundName = collisionOrigin.getSoundName();
			if (!soundName.equals(Constants.sounds.SOUND_NO_SOUND))
			{
				this.level.getGame().assets.soundAndMusicManager.playSound(soundName, Constants.sounds.SOUND_GAME_OBJECT_INTERACTION_DEFAULT_VOLUME);
			}
		}
			
	}
	
	private void updateInDrivingState(float delta) {

		this.stats.increasePhase2ElapsedTime(delta);
		// finish reached
		if (checkpoints.isEmpty()) {
			state = SubLevel2States.FINISH_STATE;
			this.updateScore();
			this.unlockNewLevel();
			this.level.getGame().assets.soundAndMusicManager.playSound(Constants.sounds.SOUND_SUB2_CHEER, Constants.sounds.SOUND_BIG_CHEER_VOLUME);
			dialogStack.push(new CompleteLevelDialog(this.level, this, this.stats, this.isNextLevelAvaible()));
		}
		else{
			Vector2 newPos = moveCarToNewPosition(delta);
				
			
			for (int i = 0; i < objectsInCollision.size(); i++) {
				if(!this.level.getCar().collides(objectsInCollision.get(i))){
					objectsInCollision.get(i).setIsActive(true);
					objectsInCollision.remove(i);
					i--;
				}
			}
						
			for (GameObject gameObject : this.level.getGameObjects()) {			
				if ( gameObject.getIsActive() && this.level.getCar().collides(gameObject)) {
					playSound(gameObject);
					gameObject.setIsActive(false);
					this.objectsInCollision.add(gameObject);
					stats.increaseCollisions();
					this.miniGame = gameObject.getMinigame(this.level, this);
				}
			}
			
			points.add(new Vector2(newPos));
		}
		
	}

	

	
	private void updateInFinishState(float delta) {
		//TODO not to main screen
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
		}
		else if (miniGame != null) {
			miniGame.draw(batch);
		}
	}

	public void render() {
		// TODO if this.state == minigame

		shapeRenderer.begin(ShapeType.Filled);
		shapeRenderer.setColor(Color.RED);
		for (CheckPoint ce : checkpoints) {
			shapeRenderer.circle((float) ce.position.x
					* Input.xStretchFactorInv, (float) ce.position.y
					* Input.yStretchFactorInv, 2);
		}

		shapeRenderer.setColor(Color.BLUE);
		for (Vector2 vec : points) {
			shapeRenderer.circle(vec.x * Input.xStretchFactorInv, vec.y
					* Input.yStretchFactorInv, 2);
		}
		shapeRenderer.end();

	}

	private Vector2 moveCarToNewPosition(float time) {
		float timeAvailable = time;
		Vector2 newPosition = null;

		while (timeAvailable > 0 && !this.checkpoints.isEmpty()) {
			// TODO assert car is always between TO AND FROM && timeAvailable
			// >=0
			Vector2 carPosition = this.level.getCar().getPosition();
			// compute time necessary to reach checkpoint To
			float distToTo = new Vector2(carPosition).sub(this.to.position)
					.len();
			float timeNecessaire = distToTo
					/ (this.velocityMagnitude * this.penalizationFactor * this.speedModifierValue);

			// move only as much as you can
			if (timeNecessaire > timeAvailable) {
				float dist = this.velocityMagnitude * timeAvailable
						* this.penalizationFactor * this.speedModifierValue;
				Vector2 traslationVec = new Vector2(this.velocity).nor().scl(
						dist);
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
		this.level.getCar().move(newPosition);		
		return newPosition;
	}

	/**
	 * Computes exact velocity from sublevel1
	 */
	private void computeVelocity() {
		float time = this.to.time - this.from.time;
		velocity = new Vector2(this.to.position).sub(this.from.position).div(
				time);
		velocityMagnitude = velocity.len();

		float distanceFromCurveCenter = distMap.At(this.level.getCar()
				.getPosition());
		if (distanceFromCurveCenter > difficulty.getMaxDistanceFromSurface()) {
			this.penalizationFactor = Constants.misc.OUT_OF_SURFACE_PENALIZATION_FACTOR
					/ (float) Math.log(distanceFromCurveCenter + 2);
		}
		else
			this.penalizationFactor = 1f;
			
	}
	
	/**
	 * Combine all speed modifiers into a single value - penalizationFactor
	 */
	private void computeSpeedModifierValue()
	{
		float mod = 1.0f;
		for (Float fl: speedModifiers)
			mod *= fl;
		this.speedModifierValue = mod;
	}
	
	
	public void onLevelComplete(){
		Dialog dialogLocal = this.dialogStack.peek();
		eraseDialog();
		
		updateScore();
		
		unlockNewLevel();
		
		switch (dialogLocal.getDecision()) {
			case CONTINUE:				
				this.playNextLevel();
				break;
			case RESTART:
				this.level.switchToPhase(this.phase1);				
				break;
			case GO_TO_MAIN_MENU:
				this.level.goToMainScreen();
				break;
			default:
				break;
		}
		
	}

	private void unlockNewLevel() {
		if (this.isNextLevelAvaible() &&
				this.level.getLevelIndex() == this.level.getDifficulty().getPlayedLevels().size() - 1) {
			this.level.getDifficulty().getPlayedLevels().add(new PlayedLevel(0, (byte)0));
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
	
	
	
	public void playNextLevel(){
		if (Autickax.levelLoadingScreen != null) {
			Autickax.levelLoadingScreen.dispose();
			Autickax.levelLoadingScreen = null;
		}

		Autickax.levelLoadingScreen = new LevelLoadingScreen(this.level.getLevelIndex() + 1, difficulty);

		this.level.getGame().setScreen(Autickax.levelLoadingScreen);
	}
	
	private boolean isNextLevelAvaible(){
		Vector<LevelLoading> availableLevels = this.level.getDifficulty().getAvailableLevels();
		Vector<PlayedLevel> playedLevels = this.level.getDifficulty().getPlayedLevels();
		
		return this.level.getLevelIndex() < playedLevels.size() &&
				this.level.getLevelIndex() < availableLevels.size() - 1;
	}

}
