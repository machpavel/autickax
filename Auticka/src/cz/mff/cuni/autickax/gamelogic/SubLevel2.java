package cz.mff.cuni.autickax.gamelogic;

import java.util.LinkedList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;

import cz.mff.cuni.autickax.Autickax;
import cz.mff.cuni.autickax.Constants;
import cz.mff.cuni.autickax.entities.GameObject;
import cz.mff.cuni.autickax.input.Input;
import cz.mff.cuni.autickax.pathway.DistanceMap;
import cz.mff.cuni.autickax.scene.GameScreen;

public class SubLevel2 extends SubLevel {

	private LinkedList<CheckPoint> checkpoints;

	private DistanceMap distMap;

	private CheckPoint from;
	private CheckPoint to;

	private Vector2 velocity;
	private float velocityMagnitude;
	private float penalizationFactor = 1f;

	private float timeElapsed = 0;
	private LinkedList<Vector2> points = new LinkedList<Vector2>();

	public SubLevel2(GameScreen gameScreen, LinkedList<CheckPoint> checkpoints,
			DistanceMap map) {
		super(gameScreen);

		this.checkpoints = checkpoints;

		this.distMap = map;

		this.from = this.checkpoints.removeFirst();
		this.to = this.checkpoints.removeFirst();
		this.Level.getCar().move(this.from.position);
		computeVelocity();
	}

	@Override
	public void update(float time) {
		timeElapsed += time;
		if (!checkpoints.isEmpty()) {
			Vector2 newPos = moveCarToNewPosition(time);
			points.add(new Vector2(newPos));

		}

	}

	@Override
	public void draw(SpriteBatch batch) {
		for (GameObject gameObject : this.Level.getGameObjects()) {
			gameObject.draw(batch);
		}
		this.Level.getCar().draw(batch);
		this.Level.getStart().draw(batch);
		this.Level.getFinish().draw(batch);
		Autickax.font.draw(batch, "time: " + String.format("%1$,.1f", timeElapsed), 10,
				(int) Gdx.graphics.getHeight() - 32);
	}

	@Override
	public void render() {
		shapeRenderer.begin(ShapeType.Filled);
		shapeRenderer.setColor(Color.RED);
		for (CheckPoint ce : checkpoints) {
			shapeRenderer.circle((float) ce.position.x * Input.xStretchFactorInv,
					(float) ce.position.y * Input.yStretchFactorInv, 2);
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
			Vector2 carPosition = this.Level.getCar().getPosition();
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
				this.Level.getCar().move(this.to.position);
				newPosition = to.position;
				this.from = this.to;
				this.to = this.checkpoints.removeFirst();
				computeVelocity();
				timeAvailable -= timeNecessaire;
			}
		}		
		this.Level.getCar().move(newPosition);
		this.Level.getCar().setRotation(new Vector2(this.to.position).sub(this.from.position).angle());
		
		return newPosition;
	}

	/**
	 * Computes exact velocity from sublevel1
	 */
	private void computeVelocity() {
		float time = this.to.time - this.from.time;
		velocity = new Vector2(this.to.position).sub(this.from.position).div(time);
		velocityMagnitude = velocity.len();

		float distanceFromCurveCenter = distMap.At(this.Level.getCar()
				.getPosition());
		if (distanceFromCurveCenter > Constants.MAX_SURFACE_DISTANCE_FROM_PATHWAY) {
			penalizationFactor = Constants.OUT_OF_SURFACE_PENALIZATION_FACTOR
					/ (float) Math.log(distanceFromCurveCenter + 2);
		} else
			penalizationFactor = 1;
		penalizationFactor *= Constants.GLOBAL_SPEED_REGULATOR;
	}
}
