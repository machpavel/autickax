package cz.mff.cuni.autickax.pathway;

import java.util.ArrayList;

import com.badlogic.gdx.math.Vector2;

import cz.mff.cuni.autickax.constants.Constants;

/**
 * @author Shabby
 * Main class for representing a pathway.
 */
public class Pathway implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	
	private DistanceMap distanceMap;
	private ArrayList<Vector2> controlPoints;
	private PathwayType pathwayType;
	private Splines.TypeOfInterpolation typeOfInterpolation;
	
	public enum PathwayType implements java.io.Serializable {
		CLOSED, OPENED;
	}
		
	public Pathway() {
		 setControlPoints(new ArrayList<Vector2>());
	}
	public Pathway(PathwayType pathwayType, Splines.TypeOfInterpolation typeOfInterpolation) {
		this();
		this.typeOfInterpolation = typeOfInterpolation;
		this.pathwayType = pathwayType;		 
	}

	
	/**
	 *  Creates distances in distanceMap.
	 *  Method must be called if controlPoints are changed.
	 */
	public void CreateDistances() {
		setDistanceMap(new DistanceMap(Constants.WORLD_HEIGHT, Constants.WORLD_WIDTH));
		getDistanceMap().CreateDistances(getControlPoints(), pathwayType, typeOfInterpolation);		
	}
	
	
	/**
	 * @param u Position in pathway. From 0 to 1.
	 * @return Point in screen coordinates.
	 */
	public Vector2 GetPosition(float u){
		return Splines.GetPoint(getControlPoints(), u, typeOfInterpolation, pathwayType);
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
	
	public void deleteDistanceMap() {
		this.distanceMap.deleteMap();
	}


	public PathwayType getType() {
		return pathwayType;
	}

	public Splines.TypeOfInterpolation getTypeOfInterpolation() {
		return typeOfInterpolation;
	}

	public void setType(PathwayType type) {
		this.pathwayType = type;
	}
	public void setType(String type) throws Exception {
		if(type.equals("OPENED"))
			this.pathwayType = PathwayType.OPENED;
		else if(type.equals("CLOSED"))
			this.pathwayType = PathwayType.CLOSED;
		else throw new Exception("Unknown type of pathway type");
	}
	public void setTypeOfInterpolation(String typeOfInterpolation) throws Exception {
		if(typeOfInterpolation.equals("CUBIC_B_SPLINE"))
			this.typeOfInterpolation = Splines.TypeOfInterpolation.CUBIC_B_SPLINE;
		else if(typeOfInterpolation.equals("CUBIC_SPLINE"))
			this.typeOfInterpolation = Splines.TypeOfInterpolation.CUBIC_SPLINE;
		else 
			throw new Exception("Unknown type of interpolation");
	}
	
	
	
	
}
