package cz.mff.cuni.autickax.gamelogic;

import java.util.LinkedList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;

import cz.mff.cuni.autickax.Autickax;
import cz.mff.cuni.autickax.Constants;
import cz.mff.cuni.autickax.dialogs.DecisionDialog;
import cz.mff.cuni.autickax.dialogs.MessageDialog;
import cz.mff.cuni.autickax.entities.GameObject;
import cz.mff.cuni.autickax.gamelogic.SubLevel1.SubLevel1States;
import cz.mff.cuni.autickax.input.Input;
import cz.mff.cuni.autickax.pathway.DistanceMap;
import cz.mff.cuni.autickax.scene.GameScreen;

public class SubLevel2 extends SubLevel {

	private LinkedList<CheckPoint> checkpoints;
	private SubLevel1 phase1;

	private DistanceMap distMap;

	private CheckPoint from;
	private CheckPoint to;
	
	private Vector2 velocity;
	private float velocityMagnitude;
	private float penalizationFactor = 1f;

	private float timeElapsed = 0;
	private boolean timeMeasured = false;
	private LinkedList<Vector2> points = new LinkedList<Vector2>();

	private SubLevel2States state = SubLevel2States.BEGINNING_STATE;

	public enum SubLevel2States {
		BEGINNING_STATE, DRIVING_STATE, FINISH_STATE, MISTAKE_STATE
	}

	public SubLevel2(GameScreen gameScreen, LinkedList<CheckPoint> checkpoints,
			DistanceMap map, SubLevel1 lastPhase) {
		super(gameScreen);

		this.checkpoints = checkpoints;
		this.phase1 = lastPhase;
		this.distMap = map;

		this.from = this.checkpoints.removeFirst();
		this.to = this.checkpoints.removeFirst();
		this.level.getCar().move(this.from.position);
		computeVelocity();
	}

	@Override
	public void onDialogEnded() {
		switch (this.dialog.getDecision()) {
		case CONTINUE:
			// TODO show some
			break;
		case RESTART:
			this.level.switchToPhase(this.phase1);
			this.phase1.reset();
			return;
		case GO_TO_MAIN_MENU:
			this.level.goToMainScreen();
			return;
		default:
			// TODO assert for type
			break;
		}
		this.dialog = null;
	}

	@Override
	public void onMinigameEnded() {
		switch (this.miniGame.getResult()) {
		case FAILED:
			this.dialog = new DecisionDialog(this.level, this, Constants.PHASE_2_MINIGAME_FAILED, false);
			break;
		case PROCEEDED:
			// Just continue normally.
			break;
		case PROCEEDED_WITH_VALUE:
			float result = this.miniGame.getResultValue();
			// TODO: implementation of speed regulation or something like this
		default:
			// TODO assert state
			break;
		}
		this.miniGame = null;
	}
	
	

	@Override
	public void update(float delta) {
		if (this.dialog != null) {
			this.dialog.update(delta);
		} else if (this.miniGame != null) {
			this.miniGame.update(delta);			
		}		
		else{
			for (GameObject gameObject : this.level.getGameObjects()) {
				gameObject.update(delta);
			}
			this.level.getCar().update(delta);
			this.level.getStart().update(delta);
			this.level.getFinish().update(delta);

			switch (state) {
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

	private void updateInBeginnigState(float delta) {
		// TODO Maybe some delay and countdown animation
		state = SubLevel2States.DRIVING_STATE;
		timeMeasured = true;

	}

	private void updateInDrivingState(float delta) {
		timeElapsed += delta;

		// finish reached
		if (checkpoints.isEmpty()) {
			timeMeasured = false;
			state = SubLevel2States.FINISH_STATE;
			dialog = new MessageDialog(this.level, this, Constants.PHASE_2_FINISH_REACHED + String.format("%1$,.2f", timeElapsed));
		}
		else{
			Vector2 newPos = moveCarToNewPosition(delta);
			points.add(new Vector2(newPos));
			for (GameObject gameObject : this.level.getGameObjects()) {
				if (this.level.getCar().collides(gameObject)) {
					System.out.println("XX");
					this.miniGame = gameObject.getMinigame(this.level, this);
				}
			}
		}
		
	}

	private void updateInFinishState(float delta) {
		this.level.goToMainScreen();
	}

	private void updateInMistakeState(float delta) {
		// TODO inform user that he has to try again - or fail menu or something
//		if (Gdx.input.justTouched()) {
//			this.level.switchToPhase1(this.phase1);
//			this.phase1.reset();
//		}
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
		
		Autickax.font.draw(batch,
				"TOHLE SMAZAT: " + String.format("%1$,.1f", timeElapsed), 10,
				(int) Gdx.graphics.getHeight() - 32);
		batch.end();

		if (dialog != null) {
			dialog.draw(batch);
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
					/ (this.velocityMagnitude * this.penalizationFactor);

			// move only as much as you can
			if (timeNecessaire > timeAvailable) {
				float dist = this.velocityMagnitude * timeAvailable
						* penalizationFactor;
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
		this.level.getCar().setRotation(
				new Vector2(this.to.position).sub(this.from.position).angle());

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
		if (distanceFromCurveCenter > Constants.MAX_SURFACE_DISTANCE_FROM_PATHWAY) {
			penalizationFactor = Constants.OUT_OF_SURFACE_PENALIZATION_FACTOR
					/ (float) Math.log(distanceFromCurveCenter + 2);
		} else
			penalizationFactor = 1;
		penalizationFactor *= Constants.GLOBAL_SPEED_REGULATOR;
	}

}
