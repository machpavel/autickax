import java.io.File;

import com.badlogic.gdx.files.FileHandle;

import cz.mff.cuni.autickax.Difficulty;
import cz.mff.cuni.autickax.LevelLoading;

public class LevelPath {


	private final File file;
	
	private LevelLoading level = null;

	public LevelPath(File file) {
		this.file = file;
	}
	
	public void parseLevel() {
		this.level = new LevelLoading();
		try {
			level.parseLevel(null, new FileHandle(this.file));
		} catch (Exception e) {
			System.out.println("Unable to parse file " + this.file.getName()
					+ ". Details: " + e.getMessage());
			e.printStackTrace();
		}
	}
	
	public LevelLoading getLevel() {
		return this.level;
	}
}
