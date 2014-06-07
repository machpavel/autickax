package cz.cuni.mff.xcars;

import java.util.Map;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

import cz.cuni.mff.xcars.constants.Constants;
import cz.cuni.mff.xcars.entities.GameObject;

public class SoundAndMusicManager {
	private Map<String, Sound> soundsMap;

	private Music raceMusic;
	private Music menuMusic;

	public SoundAndMusicManager() {
	}

	public void assignSounds(Map<String, Sound> _soundsMap) {
		this.soundsMap = _soundsMap;
	}

	public void assignMusic(Music _raceMusic, Music _menuMusic) {
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
		if (Xcars.settings.isPlayMusic()) {
			raceMusic.setLooping(true);
			raceMusic.setVolume(Constants.sounds.MUSIC_DEFAULT_VOLUME);
			raceMusic.play();
		}
	}

	public void playMenuMusic() {
		if (Xcars.settings.isPlayMusic()) {
			menuMusic.setLooping(true);
			menuMusic.setVolume(Constants.sounds.MUSIC_DEFAULT_VOLUME);
			menuMusic.play();
		}
	}

	public void pauseMenuMusic() {
		menuMusic.pause();
	}

	public void pauseRaceMusic() {
		raceMusic.pause();
	}

	public void stopRaceMusic() {
		raceMusic.stop();
	}

	public void playSound(String soundName) {
		this.playSound(soundName, Constants.sounds.SOUND_DEFAULT_VOLUME);
	}

	public void playSound(String soundName, float volume) {
		if (Xcars.settings.isPlaySounds()) {
			Sound sound = getSound(soundName);
			sound.play(volume);
		}
	}

	public void playStartEngineSound() {
		playSound(Constants.sounds.SOUND_ENGINE_START,
				Constants.sounds.SOUND_ENGINE_VOLUME);
	}

	public void pauseAllMusic() {
		this.raceMusic.pause();
		this.menuMusic.pause();
	}

	public void stopAllMusic() {
		this.raceMusic.stop();
		this.menuMusic.stop();
	}

	public void playCollisionSound(GameObject collisionOrigin) {
		if (Xcars.settings.isPlaySounds()) {
			String soundName = collisionOrigin.getSoundName();
			if (!soundName.equals(Constants.sounds.SOUND_NO_SOUND)) {
				playSound(
						soundName,
						Constants.sounds.SOUND_GAME_OBJECT_INTERACTION_DEFAULT_VOLUME);
			}
		}
	}

}
