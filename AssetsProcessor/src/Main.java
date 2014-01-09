import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.tools.imagepacker.TexturePacker.Settings;
import com.badlogic.gdx.tools.imagepacker.TexturePacker;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {

	private static final String ASSETS_PATH = "../Assets/";
	private static final String ASSETS_IMAGES_PATH = ASSETS_PATH + "images";
	private static final Path ASSETS_FONTS_PATH = Paths.get(ASSETS_PATH + "fonts");
	private static final Path ASSETS_SFX_PATH = Paths.get(ASSETS_PATH + "sfx");
	private static final Path ASSETS_LEVELS_PATH = Paths.get(ASSETS_PATH + "levels");
	
	private static final String ANDROID_PATH = "../Autickax-android/assets/";
	private static final Path ANDROID_IMAGES_PATH = Paths.get(ANDROID_PATH + "images");
	private static final Path ANDROID_FONTS_PATH = Paths.get(ANDROID_PATH + "fonts");
	private static final Path ANDROID_SFX_PATH = Paths.get(ANDROID_PATH + "sfx");
	private static final Path ANDROID_LEVELS_PATH = Paths.get(ANDROID_PATH + "levels");
	
	
	private static final String ASSETS_LEVELS_PATH_IMAGES = ASSETS_IMAGES_PATH + "/levels-paths";
	
    public static void main(String[] args) throws IOException {
    	
    	createLevelsPaths();
    	System.out.println("Levels path created.");
    	
    	compressImages();
    	System.out.println("Images compressed and copied.");
    	
    	copyFont();
    	System.out.println("Font copied.");
    	
    	copySfx();
    	System.out.println("Audio copied.");
    	
    	copyLevels();
    	System.out.println("Levels copied.");
    	
    	System.out.println("Assets processed.");
    }
    
    private static void createLevelsPaths() {
    	File levelsImagesDirectory = new File(ASSETS_LEVELS_PATH_IMAGES);
    	if (!levelsImagesDirectory.exists()) {
    		System.out.println("creating directory: " + ASSETS_LEVELS_PATH_IMAGES);
    		levelsImagesDirectory.mkdir();
    	}
    	  
    	File levelsDirectory = ASSETS_LEVELS_PATH.toFile();
    	File[] files = levelsDirectory.listFiles();
    	for( File file : files ) {
    		LevelPath levelPath = new LevelPath(file);
    		levelPath.createBitmap(800, 480, ASSETS_LEVELS_PATH_IMAGES);
    	}
    }

	private static void copyLevels() throws IOException {
		FileUtilities.copyDirectory(
    		ASSETS_LEVELS_PATH.toFile(),
			ANDROID_LEVELS_PATH.toFile()
		);
	}

	private static void copySfx() throws IOException {
		FileUtilities.copyDirectory(
    		ASSETS_SFX_PATH.toFile(),
			ANDROID_SFX_PATH.toFile()
		);
	}

	private static void copyFont() throws IOException {
		FileUtilities.copyDirectory(
    		ASSETS_FONTS_PATH.toFile(),
			ANDROID_FONTS_PATH.toFile()
		);
	}
    
    private static void compressImages() {
    	Settings settings = new Settings();
    	TexturePacker.process(
			settings,
			ASSETS_IMAGES_PATH,
			ANDROID_IMAGES_PATH.toString(),
			"images"
		);
    }
}
