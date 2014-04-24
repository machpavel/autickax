package cz.mff.cuni.autickax.miniGames.support;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import cz.mff.cuni.autickax.input.Input;
import cz.mff.cuni.autickax.miniGames.SwitchingMinigame;

public class SwitchingMinigameButton extends Button {
	SwitchingMinigame minigame;
	public SwitchingMinigameButton(ButtonStyle style, final SwitchingMinigame minigame) {		
		super(style);
		this.minigame = minigame;
		
		this.addListener(new InputListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int button) {
				if(!SwitchingMinigameButton.this.isDisabled()){
					minigame.switchButtons();
				}				
				return true;
			}			
		});					
	}

	@Override
	public float getPrefHeight() {
		return super.getPrefHeight() * Input.yStretchFactorInv;
	}

	@Override
	public float getPrefWidth() {
		return super.getPrefWidth() * Input.xStretchFactorInv;
	}

}