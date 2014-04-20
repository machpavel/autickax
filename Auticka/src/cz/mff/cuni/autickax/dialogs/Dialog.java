package cz.mff.cuni.autickax.dialogs;

import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

import cz.mff.cuni.autickax.Autickax;
import cz.mff.cuni.autickax.constants.Constants;
import cz.mff.cuni.autickax.gamelogic.SubLevel;
import cz.mff.cuni.autickax.scene.GameScreen;
import cz.mff.cuni.autickax.screenObjects.ScreenAdaptiveLabel;

public abstract class Dialog extends Comunicator{
	protected ScreenAdaptiveLabel messageLabel;	
		
	public Dialog(GameScreen gameScreen, SubLevel parent, String message) {
		super(gameScreen, parent);
		this.backgrountTexture = new TextureRegionDrawable(Autickax.getInstance().assets.getGraphics(Constants.dialog.DIALOG_BACKGROUND_TEXTURE));
		this.messageLabel = ScreenAdaptiveLabel.getDialogLabel(message);
		this.messageLabel.setCenterPosition(Constants.dialog.DIALOG_MESSAGE_POSITION_X, Constants.dialog.DIALOG_MESSAGE_POSITION_Y);
		this.stage.addActor(this.messageLabel);
	}
	protected void playButtonSound()
	{
		super.level.getGame().assets.soundAndMusicManager.playSound(Constants.sounds.SOUND_BUTTON_DIALOG_SOUND, Constants.sounds.SOUND_DEFAULT_VOLUME);
	}
	
	public abstract DecisionType getDecision();
	
	public enum DecisionType {
		CONTINUE, RESTART, GO_TO_MAIN_MENU;
	}
}
