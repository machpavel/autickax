package cz.mff.cuni.autickax.drawing;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import cz.mff.cuni.autickax.Autickax;
import cz.mff.cuni.autickax.Constants;

public class Background {

	private int type;
	// Textures
	private TextureRegion backgroundTexture;	
	
	public TextureRegion GetTexture(){
		return this.backgroundTexture;
	}
	
	public void SetType(int type){
		switch (type) {
		case 1:
			this.type = type;
			this.backgroundTexture = Autickax.getInstance().assets.getGraphics(Constants.LEVEL_BACKGROUND_TEXTURE_TYPE_1);
			break;
		case 2:
			this.type = type;
			this.backgroundTexture = Autickax.getInstance().assets.getGraphics(Constants.LEVEL_BACKGROUND_TEXTURE_TYPE_2);
			break;
		case 3:
			this.type = type;
			this.backgroundTexture = Autickax.getInstance().assets.getGraphics(Constants.LEVEL_BACKGROUND_TEXTURE_TYPE_3);
			break;
		case 4:
			this.type = type;
			this.backgroundTexture = Autickax.getInstance().assets.getGraphics(Constants.LEVEL_BACKGROUND_TEXTURE_TYPE_4);
			break;
		default:
			break;
		}
	}
	
	public void draw(SpriteBatch batch, float stageWidth, float stageHeight){
		batch.draw(this.backgroundTexture, 0, 0, stageWidth, stageHeight);
	}
	
	public int GetType(){
		return this.type;
	}
	
}
