package cz.cuni.mff.xcars.gamelogic;

import java.util.LinkedList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;

import cz.cuni.mff.xcars.constants.Constants;
import cz.cuni.mff.xcars.debug.Debug;
import cz.cuni.mff.xcars.entities.Finish;
import cz.cuni.mff.xcars.entities.Start;
import cz.cuni.mff.xcars.input.Input;
import cz.cuni.mff.xcars.pathway.Pathway;

/**
 * This class represents positions on the track that need to be crossed in order
 * to successfully finish the race.
 */
public class WayPoints extends Actor {
	/** Coordinates to check whether whole track was raced through */
	private LinkedList<Vector2> wayPoints;
	private float sumRadii;
	private Finish finish;
	private Start start;
	private Pathway pathway;
	private ShapeRenderer renderer;

	public WayPoints(Finish finish, Start start, Pathway pathway) {
		this.renderer = new ShapeRenderer();
		this.wayPoints = new LinkedList<Vector2>();
		this.finish = finish;
		this.start = start;
		this.sumRadii = finish.getBoundingRadius()
				+ Constants.misc.MAX_DISTANCE_FROM_PATHWAY;
		this.pathway = pathway;
	}

	public void initWayPoints(float start, float finish, int nofWayPoints) {
		this.wayPoints.clear();
		float step = (finish - start) / nofWayPoints;
		Vector2 lastAdded = null;
		for (float f = start; f < finish; f += step) {
			// do not add waypoints too close to finish
			Vector2 pathPosition = this.pathway.GetPosition(f);
			float sumRadii2 = sumRadii * sumRadii;
			if (distToFinish2(pathPosition) > sumRadii2
					&& distToStart2(pathPosition) > sumRadii2) {
				lastAdded = pathPosition;
				this.wayPoints.add(lastAdded);
			}
		}
	}

	private float distToFinish2(Vector2 pos) {
		return new Vector2(pos).sub(this.finish.getPosition()).len2();
	}

	private float distToStart2(Vector2 pos) {
		return new Vector2(pos).sub(this.start.getPosition()).len2();
	}

	public Vector2 peekFirst() {
		return this.wayPoints.peekFirst();
	}

	public Vector2 removeFirst() {
		return this.wayPoints.removeFirst();
	}

	public boolean isEmpty() {
		return this.wayPoints.isEmpty();
	}

	public void draw(Batch batch, float parentAlpha) {
		if (Debug.DEBUG && Debug.drawWayPoints) {
			batch.end();
			// TODO why is here blend function when we dont use any alpha??? And
			// you don't disable it again. For faster performance.
			Gdx.gl.glEnable(GL20.GL_BLEND);
			Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

			this.renderer.begin(ShapeType.Line);
			this.renderer.setColor(Color.BLACK);
			for (Vector2 way : this.wayPoints) {
				this.renderer.circle(way.x * Input.xStretchFactorInv, way.y
						* Input.yStretchFactorInv,
						Constants.misc.MAX_DISTANCE_FROM_PATHWAY
								* Input.xStretchFactorInv);
			}
			renderer.end();
			batch.begin();
		}
	}
}
