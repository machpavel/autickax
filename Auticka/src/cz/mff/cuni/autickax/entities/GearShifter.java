package cz.mff.cuni.autickax.entities;

import java.io.IOException;
import java.io.Serializable;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;

import cz.mff.cuni.autickax.constants.Constants;
import cz.mff.cuni.autickax.gamelogic.SubLevel;
import cz.mff.cuni.autickax.input.Input;
import cz.mff.cuni.autickax.miniGames.Minigame;

import com.badlogic.gdx.utils.XmlWriter;

import cz.mff.cuni.autickax.scene.GameScreen;

public final class GearShifter extends GameObject implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private boolean isDragged = false;
	private Vector2 shift = Vector2.Zero;

	public GearShifter(float x, float y, GameScreen gameScreen) {
		super(x, y, gameScreen, 0);			
	}
	
	public GearShifter(GameObject object){
		super(object);		
	}

	public boolean isDragged() {
		return this.isDragged;
	}

	public void setDragged(boolean value) {
		this.isDragged = value;
	}
	
	public void setShift(Vector2 value){
		this.shift = value;
	}

	@Override
	public String getName() {
		return "gearShifter";
	}

	@Override
	public void update(float delta) {		
		if (this.isDragged()) {
			if (Gdx.input.isTouched()) {
				Vector2 touchPos = new Vector2(Input.getX(), Input.getY());
				this.move(touchPos.add(shift));
			}
		}

		if (!Gdx.input.isTouched())
			setDragged(false);
	}

	@Override
	void aditionalsToXml(XmlWriter writer) throws IOException {
		// TODO Auto-generated method stub

	}
	
	@Override
	public GameObject copy() {
		return new GearShifter(this);
	}


	@Override
	public Minigame getMinigame(GameScreen gameScreen, SubLevel parent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getSoundName() {
		return Constants.sounds.SOUND_NO_SOUND;
	}
	
	@Override
	public void setTexture(int type) {
		super.setTexture(Constants.minigames.GEAR_SHIFTER_TEXTURE);		
	}
	
	
}
