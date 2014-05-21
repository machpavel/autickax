package cz.mff.cuni.autickax;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import cz.mff.cuni.autickax.constants.Constants;
import cz.mff.cuni.serialization.AvailableLevelsLoader;

public class Assets {

	private static final String GRAPHICS_DIR = "images";
	private static final String GRAPHICS_FILE = GRAPHICS_DIR + "/images";
	private static final String LOADING_SCREEN_GRAPHICS_FILE = "loadingScreen/images";	
	private static final String MENU_FONT_FILE = "fonts/menu.fnt";
	private static final String DIALOG_FONT_FILE = "fonts/dialog.fnt";
	private static final String FINISH_DIALOG_FONT_FILE = "fonts/finishDialog.fnt";
	private static final String TIME_STRINGS_FONT = "fonts/timeString.fnt";
	private static final String TIME_INT_FONT = "fonts/timeInt.fnt";
	private static final String LEVEL_NUMBER_FONT = "fonts/levels.fnt";
	private static final String AVAILABLE_LEVELS_FILE = "availableLevels.bin";	public AssetManager assetManager;

	private Map<String, TextureRegion> graphicsCacheMap;

	private TextureAtlas atlas;

	public SoundAndMusicManager soundAndMusicManager;

	public Assets() {
		assetManager = new AssetManager();
		graphicsCacheMap = new HashMap<String, TextureRegion>();
	}

	public boolean update() {
		return assetManager.update();
	}

	// return progress from 0 to 1
	public float getProgress() {
		return assetManager.getProgress();
	}

	public void load() {
		this.loadGraphics();
		this.loadFonts();
		this.loadSounds();
		this.loadAvailableLevels();
		//loadLevels(); it has to be loaded manually after loading whole assets because levels uses these assets 
	}
	
	public void loadLoadingScreenGraphics() {
		assetManager.load(LOADING_SCREEN_GRAPHICS_FILE, TextureAtlas.class);
	}

	public TextureRegion getGraphics(String name) {
		if (graphicsCacheMap.containsKey(name)) { // We have called this
													// already, so we can reuse
													// the cached result from
													// last time
			return graphicsCacheMap.get(name);
		}
		if (atlas == null) {
			atlas = assetManager.get(GRAPHICS_FILE, TextureAtlas.class);
		}
		TextureRegion tr = atlas.findRegion(name);

		if (tr == null) {
			throw new RuntimeException("Graphic " + name
					+ " not found it atlas");
		}

		tr.getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
		graphicsCacheMap.put(name, tr); // cache the result
		return tr;
	}

	public TextureRegion getLoadingScreenGraphics(String name) {
		if (graphicsCacheMap.containsKey(name)) { // We have called this
													// already, so we can reuse
													// the cached result from
													// last time
			return graphicsCacheMap.get(name);
		}
		if (atlas == null) {
			atlas = assetManager.get(LOADING_SCREEN_GRAPHICS_FILE, TextureAtlas.class);
		}
		TextureRegion tr = atlas.findRegion(name);

		if (tr == null) {
			throw new RuntimeException("Graphic " + name
					+ " not found it atlas");
		}

		tr.getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
		graphicsCacheMap.put(name, tr); // cache the result
		return tr;
	}
	
	public void disposeGameScreenGraphic(){
		this.atlas.dispose();
		this.atlas = null;
		this.graphicsCacheMap = new HashMap<String, TextureRegion>();
	}
	
	public void disposeLoadingScreenGraphics() {
		this.atlas.dispose();
		this.atlas = null;
		this.graphicsCacheMap = new HashMap<String, TextureRegion>();
	}

	private void loadGraphics() {
		// TODO Handle multiple resolution loading.
		// Break into loading of common controls and level-specific.
		// Figure out which graphics set to load, see http://wiki.starling-framework.org/manual/multi-resolution_development
		//int screenWidth = Gdx.app.getGraphics().getWidth();
		/*if( screenWidth >= 1280 ) {
			graphicsHorizontalRes = 1280;
			graphicsFile = GRAPHICS_FILE_X;
		}else{
			graphicsHorizontalRes = 320;
			graphicsFile = GRAPHICS_FILE_L;
		}*/
		assetManager.load(GRAPHICS_FILE , TextureAtlas.class);
	}



	private void loadFonts() {		
		assetManager.load(MENU_FONT_FILE, BitmapFont.class);
		assetManager.load(DIALOG_FONT_FILE, BitmapFont.class);
		assetManager.load(FINISH_DIALOG_FONT_FILE, BitmapFont.class);
		assetManager.load(TIME_INT_FONT, BitmapFont.class);
		assetManager.load(TIME_STRINGS_FONT, BitmapFont.class);
		assetManager.load(LEVEL_NUMBER_FONT, BitmapFont.class);
	}
	
	private void loadAvailableLevels() {
		assetManager.setLoader(AvailableLevels.class, new AvailableLevelsLoader(new InternalFileHandleResolver()));
		assetManager.load(AVAILABLE_LEVELS_FILE, AvailableLevels.class);
	}

	public BitmapFont getMenuFont() {
		return assetManager.get(MENU_FONT_FILE, BitmapFont.class);
	}

	public BitmapFont getDialogFont() {
		return assetManager.get(DIALOG_FONT_FILE, BitmapFont.class);
	}
	
	public BitmapFont getFinishDialogFont() {
		return assetManager.get(FINISH_DIALOG_FONT_FILE, BitmapFont.class);
	}
		
	public AvailableLevels getAvailableLevels() {
		return assetManager.get(AVAILABLE_LEVELS_FILE, AvailableLevels.class);
	}

	public FileHandle loadLevel(String name, Difficulty difficulty) {
		return Gdx.files.internal("levels\\"+ difficulty.toString() + "\\" + name + ".xml");
	}
	
	public BitmapFont getTimeStringFont()
	{
		return assetManager.get(TIME_STRINGS_FONT, BitmapFont.class);
	}
	
	public BitmapFont getTimeIntFont()
	{
		return assetManager.get(TIME_INT_FONT, BitmapFont.class);
	}
	
	public BitmapFont getLevelNumberFont()
	{
		return assetManager.get(LEVEL_NUMBER_FONT, BitmapFont.class);
	}
	
	private void loadSounds() {
		Map<String, Sound> soundsMap = new HashMap<String, Sound>();
		soundsMap.put(Constants.sounds.SOUND_EDITOR, Gdx.audio.newSound(Gdx.files.internal(Constants.sounds.SOUND_EDITOR_PATH)));
		soundsMap.put(Constants.sounds.SOUND_MUD, Gdx.audio.newSound(Gdx.files.internal(Constants.sounds.SOUND_MUD_PATH)));
		soundsMap.put(Constants.sounds.SOUND_TREE, Gdx.audio.newSound(Gdx.files.internal(Constants.sounds.SOUND_TREE_PATH)));
		soundsMap.put(Constants.sounds.SOUND_HOLE, Gdx.audio.newSound(Gdx.files.internal(Constants.sounds.SOUND_HOLE_PATH)));
		soundsMap.put(Constants.sounds.SOUND_STONE, Gdx.audio.newSound(Gdx.files.internal(Constants.sounds.SOUND_STONE_PATH)));
		soundsMap.put(Constants.sounds.SOUND_ENGINE_START, Gdx.audio.newSound(Gdx.files.internal(Constants.sounds.SOUND_ENGINE_START_PATH)));
		soundsMap.put(Constants.sounds.SOUND_MENU_OPEN, Gdx.audio.newSound(Gdx.files.internal(Constants.sounds.SOUND_MENU_OPEN_PATH)));
		soundsMap.put(Constants.sounds.SOUND_MENU_CLOSE, Gdx.audio.newSound(Gdx.files.internal(Constants.sounds.SOUND_MENU_CLOSE_PATH)));
		soundsMap.put(Constants.sounds.SOUND_MINIGAME_FAIL, Gdx.audio.newSound(Gdx.files.internal(Constants.sounds.SOUND_MINIGAME_FAIL_PATH)));
		soundsMap.put(Constants.sounds.SOUND_MINIGAME_SUCCESS, Gdx.audio.newSound(Gdx.files.internal(Constants.sounds.SOUND_MINIGAME_SUCCESS_PATH)));
		soundsMap.put(Constants.sounds.SOUND_SUB1_CHEER, Gdx.audio.newSound(Gdx.files.internal(Constants.sounds.SOUND_SUB1_CHEER_PATH)));
		soundsMap.put(Constants.sounds.SOUND_SUB2_CHEER, Gdx.audio.newSound(Gdx.files.internal(Constants.sounds.SOUND_SUB2_CHEER_PATH)));
		soundsMap.put(Constants.sounds.SOUND_SUB1_FAIL, Gdx.audio.newSound(Gdx.files.internal(Constants.sounds.SOUND_SUB1_FAIL_PATH)));
		soundsMap.put(Constants.sounds.SOUND_SUB2_START, Gdx.audio.newSound(Gdx.files.internal(Constants.sounds.SOUND_SUB2_START_PATH)));
		
		Music menuMusic = Gdx.audio.newMusic(Gdx.files.internal(Constants.sounds.MUSIC_MENU_PATH));
		Music raceMusic = Gdx.audio.newMusic(Gdx.files.internal(Constants.sounds.MUSIC_RACE_PATH));
		this.soundAndMusicManager = new SoundAndMusicManager(soundsMap, raceMusic, menuMusic);
	}
	

	

}
