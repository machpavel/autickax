package cz.mff.cuni.autickax.scene;

import java.util.Vector;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;

import cz.mff.cuni.autickax.Autickax;
import cz.mff.cuni.autickax.Constants;
import cz.mff.cuni.autickax.Difficulty;
import cz.mff.cuni.autickax.LevelLoading;
import cz.mff.cuni.autickax.menu.MenuImage;
import cz.mff.cuni.autickax.menu.MenuTextButton;

public class LevelSelectScreen extends BaseScreen {
	
	private final Difficulty difficulty;
	
	private static final int buttonsStartXPosition = 20;
	private static final int buttonsStartYPosition = 310;
	private static final int buttonsMaxXPosition = 750;
	private static final int buttonsXShift = 150;
	private static final int buttonsYShift = 150;
	
	private static final int starsXStart = 32;
	private static final int starsXShift = 30;
	private static final int starsY = 30;
	
	public LevelSelectScreen(final Difficulty difficulty) {
		
		this.difficulty = difficulty;
		
		
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
			final int levelIndex = i;
			MenuTextButton levelButton = new MenuTextButton (
				Integer.toString(i),
				getGame().assets.getGraphics(Constants.BUTTON_MENU_LEVEL),
				getGame().assets.getGraphics(Constants.BUTTON_MENU_LEVEL_HOVER),
				getGame().assets.getGraphics(Constants.BUTTON_MENU_LEVEL_DISABLED),
				this.getGame().assets.getLevelNumberFont()
			)
			{
				@Override
				public void action() {
					getGame().assets.soundAndMusicManager.pauseMenuMusic();
					getGame().assets.soundAndMusicManager.playSound(Constants.SOUND_MENU_OPEN, Constants.SOUND_DEFAULT_VOLUME);
					if (Autickax.gameScreen != null) {
						Autickax.gameScreen.dispose();
						Autickax.gameScreen = null;
					}

					Autickax.gameScreen = new GameScreen(levelIndex, difficulty);

					getGame().setScreen(Autickax.gameScreen);
				}
			};
			
			levelButton.setPosition(x, y);
			stage.addActor(levelButton);
			
			if (i > 5) { // for testing
				levelButton.setDisabled(true);
			} else {
				drawStars((byte)(i % 4), x, y);
			}
			
			if (x + LevelSelectScreen.buttonsXShift < LevelSelectScreen.buttonsMaxXPosition) {
				x += LevelSelectScreen.buttonsXShift;
			}
			else {
				x = LevelSelectScreen.buttonsStartXPosition;
				y -= LevelSelectScreen.buttonsYShift;
			}
		}
	}

	private void drawStars(byte stars, int x, int y) {
		byte j = 0;
		for (; j < stars; ++j) {
			drawStar(x, y, j, Constants.BUTTON_MENU_LEVEL_STAR_GAINED);
		}
		for (; j < 3; ++j) {
			drawStar(x, y, j, Constants.BUTTON_MENU_LEVEL_STAR_UNGAINED);
		}
	}

	private void drawStar(int x, int y, byte j, String textureName) {
		MenuImage gainedStar = new MenuImage(this.game.assets.getGraphics(textureName));
		gainedStar.setPosition(x + starsXStart + j * starsXShift, y + starsY);
		stage.addActor(gainedStar);
	}

	@Override
	protected void onBackKeyPressed() {
		getGame().assets.soundAndMusicManager.playSound(Constants.SOUND_MENU_CLOSE, Constants.SOUND_DEFAULT_VOLUME);
		Autickax.difficultySelectScreen.dispose();
		Autickax.difficultySelectScreen = new DifficultySelectScreen();
		this.getGame().setScreen(Autickax.difficultySelectScreen);	
	}
	
	@Override
	protected void clearScreenWithColor() {
		Gdx.gl.glClearColor(Constants.LEVELS_MENU_RED, Constants.LEVELS_MENU_GREEN, Constants.LEVELS_MENU_BLUE, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
	}

}
