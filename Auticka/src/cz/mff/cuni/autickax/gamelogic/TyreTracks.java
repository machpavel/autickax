package cz.mff.cuni.autickax.gamelogic;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;

import cz.mff.cuni.autickax.input.Input;

public class TyreTracks {

	private ArrayList<PositionAndDistance> leftTyrePositions;
	private ArrayList<PositionAndDistance> rightTyrePositions;
	private Vector2 previousObjectCenter;
	
	private final int WIDTH_HALF = 10;
	private final int TYRE_DIST_FROM_CENTER = 20;
	private final int FADE_LIMIT = 300;
	
	public TyreTracks(Vector2 objectPosition)
	{
		previousObjectCenter = objectPosition;
		leftTyrePositions = new ArrayList<TyreTracks.PositionAndDistance>();
		rightTyrePositions = new ArrayList<TyreTracks.PositionAndDistance>();
	}
	
	public void addPoint(Vector2 objectCenter)
	{
		float dist = previousObjectCenter.dst(objectCenter);
		Vector2 leftVec = getLeftTyreVector(objectCenter);
		Vector2 leftPos = new Vector2(previousObjectCenter).add(leftVec);
		
		leftTyrePositions.add(new PositionAndDistance(leftPos, dist));
		
		Vector2 rightPos = new Vector2(previousObjectCenter).add(getRightTyreVector(leftVec));
		rightTyrePositions.add(new PositionAndDistance(rightPos, dist));
		
		previousObjectCenter = objectCenter;
	}
	
	private Vector2 getLeftTyreVector(Vector2 objectCenter) 
	{
		Vector2 dirVec = new Vector2(objectCenter).sub(previousObjectCenter);
		Vector2 rotated = new Vector2(dirVec).rotate(90);
		return rotated.nor().scl(WIDTH_HALF);
	}
	
	private Vector2 getRightTyreVector(Vector2 leftVector) 
	{
		return new Vector2(leftVector).scl(-1);
	}
	
	public void render(ShapeRenderer shapeRenderer)
	{
		shapeRenderer.begin(ShapeType.Line);
	

		
		float totalDistFromCar = 0.0f;
		Gdx.gl10.glLineWidth(5);
		 Gdx.gl.glEnable(GL10.GL_BLEND);
		    Gdx.gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		    Gdx.gl.glEnable(GL10.GL_LINE_SMOOTH);
		 //  shapeRenderer.setProjectionMatrix(camera.combined);
	      
	      
		Vector2 stretch = new Vector2(Input.xStretchFactorInv, Input.yStretchFactorInv);
		for(int i = leftTyrePositions.size()-1; i > 1; i--)
		{
			PositionAndDistance left1 = leftTyrePositions.get(i);
			PositionAndDistance left2 = leftTyrePositions.get(i-1);
			
			PositionAndDistance right1 = rightTyrePositions.get(i);
			PositionAndDistance right2 = rightTyrePositions.get(i-1);
			
			boolean drawLine =  (totalDistFromCar > TYRE_DIST_FROM_CENTER && totalDistFromCar < FADE_LIMIT);
			if(drawLine)
			{
				float alpha = 1 - totalDistFromCar / FADE_LIMIT;
				shapeRenderer.setColor(new Color(0.1f, 0.1f, 0.12f, alpha));
				shapeRenderer.line(new Vector2(left1.position).scl(stretch), new Vector2(left2.position).scl(stretch));
				shapeRenderer.line(new Vector2(right1.position).scl(stretch), new Vector2(right2.position).scl(stretch));
			}
				
			
			totalDistFromCar += left1.position.dst(left2.position);
			
		}

		shapeRenderer.end();
		Gdx.gl.glDisable(GL10.GL_BLEND);
		Gdx.gl.glDisable(GL10.GL_LINE_SMOOTH);
	}
	
	class PositionAndDistance
	{
		public Vector2 position;
		public float distance;
		
		public PositionAndDistance(Vector2 pos, float dist)
		{
			this.position = pos;
			this.distance = dist;
		}
	}
	
}
