package cz.mff.cuni.autickax.scene;

import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

import cz.mff.cuni.autickax.Autickax;
import cz.mff.cuni.autickax.Constants;
import cz.mff.cuni.autickax.Difficulty;
import cz.mff.cuni.autickax.menu.MenuTextButton;

public class DifficultySelectScreen extends BaseScreen {

	private TextButton buttonKiddie;
	private TextButton buttonBeginner;
	private TextButton buttonNormal;
	private TextButton buttonHard;
	private TextButton buttonExtreme;	
	
	private final float buttonsXPositionStart = 30;
	
	public DifficultySelectScreen() {
		super();

		// Background
		Image background = new Image(getGame().assets.getGraphics(Constants.BUTTON_MENU_BACKGROUND));
		background.setSize(stageWidth, stageHeight);
		stage.addActor(background); // stage initialized in superclass
									// constructor

		// Kiddie Button-----------------------------------------------------
		this.buttonKiddie = new MenuTextButton (
			"dìcko",
			getGame().assets.getGraphics(Constants.BUTTON_MENU_DIFFICULTY),
			getGame().assets.getGraphics(Constants.BUTTON_MENU_DIFFICULTY_HOVER)
		)
		{
			@Override
			public void action() {
				if (Autickax.levelSelectScreen != null) {
					Autickax.levelSelectScreen.dispose();
					Autickax.levelSelectScreen = null;
				}
				Autickax.levelSelectScreen = new LevelSelectScreen(Difficulty.Kiddie);
				getGame().setScreen(Autickax.levelSelectScreen);
			}
		};
		this.buttonKiddie.setPosition(this.buttonsXPositionStart, 340);
		stage.addActor(this.buttonKiddie);
		
		// Beginner Button-----------------------------------------------------
		this.buttonBeginner = new MenuTextButton (
			"lehký",
			getGame().assets.getGraphics(Constants.BUTTON_MENU_DIFFICULTY),
			getGame().assets.getGraphics(Constants.BUTTON_MENU_DIFFICULTY_HOVER)
		)
		{
			@Override
			public void action() {
				if (Autickax.levelSelectScreen != null) {
					Autickax.levelSelectScreen.dispose();
					Autickax.levelSelectScreen = null;
				}
				Autickax.levelSelectScreen = new LevelSelectScreen(Difficulty.Beginner);
				getGame().setScreen(Autickax.levelSelectScreen);
			}
		};
		this.buttonBeginner.setPosition(this.buttonsXPositionStart, 260);
		stage.addActor(this.buttonBeginner);
		
		// Normal Button-----------------------------------------------------
		this.buttonNormal = new MenuTextButton (
			"normální",
			getGame().assets.getGraphics(Constants.BUTTON_MENU_DIFFICULTY),
			getGame().assets.getGraphics(Constants.BUTTON_MENU_DIFFICULTY_HOVER)
		)
		{
			@Override
			public void action() {
				if (Autickax.levelSelectScreen != null) {
					Autickax.levelSelectScreen.dispose();
					Autickax.levelSelectScreen = null;
				}
				Autickax.levelSelectScreen = new LevelSelectScreen(Difficulty.Normal);
				getGame().setScreen(Autickax.levelSelectScreen);
			}
		};
		this.buttonNormal.setPosition(this.buttonsXPositionStart, 180);
		stage.addActor(this.buttonNormal);
		
		// Hard Button-----------------------------------------------------
		this.buttonHard = new MenuTextButton (
			"tìžký",
			getGame().assets.getGraphics(Constants.BUTTON_MENU_DIFFICULTY),
			getGame().assets.getGraphics(Constants.BUTTON_MENU_DIFFICULTY_HOVER)
		)
		{
			@Override
			public void action() {
				if (Autickax.levelSelectScreen != null) {
					Autickax.levelSelectScreen.dispose();
					Autickax.levelSelectScreen = null;
				}
				Autickax.levelSelectScreen = new LevelSelectScreen(Difficulty.Hard);
				getGame().setScreen(Autickax.levelSelectScreen);
			}
		};
		this.buttonHard.setPosition(this.buttonsXPositionStart, 100);
		stage.addActor(this.buttonHard);
		
		// Extreme Button-----------------------------------------------------
		this.buttonExtreme = new MenuTextButton (
			"extrémní",
			getGame().assets.getGraphics(Constants.BUTTON_MENU_DIFFICULTY),
			getGame().assets.getGraphics(Constants.BUTTON_MENU_DIFFICULTY_HOVER)
		)
		{
			@Override
			public void action() {
				if (Autickax.levelSelectScreen != null) {
					Autickax.levelSelectScreen.dispose();
					Autickax.levelSelectScreen = null;
				}
				Autickax.levelSelectScreen = new LevelSelectScreen(Difficulty.Extreme);
				getGame().setScreen(Autickax.levelSelectScreen);
			}
		};
		this.buttonExtreme.setPosition(this.buttonsXPositionStart, 20);
		stage.addActor(this.buttonExtreme);
	}
	
	@Override
	protected void onBackKeyPressed() {
		Autickax.mainMenuScreen.dispose();
		Autickax.mainMenuScreen = new MainMenuScreen();
		this.getGame().setScreen(Autickax.mainMenuScreen);		
	}

}
