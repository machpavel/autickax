package cz.cuni.mff.xcars.scene;

import com.badlogic.gdx.Gdx;
/*import com.badlogic.gdx.math.Interpolation;
 import com.badlogic.gdx.scenes.scene2d.actions.Actions;*/
import com.badlogic.gdx.scenes.scene2d.ui.Image;


import cz.cuni.mff.xcars.PlayedLevels;
//import cz.cuni.mff.xcars.menu.MenuAnimator;
import cz.cuni.mff.xcars.Xcars;
import cz.cuni.mff.xcars.constants.Constants;
import cz.cuni.mff.xcars.screenObjects.ScreenAdaptiveButton;

/*
 * This is the "Main menu" screen in the game, only contains a play button
 */
public class MainMenuScreen extends BaseScreen {

	private ScreenAdaptiveButton buttonPlay;
	private ScreenAdaptiveButton buttonExit;
	private ScreenAdaptiveButton buttonTooltips;
	private ScreenAdaptiveButton buttonSounds;
	private ScreenAdaptiveButton buttonMusic;

	public MainMenuScreen() {
		super();
		if(Xcars.adsHandler != null){
			Xcars.adsHandler.showBanner(true);
		}
		
		Xcars.availableLevels = Xcars.getInstance().assets.getAvailableLevels();

		Xcars.playedLevels = new PlayedLevels();
		Xcars.playedLevels.loadLevels();

		this.playMenuMusic();

		// Background
		Image background = new Image(
				Xcars.getInstance().assets.getGraphics(Constants.menu.MAIN_MENU_BACKGROUND));
		background.setSize(stageWidth, stageHeight);
		stage.addActor(background); // stage initialized in superclass
									// constructor

		// Play Button-----------------------------------------------------
		this.buttonPlay = new ScreenAdaptiveButton(
				Xcars.getInstance().assets.getGraphics(Constants.menu.BUTTON_MENU_PLAY),
				Xcars.getInstance().assets.getGraphics(Constants.menu.BUTTON_MENU_PLAY_HOVER)) {
			@Override
			public void action() {
				Xcars.getInstance().assets.soundAndMusicManager.playSound(
						Constants.sounds.SOUND_MENU_OPEN, Constants.sounds.SOUND_DEFAULT_VOLUME);
				if (Xcars.difficultySelectScreen != null) {
					Xcars.difficultySelectScreen.dispose();
					Xcars.difficultySelectScreen = null;
				}
				Xcars.difficultySelectScreen = new ScenarioSelectScreen();
				Xcars.getInstance().setScreen(Xcars.difficultySelectScreen);
			}
		};
		this.buttonPlay.setPosition(170, 190);
		stage.addActor(this.buttonPlay);

		// Exit Button-----------------------------------------------------
		this.buttonExit = new ScreenAdaptiveButton(
				Xcars.getInstance().assets.getGraphics(Constants.menu.BUTTON_MENU_EXIT),
				Xcars.getInstance().assets.getGraphics(Constants.menu.BUTTON_MENU_EXIT_HOVER)) {
			@Override
			public void action() {
				onBackKeyPressed();
			}
		};
		this.buttonExit.setPosition(680, 60);
		stage.addActor(this.buttonExit);

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
		this.buttonTooltips.setPosition(-10, 340);
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
		this.buttonSounds.setPosition(260, 365);
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
					Xcars.getInstance().assets.soundAndMusicManager.playMenuMusic();
				} else {
					buttonMusic.setStyle(Xcars.getInstance().assets
							.getGraphics(Constants.menu.BUTTON_MENU_MUSIC_OFF), Xcars
							.getInstance().assets
							.getGraphics(Constants.menu.BUTTON_MENU_MUSIC_OFF_HOVER));
					Xcars.getInstance().assets.soundAndMusicManager.pauseAllMusic();
				}
			}
		};
		this.buttonMusic.setPosition(660, 365);
		stage.addActor(this.buttonMusic);

		// Actions
		/*
		 * buttonPlay.addAction(MenuAnimator.moveTo(this.buttonsXPositionEnd,
		 * 250, 3, Interpolation.elasticOut));
		 */
	}

	private void playMenuMusic() {
		Xcars.getInstance().assets.soundAndMusicManager.playMenuMusic();
	}

	@Override
	protected void onBackKeyPressed() {
		Xcars.settings.storeSettings();
		Xcars.playedLevels.storeLevels();
		Gdx.app.exit();
	}

}
