package cz.cuni.mff.xcars.entities;

import java.io.Externalizable;

import cz.cuni.mff.xcars.constants.Constants;
import cz.cuni.mff.xcars.gamelogic.SubLevel;
import cz.cuni.mff.xcars.miniGames.Minigame;
import cz.cuni.mff.xcars.miniGames.RaceMinigame;
import cz.cuni.mff.xcars.scene.GameScreen;

public final class RacingCar extends GameObject implements Externalizable {
	public static final String name = Constants.gameObjects.RACING_CAR_NAME;

	public RacingCar(float x, float y, int type) {
		super(x, y, type);
	}

	public RacingCar(GameObject object) {
		super(object);
	}

	/** Parameterless constructor for the externalization */
	public RacingCar() {
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public GameObject copy() {
		return new RacingCar(this);
	}

	/** Gets the texture name according to a type */
	public static String GetStaticTextureName(int type) {
		return Constants.gameObjects.GAME_OBJECTS_TEXTURE_PREFIX + name + '/'
				+ name + type;
	}

	@Override
	public void setTexture(int type) {
		super.setTexture(RacingCar.GetStaticTextureName(type));

	}

	@Override
	public Minigame getMinigame(GameScreen gameScreen, SubLevel parent) {
		return new RaceMinigame(gameScreen, parent);
	}

	@Override
	public String getSoundName() {
		// TODO add custom sounds!
		return Constants.sounds.SOUND_NO_SOUND;
	}
}
