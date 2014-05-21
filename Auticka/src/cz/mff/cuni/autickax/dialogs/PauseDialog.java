package cz.mff.cuni.autickax.dialogs;

import cz.mff.cuni.autickax.Autickax;
import cz.mff.cuni.autickax.constants.Constants;
import cz.mff.cuni.autickax.gamelogic.SubLevel;
import cz.mff.cuni.autickax.scene.GameScreen;
import cz.mff.cuni.autickax.screenObjects.ScreenAdaptiveButton;

public class PauseDialog extends DecisionDialog {

	private ScreenAdaptiveButton buttonTooltips;
	private ScreenAdaptiveButton buttonSounds;
	private ScreenAdaptiveButton buttonMusic;

	public PauseDialog(GameScreen gameScreen, SubLevel subLevel) {
		super(gameScreen, subLevel, Constants.strings.GAME_PAUSED, true);

		this.decision = DecisionType.CONTINUE;

		// Tooltips Button------------------------------------------------
		String tooltipsTexture = Autickax.settings.isShowTooltips() ? Constants.menu.BUTTON_MENU_TOOLTIPS_ON
				: Constants.menu.BUTTON_MENU_TOOLTIPS_OFF;
		String tooltipsHoverTexture = Autickax.settings.isShowTooltips() ? Constants.menu.BUTTON_MENU_TOOLTIPS_ON_HOVER
				: Constants.menu.BUTTON_MENU_TOOLTIPS_OFF_HOVER;

		this.buttonTooltips = new ScreenAdaptiveButton(
				Autickax.getInstance().assets.getGraphics(tooltipsTexture),
				Autickax.getInstance().assets.getGraphics(tooltipsHoverTexture)) {
			@Override
			public void action() {

				Autickax.settings.setShowTooltips(!Autickax.settings.isShowTooltips());

				if (Autickax.settings.isShowTooltips()) {
					buttonTooltips.setStyle(Autickax.getInstance().assets
							.getGraphics(Constants.menu.BUTTON_MENU_TOOLTIPS_ON), Autickax
							.getInstance().assets
							.getGraphics(Constants.menu.BUTTON_MENU_TOOLTIPS_ON_HOVER));
				} else {
					buttonTooltips.setStyle(Autickax.getInstance().assets
							.getGraphics(Constants.menu.BUTTON_MENU_TOOLTIPS_OFF), Autickax
							.getInstance().assets
							.getGraphics(Constants.menu.BUTTON_MENU_TOOLTIPS_OFF_HOVER));
				}
			}
		};
		this.buttonTooltips.setPosition(Constants.dialog.PAUSE_DIALOG_TOOLTIPS_POSITION_X,
				Constants.dialog.PAUSE_DIALOG_TOOLTIPS_POSITION_Y);
		stage.addActor(this.buttonTooltips);

		// Sounds Button------------------------------------------------
		String soundsTexture = Autickax.settings.isPlaySounds() ? Constants.menu.BUTTON_MENU_SOUNDS_ON
				: Constants.menu.BUTTON_MENU_SOUNDS_OFF;
		String soundsHoverTexture = Autickax.settings.isPlaySounds() ? Constants.menu.BUTTON_MENU_SOUNDS_ON_HOVER
				: Constants.menu.BUTTON_MENU_SOUNDS_OFF_HOVER;

		this.buttonSounds = new ScreenAdaptiveButton(
				Autickax.getInstance().assets.getGraphics(soundsTexture),
				Autickax.getInstance().assets.getGraphics(soundsHoverTexture)) {
			@Override
			public void action() {
				Autickax.settings.setPlaySounds(!Autickax.settings.isPlaySounds());

				if (Autickax.settings.isPlaySounds()) {
					buttonSounds.setStyle(Autickax.getInstance().assets
							.getGraphics(Constants.menu.BUTTON_MENU_SOUNDS_ON), Autickax
							.getInstance().assets
							.getGraphics(Constants.menu.BUTTON_MENU_SOUNDS_ON_HOVER));
				} else {
					buttonSounds.setStyle(Autickax.getInstance().assets
							.getGraphics(Constants.menu.BUTTON_MENU_SOUNDS_OFF), Autickax
							.getInstance().assets
							.getGraphics(Constants.menu.BUTTON_MENU_SOUNDS_OFF_HOVER));
				}
			}
		};
		this.buttonSounds.setPosition(Constants.dialog.PAUSE_DIALOG_SOUNDS_POSITION_X,
				Constants.dialog.PAUSE_DIALOG_SOUNDS_POSITION_Y);
		stage.addActor(this.buttonSounds);

		// Music Button------------------------------------------------
		String musicTexture = Autickax.settings.isPlayMusic() ? Constants.menu.BUTTON_MENU_MUSIC_ON
				: Constants.menu.BUTTON_MENU_MUSIC_OFF;
		String musicHoverTexture = Autickax.settings.isPlayMusic() ? Constants.menu.BUTTON_MENU_MUSIC_ON_HOVER
				: Constants.menu.BUTTON_MENU_MUSIC_OFF_HOVER;

		this.buttonMusic = new ScreenAdaptiveButton(
				Autickax.getInstance().assets.getGraphics(musicTexture),
				Autickax.getInstance().assets.getGraphics(musicHoverTexture)) {
			@Override
			public void action() {

				Autickax.settings.setPlayMusic(!Autickax.settings.isPlayMusic());

				if (Autickax.settings.isPlayMusic()) {
					buttonMusic.setStyle(Autickax.getInstance().assets
							.getGraphics(Constants.menu.BUTTON_MENU_MUSIC_ON), Autickax
							.getInstance().assets
							.getGraphics(Constants.menu.BUTTON_MENU_MUSIC_ON_HOVER));
					Autickax.getInstance().assets.soundAndMusicManager.playRaceMusic();
				} else {
					buttonMusic.setStyle(Autickax.getInstance().assets
							.getGraphics(Constants.menu.BUTTON_MENU_MUSIC_OFF), Autickax
							.getInstance().assets
							.getGraphics(Constants.menu.BUTTON_MENU_MUSIC_OFF_HOVER));
					Autickax.getInstance().assets.soundAndMusicManager.pauseAllMusic();
				}
			}
		};
		this.buttonMusic.setPosition(Constants.dialog.PAUSE_DIALOG_MUSIC_POSITION_X,
				Constants.dialog.PAUSE_DIALOG_MUSIC_POSITION_Y);
		stage.addActor(this.buttonMusic);

	}

}
