package cz.cuni.mff.xcars.scene;

import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

import cz.cuni.mff.xcars.Xcars;
import cz.cuni.mff.xcars.Difficulty;
import cz.cuni.mff.xcars.constants.Constants;
import cz.cuni.mff.xcars.screenObjects.ScreenAdaptiveTextButton;

public class DifficultySelectScreen extends BaseScreen {

	private TextButton buttonBeginner;
	private TextButton buttonNormal;
	private TextButton buttonHard;

	private final float buttonsXPositionStart = 15;

	public DifficultySelectScreen() {
		super();
		if(Xcars.adsHandler != null){
			Xcars.adsHandler.showBanner(true);
		}

		// Background
		Image background = new Image(
				Xcars.getInstance().assets
						.getGraphics(Constants.menu.DIFFICULTY_MENU_BACKGROUND));
		background.setSize(this.stageWidth, this.stageHeight);
		stage.addActor(background); // stage initialized in superclass
									// constructor

		// Beginner Button-----------------------------------------------------
		this.buttonBeginner = new ScreenAdaptiveTextButton("EASY",
				Xcars.getInstance().assets.getGraphics(Constants.menu.BUTTON_MENU_DIFFICULTY),
				Xcars.getInstance().assets
						.getGraphics(Constants.menu.BUTTON_MENU_DIFFICULTY_HOVER)) {
			@Override
			public void action() {
				Xcars.getInstance().assets.soundAndMusicManager.playSound(
						Constants.sounds.SOUND_MENU_OPEN, Constants.sounds.SOUND_DEFAULT_VOLUME);
				if (Xcars.levelSelectScreen != null) {
					Xcars.levelSelectScreen.dispose();
					Xcars.levelSelectScreen = null;
				}
				Xcars.levelSelectScreen = new LevelSelectScreen(Difficulty.Beginner);
				Xcars.getInstance().setScreen(Xcars.levelSelectScreen);
			}
		};
		this.buttonBeginner.setPosition(this.buttonsXPositionStart, 350);
		stage.addActor(this.buttonBeginner);

		// Normal Button-----------------------------------------------------
		this.buttonNormal = new ScreenAdaptiveTextButton("MEDIUM",
				Xcars.getInstance().assets.getGraphics(Constants.menu.BUTTON_MENU_DIFFICULTY),
				Xcars.getInstance().assets
						.getGraphics(Constants.menu.BUTTON_MENU_DIFFICULTY_HOVER)) {
			@Override
			public void action() {
				Xcars.getInstance().assets.soundAndMusicManager.playSound(
						Constants.sounds.SOUND_MENU_OPEN, Constants.sounds.SOUND_DEFAULT_VOLUME);
				if (Xcars.levelSelectScreen != null) {
					Xcars.levelSelectScreen.dispose();
					Xcars.levelSelectScreen = null;
				}
				Xcars.levelSelectScreen = new LevelSelectScreen(Difficulty.Normal);
				Xcars.getInstance().setScreen(Xcars.levelSelectScreen);
			}
		};
		this.buttonNormal.setPosition(this.buttonsXPositionStart, 250);
		stage.addActor(this.buttonNormal);

		// Hard Button-----------------------------------------------------
		this.buttonHard = new ScreenAdaptiveTextButton("HARD",
				Xcars.getInstance().assets.getGraphics(Constants.menu.BUTTON_MENU_DIFFICULTY),
				Xcars.getInstance().assets
						.getGraphics(Constants.menu.BUTTON_MENU_DIFFICULTY_HOVER)) {
			@Override
			public void action() {
				Xcars.getInstance().assets.soundAndMusicManager.playSound(
						Constants.sounds.SOUND_MENU_OPEN, Constants.sounds.SOUND_DEFAULT_VOLUME);
				if (Xcars.levelSelectScreen != null) {
					Xcars.levelSelectScreen.dispose();
					Xcars.levelSelectScreen = null;
				}
				Xcars.levelSelectScreen = new LevelSelectScreen(Difficulty.Hard);
				Xcars.getInstance().setScreen(Xcars.levelSelectScreen);
			}
		};
		this.buttonHard.setPosition(this.buttonsXPositionStart, 150);
		stage.addActor(this.buttonHard);
	}

	@Override
	protected void onBackKeyPressed() {
		Xcars.getInstance().assets.soundAndMusicManager.playSound(
				Constants.sounds.SOUND_MENU_CLOSE, Constants.sounds.SOUND_DEFAULT_VOLUME);
		Xcars.mainMenuScreen.dispose();
		Xcars.mainMenuScreen = new MainMenuScreen();
		Xcars.getInstance().setScreen(Xcars.mainMenuScreen);
	}

}
