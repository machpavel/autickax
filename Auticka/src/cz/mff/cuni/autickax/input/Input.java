package cz.mff.cuni.autickax.input;

import com.badlogic.gdx.Gdx;

public class Input {

	private final static float WORLD_WIDTH = 800;
	private final static float WORLD_HEIGHT = 480; 
	
	public static float xStretchFactor;
	public static float yStretchFactor;
	
	public static void InitDimensions() {
		Input.xStretchFactor = WORLD_WIDTH / Gdx.graphics.getWidth();
		Input.yStretchFactor = WORLD_HEIGHT / Gdx.graphics.getHeight();
	}
	
	public static int getX() {
		return (int)(Gdx.input.getX() * Input.xStretchFactor);
	}
	
	public static int getY() {
		return (int)((Gdx.graphics.getHeight() - Gdx.input.getY()) * Input.yStretchFactor);
	}
}
