package cz.mff.cuni.autickax.scene;

import java.util.Vector;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;

import cz.mff.cuni.autickax.Autickax;
import cz.mff.cuni.autickax.Difficulty;
import cz.mff.cuni.autickax.LevelLoading;
import cz.mff.cuni.autickax.PlayedLevel;
import cz.mff.cuni.autickax.constants.Constants;
import cz.mff.cuni.autickax.menu.MenuTextButton;

public class LevelSelectScreen extends BaseScreen {
	
	private final Difficulty difficulty;
	
	private static final int buttonsStartXPosition = 20;
	private static final int buttonsStartYPosition = 310;
	private static final int buttonsMaxXPosition = 750;
	private static final int buttonsXShift = 150;
	private static final int buttonsYShift = 150;
	
	public LevelSelectScreen(final Difficulty difficulty) {
		
		this.difficulty = difficulty;
		
		
		Vector<LevelLoading> levels = this.difficulty.getAvailableLevels();
		Vector<PlayedLevel> playedLevels = this.difficulty.getPlayedLevels();
		
		int x = buttonsStartXPosition;
		int y = buttonsStartYPosition;
		
		for (int i = 0; i < levels.size(); ++i) {		
			final int levelIndex = i;
			
			String buttonTexture = Constants.menu.BUTTON_MENU_LEVEL_NO_STAR;
			String buttonTextureHover = Constants.menu.BUTTON_MENU_LEVEL_NO_STAR_HOVER;
			
			if (i < playedLevels.size()) {
				switch (playedLevels.get(i).starsNumber) {
				case 0:
					buttonTexture = Constants.menu.BUTTON_MENU_LEVEL_NO_STAR;
					buttonTextureHover = Constants.menu.BUTTON_MENU_LEVEL_NO_STAR_HOVER;
					break;
	
				case 1:
					buttonTexture = Constants.menu.BUTTON_MENU_LEVEL_ONE_STAR;
					buttonTextureHover = Constants.menu.BUTTON_MENU_LEVEL_ONE_STAR_HOVER;
					break;
	
				case 2:
					buttonTexture = Constants.menu.BUTTON_MENU_LEVEL_TWO_STARS;
					buttonTextureHover = Constants.menu.BUTTON_MENU_LEVEL_TWO_STARS_HOVER;
					break;
	
				case 3:
					buttonTexture = Constants.menu.BUTTON_MENU_LEVEL_THREE_STARS;
					buttonTextureHover = Constants.menu.BUTTON_MENU_LEVEL_THREE_STARS_HOVER;
					break;
					
				default:
					assert true;
					// stars cannot be more than three
				}
			}
			
			MenuTextButton levelButton = new MenuTextButton (
				Integer.toString(i),
				getGame().assets.getGraphics(buttonTexture),
				getGame().assets.getGraphics(buttonTextureHover),
				getGame().assets.getGraphics(Constants.menu.BUTTON_MENU_LEVEL_DISABLED),
				this.getGame().assets.getLevelNumberFont(),
				i < playedLevels.size()
			)
			{
				@Override
				public void action() {
					getGame().assets.soundAndMusicManager.pauseMenuMusic();
					getGame().assets.soundAndMusicManager.playSound(Constants.sounds.SOUND_MENU_OPEN, Constants.sounds.SOUND_DEFAULT_VOLUME);
					
					if (Autickax.levelLoadingScreen != null) {
						Autickax.levelLoadingScreen.dispose();
						Autickax.levelLoadingScreen = null;
					}

					Autickax.levelLoadingScreen = new LevelLoadingScreen(levelIndex, difficulty);

					getGame().setScreen(Autickax.levelLoadingScreen);
				}
			};
			
			levelButton.setPosition(x, y);
			stage.addActor(levelButton);
			
			if (i >= playedLevels.size()) {
				levelButton.setDisabled(true);
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

	@Override
	protected void onBackKeyPressed() {
		getGame().assets.soundAndMusicManager.playSound(Constants.sounds.SOUND_MENU_CLOSE, Constants.sounds.SOUND_DEFAULT_VOLUME);
		Autickax.difficultySelectScreen.dispose();
		Autickax.difficultySelectScreen = new DifficultySelectScreen();
		this.getGame().setScreen(Autickax.difficultySelectScreen);	
	}
	
	@Override
	protected void clearScreenWithColor() {
		Gdx.gl.glClearColor(Constants.menu.LEVELS_MENU_RED, Constants.menu.LEVELS_MENU_GREEN, Constants.menu.LEVELS_MENU_BLUE, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
	}

}
