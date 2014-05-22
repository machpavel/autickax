package cz.mff.cuni.autickax.scene;

import java.util.Vector;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;

import cz.mff.cuni.autickax.Autickax;
import cz.mff.cuni.autickax.Difficulty;
import cz.mff.cuni.autickax.Level;
import cz.mff.cuni.autickax.PlayedLevel;
import cz.mff.cuni.autickax.constants.Constants;
import cz.mff.cuni.autickax.screenObjects.ScreenAdaptiveButton;
import cz.mff.cuni.autickax.screenObjects.ScreenAdaptiveTextButton;

public class LevelSelectScreen extends BaseScreen {

	private final Difficulty difficulty;

	private static final int buttonsStartXPosition = 20;
	private static final int buttonsStartYPosition = 330;
	private static final int buttonsMaxXPosition = 750;
	private static final int buttonsXShift = 150;
	private static final int buttonsYShift = 150;
	private static final int maximumCountOfButtons = Constants.menu.DISPLAYED_LEVELS_MAX_COUNT;

	private static final int leftShifterPositionX = 5;
	private static final int leftShifterPositionY = 200;
	private static final int rightShifterPositionX = 750;
	private static final int rightShifterPositionY = 200;

	public LevelSelectScreen(final Difficulty difficulty) {
		this(difficulty, 0);
	}

	public LevelSelectScreen(final Difficulty difficulty, final int levelsOffset) {

		this.difficulty = difficulty;

		Vector<Level> levels = this.difficulty.getAvailableLevels();
		Vector<PlayedLevel> playedLevels = this.difficulty.getPlayedLevels();

		int x = buttonsStartXPosition;
		int y = buttonsStartYPosition;
		int numberOfButtons = Math.min(maximumCountOfButtons, levels.size() - levelsOffset);
		for (int i = levelsOffset; i < levelsOffset + numberOfButtons; ++i) {
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
					throw new RuntimeException("Stars can't be more then three");
				}
			}

			ScreenAdaptiveTextButton levelButton = new ScreenAdaptiveTextButton(
					Integer.toString(i + 1),
					Autickax.getInstance().assets.getGraphics(buttonTexture),
					Autickax.getInstance().assets.getGraphics(buttonTextureHover),
					Autickax.getInstance().assets
							.getGraphics(Constants.menu.BUTTON_MENU_LEVEL_DISABLED),
					Autickax.getInstance().assets.getLevelNumberFont(), i < playedLevels.size()) {
				@Override
				public void action() {
					Autickax.getInstance().assets.soundAndMusicManager.pauseMenuMusic();
					Autickax.getInstance().assets.soundAndMusicManager
							.playSound(Constants.sounds.SOUND_MENU_OPEN,
									Constants.sounds.SOUND_DEFAULT_VOLUME);

					if (Autickax.levelLoadingScreen != null) {
						Autickax.levelLoadingScreen.dispose();
						Autickax.levelLoadingScreen = null;
					}

					Autickax.levelLoadingScreen = new LevelLoadingScreen(levelIndex, difficulty);

					Autickax.getInstance().setScreen(Autickax.levelLoadingScreen);
				}
			};

			levelButton.setPosition(x, y);
			stage.addActor(levelButton);

			if (i >= playedLevels.size()) {
				levelButton.setDisabled(true);
			}

			if (x + LevelSelectScreen.buttonsXShift < LevelSelectScreen.buttonsMaxXPosition) {
				x += LevelSelectScreen.buttonsXShift;
			} else {
				x = LevelSelectScreen.buttonsStartXPosition;
				y -= LevelSelectScreen.buttonsYShift;
			}
		}

		// Creates shift to the left
		if (levelsOffset > 0) {
			createShiftButton(leftShifterPositionX, leftShifterPositionY, ShifterDirection.LEFT,
					levelsOffset);
		}
		// Creates shift to the right
		if (levelsOffset + maximumCountOfButtons < levels.size()) {
			createShiftButton(rightShifterPositionX, rightShifterPositionY, ShifterDirection.RIGHT,
					levelsOffset);
		}
	}

	private void createShiftButton(float x, float y, ShifterDirection direction,
			final int levelsOffset) {
		final int shiftChange;
		String buttonTexture = Constants.menu.BUTTON_MENU_LEVEL_NO_STAR;
		String buttonTextureHover = Constants.menu.BUTTON_MENU_LEVEL_NO_STAR_HOVER;
		switch (direction) {
		case LEFT:
			shiftChange = -maximumCountOfButtons;
			buttonTexture = Constants.menu.BUTTON_MENU_LEFT_SHIFTER;
			buttonTextureHover = Constants.menu.BUTTON_MENU_LEFT_SHIFTER_HOVER;
			break;
		case RIGHT:
			shiftChange = maximumCountOfButtons;
			buttonTexture = Constants.menu.BUTTON_MENU_RIGHT_SHIFTER;
			buttonTextureHover = Constants.menu.BUTTON_MENU_RIGHT_SHIFTER_HOVER;
			break;
		default:
			throw new RuntimeException("Unrecognized direction: " + direction.toString());
		}

		ScreenAdaptiveButton button = new ScreenAdaptiveButton(
				Autickax.getInstance().assets.getGraphics(buttonTexture),
				Autickax.getInstance().assets.getGraphics(buttonTextureHover)) {

			@Override
			public void action() {
				if (Autickax.levelSelectScreen != null)
					Autickax.levelSelectScreen.dispose();
				Autickax.levelSelectScreen = new LevelSelectScreen(difficulty, levelsOffset
						+ shiftChange);
				Autickax.getInstance().setScreen(Autickax.levelSelectScreen);
			}
		};
		button.setPosition(x, y);
		stage.addActor(button);
	}

	@Override
	protected void onBackKeyPressed() {
		Autickax.getInstance().assets.soundAndMusicManager.playSound(
				Constants.sounds.SOUND_MENU_CLOSE, Constants.sounds.SOUND_DEFAULT_VOLUME);
		Autickax.difficultySelectScreen.dispose();
		Autickax.difficultySelectScreen = new DifficultySelectScreen();
		Autickax.getInstance().setScreen(Autickax.difficultySelectScreen);
	}

	@Override
	protected void clearScreenWithColor() {
		Gdx.gl.glClearColor(Constants.menu.LEVELS_MENU_RED, Constants.menu.LEVELS_MENU_GREEN,
				Constants.menu.LEVELS_MENU_BLUE, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
	}

	private enum ShifterDirection {
		LEFT, RIGHT
	}

}
