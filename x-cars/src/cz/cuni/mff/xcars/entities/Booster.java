package cz.cuni.mff.xcars.entities;

import java.io.Externalizable;

import cz.cuni.mff.xcars.constants.Constants;
import cz.cuni.mff.xcars.gamelogic.SubLevel;
import cz.cuni.mff.xcars.miniGames.BoostMinigame;
import cz.cuni.mff.xcars.miniGames.Minigame;
import cz.cuni.mff.xcars.scene.GameScreen;

public final class Booster extends GameObject implements Externalizable {
	public static final String name = Constants.gameObjects.BOOSTER_NAME;

	public Booster(float x, float y, int type) {
		super(x, y, type);
	}

	public Booster(GameObject object) {
		super(object);
	}

	/** Parameterless constructor for the externalization */
	public Booster() {
	}

	private float adder = 0.4f;

	@Override
	public void update(float delta) {
		this.rotateBy(delta * 100);
		//this.setRotation(this.getRotation() + delta * 100);
		this.scaleBy(adder * delta, adder * delta);
		if (this.getScaleX() > 1.1f) {
			this.setScale(1.1f);
			adder *= -1;
		} else if (this.getScaleX() < 0.9f) {
			this.setScale(0.9f);
			adder *= -1;
		}
	}

	@Override
	public String getName() {
		return name;
	}

	/** Gets the texture name according to a type */
	public static String GetTextureName(int type) {
		return Constants.gameObjects.GAME_OBJECTS_TEXTURE_PREFIX
				+ Constants.gameObjects.BOOSTER_NAME + type;
	}

	@Override
	public GameObject copy() {
		return new Booster(this);
	}

	@Override
	public void setTexture(int type) {
		super.setTexture(Booster.GetTextureName(type));
	}

	@Override
	public Minigame getMinigame(GameScreen gameScreen, SubLevel parent) {
		return new BoostMinigame(gameScreen, parent);
	}
}
