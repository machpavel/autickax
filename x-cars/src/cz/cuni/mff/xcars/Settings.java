package cz.cuni.mff.xcars;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

import cz.cuni.mff.xcars.constants.Constants;

public class Settings {
	private boolean showTooltips = true;
	private boolean playSounds = true;
	private boolean playMusic = true;

	public void loadSettings() {
		Preferences prefs = Gdx.app.getPreferences(Constants.serialization.PREFERENCES_FILENAME);

		showTooltips = prefs.getBoolean("showTooltips", true);
		playSounds = prefs.getBoolean("playSounds", true);
		playMusic = prefs.getBoolean("playMusic", true);
	}

	public void storeSettings() {
		Preferences prefs = Gdx.app.getPreferences(Constants.serialization.PREFERENCES_FILENAME);

		prefs.putBoolean("showTooltips", this.showTooltips);
		prefs.putBoolean("playSounds", this.playSounds);
		prefs.putBoolean("playMusic", this.playMusic);

		prefs.flush();
	}

	public boolean isShowTooltips() {
		return showTooltips;
	}

	public void setShowTooltips(boolean showTooltips) {
		this.showTooltips = showTooltips;
		this.storeSettings();
	}

	public boolean isPlaySounds() {
		return playSounds;
	}

	public void setPlaySounds(boolean playSounds) {
		this.playSounds = playSounds;
		this.storeSettings();
	}

	public boolean isPlayMusic() {
		return playMusic;
	}

	public void setPlayMusic(boolean playMusic) {
		this.playMusic = playMusic;
		Xcars.settings.storeSettings();
	}
}
