package cz.mff.cuni.autickax.entities;

import java.io.Externalizable;
import java.io.IOException;

import com.badlogic.gdx.utils.XmlWriter;

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
	
	/** Gets the texture name according to a type*/
	public String GetTextureName(int type){
		return this.getName() + type;
	}
	
	public void setTexture(int type) {
		super.setTexture(this.GetTextureName(type));		
	}
	
	protected abstract String getResultMessage();

	
	@Override // remove...
	void aditionalsToXml(XmlWriter writer) throws IOException {
		// TODO Auto-generated method stub

	}
}
