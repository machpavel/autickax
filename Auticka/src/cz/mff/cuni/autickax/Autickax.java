package cz.mff.cuni.autickax;

import com.badlogic.gdx.Game;
import cz.mff.cuni.autickax.scene.GameScreen;
import cz.mff.cuni.autickax.scene.TitleScreen;
import cz.mff.cuni.autickax.input.Input;
import cz.mff.cuni.autickax.scene.LoadingScreen;

public class Autickax extends Game {

	private static Autickax _instance;
	
	public static LoadingScreen loadingScreen;
	public static TitleScreen titleScreen;
	public static GameScreen gameScreen;

	public Assets assets;

	public Autickax() {
		_instance = this;
		assets = new Assets();
	}

	@Override
	public void create() {		
		Autickax.loadingScreen = new LoadingScreen();
		
		setScreen(Autickax.loadingScreen);
		Input.InitDimensions();
	}

	public static Autickax getInstance() {
		return _instance; // will get created when app starts
	}
}
