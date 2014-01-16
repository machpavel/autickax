package cz.mff.cuni.autickax.dialogs;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import cz.mff.cuni.autickax.Autickax;

public class FinishDialogLabel extends Label {
	
	public FinishDialogLabel(String text) {
		super(text, new LabelStyle(Autickax.getInstance().assets.getFinishDialogFont(), Color.WHITE));
	}
}
