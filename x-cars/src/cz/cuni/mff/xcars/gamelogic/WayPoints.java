package cz.cuni.mff.xcars.gamelogic;

import java.util.ArrayList;
import java.util.LinkedList;

import com.badlogic.gdx.graphics.Color;
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
import cz.cuni.mff.xcars.pathway.Pathway.PathwayType;
import cz.cuni.mff.xcars.pathway.Splines;

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
	private final float MAX_WIDTH;
	private final float MAX_HEIGHT;

	public WayPoints(Finish finish, Start start, Pathway pathway, float width,
			float height) {
		this.renderer = new ShapeRenderer();
		this.wayPoints = new LinkedList<Vector2>();
		this.finish = finish;
		this.start = start;
		this.sumRadii = finish.getBoundingRadius()
				+ Constants.misc.MAX_DISTANCE_FROM_PATHWAY;
		this.pathway = pathway;
		this.MAX_WIDTH = width;
		this.MAX_HEIGHT = height;
	}

	public void initWayPoints(float start, float finish, float step,
			float distThreshold) {
		this.wayPoints.clear();
		Vector2 lastAdded = null;
		Vector2 current = this.start.getPosition();
		float cumulativeDistance = 0f;
		for (float f = start; f < finish; f += step) {
			Vector2 pathPosition = this.pathway.GetPosition(f);
			float distToCurrent = current.dst(pathPosition);
			cumulativeDistance += distToCurrent;
			current = pathPosition;
			if (pathPosition.dst(this.finish.getPosition()) > sumRadii
					&& pathPosition.dst(this.start.getPosition()) > sumRadii) {
				if (cumulativeDistance > distThreshold) {
					lastAdded = pathPosition;
					this.wayPoints.add(lastAdded);
					cumulativeDistance = 0;
				}
			}
		}
	}

	public void initWayPoints2(float distThreshold) {
		this.wayPoints.clear();
		ArrayList<Vector2> points = getPoints(this.pathway.getType(),
				this.pathway.getControlPoints(),
				this.pathway.getTypeOfInterpolation());
		Vector2 lastAdded = null;
		Vector2 current = this.start.getPosition();
		float cumulativeDistance = 0f;

		for (Vector2 point : points) {

			float distToCurrent = current.dst(point);
			cumulativeDistance += distToCurrent;
			current = point;
			if (point.dst(this.finish.getPosition()) > sumRadii
					&& point.dst(this.start.getPosition()) > sumRadii) {
				if (cumulativeDistance > distThreshold) {
					lastAdded = point;
					this.wayPoints.add(lastAdded);
					cumulativeDistance = 0;
				}
			}
		}

	}

	private ArrayList<Vector2> getPoints(PathwayType pathwayType,
			ArrayList<Vector2> controlPoints,
			Splines.TypeOfInterpolation typeOfInterpolation) {
		// The calculation is related to how far control points are from
		// each other.
		ArrayList<Vector2> pointsOnCurve = new ArrayList<Vector2>();
		float localUCount = 0;
		localUCount += controlPoints.get(0).dst(controlPoints.get(1));
		localUCount += controlPoints.get(0).dst(controlPoints.get(1));
		localUCount += controlPoints.get(1).dst(controlPoints.get(2));

		for (int i = 0; i < controlPoints.size() - 3; i++) {
			localUCount -= controlPoints.get(i).dst(controlPoints.get(i + 1));
			localUCount += controlPoints.get(i + 2).dst(
					controlPoints.get(i + 3));
			for (float j = 0; j < localUCount; j++) {
				float localU = j / localUCount;
				Vector2 point = Splines.GetPoint(controlPoints, i, localU,
						typeOfInterpolation);
				if (point.x >= 0 && point.y > 0 && point.x < this.MAX_WIDTH
						&& point.y < this.MAX_HEIGHT)
					pointsOnCurve.add(point);
			}
		}
		return pointsOnCurve;
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

	@SuppressWarnings("unused")
	public void draw(Batch batch, float parentAlpha) {
		if (Debug.DEBUG && Debug.drawWayPoints) {
			batch.end();

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
