package cz.cuni.mff.xcars.entities;

import java.io.Externalizable;

import cz.cuni.mff.xcars.constants.Constants;
import cz.cuni.mff.xcars.gamelogic.SubLevel;
import cz.cuni.mff.xcars.miniGames.Crash;
import cz.cuni.mff.xcars.miniGames.Minigame;
import cz.cuni.mff.xcars.miniGames.Minigame.ResultType;
import cz.cuni.mff.xcars.scene.GameScreen;

public final class Pneu extends GameObject implements Externalizable {
	public static final String name = Constants.gameObjects.PNEU_NAME;

	public Pneu(float x, float y, int type) {
		super(x, y, type);
	}

	public Pneu(GameObject object) {
		super(object);
	}

	/** Parameterless constructor for the externalization */
	public Pneu() {
	}

	@Override
	public String getName() {
		return name;
	}

	/** Gets the texture name according to a type */
	public static String GetTextureName(int type) {
		return Constants.gameObjects.GAME_OBJECTS_TEXTURE_PREFIX + name + '/'
				+ name + type;
	}

	@Override
	public GameObject copy() {
		return new Pneu(this);
	}

	@Override
	public void setTexture(int type) {
		super.setTexture(Pneu.GetTextureName(type));
	}

	@Override
	public Minigame getMinigame(GameScreen gameScreen, SubLevel parent) {
		return new Crash(
				Constants.strings.TOOLTIP_MINIGAME_CRASHED_PNEU_RESULT_MESSAGE,
				ResultType.FAILED_WITH_VALUE,
				Constants.gameObjects.PNEU_SPEED_REDUCTION, gameScreen, parent);
	}

	@Override
	public String getSoundName() {
		return Constants.sounds.SOUND_NO_SOUND;
	}
}
