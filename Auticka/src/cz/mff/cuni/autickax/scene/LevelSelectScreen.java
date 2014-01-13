package cz.mff.cuni.autickax.scene;

import java.util.Vector;

import com.badlogic.gdx.scenes.scene2d.ui.Image;

import cz.mff.cuni.autickax.Autickax;
import cz.mff.cuni.autickax.Constants;
import cz.mff.cuni.autickax.Difficulty;
import cz.mff.cuni.autickax.LevelLoading;
import cz.mff.cuni.autickax.menu.MenuLabel;
import cz.mff.cuni.autickax.menu.MenuTextButton;

public class LevelSelectScreen extends BaseScreen {
	
	private final Difficulty difficulty;
	
	private static final int buttonsStartXPosition = 10;
	private static final int buttonsStartYPosition = 200;
	private static final int buttonsMaxXPosition = 750;
	private static final int buttonsXShift = 150;
	private static final int buttonsYShift = 150;
	
	public LevelSelectScreen(final Difficulty difficulty) {
		
		this.difficulty = difficulty;

		// Background
		Image background = new Image(getGame().assets.getGraphics(Constants.BUTTON_MENU_BACKGROUND));
		background.setSize(stageWidth, stageHeight);
		stage.addActor(background); // stage initialized in superclass
									// constructor
		
		String difficultyDescription;
		switch (this.difficulty) {
			case Kiddie: difficultyDescription = "obtížnost: dìcko"; break;
			case Beginner: difficultyDescription = "obtížnost: lehká"; break;
			case Normal: difficultyDescription = "obtížnost: normální"; break;
			case Hard: difficultyDescription = "obtížnost: tìžká"; break;
			case Extreme: difficultyDescription = "obtížnost: extrémní"; break;
			default: difficultyDescription = "obtížnost: CHYBA!!"; break;
		}
		
		MenuLabel label = new MenuLabel(difficultyDescription);
		label.setPosition(LevelSelectScreen.buttonsStartXPosition, 400);
		this.stage.addActor(label);
		
		Vector<LevelLoading> levels;
		
		switch (this.difficulty) {
			case Kiddie: levels = this.getGame().assets.getAvailableLevels().kiddieLevels; break;
			case Beginner: levels = this.getGame().assets.getAvailableLevels().beginnerLevels; break;
			case Normal: levels = this.getGame().assets.getAvailableLevels().normalLevels; break;
			case Hard: levels = this.getGame().assets.getAvailableLevels().hardLevels; break;
			case Extreme: levels = this.getGame().assets.getAvailableLevels().extremeLevels; break;
			default: levels = null;
		}
		
		int x = buttonsStartXPosition;
		int y = buttonsStartYPosition;
		
		for (int i = 0; i < levels.size(); ++i) {		
			final LevelLoading level = levels.get(i);
			MenuTextButton levelButton = new MenuTextButton (
				Integer.toString(i),
				getGame().assets.getGraphics(Constants.BUTTON_MENU_LEVEL),
				getGame().assets.getGraphics(Constants.BUTTON_MENU_LEVEL_HOVER)
			)
			{
				@Override
				public void action() {
					if (Autickax.gameScreen != null) {
						Autickax.gameScreen.dispose();
						Autickax.gameScreen = null;
					}
					Autickax.gameScreen = new GameScreen(level, difficulty);
					getGame().setScreen(Autickax.gameScreen);
				}
			};
			levelButton.setPosition(x, y);
			stage.addActor(levelButton);
			
			if (x + LevelSelectScreen.buttonsXShift < LevelSelectScreen.buttonsMaxXPosition) {
				x += LevelSelectScreen.buttonsXShift;
			}
			else {
				x = LevelSelectScreen.buttonsStartXPosition;
				y -= LevelSelectScreen.buttonsYShift;
			}
		}
	}

	@Override
	protected void onBackKeyPressed() {
		Autickax.difficultySelectScreen.dispose();
		Autickax.difficultySelectScreen = new DifficultySelectScreen();
		this.getGame().setScreen(Autickax.difficultySelectScreen);	
	}

}
