package cz.mff.cuni.autickax.gamelogic;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;

import cz.mff.cuni.autickax.constants.Constants;
import cz.mff.cuni.autickax.input.Input;

public class TyreTracks extends Actor {

	private ArrayList<TyreTrackInfo> tyrePositions;
	private Vector2 previousObjectCenter;

	private final ShapeRenderer shapeRenderer;

	

	public TyreTracks(Vector2 objectPosition) {
		previousObjectCenter = objectPosition;
		tyrePositions = new ArrayList<TyreTracks.TyreTrackInfo>();
		this.shapeRenderer = new ShapeRenderer();
	}
	

	public void addPoint(Vector2 objectCenter, float time) {
		float dist = previousObjectCenter.dst(objectCenter);
		Vector2 leftVec = getLeftTyreVector(objectCenter);
		Vector2 leftPos = new Vector2(previousObjectCenter).add(leftVec);


		Vector2 rightPos = new Vector2(previousObjectCenter)
				.add(getRightTyreVector(leftVec));
		tyrePositions.add(new TyreTrackInfo(leftPos,rightPos, dist, time));

		previousObjectCenter = objectCenter;
	}

	private Vector2 getLeftTyreVector(Vector2 objectCenter) {
		Vector2 dirVec = new Vector2(objectCenter).sub(previousObjectCenter);
		Vector2 rotated = new Vector2(dirVec).rotate(90);
		return rotated.nor().scl(Constants.tyreTracksConstants.WIDTH_HALF*Input.xStretchFactorInv);
	}

	private Vector2 getRightTyreVector(Vector2 leftVector) {
		return new Vector2(leftVector).scl(-1);
	}

	@Override
	public void draw(SpriteBatch batch, float parentAlpha) {
		batch.end();

		shapeRenderer.begin(ShapeType.Line);

		float totalDistFromCar = 0.0f;
		float elapsedInLastPoint = 0.0f;
		if (!tyrePositions.isEmpty())
			elapsedInLastPoint = tyrePositions.get(tyrePositions.size()-1).time;
		
		Gdx.gl10.glLineWidth(Constants.tyreTracksConstants.LINE_WIDTH*Input.xStretchFactorInv);
		Gdx.gl.glEnable(GL10.GL_BLEND);
		Gdx.gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		Gdx.gl.glEnable(GL10.GL_LINE_SMOOTH);
		// shapeRenderer.setProjectionMatrix(camera.combined);

		Vector2 stretch = new Vector2(Input.xStretchFactorInv,
				Input.yStretchFactorInv);
		for (int i = tyrePositions.size() - 1; i > 1; i--) {
			TyreTrackInfo trackInfo1 = tyrePositions.get(i);
			TyreTrackInfo trackInfo2 = tyrePositions.get(i - 1);
			
			float timeDiffMS = (elapsedInLastPoint - trackInfo2.time)*1000;
			boolean drawLine = totalDistFromCar > Constants.tyreTracksConstants.TYRE_DIST_FROM_CENTER &&
					timeDiffMS <= Constants.tyreTracksConstants.FADE_LIMIT;
			if (drawLine) {
				float alpha = 1 - (timeDiffMS) / Constants.tyreTracksConstants.FADE_LIMIT;
				shapeRenderer.setColor(new Color(0.1f, 0.1f, 0.12f, alpha));
				shapeRenderer.line(new Vector2(trackInfo1.leftPosition).scl(stretch),
						new Vector2(trackInfo2.leftPosition).scl(stretch));
				shapeRenderer.line(new Vector2(trackInfo1.rightPosition).scl(stretch),
						new Vector2(trackInfo2.rightPosition).scl(stretch));
			}

			totalDistFromCar += trackInfo1.distance;
		}

		shapeRenderer.end();
		Gdx.gl.glDisable(GL10.GL_BLEND);
		Gdx.gl.glDisable(GL10.GL_LINE_SMOOTH);

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
		public float distance;
		public float time;

		public TyreTrackInfo(Vector2 leftPos,Vector2 rightPos, float dist, float time) {
			this.leftPosition = leftPos;
			this.rightPosition = rightPos;
			this.distance = dist;
			this.time = time;
		}
	}

}
