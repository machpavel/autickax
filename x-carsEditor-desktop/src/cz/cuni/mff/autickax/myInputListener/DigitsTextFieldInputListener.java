package cz.cuni.mff.autickax.myInputListener;

import java.text.DecimalFormat;

import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldListener;

import cz.cuni.mff.autickax.EditorScreen;

public class DigitsTextFieldInputListener implements TextFieldListener {
	EditorScreen screen;

	public DigitsTextFieldInputListener(EditorScreen screen) {
		this.screen = screen;
	}
	
	@Override
	public void keyTyped(final TextField textField, char key) {
		if (textField.getText().length() <= 0)
			screen.setTimeLimit(0);
		if (textField.getText().length() <= 2) {
			try {
				int value = Integer.parseInt(textField.getText());
				screen.setTimeLimit(value);
			} catch (Exception e) {
				//nothing
			}
		}
		else {
			try {
				int value = Integer.parseInt(Character.toString(key));
				screen.setTimeLimit(value);
			} catch (Exception e) {
				//nothing
			}
		}
		textField.setText(new DecimalFormat().format(screen.getTimeLimit()));				
	}
}
