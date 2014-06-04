package cz.cuni.mff.xcars.gamelogic;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;

import cz.cuni.mff.xcars.constants.Constants;
import cz.cuni.mff.xcars.entities.Car;
import cz.cuni.mff.xcars.input.Input;

public class TyreTracks extends Actor {

	private ArrayList<TyreTrackInfo> tyrePositions;
	private Vector2 previousObjectCenter;

	private final ShapeRenderer shapeRenderer;
	private Car car;
	private IElapsed elapsed;
	

	public TyreTracks(Vector2 objectPosition, Car car, IElapsed elapsed) {
		previousObjectCenter = objectPosition;
		tyrePositions = new ArrayList<TyreTracks.TyreTrackInfo>();
		this.shapeRenderer = new ShapeRenderer();
		this.car = car;
		this.elapsed = elapsed;
	}
	

	public void addPoint(Vector2 objectCenter, float time) {
		float dist = previousObjectCenter.dst(objectCenter);
		Vector2 leftVec = getLeftTyreVector(objectCenter);
		Vector2 leftPos = new Vector2(previousObjectCenter).add(leftVec);


		Vector2 rightPos = new Vector2(previousObjectCenter)
				.add(getRightTyreVector(leftVec));
		
		if (dist <= Constants.tyreTracksConstants.MAX_SEGMENT_LENGTH || tyrePositions.isEmpty())
			tyrePositions.add(new TyreTrackInfo(leftPos,rightPos,  time));
		else
		{
			TyreTrackInfo last = tyrePositions.get(tyrePositions.size()-1);
			ArrayList<TyreTrackInfo> tri = getInterpolatedPositions(last.leftPosition, leftPos, 
					last.rightPosition, rightPos, last.time, time,objectCenter);
			tyrePositions.addAll(tri);
		}
		previousObjectCenter = objectCenter;
	}
	
	//CALL IF AND ONLY IF THE SEGMENT IS TOO LONG
	private ArrayList<TyreTrackInfo> getInterpolatedPositions(Vector2 startLeft, Vector2 finishLeft, 
			Vector2 startRight, Vector2 finishRight,
			float timeStart, float timeFinish, Vector2 prevCenter)
	{
		ArrayList<TyreTrackInfo> interpolated = new ArrayList<TyreTrackInfo>();
		float timeAvailable = timeFinish - timeStart;
		Vector2 dirVector = new Vector2(finishLeft).sub(startLeft);
		float distance = dirVector.len();
		float velocity = distance / timeAvailable;
		
		float stepLength = Constants.tyreTracksConstants.MAX_SEGMENT_LENGTH;
		dirVector.nor().scl(stepLength);
		float distFromFinish = distance;
		Vector2 leftVec = new Vector2(startLeft);
		Vector2 rightVec = new Vector2(startRight);
		float time = timeStart;
		float timeStep = stepLength / velocity;
		
		while(distFromFinish > stepLength)
		{
			interpolated.add(new TyreTrackInfo(new Vector2(leftVec), new Vector2(rightVec),  time));
			distFromFinish -= stepLength;
			time+=timeStep;
			leftVec.add(dirVector);
			rightVec.add(dirVector);
		}
		if (distFromFinish != 0)
		{
			interpolated.add(new TyreTrackInfo(finishLeft, finishRight, timeFinish));
		}
		
		
		return interpolated;
	}

	private Vector2 getLeftTyreVector(Vector2 objectCenter) {
		Vector2 dirVec = new Vector2(objectCenter).sub(previousObjectCenter);
		Vector2 rotated = new Vector2(dirVec).rotate(90);
		return rotated.nor().scl(Constants.tyreTracksConstants.WIDTH_HALF);
	}

	private Vector2 getRightTyreVector(Vector2 leftVector) {
		return new Vector2(leftVector).scl(-1);
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		batch.end();

		shapeRenderer.begin(ShapeType.Line);

		float distSquaredFromCenter;
		float elapsedInLastPoint = elapsed.getElapsed();
		
		
		Gdx.gl20.glLineWidth(Constants.tyreTracksConstants.LINE_WIDTH*Input.xStretchFactorInv);
		Gdx.gl.glEnable(GL20.GL_BLEND);
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		//Gdx.gl.glEnable(GL20.GL_LINE_SMOOTH);
		// shapeRenderer.setProjectionMatrix(camera.combined);

		Vector2 stretch = new Vector2(Input.xStretchFactorInv,
				Input.yStretchFactorInv);
		for (int i = tyrePositions.size() - 1; i > 1; i--) {
			TyreTrackInfo trackInfo1 = tyrePositions.get(i);
			TyreTrackInfo trackInfo2 = tyrePositions.get(i - 1);
			
			float timeDiffMS = (elapsedInLastPoint - trackInfo2.time)*1000;
			distSquaredFromCenter =  new Vector2(car.getPosition()).dst2(trackInfo1.leftPosition);
			boolean drawLine = distSquaredFromCenter > Constants.tyreTracksConstants.MAX_DIST_SQUARED && timeDiffMS <= Constants.tyreTracksConstants.FADE_LIMIT;
			if (drawLine) {
				float alpha = 1 - (timeDiffMS) / Constants.tyreTracksConstants.FADE_LIMIT;
				Color tyreColor = Constants.tyreTracksConstants.TYRE_TRACK_COLOR; 
				shapeRenderer.setColor(new Color(tyreColor.r, tyreColor.g, tyreColor.b, alpha));
				shapeRenderer.line(new Vector2(trackInfo1.leftPosition).scl(stretch),
						new Vector2(trackInfo2.leftPosition).scl(stretch));
				shapeRenderer.line(new Vector2(trackInfo1.rightPosition).scl(stretch),
						new Vector2(trackInfo2.rightPosition).scl(stretch));
			}
		}
		shapeRenderer.end();
		Gdx.gl.glDisable(GL20.GL_BLEND);
		//Gdx.gl.glDisable(GL10.GL_LINE_SMOOTH);
		batch.begin();
	}
	
	public void clear()
	{
		tyrePositions.clear();
	}

	
	public void setPreviousObject(Vector2 prevObjectCenter)
	{
		this.previousObjectCenter = prevObjectCenter;
	}

	class TyreTrackInfo {
		public Vector2 leftPosition;
		public Vector2 rightPosition;
		public float time;

		public TyreTrackInfo(Vector2 leftPos,Vector2 rightPos, float time) {
			this.leftPosition = leftPos;
			this.rightPosition = rightPos;
			this.time = time;
		}
		
	}

}
