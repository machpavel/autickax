package cz.mff.cuni.autickax.entities;

import java.io.Externalizable;
import cz.mff.cuni.autickax.constants.Constants;

public final class AvoidHole extends GameObject implements Externalizable {

	public AvoidHole(float x, float y, int type) {	
		super(x, y, type);
	}
	
	public AvoidHole(GameObject object){
		super(object);		
	}
	
	/** Parameterless constructor for the externalization */
	public AvoidHole() {
	}
		
	/** Gets the texture name according to a type*/
	public static  String GetTextureName(int type){
		return Constants.gameObjects.AVOID_HOLES_TEXTURE_NAME_PREFIX + type;
		
	}
	
	@Override
	public GameObject copy() {
		return new AvoidHole(this);
	}

	@Override
	public void setTexture(int type) {
		super.setTexture(AvoidHole.GetTextureName(type));		
	}
	
	
	@Override
	public String getSoundName() {
		return Constants.sounds.SOUND_HOLE;
	}

	@Override
	public String getName() {
		return "avoidhole";
	}

}
