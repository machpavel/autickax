package cz.mff.cuni.autickax.scene;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

import cz.mff.cuni.autickax.Autickax;
import cz.mff.cuni.autickax.Constants;
import cz.mff.cuni.autickax.menu.MenuAnimator;
import cz.mff.cuni.autickax.menu.MenuButton;

/*
 * This is the "Main menu" screen in the game, only contains a play button
 */
public class MainMenuScreen extends BaseScreen {

	private Button buttonPlay;
	private Button buttonExit;
	
	private final float buttonsXPositionStart = 10;
	private final float buttonsXPositionEnd = 100;

	public MainMenuScreen() {
		super();

		// Background
		Image background = new Image(game.assets.getGraphics(Constants.BUTTON_MENU_BACKGROUND));
		background.setSize(stageWidth, stageHeight);
		stage.addActor(background); // stage initialized in superclass
									// constructor

		// Play Button-----------------------------------------------------
		buttonPlay = new MenuButton (
			game.assets.getGraphics(Constants.BUTTON_MENU_PLAY),
			game.assets.getGraphics(Constants.BUTTON_MENU_PLAY_HOVER)
		)
		{
			@Override
			public void action() {
				Autickax.gameScreen = new GameScreen("level0");
				game.setScreen(Autickax.gameScreen);
			}
		};
		buttonPlay.setPosition(this.buttonsXPositionStart, 250);
		stage.addActor(buttonPlay);
		
		// Exit Button-----------------------------------------------------
		buttonExit = new MenuButton (
			game.assets.getGraphics(Constants.BUTTON_MENU_EXIT),
			game.assets.getGraphics(Constants.BUTTON_MENU_EXIT_HOVER)
		)
		{
			@Override
			public void action() {
				onBackKeyPressed();
			}
		};
		buttonExit.setPosition(this.buttonsXPositionStart, 30);
		stage.addActor(buttonExit);

		// Actions
		/*buttonPlay.addAction(MenuAnimator.moveTo(this.buttonsXPositionEnd, 250, 3,
				Interpolation.elasticOut));*/
	}

	@Override
	public void render(float delta) {
		super.render(delta);
	}

	@Override
	protected void onBackKeyPressed() {
		Gdx.app.exit();		
	}

}
