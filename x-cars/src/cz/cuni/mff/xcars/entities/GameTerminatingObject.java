package cz.cuni.mff.xcars.entities;

import java.io.Externalizable;

import cz.cuni.mff.xcars.gamelogic.SubLevel;
import cz.cuni.mff.xcars.miniGames.Crash;
import cz.cuni.mff.xcars.miniGames.Minigame;
import cz.cuni.mff.xcars.scene.GameScreen;

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
