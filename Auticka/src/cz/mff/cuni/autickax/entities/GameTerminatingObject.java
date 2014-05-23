package cz.mff.cuni.autickax.entities;

import java.io.Externalizable;

import cz.mff.cuni.autickax.gamelogic.SubLevel;
import cz.mff.cuni.autickax.miniGames.Crash;
import cz.mff.cuni.autickax.miniGames.Minigame;
import cz.mff.cuni.autickax.scene.GameScreen;

public abstract class GameTerminatingObject extends GameObject implements Externalizable {

	/** Parameterless constructor for the externalization */
	protected GameTerminatingObject() {
	}

	public GameTerminatingObject(float x, float y, int type) {
		super(x, y, type);
	}

	public GameTerminatingObject(GameObject object) {
		super(object);
	}

	@Override
	public Minigame getMinigame(GameScreen gameScreen, SubLevel parent) {
		return new Crash(this.getResultMessage(), gameScreen, parent);
	}
	
	public void setTexture(int type) {
		super.setTexture(this.GetTextureName(type));		
	}
	
	protected abstract String getResultMessage();
	
	public abstract String GetTextureName(int type);
}
