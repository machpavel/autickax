package cz.mff.cuni.autickax.myInputListener;

import com.badlogic.gdx.scenes.scene2d.InputEvent;

import cz.mff.cuni.autickax.EditorScreen;
import cz.mff.cuni.autickax.constants.Constants;
import cz.mff.cuni.autickax.pathway.Arrow;

public class PlacedObjectsInputListener extends MyInputListener {
	private final Object representedObject;

	public PlacedObjectsInputListener(Object representedObject, EditorScreen screen) {
		super(screen);
		this.representedObject = representedObject;
	}

	@Override
	public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
		this.screen.draggedObject = representedObject;
		if(representedObject instanceof Arrow){
			this.screen.lastArrowMoved = (Arrow)representedObject;
		}
		return super.touchDown(event, x, Constants.WORLD_HEIGHT - y, pointer, button);
	}
}
