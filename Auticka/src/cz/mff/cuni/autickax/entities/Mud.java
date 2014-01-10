package cz.mff.cuni.autickax.entities;

import java.io.IOException;

import com.badlogic.gdx.utils.XmlWriter;

import cz.mff.cuni.autickax.Constants;
import cz.mff.cuni.autickax.scene.GameScreen;

public final class Mud extends GameObject{

	public Mud(float x, float y, GameScreen gameScreen, int type) {	
		super(x,y,gameScreen);
		switch (type) {
		case 0:
			super.setMeasurements(Constants.MUD_TYPE_1_WIDTH, Constants.MUD_TYPE_1_HEIGHT);
			super.setTexture(Constants.MUD_TYPE_1_TEXTURE_NAME);			
			break;
		default:
			break;
		}
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

}
