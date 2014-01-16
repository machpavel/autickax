package cz.mff.cuni.autickax.scene;

import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

import cz.mff.cuni.autickax.Autickax;
import cz.mff.cuni.autickax.Constants;
import cz.mff.cuni.autickax.Difficulty;
import cz.mff.cuni.autickax.menu.MenuTextButton;

public class DifficultySelectScreen extends BaseScreen {

	private TextButton buttonBeginner;
	private TextButton buttonNormal;
	private TextButton buttonHard;
	
	private final float buttonsXPositionStart = 15;
	
	public DifficultySelectScreen() {
		super();

		// Background
		Image background = new Image(getGame().assets.getGraphics(Constants.DIFFICULTY_MENU_BACKGROUND));
		background.setSize(stageWidth, stageHeight);
		stage.addActor(background); // stage initialized in superclass
									// constructor
		
		// Beginner Button-----------------------------------------------------
		this.buttonBeginner = new MenuTextButton (
			"easy",
			getGame().assets.getGraphics(Constants.BUTTON_MENU_DIFFICULTY),
			getGame().assets.getGraphics(Constants.BUTTON_MENU_DIFFICULTY_HOVER)
		)
		{
			@Override
			public void action() {
				getGame().assets.soundAndMusicManager.playSound(Constants.SOUND_MENU_OPEN, Constants.SOUND_DEFAULT_VOLUME);
				if (Autickax.levelSelectScreen != null) {
					Autickax.levelSelectScreen.dispose();
					Autickax.levelSelectScreen = null;
				}
				Autickax.levelSelectScreen = new LevelSelectScreen(Difficulty.Beginner);
				getGame().setScreen(Autickax.levelSelectScreen);
			}
		};
		this.buttonBeginner.setPosition(this.buttonsXPositionStart, 350);
		stage.addActor(this.buttonBeginner);
		
		// Normal Button-----------------------------------------------------
		this.buttonNormal = new MenuTextButton (
			"medium",
			getGame().assets.getGraphics(Constants.BUTTON_MENU_DIFFICULTY),
			getGame().assets.getGraphics(Constants.BUTTON_MENU_DIFFICULTY_HOVER)
		)
		{
			@Override
			public void action() {
				getGame().assets.soundAndMusicManager.playSound(Constants.SOUND_MENU_OPEN, Constants.SOUND_DEFAULT_VOLUME);
				if (Autickax.levelSelectScreen != null) {
					Autickax.levelSelectScreen.dispose();
					Autickax.levelSelectScreen = null;
				}
				Autickax.levelSelectScreen = new LevelSelectScreen(Difficulty.Normal);
				getGame().setScreen(Autickax.levelSelectScreen);
			}
		};
		this.buttonNormal.setPosition(this.buttonsXPositionStart, 250);
		stage.addActor(this.buttonNormal);
		
		// Hard Button-----------------------------------------------------
		this.buttonHard = new MenuTextButton (
			"hard",
			getGame().assets.getGraphics(Constants.BUTTON_MENU_DIFFICULTY),
			getGame().assets.getGraphics(Constants.BUTTON_MENU_DIFFICULTY_HOVER)
		)
		{
			@Override
			public void action() {
				getGame().assets.soundAndMusicManager.playSound(Constants.SOUND_MENU_OPEN, Constants.SOUND_DEFAULT_VOLUME);
				if (Autickax.levelSelectScreen != null) {
					Autickax.levelSelectScreen.dispose();
					Autickax.levelSelectScreen = null;
				}
				Autickax.levelSelectScreen = new LevelSelectScreen(Difficulty.Hard);
				getGame().setScreen(Autickax.levelSelectScreen);
			}
		};
		this.buttonHard.setPosition(this.buttonsXPositionStart, 150);
		stage.addActor(this.buttonHard);
	}
	
	@Override
	protected void onBackKeyPressed() {
		getGame().assets.soundAndMusicManager.playSound(Constants.SOUND_MENU_CLOSE, Constants.SOUND_DEFAULT_VOLUME);
		Autickax.mainMenuScreen.dispose();
		Autickax.mainMenuScreen = new MainMenuScreen();
		this.getGame().setScreen(Autickax.mainMenuScreen);		
	}

}
