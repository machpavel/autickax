package cz.cuni.mff.xcars.scene;

import java.util.Vector;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

import cz.cuni.mff.xcars.PlayedLevel;
import cz.cuni.mff.xcars.Scenario;
import cz.cuni.mff.xcars.Xcars;
import cz.cuni.mff.xcars.constants.Constants;
import cz.cuni.mff.xcars.constants.Menu;
import cz.cuni.mff.xcars.screenObjects.ScreenAdaptiveTextButton;

public class LevelSelectScreen extends SelectScreenBase {

	private final Scenario scenario;

	public LevelSelectScreen(final int levelIndex, Scenario levelsSet) {
		super();

		// Positioning
		this.buttonsMinXPosition = Constants.menu.LEVEL_SELECT_SCREEN_buttonsMinXPosition;
		this.buttonsMinYPosition = Constants.menu.LEVEL_SELECT_SCREEN_buttonsMinYPosition;
		this.buttonsMaxYPosition = Constants.menu.LEVEL_SELECT_SCREEN_buttonsMaxYPosition;
		this.buttonsXOffset = Constants.menu.LEVEL_SELECT_SCREEN_buttonsXOffset;
		this.buttonsYOffset = Constants.menu.LEVEL_SELECT_SCREEN_buttonsYOffset;

		// Ads
		if (Xcars.adsHandler != null) {
			Xcars.adsHandler.showBanner(false);
		}

		// Background
		setBackground(Menu.SCENARIO_TO_BG_MAPPING.get(levelsSet.name.toLowerCase()));

		// Buttons
		this.scenario = levelsSet;
		ScreenAdaptiveTextButton[] buttons = new ScreenAdaptiveTextButton[levelsSet.levels.size()];

		Vector<PlayedLevel> playedLevels = Xcars.playedLevels.levels.containsKey(levelsSet.name) ? Xcars.playedLevels.levels
				.get(levelsSet.name) : new Vector<PlayedLevel>();

		for (int i = 0; i < levelsSet.levels.size(); ++i) {
			ScreenAdaptiveTextButton levelButton = createButton(i, playedLevels, this.scenario);
			buttons[i] = levelButton;
		}
		this.RegisterButtons(levelIndex, buttons);
	}

	// TODO what is this for?
	private void setBackground(String backgroundPath) {
		Image background = new Image(Xcars.getInstance().assets.getGraphics(backgroundPath));
		background.setSize(this.stageWidth, this.stageHeight);
		stage.addActor(background);
	}

	/**
	 * Creates a text button with according start level handler.
	 */
	private ScreenAdaptiveTextButton createButton(final int levelIndex, Vector<PlayedLevel> playedLevels,
			final Scenario levelsSet) {

		String buttonTexture = Constants.menu.BUTTON_MENU_LEVEL_NO_STAR;
		String buttonTextureHover = Constants.menu.BUTTON_MENU_LEVEL_NO_STAR_HOVER;

		if (levelIndex < playedLevels.size()) {
			switch (playedLevels.get(levelIndex).starsNumber) {
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
		ScreenAdaptiveTextButton levelButton = new ScreenAdaptiveTextButton(Integer.toString(levelIndex + 1),
				Xcars.getInstance().assets.getGraphics(buttonTexture),
				Xcars.getInstance().assets.getGraphics(buttonTextureHover),
				Xcars.getInstance().assets.getGraphics(Constants.menu.BUTTON_MENU_LEVEL_DISABLED),
				Xcars.getInstance().assets.getLevelNumberFont(), levelIndex < playedLevels.size()) {
			@Override
			public void action() {
				if (!wasPanned) {
					Xcars.getInstance().assets.soundAndMusicManager.playSound(Constants.sounds.SOUND_MENU_OPEN,
							Constants.sounds.SOUND_DEFAULT_VOLUME);
					if (Xcars.levelLoadingScreen != null) {
						Xcars.levelLoadingScreen.dispose();
						Xcars.levelLoadingScreen = null;
					}
					Xcars.levelLoadingScreen = new LevelLoadingScreen(levelIndex, levelsSet);
					Xcars.getInstance().setScreen(Xcars.levelLoadingScreen);
				}
			}
		};
		// Disables a button with unplayed level
		if (levelIndex >= playedLevels.size()) {
			levelButton.setDisabled(true, true);
		}
		return levelButton;
	}

	@Override
	protected void onBackKeyPressed() {
		Xcars.getInstance().assets.soundAndMusicManager.playSound(Constants.sounds.SOUND_MENU_CLOSE);
		Xcars.difficultySelectScreen.dispose();
		Xcars.difficultySelectScreen = new ScenarioSelectScreen(
				(int) (Xcars.difficultySelectScreen.GetActualPage() * Xcars.difficultySelectScreen.GetButtonsPerPage()));
		Xcars.getInstance().setScreen(Xcars.difficultySelectScreen);
	}

	@Override
	protected void clearScreenWithColor() {
		Gdx.gl.glClearColor(Constants.menu.LEVELS_MENU_RED, Constants.menu.LEVELS_MENU_GREEN,
				Constants.menu.LEVELS_MENU_BLUE, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
	}
}
