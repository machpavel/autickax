package cz.cuni.mff.autickax.miniGames.support;

import java.io.Externalizable;

import cz.cuni.mff.autickax.constants.Constants;
import cz.cuni.mff.autickax.entities.GameObject;
import cz.cuni.mff.autickax.gamelogic.SubLevel;
import cz.cuni.mff.autickax.miniGames.Minigame;
import cz.cuni.mff.autickax.scene.GameScreen;

public final class AvoidStone extends GameObject implements Externalizable {
	public static String name = Constants.minigames.AVOID_STONE_NAME;

	public AvoidStone(float x, float y, int type) {
		super(x, y, type);
	}

	public AvoidStone(GameObject object) {
		super(object);
	}

	/** Parameterless constructor for the externalization */
	public AvoidStone() {
	}

	@Override
	public String getName() {
		return name;
	}

	/** Gets the texture name according to a type */
	public static String GetTextureName(int type) {
		return Constants.minigames.AVOID_STONE_TEXTURE_PREFIX
				+ Constants.minigames.AVOID_STONE_NAME + type;

	}

	@Override
	public GameObject copy() {
		return new AvoidStone(this);
	}

	@Override
	public void setTexture(int type) {
		super.setTexture(AvoidStone.GetTextureName(type));
	}

	@Override
	public Minigame getMinigame(GameScreen gameScreen, SubLevel parent) {
		return null;
	}

	@Override
	public String getSoundName() {
		return Constants.sounds.SOUND_STONE;
	}

}
