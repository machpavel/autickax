package cz.mff.cuni.autickax.drawing;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import cz.mff.cuni.autickax.Autickax;
import cz.mff.cuni.autickax.Constants;

public class LevelBackground {
	
	private int type;
	private TextureRegion backgroundTexture;	
	
	
	public TextureRegion GetTexture(){
		return this.backgroundTexture;
	}
	
	public void SetType(int type){
		this.type = type;
		this.backgroundTexture = Autickax.getInstance().assets.getGraphics(LevelBackground.GetTextureName(type));			
	}
	
	public void draw(SpriteBatch batch, float stageWidth, float stageHeight){
		batch.draw(this.backgroundTexture, 0, 0, stageWidth, stageHeight);
	}
	
	public int GetType(){
		return this.type;
	}
	
	/** Gets the height according to a type*/
	public static String GetTextureName(int type){
		switch (type) {
		case 1:
			return Constants.LEVEL_BACKGROUND_TEXTURE_TYPE_1;
		case 2:
			return Constants.LEVEL_BACKGROUND_TEXTURE_TYPE_2;
		case 3:
			return Constants.LEVEL_BACKGROUND_TEXTURE_TYPE_3;
		case 4:
			return Constants.LEVEL_BACKGROUND_TEXTURE_TYPE_4;
		default:
			//TODO exception
			return null;
		}
	}	
	/** Gets the height according to a type*/
	public static String GetSmallTextureName(int type){
		switch (type) {
		case 1:
			return Constants.LEVEL_BACKGROUND_TEXTURE_TYPE_1_SMALL;
		case 2:
			return Constants.LEVEL_BACKGROUND_TEXTURE_TYPE_2_SMALL;
		case 3:
			return Constants.LEVEL_BACKGROUND_TEXTURE_TYPE_3_SMALL;
		case 4:
			return Constants.LEVEL_BACKGROUND_TEXTURE_TYPE_4_SMALL;
		default:
			//TODO exception
			return null;
		}
	}
	
}
