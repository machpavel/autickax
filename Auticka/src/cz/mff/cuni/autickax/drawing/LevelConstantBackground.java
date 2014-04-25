package cz.mff.cuni.autickax.drawing;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class LevelConstantBackground extends LevelBackground implements Externalizable {

	private final Color backgroundColor;

	/** Default constructor for the externalization */
	public LevelConstantBackground() {
		this.backgroundColor = new Color();
	}
	
	/**
	 * Creates background from the color components.
	 * @param red Red component, ranging from 0 to 255
	 * @param green Green component, ranging from 0 to 255
	 * @param blue Blue component, ranging from 0 to 255
	 */
	public LevelConstantBackground(int red, int green, int blue) {
		Color backgroundColor = new Color((float)red / 255, (float)green / 255, (float)blue / 255, 0);

		this.backgroundColor = backgroundColor;
	}

	@Override
	public void draw(SpriteBatch batch, float stageWidth, float stageHeight) {
		Gdx.gl.glClearColor (
			this.backgroundColor.r,
			this.backgroundColor.g,
			this.backgroundColor.b,
			1
		);
	}

	@Override
	public void readExternal(ObjectInput in) throws IOException,
			ClassNotFoundException {
		this.backgroundColor.r = in.readFloat();
		this.backgroundColor.g = in.readFloat();
		this.backgroundColor.b = in.readFloat();
		this.backgroundColor.a = 1;
		
	}

	@Override
	public void writeExternal(ObjectOutput out) throws IOException {
		out.writeFloat(this.backgroundColor.r);
		out.writeFloat(this.backgroundColor.g);
		out.writeFloat(this.backgroundColor.b);
	}

}
