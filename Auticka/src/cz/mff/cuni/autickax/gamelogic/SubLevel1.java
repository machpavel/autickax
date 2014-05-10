package cz.mff.cuni.autickax.gamelogic;

import java.util.ArrayList;
import java.util.LinkedList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;

import cz.mff.cuni.autickax.Autickax;
import cz.mff.cuni.autickax.constants.Constants;
import cz.mff.cuni.autickax.dialogs.DecisionDialog;
import cz.mff.cuni.autickax.dialogs.Dialog;
import cz.mff.cuni.autickax.dialogs.MessageDialog;
import cz.mff.cuni.autickax.drawing.TimeStatusBar;
import cz.mff.cuni.autickax.entities.GameObject;
import cz.mff.cuni.autickax.entities.GameTerminatingObject;
import cz.mff.cuni.autickax.exceptions.IllegalCommandException;
import cz.mff.cuni.autickax.input.Input;
import cz.mff.cuni.autickax.pathway.DistanceMap;
import cz.mff.cuni.autickax.pathway.Pathway;
import cz.mff.cuni.autickax.scene.GameScreen;

public class SubLevel1 extends SubLevel {

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
	
	private GameStatistics stats;

	
	private ArrayList<GameTerminatingObject> terminators = new ArrayList<GameTerminatingObject>();
	
	public SubLevel1(GameScreen gameScreen, float tLimit) {
		super(gameScreen);
		timeStatusBar = new TimeStatusBar(gameScreen,tLimit, true);
		pathway = gameScreen.getPathWay();


		this.stats = new GameStatistics(gameScreen.getDifficulty(), tLimit); 
		checkPoints = new LinkedList<CheckPoint>();	
		state = SubLevel1States.BEGINNING_STATE;
		
				
				
		initTerminatingGameObjects();
		initWayPoints(Constants.misc.START_POSITION_IN_CURVE,
				Constants.misc.FINISH_POSITION_IN_CURVE, Constants.misc.WAYPOINTS_COUNT);
		
		reset();
	}
	
	private void initTerminatingGameObjects()
	{
		//collision checking
		for (GameObject gameObject : this.level.getGameObjects()) {			
			if (gameObject instanceof GameTerminatingObject) {
				terminators.add((GameTerminatingObject)gameObject);
			}
		}
	}
	
	private void playStartEngineSound()
	{
		this.level.getGame().assets.soundAndMusicManager.playSound(Constants.sounds.SOUND_ENGINE_START, Constants.sounds.SOUND_ENGINE_VOLUME);
	}
	
	public void onDialogEnded() {
		Dialog dialogLocal = this.dialogStack.peek();
		eraseDialog();
		switch (dialogLocal.getDecision()) {
		case CONTINUE:
			break;
		case RESTART:
			reset();
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
		eraseMinigame();		
	}

	@Override
	public void update(float delta) {
		if (!this.dialogStack.isEmpty()) {
			this.dialogStack.peek().update(delta);			
		} 		
		else {
			if (timeMeasured)
				this.stats.increasePhase1ElapsedTime(delta);
	
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
				throw new IllegalStateException(state.toString());
			}
		}				
	}

	private void updateInMistakeState(float delta) {
		if (Gdx.input.justTouched()) {
			reset();
		}
	}

	private void updateInFinishState(float delta) {
		this.level.switchToPhase2(checkPoints, pathway.getDistanceMap(), this, this.stats);
	}

	private void updateInDrivingState(float delta) {
		timeStatusBar.update(stats.getPhase1ElapsedTime());
		// stopped dragging
		if (!this.level.getCar().isDragged()) {
			switchToMistakeState(Constants.strings.PHASE_1_FINISH_NOT_REACHED);
			return;
		} else if (stats.getPhase1ElapsedTime() >= stats.getPhase1TimeLimit()) {
			switchToMistakeState(Constants.strings.PHASE_1_TIME_EXPIRED);
			return;
		}
		
		

		

		this.level.getCar().update(delta);
		// did not move from last update
		if (Gdx.input.getDeltaX() == 0 && Gdx.input.getDeltaY() == 0)
			return;

		Vector2 carPosition = this.level.getCar().getPosition();

		//collision checking
		for (GameTerminatingObject gameObject : this.terminators) {	
			//double boundRadius = this.level.getCar().getBoundingRadius();
			
			//float distTravelled = new Vector2(carPosition).sub(formerPosition).len();
			if (!checkPoints.isEmpty())
			{
				Vector2 formerPosition = checkPoints.getLast().getPosition();
				if (this.level.getCar().collidesWithinLineSegment(gameObject, formerPosition)) {
					this.level.getGame().assets.soundAndMusicManager.playCollisionSound(gameObject);
					switchToMistakeState("You crashed into a " + gameObject.getName()+ "!");
					return;
				}
			}
		}
		
		// coordinates ok
		DistanceMap map = pathway.getDistanceMap();
		if (carPosition.x >= 0 && carPosition.x < map.getWidth()
				&& carPosition.y >= 0 && carPosition.y < map.getHeight()) {

			//if(this.level.getCar().positionCollides(this.level.getFinish())){
			if(this.level.getCar().collides(this.level.getFinish())){
				
				if (wayPoints.isEmpty()){
					state = SubLevel1States.FINISH_STATE;
					this.level.getGame().assets.soundAndMusicManager.playSound(Constants.sounds.SOUND_SUB1_CHEER, Constants.sounds.SOUND_DEFAULT_VOLUME);
					dialogStack.push(new DecisionDialog(this.level, this, Constants.strings.PHASE_1_FINISH_REACHED, true));
					timeMeasured = false;
					this.level.getCar().setDragged(false);
				}
				else{
					switchToMistakeState(Constants.strings.PHASE_1_FINISH_REACHED_BUT_NOT_CHECKPOINTS);
				}
			}


			// not on track OR all checkpoint not yet reached (if reached, we
			// may have reached the finish line)
			if (map.At(carPosition) > Constants.misc.MAX_DISTANCE_FROM_PATHWAY) {
				switchToMistakeState(Constants.strings.PHASE_1_OUT_OF_LINE);

			} else {				
				checkPoints.add(new CheckPoint(stats.getPhase1ElapsedTime(), carPosition));

				if (!wayPoints.isEmpty()) {
					boolean canRemove = true;
					while (canRemove && !wayPoints.isEmpty()) {
						Vector2 way = new Vector2(wayPoints.peekFirst());
						Vector2 pos = new Vector2(this.level.getCar()
								.getPosition());
						if (way.sub(pos).len() <= Constants.misc.MAX_DISTANCE_FROM_PATHWAY*1.2)
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

			Vector2 shift = new Vector2(this.level.getCar().getPosition()).sub(touchPos.x, touchPos.y);
			if (shift.len() <= Constants.misc.CAR_CAPABLE_DISTANCE) {
				this.level.getCar().setDragged(true);
				this.level.getCar().setShift(shift);
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
		timeStatusBar.draw(batch);
		if (!dialogStack.isEmpty()) {
			dialogStack.peek().draw(batch);
		}
		
	}

	/**
	 * Sets this game phase to its beginning Called when player leaves the road
	 * or makes a discontinuous move
	 */
	public void reset() {
		takeFocus();
		playStartEngineSound();
		
		this.dialogStack.clear(); 
		eraseMinigame();
		takeFocus();	
		
		state = SubLevel1States.BEGINNING_STATE;
		
		this.stats.reset();		
		checkPoints.clear();
		initWayPoints(Constants.misc.START_POSITION_IN_CURVE,
				Constants.misc.FINISH_POSITION_IN_CURVE, Constants.misc.WAYPOINTS_COUNT);
		
		for (GameObject gameObject : this.level.getGameObjects()) {
			gameObject.reset();
		}
		
		// Car positioning
		this.level.getCar().reset();
		setCarToStart();
					
		if (Autickax.settings.showTooltips)
			this.dialogStack.push(new MessageDialog(this.level, this, 
					Constants.strings.TOOLTIP_PHASE_1_WHAT_TO_DO));
		
		timeStatusBar.update(0.f);
	}

	public void setCarToStart(){
		// Car positioning
		float EPSILON_F = 0.01f;
		Vector2 startPosition = this.level.getStart().getPosition();
		Vector2 startDirection = new Vector2(this.pathway.GetPosition(Constants.misc.START_POSITION_IN_CURVE + EPSILON_F)).sub(startPosition).nor().scl(2 * Constants.gameObjects.CAR_MINIMAL_DISTANCE_TO_SHOW_ROTATION);
		Vector2 preparePosition = new Vector2(startPosition).sub(startDirection);						
		this.level.getCar().move(new Vector2(preparePosition));		
		this.level.getCar().move(new Vector2(startPosition));
	}
	
	/**
	 * Player failed to finish the track
	 */
	private void switchToMistakeState(String str) {
		this.level.getGame().assets.soundAndMusicManager.playSound(Constants.sounds.SOUND_SUB1_FAIL, Constants.sounds.SOUND_DEFAULT_VOLUME);
		this.dialogStack.push(new DecisionDialog(this.level, this, str, false));
		this.state = SubLevel1States.MISTAKE_STATE;
		this.level.getCar().setDragged(false);
		timeMeasured = false;
		this.state.setMistake(str);
	}

	private void initWayPoints(float start, float finish, int nofWayPoints) {
		wayPoints = new LinkedList<Vector2>();
		float step = (finish - start) / nofWayPoints;
		Vector2 lastAdded = null;
		float sumRadii = this.level.getFinish().getBoundingRadius() + Constants.misc.MAX_DISTANCE_FROM_PATHWAY;
		for (float f = start; f < finish; f += step) {
			//do not add waypoints too close to finish
			Vector2 pathPosition = pathway.GetPosition(f);
			if (new Vector2(pathPosition).sub(this.level.getFinish().getPosition()).len() >   sumRadii )
			{
				lastAdded = pathPosition;
				wayPoints.add(lastAdded);
			}
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
		
		/*shapeRenderer.setColor(new Color(0.1f, 0.1f, 0.9f, 0.5f));
		//shapeRenderer.circle(this.level.getFinish().getX(), this.level.getFinish().getY(), Constants.FINISH_BOUNDING_RADIUS);
		
		shapeRenderer.setColor(new Color(0.5f, 0.5f, 0.5f, 0.5f));
		for(Vector2 waypoint: wayPoints)
		{
			shapeRenderer.circle(waypoint.x, waypoint.y, 20);
		}*/
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
