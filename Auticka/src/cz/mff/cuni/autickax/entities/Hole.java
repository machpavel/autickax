package cz.mff.cuni.autickax.entities;

import java.io.Externalizable;

import cz.mff.cuni.autickax.constants.Constants;
import cz.mff.cuni.autickax.gamelogic.SubLevel;
import cz.mff.cuni.autickax.miniGames.AnglicakMinigame;
import cz.mff.cuni.autickax.miniGames.AvoidObstaclesMinigame;
import cz.mff.cuni.autickax.miniGames.AvoidObstaclesMinigame.ObstaclesType;
import cz.mff.cuni.autickax.miniGames.Minigame;
import cz.mff.cuni.autickax.scene.GameScreen;

public final class Hole extends GameObject implements Externalizable {
	public static final String name = Constants.gameObjects.HOLE_NAME;

	public Hole(float x, float y, int type) {
		super(x, y, type);
	}

	public Hole(GameObject object) {
		super(object);
	}

	/** Parameterless constructor for the externalization */
	public Hole() {
	}

	@Override
	public String getName() {
		return name;
	}

	/** Gets the texture name according to a type */
	public static String GetTextureName(int type) {
		return Constants.gameObjects.HOLE_NAME + type;
	}

	@Override
	public GameObject copy() {
		return new Hole(this);
	}

	@Override
	public void setTexture(int type) {
		super.setTexture(Hole.GetTextureName(type));
	}

	@Override
	public Minigame getMinigame(GameScreen gameScreen, SubLevel parent) {
		return new AnglicakMinigame(gameScreen, parent);
	}

	@Override
	public String getSoundName() {
		return Constants.sounds.SOUND_HOLE;
	}

}
