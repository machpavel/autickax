package cz.mff.cuni.autickax.drawing;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.utils.TiledDrawable;

import cz.mff.cuni.autickax.Autickax;

public class LevelTextureBackground extends LevelBackground implements Externalizable {

	private String textureName;
	private TextureRegion texture;
	private TiledDrawable tile;

	/** Default constructor for the externalization */
	public LevelTextureBackground() {
	}
	
	/**
	 * Creates background from the color components.
	 * @param red Red component, ranging from 0 to 255
	 * @param green Green component, ranging from 0 to 255
	 * @param blue Blue component, ranging from 0 to 255
	 */
	public LevelTextureBackground(String textureName) {
		this.textureName = textureName;
	}

	@Override
	public void draw(SpriteBatch batch, float stageWidth, float stageHeight) {
		if (this.texture == null) {
			this.texture = Autickax.getInstance().assets.getGraphics(this.textureName);
			this.tile = new TiledDrawable(this.texture);
		}
		
		tile.draw(batch, 0, 0, stageWidth, stageHeight);
	}

	@Override
	public void readExternal(ObjectInput in) throws IOException,
			ClassNotFoundException {
		this.textureName = in.readUTF();
	}

	@Override
	public void writeExternal(ObjectOutput out) throws IOException {
		out.writeUTF(this.textureName);
	}

}
