package cz.mff.cuni.autickax.entities;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.XmlReader.Element;

import cz.mff.cuni.autickax.constants.Constants;
import cz.mff.cuni.autickax.input.Input;

public class Start extends GameObject implements Externalizable {
	public static final String name = Constants.gameObjects.START_NAME;
	Vector2 visualShift = new Vector2(0, 0);

	public Start(float x, float y, int type) {
		super(x, y, type);

	}

	/** Parameterless constructor for the externalization */
	public Start() {
	}

	public Start(GameObject object) {
		super(object);
	}

	public static Start parseStart(Element start) {
		return new Start(start.getFloat("X"), start.getFloat("Y"), start.getInt("type", 1));
	}

	public void setShift(Vector2 shift) {
		this.visualShift = shift;
	}

	public Vector2 getShift() {
		return this.visualShift;
	}

	@Override
	public void draw(SpriteBatch batch) {
		batch.draw(this.getTexture(), ((this.position.x - this.getWidth() / 2) + visualShift.x)
				* Input.xStretchFactorInv,
				((this.position.y - this.getHeight() / 2) + visualShift.y)
						* Input.yStretchFactorInv, (this.getWidth() / 2) * Input.xStretchFactorInv,
				(this.getHeight() / 2) * Input.yStretchFactorInv, this.getWidth()
						* Input.xStretchFactorInv, this.getHeight() * Input.yStretchFactorInv,
				scale.x, scale.y, this.rotation);
	}

	@Override
	public String getName() {
		return name;
	}

	/** Gets the texture name according to a type */
	public static String GetTextureName(int type) {
		return Constants.gameObjects.START_NAME + type;
	}

	@Override
	public void setTexture(int type) {
		super.setTexture(Start.GetTextureName(type));
	}

	@Override
	public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
		super.readExternal(in);

		this.visualShift = (Vector2) in.readObject();
	}

	@Override
	public void writeExternal(ObjectOutput out) throws IOException {
		super.writeExternal(out);

		out.writeObject(this.visualShift);
	}

	@Override
	public GameObject copy() {
		return new Start(this);
	}
}
