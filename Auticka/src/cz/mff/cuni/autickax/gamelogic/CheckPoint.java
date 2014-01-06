package cz.mff.cuni.autickax.gamelogic;

/**
 * Represents one point that player did went through. Contains time of the event and coordinates.
 */
public class CheckPoint {
	double time = 0;
	double x = 0;
	double y = 0;
	
	/**
	 * @return Gets time in milliseconds from the start of path following.
	 */
	double getTime() {
		return this.time;
	}
	
	/**
	 * @return Gets X-coordinate.
	 */
	double getX() {
		return this.x;
	}
	
	/**
	 * @return Gets y-coordinate.
	 */
	double getY() {
		return this.y;
	}
	
	/**
	 * @param time Time in milliseconds from the start of path following. 
	 * @param x x-coordinate.
	 * @param y y-coordinate.
	 */
	CheckPoint(double time, double x, double y) {
		this.time = time;
		this.x = x;
		this.y = y;
	}
}
