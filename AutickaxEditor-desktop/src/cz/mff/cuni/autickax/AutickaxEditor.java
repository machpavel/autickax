package cz.mff.cuni.autickax;

import com.badlogic.gdx.Game;

import cz.mff.cuni.autickax.input.Input;

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
}
