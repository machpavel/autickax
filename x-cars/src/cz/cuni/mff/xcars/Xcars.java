package cz.cuni.mff.xcars;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

import cz.cuni.mff.xcars.constants.Constants;
import cz.cuni.mff.xcars.input.Input;
import cz.cuni.mff.xcars.scene.DifficultySelectScreen;
import cz.cuni.mff.xcars.scene.GameScreen;
import cz.cuni.mff.xcars.scene.LevelLoadingScreen;
import cz.cuni.mff.xcars.scene.LevelSelectScreen;
import cz.cuni.mff.xcars.scene.LoadingScreen;
import cz.cuni.mff.xcars.scene.MainMenuScreen;

public class Xcars extends Game {

	private static Xcars _instance;
	
	public static LoadingScreen loadingScreen;
	public static MainMenuScreen mainMenuScreen;
	public static DifficultySelectScreen difficultySelectScreen;
	public static LevelSelectScreen levelSelectScreen;
	public static LevelLoadingScreen levelLoadingScreen;
	public static GameScreen gameScreen;
	public static Settings settings;
	public static PlayedLevels playedLevels;
	
	public final Assets assets;

	public Xcars() {
		_instance = this;
		assets = new Assets();
	}
	

	@Override
	public void create() {
		Input.InitDimensions();
		
		Xcars.loadingScreen = new LoadingScreen();
		
		settings = new Settings();
		settings.loadSettings();
		
		playedLevels = new PlayedLevels();
		playedLevels.loadLevels();
		
		setScreen(Xcars.loadingScreen);
	}

	public static Xcars getInstance() {
		return _instance; // will get created when app starts
	}
	
	
	@Override
	public void dispose() {
		assets.disposeGameScreenGraphic();
		
		
		// Deletes the temporary pathway texture file
		FileHandle textureFile = null;
    	if(Gdx.files.isLocalStorageAvailable())
    		textureFile = Gdx.files.local(Constants.misc.TEMPORARY_PATHWAY_TEXTURE_STORAGE_NAME + ".cim");
    	else
    		textureFile = Gdx.files.internal(Constants.misc.TEMPORARY_PATHWAY_TEXTURE_STORAGE_NAME + ".cim");    	
		if(textureFile.exists())
			textureFile.delete();
		super.dispose();
	}
}
