package cz.cuni.mff.xcars.sfx;

import java.util.ArrayList;
import java.util.Map;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

import cz.cuni.mff.xcars.Xcars;
import cz.cuni.mff.xcars.constants.Constants;
import cz.cuni.mff.xcars.entities.GameObject;

public class SoundAndMusicManager{
	private Map<String, Sound> soundsMap;

	private MusicPlayer raceMusicPlayer;
	private MusicPlayer menuMusicPlayer;

	public SoundAndMusicManager() {
	}

	public void assignSounds(Map<String, Sound> _soundsMap) {
		this.soundsMap = _soundsMap;
	}

	public void assignMusic(ArrayList<Music> _raceMusic, ArrayList<Music> _menuMusic) {
		this.raceMusicPlayer = new MusicPlayer(_raceMusic);
		this.menuMusicPlayer = new MusicPlayer(_menuMusic);
	}

	Sound getSound(String name) {
		if (soundsMap.containsKey(name)) { // Reuse cached result
			return soundsMap.get(name);
		}
		throw new RuntimeException("Sound " + name + " not loaded");
	}

	public void playRaceMusic() {
		if (Xcars.settings.isPlayMusic()) {
			raceMusicPlayer.play();
		}
	}

	public void playMenuMusic() {
		if (Xcars.settings.isPlayMusic()) {
			menuMusicPlayer.play();
		}
	}

	public void pauseMenuMusic() {
		menuMusicPlayer.pause();
	}

	public void pauseRaceMusic() {
		raceMusicPlayer.pause();
	}

	public void stopRaceMusic() {
		raceMusicPlayer.stop();
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
		this.raceMusicPlayer.pause();
		this.menuMusicPlayer.pause();
	}

	public void stopAllMusic() {
		this.raceMusicPlayer.stop();
		this.menuMusicPlayer.stop();
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
