package cz.cuni.mff.xcars;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

import cz.cuni.mff.xcars.Assets;
import cz.cuni.mff.xcars.constants.Constants;
import cz.cuni.mff.xcars.input.Input;

public class AutickaxEditor extends Game {

	private static AutickaxEditor _instance;

	public Assets assets;

	public AutickaxEditor() {
		_instance = this;
		assets = new Assets();
	}

	@Override
	public void create() {
		setScreen(new LoadingScreenEditor());
		Input.InitDimensionsInEditor();
	}

	public static AutickaxEditor getInstance() {
		return _instance; // will get created when app starts
	}

	public static void main(String[] args) {
		// Runtime packing - expects assetSrc folder in a parent folder of this
		// project
		// TexturePacker2.process("../assetSrc/",
		// "../SampleGDXgame-android/assets/img/packed", "graphics");

		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "AutickaX";
		cfg.width = (int) (Constants.WORLD_WIDTH + EditorConstants.CONTROL_PANEL_WIDTH);
		cfg.height = (int) Constants.WORLD_HEIGHT;
		cfg.resizable = false;

		new LwjglApplication(new AutickaxEditor(), cfg);
	}
}
