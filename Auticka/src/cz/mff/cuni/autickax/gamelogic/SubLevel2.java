package cz.mff.cuni.autickax.gamelogic;

import java.util.LinkedList;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;

import cz.mff.cuni.autickax.input.Input;
import cz.mff.cuni.autickax.pathway.DistanceMap;
import cz.mff.cuni.autickax.scene.GameScreen;

public class SubLevel2 extends SubLevel {

	private LinkedList<CheckPoint> checkpoints;
	private double pathFollowingAccuracy = 0;
	private double pathFollowingTime = 0;
	private DistanceMap distMap;
	
	private CheckPoint from;
	private CheckPoint to;
	
	private Vector2 velocity;
	private float velocityMagnitude;

	
	
	private float timeElapsed = 0;
	
	private float penalConst = 3f;
	private float maxDistance = 20;


	public SubLevel2(GameScreen gameScreen, LinkedList<CheckPoint> checkpoints, DistanceMap map, double pathFollowingTime) {
		super(gameScreen);

		this.checkpoints = checkpoints;
		this.pathFollowingAccuracy = pathFollowingAccuracy;
		this.pathFollowingTime = pathFollowingTime;
		this.distMap = map;
		


		this.from = checkpoints.removeFirst();
		this.to = checkpoints.removeFirst();
		float startX = this.from.x;
		float startY = this.from.y;
		this.Level.getCar().move(startX, startY);
		computeVelocity();
	}

	@Override
	public void update(float time) {
		timeElapsed += time;
		if (!checkpoints.isEmpty())
		{
			Vector2 newPos = getNewCarPosition(time);
			this.Level.getCar().move(newPos.x, newPos.y);

		}
		
	}

	@Override
	public void draw(SpriteBatch sprite) {
		this.Level.getCar().draw(sprite);
		
	}

	@Override
	public void render() {
		shapeRenderer.begin(ShapeType.Filled);
		shapeRenderer.setColor(Color.RED);
		for(CheckPoint ce: checkpoints)
		{
			shapeRenderer.circle((float)ce.x / Input.xStretchFactor, (float)ce.y / Input.yStretchFactor,2);
		}

		shapeRenderer.end();
		
	}
	
	private Vector2 getNewCarPosition(float time)
	{
		//compute new position vector and check if it lies on the old vector
		Vector2 newPos = computeNewPosition(time,this.maxDistance, this.penalConst);
		
		//invalid direction (new checkpoint needed)
		if ( new Vector2(this.to.x,this.to.y).sub(newPos).dot(this.velocity) < 0)
		{
			this.from = this.to;
			//assign new target position
			this.to = checkpoints.removeFirst();
			computeVelocity();
			newPos = computeNewPosition(time,this.maxDistance, this.penalConst);
		}
		
		return newPos;
	}
	
	/**
	 * Computes exact velocity from sublevel1
	 */
	private void computeVelocity()
	{
		float time = this.to.time - this.from.time;
		velocity = new Vector2(this.to.x, this.to.y).sub(this.from.x, this.from.y).div(time);
		velocityMagnitude = velocity.len();
	}
	
	private Vector2 computeNewPosition(float time, float maxDistance, float penalConst)
	{
		float oldX = this.Level.getCar().getX();
		float oldY = this.Level.getCar().getY();
		float penalizationFactor = 1f;
		
		float distanceFromCurveCenter = distMap.At((int)oldX, (int)oldY);
		if(distanceFromCurveCenter > maxDistance)
		{
			penalizationFactor*= penalConst / Math.log(distanceFromCurveCenter+2);
		}
		
		Vector2 newPos = new Vector2(oldX, oldY);
		float dist = this.velocityMagnitude*time*penalizationFactor;
		Vector2 traslationVec = new Vector2(this.velocity).nor().scl(dist);
		newPos.add(traslationVec);
		return newPos;
	}
	

}
