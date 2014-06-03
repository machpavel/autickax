package cz.cuni.mff.xcars;

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
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import cz.cuni.mff.xcars.constants.Constants;
import cz.cuni.mff.xcars.serialization.AvailableLevelsLoader;

public class Assets {

	private static final String GRAPHICS_DIR = "images";
	private static final String GRAPHICS_FILE = GRAPHICS_DIR + "/images.atlas";
	private static final String LOADING_SCREEN_GRAPHICS_FILE = "loadingScreen/images.atlas";
	private static final String MENU_FONT_FILE = "fonts/menu.fnt";
	private static final String DIALOG_FONT_FILE = "fonts/dialog.fnt";
	private static final String FINISH_DIALOG_FONT_FILE = "fonts/finishDialog.fnt";
	private static final String TIME_STRINGS_FONT = "fonts/timeString.fnt";
	private static final String TIME_INT_FONT = "fonts/timeInt.fnt";
	private static final String LEVEL_NUMBER_FONT = "fonts/levels.fnt";
	private static final String AVAILABLE_LEVELS_FILE = "availableLevels.bin";

	public AssetManager assetManager = new AssetManager();;
	private Map<String, TextureRegion> graphicsCacheMap = new HashMap<String, TextureRegion>();
	private Map<String, NinePatch> ninePatchCacheMap = new HashMap<String, NinePatch>();
	private Map<String, TextureRegion> loadingGraphicsCacheMap = new HashMap<String, TextureRegion>();

	private TextureAtlas graphicsAtlas;
	private TextureAtlas loadingGraphicsAtlas;

	public SoundAndMusicManager soundAndMusicManager;

	public Assets() {
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
		// loadLevels(); it has to be loaded manually after loading whole assets
		// because levels uses these assets
	}

	public void loadLoadingScreenGraphics() {
		assetManager.load(LOADING_SCREEN_GRAPHICS_FILE, TextureAtlas.class);
	}

	public NinePatch getNinePatch(String name) {
		if (this.ninePatchCacheMap.containsKey(name)) {
			return this.ninePatchCacheMap.get(name);
		}
		if (this.graphicsAtlas == null) {
			this.graphicsAtlas = this.assetManager.get(GRAPHICS_FILE, TextureAtlas.class);
		}
		NinePatch retVal = this.graphicsAtlas.createPatch(name);

		if (retVal == null) {
			throw new RuntimeException("Graphic " + name + " not found it atlas");
		}

		retVal.getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
		this.ninePatchCacheMap.put(name, retVal);
		return retVal;
	}

	public TextureRegion getAnyGraphic(String name, TextureAtlas atlas,
			Map<String, TextureRegion> cacheMap) {
		if (cacheMap.containsKey(name)) {
			return cacheMap.get(name);
		}

		TextureRegion retVal = atlas.findRegion(name);

		if (retVal == null) {
			throw new RuntimeException("Graphic " + name + " not found it atlas");
		}

		retVal.getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
		cacheMap.put(name, retVal); // cache the result
		return retVal;
	}

	public TextureRegion getGraphics(String name) {
		if (this.graphicsAtlas == null) {
			this.graphicsAtlas = this.assetManager.get(GRAPHICS_FILE, TextureAtlas.class);
		}
		return getAnyGraphic(name, this.graphicsAtlas, this.graphicsCacheMap);
	}

	public TextureRegion getLoadingScreenGraphics(String name) {
		if (this.loadingGraphicsAtlas == null) {
			this.loadingGraphicsAtlas = this.assetManager.get(LOADING_SCREEN_GRAPHICS_FILE,
					TextureAtlas.class);
		}
		return getAnyGraphic(name, this.loadingGraphicsAtlas, this.loadingGraphicsCacheMap);
	}

	public void disposeGameScreenGraphic() {
		if (this.graphicsAtlas != null) {
			this.graphicsAtlas.dispose();
			this.graphicsAtlas = null;
		}
		this.graphicsCacheMap = new HashMap<String, TextureRegion>();
		this.ninePatchCacheMap = new HashMap<String, NinePatch>();
	}

	public void disposeLoadingScreenGraphics() {
		if (this.loadingGraphicsAtlas != null) {
			this.loadingGraphicsAtlas.dispose();
			this.loadingGraphicsAtlas = null;
		}
		this.loadingGraphicsCacheMap = new HashMap<String, TextureRegion>();
	}

	private void loadGraphics() {
		// TODO Handle multiple resolution loading.
		// Break into loading of common controls and level-specific.
		// Figure out which graphics set to load, see
		// http://wiki.starling-framework.org/manual/multi-resolution_development
		// int screenWidth = Gdx.app.getGraphics().getWidth();
		/*
		 * if( screenWidth >= 1280 ) { graphicsHorizontalRes = 1280;
		 * graphicsFile = GRAPHICS_FILE_X; }else{ graphicsHorizontalRes = 320;
		 * graphicsFile = GRAPHICS_FILE_L; }
		 */
		assetManager.load(GRAPHICS_FILE, TextureAtlas.class);
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
		assetManager.setLoader(AvailableLevels.class, new AvailableLevelsLoader(
				new InternalFileHandleResolver()));
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
		return Gdx.files.internal("levels\\" + difficulty.toString() + "\\" + name + ".xml");
	}

	public BitmapFont getTimeStringFont() {
		return assetManager.get(TIME_STRINGS_FONT, BitmapFont.class);
	}

	public BitmapFont getTimeIntFont() {
		return assetManager.get(TIME_INT_FONT, BitmapFont.class);
	}

	public BitmapFont getLevelNumberFont() {
		return assetManager.get(LEVEL_NUMBER_FONT, BitmapFont.class);
	}

	private void loadSounds() {
		Map<String, Sound> soundsMap = new HashMap<String, Sound>();
		soundsMap.put(Constants.sounds.SOUND_EDITOR,
				Gdx.audio.newSound(Gdx.files.internal(Constants.sounds.SOUND_EDITOR_PATH)));
		soundsMap.put(Constants.sounds.SOUND_MUD,
				Gdx.audio.newSound(Gdx.files.internal(Constants.sounds.SOUND_MUD_PATH)));
		soundsMap.put(Constants.sounds.SOUND_TREE,
				Gdx.audio.newSound(Gdx.files.internal(Constants.sounds.SOUND_TREE_PATH)));
		soundsMap.put(Constants.sounds.SOUND_HOLE,
				Gdx.audio.newSound(Gdx.files.internal(Constants.sounds.SOUND_HOLE_PATH)));
		soundsMap.put(Constants.sounds.SOUND_STONE,
				Gdx.audio.newSound(Gdx.files.internal(Constants.sounds.SOUND_STONE_PATH)));
		soundsMap.put(Constants.sounds.SOUND_ENGINE_START,
				Gdx.audio.newSound(Gdx.files.internal(Constants.sounds.SOUND_ENGINE_START_PATH)));
		soundsMap.put(Constants.sounds.SOUND_MENU_OPEN,
				Gdx.audio.newSound(Gdx.files.internal(Constants.sounds.SOUND_MENU_OPEN_PATH)));
		soundsMap.put(Constants.sounds.SOUND_MENU_CLOSE,
				Gdx.audio.newSound(Gdx.files.internal(Constants.sounds.SOUND_MENU_CLOSE_PATH)));
		soundsMap.put(Constants.sounds.SOUND_MINIGAME_FAIL,
				Gdx.audio.newSound(Gdx.files.internal(Constants.sounds.SOUND_MINIGAME_FAIL_PATH)));
		soundsMap.put(Constants.sounds.SOUND_MINIGAME_SUCCESS, Gdx.audio.newSound(Gdx.files
				.internal(Constants.sounds.SOUND_MINIGAME_SUCCESS_PATH)));
		soundsMap.put(Constants.sounds.SOUND_SUB1_CHEER,
				Gdx.audio.newSound(Gdx.files.internal(Constants.sounds.SOUND_SUB1_CHEER_PATH)));
		soundsMap.put(Constants.sounds.SOUND_SUB2_CHEER,
				Gdx.audio.newSound(Gdx.files.internal(Constants.sounds.SOUND_SUB2_CHEER_PATH)));
		soundsMap.put(Constants.sounds.SOUND_SUB1_FAIL,
				Gdx.audio.newSound(Gdx.files.internal(Constants.sounds.SOUND_SUB1_FAIL_PATH)));
		soundsMap.put(Constants.sounds.SOUND_SUB2_START,
				Gdx.audio.newSound(Gdx.files.internal(Constants.sounds.SOUND_SUB2_START_PATH)));

		for (int i = 1; i <= 3; i++) {
			soundsMap.put(Constants.sounds.SOUND_FINISH_DIALOG_STAR_PREFIX + i, Gdx.audio
					.newSound(Gdx.files
							.internal(Constants.sounds.SOUND_FINISH_DIALOG_STAR_PATH_PREFIX + i
									+ ".mp3")));
		}

		Music menuMusic = Gdx.audio.newMusic(Gdx.files.internal(Constants.sounds.MUSIC_MENU_PATH));
		Music raceMusic = Gdx.audio.newMusic(Gdx.files.internal(Constants.sounds.MUSIC_RACE_PATH));
		this.soundAndMusicManager = new SoundAndMusicManager(soundsMap, raceMusic, menuMusic);
	}

}
