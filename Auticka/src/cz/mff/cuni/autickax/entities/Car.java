package cz.mff.cuni.autickax.entities;

import java.io.IOException;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;

import cz.mff.cuni.autickax.Constants;
import cz.mff.cuni.autickax.input.Input;

import com.badlogic.gdx.utils.XmlWriter;

import cz.mff.cuni.autickax.scene.GameScreen;

public final class Car extends GameObject {

	private boolean isDragged = false;	
	
	public Car(float x, float y, GameScreen gameScreen, int type) {	
		super(x,y,gameScreen);
		switch (type) {
		case 0:
			super.setTexture(Constants.CAR_TYPE_0_WIDTH, Constants.CAR_TYPE_0_WIDTH, Constants.CAR_TYPE_0_TEXTURE_NAME);					
			break;
		default:
			break;
		}
	}


	public boolean isDragged() {
		return this.isDragged;
	}

	public void setDragged(boolean value) {
		this.isDragged = value;
	}

	@Override
	public String getName() {
		return "car";
	}

	@Override
	public void update(float delta) {
		/*
		 * if (Gdx.input.justTouched()) { Vector3 touchPos = new Vector3();
		 * touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
		 * this.level.unproject(touchPos);
		 * 
		 * this.setDragged(true); }
		 */

		if (this.isDragged()) {
			if (Gdx.input.isTouched()) {
				Vector2 touchPos = new Vector2(Input.getX(), Input.getY());
				this.move(touchPos.x, touchPos.y);
			}
		}

		if (!Gdx.input.isTouched())
			setDragged(false);
	}

	@Override
	void aditionalsToXml(XmlWriter writer) throws IOException {
		// TODO Auto-generated method stub
		
	}

}
