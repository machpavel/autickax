package cz.mff.cuni.autickax.myInputListener;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;

import cz.mff.cuni.autickax.EditorScreen;
import cz.mff.cuni.autickax.constants.Constants;
import cz.mff.cuni.autickax.entities.GameObject;

public class PlacedObjectsInputListener extends MyInputListener {
	private final GameObject representedObject;
	private final Button owner;

	public PlacedObjectsInputListener(GameObject representedObject,
			Button owner, EditorScreen screen) {
		super(screen);
		this.representedObject = representedObject;
		this.owner = owner;
	}

	@Override
	public void touchUp(InputEvent event, float x, float y, int pointer,
			int button) {
		this.representedObject.setPosition(new Vector2(Gdx.input.getX()
		, Constants.WORLD_HEIGHT - Gdx.input.getY()
		));
		super.touchUp(event, x, y, pointer, button);
	}

	@Override
	public boolean touchDown(InputEvent event, float x, float y, int pointer,
			int button) {

		this.screen.newObjectIsDragging = false;
		this.screen.draggedObject = null;
		this.screen.draggedButton = this.owner;
		this.screen.objectIsDragging = true;
		return super.touchDown(event, x, Constants.WORLD_HEIGHT - y, pointer,
				button);
	}
}
