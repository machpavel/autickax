package cz.cuni.mff.autickax.entities;

import java.io.Externalizable;

import cz.cuni.mff.autickax.constants.Constants;
import cz.cuni.mff.autickax.gamelogic.SubLevel;
import cz.cuni.mff.autickax.miniGames.AvoidObstaclesMinigame;
import cz.cuni.mff.autickax.miniGames.Minigame;
import cz.cuni.mff.autickax.miniGames.AvoidObstaclesMinigame.ObstaclesType;
import cz.cuni.mff.autickax.scene.GameScreen;

public final class Stone extends GameObject implements Externalizable {
	public static final String name = Constants.gameObjects.STONE_NAME;

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
		return name;
	}

	/** Gets the texture name according to a type */
	public static String GetTextureName(int type) {
		return Constants.gameObjects.GAME_OBJECTS_TEXTURE_PREFIX
				+ Constants.gameObjects.STONE_NAME + type;
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
		return new AvoidObstaclesMinigame(gameScreen, parent,
				ObstaclesType.STONES);
	}

	@Override
	public String getSoundName() {
		return Constants.sounds.SOUND_STONE;
	}

}
