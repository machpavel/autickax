package cz.mff.cuni.autickax;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * This class takes care of all asset loading and parsing Call load to load
 * graphics, sounds, fonts etc
 * 
 * @author Ondrej Paska
 */
public class Assets {

	private static final String GRAPHICS_DIR = "images";
	private static final String GRAPHICS_FILE = GRAPHICS_DIR + "/images";
	private static final String FONT_FILE = "fonts/font.fnt";
	private static final String MENU_FONT_FILE = "fonts/menu.fnt";
	public AssetManager assetManager;

	private Map<String, TextureRegion> graphicsCacheMap;

	private TextureAtlas atlas;

	private Map<String, Sound> soundsMap;
	
	public Music music;

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
		loadGraphics();
		loadFont();
		loadSounds();
		//loadLevels(); it has to be loaded manually after loading whole assets because levels uses these assets 
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

		graphicsCacheMap.put(name, tr); // cache the result
		return tr;
	}

	private void loadGraphics() {
		// TODO: Break into loading of common controls and level-specific.
		// TODO: Handle multiple resolution loading.
		//Figure out which graphics set to load, see http://wiki.starling-framework.org/manual/multi-resolution_development
		//int screenWidth = Gdx.app.getGraphics().getWidth();
		/*if( screenWidth >= 1280 ) {
			graphicsHorizontalRes = 1280;
			graphicsFile = GRAPHICS_FILE_X;
		}else{
			graphicsHorizontalRes = 320;
			graphicsFile = GRAPHICS_FILE_L;
		}*/
		assetManager.load(GRAPHICS_FILE , TextureAtlas.class);
		assetManager.load(GRAPHICS_FILE , TextureAtlas.class);
	}

	private void loadSounds() {
		soundsMap = new HashMap<String, Sound>();
		soundsMap.put(Constants.SOUND_JUMP, Gdx.audio.newSound(Gdx.files.internal("sfx/jump.wav")));
		soundsMap.put(Constants.SOUND_HIT, Gdx.audio.newSound(Gdx.files.internal("sfx/hit.wav")));
		music = Gdx.audio.newMusic(Gdx.files.internal("sfx/music.mp3"));
	}

	public Sound getSound(String name) {
		if (soundsMap.containsKey(name)) { // Reuse cached result
			return soundsMap.get(name);
		}
		throw new RuntimeException("Sound " + name + " not loaded");
	}

	private void loadFont() {
		assetManager.load(FONT_FILE, BitmapFont.class);
		assetManager.load(MENU_FONT_FILE, BitmapFont.class);
	}

	public BitmapFont getFont() {
		return assetManager.get(FONT_FILE, BitmapFont.class);
	}

	public BitmapFont getMenuFont() {
		return assetManager.get(MENU_FONT_FILE, BitmapFont.class);
	}

	public FileHandle loadLevel(String name, Difficulty difficulty) {
		return Gdx.files.internal("levels\\"+ difficulty.toString() + "\\" + name + ".xml");
	}
}
