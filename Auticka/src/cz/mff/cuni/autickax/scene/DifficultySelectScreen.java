package cz.mff.cuni.autickax.scene;

import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

import cz.mff.cuni.autickax.Autickax;
import cz.mff.cuni.autickax.Difficulty;
import cz.mff.cuni.autickax.constants.Constants;
import cz.mff.cuni.autickax.screenObjects.ScreenAdaptiveTextButton;

public class DifficultySelectScreen extends BaseScreen {

	private TextButton buttonBeginner;
	private TextButton buttonNormal;
	private TextButton buttonHard;

	private final float buttonsXPositionStart = 15;

	public DifficultySelectScreen() {
		super();

		// Background
		Image background = new Image(
				Autickax.getInstance().assets
						.getGraphics(Constants.menu.DIFFICULTY_MENU_BACKGROUND));
		background.setSize(this.stageWidth, this.stageHeight);
		stage.addActor(background); // stage initialized in superclass
									// constructor

		// Beginner Button-----------------------------------------------------
		this.buttonBeginner = new ScreenAdaptiveTextButton("EASY",
				Autickax.getInstance().assets.getGraphics(Constants.menu.BUTTON_MENU_DIFFICULTY),
				Autickax.getInstance().assets
						.getGraphics(Constants.menu.BUTTON_MENU_DIFFICULTY_HOVER)) {
			@Override
			public void action() {
				Autickax.getInstance().assets.soundAndMusicManager.playSound(
						Constants.sounds.SOUND_MENU_OPEN, Constants.sounds.SOUND_DEFAULT_VOLUME);
				if (Autickax.levelSelectScreen != null) {
					Autickax.levelSelectScreen.dispose();
					Autickax.levelSelectScreen = null;
				}
				Autickax.levelSelectScreen = new LevelSelectScreen(Difficulty.Beginner);
				Autickax.getInstance().setScreen(Autickax.levelSelectScreen);
			}
		};
		this.buttonBeginner.setPosition(this.buttonsXPositionStart, 350);
		stage.addActor(this.buttonBeginner);

		// Normal Button-----------------------------------------------------
		this.buttonNormal = new ScreenAdaptiveTextButton("MEDIUM",
				Autickax.getInstance().assets.getGraphics(Constants.menu.BUTTON_MENU_DIFFICULTY),
				Autickax.getInstance().assets
						.getGraphics(Constants.menu.BUTTON_MENU_DIFFICULTY_HOVER)) {
			@Override
			public void action() {
				Autickax.getInstance().assets.soundAndMusicManager.playSound(
						Constants.sounds.SOUND_MENU_OPEN, Constants.sounds.SOUND_DEFAULT_VOLUME);
				if (Autickax.levelSelectScreen != null) {
					Autickax.levelSelectScreen.dispose();
					Autickax.levelSelectScreen = null;
				}
				Autickax.levelSelectScreen = new LevelSelectScreen(Difficulty.Normal);
				Autickax.getInstance().setScreen(Autickax.levelSelectScreen);
			}
		};
		this.buttonNormal.setPosition(this.buttonsXPositionStart, 250);
		stage.addActor(this.buttonNormal);

		// Hard Button-----------------------------------------------------
		this.buttonHard = new ScreenAdaptiveTextButton("HARD",
				Autickax.getInstance().assets.getGraphics(Constants.menu.BUTTON_MENU_DIFFICULTY),
				Autickax.getInstance().assets
						.getGraphics(Constants.menu.BUTTON_MENU_DIFFICULTY_HOVER)) {
			@Override
			public void action() {
				Autickax.getInstance().assets.soundAndMusicManager.playSound(
						Constants.sounds.SOUND_MENU_OPEN, Constants.sounds.SOUND_DEFAULT_VOLUME);
				if (Autickax.levelSelectScreen != null) {
					Autickax.levelSelectScreen.dispose();
					Autickax.levelSelectScreen = null;
				}
				Autickax.levelSelectScreen = new LevelSelectScreen(Difficulty.Hard);
				Autickax.getInstance().setScreen(Autickax.levelSelectScreen);
			}
		};
		this.buttonHard.setPosition(this.buttonsXPositionStart, 150);
		stage.addActor(this.buttonHard);
	}

	@Override
	protected void onBackKeyPressed() {
		Autickax.getInstance().assets.soundAndMusicManager.playSound(
				Constants.sounds.SOUND_MENU_CLOSE, Constants.sounds.SOUND_DEFAULT_VOLUME);
		Autickax.mainMenuScreen.dispose();
		Autickax.mainMenuScreen = new MainMenuScreen();
		Autickax.getInstance().setScreen(Autickax.mainMenuScreen);
	}

}
