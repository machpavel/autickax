package cz.mff.cuni.autickax.gamelogic;

import java.io.Serializable;

import com.badlogic.gdx.math.Vector2;

/**
 * Represents one point that player did went through. Contains time of the event and coordinates.
 */
public class CheckPoint implements Serializable{
	final float time;
	final Vector2 position;
	
	/**
	 * @return Gets time in milliseconds from the start of path following.
	 */
	float getTime() {
		return this.time;
	}
	
	/**
	 * @return Gets position.
	 */
	Vector2 getPosition() {
		return this.position;
	}
	
	
	/**
	 * @param time Time in milliseconds from the start of path following. 
	 * @param x x-coordinate.
	 * @param y y-coordinate.
	 */
	CheckPoint(float time, float x, float y) {
		this.time = time;
		this.position = new Vector2(x, y);		
	}
	
	CheckPoint(float time, Vector2 position) {
		this.time = time;
		this.position = new Vector2(position);		
	}
}
