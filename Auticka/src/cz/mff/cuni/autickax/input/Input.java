package cz.mff.cuni.autickax.input;

import com.badlogic.gdx.Gdx;

import cz.mff.cuni.autickax.constants.Constants;

public class Input {
	public static float xStretchFactor;
	public static float yStretchFactor;
	public static float xStretchFactorInv;
	public static float yStretchFactorInv;

	public static void InitDimensions() {
		Input.xStretchFactor = (float) Constants.WORLD_WIDTH / Gdx.graphics.getWidth();
		Input.yStretchFactor = (float) Constants.WORLD_HEIGHT / Gdx.graphics.getHeight();
		xStretchFactorInv = 1.0f / xStretchFactor;
		yStretchFactorInv = 1.0f / yStretchFactor;
	}

	public static void InitDimensionsInEditor() {
		Input.xStretchFactor = 1;
		Input.yStretchFactor = 1;
		xStretchFactorInv = 1.0f / xStretchFactor;
		yStretchFactorInv = 1.0f / yStretchFactor;
	}

	public static int getX() {
		return (int) (Gdx.input.getX() * Input.xStretchFactor);
	}

	public static int getY() {
		return (int) ((Gdx.graphics.getHeight() - Gdx.input.getY()) * Input.yStretchFactor);
	}
}
