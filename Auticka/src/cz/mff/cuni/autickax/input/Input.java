package cz.mff.cuni.autickax.input;

import com.badlogic.gdx.Gdx;

import cz.mff.cuni.autickax.Constants;

public class Input {	
	public static float xStretchFactor;
	public static float yStretchFactor;
	
	public static void InitDimensions() {
		Input.xStretchFactor = Constants.WORLD_WIDTH / Gdx.graphics.getWidth();
		Input.yStretchFactor = Constants.WORLD_HEIGHT / Gdx.graphics.getHeight();
	}
	
	public static int getX() {
		return (int)(Gdx.input.getX() * Input.xStretchFactor);
	}
	
	public static int getY() {
		return (int)((Gdx.graphics.getHeight() - Gdx.input.getY()) * Input.yStretchFactor);
	}
}
