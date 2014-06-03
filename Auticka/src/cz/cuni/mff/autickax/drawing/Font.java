package cz.cuni.mff.autickax.drawing;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Font {
	private final BitmapFont font;
	
	public Font(BitmapFont font) {
		this.font = font;
	}
	
	public void draw(SpriteBatch batch, String text, int x, int y) {				
		this.font.draw(batch, text, x, y);
	}
}
