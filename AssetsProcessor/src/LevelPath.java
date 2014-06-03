import java.io.File;

import com.badlogic.gdx.files.FileHandle;

import cz.cuni.mff.autickax.Level;

public class LevelPath {


	private final File file;
	
	private Level level = null;

	public LevelPath(File file) {
		this.file = file;
	}
	
	public void parseLevel() {
		this.level = new Level();
		try {
			level.parseLevel(new FileHandle(this.file));
		} catch (Exception e) {
			System.out.println("Unable to parse file " + this.file.getName()
					+ ". Details: " + e.getMessage());
			e.printStackTrace();
		}
	}
	
	public Level getLevel() {
		return this.level;
	}
}
