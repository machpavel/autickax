package cz.cuni.mff.xcars.dialogs;

import cz.cuni.mff.xcars.Xcars;
import cz.cuni.mff.xcars.constants.Constants;
import cz.cuni.mff.xcars.gamelogic.SubLevel;
import cz.cuni.mff.xcars.scene.GameScreen;
import cz.cuni.mff.xcars.screenObjects.ScreenAdaptiveButton;

public class PauseDialog extends DecisionDialog {

	private ScreenAdaptiveButton buttonTooltips;
	private ScreenAdaptiveButton buttonSounds;
	private ScreenAdaptiveButton buttonMusic;

	public PauseDialog(GameScreen gameScreen, SubLevel subLevel) {
		super(gameScreen, subLevel, Constants.strings.GAME_PAUSED, true);

		this.decision = DecisionType.CONTINUE;

		// Tooltips Button------------------------------------------------
		String tooltipsTexture = Xcars.settings.isShowTooltips() ? Constants.menu.BUTTON_MENU_TOOLTIPS_ON
				: Constants.menu.BUTTON_MENU_TOOLTIPS_OFF;
		String tooltipsHoverTexture = Xcars.settings.isShowTooltips() ? Constants.menu.BUTTON_MENU_TOOLTIPS_ON_HOVER
				: Constants.menu.BUTTON_MENU_TOOLTIPS_OFF_HOVER;

		this.buttonTooltips = new ScreenAdaptiveButton(
				Xcars.getInstance().assets.getGraphics(tooltipsTexture),
				Xcars.getInstance().assets.getGraphics(tooltipsHoverTexture)) {
			@Override
			public void action() {

				Xcars.settings.setShowTooltips(!Xcars.settings.isShowTooltips());

				if (Xcars.settings.isShowTooltips()) {
					buttonTooltips.setStyle(Xcars.getInstance().assets
							.getGraphics(Constants.menu.BUTTON_MENU_TOOLTIPS_ON), Xcars
							.getInstance().assets
							.getGraphics(Constants.menu.BUTTON_MENU_TOOLTIPS_ON_HOVER));
				} else {
					buttonTooltips.setStyle(Xcars.getInstance().assets
							.getGraphics(Constants.menu.BUTTON_MENU_TOOLTIPS_OFF), Xcars
							.getInstance().assets
							.getGraphics(Constants.menu.BUTTON_MENU_TOOLTIPS_OFF_HOVER));
				}
			}
		};
		this.buttonTooltips.setPosition(Constants.dialog.PAUSE_DIALOG_TOOLTIPS_POSITION_X,
				Constants.dialog.PAUSE_DIALOG_TOOLTIPS_POSITION_Y);
		stage.addActor(this.buttonTooltips);

		// Sounds Button------------------------------------------------
		String soundsTexture = Xcars.settings.isPlaySounds() ? Constants.menu.BUTTON_MENU_SOUNDS_ON
				: Constants.menu.BUTTON_MENU_SOUNDS_OFF;
		String soundsHoverTexture = Xcars.settings.isPlaySounds() ? Constants.menu.BUTTON_MENU_SOUNDS_ON_HOVER
				: Constants.menu.BUTTON_MENU_SOUNDS_OFF_HOVER;

		this.buttonSounds = new ScreenAdaptiveButton(
				Xcars.getInstance().assets.getGraphics(soundsTexture),
				Xcars.getInstance().assets.getGraphics(soundsHoverTexture)) {
			@Override
			public void action() {
				Xcars.settings.setPlaySounds(!Xcars.settings.isPlaySounds());

				if (Xcars.settings.isPlaySounds()) {
					buttonSounds.setStyle(Xcars.getInstance().assets
							.getGraphics(Constants.menu.BUTTON_MENU_SOUNDS_ON), Xcars
							.getInstance().assets
							.getGraphics(Constants.menu.BUTTON_MENU_SOUNDS_ON_HOVER));
				} else {
					buttonSounds.setStyle(Xcars.getInstance().assets
							.getGraphics(Constants.menu.BUTTON_MENU_SOUNDS_OFF), Xcars
							.getInstance().assets
							.getGraphics(Constants.menu.BUTTON_MENU_SOUNDS_OFF_HOVER));
				}
			}
		};
		this.buttonSounds.setPosition(Constants.dialog.PAUSE_DIALOG_SOUNDS_POSITION_X,
				Constants.dialog.PAUSE_DIALOG_SOUNDS_POSITION_Y);
		stage.addActor(this.buttonSounds);

		// Music Button------------------------------------------------
		String musicTexture = Xcars.settings.isPlayMusic() ? Constants.menu.BUTTON_MENU_MUSIC_ON
				: Constants.menu.BUTTON_MENU_MUSIC_OFF;
		String musicHoverTexture = Xcars.settings.isPlayMusic() ? Constants.menu.BUTTON_MENU_MUSIC_ON_HOVER
				: Constants.menu.BUTTON_MENU_MUSIC_OFF_HOVER;

		this.buttonMusic = new ScreenAdaptiveButton(
				Xcars.getInstance().assets.getGraphics(musicTexture),
				Xcars.getInstance().assets.getGraphics(musicHoverTexture)) {
			@Override
			public void action() {

				Xcars.settings.setPlayMusic(!Xcars.settings.isPlayMusic());

				if (Xcars.settings.isPlayMusic()) {
					buttonMusic.setStyle(Xcars.getInstance().assets
							.getGraphics(Constants.menu.BUTTON_MENU_MUSIC_ON), Xcars
							.getInstance().assets
							.getGraphics(Constants.menu.BUTTON_MENU_MUSIC_ON_HOVER));
					Xcars.getInstance().assets.soundAndMusicManager.playRaceMusic();
				} else {
					buttonMusic.setStyle(Xcars.getInstance().assets
							.getGraphics(Constants.menu.BUTTON_MENU_MUSIC_OFF), Xcars
							.getInstance().assets
							.getGraphics(Constants.menu.BUTTON_MENU_MUSIC_OFF_HOVER));
					Xcars.getInstance().assets.soundAndMusicManager.pauseAllMusic();
				}
			}
		};
		this.buttonMusic.setPosition(Constants.dialog.PAUSE_DIALOG_MUSIC_POSITION_X,
				Constants.dialog.PAUSE_DIALOG_MUSIC_POSITION_Y);
		stage.addActor(this.buttonMusic);

	}

}
