package cz.cuni.mff.xcars.myInputListener;

import com.badlogic.gdx.scenes.scene2d.InputEvent;

import cz.cuni.mff.xcars.Difficulty;
import cz.cuni.mff.xcars.EditorScreen;

public class DifficultyChangeListerner extends MyInputListener {
	Difficulty difficulty;

	public DifficultyChangeListerner(EditorScreen screen, int index) {
		super(screen);
		this.difficulty = Difficulty.values()[index];
	}

	public void touchUp(InputEvent event, float x, float y, int pointer,
			int button) {
		screen.difficulty = this.difficulty;
		if (screen.getPathway() != null
				&& screen.getPathway().getDistanceMap() != null)
			screen.setPathwayTexture(screen.getPathway().getDistanceMap()
					.generateTexture(this.difficulty));
		super.touchUp(event, x, y, pointer, button);
	}

}
