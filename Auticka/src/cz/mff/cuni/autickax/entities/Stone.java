package cz.mff.cuni.autickax.entities;

import java.io.IOException;
import java.io.Serializable;

import com.badlogic.gdx.utils.XmlWriter;

import cz.mff.cuni.autickax.Constants;
import cz.mff.cuni.autickax.gamelogic.SubLevel;
import cz.mff.cuni.autickax.miniGames.AvoidObstaclesMinigame;
import cz.mff.cuni.autickax.miniGames.Minigame;
import cz.mff.cuni.autickax.miniGames.AvoidObstaclesMinigame.ObstaclesType;
import cz.mff.cuni.autickax.scene.GameScreen;

public final class Stone extends GameObject implements Serializable {
	private static final long serialVersionUID = 1L;

	public Stone(float x, float y, GameScreen gameScreen, int type) {	
		super(x,y,gameScreen);
		super.type = type;		
		this.setTexture();
	}
	
	public Stone(GameObject object){
		super(object);		
	}
	

	@Override
	public void update(float delta) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getName() {		
		return "stone";
	}

	@Override
	void aditionalsToXml(XmlWriter writer) throws IOException {
		// TODO Auto-generated method stub
		
	}
		
	/** Gets the texture name according to a type*/
	public static  String GetTextureName(int type){
		return Constants.STONE_TEXTURE_PREFIX + type;
	}
	
	@Override
	public GameObject copy() {
		return new Stone(this);
	}

	@Override
	public void setTexture() {
		super.setTexture(Stone.GetTextureName(type));		
	}

	@Override
	public Minigame getMinigame(GameScreen gameScreen, SubLevel parent) {
		return new AvoidObstaclesMinigame(gameScreen, parent, ObstaclesType.STONES);
	}
	
	@Override
	public String getSoundName() {
		return Constants.SOUND_STONE;
	}

}
