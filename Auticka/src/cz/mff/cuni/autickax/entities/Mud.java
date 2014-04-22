package cz.mff.cuni.autickax.entities;

import java.io.Externalizable;
import java.io.IOException;

import com.badlogic.gdx.utils.XmlWriter;

import cz.mff.cuni.autickax.constants.Constants;
import cz.mff.cuni.autickax.gamelogic.SubLevel;
import cz.mff.cuni.autickax.miniGames.GearShiftMinigame;
import cz.mff.cuni.autickax.miniGames.Minigame;
import cz.mff.cuni.autickax.scene.GameScreen;

public final class Mud extends GameObject implements Externalizable {

	public Mud(float x, float y, int type) {	
		super(x, y, type);
	}
	
	public Mud(GameObject object){
		super(object);		
	}
	
	/** Parameterless constructor for the externalization */
	public Mud() {
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
		return Constants.gameObjects.MUD_TEXTURE_PREFIX + type;			
		
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
		return Constants.sounds.SOUND_MUD;
	}

}
