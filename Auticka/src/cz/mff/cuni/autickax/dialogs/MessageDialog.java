package cz.mff.cuni.autickax.dialogs;

import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

import cz.mff.cuni.autickax.Autickax;
import cz.mff.cuni.autickax.Constants;
import cz.mff.cuni.autickax.dialogs.DecisionDialog.DecisionType;
import cz.mff.cuni.autickax.scene.GameScreen;

public class MessageDialog extends Dialog {

	private Button buttonOk;

	public MessageDialog(GameScreen gameScreen, String message) {
		super(gameScreen, message);
		buttonOk = new DialogButton(
				new TextureRegionDrawable(
						Autickax.getInstance().assets.getGraphics(Constants.MESSAGE_DIALOG_BUTTON_OK_TEXTURE))) {
			@Override
			public void action() {
				status = DialogAbstractStatus.FINISHED;
			}
		};
		buttonOk.setPosition(
				Constants.MESSAGE_DIALOG_BUTTON_OK_POSITION_X
						- buttonOk.getWidth() / 2,
				Constants.MESSAGE_DIALOG_BUTTON_OK_POSITION_Y
						- buttonOk.getHeight() / 2);
		this.stage.addActor(buttonOk);
	}

	@Override
	public void render() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public DecisionType getDecision() {
		return DecisionType.CONTINUE;
	}

}
