package cz.mff.cuni.autickax.dialogs;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

import cz.mff.cuni.autickax.Autickax;
import cz.mff.cuni.autickax.Constants;
import cz.mff.cuni.autickax.gamelogic.SubLevel;
import cz.mff.cuni.autickax.scene.GameScreen;

public abstract class Dialog extends Comunicator{
	protected Label messageLabel;	
		
	public Dialog(GameScreen gameScreen, SubLevel parent, String message) {
		super(gameScreen, parent);
		this.backgrountTexture = new TextureRegionDrawable(Autickax.getInstance().assets.getGraphics(Constants.DIALOG_BACKGROUND_TEXTURE));
		this.messageLabel = new DialogLabel(message);
		this.messageLabel.setPosition( Constants.DIALOG_MESSAGE_POSITION_X  - messageLabel.getWidth() / 2, Constants.DIALOG_MESSAGE_POSITION_Y - messageLabel.getHeight() / 2);
		this.stage.addActor(this.messageLabel);
	}
	protected void playButtonSound()
	{
		super.level.getGame().assets.soundAndMusicManager.playSound(Constants.SOUND_BUTTON_DIALOG_SOUND, Constants.SOUND_DEFAULT_VOLUME);
	}
	
	public abstract DecisionType getDecision();
	
	public enum DecisionType {
		CONTINUE, RESTART, GO_TO_MAIN_MENU;
	}
}
