package cz.mff.cuni.autickax;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

import cz.mff.cuni.autickax.constants.Constants;

public class Main {
	public static void main(String[] args) {
	// Runtime packing - expects assetSrc folder in a parent folder of this project
	//	TexturePacker2.process("../assetSrc/", "../SampleGDXgame-android/assets/img/packed", "graphics");
		
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "AutickaX";
		cfg.useGL20 = false;
		cfg.width = (int)(Constants.WORLD_WIDTH + EditorConstants.CONTROL_PANEL_WIDTH);
		cfg.height = (int)Constants.WORLD_HEIGHT;
		
		
		new LwjglApplication(new AutickaxEditor(), cfg);
	}
}
