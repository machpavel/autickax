package cz.cuni.mff.xcars.entities;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.XmlWriter;
import com.badlogic.gdx.utils.XmlReader.Element;

import cz.cuni.mff.xcars.debug.Debug;

public abstract class VisuallyShiftableGameObject extends GameObject {
	Vector2 visualShift = new Vector2();

	public VisuallyShiftableGameObject(float x, float y, int type) {
		super(x, y, type);
	}

	/** Parameterless constructor for the externalization */
	public VisuallyShiftableGameObject() {
		super();
	}

	public VisuallyShiftableGameObject(GameObject object) {
		super(object);
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
	void aditionalsToXml(XmlWriter writer) throws IOException {
		if (this.visualShift.x != 0)
			writer.attribute("visualShiftX", this.visualShift.x);
		if (this.visualShift.y != 0)
			writer.attribute("visualShiftY", this.visualShift.y);
	}

	@Override
	void parseAditionals(Element gameObject) throws IOException {
		float visualShiftX = gameObject.getFloat("visualShiftX", 0);
		float visualShiftY = gameObject.getFloat("visualShiftY", 0);
		this.setVisualShift(new Vector2(visualShiftX, visualShiftY));
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		batch.draw(this.getTexture(), (this.getLeftBottomCorner().x + visualShift.x),
				(this.getLeftBottomCorner().y + visualShift.y), (this.getWidth() / 2), (this.getHeight() / 2),
				this.getWidth(), this.getHeight(), this.getScaleX(), this.getScaleY(), this.getRotation());

		if (Debug.DEBUG) {
			if (Debug.drawBoundingBoxes) {
				batch.end();
				Debug.drawCircle(this.getPosition(), boundingCircleRadius, new Color(0, 0, 1, 1), 2);
				batch.begin();
			}
		}
	}

	public void setVisualShift(Vector2 shift) {
		this.visualShift = new Vector2(shift);
	}

	public Vector2 getVisualShift() {
		return this.visualShift;
	}

}
