package cz.cuni.mff.xcars.drawing;

import java.io.Externalizable;
import java.io.IOException;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.XmlWriter;
import com.badlogic.gdx.utils.XmlReader.Element;

public abstract class LevelBackground implements Externalizable {

	public static LevelBackground parseLevelBackground(Element levelBackgroundRoot)
			throws IOException {
		Element levelBackground = levelBackgroundRoot.getChild(0);

		String backgroundType = levelBackground.getName();
		if (backgroundType.equals(LevelConstantBackground.xmlName)) {
			return LevelConstantBackground.parseLevelBackground(levelBackground);
		} else if (backgroundType.equals(LevelTextureBackground.xmlName)) {
			return LevelTextureBackground.parseLevelBackground(levelBackground);
		} else {
			throw new IOException("Unknown type of background: " + backgroundType);
		}
	}

	public abstract void toXml(XmlWriter writer) throws IOException;

	public abstract void draw(SpriteBatch batch, float stageWidth, float stageHeight);
}
