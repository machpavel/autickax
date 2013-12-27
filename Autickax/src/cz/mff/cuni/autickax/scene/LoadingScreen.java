package cz.mff.cuni.autickax.scene;

import cz.mff.cuni.autickax.MyGdxGame;

/**
 * In this screen we load all our assets and then switch to TitleScreen
 * @author Ondrej Paska
 */
public class LoadingScreen extends BaseScreen{

	
	public LoadingScreen() {
		super();
		
		game.assets.load();
	}

	@Override
	public void render(float delta) {
		if( game.assets.update() ) { //Keep calling this, until returns true
			MyGdxGame.getInstance().setScreen( new TitleScreen() );
			return;
		}
		//trace("loading progress:"+Assets.getProgress()); //TODO visualize progress
	}


}
