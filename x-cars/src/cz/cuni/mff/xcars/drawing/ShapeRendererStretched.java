package cz.cuni.mff.xcars.drawing;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

import cz.cuni.mff.xcars.input.Input;

public class ShapeRendererStretched extends ShapeRenderer {

	@Override
	public void line(float x, float y, float z, float x2, float y2, float z2, Color c1, Color c2) {
		super.line(x * Input.xStretchFactorInv, y * Input.yStretchFactorInv, z, x2 * Input.xStretchFactorInv, y2
				* Input.yStretchFactorInv, z2, c1, c2);
	}

	@Override
	public void rectLine(Vector2 p1, Vector2 p2, float width) {
		this.rectLine(p1.x, p1.y, p2.x, p2.y, width);
	}

	@Override
	public void rectLine(float x1, float y1, float x2, float y2, float width) {
		super.rectLine(x1 * Input.xStretchFactorInv, y1 * Input.yStretchFactorInv, x2 * Input.xStretchFactorInv, y2
				* Input.yStretchFactorInv, width * (Input.xStretchFactorInv + Input.yStretchFactorInv) / 2);
	}

	@Override
	public void rect(float x, float y, float width, float height) {
		super.rect(x * Input.xStretchFactorInv, y * Input.yStretchFactorInv, width * Input.xStretchFactorInv, height
				* Input.yStretchFactorInv);
	}

	@Override
	public void circle(float x, float y, float radius) {
		super.circle(x * Input.xStretchFactorInv, y * Input.yStretchFactorInv, radius
				* (Input.xStretchFactorInv + Input.yStretchFactorInv) / 2);
	}

}
