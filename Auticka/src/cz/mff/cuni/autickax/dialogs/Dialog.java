package cz.mff.cuni.autickax.dialogs;

import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

import cz.mff.cuni.autickax.Autickax;
import cz.mff.cuni.autickax.constants.Constants;
import cz.mff.cuni.autickax.gamelogic.SubLevel;
import cz.mff.cuni.autickax.input.Input;
import cz.mff.cuni.autickax.scene.GameScreen;
import cz.mff.cuni.autickax.screenObjects.ScreenAdaptiveLabel;

public abstract class Dialog extends Comunicator {
	protected ScreenAdaptiveLabel messageLabel;

	public Dialog(GameScreen gameScreen, SubLevel parent, String message) {
		super(gameScreen, parent);
		this.backgroundTexture = new TextureRegionDrawable(
				Autickax.getInstance().assets
						.getGraphics(Constants.dialog.DIALOG_BACKGROUND_TEXTURE));
		this.messageLabel = ScreenAdaptiveLabel.getDialogLabel(message);

		this.messageLabel.setWrap(true);
		this.messageLabel.setAlignment(0, 0);
		this.messageLabel.setFontScaleX(Input.xStretchFactorInv);
		this.messageLabel.setFontScaleY(Input.yStretchFactorInv);
		Table table = new Table();
		table.setPosition(Constants.dialog.DIALOG_MESSAGE_POSITION_X * Input.xStretchFactorInv,
				Constants.dialog.DIALOG_MESSAGE_POSITION_Y * Input.yStretchFactorInv);
		table.add(messageLabel).width(Constants.dialog.DIALOG_MESSAGE_WIDTH);
		this.stage.addActor(table);
	}

	protected void playButtonSound() {
		Autickax.getInstance().assets.soundAndMusicManager.playSound(
				Constants.sounds.SOUND_BUTTON_DIALOG_SOUND, Constants.sounds.SOUND_DEFAULT_VOLUME);
	}

	public abstract DecisionType getDecision();

	protected void endCommunication() {
		parent.onDialogEnded();
		super.endCommunication();
	}

	public enum DecisionType {
		CONTINUE, RESTART, GO_TO_MAIN_MENU;
	}
}
