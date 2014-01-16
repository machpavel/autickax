package cz.mff.cuni.autickax.scene;

import com.badlogic.gdx.Gdx;
/*import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;*/
import com.badlogic.gdx.scenes.scene2d.ui.Image;

import cz.mff.cuni.autickax.Autickax;
import cz.mff.cuni.autickax.Constants;
//import cz.mff.cuni.autickax.menu.MenuAnimator;
import cz.mff.cuni.autickax.menu.MenuButton;

/*
 * This is the "Main menu" screen in the game, only contains a play button
 */
public class MainMenuScreen extends BaseScreen {

	private MenuButton buttonPlay;
	private MenuButton buttonExit;
	private MenuButton buttonTooltips;
	private MenuButton buttonSounds;
	private MenuButton buttonMusic;

	public MainMenuScreen() {
		super();
		
		this.playMenuMusic();

		// Background
		Image background = new Image(getGame().assets.getGraphics(Constants.MAIN_MENU_BACKGROUND));
		background.setSize(stageWidth, stageHeight);
		stage.addActor(background); // stage initialized in superclass
									// constructor

		// Play Button-----------------------------------------------------
		this.buttonPlay = new MenuButton (
			getGame().assets.getGraphics(Constants.BUTTON_MENU_PLAY),
			getGame().assets.getGraphics(Constants.BUTTON_MENU_PLAY_HOVER)
		)
		{
			@Override
			public void action() {
				getGame().assets.soundAndMusicManager.playSound(Constants.SOUND_MENU_OPEN, Constants.SOUND_DEFAULT_VOLUME);
				if (Autickax.difficultySelectScreen != null) {
					Autickax.difficultySelectScreen.dispose();
					Autickax.difficultySelectScreen = null;
				}
				Autickax.difficultySelectScreen = new DifficultySelectScreen();
				getGame().setScreen(Autickax.difficultySelectScreen);
			}
		};
		this.buttonPlay.setPosition(170, 190);
		stage.addActor(this.buttonPlay);
		
		// Exit Button-----------------------------------------------------
		this.buttonExit = new MenuButton (
			getGame().assets.getGraphics(Constants.BUTTON_MENU_EXIT),
			getGame().assets.getGraphics(Constants.BUTTON_MENU_EXIT_HOVER)
		)
		{
			@Override
			public void action() {
				onBackKeyPressed();
			}
		};
		this.buttonExit.setPosition(680, 60);
		stage.addActor(this.buttonExit);
		
		// Tooltips Button------------------------------------------------
		String tooltipsTexture = Autickax.settings.showTooltips ?
				Constants.BUTTON_MENU_TOOLTIPS_ON : Constants.BUTTON_MENU_TOOLTIPS_OFF;
		String tooltipsHoverTexture = Autickax.settings.showTooltips ?
				Constants.BUTTON_MENU_TOOLTIPS_ON_HOVER : Constants.BUTTON_MENU_TOOLTIPS_OFF_HOVER;
		
		this.buttonTooltips = new MenuButton (
			getGame().assets.getGraphics(tooltipsTexture),
			getGame().assets.getGraphics(tooltipsHoverTexture)
		)
		{
			@Override
			public void action() {
				
				Autickax.settings.showTooltips = !Autickax.settings.showTooltips; 
				
				if (Autickax.settings.showTooltips) {
					buttonTooltips.setStyle(
							getGame().assets.getGraphics(Constants.BUTTON_MENU_TOOLTIPS_ON),
							getGame().assets.getGraphics(Constants.BUTTON_MENU_TOOLTIPS_ON_HOVER)
					);
				}
				else {
					buttonTooltips.setStyle(
							getGame().assets.getGraphics(Constants.BUTTON_MENU_TOOLTIPS_OFF),
							getGame().assets.getGraphics(Constants.BUTTON_MENU_TOOLTIPS_OFF_HOVER)
					);
				}
			}
		};
		this.buttonTooltips.setPosition(-10, 340);
		stage.addActor(this.buttonTooltips);
		
		// Sounds Button------------------------------------------------
		String soundsTexture = Autickax.settings.playSounds ?
				Constants.BUTTON_MENU_SOUNDS_ON : Constants.BUTTON_MENU_SOUNDS_OFF;
		String soundsHoverTexture = Autickax.settings.playSounds ?
				Constants.BUTTON_MENU_SOUNDS_ON_HOVER : Constants.BUTTON_MENU_SOUNDS_OFF_HOVER;
		
		this.buttonSounds = new MenuButton (
			getGame().assets.getGraphics(soundsTexture),
			getGame().assets.getGraphics(soundsHoverTexture)
		)
		{
			@Override
			public void action() {
				
				Autickax.settings.playSounds = !Autickax.settings.playSounds; 
				
				if (Autickax.settings.playSounds) {
					buttonSounds.setStyle(
							getGame().assets.getGraphics(Constants.BUTTON_MENU_SOUNDS_ON),
							getGame().assets.getGraphics(Constants.BUTTON_MENU_SOUNDS_ON_HOVER)
					);
				}
				else {
					buttonSounds.setStyle(
							getGame().assets.getGraphics(Constants.BUTTON_MENU_SOUNDS_OFF),
							getGame().assets.getGraphics(Constants.BUTTON_MENU_SOUNDS_OFF_HOVER)
					);
				}
			}
		};
		this.buttonSounds.setPosition(260, 365);
		stage.addActor(this.buttonSounds);
		
		// Music Button------------------------------------------------
		String musicTexture = Autickax.settings.playMusic ?
				Constants.BUTTON_MENU_MUSIC_ON : Constants.BUTTON_MENU_MUSIC_OFF;
		String musicHoverTexture = Autickax.settings.playMusic ?
				Constants.BUTTON_MENU_MUSIC_ON_HOVER : Constants.BUTTON_MENU_MUSIC_OFF_HOVER;
		
		this.buttonMusic = new MenuButton (
			getGame().assets.getGraphics(musicTexture),
			getGame().assets.getGraphics(musicHoverTexture)
		)
		{
			@Override
			public void action() {
				
				Autickax.settings.playMusic = !Autickax.settings.playMusic; 
				
				if (Autickax.settings.playMusic) {
					buttonMusic.setStyle(
							getGame().assets.getGraphics(Constants.BUTTON_MENU_MUSIC_ON),
							getGame().assets.getGraphics(Constants.BUTTON_MENU_MUSIC_ON_HOVER)
					);
					getGame().assets.soundAndMusicManager.playMenuMusic();
				}
				else {
					buttonMusic.setStyle(
							getGame().assets.getGraphics(Constants.BUTTON_MENU_MUSIC_OFF),
							getGame().assets.getGraphics(Constants.BUTTON_MENU_MUSIC_OFF_HOVER)
					);
					getGame().assets.soundAndMusicManager.stopAllMusic();
				}
			}
		};
		this.buttonMusic.setPosition(660, 365);
		stage.addActor(this.buttonMusic);

		// Actions
		/*buttonPlay.addAction(MenuAnimator.moveTo(this.buttonsXPositionEnd, 250, 3,
				Interpolation.elasticOut));*/
	}
	
	private void playMenuMusic()
	{
		getGame().assets.soundAndMusicManager.playMenuMusic();
	}


	@Override
	public void render(float delta) {
		super.render(delta);
	}

	@Override
	protected void onBackKeyPressed() {
		Gdx.app.exit();		
	}

}
