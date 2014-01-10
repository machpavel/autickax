package cz.mff.cuni.autickax.entities;

import java.io.IOException;

import com.badlogic.gdx.utils.XmlWriter;
import cz.mff.cuni.autickax.Constants;
import cz.mff.cuni.autickax.scene.GameScreen;

public final class Stone extends GameObject {

	public Stone(float x, float y, GameScreen gameScreen, int type) {	
		super(x,y,gameScreen);
		switch (type) {
		case 0:
			super.setMeasurements(Constants.STONE_TYPE_1_WIDTH, Constants.STONE_TYPE_1_HEIGHT);
			super.setTexture(Constants.STONE_TYPE_1_TEXTURE_NAME);						
			break;
		default:
			break;
		}
	}
	

	@Override
	public void update(float delta) {
		this.rotation  = (this.rotation + delta * 50) % 360;
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

}
