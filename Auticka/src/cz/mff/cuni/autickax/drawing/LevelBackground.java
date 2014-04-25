package cz.mff.cuni.autickax.drawing;

import java.io.Externalizable;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.XmlReader.Element;

public abstract class LevelBackground implements Externalizable {
	
	public static LevelBackground parseLevelBackground(
			Element levelBackgroundRoot) {
		Element levelBackground = levelBackgroundRoot.getChild(0);

		if (levelBackground.getName().equals("constantBackground")) {
			int red = levelBackground.getInt("red");
			int green = levelBackground.getInt("green");
			int blue = levelBackground.getInt("blue");

			return new LevelConstantBackground(red, green, blue);

		} else {
			String textureName = levelBackground.getAttribute("textureName");
			
			return new LevelTextureBackground(textureName);
		}

	}

	public abstract void draw(SpriteBatch batch, float stageWidth,
			float stageHeight);
}
