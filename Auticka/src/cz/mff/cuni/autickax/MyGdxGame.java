package cz.mff.cuni.autickax;

import com.badlogic.gdx.Game;

import cz.mff.cuni.autickax.input.Input;
import cz.mff.cuni.autickax.scene.LoadingScreen;

public class MyGdxGame extends Game {
	
	private static MyGdxGame _instance;

	public Assets assets;

	public MyGdxGame() {
		_instance = this;
		assets = new Assets();
	}

	@Override
	public void create() {
		setScreen(new LoadingScreen());
		Input.InitDimensions();
	}

	public static MyGdxGame getInstance() {
		return _instance; // will get created when app starts
	}
}
