package cz.mff.cuni.autickax.gamelogic;

import java.util.LinkedList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;

import cz.mff.cuni.autickax.Constants;
import cz.mff.cuni.autickax.drawing.Font;
import cz.mff.cuni.autickax.entities.GameObject;
import cz.mff.cuni.autickax.input.Input;
import cz.mff.cuni.autickax.pathway.DistanceMap;
import cz.mff.cuni.autickax.pathway.Pathway;
import cz.mff.cuni.autickax.scene.GameScreen;

public class SubLevel1 extends SubLevel {

	// Time
	private float timeElapsed = 0;

	/** Time available for player to finish the race */
	private float timeLimit;



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
		
		String mistakeMsg;
		public void setMistake(String str)
		{
			mistakeMsg = str;
		}
		
		public String getMistakeMsg()
		{
			return mistakeMsg;
		}
	}

	/** Coordinates to check whether whole track was raced through */
	private LinkedList<Vector2> wayPoints;
	
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



	public SubLevel1(GameScreen gameScreen, float tLimit) {
		super(gameScreen);
		gameScr = gameScreen;
		pathway = gameScreen.getPathWay();

		initWayPoints(Constants.START_POSITION_IN_CURVE,
				Constants.FINISH_POSITION_IN_CURVE, Constants.WAYPOINTS_COUNT);		

		checkPoints = new LinkedList<CheckPoint>();
		this.Level.getCar().move(this.Level.getStart().getPosition());


		state = SubLevel1States.BEGINNING_STATE;
		timeLimit = tLimit;

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
		if (Gdx.input.justTouched()) {
			reset();
		}
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
			switchToMistakeState("You have not reached finish.");
			return;
		}
		else if (timeElapsed >= timeLimit)
		{
			switchToMistakeState("Time limit exceeded.");
			return;
		}

		this.Level.getCar().update(delta);
		//did not move from last update
		if (Gdx.input.getDeltaX() == 0 && Gdx.input.getDeltaY() == 0)
			return;

		Vector2 carPosition = this.Level.getCar().getPosition();

		// coordinates ok
		DistanceMap map = pathway.getDistanceMap();
		if (carPosition.x >= 0 && carPosition.x < map.getWidth()
				&& carPosition.y >= 0 && carPosition.y < map.getHeight()) {

			
			if (wayPoints.isEmpty() && this.Level.getCar().positionCollides(this.Level.getFinish())) {
				state = SubLevel1States.FINISH_STATE;
				timeMeasured = false;
			}
			
			

			// not on track OR all checkpoint not yet reached (if reached, we
			// may have reached the finish line)
			if (map.At(carPosition) > Constants.MAX_DISTANCE_FROM_PATHWAY) {
				switchToMistakeState("You are too far from track.");

			} else {
				checkPoints.add(new CheckPoint(timeElapsed, carPosition.x,
						carPosition.y));

				if (!wayPoints.isEmpty())
				{
					boolean canRemove = true;
					while(canRemove && !wayPoints.isEmpty())
					{
						Vector2 way = new Vector2(wayPoints.peekFirst());
						Vector2 pos = new Vector2(this.Level.getCar().getPosition());
						if (way.sub(pos).len() <= Constants.MAX_DISTANCE_FROM_PATHWAY)
							wayPoints.removeFirst();
						else
							canRemove = false;
					}
				}
			}
		}

	}

	private void updateInBeginnigState(float delta) {
		if (Gdx.input.justTouched()) {
			Vector2 touchPos = new Vector2(Input.getX(), Input.getY());

			if (this.Level.getCar().getPosition().dst(touchPos.x, touchPos.y) <= Constants.MAX_DISTANCE_FROM_PATHWAY) {
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

		
		// TODO rewrite positioning into constants		
		float stageHeight = Gdx.graphics.getHeight();
		float stageWidth = Gdx.graphics.getWidth();
		// Draw time
		font.draw(batch, "time: " + String.format("%1$,.1f", timeElapsed) + " limit: " + String.format("%1$,.1f", timeLimit), 10,
				(int) stageHeight - 32);

		status = updateStatus();
		font.draw(batch, status, 10, 64);
	}

	/**
	 * Sets this game phase to its beginning Called when player leaves the road
	 * or makes a discontinuous move
	 */
	public void reset() {

		state = SubLevel1States.BEGINNING_STATE;
		this.timeElapsed = 0;
		checkPoints.clear();
		initWayPoints(Constants.START_POSITION_IN_CURVE,
				Constants.FINISH_POSITION_IN_CURVE, Constants.WAYPOINTS_COUNT);
		this.Level.getCar().move(this.Level.getStart().getPosition());

	}
	
	/**
	 * Player failed to finish the track
	 */
	private void switchToMistakeState(String str)
	{
		this.state = SubLevel1States.MISTAKE_STATE;
		this.Level.getCar().setDragged(false);
		timeMeasured = false;
		this.state.setMistake(str);
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
			status = "Continue to finish";
			break;
		case FINISH_STATE:
			status = "Finished - well done! ";
			break;
		case MISTAKE_STATE:
			status = state.getMistakeMsg();
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

		shapeRenderer.setColor(Color.RED);
		for (CheckPoint ce : checkPoints) {
			shapeRenderer.circle((float) ce.position.x * Input.xStretchFactorInv,
					(float) ce.position.y * Input.yStretchFactorInv, 2);
		}

		shapeRenderer.end();
	}

}
