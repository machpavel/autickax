package cz.mff.cuni.autickax.entities;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.XmlWriter;

import cz.mff.cuni.autickax.constants.Constants;
import cz.mff.cuni.autickax.gamelogic.SubLevel;
import cz.mff.cuni.autickax.input.Input;
import cz.mff.cuni.autickax.miniGames.Minigame;
import cz.mff.cuni.autickax.scene.GameScreen;

public class Start extends GameObject implements Externalizable {
	Vector2 shift = new Vector2(0, 0);
	
	public Start(float x, float y, int type) {	
		super(x, y, type);
		
	}
	
	/** Parameterless constructor for the externalization */
	public Start() {
	}
	
	public void setShift(Vector2 shift){
		this.shift = shift;
	}
	public Vector2 getShift(){
		return this.shift;
	}
	
	@Override
	public void draw(SpriteBatch batch) {
		batch.draw(this.getTexture(), ((this.position.x - this.getWidth() / 2) + shift.x )
				* Input.xStretchFactorInv, ((this.position.y - this.getHeight() / 2) + shift.y)
				* Input.yStretchFactorInv, (this.getWidth() / 2)
				* Input.xStretchFactorInv, (this.getHeight() / 2)
				* Input.yStretchFactorInv,
				this.getWidth() * Input.xStretchFactorInv, this.getHeight()
						* Input.yStretchFactorInv, scale.x, scale.y,
				this.rotation);
	}
	
	@Override
	public void update(float delta) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "start";
	}

	@Override
	void aditionalsToXml(XmlWriter writer) throws IOException {
		// TODO Auto-generated method stub		
	}
	
	/** Gets the texture name according to a type*/
	public static  String GetTextureName(int type){
		return Constants.gameObjects.START_TEXTURE_PREFIX + type;
	}
	
	@Override
	public GameObject copy() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setTexture(int type) {
		super.setTexture(Start.GetTextureName(type));		
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
	public void readExternal(ObjectInput in) throws IOException,
			ClassNotFoundException {
		super.readExternal(in);
		
		this.shift = (Vector2) in.readObject();
	}

	@Override
	public void writeExternal(ObjectOutput out) throws IOException {
		super.writeExternal(out);
		
		out.writeObject(this.shift);
	}
}
