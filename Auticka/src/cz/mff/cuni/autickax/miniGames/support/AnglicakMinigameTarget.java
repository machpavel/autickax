package cz.mff.cuni.autickax.miniGames.support;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

import cz.mff.cuni.autickax.Autickax;
import cz.mff.cuni.autickax.constants.Constants;
import cz.mff.cuni.autickax.input.Input;

public class AnglicakMinigameTarget extends Image {

	private Vector2 actualPosition;
	private float boundingRadius;

	public AnglicakMinigameTarget(int x, int y, float targetRadius) {
		super(Autickax.getInstance().assets
				.getGraphics(Constants.minigames.ANGLICAK_MINIGAME_TARGET_TEXTURE));
		this.setWidth(targetRadius * 2 * Input.xStretchFactorInv);
		this.setHeight(targetRadius * 2 * Input.yStretchFactorInv);
		this.setPosition(x * Input.xStretchFactorInv - this.getWidth() / 2, y
				* Input.yStretchFactorInv - this.getHeight() / 2);
		actualPosition = new Vector2(x, y);		
		this.boundingRadius = targetRadius;
	}

	/**
	 * Counts how far is position from target. 
	 * @param position
	 * @return values between 0 and 1 if target was hit. Less then 0 if the target wasnt hit.
	 */
	public float distanceInPerc(Vector2 position) {				
		return 1 - actualPosition.dst(position) / this.boundingRadius;
	}
}
