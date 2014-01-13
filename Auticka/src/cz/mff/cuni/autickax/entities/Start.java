package cz.mff.cuni.autickax.entities;

import java.io.IOException;
import java.io.Serializable;

import com.badlogic.gdx.utils.XmlWriter;

import cz.mff.cuni.autickax.Constants;
import cz.mff.cuni.autickax.gamelogic.SubLevel;
import cz.mff.cuni.autickax.miniGames.Minigame;
import cz.mff.cuni.autickax.scene.GameScreen;

public class Start extends GameObject implements Serializable {
	private static final long serialVersionUID = 1L;

	public Start(float x, float y, GameScreen gameScreen, int type) {	
		super(x,y,gameScreen);
		super.type = type;
		super.setMeasurements(Start.GetWidth(type), Start.GetHeight(type));
		this.setTexture();
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
	
	/** Gets the width according to a type*/
	public static int GetWidth(int type){
		switch (type) {
		case 1:
			return Constants.START_TYPE_1_WIDTH;			
		case 2:
			return Constants.START_TYPE_2_WIDTH;	
		case 3:
			return Constants.START_TYPE_3_WIDTH;
		case 4:
			return Constants.START_TYPE_4_WIDTH;
		default:
			//TODO exception
			return 0;
		}
	}
	/** Gets the height according to a type*/
	public static int GetHeight(int type){
		switch (type) {
		case 1:
			return Constants.START_TYPE_1_HEIGHT;			
		case 2:
			return Constants.START_TYPE_2_HEIGHT;	
		case 3:
			return Constants.START_TYPE_3_HEIGHT;
		case 4:
			return Constants.START_TYPE_4_HEIGHT;
		default:
			//TODO exception
			return 0;
		}
	}	
	/** Gets the texture name according to a type*/
	public static  String GetTextureName(int type){
		switch (type) {
		case 1:
			return Constants.START_TYPE_1_TEXTURE_NAME;			
		case 2:
			return Constants.START_TYPE_2_TEXTURE_NAME;
		case 3:
			return Constants.START_TYPE_3_TEXTURE_NAME;
		case 4:
			return Constants.START_TYPE_4_TEXTURE_NAME;
		default:
			//TODO exception
			return null;
		}
	}
	
	@Override
	public GameObject copy() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setTexture() {
		super.setTexture(Start.GetTextureName(type));		
	}

	@Override
	public Minigame getMinigame(GameScreen gameScreen, SubLevel parent) {
		// TODO Auto-generated method stub
		return null;
	}

}
