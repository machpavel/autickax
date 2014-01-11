package cz.mff.cuni.autickax.entities;

import java.io.IOException;

import com.badlogic.gdx.utils.XmlWriter;

import cz.mff.cuni.autickax.Constants;
import cz.mff.cuni.autickax.scene.GameScreen;

public final class Hole extends GameObject {

	public Hole(float x, float y, GameScreen gameScreen, int type) {	
		super(x,y,gameScreen);
		switch (type) {
		case 1:
			super.type = 1;
			super.setMeasurements(Constants.HOLE_TYPE_1_WIDTH, Constants.HOLE_TYPE_1_HEIGHT);
			super.setTexture(Constants.HOLE_TYPE_1_TEXTURE_NAME);		
			break;
		case 2:
			super.type = 2;
			super.setMeasurements(Constants.HOLE_TYPE_2_WIDTH, Constants.HOLE_TYPE_2_HEIGHT);
			super.setTexture(Constants.HOLE_TYPE_2_TEXTURE_NAME);		
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
		return "hole";
	}

	@Override
	void aditionalsToXml(XmlWriter writer) throws IOException {
		// TODO Auto-generated method stub

	}

}
