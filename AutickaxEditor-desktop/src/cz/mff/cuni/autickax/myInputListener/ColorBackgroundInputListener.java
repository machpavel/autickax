package cz.mff.cuni.autickax.myInputListener;

import javax.swing.JOptionPane;

import com.badlogic.gdx.scenes.scene2d.InputEvent;

import cz.mff.cuni.autickax.EditorScreen;
import cz.mff.cuni.autickax.drawing.LevelConstantBackground;

public class ColorBackgroundInputListener extends MyInputListener {

	public ColorBackgroundInputListener(EditorScreen screen) {
		super(screen);
	}

	@Override
	public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
		// this.screen.SetBackground(new LevelTextureBackground(name,
		// AutickaxEditor.getInstance().assets.getGraphics(name)));

		String result = (String) JOptionPane.showInputDialog(null,
				"Write color. Three integer values (0-255) separated with empty spaces.  ",
				"Color picker", JOptionPane.PLAIN_MESSAGE, null, null, null);
		if (result != null) {
			String[] tokens = result.split("[ ]+");
			if (tokens.length == 4) {
				String[] newArray = new String[] { tokens[1], tokens[2], tokens[3] };
				tokens = newArray;
			}

			if (tokens.length == 3) {
				int[] color = new int[3];
				for(int i = 0; i < 3; i++){
					try {
						color[i] = Integer.parseInt(tokens[i]);
					} catch (Exception e) {
						informAboutWrongInput("\"" + tokens[i] + "\" is not a number.");
						return;
					}
	
				}
				
				if (color[0] < 0 || color[0] > 255)
					informAboutWrongInput("RED \"" + color[0] + "\" is nor between 0 and 255");
				else if (color[1] < 0 || color[1] > 255)
					informAboutWrongInput("GREEN \"" + color[1] + "\" is nor between 0 and 255");
				else if (color[2] < 0 || color[2] > 255)
					informAboutWrongInput("BLUE \"" + color[2] + "\" is nor between 0 and 255");
				else
					screen.SetBackground(new LevelConstantBackground(color[0], color[1], color[2]));
			} else {
				informAboutWrongInput("There has to be 3 tokens separated with empty spaces");
			}
		}
	}

	private void informAboutWrongInput(String message) {
		String messageOut = message != null ? message : "Wrong input";
		JOptionPane.showMessageDialog(null, messageOut, "Hey: ", JOptionPane.INFORMATION_MESSAGE);
	}
}
