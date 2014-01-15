package cz.mff.cuni.autickax.entities;

import java.io.IOException;
import java.io.Serializable;

import com.badlogic.gdx.utils.XmlWriter;

import cz.mff.cuni.autickax.Constants;
import cz.mff.cuni.autickax.gamelogic.SubLevel;
import cz.mff.cuni.autickax.miniGames.GearShiftMinigame;
import cz.mff.cuni.autickax.miniGames.Minigame;
import cz.mff.cuni.autickax.scene.GameScreen;

public final class Mud extends GameObject implements Serializable {
	private static final long serialVersionUID = 1L;

	public Mud(float x, float y, GameScreen gameScreen, int type) {	
		super(x,y,gameScreen, type);
	}
	
	public Mud(GameObject object){
		super(object);		
	}
	

	@Override
	public void update(float delta) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getName() { 
		return "mud";
	}

	@Override
	void aditionalsToXml(XmlWriter writer) throws IOException {
		// TODO Auto-generated method stub
		
	}
		
	/** Gets the texture name according to a type*/
	public static  String GetTextureName(int type){
		return Constants.MUD_TEXTURE_PREFIX + type;			
		
	}
	
	@Override
	public GameObject copy() {
		return new Mud(this);
	}

	@Override
	public void setTexture(int type) {
		super.setTexture(Mud.GetTextureName(type));		
	}

	@Override
	public Minigame getMinigame(GameScreen gameScreen, SubLevel parent) {
		return new GearShiftMinigame(gameScreen, parent);
	}
	
	@Override
	public String getSoundName() {
		return Constants.SOUND_MUD;
	}

}
