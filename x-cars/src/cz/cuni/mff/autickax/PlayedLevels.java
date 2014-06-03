package cz.cuni.mff.autickax;

import java.util.Vector;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

import cz.cuni.mff.autickax.constants.Constants;

public class PlayedLevels {
	public Vector<PlayedLevel> kiddieLevels = new Vector<PlayedLevel>();
	public Vector<PlayedLevel> beginnerLevels = new Vector<PlayedLevel>();
	public Vector<PlayedLevel> normalLevels = new Vector<PlayedLevel>();
	public Vector<PlayedLevel> hardLevels = new Vector<PlayedLevel>();
	public Vector<PlayedLevel> extremeLevels = new Vector<PlayedLevel>();
	
	/**
	 * Stores information about played levels in a file, so that can be read again.
	 * 
	 * Note that levels are determined only by their order!
	 * 
	 * Each levels category is encoded into string and than stored in an xml file.
	 * Serialization is not used, because this way we have complete control about how it is stored
	 * and can make it as robust as we want, that can be helpful when updating the application.
	 * 
	 * One drawback is that skilled desktop user can locate the file and edit it so he can play now
	 * locked levels. This isn't such an issue, since it is not designed for desktop.
	 */
	public void storeLevels() {
		Preferences prefs = Gdx.app.getPreferences(Constants.serialization.PLAYED_LEVELS_FILENAME);
		
		putLevels(prefs, this.kiddieLevels, Constants.serialization.PLAYED_LEVELS_KIDDIE_NAME);
		putLevels(prefs, this.beginnerLevels, Constants.serialization.PLAYED_LEVELS_BEGINNER_NAME);
		putLevels(prefs, this.normalLevels, Constants.serialization.PLAYED_LEVELS_NORMAL_NAME);
		putLevels(prefs, this.hardLevels, Constants.serialization.PLAYED_LEVELS_HARD_NAME);
		putLevels(prefs, this.extremeLevels, Constants.serialization.PLAYED_LEVELS_EXTREME_NAME);
		
		prefs.flush();
	}

	private void putLevels(Preferences prefs, Vector<PlayedLevel> levels, String levelsName) {
		if (levels != null && levels.size() > 0) {
			String levelsString = "";
			for (int i = 0; i < levels.size(); ++i) {
				levelsString += levels.get(i).toString() + "|";
			}
			prefs.putString(levelsName, levelsString);
		}
	}
	
	public void loadLevels() {
		Preferences prefs = Gdx.app.getPreferences(Constants.serialization.PLAYED_LEVELS_FILENAME);
		
		loadLevels(prefs, this.kiddieLevels, Constants.serialization.PLAYED_LEVELS_KIDDIE_NAME);
		loadLevels(prefs, this.beginnerLevels, Constants.serialization.PLAYED_LEVELS_BEGINNER_NAME);
		loadLevels(prefs, this.normalLevels, Constants.serialization.PLAYED_LEVELS_NORMAL_NAME);
		loadLevels(prefs, this.hardLevels, Constants.serialization.PLAYED_LEVELS_HARD_NAME);
		loadLevels(prefs, this.extremeLevels, Constants.serialization.PLAYED_LEVELS_EXTREME_NAME);
	}

	private void loadLevels(Preferences prefs, Vector<PlayedLevel> levels, String levelsName) {
		if (prefs.contains(levelsName)) {
			String encodedLevelsString = prefs.getString(levelsName);
			String[] encodedLevels = encodedLevelsString.split("\\|");
			
			for (int i = 0; i < encodedLevels.length; ++i) {
				String[] values = encodedLevels[i].split(";");
				
				byte stars = Byte.parseByte(values[0].substring(6)); // "stars:5"
				float score = Float.parseFloat(values[1].substring(6)); // "score:123.5"
				
				levels.add(new PlayedLevel(score, stars));
			}
		} else { // initial level must be playable
			levels.add(new PlayedLevel(0, (byte)0));
		}
	}
}
