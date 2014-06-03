package cz.cuni.mff.autickax.constants;

import com.badlogic.gdx.graphics.Color;

public class TimeStatusBarConsts {

	public final float SMALL_FONT_SCALE = 0.3f;
	public final float BIG_FONT_SCALE = 0.46527f;
	
	public final Color black90 = new Color(0.1f, 0.1f, 0.1f, 1f);
	public final Color black90alpha = new Color(0.1f, 0.1f, 0.1f, 0.1f);
	
	//Time string coordinates
	
	public final int xTimeStringLabel = Constants.WORLD_WIDTH - 150;
	public final int yTimeStringLabel = Constants.WORLD_HEIGHT - 30;
	
	//Time value coordinates
	
	public final int xTimeIntLabel = xTimeStringLabel + 75;
	public final int yTimeIntLabel = Constants.WORLD_HEIGHT - 35;
	
	//RECTANGLE - coordinates of bottom left corner and width and height
	public final int xRectangle = xTimeStringLabel - 10;
	public final int yRectangle = Constants.WORLD_HEIGHT - 40;
	public final int RectWidth = 200;
	public final int RectHeight = 40;
}
