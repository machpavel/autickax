package cz.mff.cuni.autickax.gamelogic;

import java.util.LinkedList;

import cz.mff.cuni.autickax.Constants;
import cz.mff.cuni.autickax.entities.GameObject;
import cz.mff.cuni.autickax.linePackage.MyLine;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;

import cz.mff.cuni.autickax.pathway.DistanceMap;
import cz.mff.cuni.autickax.pathway.Pathway;
import cz.mff.cuni.autickax.drawing.Font;
import cz.mff.cuni.autickax.input.Input;
import cz.mff.cuni.autickax.scene.GameScreen;

public class SubLevel1 extends SubLevel {

	// Time
	private float timeElapsed = 0;

	/**
	 * Origin of the track
	 */
	private Vector2 startPoint;

	/**
	 * Finnish of the track
	 */
	private Vector2 finishPoint;

	/**
	 * Path representation
	 */
	private Pathway pathway;

	/**
	 * state of the phase: beginning, driving, finish, mistake - must repeat
	 */
	private SubLevel1States state;

	/**
	 * Player just starts this phase or made a mistake and was moved again
	 */
	public enum SubLevel1States {
		BEGINNING_STATE, DRIVING_STATE, // Driving in progress
		FINISH_STATE, // Player successfully finished the race
		MISTAKE_STATE;
	}

	/** Coordinates to check whether whole track was raced through */
	// TODO purely for visual purposes, erase when graphics is done
	private LinkedList<Vector2> wayPoints;

	/**
	 * Player must cross these
	 */
	private LinkedList<Circle> wayCircles;
	
	/** Index in wayCircles */
	private int currentCircle = 0;
	
	/**
	 * Record of movement through the track;
	 */
	private LinkedList<CheckPoint> checkPoints;
	/**
	 * true if time is being measure, false otherwise
	 */
	private boolean timeMeasured = false;

	/**
	 * Instance of gameScreen to be able to switch to another phase
	 */
	private GameScreen gameScr;

	/**
	 * For printing messages
	 */
	private String status = "";

	private Vector2 lastPoint;

	public SubLevel1(GameScreen gameScreen) {
		super(gameScreen);
		gameScr = gameScreen;
		pathway = gameScreen.getPathWay();

		startPoint = new Vector2(gameScreen.getStart().getX(), gameScreen
				.getStart().getY());
		finishPoint = new Vector2(gameScreen.getFinish().getX(), gameScreen
				.getFinish().getY());

		initWayPoints(Constants.START_POSITION_IN_CURVE,
				Constants.FINISH_POSITION_IN_CURVE, Constants.WAYPOINTS_COUNT);		
		wayCircles = createWayPointCircles(Constants.START_POSITION_IN_CURVE,
				Constants.FINISH_POSITION_IN_CURVE, Constants.WAYPOINTS_COUNT);

		checkPoints = new LinkedList<CheckPoint>();
		this.Level.getCar().move(startPoint.x, startPoint.y);
		lastPoint = new Vector2(startPoint);

		state = SubLevel1States.BEGINNING_STATE;

	}

	@Override
	public void update(float delta) {
		if (timeMeasured)
			timeElapsed += delta;

		for (GameObject gameObject : this.Level.getGameObjects()) {
			gameObject.update(delta);
		}
		this.Level.getCar().update(delta);
		this.Level.getStart().update(delta);
		this.Level.getFinish().update(delta);
		
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

	private void updateInMistakeState(float delta) {
		// TODO Auto-generated method stub

	}

	private void updateInFinishState(float delta) {
		// switch whenever ready
		if (Gdx.input.justTouched()) {
			gameScr.switchToPhase2(checkPoints, pathway.getDistanceMap());
		}

	}

	private void updateInDrivingState(float delta) {
		// stopped dragging
		if (!this.Level.getCar().isDragged()) {
			reset();
			return;
		}

		this.Level.getCar().update(delta);
		if (Gdx.input.getDeltaX() == 0 && Gdx.input.getDeltaY() == 0)
			return;

		Vector2 carPosition = this.Level.getCar().getPosition();

		// coordinates ok
		DistanceMap map = pathway.getDistanceMap();
		if (carPosition.x >= 0 && carPosition.x < map.getWidth()
				&& carPosition.y >= 0 && carPosition.y < map.getHeight()) {

			// TODO do not tick off wayCircles if not on track
			if (this.Level.getCar().collides(this.Level.getFinish())) {
				state = SubLevel1States.FINISH_STATE;
				timeMeasured = false;
			}

			// not on track OR all checkpoint not yet reached (if reached, we
			// may have reached the finish line)
			if (map.At(carPosition) > Constants.MAX_DISTANCE_FROM_PATHWAY) {
				reset();

			} else {
				checkPoints.add(new CheckPoint(timeElapsed, carPosition.x,
						carPosition.y));

				// if (checkWayPoints(x, y))

				lastPoint = carPosition;
			}
		}

	}

	private void updateInBeginnigState(float delta) {
		if (Gdx.input.justTouched()) {
			Vector2 touchPos = new Vector2(Input.getX(), Input.getY());

			if (startPoint.dst(touchPos.x, touchPos.y) <= Constants.MAX_DISTANCE_FROM_PATHWAY) {
				this.Level.getCar().setDragged(true);
				state = SubLevel1States.DRIVING_STATE;
				timeMeasured = true;
			}
		}

	}

	@Override
	public void draw(SpriteBatch batch) {
		Font font = new Font(this.Level.getFont());		

		for (GameObject gameObject : this.Level.getGameObjects()) {
			gameObject.draw(batch);
		}	
		this.Level.getStart().draw(batch);
		this.Level.getFinish().draw(batch);
		this.Level.getCar().draw(batch);

		// render the track
		// TODO render quicker
		// TODO move this to gamescreen class
		/*
		 * shapeRenderer.begin(ShapeType.Point); shapeRenderer.setColor(new
		 * Color(Color.WHITE)); for (int x = 0; x < (int) stageWidth; x++) { for
		 * (int y = 0; y < (int) stageHeight; y++) { if
		 * (pathway.getDistanceMap().At(x, y) < Float.MAX_VALUE )
		 * shapeRenderer.point(x, y, 0);
		 * 
		 * } } shapeRenderer.end();
		 */
		
		// TODO rewrite positioning into constants		
		float stageHeight = Gdx.graphics.getHeight();
		float stageWidth = Gdx.graphics.getWidth();
		// Draw time
		font.draw(batch, "time: " + String.format("%1$,.1f", timeElapsed), 10,
				(int) stageHeight - 32);

		// status = "Phase: " + getStateString() + " dragged: " +
		// this.Level.getCar().isDragged();
		status = updateStatus();
		font.draw(batch, status, 10, 64);
	}

	/**
	 * Sets this game phase to its beginning Called when player leaves the road
	 * or makes a discontinuous move
	 */
	public void reset() {
		if (state != SubLevel1States.FINISH_STATE) {

			state = SubLevel1States.BEGINNING_STATE;
			this.timeElapsed = 0;
			this.Level.getCar().move(startPoint.x, startPoint.y);
			this.Level.getCar().setDragged(false);
			checkPoints.clear();
			currentCircle = 0;
			timeMeasured = false;
			initWayPoints(Constants.START_POSITION_IN_CURVE,
					Constants.FINISH_POSITION_IN_CURVE,
					Constants.WAYPOINTS_COUNT);
			lastPoint = new Vector2(startPoint);
		}
	}

	/**
	 * 
	 * @return string representation of the current state of this sublevel
	 */
	private String getStateString() {
		switch (state) {
		case BEGINNING_STATE:
			return "Beginning";
		case DRIVING_STATE:
			return "Driving";
		case FINISH_STATE:
			return "Finished";
		case MISTAKE_STATE:
			return "Mistake";
		default:
			return "";
		}
	}

	private String updateStatus() {
		switch (state) {
		case BEGINNING_STATE:
			status = "Draw the path for your vehicle";
			break;
		case DRIVING_STATE:
			status = "Driving";
			break;
		case FINISH_STATE:
			status = "Finished - well done! " + checkPoints.size();
			break;
		case MISTAKE_STATE:
			status = "Mistake";
			break;
		}

		return status;
	}

	private void initWayPoints(float start, float finish, int nofWayPoints) {
		wayPoints = new LinkedList<Vector2>();
		float step = (finish - start) / nofWayPoints;
		Vector2 lastAdded = null;
		for (float f = start; f < finish; f += step) {
			lastAdded = pathway.GetPosition(f);
			wayPoints.add(lastAdded);
		}

	}

	@Override
	public void render() {
		shapeRenderer.begin(ShapeType.Filled);
		shapeRenderer.setColor(Color.BLUE);
		shapeRenderer.circle(startPoint.x / Input.xStretchFactor, startPoint.y
				/ Input.yStretchFactor, Constants.MAX_DISTANCE_FROM_PATHWAY);
		shapeRenderer.setColor(Color.YELLOW);
		shapeRenderer.circle(finishPoint.x / Input.xStretchFactor,
				finishPoint.y / Input.yStretchFactor,
				Constants.MAX_DISTANCE_FROM_PATHWAY);

		shapeRenderer.setColor(new Color(Color.DARK_GRAY));
		for (Vector2 vec : wayPoints) {
			shapeRenderer.circle(vec.x / Input.xStretchFactor, vec.y
					/ Input.yStretchFactor, 10);

		}

		/*
		 * for (int i = 0; i < this.wayCircles.size(); i++) { Circle c =
		 * this.wayCircles.get(i); shapeRenderer.circle(c.x /
		 * Input.xStretchFactor, c.y / Input.yStretchFactor, c.radius);
		 * 
		 * }
		 */

		shapeRenderer.setColor(Color.RED);
		for (CheckPoint ce : checkPoints) {
			shapeRenderer.circle((float) ce.x / Input.xStretchFactor,
					(float) ce.y / Input.yStretchFactor, 2);
		}

		shapeRenderer.end();
	}


	private LinkedList<Circle> createWayPointCircles(float start, float finish,
			int nofWayPoints) {
		LinkedList<Circle> circles = new LinkedList<Circle>();

		float step = (finish - start) / nofWayPoints;
		for (float f = start + step; f < finish - step; f += step) {
			circles.add(new Circle(pathway.GetPosition(f),
					Constants.MAX_DISTANCE_FROM_PATHWAY));
		}
		return circles;
	}
}
