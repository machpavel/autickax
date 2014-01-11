package cz.mff.cuni.autickax.entities;

import java.io.IOException;

import com.badlogic.gdx.utils.XmlWriter;

import cz.mff.cuni.autickax.Constants;
import cz.mff.cuni.autickax.scene.GameScreen;

public final class Mud extends GameObject{

	public Mud(float x, float y, GameScreen gameScreen, int type) {	
		super(x,y,gameScreen);
		super.type = type;
		super.setMeasurements(Mud.GetWidth(type), Mud.GetHeight(type));
		super.setTexture(Mud.GetTextureName(type));
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
	
	/** Gets the width according to a type*/
	public static int GetWidth(int type){
		switch (type) {
		case 1:
			return Constants.MUD_TYPE_1_WIDTH;			
		case 2:
			return Constants.MUD_TYPE_2_WIDTH;	
		case 3:
			return Constants.MUD_TYPE_3_WIDTH;
		default:
			//TODO exception
			return 0;
		}
	}
	/** Gets the height according to a type*/
	public static int GetHeight(int type){
		switch (type) {
		case 1:
			return Constants.MUD_TYPE_1_HEIGHT;			
		case 2:
			return Constants.MUD_TYPE_2_HEIGHT;	
		case 3:
			return Constants.MUD_TYPE_3_HEIGHT;	
		default:
			//TODO exception
			return 0;
		}
	}	
	/** Gets the texture name according to a type*/
	public static  String GetTextureName(int type){
		switch (type) {
		case 1:
			return Constants.MUD_TYPE_1_TEXTURE_NAME;			
		case 2:
			return Constants.MUD_TYPE_2_TEXTURE_NAME;
		case 3:
			return Constants.MUD_TYPE_3_TEXTURE_NAME;
		default:
			//TODO exception
			return null;
		}
	}

}
