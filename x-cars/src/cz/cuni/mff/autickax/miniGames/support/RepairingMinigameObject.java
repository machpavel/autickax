package cz.cuni.mff.autickax.miniGames.support;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;

import cz.cuni.mff.autickax.constants.Constants;
import cz.cuni.mff.autickax.entities.GameObject;
import cz.cuni.mff.autickax.entities.ShiftableGameObject;
import cz.cuni.mff.autickax.input.Input;
import cz.cuni.mff.autickax.miniGames.RepairingMinigame;

public class RepairingMinigameObject extends ShiftableGameObject {
	public static final String name = Constants.minigames.REPAIRING_MINIGAME_OBJECT;
	
	private RepairingMinigame game;
	
	private static float targetRadius = Constants.minigames.REPAIRING_MINIGAME_TARGET_RADIUS;

	private Vector2 origin;
	private Vector2 target;	
	private boolean stickWithTarget;
	

	public RepairingMinigameObject(float originX, float originY, float targetX, float targetY,
			String textureName, RepairingMinigame game, boolean stickWithTarget) {
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
					if (isActive && isInTarget() ) {						
						doInTargetAction();
					} else {
						this.reset();				
					}
				}
			}
			// Just touched
			else if (Gdx.input.justTouched()) {
				Vector2 touchPos = new Vector2(Input.getX(), Input.getY());
				Vector2 shift = new Vector2(this.getPosition()).sub(touchPos.x, touchPos.y);
				if (shift.len() <= Constants.misc.SHIFTABLE_OBJECT_MAX_CAPABLE_DISTANCE) {
					this.setDragged(true);
					this.setShift(shift);
				}
			}
		}
	}

	private boolean isInTarget() {
		return this.getPosition().dst(target) <= targetRadius;
	}

	protected void doInTargetAction(){
		game.switchToNextState();
		if(stickWithTarget)
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

}
