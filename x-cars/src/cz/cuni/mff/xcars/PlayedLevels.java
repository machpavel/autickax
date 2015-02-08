package cz.cuni.mff.xcars;

import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.Vector;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

import cz.cuni.mff.xcars.constants.Constants;

public class PlayedLevels {
	public TreeMap<String, Vector<PlayedLevel>> levels = new TreeMap<String, Vector<PlayedLevel>>();
	
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
		
		for(Entry<String, Vector<PlayedLevel>> levelsSets : this.levels.entrySet()) {
		    String name = levelsSets.getKey();
		    Vector<PlayedLevel> levels = levelsSets.getValue();

		    putLevels(prefs, levels, name);
		}
		
		prefs.flush();
	}
	
	public void loadLevels() {
		Preferences prefs = Gdx.app.getPreferences(Constants.serialization.PLAYED_LEVELS_FILENAME);
		
	 	@SuppressWarnings("unchecked")
		Map<String, String> loadedLevelsSets = (Map<String, String>) prefs.get();
	 	
	 	if (loadedLevelsSets.size() > 0) {
		 	for(Entry<String, String> levelsSets : loadedLevelsSets.entrySet()) {
		 		Vector<PlayedLevel> newLevels = new Vector<PlayedLevel>();
		 		this.levels.put(levelsSets.getKey(), newLevels);
	
			    loadLevels(newLevels, levelsSets.getValue());
		 	}
	 	}
	 	else
	 	{
	 		String firstScenarioName = Xcars.availableLevels.scenarios.get(0).name;
	 		Vector<PlayedLevel> firstScenarioPlayedLevels = new Vector<PlayedLevel>();
	 		firstScenarioPlayedLevels.add(new PlayedLevel(0, (byte)0));
	 		this.levels.put(firstScenarioName, firstScenarioPlayedLevels);
	 	}
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

	private void loadLevels(Vector<PlayedLevel> levels, String encodedLevelsString) {
		String[] encodedLevels = encodedLevelsString.split("\\|");
		
		for (int i = 0; i < encodedLevels.length; ++i) {
			String[] values = encodedLevels[i].split(";");
			
			byte stars = Byte.parseByte(values[0].substring(6)); // "stars:5"
			float score = Float.parseFloat(values[1].substring(6)); // "score:123.5"
			
			levels.add(new PlayedLevel(score, stars));
		}
	}
}
