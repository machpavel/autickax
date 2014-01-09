package cz.mff.cuni.autickax.gamelogic;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;

import cz.mff.cuni.autickax.Constants;
import cz.mff.cuni.autickax.entities.GameObject;
import cz.mff.cuni.autickax.input.Input;
import cz.mff.cuni.autickax.pathway.DistanceMap;
import cz.mff.cuni.autickax.scene.GameScreen;

public class SubLevel2 extends SubLevel {

	private LinkedList<CheckPoint> checkpoints;
	//for debugging
	private LinkedList<CheckPoint> clonedCheckpoints;
	private DistanceMap distMap;
	
	private CheckPoint from;
	private CheckPoint to;
	
	private Vector2 velocity;
	private float velocityMagnitude;

	
	
	private float timeElapsed = 0;
	/**Stored checkpoints */
	private File checkpointFile;

	private LinkedList<Vector2> points = new LinkedList<Vector2>();

	public SubLevel2(GameScreen gameScreen, LinkedList<CheckPoint> checkpoints, DistanceMap map) {
		super(gameScreen);

		this.checkpoints = checkpoints;
		//backup checkpoints
		storeCheckpointOnDisk();
		LinkedList<CheckPoint> loadedCheckpoints; 
		//loadedCheckpoints = loadCheckPoints(new File("..\\StoredCheckpoints\\moc okolo"));
		
		/*if (loadedCheckpoints != null)
			this.checkpoints = loadedCheckpoints;*/


		this.clonedCheckpoints = (LinkedList<CheckPoint>)checkpoints.clone();
		this.distMap = map;
		


		this.from = checkpoints.removeFirst();
		this.to = checkpoints.removeFirst();
		this.Level.getCar().move(this.from.x, this.from.y);
		computeVelocity();
	}

	@Override
	public void update(float time) {
		timeElapsed += time;
		if (!checkpoints.isEmpty())
		{
			Vector2 newPos = getNewCarPosition(time);
			this.Level.getCar().move(newPos.x, newPos.y);
			points.add(new Vector2(newPos));
			//successful -> erase checkpoints from disk
			eraseCheckpointOndDisk();
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
	}

	@Override
	public void render() {
		shapeRenderer.begin(ShapeType.Filled);
		shapeRenderer.setColor(Color.RED);
		for(CheckPoint ce: checkpoints)
		{
			shapeRenderer.circle((float)ce.x / Input.xStretchFactor, (float)ce.y / Input.yStretchFactor,2);
		}
		
		shapeRenderer.setColor(Color.BLUE);
		for(Vector2 vec: points)
		{
			shapeRenderer.circle(vec.x / Input.xStretchFactor,vec.y / Input.yStretchFactor,2);
		}
		
		

		shapeRenderer.end();
		
	}
	
	private Vector2 getNewCarPosition(float time)
	{
		//compute new position vector and check if it lies on the old vector
		Vector2 newPos = computeNewPosition(time, Constants.MAX_SURFACE_DISTANCE_FROM_PATHWAY, Constants.OUT_OF_SURFACE_PENALIZATION_FACTOR);
		
		//invalid direction (new checkpoint needed)
		if ( new Vector2(this.to.x,this.to.y).sub(newPos).dot(this.velocity) < 0)
		{
			this.from = this.to;
			//assign new target position
			
			this.to = checkpoints.removeFirst();
			computeVelocity();
			newPos = computeNewPosition(time,Constants.MAX_SURFACE_DISTANCE_FROM_PATHWAY, Constants.OUT_OF_SURFACE_PENALIZATION_FACTOR);
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
		float dist = this.velocityMagnitude * time * penalizationFactor;
		Vector2 traslationVec = new Vector2(this.velocity).nor().scl(dist);
		newPos.add(traslationVec);
		
		if (newPos.x < 0 || newPos.y < 0)
		{
			System.out.println("FUCK");
		
		}
		
		return newPos;
	}
	
	private void storeCheckpointOnDisk()
	{
	      try
	      {
	    	 String dt = new SimpleDateFormat("yyyyMMddhhmm").format(new Date()).toString();
	    	 String name = "check"  + dt +  ".chck";
	    	 checkpointFile = new File("..\\StoredCheckpoints\\" + name);  
	         FileOutputStream fileOut = new FileOutputStream(checkpointFile);
	         ObjectOutputStream out = new ObjectOutputStream(fileOut);
	         out.writeObject(checkpoints);
	         out.close();
	         fileOut.close();
	         System.out.println("Checkpoints stored");
	         
	      }catch(IOException i)
	      {
	          i.printStackTrace();
	      }
	}
	
	private void eraseCheckpointOndDisk()
	{
		if(checkpointFile.delete())
			System.out.println("Checkpoints erased");
	}

	private LinkedList<CheckPoint> loadCheckPoints(File f)
	{
		LinkedList<CheckPoint> chck = null;
	      try
	      {
	         FileInputStream fileIn = new FileInputStream(f);
	         ObjectInputStream in = new ObjectInputStream(fileIn);
	         chck = (LinkedList<CheckPoint>) in.readObject();
	         in.close();
	         fileIn.close();
	         System.out.println("Checkpoints loaded");
	      }catch(IOException i)
	      {
	         i.printStackTrace();
	      }catch(ClassNotFoundException c)
	      {
	         System.out.println("Checkpoint Class not found");
	         c.printStackTrace();
	      }
	      return chck;
	}

}
