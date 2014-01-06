package cz.mff.cuni.autickax;

import com.badlogic.gdx.Game;

import cz.mff.cuni.autickax.scene.LoadingScreen;

public class Autickax extends Game {

	private static Autickax _instance;
	public GameConfiguration cfg;

	public Assets assets;

	public Autickax(GameConfiguration cfg) {
		_instance = this;
		assets = new Assets();
		this.cfg = cfg;
	}

	@Override
	public void create() {
		setScreen(new LoadingScreen());
	}

	public static Autickax getInstance() {
		return _instance; // will get created when app starts
	}
}
