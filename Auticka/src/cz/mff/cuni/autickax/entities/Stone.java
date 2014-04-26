package cz.mff.cuni.autickax.entities;

import java.io.Externalizable;

import cz.mff.cuni.autickax.constants.Constants;
import cz.mff.cuni.autickax.gamelogic.SubLevel;
import cz.mff.cuni.autickax.miniGames.AvoidObstaclesMinigame;
import cz.mff.cuni.autickax.miniGames.AvoidObstaclesMinigame.ObstaclesType;
import cz.mff.cuni.autickax.miniGames.Minigame;
import cz.mff.cuni.autickax.scene.GameScreen;

public final class Stone extends GameObject implements Externalizable {

	public Stone(float x, float y, int type) {
		super(x, y, type);
	}

	public Stone(GameObject object) {
		super(object);
	}

	/** Parameterless constructor for the externalization */
	public Stone() {
	}

	@Override
	public String getName() {
		return "stone";
	}

	/** Gets the texture name according to a type */
	public static String GetTextureName(int type) {
		return Constants.gameObjects.STONE_TEXTURE_PREFIX + type;
	}

	@Override
	public GameObject copy() {
		return new Stone(this);
	}

	@Override
	public void setTexture(int type) {
		super.setTexture(Stone.GetTextureName(type));
	}

	@Override
	public Minigame getMinigame(GameScreen gameScreen, SubLevel parent) {
		return new AvoidObstaclesMinigame(gameScreen, parent, ObstaclesType.STONES);
	}

	@Override
	public String getSoundName() {
		return Constants.sounds.SOUND_STONE;
	}

}
