package cz.mff.cuni.autickax.miniGames.support;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

import cz.mff.cuni.autickax.input.Input;
import cz.mff.cuni.autickax.miniGames.BoostMinigame;

public class BoostMinigameButton extends Button {
	private int index;
	private BoostMinigame minigame;

	public BoostMinigameButton(int index, BoostMinigame minigame, TextureRegion textureRegion ) {		
		super(new TextureRegionDrawable(textureRegion));

		this.addListener(new InputListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int button) {
				BoostMinigameButton.this.minigame.buttonPressed(BoostMinigameButton.this.index);
				return true;
			}			
		});
		
		this.index = index;
		this.minigame = minigame;
		
		
	}
	@Override
	public float getPrefHeight() {
		return super.getPrefHeight() * Input.yStretchFactorInv;
	}
	@Override
	public float getPrefWidth() {
		return super.getPrefWidth() * Input.xStretchFactorInv;
	}
	
	public float getActualWidth() {
		return super.getWidth() * Input.xStretchFactor;
	}
	
	public float getActualHeight() {
		return super.getHeight() * Input.yStretchFactor;
	}
	
	public float getActualX() {
		return super.getX() * Input.xStretchFactor;
	}
	
	public float getActualY() {
		return super.getY() * Input.yStretchFactor;
	}
	
	
	@Override
	public void setPosition(float x, float y) {
		super.setPosition(x * Input.xStretchFactorInv, y * Input.yStretchFactorInv);
	}
	

}
