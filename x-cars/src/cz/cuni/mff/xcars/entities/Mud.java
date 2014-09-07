package cz.cuni.mff.xcars.entities;

import java.io.Externalizable;

import cz.cuni.mff.xcars.constants.Constants;
import cz.cuni.mff.xcars.gamelogic.SubLevel;
import cz.cuni.mff.xcars.miniGames.AnglicakMinigame;
import cz.cuni.mff.xcars.miniGames.Minigame;
import cz.cuni.mff.xcars.scene.GameScreen;

public final class Mud extends GameObject implements Externalizable {
	public static final String name = Constants.gameObjects.MUD_NAME;

	public Mud(float x, float y, int type) {
		super(x, y, type);
	}

	public Mud(GameObject object) {
		super(object);
	}

	/** Parameterless constructor for the externalization */
	public Mud() {
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
		return new Mud(this);
	}

	@Override
	public void setTexture(int type) {
		super.setTexture(Mud.GetTextureName(type));
	}

	@Override
	public Minigame getMinigame(GameScreen gameScreen, SubLevel parent) {
		return new AnglicakMinigame(gameScreen, parent);
	}

	@Override
	public String getSoundName() {
		return Constants.sounds.SOUND_MUD;
	}

}
