package cz.mff.cuni.autickax.scene;

import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

/*
 * This is the "Main menu" screen in the game, only contains a play button
 */
public class TitleScreen extends BaseScreen {

	Button buttonPlay;
	Button buttonEditor;

	public TitleScreen() {
		super();

		// Background
		Image background = new Image(game.assets.getGraphics("sky"));
		background.setSize(stageWidth, stageHeight);
		stage.addActor(background); // stage initialized in superclass
									// constructor

		// Play Button-----------------------------------------------------
		//buttonPlay = new ImageButton(new TextureRegionDrawable(game.assets.getGraphics("play")));
		buttonPlay = new TextButton("Play", new TextButtonStyle(new TextureRegionDrawable(
				game.assets.getGraphics("play")),new TextureRegionDrawable(
						game.assets.getGraphics("play")),new TextureRegionDrawable(
								game.assets.getGraphics("play")),game.assets.getFont()));

		buttonPlay.setPosition(150, 250);
		// buttonPlay.setOrigin(buttonPlay.getWidth()/2,
		// buttonPlay.getHeight()/2);
		stage.addActor(buttonPlay);

		// User Input for Play Button
		buttonPlay.addListener(new InputListener() {
			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int button) {
				return true;
			}
			public void touchUp(InputEvent event, float x, float y,
					int pointer, int button) {				
				game.setScreen(game.assets.loadLevel("level"));
			}
		});

		// Action
		float targetYplay = (stageHeight - buttonPlay.getHeight()) / 2;
		buttonPlay.addAction(Actions.moveTo(buttonPlay.getX(), targetYplay, 3,
				Interpolation.elasticOut));
		//End of Play Button-------------------------------------------------
		
		// Editor Button-----------------------------------------------------
		buttonEditor = new TextButton("Editor", new TextButtonStyle(new TextureRegionDrawable(
				game.assets.getGraphics("play")),new TextureRegionDrawable(
						game.assets.getGraphics("play")),new TextureRegionDrawable(
								game.assets.getGraphics("play")),game.assets.getFont()));

		buttonEditor.setPosition(350, 250);
		stage.addActor(buttonEditor);

		// User Input for Play Button
		buttonEditor.addListener(new InputListener() {
			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int button) {
				return true;
			}
			public void touchUp(InputEvent event, float x, float y,
					int pointer, int button) {										
				game.setScreen(new EditorScreen());
			}
		});

		// Action
		float targetYeditor = (stageHeight - buttonEditor.getHeight()) / 2;
		buttonEditor.addAction(Actions.moveTo(buttonEditor.getX(), targetYeditor, 3,
				Interpolation.elasticOut));
		//End of Editor Button-------------------------------------------------

		

	}

	@Override
	public void render(float delta) {
		super.render(delta);
	}

}
