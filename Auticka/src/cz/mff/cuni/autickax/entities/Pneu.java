package cz.mff.cuni.autickax.entities;

import java.io.Externalizable;

import cz.mff.cuni.autickax.constants.Constants;
import cz.mff.cuni.autickax.gamelogic.SubLevel;
import cz.mff.cuni.autickax.miniGames.Crash;
import cz.mff.cuni.autickax.miniGames.Minigame;
import cz.mff.cuni.autickax.miniGames.Minigame.ResultType;
import cz.mff.cuni.autickax.scene.GameScreen;

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
		return Constants.gameObjects.PNEU_NAME + type;
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
		return new Crash(Constants.strings.TOOLTIP_MINIGAME_CRASHED_PNEU_RESULT_MESSAGE,
				ResultType.FAILED_WITH_VALUE, Constants.gameObjects.PNEU_SPEED_REDUCTION,
				gameScreen, parent);
	}

	@Override
	public String getSoundName() {
		return Constants.sounds.SOUND_NO_SOUND;
	}
}
