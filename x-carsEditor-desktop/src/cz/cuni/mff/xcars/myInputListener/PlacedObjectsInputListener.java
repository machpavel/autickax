package cz.cuni.mff.xcars.myInputListener;

import com.badlogic.gdx.scenes.scene2d.InputEvent;

import cz.cuni.mff.xcars.EditorScreen;
import cz.cuni.mff.xcars.constants.Constants;
import cz.cuni.mff.xcars.entities.GameObject;

public class PlacedObjectsInputListener extends MyInputListener {
	private final GameObject representedObject;

	public PlacedObjectsInputListener(GameObject representedObject, EditorScreen screen) {
		super(screen);
		this.representedObject = representedObject;
	}

	@Override
	public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
		this.screen.setDraggedObject(representedObject);
		this.screen.setLastObjectMoved(representedObject);
		return super.touchDown(event, x, Constants.WORLD_HEIGHT - y, pointer, button);
	}

	@Override
	public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
		if (!this.screen.isInWorld(this.representedObject)) {
			this.screen.removeObject(this.representedObject);
			this.screen.setLastObjectMoved(null);
		}
		super.touchUp(event, x, y, pointer, button);
	}
}
