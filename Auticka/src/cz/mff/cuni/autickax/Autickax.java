package cz.mff.cuni.autickax;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

import cz.mff.cuni.autickax.scene.DifficultySelectScreen;
import cz.mff.cuni.autickax.scene.GameScreen;
import cz.mff.cuni.autickax.scene.LevelLoadingScreen;
import cz.mff.cuni.autickax.scene.LevelSelectScreen;
import cz.mff.cuni.autickax.scene.MainMenuScreen;
import cz.mff.cuni.autickax.constants.Constants;
import cz.mff.cuni.autickax.input.Input;
import cz.mff.cuni.autickax.scene.LoadingScreen;

public class Autickax extends Game {

	private static Autickax _instance;
	
	public static LoadingScreen loadingScreen;
	public static MainMenuScreen mainMenuScreen;
	public static DifficultySelectScreen difficultySelectScreen;
	public static LevelSelectScreen levelSelectScreen;
	public static LevelLoadingScreen levelLoadingScreen;
	public static GameScreen gameScreen;
	public static Settings settings;
	public static PlayedLevels playedLevels;
	
	public final Assets assets;

	public Autickax() {
		_instance = this;
		assets = new Assets();
	}
	

	

	@Override
	public void create() {
		Input.InitDimensions();
		
		Autickax.loadingScreen = new LoadingScreen();
		
		settings = new Settings();
		settings.loadSettings();
		
		playedLevels = new PlayedLevels();
		playedLevels.loadLevels();
		
		setScreen(Autickax.loadingScreen);
	}
	
	
	@Override
	public void resume() {
		if(gameScreen != null){
			gameScreen.onApplicationResume();
		}
		super.resume();
	}

	public static Autickax getInstance() {
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

	@Override
	public void pause() {
		if(gameScreen != null){
			gameScreen.onApplicationPause();
		}		
		super.pause();
	}
}
