package cz.mff.cuni.autickax.entities;

import java.io.IOException;

import com.badlogic.gdx.utils.XmlWriter;

import cz.mff.cuni.autickax.Constants;
import cz.mff.cuni.autickax.scene.GameScreen;

public class Finish extends GameObject {
	
	public Finish(float x, float y, GameScreen gameScreen, int type) {	
		super(x,y,gameScreen);
		switch (type) {
		case 0:
			super.setMeasurements(Constants.FINISH_TYPE_1_WIDTH, Constants.FINISH_TYPE_1_HEIGHT);
			super.setTexture(Constants.FINISH_TYPE_1_TEXTURE_NAME);	
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
		return "finish";
	}

	@Override
	void aditionalsToXml(XmlWriter writer) throws IOException {
		// TODO Auto-generated method stub		
	}

}
