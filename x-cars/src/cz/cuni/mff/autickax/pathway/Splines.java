package cz.cuni.mff.autickax.pathway;

import java.util.ArrayList;

import com.badlogic.gdx.math.Vector2;

public class Splines {
	public enum TypeOfInterpolation {
		CUBIC_B_SPLINE, CUBIC_SPLINE
	};

	private static float B0(float u) {
		return (1 - u) * (1 - u) * (1 - u) / 6;
	}

	private static float B1(float u) {
		return (3 * u * u * u - 6 * u * u + 4) / 6;
	}

	private static float B2(float u) {
		return (-3 * u * u * u + 3 * u * u + 3 * u + 1) / 6;
	}

	private static float B3(float u) {
		return u * u * u / 6;
	}

	public static Vector2 GetLocalPointSpline(ArrayList<Vector2> points,
			float u, int index) {

		float a0x, a1x, a2x, a3x, a0y, a1y, a2y, a3y;

		a0x = points.get((index + 3) % points.size()).x
				- points.get((index + 2) % points.size()).x
				- points.get((index + 0) % points.size()).x
				+ points.get((index + 1) % points.size()).x; // p
		a1x = points.get((index + 0) % points.size()).x
				- points.get((index + 1) % points.size()).x - a0x;
		a2x = points.get((index + 2) % points.size()).x
				- points.get((index + 0) % points.size()).x;
		a3x = points.get((index + 1) % points.size()).x;

		a0y = points.get((index + 3) % points.size()).y
				- points.get((index + 2) % points.size()).y
				- points.get((index + 0) % points.size()).y
				+ points.get((index + 1) % points.size()).y; // p
		a1y = points.get((index + 0) % points.size()).y
				- points.get((index + 1) % points.size()).y - a0y;
		a2y = points.get((index + 2) % points.size()).y
				- points.get((index + 0) % points.size()).y;
		a3y = points.get((index + 1) % points.size()).y;

		return new Vector2(a0x * u * u * u + a1x * u * u + a2x * u + a3x, a0y
				* u * u * u + a1y * u * u + a2y * u + a3y);
	}

	public static Vector2 GetLocalPointBSpline(ArrayList<Vector2> points,
			float u, int index) {
		return new Vector2(B0(u) * points.get(index % points.size()).x + B1(u)
				* points.get((index + 1) % points.size()).x + B2(u)
				* points.get((index + 2) % points.size()).x + B3(u)
				* points.get((index + 3) % points.size()).x, B0(u)
				* points.get(index % points.size()).y + B1(u)
				* points.get((index + 1) % points.size()).y + B2(u)
				* points.get((index + 2) % points.size()).y + B3(u)
				* points.get((index + 3) % points.size()).y);
	}

	public static Vector2 GetPoint(ArrayList<Vector2> points, int index, float localU,
			TypeOfInterpolation type) {	
		switch (type) {
		case CUBIC_B_SPLINE:
			return GetLocalPointBSpline(points, localU, index);
		case CUBIC_SPLINE:
			return GetLocalPointSpline(points, localU, index);
		default:
			throw new RuntimeException("Unrecognized spline interpolation type: " + type.toString());
		}

	}
	
	public static Vector2 GetPoint(ArrayList<Vector2> points, float u,
			TypeOfInterpolation type, Pathway.PathwayType pathwayType) {
		float part = 0;
		int index = 0;
		float localU = 0;
		
		switch (pathwayType) {
		case CLOSED:
			part = 1f / (points.size());
			index = (int) (u / part);
			localU = (u - (float) index * part) / part;
			break;
		case OPENED:
			part = 1f / (points.size() - 3);
			index = (int) (u / part);			
			localU = (u - (float) index * part) / part;
			break;
		default:
			throw new RuntimeException("Unrecognized pathway type: " + pathwayType.toString());
			
		}

		switch (type) {
		case CUBIC_B_SPLINE:
			return GetLocalPointBSpline(points, localU, index);
		case CUBIC_SPLINE:
			return GetLocalPointSpline(points, localU, index);
		default:
			throw new RuntimeException("Unrecognized spline interpolation type: " + type.toString());
		}

	}

}
