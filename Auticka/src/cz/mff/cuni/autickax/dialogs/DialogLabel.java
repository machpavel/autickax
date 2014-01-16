package cz.mff.cuni.autickax.dialogs;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import cz.mff.cuni.autickax.Autickax;
import cz.mff.cuni.autickax.input.Input;

public class DialogLabel extends Label {
	
	public DialogLabel(String text) {
		super(text, new LabelStyle(Autickax.getInstance().assets.getDialogFont(), Color.PINK));
	}
}
