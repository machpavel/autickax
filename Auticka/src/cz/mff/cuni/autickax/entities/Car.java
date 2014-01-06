package cz.mff.cuni.autickax.entities;

import java.io.IOException;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;

import cz.mff.cuni.autickax.input.Input;
import com.badlogic.gdx.utils.XmlReader.Element;
import com.badlogic.gdx.utils.XmlWriter;

import cz.mff.cuni.autickax.scene.GameScreen;

public final class Car extends GameObject {

	private boolean isDragged = false;
	private GameScreen level;	
	
	public Car( float x, float y, GameScreen level) {
		super(x, y, 100, 67);
		super.texture = super.game.assets.getGraphics("car");
		this.level = level;
	}

	public Car(Element element, GameScreen gameScreen) {
		super(element.getFloat("X"), element.getFloat("Y"), 100, 67, gameScreen, element.getAttribute("textureName"));
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
