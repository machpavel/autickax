package cz.cuni.mff.xcars;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

import cz.cuni.mff.xcars.Assets;
import cz.cuni.mff.xcars.constants.Constants;
import cz.cuni.mff.xcars.input.Input;

public class XcarsEditor extends Game {

	private static XcarsEditor _instance;

	public Assets assets;

	public XcarsEditor() {
		_instance = this;
		assets = new Assets();
	}

	@Override
	public void create() {
		Input.InitDimensionsInEditor();
		setScreen(new LoadingScreenEditor());
	}

	public static XcarsEditor getInstance() {
		return _instance; // will get created when app starts
	}

	public static void main(String[] args) {
		// Runtime packing - expects assetSrc folder in a parent folder of this
		// project
		// TexturePacker2.process("../assetSrc/",
		// "../SampleGDXgame-android/assets/img/packed", "graphics");

		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "XCars-Editor";
		cfg.width = (int) (Constants.WORLD_WIDTH + EditorConstants.CONTROL_PANEL_WIDTH);
		cfg.height = (int) Constants.WORLD_HEIGHT;
		cfg.resizable = false;

		new LwjglApplication(new XcarsEditor(), cfg);
	}
}
