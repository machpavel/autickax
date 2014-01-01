import com.badlogic.gdx.tools.imagepacker.TexturePacker.Settings;
import com.badlogic.gdx.tools.imagepacker.TexturePacker;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {

	private static final String ASSETS_PATH = "../Assets/";
	private static final Path ASSETS_IMAGES_PATH = Paths.get(ASSETS_PATH + "images");
	private static final Path ASSETS_FONTS_PATH = Paths.get(ASSETS_PATH + "fonts");
	private static final Path ASSETS_SFX_PATH = Paths.get(ASSETS_PATH + "sfx");
	private static final Path ASSETS_LEVELS_PATH = Paths.get(ASSETS_PATH + "levels");
	
	private static final String ANDROID_PATH = "../Autickax-android/assets/";
	private static final Path ANDROID_IMAGES_PATH = Paths.get(ANDROID_PATH + "images");
	private static final Path ANDROID_FONTS_PATH = Paths.get(ANDROID_PATH + "fonts");
	private static final Path ANDROID_SFX_PATH = Paths.get(ANDROID_PATH + "sfx");
	private static final Path ANDROID_LEVELS_PATH = Paths.get(ANDROID_PATH + "levels");
	
	private static final String DESKTOP_PATH = "../Autickax-desktop/bin/";
	private static final Path DESKTOP_IMAGES_PATH = Paths.get(DESKTOP_PATH + "images");
	private static final Path DESKTOP_FONTS_PATH = Paths.get(DESKTOP_PATH + "fonts");
	private static final Path DESKTOP_SFX_PATH = Paths.get(DESKTOP_PATH + "sfx");
	private static final Path DESKTOP_LEVELS_PATH = Paths.get(DESKTOP_PATH + "levels");
	
	
	
    public static void main(String[] args) throws IOException {
    	
    	Settings settings = new Settings();
    	TexturePacker.process(
			settings,
			ASSETS_IMAGES_PATH.toString(),
			ANDROID_IMAGES_PATH.toString(),
			"images"
		);
    	TexturePacker.process(
			settings,
			ASSETS_IMAGES_PATH.toString(),
			DESKTOP_IMAGES_PATH.toString(),
			"images"
		);
    	
    	FileUtilities.copyDirectory (
			ASSETS_FONTS_PATH.toFile(),
			ANDROID_FONTS_PATH.toFile()
		);
    	
    	FileUtilities.copyDirectory(
    		ASSETS_FONTS_PATH.toFile(),
			ANDROID_FONTS_PATH.toFile()
		);
    	FileUtilities.copyDirectory(
    		ASSETS_FONTS_PATH.toFile(),
			DESKTOP_FONTS_PATH.toFile()
		);
    	
    	FileUtilities.copyDirectory(
    		ASSETS_SFX_PATH.toFile(),
			ANDROID_SFX_PATH.toFile()
		);
    	FileUtilities.copyDirectory(
    		ASSETS_SFX_PATH.toFile(),
			DESKTOP_SFX_PATH.toFile()
		);
    	
    	FileUtilities.copyDirectory(
    		ASSETS_LEVELS_PATH.toFile(),
			ANDROID_LEVELS_PATH.toFile()
		);
    	
    	FileUtilities.copyDirectory(
    		ASSETS_LEVELS_PATH.toFile(),
			DESKTOP_LEVELS_PATH.toFile()
		);
    }
}
