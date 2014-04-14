package cz.mff.cuni.autickax.scene;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

import cz.mff.cuni.autickax.Autickax;
import cz.mff.cuni.autickax.Difficulty;
import cz.mff.cuni.autickax.constants.Constants;

public class LevelLoadingScreen extends BaseScreen {
	
	public LevelLoadingScreen(final int levelIndex, final Difficulty levelDifficulty) {
		super();
		
		// setup a game screen
		if (Autickax.gameScreen != null) {
			Autickax.gameScreen.dispose();
			Autickax.gameScreen = null;
		}

		Autickax.gameScreen = new GameScreen(levelIndex, levelDifficulty);
		
		Thread distanceMapLoader = new Thread(new Runnable() {
	         @Override
	         public void run() {
	        	 Autickax.gameScreen.initializeDistanceMap();
	        	 
	        	 Gdx.app.postRunnable(new Runnable() {
	    	         @Override
	    	         public void run() {
	    				Autickax.gameScreen.initializeGameScreen();
	    				Autickax.getInstance().setScreen(Autickax.gameScreen);
	    	         }
	    	      });
	         }
	      });
		
		distanceMapLoader.start();
		

		// now draw elements
		Image background = new Image(getGame().assets.getGraphics(Constants.menu.LOADING_LEVEL_MENU_BACKGROUND));
		background.setSize(stageWidth, stageHeight);
		stage.addActor(background);
	}

	@Override
	protected void onBackKeyPressed() {
		// TODO Auto-generated method stub
		
	}
}
