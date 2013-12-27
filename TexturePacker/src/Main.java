import com.badlogic.gdx.tools.imagepacker.TexturePacker.Settings;
import com.badlogic.gdx.tools.imagepacker.TexturePacker;

import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {
	
    public static void main(String[] args) {
    	
    	Path imagesPath = Paths.get("../Assets/images");
    	Path outputPath = Paths.get("../Autickax-android/assets/images");
    	
    	Settings settings = new Settings();
    	TexturePacker.process(settings, imagesPath.toString(), outputPath.toString(), "images");
    }
}
