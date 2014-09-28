package cz.cuni.mff.xcars.miniGames.support;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;

import cz.cuni.mff.xcars.Xcars;
import cz.cuni.mff.xcars.constants.Constants;
import cz.cuni.mff.xcars.debug.Debug;
import cz.cuni.mff.xcars.entities.GameObject;
import cz.cuni.mff.xcars.entities.ShiftableGameObject;
import cz.cuni.mff.xcars.input.Input;
import cz.cuni.mff.xcars.miniGames.RepairingMinigame;

public class RepairingMinigameObject extends ShiftableGameObject {
	public static final String name = Constants.minigames.REPAIRING_MINIGAME_OBJECT;

	private RepairingMinigame game;

	private static float targetRadius = Constants.minigames.REPAIRING_MINIGAME_TARGET_RADIUS;

	private Vector2 origin;
	private Vector2 target;
	private boolean stickWithTarget;

	public RepairingMinigameObject(float originX, float originY, float targetX,
			float targetY, String textureName, RepairingMinigame game,
			boolean stickWithTarget) {
		super();
		this.setPosition(originX, originY);
		this.origin = new Vector2(originX, originY);
		this.target = new Vector2(targetX, targetY);
		this.setTexture(textureName);
		this.game = game;
		this.isActive = false;
		this.canBeDragged = true;
		this.stickWithTarget = stickWithTarget;
	}

	public RepairingMinigameObject(float x, float y, int type) {
		super(x, y, type);
		this.origin = new Vector2(x, y);
		this.isActive = false;
		this.canBeDragged = true;
		this.stickWithTarget = false;
	}

	@Override
	public void update(float delta) {
		if (this.canBeDragged) {
			if (this.isDragged) {
				// Is dragged
				if (Gdx.input.isTouched()) {
					Vector2 touchPos = new Vector2(Input.getX(), Input.getY());
					this.move(touchPos);
				}
				// Just released
				else {
					if (isActive && isInTarget()) {
						doInTargetAction();
					} else {
						Xcars.getInstance().assets.soundAndMusicManager
								.playSound(Constants.sounds.SOUND_MINIGAME_REPAIRING_WRONG);
						this.reset();
					}
				}
			}
			// Just touched
			else if (Gdx.input.justTouched()) {
				Vector2 touchPos = new Vector2(Input.getX(), Input.getY());
				Vector2 shift = new Vector2(this.getPosition()).sub(touchPos.x,
						touchPos.y);
				float maxDistance = Constants.misc.SHIFTABLE_OBJECT_MAX_CAPABLE_DISTANCE
						* (Input.xStretchFactorInv + Input.yStretchFactorInv / 2);

				if (shift.len() <= maxDistance) {
					this.setDragged(true);
					this.setShift(shift);
				}
			}

		}
	}

	private boolean isInTarget() {
		return this.getPosition().dst(target) <= targetRadius;
	}

	protected void doInTargetAction() {
		game.switchToNextState();
		if (stickWithTarget)
			this.origin = this.target;
		this.reset();

	}

	public RepairingMinigameObject(GameObject object) {
		super(object);
	}

	@Override
	public void reset() {
		boolean wasActive = this.isActive;
		super.reset();
		this.isActive = wasActive;
		this.move(origin);
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public GameObject copy() {
		return new RepairingMinigameObject(this);
	}

	/** Gets the texture name according to a type */
	public static String GetTextureName(int type) {
		return name + type;
	}

	@Override
	public void setTexture(int type) {
		super.setTexture(RepairingMinigameObject.GetTextureName(type));
	}

	public void DrawMaxTouchableArea() {
		if (this.canBeDragged && !this.isDragged) {
			float maxDistance = Constants.misc.SHIFTABLE_OBJECT_MAX_CAPABLE_DISTANCE
					* (Input.xStretchFactorInv + Input.yStretchFactorInv / 2);
			if (isActive) {
				Debug.drawCircle(this.getPosition(), maxDistance, new Color(1,
						1, 0, 1), 3);
			} else {
				Debug.drawCircle(this.getPosition(), maxDistance, new Color(1,
						1, 0, 1), 1);
			}
		}
	}
}
