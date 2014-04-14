package cz.mff.cuni.autickax.entities;

import java.io.IOException;
import java.io.Serializable;

import com.badlogic.gdx.utils.XmlWriter;

import cz.mff.cuni.autickax.constants.Constants;
import cz.mff.cuni.autickax.gamelogic.SubLevel;
import cz.mff.cuni.autickax.miniGames.BoostMinigame;
import cz.mff.cuni.autickax.miniGames.Minigame;
import cz.mff.cuni.autickax.scene.GameScreen;

public final class Booster extends GameObject implements Serializable {
	private static final long serialVersionUID = 1L;

	public Booster(float x, float y, GameScreen gameScreen, int type) {	
		super(x,y,gameScreen, type);
	}

	public Booster(GameObject object){
		super(object);		
	}
	
	
	@Override
	public void update(float delta) {
		// TODO Auto-generated method stub

	}

	@Override
	public String getName() {
		return "booster";
	}

	@Override
	void aditionalsToXml(XmlWriter writer) throws IOException {
		// TODO Auto-generated method stub
	}

	/** Gets the texture name according to a type*/
	public static  String GetTextureName(int type){
		return Constants.gameObjects.BOOSTER_TEXTURE_NAME_PREFIX + type;
	}

	@Override
	public GameObject copy() {
		return new Booster(this);
	}

	@Override
	public void setTexture(int type) {
		// TODO Auto-generated method stub
		super.setTexture(Booster.GetTextureName(type));		
	}

	@Override
	public Minigame getMinigame(GameScreen gameScreen, SubLevel parent) {
		return new BoostMinigame(gameScreen, parent);
	}
	
	@Override
	public String getSoundName() {
		return Constants.sounds.SOUND_NO_SOUND;
	}

}
