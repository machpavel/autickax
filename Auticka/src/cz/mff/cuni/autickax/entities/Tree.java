package cz.mff.cuni.autickax.entities;

import java.io.Externalizable;

import cz.mff.cuni.autickax.constants.Constants;

public final class Tree extends GameTerminatingObject implements Externalizable {

	public Tree(float x, float y, int type) {
		super(x, y, type);
	}

	public Tree(GameObject object) {
		super(object);
	}

	/** Parameterless constructor for the externalization */
	public Tree() {
	}

	@Override
	public String getName() {
		return "tree";
	}

	@Override
	public GameObject copy() {
		return new Tree(this);
	}

	@Override
	public String getSoundName() {
		return Constants.sounds.SOUND_TREE;
	}

	@Override
	protected String getResultMessage() {
		return Constants.strings.TOOLTIP_MINIGAME_CRASHED_TREE_RESULT_MESSAGE;
	}
}
