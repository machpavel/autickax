package cz.cuni.mff.xcars.myInputListener;

import com.badlogic.gdx.scenes.scene2d.InputEvent;

import cz.cuni.mff.xcars.EditorScreen;
import cz.cuni.mff.xcars.constants.Constants;
import cz.cuni.mff.xcars.entities.Arrow;
import cz.cuni.mff.xcars.entities.Booster;
import cz.cuni.mff.xcars.entities.Fence;
import cz.cuni.mff.xcars.entities.GameObject;
import cz.cuni.mff.xcars.entities.Hill;
import cz.cuni.mff.xcars.entities.Hole;
import cz.cuni.mff.xcars.entities.House;
import cz.cuni.mff.xcars.entities.Mud;
import cz.cuni.mff.xcars.entities.ParkingCar;
import cz.cuni.mff.xcars.entities.Pneu;
import cz.cuni.mff.xcars.entities.RacingCar;
import cz.cuni.mff.xcars.entities.Stone;
import cz.cuni.mff.xcars.entities.Tornado;
import cz.cuni.mff.xcars.entities.Tree;
import cz.cuni.mff.xcars.entities.UniversalGameObject;
import cz.cuni.mff.xcars.entities.Wall;
import cz.cuni.mff.xcars.exceptions.IllegalGameObjectException;

public class MyInputListenerForGameObjects extends MyInputListener {
	EditorScreen.TypeOfObjectToDrag typeOfClass;
	int type;

	public MyInputListenerForGameObjects(EditorScreen.TypeOfObjectToDrag typeOfClass, int type, EditorScreen screen) {
		super(screen);
		this.type = type;
		this.typeOfClass = typeOfClass;
	}

	@Override
	public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
		switch (typeOfClass) {
		case HOLE:
			this.screen.setDraggedObject(new Hole(x, y, type));
			break;
		case MUD:
			this.screen.setDraggedObject(new Mud(x, y, type));
			break;
		case STONE:
			this.screen.setDraggedObject(new Stone(x, y, type));
			break;
		case TREE:
			this.screen.setDraggedObject(new Tree(x, y, type));
			break;
		case BOOSTER:
			this.screen.setDraggedObject(new Booster(x, y, type));
			break;
		case FENCE:
			this.screen.setDraggedObject(new Fence(x, y, type));
			break;
		case HOUSE:
			this.screen.setDraggedObject(new House(x, y, type));
			break;
		case PARKING_CAR:
			this.screen.setDraggedObject(new ParkingCar(x, y, type));
			break;
		case WALL:
			this.screen.setDraggedObject(new Wall(x, y, type));
			break;
		case HILL:
			this.screen.setDraggedObject(new Hill(x, y, type));
			break;
		case TORNADO:
			this.screen.setDraggedObject(new Tornado(x, y, type));
			break;
		case RACING_CAR:
			this.screen.setDraggedObject(new RacingCar(x, y, type));
			break;
		case PNEU:
			this.screen.setDraggedObject(new Pneu(x, y, type));
			break;
		case ARROW:
			this.screen.setDraggedObject(new Arrow(x, y, type));
			break;
		case UNIVERSAL:
			this.screen.setDraggedObject(new UniversalGameObject(x, y, type));
			break;
		default:
			throw new IllegalGameObjectException(typeOfClass.toString());
		}

		GameObject object = this.screen.getDraggedObject();
		object.setTexture();
		object.setCanBeDragged(true);

		this.screen.setLastObjectMoved(this.screen.getDraggedObject());
		this.screen.setDraggingNewObject(true);
		return super.touchDown(event, x, Constants.WORLD_HEIGHT - y, pointer, button);
	}
}
