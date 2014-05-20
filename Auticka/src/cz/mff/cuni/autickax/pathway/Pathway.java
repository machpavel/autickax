package cz.mff.cuni.autickax.pathway;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.ArrayList;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.XmlReader.Element;

import cz.mff.cuni.autickax.constants.Constants;
import cz.mff.cuni.autickax.pathway.Splines.TypeOfInterpolation;

/**
 * @author Shabby Main class for representing a pathway.
 */
public class Pathway implements java.io.Externalizable {

	private DistanceMap distanceMap;
	private ArrayList<Vector2> controlPoints;
	private PathwayType pathwayType;
	private Splines.TypeOfInterpolation typeOfInterpolation;
	private int textureType;
	private float distanceMapNodesCount = 1;

	public enum PathwayType {
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

	public int getTextureType() {
		return this.textureType;
	}

	public static Pathway parsePathway(Element root) throws Exception {
		Pathway retval = new Pathway();
		Element pathwayElement = root.getChildByName("pathway");
		retval.setType(pathwayElement.getAttribute("pathwayType"));
		retval.setTypeOfInterpolation(pathwayElement.getAttribute("typeOfInterpolation"));
		retval.textureType = pathwayElement.getInt("textureType");
		Element controlPoints = pathwayElement.getChildByName("controlPoints");
		int controlPointsCount = controlPoints.getChildCount();

		for (int i = 0; i < controlPointsCount; ++i) {
			Element controlPoint = controlPoints.getChild(i);
			Vector2 controlPointPosition = new Vector2(controlPoint.getFloat("X"),
					controlPoint.getFloat("Y"));
			retval.getControlPoints().add(controlPointPosition);
		}
		Element distanceMap = pathwayElement.getChildByName("distanceMap");
		if (distanceMap != null)
			retval.distanceMapNodesCount = distanceMap.getFloat("nodesCount");
		else
			retval.distanceMapNodesCount = 1;

		return retval;
	}

	/**
	 * Creates distances in distanceMap. Method must be called if controlPoints
	 * are changed.
	 */
	public void CreateDistances() {
		this.distanceMap = new DistanceMap(Constants.WORLD_HEIGHT, Constants.WORLD_WIDTH,
				distanceMapNodesCount);
		this.distanceMap.CreateDistances(getControlPoints(), pathwayType, typeOfInterpolation);
	}

	/**
	 * @param u
	 *            Position in pathway. From 0 to 1.
	 * @return Point in screen coordinates.
	 */
	public Vector2 GetPosition(float u) {
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
		if (type.equals("OPENED"))
			this.pathwayType = PathwayType.OPENED;
		else if (type.equals("CLOSED"))
			this.pathwayType = PathwayType.CLOSED;
		else
			throw new Exception("Unknown type of pathway type");
	}

	public void setTypeOfInterpolation(String typeOfInterpolation) throws Exception {
		if (typeOfInterpolation.equals("CUBIC_B_SPLINE"))
			this.typeOfInterpolation = Splines.TypeOfInterpolation.CUBIC_B_SPLINE;
		else if (typeOfInterpolation.equals("CUBIC_SPLINE"))
			this.typeOfInterpolation = Splines.TypeOfInterpolation.CUBIC_SPLINE;
		else
			throw new Exception("Unknown type of interpolation");
	}

	@SuppressWarnings("unchecked")
	@Override
	public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
		this.controlPoints = (ArrayList<Vector2>) in.readObject();
		this.pathwayType = (PathwayType) in.readObject();
		this.typeOfInterpolation = (TypeOfInterpolation) in.readObject();
		this.textureType = in.readInt();
		this.distanceMapNodesCount = in.readFloat();
	}

	@Override
	public void writeExternal(ObjectOutput out) throws IOException {
		out.writeObject(this.controlPoints);
		out.writeObject(this.pathwayType);
		out.writeObject(this.typeOfInterpolation);
		out.writeInt(this.textureType);
		out.writeFloat(this.distanceMapNodesCount);
	}

}
