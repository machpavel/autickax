package cz.mff.cuni.autickax.entities;

import java.io.Externalizable;

import cz.mff.cuni.autickax.constants.Constants;

public class Fence extends GameTerminatingObject implements Externalizable {
	
	public Fence(float x, float y, int type) {	
		super(x, y, type);
	}
	
	public Fence(GameObject object){
		super(object);		
	}
	
	/** Parameterless constructor for the externalization */
	public Fence() {
	}
	
	@Override
	protected String getResultMessage() {
		return Constants.strings.TOOLTIP_MINIGAME_CRASHED_FENCE_RESULT_MESSAGE;
	}

	@Override
	public String getName() {
		return "fence";
	}

	@Override
	public GameObject copy() {
		return new Fence(this);
	}


	@Override
	public String getSoundName() {
		// TODO add custom sounds!
		return Constants.sounds.SOUND_TREE; 
	}

}
