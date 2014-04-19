package cz.mff.cuni.autickax.dialogs;

import cz.mff.cuni.autickax.Autickax;
import cz.mff.cuni.autickax.constants.Constants;
import cz.mff.cuni.autickax.gamelogic.SubLevel;
import cz.mff.cuni.autickax.menu.MenuButton;
import cz.mff.cuni.autickax.scene.GameScreen;

public class PauseDialog extends DecisionDialog {

	private MenuButton buttonTooltips;
	private MenuButton buttonSounds;
	private MenuButton buttonMusic;

	public PauseDialog(GameScreen gameScreen, SubLevel subLevel) {
		super(gameScreen, subLevel, Constants.strings.GAME_PAUSED, true);

		// Tooltips Button------------------------------------------------
		String tooltipsTexture = Autickax.settings.showTooltips ? Constants.menu.BUTTON_MENU_TOOLTIPS_ON
				: Constants.menu.BUTTON_MENU_TOOLTIPS_OFF;
		String tooltipsHoverTexture = Autickax.settings.showTooltips ? Constants.menu.BUTTON_MENU_TOOLTIPS_ON_HOVER
				: Constants.menu.BUTTON_MENU_TOOLTIPS_OFF_HOVER;

		this.buttonTooltips = new MenuButton(
				Autickax.getInstance().assets.getGraphics(tooltipsTexture),
				Autickax.getInstance().assets.getGraphics(tooltipsHoverTexture)) {
			@Override
			public void action() {

				Autickax.settings.showTooltips = !Autickax.settings.showTooltips;

				if (Autickax.settings.showTooltips) {
					buttonTooltips
							.setStyle(
									Autickax.getInstance().assets
											.getGraphics(Constants.menu.BUTTON_MENU_TOOLTIPS_ON),
									Autickax.getInstance().assets
											.getGraphics(Constants.menu.BUTTON_MENU_TOOLTIPS_ON_HOVER));
				} else {
					buttonTooltips
							.setStyle(
									Autickax.getInstance().assets
											.getGraphics(Constants.menu.BUTTON_MENU_TOOLTIPS_OFF),
									Autickax.getInstance().assets
											.getGraphics(Constants.menu.BUTTON_MENU_TOOLTIPS_OFF_HOVER));
				}
			}
		};
		this.buttonTooltips.setPosition (
			Constants.dialog.PAUSE_DIALOG_TOOLTIPS_POSITION_X,
			Constants.dialog.PAUSE_DIALOG_TOOLTIPS_POSITION_Y
		);
		stage.addActor(this.buttonTooltips);

		// Sounds Button------------------------------------------------
		String soundsTexture = Autickax.settings.playSounds ? Constants.menu.BUTTON_MENU_SOUNDS_ON
				: Constants.menu.BUTTON_MENU_SOUNDS_OFF;
		String soundsHoverTexture = Autickax.settings.playSounds ? Constants.menu.BUTTON_MENU_SOUNDS_ON_HOVER
				: Constants.menu.BUTTON_MENU_SOUNDS_OFF_HOVER;

		this.buttonSounds = new MenuButton(
				Autickax.getInstance().assets.getGraphics(soundsTexture),
				Autickax.getInstance().assets.getGraphics(soundsHoverTexture)) {
			@Override
			public void action() {

				Autickax.settings.playSounds = !Autickax.settings.playSounds;

				if (Autickax.settings.playSounds) {
					buttonSounds
							.setStyle(
									Autickax.getInstance().assets
											.getGraphics(Constants.menu.BUTTON_MENU_SOUNDS_ON),
									Autickax.getInstance().assets
											.getGraphics(Constants.menu.BUTTON_MENU_SOUNDS_ON_HOVER));
				} else {
					buttonSounds
							.setStyle(
									Autickax.getInstance().assets
											.getGraphics(Constants.menu.BUTTON_MENU_SOUNDS_OFF),
									Autickax.getInstance().assets
											.getGraphics(Constants.menu.BUTTON_MENU_SOUNDS_OFF_HOVER));
				}
			}
		};
		this.buttonSounds.setPosition (
			Constants.dialog.PAUSE_DIALOG_SOUNDS_POSITION_X,
			Constants.dialog.PAUSE_DIALOG_SOUNDS_POSITION_Y
		);
		stage.addActor(this.buttonSounds);

		// Music Button------------------------------------------------
		String musicTexture = Autickax.settings.playMusic ? Constants.menu.BUTTON_MENU_MUSIC_ON
				: Constants.menu.BUTTON_MENU_MUSIC_OFF;
		String musicHoverTexture = Autickax.settings.playMusic ? Constants.menu.BUTTON_MENU_MUSIC_ON_HOVER
				: Constants.menu.BUTTON_MENU_MUSIC_OFF_HOVER;

		this.buttonMusic = new MenuButton(
				Autickax.getInstance().assets.getGraphics(musicTexture),
				Autickax.getInstance().assets.getGraphics(musicHoverTexture)) {
			@Override
			public void action() {

				Autickax.settings.playMusic = !Autickax.settings.playMusic;

				if (Autickax.settings.playMusic) {
					buttonMusic
							.setStyle(
									Autickax.getInstance().assets
											.getGraphics(Constants.menu.BUTTON_MENU_MUSIC_ON),
									Autickax.getInstance().assets
											.getGraphics(Constants.menu.BUTTON_MENU_MUSIC_ON_HOVER));
					Autickax.getInstance().assets.soundAndMusicManager
							.playRaceMusic();
				} else {
					buttonMusic
							.setStyle(
									Autickax.getInstance().assets
											.getGraphics(Constants.menu.BUTTON_MENU_MUSIC_OFF),
									Autickax.getInstance().assets
											.getGraphics(Constants.menu.BUTTON_MENU_MUSIC_OFF_HOVER));
					Autickax.getInstance().assets.soundAndMusicManager
							.stopAllMusic();
				}
			}
		};
		this.buttonMusic.setPosition (
			Constants.dialog.PAUSE_DIALOG_MUSIC_POSITION_X,
			Constants.dialog.PAUSE_DIALOG_MUSIC_POSITION_Y
		);
		stage.addActor(this.buttonMusic);

	}

}
