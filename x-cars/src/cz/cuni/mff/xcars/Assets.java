package cz.cuni.mff.xcars;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import com.badlogic.gdx.Application.ApplicationType;
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
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;

import cz.cuni.mff.xcars.debug.Debug;
import cz.cuni.mff.xcars.input.Input;
import cz.cuni.mff.xcars.serialization.AvailableLevelsLoader;
import cz.cuni.mff.xcars.sfx.SoundAndMusicManager;

public class Assets {

	private static final String GRAPHICS_DIR = "images/";
	private static final String GRAPHICS_FILE = GRAPHICS_DIR + "images.atlas";
	private static final String LOADING_SCREEN_GRAPHICS_FILE = "loadingScreen/images.atlas";
	private static final String FONTS_DIRECTORY = "fonts/";
	private static final String MENU_FONT_FILE = FONTS_DIRECTORY + "menu.fnt";
	private static final String DIALOG_FONT_FILE = FONTS_DIRECTORY + "dialog.fnt";
	private static final String FINISH_DIALOG_FONT_FILE = FONTS_DIRECTORY + "finishDialog.fnt";
	private static final String TIME_STRINGS_FONT = FONTS_DIRECTORY + "timeString.fnt";
	private static final String TIME_INT_FONT = FONTS_DIRECTORY + "timeInt.fnt";
	private static final String LEVEL_NUMBER_FONT = FONTS_DIRECTORY + "levels.fnt";
	private static final String LIGHT_FONT = FONTS_DIRECTORY + "xcars-Light.ttf";
	private static final String BOLD_FONT = FONTS_DIRECTORY + "xcars-Bold.ttf";
	private static final String AVAILABLE_LEVELS_FILE = "availableLevels.bin";
	private static final String SOUNDS_DIR = "sfx/sounds/";
	private static final String MUSIC_MENU_DIR = "sfx/music/menu";
	private static final String MUSIC_RACE_DIR = "sfx/music/race";

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
		this.loadSfx();
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
			this.graphicsAtlas = this.assetManager.get(GRAPHICS_FILE,
					TextureAtlas.class);
		}
		NinePatch retVal = this.graphicsAtlas.createPatch(name);

		if (retVal == null) {
			if (name == "noTexture")
				throw new RuntimeException("Graphic " + name
						+ " not found it atlas");
			else {
				Debug.Log("Graphic \"" + name + "\" not found.");
				return getNinePatch("noTexture");
			}
		}

		retVal.getTexture().setFilter(TextureFilter.Linear,
				TextureFilter.Linear);
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
			if (name == "noTexture")
				throw new RuntimeException("Graphic " + name
						+ " not found it atlas");
			else {
				Debug.Log("Graphic \"" + name + "\" not found.");
				return getAnyGraphic("noTexture", atlas, cacheMap);
			}
		}

		retVal.getTexture().setFilter(TextureFilter.Linear,
				TextureFilter.Linear);
		cacheMap.put(name, retVal); // cache the result
		return retVal;
	}

	public TextureRegion getGraphics(String name) {
		if (this.graphicsAtlas == null) {
			this.graphicsAtlas = this.assetManager.get(GRAPHICS_FILE,
					TextureAtlas.class);
		}
		return getAnyGraphic(name, this.graphicsAtlas, this.graphicsCacheMap);
	}

	public TextureRegion getLoadingScreenGraphics(String name) {
		if (this.loadingGraphicsAtlas == null) {
			this.loadingGraphicsAtlas = this.assetManager.get(
					LOADING_SCREEN_GRAPHICS_FILE, TextureAtlas.class);
		}
		return getAnyGraphic(name, this.loadingGraphicsAtlas,
				this.loadingGraphicsCacheMap);
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
		FileHandle lightFontHandle = Gdx.files.internal(Assets.LIGHT_FONT);
		FreeTypeFontGenerator lightFontGenerator = new FreeTypeFontGenerator(lightFontHandle);
		
		FileHandle boldFontHandle = Gdx.files.internal(Assets.BOLD_FONT);
		FreeTypeFontGenerator boldFontGenerator = new FreeTypeFontGenerator(boldFontHandle);
		
		FreeTypeFontParameter lightFontParameter = new FreeTypeFontParameter();
		lightFontParameter.size = (int) (30 * Input.xStretchFactorInv);
		
		this.timeIntFont = lightFontGenerator.generateFont(lightFontParameter);
		this.timeIntFont.setScale(Input.xStretchFactor);
		

		FreeTypeFontParameter dialogFontParameter = new FreeTypeFontParameter();
		dialogFontParameter.size = (int) (30 * Input.xStretchFactorInv);
		
		this.dialogFont = lightFontGenerator.generateFont(dialogFontParameter);
		this.dialogFont.setScale(Input.xStretchFactor);
		
		FreeTypeFontParameter boldFontParameter = new FreeTypeFontParameter();
		boldFontParameter.size = (int) (30 * Input.xStretchFactorInv);
		
		this.menuFont = lightFontGenerator.generateFont(boldFontParameter);
		this.menuFont.setScale(Input.xStretchFactor);
		
		this.levelNumberFont = boldFontGenerator.generateFont(boldFontParameter);
		this.levelNumberFont.setScale(Input.xStretchFactor);
		
		this.timeStringFont = lightFontGenerator.generateFont(boldFontParameter);
		this.timeStringFont.setScale(Input.xStretchFactor);
		
		this.finishDialogFont = lightFontGenerator.generateFont(boldFontParameter);
		this.finishDialogFont.setScale(Input.xStretchFactor);
		
		
		lightFontGenerator.dispose();
		boldFontGenerator.dispose();
	}

	private void loadAvailableLevels() {
		assetManager.setLoader(AvailableLevels.class,
				new AvailableLevelsLoader(new InternalFileHandleResolver()));
		assetManager.load(AVAILABLE_LEVELS_FILE, AvailableLevels.class);
	}

	private BitmapFont menuFont;
	public BitmapFont getMenuFont() {
		return this.menuFont;
	}

	private BitmapFont dialogFont;
	public BitmapFont getDialogFont() {
		return this.dialogFont;
	}

	private BitmapFont finishDialogFont;
	public BitmapFont getFinishDialogFont() {
		return this.finishDialogFont;
	}

	private BitmapFont timeStringFont;
	public BitmapFont getTimeStringFont() {
		return this.timeStringFont;
	}

	private BitmapFont timeIntFont;
	public BitmapFont getTimeIntFont() {
		return this.timeIntFont;
	}

	private BitmapFont levelNumberFont;
	public BitmapFont getLevelNumberFont() {
		return this.levelNumberFont;
	}

	public AvailableLevels getAvailableLevels() {
		return assetManager.get(AVAILABLE_LEVELS_FILE, AvailableLevels.class);
	}

	private void loadSfx() {
		this.soundAndMusicManager = new SoundAndMusicManager();
		loadMusic(this.soundAndMusicManager);
		loadSounds(this.soundAndMusicManager);
	}

	private void loadMusic(SoundAndMusicManager soundAndMusicManager) {
		ArrayList<Music> menuMusic = loadAllMusicFromDir(MUSIC_MENU_DIR);
		ArrayList<Music> raceMusic = loadAllMusicFromDir(MUSIC_RACE_DIR);
		soundAndMusicManager.assignMusic(raceMusic, menuMusic);
	}

	private ArrayList<Music> loadAllMusicFromDir(String path) {
		FileHandle dirHandle;
		String prefix;
		if (Gdx.app.getType() == ApplicationType.Android) {
			prefix = "";
		} else {
			prefix = "./bin/";
		}
		ArrayList<Music> music = new ArrayList<Music>();
		JarAssetsLoader jarLoader = new JarAssetsLoader();
		LinkedList<FileHandle> handles = jarLoader.LoadFromJar(path);
		if (handles.isEmpty()) //run from IDE
		{
			dirHandle = Gdx.files.internal(prefix + path);
			getHandles(dirHandle, handles);
		}
		for (FileHandle handle : handles) {
			music.add(Gdx.audio.newMusic(handle));
		}

		return music;
	}

	private void loadSounds(SoundAndMusicManager soundAndMusicManager) {
		FileHandle dirHandle;
		String prefix;
		if (Gdx.app.getType() == ApplicationType.Android) {
			prefix = "";
		} else {
			prefix = "./bin/";
		}
		String path = SOUNDS_DIR;
		JarAssetsLoader jarLoader = new JarAssetsLoader();
		LinkedList<FileHandle> handles = jarLoader.LoadFromJar(path);
		if (handles.isEmpty())
		{
			dirHandle = Gdx.files.internal(prefix + path);
			getHandles(dirHandle, handles);
		}
		else
			prefix = "";
		Map<String, Sound> soundsMap = new HashMap<String, Sound>();
		for (FileHandle fileHandle : handles) {
			String name = fileHandle.path();
			name = name.substring(prefix.length());
			soundsMap.put(name, Gdx.audio.newSound(fileHandle));
		}

		soundAndMusicManager.assignSounds(soundsMap);
	}

	public void getHandles(FileHandle begin, LinkedList<FileHandle> handles) {
		FileHandle[] newHandles = begin.list();
		for (FileHandle file : newHandles) {
			if (file.isDirectory()) {
				getHandles(file, handles);
			} else {
				handles.add(file);
			}
		}
	}

}
