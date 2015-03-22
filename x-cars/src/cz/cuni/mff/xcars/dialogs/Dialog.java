package cz.cuni.mff.xcars.dialogs;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;

import cz.cuni.mff.xcars.Xcars;
import cz.cuni.mff.xcars.constants.Constants;
import cz.cuni.mff.xcars.gamelogic.SubLevel;
import cz.cuni.mff.xcars.scene.GameScreen;
import cz.cuni.mff.xcars.screenObjects.ScreenAdaptiveLabel;

public abstract class Dialog extends Comunicator {
	protected ScreenAdaptiveLabel messageLabel;
	protected final Color backgroundColor = Constants.dialog.DIALOG_BACKGROUND_COLOR;
	protected NinePatchDrawable backgroundTexture;

	public Dialog(GameScreen gameScreen, SubLevel parent, String message) {
		super(gameScreen, parent);
		this.backgroundTexture = new NinePatchDrawable(
				Xcars.getInstance().assets.getNinePatch(Constants.dialog.DIALOG_BACKGROUND_TEXTURE));
		this.messageLabel = ScreenAdaptiveLabel.getDialogLabel(message);

		this.messageLabel.setWrap(true);
		this.messageLabel.setAlignment(0, 0);
		this.messageLabel.setColor(Constants.dialog.DIALOG_FONT_COLOR);

		Table table = new Table();
		table.setPosition(Constants.dialog.DIALOG_MESSAGE_POSITION_X, Constants.dialog.DIALOG_MESSAGE_POSITION_Y);
		table.add(messageLabel).width(Constants.dialog.DIALOG_MESSAGE_WIDTH);
		this.stage.addActor(table);

		this.soundsManager.playSound(Constants.sounds.SOUND_DIALOG_OPEN_SOUND);
	}

	protected void playButtonSound() {
		this.soundsManager.playSound(Constants.sounds.SOUND_DIALOG_CLOSE_SOUND, Constants.sounds.SOUND_DEFAULT_VOLUME);
	}

	public abstract DecisionType getDecision();

	public void endCommunication() {
		parent.onDialogEnded();
		super.endCommunication();
	}

	public enum DecisionType {
		CONTINUE, RESTART, GO_TO_MAIN_MENU;
	}

	// Draws background texture
	// It is separated because it can be rewritten by minigames (different size
	// needed)
	protected void drawBackGroundTexture(SpriteBatch batch) {
		// Draws transparent layer behind background
		Gdx.gl.glEnable(GL20.GL_BLEND);
		shapeRenderer.begin(ShapeType.Filled);
		shapeRenderer.setColor(backgroundColor);
		shapeRenderer.rect(0, 0, Constants.WORLD_WIDTH, Constants.WORLD_HEIGHT);
		shapeRenderer.end();
		Gdx.gl.glDisable(GL20.GL_BLEND);

		batch.begin();
		this.backgroundTexture.draw(batch, Constants.dialog.DIALOG_WORLD_X_OFFSET,
				Constants.dialog.DIALOG_WORLD_Y_OFFSET, Constants.dialog.DIALOG_WORLD_WIDTH,
				Constants.dialog.DIALOG_WORLD_HEIGHT);
		batch.end();
	}
}
