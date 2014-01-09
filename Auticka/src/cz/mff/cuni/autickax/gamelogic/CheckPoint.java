package cz.mff.cuni.autickax.gamelogic;

import java.io.Serializable;

/**
 * Represents one point that player did went through. Contains time of the event and coordinates.
 */
public class CheckPoint implements Serializable{
	final float time;
	final float x;
	final float y;
	
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
