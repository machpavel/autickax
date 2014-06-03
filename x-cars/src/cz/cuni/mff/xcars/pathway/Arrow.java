package cz.cuni.mff.xcars.pathway;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.XmlReader.Element;
import com.badlogic.gdx.utils.XmlWriter;

import cz.cuni.mff.xcars.Xcars;
import cz.cuni.mff.xcars.constants.Constants;
import cz.cuni.mff.xcars.screenObjects.ScreenAdaptiveImage;

public class Arrow extends ScreenAdaptiveImage implements Externalizable {
	private static final String name = Constants.misc.ARROW_NAME;
	int type;
	float length = -1;

	public Arrow() {
		this(1);
	}

	public Arrow(int type) {
		super();
		this.type = type;
	}

	public Arrow(float x, float y, float rotation, float length, int type) {
		this(type);
		this.setX(x);
		this.setY(y);
		this.setRotation(rotation);
		this.setLength(length);
	}

	public void toXml(XmlWriter writer) throws IOException {
		writer.element(name);
		writer.attribute("X", this.getX());
		writer.attribute("Y", this.getY());
		writer.attribute("rotation", this.getRotation());
		writer.attribute("length", this.length);
		writer.attribute("type", this.type);
		writer.pop();
	}

	public static Arrow parseArrow(Element arrow) {
		return new Arrow(arrow.getFloat("X"), arrow.getFloat("Y"), arrow.getFloat("rotation"),
				arrow.getFloat("length"), arrow.getInt("type"));
	}

	public void setLength(float length) {
		this.length = length;
		this.setWidth(length);
	}

	public float getLength() {
		return this.length;
	}

	public void setTexture() {
		// TODO figure out how rotate NinePatches
		
		// NinePatch patch = Xcars.getInstance().assets.getNinePatch(name +
		// type);
		// this.setDrawable(new NinePatchDrawable(patch));
		this.setDrawable(new TextureRegionDrawable(Xcars.getInstance().assets.getGraphics(name
				+ type)));

		this.setHeight(this.getDrawable().getMinHeight());
		if (this.length <= 0)
			this.length = this.getDrawable().getMinWidth();
		this.setWidth(length);

	}

	@Override
	public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
		this.setPosition(in.readFloat(), in.readFloat());
		this.setRotation(in.readFloat());
		this.setLength(in.readFloat());
		this.type = in.readInt();
	}

	@Override
	public void writeExternal(ObjectOutput out) throws IOException {
		out.writeFloat(this.getX());
		out.writeFloat(this.getY());
		out.writeFloat(this.getRotation());
		out.writeFloat(this.length);
		out.writeInt(this.type);
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		this.setOriginX(this.getWidth() / 2);
		this.setOriginY(this.getHeight() / 2);
		super.draw(batch, parentAlpha);
	}

}
