package cz.mff.cuni.autickax.drawing;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.utils.TiledDrawable;
import com.badlogic.gdx.utils.XmlWriter;
import com.badlogic.gdx.utils.XmlReader.Element;

import cz.mff.cuni.autickax.Autickax;
import cz.mff.cuni.autickax.constants.Constants;

public class LevelTextureBackground extends LevelBackground implements Externalizable {
	public static final String xmlName = "textureBackground";
	private String textureName;
	private TextureRegion texture;
	private TiledDrawable tile;

	/** Default constructor for the externalization */
	public LevelTextureBackground() {
	}

	public LevelTextureBackground(String textureName) {
		this.textureName = Constants.levelBackgroundsDirectory + textureName;
	}
	
	public LevelTextureBackground(String textureName, TextureRegion textureRegion){
		this(textureName);
		this.texture = textureRegion;
		this.tile = new TiledDrawable(this.texture);
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
	public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
		this.textureName = in.readUTF();
	}

	@Override
	public void writeExternal(ObjectOutput out) throws IOException {
		out.writeUTF(this.textureName);
	}

	@Override
	public void toXml(XmlWriter writer) throws IOException {
		writer.element(xmlName);
		writer.attribute("textureName", textureName.toString());
		writer.pop();
	}
	
	public static LevelBackground parseLevelBackground(Element levelBackground) {
		String textureName = levelBackground.getAttribute("textureName");
		return new LevelTextureBackground(textureName);
	}

}
