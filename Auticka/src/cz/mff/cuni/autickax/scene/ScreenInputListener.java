package cz.mff.cuni.autickax.scene;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;

public class ScreenInputListener extends InputListener {
	private final BaseScreen screen;
	
	public ScreenInputListener(BaseScreen screen) {
		this.screen = screen;
	}
	
	@Override
	public boolean keyUp(InputEvent event, int keycode) {
		if (keycode == Keys.BACK || keycode == Keys.ESCAPE) {
			this.screen.onBackKeyPressed();
		}
		
		return true;
	}
}
