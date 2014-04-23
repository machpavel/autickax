package cz.mff.cuni.autickax.myInputListener;

import java.text.DecimalFormat;

import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldListener;

import cz.mff.cuni.autickax.EditorScreen;

public class MyTextNumberListener implements TextFieldListener {
	EditorScreen screen;

	public MyTextNumberListener(EditorScreen screen) {
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
