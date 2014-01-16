package cz.mff.cuni.autickax;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

public class Settings {
	public boolean showTooltips = true;
	public boolean playSounds = true;
	public boolean playMusic = true;
	
	public void loadSettings() {
		Preferences prefs = Gdx.app.getPreferences("autickaxPreferences");
		
		this.showTooltips = prefs.getBoolean("showTooltips", true);
		this.playSounds = prefs.getBoolean("playSounds", true);
		this.playMusic = prefs.getBoolean("playMusic", true);
	}
	
	public void storeSettings() {
		Preferences prefs = Gdx.app.getPreferences("autickaxPreferences");
		
		prefs.putBoolean("showTooltips", this.showTooltips);
		prefs.putBoolean("playSounds", this.playSounds);
		prefs.putBoolean("playMusic", this.playMusic);
		
		prefs.flush();
	}
}
