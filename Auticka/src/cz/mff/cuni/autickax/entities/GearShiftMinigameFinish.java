package cz.mff.cuni.autickax.entities;

import java.io.Externalizable;
import java.io.IOException;

import com.badlogic.gdx.utils.XmlWriter;

import cz.mff.cuni.autickax.constants.Constants;
import cz.mff.cuni.autickax.gamelogic.SubLevel;
import cz.mff.cuni.autickax.miniGames.Minigame;
import cz.mff.cuni.autickax.scene.GameScreen;

public class GearShiftMinigameFinish extends GameObject implements Externalizable {

	public GearShiftMinigameFinish(float x, float y) {	
		super(x, y, 0);
		this.boundingCircleRadius = Constants.minigames.GEAR_SHIFT_MINIGAME_FINISH_RADIUS;
	}
	
	/** Parameterless constructor for the externalization */
	public GearShiftMinigameFinish() {
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
	public void setTexture(int type) {
		super.setTexture(Constants.minigames.GEAR_SHIFT_MINIGAME_FINISH_TEXTURE);		
	}

	@Override
	public Minigame getMinigame(GameScreen gameScreen, SubLevel parent) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public String getSoundName() {
		return Constants.sounds.SOUND_NO_SOUND;
	}

}
