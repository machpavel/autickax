package cz.mff.cuni.autickax.entities;

import java.io.IOException;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import cz.mff.cuni.autickax.constants.Constants;
import cz.mff.cuni.autickax.gamelogic.SubLevel;
import cz.mff.cuni.autickax.input.Input;
import cz.mff.cuni.autickax.miniGames.Minigame;

import com.badlogic.gdx.utils.XmlReader.Element;
import com.badlogic.gdx.utils.XmlWriter;

import cz.mff.cuni.autickax.scene.GameScreen;

public final class Car extends ShiftableGameObject {
	
	private boolean isDragged = false;
	private TextureRegion[] positionTextures;	
	private Vector2 lastCarPosition;
	private float lastRotationDistance = 0;
	
	/** Parameterless constructor for the externalization */
	public Car() {
	}

	public Car(float x, float y, int type) {
		super(x, y, type);
		this.lastCarPosition = new Vector2(x, y);		
	}
	
	public static Car parseCar(Element car) {
		return new Car(car.getFloat("X"), car.getFloat("Y"), car.getInt("type", 1));
	}
	

	public void reset(){
		super.reset();
		this.lastCarPosition = new Vector2(0,0);
		this.lastRotationDistance = 0;
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
	public void move(Vector2 newPos) {
		super.move(newPos); 
				
		lastRotationDistance += newPos.dst(lastCarPosition);
		if(lastRotationDistance > Constants.gameObjects.CAR_MINIMAL_DISTANCE_TO_SHOW_ROTATION){	
			lastRotationDistance = 0;
			this.setRotation(new Vector2(this.lastCarPosition).sub(newPos).scl(-1).angle());			
			this.lastCarPosition = newPos;
		}
	}
	
	public void setNextPositionIsDirection(){
		this.lastRotationDistance  = Constants.gameObjects.CAR_MINIMAL_DISTANCE_TO_SHOW_ROTATION;
	}
	
	@Override
	public void update(float delta) {		
		if (this.isDragged()) {
			if (Gdx.input.isTouched()) {
				Vector2 touchPos = new Vector2(Input.getX(), Input.getY());
				this.move(touchPos);
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
	public void draw(SpriteBatch batch) {				                
		if (this.rotation >= 22.5 && this.rotation < 67.5)
			this.setTexture(positionTextures[1]);
		else if (this.rotation >= 67.5 && this.rotation < 112.5)
			this.setTexture(positionTextures[2]);
		else if (this.rotation >= 112.5 && this.rotation < 157.5)
			this.setTexture(positionTextures[3]);
		else if (this.rotation >= 157.5 && this.rotation < 202.5)
			this.setTexture(positionTextures[4]);
		else if (this.rotation >= 202.5 && this.rotation < 247.5)
			this.setTexture(positionTextures[5]);
		else if (this.rotation >= 247.5 && this.rotation < 292.5)
			this.setTexture(positionTextures[6]);
		else if (this.rotation >= 292.5 && this.rotation < 337.5)
			this.setTexture(positionTextures[7]);
		else if ((this.rotation >= 337.5 && this.rotation < 360)
				|| (this.rotation >= 0 && this.rotation < 22.5))
			this.setTexture(positionTextures[0]);
		else {
			// TODO: throw Exception
		}
		
		//Color c = batch.getColor();
        //batch.setColor(c.r, c.g, c.b, 1f);
		super.draw(batch);		 		
        //batch.setColor(c.r, c.g, c.b, 1f);
	}
	
	@Override
	public GameObject copy() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setTexture(int type) {
		if(this.positionTextures == null)
			this.positionTextures = new TextureRegion[8];
		switch (type) {
		case 1:
			this.positionTextures[0] = this.gameScreen.getGame().assets
					.getGraphics(Constants.gameObjects.CAR_TYPE_1_POSITION_0_TEXTURE_NAME);
			this.positionTextures[1] = this.gameScreen.getGame().assets
					.getGraphics(Constants.gameObjects.CAR_TYPE_1_POSITION_1_TEXTURE_NAME);
			this.positionTextures[2] = this.gameScreen.getGame().assets
					.getGraphics(Constants.gameObjects.CAR_TYPE_1_POSITION_2_TEXTURE_NAME);
			this.positionTextures[3] = this.gameScreen.getGame().assets
					.getGraphics(Constants.gameObjects.CAR_TYPE_1_POSITION_3_TEXTURE_NAME);
			this.positionTextures[4] = this.gameScreen.getGame().assets
					.getGraphics(Constants.gameObjects.CAR_TYPE_1_POSITION_4_TEXTURE_NAME);
			this.positionTextures[5] = this.gameScreen.getGame().assets
					.getGraphics(Constants.gameObjects.CAR_TYPE_1_POSITION_5_TEXTURE_NAME);
			this.positionTextures[6] = this.gameScreen.getGame().assets
					.getGraphics(Constants.gameObjects.CAR_TYPE_1_POSITION_6_TEXTURE_NAME);
			this.positionTextures[7] = this.gameScreen.getGame().assets
					.getGraphics(Constants.gameObjects.CAR_TYPE_1_POSITION_7_TEXTURE_NAME);
			super.texture = this.positionTextures[0];			
			super.setMeasurements(texture.getRegionWidth(), texture.getRegionHeight());
			break;
		default:
			break;
		}
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
}
