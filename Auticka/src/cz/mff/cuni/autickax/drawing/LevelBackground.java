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
		return Constants.LEVEL_BACKGROUND_TEXTURE_PREFIX + type;
	}	
	/** Gets the height according to a type*/
	public static String GetSmallTextureName(int type){
		return Constants.LEVEL_BACKGROUND_TEXTURE_PREFIX + type + Constants.LEVEL_SMALL_BACKGROUND_TEXTURE_POSTFIX;
	}
	
}
