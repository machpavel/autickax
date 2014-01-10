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
	/**Stored checkpoints */
	private File checkpointFile;

	private LinkedList<Vector2> points = new LinkedList<Vector2>();

	public SubLevel2(GameScreen gameScreen, LinkedList<CheckPoint> checkpoints, DistanceMap map) {
		super(gameScreen);

		this.checkpoints = checkpoints;
		//backup checkpoints
		storeCheckpointOnDisk();
		/*LinkedList<CheckPoint> loadedCheckpoints; 
		loadedCheckpoints = loadCheckPoints(new File("..\\StoredCheckpoints\\check201401091034Svine.chck"));
		
		if (loadedCheckpoints != null)
			this.checkpoints = loadedCheckpoints;*/

		this.distMap = map;

		this.from = this.checkpoints.removeFirst();
		this.to = this.checkpoints.removeFirst();
		this.Level.getCar().move(this.from.x, this.from.y);
		computeVelocity();
	}

	@Override
	public void update(float time) {
		timeElapsed += time;
		if (!checkpoints.isEmpty())
		{
			Vector2 newPos = moveCarToNewPosition(time);
			points.add(new Vector2(newPos));
			
			
		}
		else//successful -> erase checkpoints from disk
			{
				
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
		Font font = new Font(this.Level.getFont());
		font.draw(batch, "time: " + String.format("%1$,.1f", timeElapsed),
				10, (int) Gdx.graphics.getHeight() - 32);
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
	
	private Vector2 moveCarToNewPosition(float time)
	{
		float timeAvailable = time;
		Vector2 newPos = null;
		
		while (timeAvailable > 0 && !this.checkpoints.isEmpty())
		{
			//TODO assert car is always between TO AND FROM && timeAvailable >=0
			Vector2 carPos = this.Level.getCar().getPosition();
			//compute time necessary to reach checkpoint To
			float distToTo = new Vector2(carPos).sub(to.getX(), to.getY()).len();
			float timeNecessaire = distToTo / (this.velocityMagnitude *  this.penalizationFactor);
			
			//move only as much as you can
			if (timeNecessaire > timeAvailable)
			{
				float dist = this.velocityMagnitude * timeAvailable * penalizationFactor;
				Vector2 traslationVec = new Vector2(this.velocity).nor().scl(dist);
				newPos = new  Vector2(carPos); 
				newPos.add(traslationVec);
				timeAvailable = 0;
			}
			//move to checkpoint to and subtract the time, recalculate velocity, and checkpoints
			else
			{
				this.Level.getCar().move(to.x, to.y);
				newPos = new Vector2(to.x,to.y);
				this.from = this.to;
				this.to = this.checkpoints.removeFirst();
				computeVelocity();
				timeAvailable -= timeNecessaire;
			}
		}
		this.Level.getCar().move(newPos);
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
		
		float distanceFromCurveCenter = distMap.At(this.Level.getCar().getPosition());
		if(distanceFromCurveCenter > Constants.MAX_SURFACE_DISTANCE_FROM_PATHWAY)
		{
			penalizationFactor = Constants.OUT_OF_SURFACE_PENALIZATION_FACTOR / (float)Math.log(distanceFromCurveCenter+2);
		}
		else
			penalizationFactor = 1;
		
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
