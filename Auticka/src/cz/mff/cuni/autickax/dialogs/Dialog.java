package cz.mff.cuni.autickax.dialogs;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

import cz.mff.cuni.autickax.Autickax;
import cz.mff.cuni.autickax.Constants;
import cz.mff.cuni.autickax.dialogs.DecisionDialog.DecisionType;
import cz.mff.cuni.autickax.scene.GameScreen;

public abstract class Dialog extends DialogAbstract{
	protected Label messageLabel;	
		
	public Dialog(GameScreen gameScreen, String message) {
		super(gameScreen);
		this.backgrountTexture = new TextureRegionDrawable(Autickax.getInstance().assets.getGraphics(Constants.DIALOG_BACKGROUND_TEXTURE));
		this.messageLabel = new DialogLabel(message);
		this.messageLabel.setPosition( Constants.DIALOG_MESSAGE_POSITION_X  - messageLabel.getWidth() / 2, Constants.DIALOG_MESSAGE_POSITION_Y - messageLabel.getHeight() / 2);
		this.stage.addActor(this.messageLabel);
	}
	
	public abstract DecisionType getDecision();
}
