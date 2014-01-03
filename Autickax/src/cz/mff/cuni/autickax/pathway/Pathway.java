package cz.mff.cuni.autickax.pathway;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;

/**
 * @author Shabby
 * Main class for representing a pathway.
 */
public class Pathway {	
	private DistanceMap distanceMap;
	private ArrayList<Vector2> controlPoints;
	
	public Pathway() {
		 setDistanceMap(new DistanceMap((int)Gdx.graphics.getHeight(), (int)Gdx.graphics.getWidth()));
		 setControlPoints(new ArrayList<Vector2>());
	}

	
	/**
	 *  Creates distances in distanceMap.
	 *  Method must be called if controlPoints are changed.
	 */
	public void CreateDistances() {
		getDistanceMap().CreateDistances(getControlPoints());		
	}
	
	
	/**
	 * @param u Position in pathway. From 0 to 1.
	 * @return Point in screen coordinates.
	 */
	public Vector2 GetPosition(float u){
		return Splines.GetPoint(getControlPoints(), u, DistanceMap.getTypeOfInterpolation());
	}

	public ArrayList<Vector2> getControlPoints() {
		return controlPoints;
	}

	public void setControlPoints(ArrayList<Vector2> controlPoints) {
		this.controlPoints = controlPoints;
	}

	public DistanceMap getDistanceMap() {
		return distanceMap;
	}

	public void setDistanceMap(DistanceMap distanceMap) {
		this.distanceMap = distanceMap;
	}
	
	
	
	
}
