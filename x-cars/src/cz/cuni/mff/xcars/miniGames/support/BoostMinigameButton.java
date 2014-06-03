package cz.cuni.mff.xcars.miniGames.support;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

import cz.cuni.mff.xcars.miniGames.BoostMinigame;

public class BoostMinigameButton extends Button {
	private int index;
	private BoostMinigame minigame;

	public BoostMinigameButton(int index, BoostMinigame minigame, TextureRegion textureRegion) {
		super(new TextureRegionDrawable(textureRegion));

		this.addListener(new InputListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				BoostMinigameButton.this.minigame.buttonPressed(BoostMinigameButton.this.index);
				return true;
			}
		});

		this.index = index;
		this.minigame = minigame;
	}
}
