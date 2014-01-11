package cz.mff.cuni.autickax.entities;

import java.io.IOException;

import com.badlogic.gdx.utils.XmlWriter;

import cz.mff.cuni.autickax.Constants;
import cz.mff.cuni.autickax.scene.GameScreen;

public final class Tree extends GameObject {

	public Tree(float x, float y, GameScreen gameScreen, int type) {	
		super(x,y,gameScreen);
		switch (type) {
		case 1:
			super.setMeasurements(Constants.TREE_TYPE_1_WIDTH, Constants.TREE_TYPE_1_HEIGHT);
			super.setTexture(Constants.TREE_TYPE_1_TEXTURE_NAME);		
			break;
		case 2:
			super.setMeasurements(Constants.TREE_TYPE_2_WIDTH, Constants.TREE_TYPE_2_HEIGHT);
			super.setTexture(Constants.TREE_TYPE_2_TEXTURE_NAME);		
			break;
		case 3:
			super.setMeasurements(Constants.TREE_TYPE_3_WIDTH, Constants.TREE_TYPE_3_HEIGHT);
			super.setTexture(Constants.TREE_TYPE_3_TEXTURE_NAME);		
			break;
		case 4:
			super.setMeasurements(Constants.TREE_TYPE_4_WIDTH, Constants.TREE_TYPE_4_HEIGHT);
			super.setTexture(Constants.TREE_TYPE_4_TEXTURE_NAME);		
			break;
		case 5:
			super.setMeasurements(Constants.TREE_TYPE_5_WIDTH, Constants.TREE_TYPE_5_HEIGHT);
			super.setTexture(Constants.TREE_TYPE_5_TEXTURE_NAME);		
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
		return "tree";
	}

	@Override
	void aditionalsToXml(XmlWriter writer) throws IOException {
		// TODO Auto-generated method stub

	}

}
