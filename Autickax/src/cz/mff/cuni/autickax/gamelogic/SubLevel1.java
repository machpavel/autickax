package cz.mff.cuni.autickax.gamelogic;

import java.util.LinkedList;





import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import cz.mff.cuni.autickax.pathway.DistanceMap;
import cz.mff.cuni.autickax.pathway.Pathway;
import cz.mff.cuni.autickax.scene.GameScreen;

public class SubLevel1 extends SubLevel {

	// Score
	private float score = 1;
	// Time
	private float timeElapsed = 0;
	
	//TODO should be associated with given track
	/**
	 * If farther from the road, this phases ends
	 */
	private float maxDistance;

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
	 * Array of distances from the curve that represents the track
	 */
	private DistanceMap map;
	
	/**
	 * state of the phase: beginning, driving, finish, mistake - must repeat
	 */
	private int state;
	
	/**
	 * Player just starts this phase or made a mistake and was moved again 
	 */
	private static final int BEGINNING_STATE = 0;
	/**
	 * Driving in progress
	 */
	private static final int DRIVING_STATE = 1;
	/**
	 * Player successfully finished the race
	 */
	private static final int FINISH_STATE = 2;
	private static final int MISTAKE_STATE = 3;
	
	
	/**
	 * Number of waypoints that check if the player raced through all the track
	 */
	private int nofWayPoints;
	/**
	 * between 0-1 determines the part of path where the race ends
	 */
	private float finish;
	/**
	 * between 0-1 determines the part of path where the race starts
	 */
	private float start;
	
	/**
	 * Coordinates to check whether whole track was raced through
	 */
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
	
	public SubLevel1(GameScreen gameScreen) {
		super(gameScreen);
		gameScr = gameScreen;
		pathway = gameScreen.getPathWay();
		maxDistance = 40;
		map = this.Level.getPathWay().getDistanceMap();
		start = 0.3f;
		finish = 0.8f;
		startPoint = pathway.GetPosition(start);
		finishPoint = pathway.GetPosition(finish);
		nofWayPoints = 20;

		initWayPoints(finish, start, nofWayPoints);

		
		checkPoints = new LinkedList<CheckPoint>();
		this.Level.getCar().move(startPoint.x, startPoint.y);
	}

	@Override
	public void update(float delta) {
		if (timeMeasured)
			timeElapsed += delta;
		
		//allow dragging in the beginning state
		if (state == BEGINNING_STATE)
		{
			if (Gdx.input.justTouched()) 
			{
				Vector3 touchPos = new Vector3();
				touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
				this.Level.unproject(touchPos);
				
				if (startPoint.dst(touchPos.x, touchPos.y) <= maxDistance)
				{
					
					this.Level.getCar().setDragged(true);
					state = DRIVING_STATE;
					timeMeasured = true;
				}
			}
		}
		else if (state == DRIVING_STATE)
		{
			//stopped dragging
			if (!this.Level.getCar().isDragged())
			{
				reset();
				return;
			}
			
			this.Level.getCar().update(delta);
			int x = (int)this.Level.getCar().getX();
			int y = (int)this.Level.getCar().getY();
			//coordinates ok
			if (x >= 0 && x < map.getWidth() && y >= 0 && y < map.getHeight())
			{
				if (map.At(x, y) > maxDistance)
				{
				    reset();
				   
				}
				else
				{
					checkPoints.add(new CheckPoint(timeElapsed, x, y));
					score += 1/(map.At(x, y)+0.3);
					if (checkWayPoints(x, y))
					{
						state = FINISH_STATE;
						timeMeasured = false;
					}
				
			}
}
		}
		//Finish state
		else
		{
			//stop time etc
			if(Gdx.input.justTouched()) 
			{
				CheckPoint [] arrayCheckPoint = new CheckPoint[checkPoints.size()];
				for(int i = 0; i < checkPoints.size(); i++)
				{
					arrayCheckPoint[i] = checkPoints.get(i);
				}
				gameScr.switchToPhase2(arrayCheckPoint, score, timeElapsed);
			}
		}
		
		

	}

	@Override
	public void draw(SpriteBatch batch) {
		BitmapFont font = this.Level.getFont();
		float stageHeight = this.Level.getStageHeight();
		float stageWidth = this.Level.getStageWidth();
		
		this.Level.getCar().draw(batch);
		
		//render the track
		shapeRenderer.begin(ShapeType.Point);
		shapeRenderer.setColor(new Color(Color.BLACK));
		for (int x = 0; x < (int) stageWidth; x++) {
			for (int y = 0; y < (int) stageHeight; y++) {
				if (map.At(x, y) < maxDistance)
					shapeRenderer.point(x, y, 0);

			}
		}
		shapeRenderer.end();
		
		shapeRenderer.begin(ShapeType.Filled);
		shapeRenderer.setColor(Color.BLUE);
		shapeRenderer.circle(startPoint.x, startPoint.y, maxDistance);
		shapeRenderer.setColor(Color.YELLOW);
		shapeRenderer.circle(finishPoint.x, finishPoint.y, maxDistance);
		
		shapeRenderer.setColor(new Color(Color.DARK_GRAY));
		for (Vector2 vec: wayPoints)
		{
			shapeRenderer.circle(vec.x, vec.y, 10);
		}
		
		shapeRenderer.setColor(Color.RED);
		for(CheckPoint ce: checkPoints)
		{
			shapeRenderer.circle((float)ce.x, (float)ce.y,2);
		}
		
		shapeRenderer.end();
		// Draw score
		font.draw(batch, "score: " + score, 10, (int)stageHeight-32);
		// Draw time
		font.draw(batch, "time: "+ String.format("%1$,.1f", timeElapsed), (int) stageWidth/2, (int)stageHeight-32);
		
		//status = "Phase: " + getStateString() + " dragged: " + this.Level.getCar().isDragged();
		status = updateStatus();
		font.draw(batch, status, 10, 64);
	}
	/**
	 * Sets this game phase to its beginning
	 * Called when player leaves the road or makes a discontinuous move
	 */
	public void reset()
	{
		if (state != FINISH_STATE)
		{
			state = BEGINNING_STATE;
			this.timeElapsed = 0;
			this.Level.getCar().move(startPoint.x, startPoint.y);
			this.Level.getCar().setDragged(false);
			this.score = 1;
			checkPoints.clear();
			
			timeMeasured = false;
			initWayPoints(finish, start, nofWayPoints);
			
		}
	}
	
	/**
	 * 
	 * @return string representation of the current state of this sublevel
	 */
	private String getStateString()
	{
		switch(state)
		{
			case BEGINNING_STATE: return "Beginning";
			case DRIVING_STATE: return "Driving";
			case FINISH_STATE: return "Finished";
			case MISTAKE_STATE: return "Mistake";
			default: return "";
		}
	}
	
	private String updateStatus()
	{
		switch(state)
		{
			case BEGINNING_STATE: status = "Draw the path for your vehicle";
			break;
			case DRIVING_STATE: status =  "Driving";
			break;
			case FINISH_STATE: status = "Finished - well done!";
			break;
			case MISTAKE_STATE: status = "Mistake";
			break;
			
		}
		
		return status;
	}
	
	/**
	 * 
	 * @param x
	 * @param y
	 * @return true if all checkpoints were reached
	 */
	private boolean checkWayPoints(int x, int y)
	{
		if (!wayPoints.isEmpty())
		{
			Vector2 first = wayPoints.peek();
			if (first.dst(x,y) <= maxDistance)
				wayPoints.removeFirst();
		}
		
		if (wayPoints.isEmpty())
		{
			//distance from finish line
			if (finishPoint.dst(x, y) <= maxDistance)
				return true;
		}
		
		return false;
	}
	
	private void initWayPoints(float finish, float start, int nofWayPoints)
	{
		wayPoints = new LinkedList<Vector2>();
		float step = (finish-start)/nofWayPoints; 
		Vector2 lastAdded = null;
		for(float f = start; f < finish; f += step)
		{
			lastAdded = pathway.GetPosition(f);
			wayPoints.add(lastAdded);
		}
		if (lastAdded != finishPoint)
			wayPoints.add(finishPoint);
	}
	

	

}
