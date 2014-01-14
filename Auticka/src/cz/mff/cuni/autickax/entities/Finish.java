package cz.mff.cuni.autickax.entities;

import java.io.IOException;
import java.io.Serializable;

import com.badlogic.gdx.utils.XmlWriter;

import cz.mff.cuni.autickax.Constants;
import cz.mff.cuni.autickax.gamelogic.SubLevel;
import cz.mff.cuni.autickax.miniGames.Minigame;
import cz.mff.cuni.autickax.scene.GameScreen;

public class Finish extends GameObject implements Serializable {
	private static final long serialVersionUID = 1L;

	public Finish(float x, float y, GameScreen gameScreen, int type) {	
		super(x,y,gameScreen);
		super.type = type;
		super.setMeasurements(Finish.GetWidth(type), Finish.GetHeight(type));
		this.setTexture();
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
	
	/** Gets the width according to a type*/
	public static int GetWidth(int type){
		switch (type) {
		case 1:
			return Constants.FINISH_TYPE_1_WIDTH;					
		case 2:
			return Constants.FINISH_TYPE_2_WIDTH;
		default:
			//TODO exception
			return 0;
		}
	}
	/** Gets the height according to a type*/
	public static int GetHeight(int type){
		switch (type) {
		case 1:
			return Constants.FINISH_TYPE_1_HEIGHT;			
		case 2:
			return Constants.FINISH_TYPE_2_HEIGHT;
		default:
			//TODO exception
			return 0;
		}
	}	
	/** Gets the texture name according to a type*/
	public static  String GetTextureName(int type){
		switch (type) {
		case 1:
			return Constants.FINISH_TYPE_1_TEXTURE_NAME;
		case 2:
			return Constants.FINISH_TYPE_2_TEXTURE_NAME;
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
		super.setTexture(Finish.GetTextureName(type));		
	}

	@Override
	public Minigame getMinigame(GameScreen gameScreen, SubLevel parent) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public String getSoundName() {
		return Constants.SOUND_NO_SOUND;
	}

}
