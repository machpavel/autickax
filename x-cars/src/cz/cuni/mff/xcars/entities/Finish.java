package cz.cuni.mff.xcars.entities;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.XmlReader.Element;

import cz.cuni.mff.xcars.constants.Constants;

public class Finish extends GameObject implements Externalizable {
	public static final String name = Constants.gameObjects.FINISH_NAME;
	Vector2 visualShift = new Vector2(0, 0);

	public Finish(float x, float y, int type) {
		super(x, y, type);
		this.boundingCircleRadius = Constants.gameObjects.FINISH_BOUNDING_RADIUS;
	}

	/** Parameterless constructor for the externalization */
	public Finish() {
	}

	public Finish(GameObject object) {
		super(object);
	}

	public static Finish parseFinish(Element finish) {
		return new Finish(finish.getFloat("X"), finish.getFloat("Y"),
				finish.getInt("type", 1));
	}

	public void setShift(Vector2 shift) {
		this.visualShift = shift;
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		batch.draw(
				this.getTexture(),
				((this.getPosition().x - this.getWidth() / 2) + visualShift.x),
				((this.getPosition().y - this.getHeight() / 2) + visualShift.y),
				(this.getWidth() / 2), (this.getHeight() / 2), this.getWidth(),
				this.getHeight(), this.getScaleX(), this.getScaleY(),
				this.getRotation());
	}

	@Override
	public String getName() {
		return name;
	}

	/** Gets the texture name according to a type */
	public static String GetTextureName(int type) {
		return Constants.gameObjects.GAME_OBJECTS_TEXTURE_PREFIX + name + '/'
				+ name + type;
	}

	@Override
	public GameObject copy() {
		return new Finish(this);
	}

	@Override
	public void setTexture(int type) {
		super.setTexture(Finish.GetTextureName(type));
	}

	public float getBoundingRadius() {
		return this.boundingCircleRadius;
	}

	@Override
	public void readExternal(ObjectInput in) throws IOException,
			ClassNotFoundException {
		super.readExternal(in);

		this.visualShift = (Vector2) in.readObject();
	}

	@Override
	public void writeExternal(ObjectOutput out) throws IOException {
		super.writeExternal(out);

		out.writeObject(this.visualShift);
	}
}
