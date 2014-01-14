package cz.mff.cuni.autickax.scene;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
/*import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;*/
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

import cz.mff.cuni.autickax.Autickax;
import cz.mff.cuni.autickax.Constants;
//import cz.mff.cuni.autickax.menu.MenuAnimator;
import cz.mff.cuni.autickax.menu.MenuButton;

/*
 * This is the "Main menu" screen in the game, only contains a play button
 */
public class MainMenuScreen extends BaseScreen {

	private Button buttonPlay;
	private Button buttonExit;
	
	private final float buttonsXPositionStart = 10;
	//private final float buttonsXPositionEnd = 100;

	public MainMenuScreen() {
		super();
		initMenuMusic();

		// Background
		Image background = new Image(getGame().assets.getGraphics(Constants.BUTTON_MENU_BACKGROUND));
		background.setSize(stageWidth, stageHeight);
		stage.addActor(background); // stage initialized in superclass
									// constructor

		// Play Button-----------------------------------------------------
		this.buttonPlay = new MenuButton (
			getGame().assets.getGraphics(Constants.BUTTON_MENU_PLAY),
			getGame().assets.getGraphics(Constants.BUTTON_MENU_PLAY_HOVER)
		)
		{
			@Override
			public void action() {
				if (Autickax.difficultySelectScreen != null) {
					Autickax.difficultySelectScreen.dispose();
					Autickax.difficultySelectScreen = null;
				}
				Autickax.difficultySelectScreen = new DifficultySelectScreen();
				getGame().setScreen(Autickax.difficultySelectScreen);
			}
		};
		this.buttonPlay.setPosition(this.buttonsXPositionStart, 250);
		stage.addActor(this.buttonPlay);
		
		// Exit Button-----------------------------------------------------
		this.buttonExit = new MenuButton (
			getGame().assets.getGraphics(Constants.BUTTON_MENU_EXIT),
			getGame().assets.getGraphics(Constants.BUTTON_MENU_EXIT_HOVER)
		)
		{
			@Override
			public void action() {
				onBackKeyPressed();
			}
		};
		this.buttonExit.setPosition(this.buttonsXPositionStart, 30);
		stage.addActor(this.buttonExit);

		// Actions
		/*buttonPlay.addAction(MenuAnimator.moveTo(this.buttonsXPositionEnd, 250, 3,
				Interpolation.elasticOut));*/
	}
	
	private void initMenuMusic()
	{
		Music menuMusic = getGame().assets.menuMusic;
		menuMusic.setVolume(Constants.MUSIC_DEFAULT_VOLUME);
		menuMusic.setLooping(true);
		menuMusic.play();
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
