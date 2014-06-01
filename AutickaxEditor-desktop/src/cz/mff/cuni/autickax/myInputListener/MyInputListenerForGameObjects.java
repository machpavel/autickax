package cz.mff.cuni.autickax.myInputListener;

import com.badlogic.gdx.scenes.scene2d.InputEvent;

import cz.mff.cuni.autickax.EditorScreen;
import cz.mff.cuni.autickax.EditorScreen.TypeOfObjectToDrag;
import cz.mff.cuni.autickax.constants.Constants;
import cz.mff.cuni.autickax.entities.Booster;
import cz.mff.cuni.autickax.entities.Fence;
import cz.mff.cuni.autickax.entities.GameObject;
import cz.mff.cuni.autickax.entities.Hill;
import cz.mff.cuni.autickax.entities.Hole;
import cz.mff.cuni.autickax.entities.House;
import cz.mff.cuni.autickax.entities.Mud;
import cz.mff.cuni.autickax.entities.ParkingCar;
import cz.mff.cuni.autickax.entities.Pneu;
import cz.mff.cuni.autickax.entities.RacingCar;
import cz.mff.cuni.autickax.entities.Stone;
import cz.mff.cuni.autickax.entities.Tornado;
import cz.mff.cuni.autickax.entities.Tree;
import cz.mff.cuni.autickax.entities.UniversalGameObject;
import cz.mff.cuni.autickax.entities.Wall;
import cz.mff.cuni.autickax.exceptions.IllegalGameObjectException;
import cz.mff.cuni.autickax.pathway.Arrow;

public class MyInputListenerForGameObjects extends MyInputListener {
	EditorScreen.TypeOfObjectToDrag typeOfClass;
	int type;

	public MyInputListenerForGameObjects(EditorScreen.TypeOfObjectToDrag typeOfClass, int type,
			EditorScreen screen) {
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
			this.screen.draggedObject = new Arrow(type);
			break;
		case UNIVERSAL:
			this.screen.draggedObject = new UniversalGameObject(x, y, type);
			break;
		default:
			throw new IllegalGameObjectException(typeOfClass.toString());
		}

		if (typeOfClass != TypeOfObjectToDrag.ARROW) {
			GameObject object = (GameObject) this.screen.draggedObject;
			object.setTexture();
			object.setCanBeDragged(true);
		} else {
			Arrow object = (Arrow) this.screen.draggedObject;
			object.setTexture();
			this.screen.lastArrowMoved = object;
		}
		this.screen.draggedNewObject = true;
		return super.touchDown(event, x, Constants.WORLD_HEIGHT - y, pointer, button);
	}
}
