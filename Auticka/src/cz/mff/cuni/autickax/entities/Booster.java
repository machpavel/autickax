package cz.mff.cuni.autickax.entities;

import java.io.Externalizable;

import cz.mff.cuni.autickax.constants.Constants;
import cz.mff.cuni.autickax.gamelogic.SubLevel;
import cz.mff.cuni.autickax.miniGames.BoostMinigame;
import cz.mff.cuni.autickax.miniGames.Minigame;
import cz.mff.cuni.autickax.scene.GameScreen;

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
		this.rotation += delta * 100;
		this.scale.add(adder * delta, adder * delta);
		if (this.scale.x > 1.1f) {
			this.scale.x = 1.1f;
			this.scale.y = 1.1f;
			adder *= -1;
		} else if (this.scale.x < 0.9f) {
			this.scale.x = 0.9f;
			this.scale.y = 0.9f;
			adder *= -1;
		}
	}

	@Override
	public String getName() {
		return name;
	}

	/** Gets the texture name according to a type */
	public static String GetTextureName(int type) {
		return Constants.gameObjects.BOOSTER_NAME + type;
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
