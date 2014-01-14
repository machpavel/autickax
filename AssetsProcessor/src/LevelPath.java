import java.io.File;

import com.badlogic.gdx.files.FileHandle;

import cz.mff.cuni.autickax.LevelLoading;

public class LevelPath {


	private final File file;
	
	public LevelLoading level;

	public LevelPath(File file, String directory) {
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
