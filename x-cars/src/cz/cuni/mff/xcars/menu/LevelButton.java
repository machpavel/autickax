package cz.cuni.mff.xcars.menu;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import cz.cuni.mff.xcars.Xcars;
import cz.cuni.mff.xcars.constants.Constants;

public class LevelButton {
	public final static int MAXIMAL_STARS_COUNT = 3;
	
	static TextureRegion[] buttons = new TextureRegion[MAXIMAL_STARS_COUNT + 1];
	static TextureRegion[] buttonsHover = new TextureRegion[MAXIMAL_STARS_COUNT + 1];
	
	public static TextureRegion getButtonHoverTexture(byte stars) {
		return getButtonTextureFromCache(true, stars);
	}

	public static TextureRegion getButtonTexture(byte stars) {
		return getButtonTextureFromCache(false, stars);
	}
	
	private static Pixmap GetPixmap(String textureRegionName) {
		TextureRegion region = Xcars.getInstance().assets.getGraphics(textureRegionName);
		Texture texture = region.getTexture();
		if (!texture.getTextureData().isPrepared()) {
			texture.getTextureData().prepare();
		}
		Pixmap texturePixmap = texture.getTextureData().consumePixmap();
		
		int regionX = region.getRegionX();
		int regionY = region.getRegionY();
		
		Pixmap retvalPixmap = new Pixmap(region.getRegionWidth(), region.getRegionHeight(), Format.RGBA8888);
		
		
		for (int x = 0; x < region.getRegionWidth(); ++x){
			for (int y = 0; y < region.getRegionHeight(); ++y) {
				retvalPixmap.drawPixel(x, y, texturePixmap.getPixel(x + regionX, y + regionY));
			}
		}
		
		//retvalPixmap.drawPixmap(texturePixmap, -region.getRegionX(), -region.getRegionY());
		
		return retvalPixmap;
	}
	
	private static TextureRegion getButtonTextureFromCache(boolean hover, byte stars) {
		TextureRegion[] regions = hover ? buttonsHover : buttons;
		
		if (regions[stars] == null)
		{
			regions[stars] = getButtonTexture(hover, stars);
		}
		
		return regions[stars];
	}

	private static TextureRegion getButtonTexture(boolean hover, byte stars) {
		String textureName = hover ? Constants.menu.BUTTON_MENU_LEVEL_HOVER : Constants.menu.BUTTON_MENU_LEVEL;
		
		Pixmap buttonPixmap = GetPixmap(textureName);
		int buttonHeight = buttonPixmap.getWidth();
		
		Pixmap emptyStar = GetPixmap(Constants.menu.LEVEL_STAR_EMPTY);
		Pixmap fullStar = GetPixmap(Constants.menu.LEVEL_STAR_FULL);
		int starHeight = emptyStar.getHeight();
		int starWidth = emptyStar.getWidth();
		
		int starTop = buttonHeight - 2 * starHeight;
		
		int[] starsPositions =
		{
			(buttonHeight - (int)(3.5 * starWidth)) / 2,
			(buttonHeight - starWidth) / 2,
			(buttonHeight + (int)(1.5 * starWidth)) / 2
		};
		
		int i = 0;
		for (; i < stars; ++i) {
			buttonPixmap.drawPixmap(emptyStar, starsPositions[i], starTop);
		}

		for (; i < MAXIMAL_STARS_COUNT; ++i) {
			buttonPixmap.drawPixmap(fullStar, starsPositions[i], starTop);
		}

		Texture buttonTexture = new Texture(buttonPixmap);
		TextureRegion buttonTextureRegion = new TextureRegion(buttonTexture);

		return buttonTextureRegion;
	}
}
