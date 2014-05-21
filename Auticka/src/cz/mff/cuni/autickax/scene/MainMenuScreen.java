package cz.mff.cuni.autickax.scene;

import com.badlogic.gdx.Gdx;
/*import com.badlogic.gdx.math.Interpolation;
 import com.badlogic.gdx.scenes.scene2d.actions.Actions;*/
import com.badlogic.gdx.scenes.scene2d.ui.Image;

import cz.mff.cuni.autickax.Autickax;
import cz.mff.cuni.autickax.constants.Constants;
//import cz.mff.cuni.autickax.menu.MenuAnimator;
import cz.mff.cuni.autickax.screenObjects.ScreenAdaptiveButton;

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

		this.playMenuMusic();

		// Background
		Image background = new Image(
				Autickax.getInstance().assets.getGraphics(Constants.menu.MAIN_MENU_BACKGROUND));
		background.setSize(stageWidth, stageHeight);
		stage.addActor(background); // stage initialized in superclass
									// constructor

		// Play Button-----------------------------------------------------
		this.buttonPlay = new ScreenAdaptiveButton(
				Autickax.getInstance().assets.getGraphics(Constants.menu.BUTTON_MENU_PLAY),
				Autickax.getInstance().assets.getGraphics(Constants.menu.BUTTON_MENU_PLAY_HOVER)) {
			@Override
			public void action() {
				Autickax.getInstance().assets.soundAndMusicManager.playSound(
						Constants.sounds.SOUND_MENU_OPEN, Constants.sounds.SOUND_DEFAULT_VOLUME);
				if (Autickax.difficultySelectScreen != null) {
					Autickax.difficultySelectScreen.dispose();
					Autickax.difficultySelectScreen = null;
				}
				Autickax.difficultySelectScreen = new DifficultySelectScreen();
				Autickax.getInstance().setScreen(Autickax.difficultySelectScreen);
			}
		};
		this.buttonPlay.setPosition(170, 190);
		stage.addActor(this.buttonPlay);

		// Exit Button-----------------------------------------------------
		this.buttonExit = new ScreenAdaptiveButton(
				Autickax.getInstance().assets.getGraphics(Constants.menu.BUTTON_MENU_EXIT),
				Autickax.getInstance().assets.getGraphics(Constants.menu.BUTTON_MENU_EXIT_HOVER)) {
			@Override
			public void action() {
				onBackKeyPressed();
			}
		};
		this.buttonExit.setPosition(680, 60);
		stage.addActor(this.buttonExit);

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
		this.buttonTooltips.setPosition(-10, 340);
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
		this.buttonSounds.setPosition(260, 365);
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
					Autickax.getInstance().assets.soundAndMusicManager.playMenuMusic();
				} else {
					buttonMusic.setStyle(Autickax.getInstance().assets
							.getGraphics(Constants.menu.BUTTON_MENU_MUSIC_OFF), Autickax
							.getInstance().assets
							.getGraphics(Constants.menu.BUTTON_MENU_MUSIC_OFF_HOVER));
					Autickax.getInstance().assets.soundAndMusicManager.pauseAllMusic();
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
		Autickax.getInstance().assets.soundAndMusicManager.playMenuMusic();
	}

	@Override
	protected void onBackKeyPressed() {
		Autickax.settings.storeSettings();
		Autickax.playedLevels.storeLevels();
		Gdx.app.exit();
	}

}
