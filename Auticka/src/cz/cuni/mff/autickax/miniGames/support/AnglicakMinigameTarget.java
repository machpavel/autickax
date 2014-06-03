package cz.cuni.mff.autickax.miniGames.support;

import com.badlogic.gdx.math.Vector2;

import cz.cuni.mff.autickax.Autickax;
import cz.cuni.mff.autickax.constants.Constants;
import cz.cuni.mff.autickax.screenObjects.ScreenAdaptiveImage;

public class AnglicakMinigameTarget extends ScreenAdaptiveImage {

	private Vector2 actualPosition;
	private float boundingRadius;

	public AnglicakMinigameTarget(int x, int y, float targetRadius) {
		super(Autickax.getInstance().assets
				.getGraphics(Constants.minigames.ANGLICAK_MINIGAME_TARGET_TEXTURE));
		this.setWidth(targetRadius * 2);
		this.setHeight(targetRadius * 2);
		this.setCenterPosition(x, y);
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
