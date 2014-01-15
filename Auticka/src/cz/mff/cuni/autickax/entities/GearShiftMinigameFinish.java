package cz.mff.cuni.autickax.entities;

import java.io.IOException;
import java.io.Serializable;

import com.badlogic.gdx.utils.XmlWriter;

import cz.mff.cuni.autickax.Constants;
import cz.mff.cuni.autickax.gamelogic.SubLevel;
import cz.mff.cuni.autickax.miniGames.Minigame;
import cz.mff.cuni.autickax.scene.GameScreen;

public class GearShiftMinigameFinish extends GameObject implements Serializable {
	private static final long serialVersionUID = 1L;
	

	public GearShiftMinigameFinish(float x, float y, GameScreen gameScreen) {	
		super(x,y,gameScreen);
		super.type = type;
		this.setTexture();
		this.boundingCircleRadius = Constants.GEAR_SHIFT_MINIGAME_FINISH_RADIUS;
	}

	@Override
	public void update(float delta) {
		// TODO Auto-generated method stub
	
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "gearShiftMinigameFinish";
	}

	@Override
	void aditionalsToXml(XmlWriter writer) throws IOException {
		// TODO Auto-generated method stub		
	}
		
	
	@Override
	public GameObject copy() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setTexture() {
		super.setTexture(Constants.GEAR_SHIFT_MINIGAME_FINISH_TEXTURE);		
	}

	@Override
	public Minigame getMinigame(GameScreen gameScreen, SubLevel parent) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public String getSoundName() {
		return Constants.SOUND_NO_SOUND;
	}

}
