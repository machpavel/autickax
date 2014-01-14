package cz.mff.cuni.autickax.miniGames;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

import cz.mff.cuni.autickax.Autickax;
import cz.mff.cuni.autickax.Constants;
import cz.mff.cuni.autickax.dialogs.MessageDialog;
import cz.mff.cuni.autickax.entities.Car;
import cz.mff.cuni.autickax.entities.Finish;
import cz.mff.cuni.autickax.entities.GameObject;
import cz.mff.cuni.autickax.gamelogic.SubLevel;
import cz.mff.cuni.autickax.input.Input;
import cz.mff.cuni.autickax.scene.GameScreen;

public final class AvoidObstaclesMinigame extends Minigame {
	private static final float CAR_START_POSITION_X = Constants.DIALOG_WORLD_X_OFFSET
			+ 30;
	private static final float FINISH_START_POSITION_X = Constants.WORLD_WIDTH
			- Constants.DIALOG_WORLD_X_OFFSET - 20;
	private static final int FINISH_TYPE = 1;
	private ArrayList<GameObject> gameObjects;
	private Car car;
	private Finish finish;
	States state = States.BEGINNING_STATE;

	public AvoidObstaclesMinigame(GameScreen gameScreen, SubLevel parent) {
		super(gameScreen, parent);
		this.backgrountTexture = new TextureRegionDrawable(
				Autickax.getInstance().assets
						.getGraphics(Constants.AVOID_OBSTACLES_MINIGAME_BACKGROUND_TEXTURE));

		if (Autickax.showTooltips)
			this.parent.setDialog(new MessageDialog(gameScreen, parent,
					Constants.TOOLTIP_MINIGAME_AVOID_OBSTACLES_WHAT_TO_DO));

		this.gameObjects = new ArrayList<GameObject>();
		generateObstacles(gameObjects);

		this.finish = new Finish(FINISH_START_POSITION_X,
				Constants.WORLD_HEIGHT / 2, gameScreen, FINISH_TYPE);
		this.car = new Car(CAR_START_POSITION_X, Constants.WORLD_HEIGHT / 2,
				gameScreen, 1);
		this.car.setDragged(false);
	}

	private void generateObstacles(ArrayList<GameObject> gameObjects) {

	}

	@Override
	public void update(float delta) {
		for (GameObject gameObject : this.level.getGameObjects()) {
			gameObject.update(delta);
		}
		this.car.update(delta);
		this.finish.update(delta);

		switch (state) {
		case BEGINNING_STATE:
			updateInBeginnigState(delta);
			break;
		case DRIVING_STATE:
			updateInDrivingState(delta);
			break;
		case FINISH_STATE:
			updateInFinishState(delta);
			break;
		default:
			// TODO implementation of exception
			break;
		}
	}

	private void updateInBeginnigState(float delta) {
		this.car.setRotation((this.car.getRotation() + delta * 50) % 360);
		if (Gdx.input.justTouched()) {
			Vector2 touchPos = new Vector2(Input.getX(), Input.getY());
			if (this.car.getPosition().dst(touchPos.x, touchPos.y) <= Constants.MAX_DISTANCE_FROM_PATHWAY) {
				this.car.setDragged(true);
				state = States.DRIVING_STATE;
			}
		}

	}

	private void updateInDrivingState(float delta) {
		// Focus was lost
		if (!this.car.isDragged()) {
			// this.parent.setDialog(new DecisionDialog(this.level, this, "Pustil jsi auto", false));
			this.status = DialogAbstractStatus.FINISHED;
			this.result = ResultType.FAILED;
			parent.onMinigameEnded();
		}
		//Finish reached
		else if (this.car.positionCollides(finish)) {
			this.state = States.FINISH_STATE;
			this.result = ResultType.PROCEEDED;
			parent.onMinigameEnded();
		}
		else {
			// Collision detection
			for (GameObject gameObject : this.level.getGameObjects()) {
				this.car.collides(gameObject);
				// this.parent.setDialog(new DecisionDialog(this.level, this, "Narazil jsi do kamene", false));
				this.status = DialogAbstractStatus.FINISHED;
				this.result = ResultType.FAILED;
			}
		}
	}

	private void updateInFinishState(float delta) {
		this.status = DialogAbstractStatus.FINISHED;
		this.result = ResultType.PROCEEDED;
		parent.onMinigameEnded();
	}


	@Override
	public void draw(SpriteBatch batch) {
		super.draw(batch);
		batch.begin();
		for (GameObject gameObject : this.level.getGameObjects()) {
			gameObject.draw(batch);
		}
		this.finish.draw(batch);
		this.car.draw(batch);
		batch.end();
	}

	@Override
	public void render() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onDialogEnded() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onMinigameEnded() {
		// TODO Auto-generated method stub

	}
	
	private enum States {
		BEGINNING_STATE, DRIVING_STATE, FINISH_STATE;
	}

}
