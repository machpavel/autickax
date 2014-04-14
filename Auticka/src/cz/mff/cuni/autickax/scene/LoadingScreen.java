package cz.mff.cuni.autickax.scene;

import java.util.Vector;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import cz.mff.cuni.autickax.Autickax;
import cz.mff.cuni.autickax.constants.Constants;
import cz.mff.cuni.autickax.input.Input;


public class LoadingScreen extends BaseScreen {
	
	private final TextureRegion background;
	private final TextureRegion car;
	private final TextureRegion fume;
	
	private final int carYPosition;
	private final int fumeYPositon;
	private final int fumeLaunchInterval = 100;
	
	private int carFumePosition = 0; 
	private Vector<Integer> fumeXPositions = new Vector<Integer>(); 
	
	private int carXPosition = 0;

	public LoadingScreen() {
		super();
		
		this.carYPosition = (int)(50 * Input.yStretchFactorInv);
		this.fumeYPositon = (int)(70 * Input.yStretchFactorInv);
		
		this.getGame().assets.loadLoadingScreenGraphics();
		while (!getGame().assets.update()); // wait until it is loaded
		
		this.background = new TextureRegion(this.game.assets.getLoadingScreenGraphics(Constants.menu.LOADING_SCREEN_BACKGROUND));
		this.car = new TextureRegion(this.game.assets.getLoadingScreenGraphics(Constants.menu.LOADING_SCREEN_CAR));
		this.fume = new TextureRegion(this.game.assets.getLoadingScreenGraphics(Constants.menu.LOADING_SCREEN_FUME));
		
		getGame().assets.load();
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0f, 1f, 0f, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		if (this.getGame().assets.update()) {
			this.getGame().assets.disposeLoadingScreenGraphics();
			
			Autickax.mainMenuScreen = new MainMenuScreen(); // we know that it is null, no need for check
			getGame().setScreen(Autickax.mainMenuScreen);
		} else {
			this.batch.begin();
			
			this.batch.draw(this.background, 0, 0, this.stageWidth, this.stageHeight);
			this.batch.draw (
				this.car,
				this.carXPosition,
				this.carYPosition,
				this.car.getRegionWidth() * Input.xStretchFactorInv,
				this.car.getRegionHeight() * Input.yStretchFactorInv
			);
			
			for (int x : this.fumeXPositions) {
				this.batch.draw (
					this.fume,
					x,
					this.fumeYPositon,
					this.fume.getRegionWidth() * Input.xStretchFactorInv,
					this.fume.getRegionHeight() * Input.yStretchFactorInv
				);
			}
			
			
			this.batch.end();

			int newCarPosition = (int)(this.game.assets.getProgress() * this.stageWidth);
			this.carFumePosition += newCarPosition - this.carXPosition;
			this.carXPosition = newCarPosition;
			
			if (this.carFumePosition > this.fumeLaunchInterval) {
				this.fumeXPositions.add(this.carXPosition - this.fumeLaunchInterval / 2);
				this.carFumePosition = 0;
			}
		}
		//trace("loading progress:" + game.assets.getProgress()); //TODO visualize

	}

	@Override
	protected void onBackKeyPressed() {
		// TODO Auto-generated method stub
		
	}

}
