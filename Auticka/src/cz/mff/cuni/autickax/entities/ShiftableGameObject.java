package cz.mff.cuni.autickax.entities;

import com.badlogic.gdx.math.Vector2;

public abstract class ShiftableGameObject extends GameObject {	
	protected Vector2 shift;
	
	/** Parameterless constructor for the externalization */
	public ShiftableGameObject() {
		
	}
	
	public void move(Vector2 newPos) {
		newPos.add(shift);
		super.move(newPos);		
	}
	
	public void reset(){
		super.reset();
		this.shift = Vector2.Zero;
	}
	public ShiftableGameObject(float startX, float startY, int type) {
		super(startX, startY, type);
		shift = Vector2.Zero;
	}
	public ShiftableGameObject(GameObject object){
		super(object);	
		shift = Vector2.Zero;
	}
	
	public void setShift(Vector2 value){
		this.shift = value;
	}
	
}
