package cz.mff.cuni.autickax;

import java.util.Map;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

import cz.mff.cuni.autickax.constants.Constants;
import cz.mff.cuni.autickax.entities.GameObject;

public class SoundAndMusicManager {
	private Map<String, Sound> soundsMap;

	private Music raceMusic;
	private Music menuMusic;

	public SoundAndMusicManager(Map<String, Sound> _soundsMap, Music _raceMusic, Music _menuMusic) {
		this.soundsMap = _soundsMap;
		this.raceMusic = _raceMusic;
		this.menuMusic = _menuMusic;
	}

	Sound getSound(String name) {
		if (soundsMap.containsKey(name)) { // Reuse cached result
			return soundsMap.get(name);
		}
		throw new RuntimeException("Sound " + name + " not loaded");
	}

	public void playRaceMusic() {
		if (Autickax.settings.playMusic) {
			raceMusic.setLooping(true);
			raceMusic.setVolume(Constants.sounds.MUSIC_DEFAULT_VOLUME);
			raceMusic.play();
		}
	}

	public void playMenuMusic() {
		if (Autickax.settings.playMusic) {
			menuMusic.setLooping(true);
			menuMusic.setVolume(Constants.sounds.MUSIC_DEFAULT_VOLUME);
			menuMusic.play();
		}
	}

	public void pauseMenuMusic() {
		menuMusic.pause();
	}

	public void stopRaceMusic() {
		raceMusic.stop();
	}

	public void playSound(String soundName, float volume) {
		if (Autickax.settings.playSounds) {
			Sound sound = getSound(soundName);
			sound.play(volume);
		}
	}

	public void stopAllMusic() {
		this.raceMusic.stop();
		this.menuMusic.stop();
	}

	public void playCollisionSound(GameObject collisionOrigin) {
		if (Autickax.settings.playSounds) {
			String soundName = collisionOrigin.getSoundName();
			if (!soundName.equals(Constants.sounds.SOUND_NO_SOUND)) {
				playSound(soundName, Constants.sounds.SOUND_GAME_OBJECT_INTERACTION_DEFAULT_VOLUME);
			}
		}
	}
}
