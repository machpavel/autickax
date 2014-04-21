package cz.mff.cuni.autickax.entities;

import java.io.Externalizable;
import java.io.IOException;

import com.badlogic.gdx.utils.XmlWriter;

import cz.mff.cuni.autickax.constants.Constants;
import cz.mff.cuni.autickax.gamelogic.SubLevel;
import cz.mff.cuni.autickax.miniGames.Minigame;
import cz.mff.cuni.autickax.scene.GameScreen;

public final class AvoidHole extends GameObject implements Externalizable {

	public AvoidHole(float x, float y, GameScreen gameScreen, int type) {	
		super(x,y,gameScreen, type);
	}
	
	public AvoidHole(GameObject object){
		super(object);		
	}
	
	/** Parameterless constructor for the externalization */
	public AvoidHole() {
	}
	

	@Override
	public void update(float delta) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getName() {		
		return "avoidhole";
	}

	@Override
	void aditionalsToXml(XmlWriter writer) throws IOException {
		// TODO Auto-generated method stub
		
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
	public Minigame getMinigame(GameScreen gameScreen, SubLevel parent) {
		return null;
	}
	
	@Override
	public String getSoundName() {
		return Constants.sounds.SOUND_HOLE;
	}

}
