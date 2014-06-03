package cz.cuni.mff.xcars.miniGames.support;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Button;

import cz.cuni.mff.xcars.miniGames.SwitchingMinigame;

public class SwitchingMinigameButton extends Button {
	SwitchingMinigame minigame;

	public SwitchingMinigameButton(ButtonStyle style, final SwitchingMinigame minigame) {
		super(style);
		this.minigame = minigame;

		this.addListener(new InputListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				if (!SwitchingMinigameButton.this.isDisabled()) {
					minigame.switchButtons();
				}
				return true;
			}
		});
	}

	public void setCenterPosition(float x, float y) {
		super.setPosition(x - this.getWidth() / 2, y - this.getHeight() / 2);
	}
}
