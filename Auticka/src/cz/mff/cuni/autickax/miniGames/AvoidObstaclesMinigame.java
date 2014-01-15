package cz.mff.cuni.autickax.miniGames;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

import cz.mff.cuni.autickax.Autickax;
import cz.mff.cuni.autickax.Constants;
import cz.mff.cuni.autickax.Difficulty;
import cz.mff.cuni.autickax.dialogs.MessageDialog;
import cz.mff.cuni.autickax.entities.AvoidHole;
import cz.mff.cuni.autickax.entities.AvoidStone;
import cz.mff.cuni.autickax.entities.Car;
import cz.mff.cuni.autickax.entities.Finish;
import cz.mff.cuni.autickax.entities.GameObject;
import cz.mff.cuni.autickax.gamelogic.SubLevel;
import cz.mff.cuni.autickax.input.Input;
import cz.mff.cuni.autickax.scene.GameScreen;

public final class AvoidObstaclesMinigame extends Minigame {
	private static final float MINIMAL_DISTANCE_TO_SHOW_ROTATION = 2;
	private final float CAR_START_POSITION_X = Constants.AVOID_OBSTACLES_CAR_START_POSITION_X;
	private final float FINISH_START_POSITION_X = Constants.AVOID_OBSTACLES_FINISH_START_POSITION_X;
	private final int FINISH_TYPE = Constants.AVOID_OBSTACLES_FINISH_TYPE;	
	private float MINIMAL_DISTANCE_BETWEEN_OBSTACLES;
	private final float MINIMAL_DISTANCE_BETWEEN_CAR = Constants.AVOID_OBSTACLES_MINIMAL_DISTANCE_BETWEEN_CAR;
	private final float MINIMAL_DISTANCE_BETWEEN_FINISH = Constants.AVOID_OBSTACLES_MINIMAL_DISTANCE_BETWEEN_FINISH;
	private final int NUMBER_OF_TRIES_TO_GENERATE_OBSTACLE = Constants.AVOID_OBSTACLES_NUMBER_OF_TRIES_TO_GENERATE_OBSTACLE;
	
	private final float FAIL_VALUE = Constants.AVOID_HOLES_FAIL_VALUE;
	
	private ObstaclesType obstaclesType;
	private ArrayList<GameObject> gameObjects;
	private Car car;
	private Finish finish;
	States state = States.BEGINNING_STATE;
	private Vector2 lastCarPosition = new Vector2(0, Constants.WORLD_HEIGHT / 2);

	public AvoidObstaclesMinigame(GameScreen gameScreen, SubLevel parent, ObstaclesType obstacleType) {
		super(gameScreen, parent);						
		this.obstaclesType = obstacleType;
		
		setDifficulty(this.level.getDifficulty());
				
		this.backgrountTexture = new TextureRegionDrawable(Autickax.getInstance().assets.getGraphics(Constants.AVOID_OBSTACLES_MINIGAME_BACKGROUND_TEXTURE));

		if (Autickax.showTooltips)
			this.parent.setDialog(new MessageDialog(gameScreen, parent, Constants.TOOLTIP_MINIGAME_AVOID_OBSTACLES_WHAT_TO_DO));

		this.gameObjects = new ArrayList<GameObject>();
		
		this.finish = new Finish(FINISH_START_POSITION_X, Constants.WORLD_HEIGHT / 2, gameScreen, FINISH_TYPE);
		this.car = new Car(CAR_START_POSITION_X, Constants.WORLD_HEIGHT / 2, gameScreen, 1);
		this.car.setDragged(false);
		
		generateObstacles(gameObjects);
	}

	private void generateObstacles(ArrayList<GameObject> gameObjects) {
		int tryCount = 0;
		boolean limitReached = false;
		do {
			tryCount = 0;
			float xPosition = 0;
			float yPosition = 0;
			
			// Finds new position. It has to be farer from another obstacles.			
			boolean positionIsCorrect = true;
			do  {
				positionIsCorrect = true;
				
				xPosition = MathUtils.random(Constants.DIALOG_WORLD_WIDTH - 20) + Constants.DIALOG_WORLD_X_OFFSET + 10;
				yPosition = MathUtils.random(Constants.DIALOG_WORLD_HEIGHT - 20) + Constants.DIALOG_WORLD_Y_OFFSET + 10;
				Vector2 position = new Vector2(xPosition, yPosition);

				
				if(position.dst(car.getPosition()) < MINIMAL_DISTANCE_BETWEEN_CAR){
					positionIsCorrect = false;
				}
				else if(position.dst(finish.getPosition()) < MINIMAL_DISTANCE_BETWEEN_FINISH) {
					positionIsCorrect = false;
				}
				else{				
					for (GameObject gameObject : gameObjects) {

						if(position.dst(gameObject.getPosition()) < MINIMAL_DISTANCE_BETWEEN_OBSTACLES){
							positionIsCorrect = false;
							break;
						}
					}				
				}												
				++tryCount;
				limitReached = tryCount > NUMBER_OF_TRIES_TO_GENERATE_OBSTACLE;
			} while (!positionIsCorrect && !limitReached);
			
			if(!limitReached)
			{
				switch (obstaclesType) {
				case HOLES:
					int avoidHoleType = MathUtils.random(1, Constants.AVOID_HOLES_TYPES_COUNT);
					gameObjects.add(new AvoidHole(xPosition, yPosition, this.level, avoidHoleType));
					break;
				case STONES:
					int avoidStoneType = MathUtils.random(1, Constants.AVOID_STONE_TYPES_COUNT);
					gameObjects.add(new AvoidStone(xPosition, yPosition, this.level, avoidStoneType));
					break;
				default:
					//TODO assert type
					break;
				}				
			}					
		} while (!limitReached);
	}


	@Override
	public void update(float delta) {
		for (GameObject gameObject : gameObjects) {
			gameObject.update(delta);
		}
		this.car.update(delta);
				
		Vector2 carPosition = this.car.getPosition(); 
		if(carPosition.dst(lastCarPosition) > MINIMAL_DISTANCE_TO_SHOW_ROTATION){			
			this.car.setRotation(this.lastCarPosition.sub(carPosition).scl(-1).angle());
			this.lastCarPosition = carPosition;
		}
		
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
			fail();
		}
		//Finish reached
		else if (this.car.positionCollides(finish)) {
			this.state = States.FINISH_STATE;			
		}
		else {
			// Collision detection
			for (GameObject gameObject : gameObjects) {
				if(this.car.collides(gameObject)){
				// this.parent.setDialog(new DecisionDialog(this.level, this, "Narazil jsi do kamene", false));
				fail();
				break;
				}
			}
		}
	}
	
	private void fail(){
		this.status = DialogAbstractStatus.FINISHED;
		switch (obstaclesType) {
		case STONES:
			this.result = ResultType.FAILED;
			break;
		case HOLES:
			this.result = ResultType.PROCEEDED_WITH_VALUE;
			this.resultValue = FAIL_VALUE; 
			break;
		default:
			this.result = ResultType.FAILED;
			break;
		}		
		parent.onMinigameEnded();
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
		for (GameObject gameObject : gameObjects) {
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
	
	public enum States {
		BEGINNING_STATE, DRIVING_STATE, FINISH_STATE;
	}
	
	public enum ObstaclesType{
		STONES, HOLES
	}


	private void setDifficulty(Difficulty difficulty) {
		switch (difficulty) {
		case Kiddie:
			MINIMAL_DISTANCE_BETWEEN_OBSTACLES = Constants.AVOID_OBSTACLES_MINIMAL_DISTANCE_BETWEEN_OBSTACLES_KIDDIE;
			break;
		case Beginner:
			MINIMAL_DISTANCE_BETWEEN_OBSTACLES = Constants.AVOID_OBSTACLES_MINIMAL_DISTANCE_BETWEEN_OBSTACLES_BEGINNER;
			break;
		case Normal:
			MINIMAL_DISTANCE_BETWEEN_OBSTACLES = Constants.AVOID_OBSTACLES_MINIMAL_DISTANCE_BETWEEN_OBSTACLES_NORMAL;
			break;
		case Hard:
			MINIMAL_DISTANCE_BETWEEN_OBSTACLES = Constants.AVOID_OBSTACLES_MINIMAL_DISTANCE_BETWEEN_OBSTACLES_HARD;
			break;
		case Extreme:
			MINIMAL_DISTANCE_BETWEEN_OBSTACLES = Constants.AVOID_OBSTACLES_MINIMAL_DISTANCE_BETWEEN_OBSTACLES_EXTREME;
			break;
		default:
			MINIMAL_DISTANCE_BETWEEN_OBSTACLES = Constants.AVOID_OBSTACLES_MINIMAL_DISTANCE_BETWEEN_OBSTACLES_DEFAULT;
			break;
		}
		return;
	}

}
