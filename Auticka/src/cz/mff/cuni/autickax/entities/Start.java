package cz.mff.cuni.autickax.entities;

import java.io.IOException;

import com.badlogic.gdx.utils.XmlWriter;

import cz.mff.cuni.autickax.Constants;
import cz.mff.cuni.autickax.scene.GameScreen;

public class Start extends GameObject {
	
	public Start(float x, float y, GameScreen gameScreen, int type) {	
		super(x,y,gameScreen);
		switch (type) {
		case 1:
			super.setMeasurements(Constants.START_TYPE_1_WIDTH, Constants.START_TYPE_1_HEIGHT);
			super.setTexture(Constants.START_TYPE_1_TEXTURE_NAME);			
			break;
		case 2:
			super.setMeasurements(Constants.START_TYPE_2_WIDTH, Constants.START_TYPE_2_HEIGHT);
			super.setTexture(Constants.START_TYPE_2_TEXTURE_NAME);			
			break;
		case 3:
			super.setMeasurements(Constants.START_TYPE_3_WIDTH, Constants.START_TYPE_3_HEIGHT);
			super.setTexture(Constants.START_TYPE_3_TEXTURE_NAME);			
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
		// TODO Auto-generated method stub
		return "start";
	}

	@Override
	void aditionalsToXml(XmlWriter writer) throws IOException {
		// TODO Auto-generated method stub		
	}

}
