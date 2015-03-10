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
			this.screen.draggedObject = new Hole(x, y, type);
			break;
		case MUD:
			this.screen.draggedObject = new Mud(x, y, type);
			break;
		case STONE:
			this.screen.draggedObject = new Stone(x, y, type);
			break;
		case TREE:
			this.screen.draggedObject = new Tree(x, y, type);
			break;
		case BOOSTER:
			this.screen.draggedObject = new Booster(x, y, type);
			break;
		case FENCE:
			this.screen.draggedObject = new Fence(x, y, type);
			break;
		case HOUSE:
			this.screen.draggedObject = new House(x, y, type);
			break;
		case PARKING_CAR:
			this.screen.draggedObject = new ParkingCar(x, y, type);
			break;
		case WALL:
			this.screen.draggedObject = new Wall(x, y, type);
			break;
		case HILL:
			this.screen.draggedObject = new Hill(x, y, type);
			break;
		case TORNADO:
			this.screen.draggedObject = new Tornado(x, y, type);
			break;
		case RACING_CAR:
			this.screen.draggedObject = new RacingCar(x, y, type);
			break;
		case PNEU:
			this.screen.draggedObject = new Pneu(x, y, type);
			break;
		case ARROW:
			this.screen.draggedObject = new Arrow(x, y, type);
			break;
		case UNIVERSAL:
			this.screen.draggedObject = new UniversalGameObject(x, y, type);
			break;
		default:
			throw new IllegalGameObjectException(typeOfClass.toString());
		}

		GameObject object = this.screen.draggedObject;
		object.setTexture();
		object.setCanBeDragged(true);

		this.screen.lastObjectMoved = this.screen.draggedObject;
		this.screen.draggingNewObject = true;
		return super.touchDown(event, x, Constants.WORLD_HEIGHT - y, pointer, button);
	}
}
