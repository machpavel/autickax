package cz.cuni.mff.xcars.scene;

import java.util.TreeMap;
import java.util.Vector;

import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

import cz.cuni.mff.xcars.Scenario;
import cz.cuni.mff.xcars.PlayedLevel;
import cz.cuni.mff.xcars.Xcars;
import cz.cuni.mff.xcars.constants.Constants;
import cz.cuni.mff.xcars.screenObjects.ScreenAdaptiveTextButton;

public class ScenarioSelectScreen extends BaseScreen {
	private final float buttonsXPositionStart = 15;

	public ScenarioSelectScreen() {
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
		
		TreeMap<String, Vector<PlayedLevel>> playedLevels = Xcars.playedLevels.levels;
		Vector<Scenario> scenarios = Xcars.availableLevels.scenarios;
		
		for (int i = 0; i < scenarios.size(); ++i) {
			final Scenario scenario = scenarios.get(i);
			boolean unlocked = playedLevels.containsKey(scenario.name);
			String buttonAssetName = unlocked ?
				Constants.menu.SCENARIOS_FOLDER_NAME + "/" + scenario.name :
					Constants.menu.BUTTON_MENU_DIFFICULTY;
			
			TextButton scenarioButton = new ScreenAdaptiveTextButton(scenario.name,
					Xcars.getInstance().assets.getGraphics(buttonAssetName),
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
					Xcars.levelSelectScreen = new LevelSelectScreen(scenario);
					Xcars.getInstance().setScreen(Xcars.levelSelectScreen);
				}
			};
			scenarioButton.setPosition(this.buttonsXPositionStart, (i + 1)*60);
			stage.addActor(scenarioButton);
		}
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
