package cz.mff.cuni.autickax.gamelogic;

/**
 * Represents one point that player did went through. Contains time of the event and coordinates.
 */
public class CheckPoint {
	float time = 0;
	float x = 0;
	float y = 0;
	
	/**
	 * @return Gets time in milliseconds from the start of path following.
	 */
	float getTime() {
		return this.time;
	}
	
	/**
	 * @return Gets X-coordinate.
	 */
	float getX() {
		return this.x;
	}
	
	/**
	 * @return Gets y-coordinate.
	 */
	float getY() {
		return this.y;
	}
	
	/**
	 * @param time Time in milliseconds from the start of path following. 
	 * @param x x-coordinate.
	 * @param y y-coordinate.
	 */
	CheckPoint(float time, float x, float y) {
		this.time = time;
		this.x = x;
		this.y = y;
	}
}
