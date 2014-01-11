package cz.mff.cuni.autickax.myInputListener;

import com.badlogic.gdx.scenes.scene2d.InputEvent;

import cz.mff.cuni.autickax.Constants;
import cz.mff.cuni.autickax.EditorScreen;
import cz.mff.cuni.autickax.entities.Hole;
import cz.mff.cuni.autickax.entities.Mud;
import cz.mff.cuni.autickax.entities.Stone;
import cz.mff.cuni.autickax.entities.Tree;

public class MyInputListenerForGameObjects extends MyInputListener {
	EditorScreen.TypeOfGameObjectButton typeOfClass;
	int type;
	public MyInputListenerForGameObjects(EditorScreen.TypeOfGameObjectButton typeOfClass, int type, EditorScreen screen) {
		super(screen);
		this.type = type;
		this.typeOfClass = typeOfClass;
	}
	
	@Override
	public boolean touchDown(InputEvent event, float x, float y, int pointer,
			int button) {
		switch (typeOfClass) {
		case HOLE:
			this.screen.draggedObject = new Hole(x ,y, null, type);
			break;
		case MUD:
			this.screen.draggedObject = new Mud(x ,y, null, type);
			break;
		case STONE:
			this.screen.draggedObject = new Stone(x ,y, null, type);
			break;
		case TREE:
			this.screen.draggedObject = new Tree(x ,y, null, type);
			break;
		default:
			break;
		}
		this.screen.objectIsDragging = true;
		return super.touchDown(event, x, Constants.WORLD_HEIGHT - y, pointer, button);
	}
}
