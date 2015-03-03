package cz.cuni.mff.xcars.scene;

import java.util.Vector;

import com.badlogic.gdx.scenes.scene2d.ui.Image;

import cz.cuni.mff.xcars.Scenario;
import cz.cuni.mff.xcars.Xcars;
import cz.cuni.mff.xcars.constants.Constants;
import cz.cuni.mff.xcars.screenObjects.ScreenAdaptiveTextButton;

public class ScenarioSelectScreen extends SelectScreenBase {
	public ScenarioSelectScreen(final int levelIndex) {
		super();

		// Positioning
		this.buttonsMinXPosition = Constants.menu.SCENARIO_SELECT_SCREEN_buttonsMinXPosition;
		this.buttonsMinYPosition = Constants.menu.SCENARIO_SELECT_SCREEN_buttonsMinYPosition;
		this.buttonsMaxYPosition = Constants.menu.SCENARIO_SELECT_SCREEN_buttonsMaxYPosition;
		this.buttonsXOffset = Constants.menu.SCENARIO_SELECT_SCREEN_buttonsXOffset;
		this.buttonsYOffset = Constants.menu.SCENARIO_SELECT_SCREEN_buttonsYOffset;

		// Ads
		if (Xcars.adsHandler != null) {
			Xcars.adsHandler.showBanner(true);
		}

		// Background
		Image background = new Image(Xcars.getInstance().assets.getGraphics(Constants.menu.DIFFICULTY_MENU_BACKGROUND));
		background.setSize(this.stageWidth, this.stageHeight);
		stage.addActor(background); // stage initialized in superclass
									// constructor

		// Buttons
		Vector<Scenario> scenarios = Xcars.availableLevels.scenarios;
		ScreenAdaptiveTextButton[] buttons = new ScreenAdaptiveTextButton[scenarios.size()];
		for (int i = 0; i < scenarios.size(); ++i) {
			final Scenario scenario = scenarios.get(i);
			boolean isEnabled = Xcars.playedLevels.levels.containsKey(scenario.name);
			ScreenAdaptiveTextButton scenarioButton = new ScreenAdaptiveTextButton(scenario.name,
					Xcars.getInstance().assets.getGraphics(Constants.menu.SCENARIOS_FOLDER_NAME + "/" + scenario.name),
					Xcars.getInstance().assets.getGraphics(Constants.menu.BUTTON_MENU_DIFFICULTY_HOVER),
					Xcars.getInstance().assets.getGraphics(Constants.menu.BUTTON_MENU_DIFFICULTY),
					Xcars.getInstance().assets.getLevelNumberFont(), isEnabled) {
				@Override
				public void action() {
					if (!wasPanned) {
						Xcars.getInstance().assets.soundAndMusicManager.playSound(Constants.sounds.SOUND_MENU_OPEN,
								Constants.sounds.SOUND_DEFAULT_VOLUME);
						if (Xcars.levelSelectScreen != null) {
							Xcars.levelSelectScreen.dispose();
							Xcars.levelSelectScreen = null;
						}
						Xcars.levelSelectScreen = new LevelSelectScreen(Xcars.playedLevels.levels.get(scenario.name)
								.size() - 1, scenario);
						Xcars.getInstance().setScreen(Xcars.levelSelectScreen);
					}
				}
			};
			if (!isEnabled)
				scenarioButton.setDisabled(true);
			buttons[i] = scenarioButton;
		}
		this.RegisterButtons(levelIndex, buttons);
	}

	@Override
	protected void onBackKeyPressed() {
		Xcars.getInstance().assets.soundAndMusicManager.playSound(Constants.sounds.SOUND_MENU_CLOSE,
				Constants.sounds.SOUND_DEFAULT_VOLUME);
		Xcars.mainMenuScreen.dispose();
		Xcars.mainMenuScreen = new MainMenuScreen();
		Xcars.getInstance().setScreen(Xcars.mainMenuScreen);
	}

}
