package cz.mff.cuni.autickax.scene;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;

import cz.mff.cuni.autickax.Autickax;
import cz.mff.cuni.autickax.Difficulty;

public class LevelLoadingScreen extends BaseScreen {
	
	public LevelLoadingScreen(final int levelIndex, final Difficulty levelDifficulty) {
		super();
		
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
	}

	@Override
	public void render(float delta) {		
		//TODO: loading animation
		Gdx.gl.glClearColor(0f, 1f, 0f, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		//trace("loading progress:" + game.assets.getProgress()); //TODO visualize

	}

	@Override
	protected void onBackKeyPressed() {
		// TODO Auto-generated method stub
		
	}
}
