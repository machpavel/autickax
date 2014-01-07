package cz.mff.cuni.autickax.entities;

import java.io.IOException;

import com.badlogic.gdx.utils.XmlWriter;
import cz.mff.cuni.autickax.scene.GameScreen;

public final class Mud extends GameObject{

	public Mud(float x, float y, GameScreen gameScreen, int type) {	
		super(x,y,gameScreen);
		switch (type) {
		case 0:
			this.width = 30;
			this.height = 30;	
			this.textureName = "mud";
			this.texture = this.game.assets.getGraphics(textureName);			
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
