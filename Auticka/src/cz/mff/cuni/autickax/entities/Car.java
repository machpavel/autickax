package cz.mff.cuni.autickax.entities;

import java.io.IOException;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import cz.mff.cuni.autickax.Constants;
import cz.mff.cuni.autickax.input.Input;

import com.badlogic.gdx.utils.XmlWriter;

import cz.mff.cuni.autickax.scene.GameScreen;

public final class Car extends GameObject {

	private boolean isDragged = false;
	private TextureRegion[] positionTextures = new TextureRegion[8];

	public Car(float x, float y, GameScreen gameScreen, int type) {
		super(x, y, gameScreen);
		// TODO: This condition is temporary hack due to loading levels in
		// AssetsProcessor. REWRITE!
		if (this.game != null) {
			switch (type) {
			case 0:
				super.setMeasurements(Constants.CAR_TYPE_0_WIDTH,
						Constants.CAR_TYPE_0_HEIGHT);
				this.positionTextures[0] = this.game.assets
						.getGraphics(Constants.CAR_TYPE_0_POSITION_0_TEXTURE_NAME);
				this.positionTextures[1] = this.game.assets
						.getGraphics(Constants.CAR_TYPE_0_POSITION_1_TEXTURE_NAME);
				this.positionTextures[2] = this.game.assets
						.getGraphics(Constants.CAR_TYPE_0_POSITION_2_TEXTURE_NAME);
				this.positionTextures[3] = this.game.assets
						.getGraphics(Constants.CAR_TYPE_0_POSITION_3_TEXTURE_NAME);
				this.positionTextures[4] = this.game.assets
						.getGraphics(Constants.CAR_TYPE_0_POSITION_4_TEXTURE_NAME);
				this.positionTextures[5] = this.game.assets
						.getGraphics(Constants.CAR_TYPE_0_POSITION_5_TEXTURE_NAME);
				this.positionTextures[6] = this.game.assets
						.getGraphics(Constants.CAR_TYPE_0_POSITION_6_TEXTURE_NAME);
				this.positionTextures[7] = this.game.assets
						.getGraphics(Constants.CAR_TYPE_0_POSITION_7_TEXTURE_NAME);
				super.texture = this.positionTextures[0];
				break;
			default:
				break;
			}
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
//		if (this.rotation >= 22.5 && this.rotation < 67.5)
//			this.texture = positionTextures[1];
//		else if (this.rotation >= 67.5 && this.rotation < 112.5)
//			this.texture = positionTextures[2];
//		else if (this.rotation >= 112.5 && this.rotation < 157.5)
//			this.texture = positionTextures[3];
//		else if (this.rotation >= 157.5 && this.rotation < 202.5)
//			this.texture = positionTextures[4];
//		else if (this.rotation >= 202.5 && this.rotation < 247.5)
//			this.texture = positionTextures[5];
//		else if (this.rotation >= 247.5 && this.rotation < 292.5)
//			this.texture = positionTextures[6];
//		else if (this.rotation >= 292.5 && this.rotation < 337.5)
//			this.texture = positionTextures[7];
//		else if ((this.rotation >= 337.5 && this.rotation < 360)
//				|| (this.rotation >= 0 && this.rotation < 22.5))
//			this.texture = positionTextures[0];
//		else {
//			// TODO: throw Exception
		
		
		if (this.rotation >= 45 && this.rotation < 135)			
			this.texture = positionTextures[2];
		else if (this.rotation >= 135 && this.rotation < 225)
			this.texture = positionTextures[4];
		else if (this.rotation >= 225 && this.rotation < 315)			
			this.texture = positionTextures[6];
		else if ((this.rotation >= 315 && this.rotation < 360)
				|| (this.rotation >= 0 && this.rotation < 45))
			this.texture = positionTextures[0];
		else {
			// TODO: throw Exception
		}
		
		super.draw(batch);
	}
}
