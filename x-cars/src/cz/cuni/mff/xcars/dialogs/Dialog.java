package cz.cuni.mff.xcars.dialogs;

import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;

import cz.cuni.mff.xcars.Xcars;
import cz.cuni.mff.xcars.constants.Constants;
import cz.cuni.mff.xcars.gamelogic.SubLevel;
import cz.cuni.mff.xcars.scene.GameScreen;
import cz.cuni.mff.xcars.screenObjects.ScreenAdaptiveLabel;

public abstract class Dialog extends Comunicator {
	protected ScreenAdaptiveLabel messageLabel;

	public Dialog(GameScreen gameScreen, SubLevel parent, String message) {
		super(gameScreen, parent);
		this.backgroundTexture = new NinePatchDrawable(
				Xcars.getInstance().assets
						.getNinePatch(Constants.dialog.DIALOG_BACKGROUND_TEXTURE));
		this.messageLabel = ScreenAdaptiveLabel.getDialogLabel(message);

		this.messageLabel.setWrap(true);
		this.messageLabel.setAlignment(0, 0);

		Table table = new Table();
		table.setPosition(Constants.dialog.DIALOG_MESSAGE_POSITION_X,
				Constants.dialog.DIALOG_MESSAGE_POSITION_Y);
		table.add(messageLabel).width(Constants.dialog.DIALOG_MESSAGE_WIDTH);
		this.stage.addActor(table);
	}

	protected void playButtonSound() {
		this.soundsManager.playSound(
				Constants.sounds.SOUND_BUTTON_DIALOG_SOUND, Constants.sounds.SOUND_DEFAULT_VOLUME);
	}

	public abstract DecisionType getDecision();

	public void endCommunication() {
		parent.onDialogEnded();
		super.endCommunication();
	}

	public enum DecisionType {
		CONTINUE, RESTART, GO_TO_MAIN_MENU;
	}
}
