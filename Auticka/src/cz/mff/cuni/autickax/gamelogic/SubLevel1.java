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
import cz.mff.cuni.autickax.drawing.TimeStatusBar;
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

	/** Path representation */
	private Pathway pathway;

	/** state of the phase: beginning, driving, finish, mistake - must repeat */
	private SubLevel1States state;

	/** Coordinates to check whether whole track was raced through */
	private LinkedList<Vector2> wayPoints;

	/** Record of movement through the track; */
	private LinkedList<CheckPoint> checkPoints;
	
	private TimeStatusBar timeStatusBar;
	
	/** true if time is being measure, false otherwise */
	private boolean timeMeasured = false;

	public SubLevel1(GameScreen gameScreen, float tLimit) {
		super(gameScreen);
		timeStatusBar = new TimeStatusBar(gameScreen,tLimit, true);
		playStartEngineSound();
		pathway = gameScreen.getPathWay();

		initWayPoints(Constants.START_POSITION_IN_CURVE,
				Constants.FINISH_POSITION_IN_CURVE, Constants.WAYPOINTS_COUNT);

		checkPoints = new LinkedList<CheckPoint>();	
		state = SubLevel1States.BEGINNING_STATE;
		timeLimit = tLimit;
		

		reset();
	}
	
	private void playStartEngineSound()
	{
		this.level.getGame().assets.soundAndMusicManager.playSound(Constants.SOUND_ENGINE_START, Constants.SOUND_ENGINE_VOLUME);
	}
	
	public void onDialogEnded() {
		switch (this.dialog.getDecision()) {
		case CONTINUE:
			break;
		case RESTART:
			reset();
			break;
		case GO_TO_MAIN_MENU:
			this.level.goToMainScreen();
			break;
		default:
			// TODO assert for type
			break;
		}
		this.dialog = null;
	}

	@Override
	public void onMinigameEnded() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update(float delta) {
		if (this.dialog != null) {
			this.dialog.update(delta);			
		} 		
		else {
			if (timeMeasured)
				timeElapsed += delta;
	
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
		timeStatusBar.update(timeElapsed);
		
	}

	private void updateInMistakeState(float delta) {
		if (Gdx.input.justTouched()) {
			reset();
		}
	}

	private void updateInFinishState(float delta) {
		this.level.switchToPhase2(checkPoints, pathway.getDistanceMap(), this);
	}

	private void updateInDrivingState(float delta) {
		// stopped dragging
		if (!this.level.getCar().isDragged()) {
			switchToMistakeState(Constants.PHASE_1_FINISH_NOT_REACHED);
			return;
		} else if (timeElapsed >= timeLimit) {
			switchToMistakeState(Constants.PHASE_1_TIME_EXPIRED);
			return;
		}

		this.level.getCar().update(delta);
		// did not move from last update
		if (Gdx.input.getDeltaX() == 0 && Gdx.input.getDeltaY() == 0)
			return;

		Vector2 carPosition = this.level.getCar().getPosition();

		// coordinates ok
		DistanceMap map = pathway.getDistanceMap();
		if (carPosition.x >= 0 && carPosition.x < map.getWidth()
				&& carPosition.y >= 0 && carPosition.y < map.getHeight()) {

			if(this.level.getCar().positionCollides(this.level.getFinish())){
				
				if (wayPoints.isEmpty()){
					state = SubLevel1States.FINISH_STATE;
					this.level.getGame().assets.soundAndMusicManager.playSound(Constants.SOUND_SUB1_CHEER, Constants.SOUND_DEFAULT_VOLUME);
					dialog = new DecisionDialog(this.level, this, Constants.PHASE_1_FINISH_REACHED, true);
					timeMeasured = false;
					this.level.getCar().setDragged(false);
				}
				else{
					switchToMistakeState(Constants.PHASE_1_FINISH_REACHED_BUT_NOT_CHECKPOINTS);
				}
			}


			// not on track OR all checkpoint not yet reached (if reached, we
			// may have reached the finish line)
			if (map.At(carPosition) > Constants.MAX_DISTANCE_FROM_PATHWAY) {
				switchToMistakeState(Constants.PHASE_1_OUT_OF_LINE);

			} else {				
				checkPoints.add(new CheckPoint(timeElapsed, carPosition));

				if (!wayPoints.isEmpty()) {
					boolean canRemove = true;
					while (canRemove && !wayPoints.isEmpty()) {
						Vector2 way = new Vector2(wayPoints.peekFirst());
						Vector2 pos = new Vector2(this.level.getCar()
								.getPosition());
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

			if (this.level.getCar().getPosition().dst(touchPos.x, touchPos.y) <= Constants.MAX_DISTANCE_FROM_PATHWAY) {
				this.level.getCar().setDragged(true);
				state = SubLevel1States.DRIVING_STATE;
				timeMeasured = true;
			}
		}
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

		if (dialog != null) {
			dialog.draw(batch);
		}
		
		if(miniGame != null){
			dialog.draw(batch);
		}
		
		timeStatusBar.draw(batch);
		
		
	}

	/**
	 * Sets this game phase to its beginning Called when player leaves the road
	 * or makes a discontinuous move
	 */
	public void reset() {
		this.miniGame = null;
		if (Autickax.settings.showTooltips)
			this.dialog = new MessageDialog(this.level, this, 
					Constants.TOOLTIP_PHASE_1_WHAT_TO_DO);
		state = SubLevel1States.BEGINNING_STATE;
		this.timeElapsed = 0;
		checkPoints.clear();
		initWayPoints(Constants.START_POSITION_IN_CURVE,
				Constants.FINISH_POSITION_IN_CURVE, Constants.WAYPOINTS_COUNT);
		
		for (GameObject gameObject : this.level.getGameObjects()) {
			gameObject.reset();
		}
		
		// Car positioning
		Vector2 carStartDirection = new Vector2(wayPoints.get(1)).sub(wayPoints.get(0)).nor();
		Vector2 oldPosition = this.level.getStart().getPosition();
		Vector2 newCarPositionFarther = new Vector2(oldPosition).sub(carStartDirection.scl(Constants.CAR_DISTANCE_FROM_START*2));
		this.level.getCar().move(newCarPositionFarther);
		Vector2 newCarPosition = new Vector2(oldPosition).sub(carStartDirection.scl(1/2));
		this.level.getCar().move(newCarPosition);
		
		

	}

	/**
	 * Player failed to finish the track
	 */
	private void switchToMistakeState(String str) {
		this.dialog = new DecisionDialog(this.level, this, str, false);
		this.state = SubLevel1States.MISTAKE_STATE;
		this.level.getCar().setDragged(false);
		timeMeasured = false;
		this.state.setMistake(str);
	}

	private void initWayPoints(float start, float finish, int nofWayPoints) {
		wayPoints = new LinkedList<Vector2>();
		float step = (finish - start) / nofWayPoints;
		Vector2 lastAdded = null;
		float skipTwo = 2*step;
		for (float f = start+skipTwo; f < finish; f += step) {
			lastAdded = pathway.GetPosition(f);
			wayPoints.add(lastAdded);
		}

	}

	@Override
	public void render() {
		shapeRenderer.begin(ShapeType.Filled);

		shapeRenderer.setColor(Color.RED);
		for (CheckPoint ce : checkPoints) {
			shapeRenderer.circle((float) ce.position.x
					* Input.xStretchFactorInv, (float) ce.position.y
					* Input.yStretchFactorInv, 2);
		}
		shapeRenderer.end();
	}

	/** Player just starts this phase or made a mistake and was moved again */
	public enum SubLevel1States {
		BEGINNING_STATE, DRIVING_STATE, // Driving in progress
		FINISH_STATE, // Player successfully finished the race
		MISTAKE_STATE;

		String mistakeMsg;

		public void setMistake(String str) {
			mistakeMsg = str;
		}

		public String getMistakeMsg() {
			return mistakeMsg;
		}
	}


}
