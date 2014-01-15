package cz.mff.cuni.autickax.miniGames;

import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

import cz.mff.cuni.autickax.Autickax;
import cz.mff.cuni.autickax.Constants;
import cz.mff.cuni.autickax.Difficulty;
import cz.mff.cuni.autickax.dialogs.MessageDialog;
import cz.mff.cuni.autickax.entities.GearShiftMinigameFinish;
import cz.mff.cuni.autickax.entities.GearShifter;
import cz.mff.cuni.autickax.gamelogic.SubLevel;
import cz.mff.cuni.autickax.input.Input;
import cz.mff.cuni.autickax.scene.GameScreen;

public final class GearShiftMinigame extends Minigame {	
	private static final float FAIL_VALUE = Constants.GEAR_SHIFT_FAIL_VALUE;
	private static final float ROW_1 = Constants.GEAR_SHIFT_MINIGAME_ROW_1;
	private static final float ROW_2 = Constants.GEAR_SHIFT_MINIGAME_ROW_2;
	private static final float ROW_3 = Constants.GEAR_SHIFT_MINIGAME_ROW_3;
	private static final float COLUMN_1 = Constants.GEAR_SHIFT_MINIGAME_COLUMN_1;
	private static final float COLUMN_2 = Constants.GEAR_SHIFT_MINIGAME_COLUMN_2;
	private static final float COLUMN_3 = Constants.GEAR_SHIFT_MINIGAME_COLUMN_3;
	private static float MAX_DISTANCE_FROM_LINE;
	
	private States state = States.BEGINNING_STATE;
	private GearShifter gearShifter;
	private GearShiftMinigameFinish finish;

	public GearShiftMinigame(GameScreen screen, SubLevel parent) {
		super(screen, parent);
		setDifficulty(this.level.getDifficulty());		
		this.backgrountTexture = new TextureRegionDrawable(Autickax.getInstance().assets.getGraphics(Constants.GEAR_SHIFT_MINIGAME_BACKGROUND_TEXTURE));
				
		if(Autickax.showTooltips)
			this.parent.setDialog(new MessageDialog(screen, parent, Constants.TOOLTIP_MINIGAME_GEAR_SHIFT_WHAT_TO_DO));
		
		randomizeStartAndFinish(screen);
	}
	
	public void randomizeStartAndFinish(GameScreen screen){
		float[] rows = new float[] { ROW_1, ROW_2, ROW_3 };
		float[] columns = new float[] { COLUMN_1, COLUMN_2, COLUMN_3 };
		this.gearShifter = new GearShifter(columns[MathUtils.random(2)], rows[MathUtils.random(2)], this.level);		
		this.finish = new GearShiftMinigameFinish(columns[MathUtils.random(2)], rows[MathUtils.random(2)], screen);
	}

	@Override
	public void update(float delta) {		
		this.gearShifter.update(delta);			
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
		if (this.gearShifter.getPosition().dst(touchPos.x, touchPos.y) <= MAX_DISTANCE_FROM_LINE) {
			this.gearShifter.setDragged(true);
			state = States.DRIVING_STATE;
		}
	}
		
	}

	private void updateInDrivingState(float delta) {
		// Focus was lost
		if (!this.gearShifter.isDragged()) {
			// this.parent.setDialog(new DecisionDialog(this.level, this, "Pustil jsi auto", false));
			fail();
		}
		//Finish reached
		else if (this.gearShifter.positionCollides(finish)) {
			this.state = States.FINISH_STATE;
		}
		// Out of pathway
		else if (!isInGrid(this.gearShifter.getPosition().x, this.gearShifter.getPosition().y)) {
			fail();
		}		
	}
	

	private void fail(){
		//this.result = ResultType.PROCEEDED_WITH_VALUE;
		this.result = ResultType.FAILED;
		this.resultValue = FAIL_VALUE; 
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
		this.finish.draw(batch);
		this.gearShifter.draw(batch);
		batch.end();
	}

	@Override
	public void onDialogEnded() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onMinigameEnded() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void render() {
		// TODO Auto-generated method stub
		
	}
	
	private boolean isInGrid(float x, float y){
		// Vertical limits
		if( y > ROW_3 + MAX_DISTANCE_FROM_LINE || y < ROW_1 - MAX_DISTANCE_FROM_LINE)
			return false;
		
		// In first column
		if (x > COLUMN_1 - MAX_DISTANCE_FROM_LINE && x < COLUMN_1 + MAX_DISTANCE_FROM_LINE)
			return true;
		
		// In second column
		if (x > COLUMN_2 - MAX_DISTANCE_FROM_LINE && x < COLUMN_2 + MAX_DISTANCE_FROM_LINE)
			return true;
		
		// In second column
		if (x > COLUMN_3 - MAX_DISTANCE_FROM_LINE && x < COLUMN_3 + MAX_DISTANCE_FROM_LINE)
			return true;
		
		// Middle row
		if(x > COLUMN_1 - MAX_DISTANCE_FROM_LINE && x < COLUMN_3 + MAX_DISTANCE_FROM_LINE && 
				y > ROW_2 - MAX_DISTANCE_FROM_LINE && y < ROW_2 + MAX_DISTANCE_FROM_LINE)
			return true;
		
		return false;
	}
	

	private void setDifficulty(Difficulty difficulty) {
		switch (difficulty) {
		case Kiddie:
			MAX_DISTANCE_FROM_LINE = Constants.GEAR_SHIFT_MINIGAME_MAX_DISTANCE_FROM_LINE_KIDDIE;
			break;
		case Beginner:
			MAX_DISTANCE_FROM_LINE = Constants.GEAR_SHIFT_MINIGAME_MAX_DISTANCE_FROM_LINE_BEGINNER;
			break;
		case Normal:
			MAX_DISTANCE_FROM_LINE = Constants.GEAR_SHIFT_MINIGAME_MAX_DISTANCE_FROM_LINE_NORMAL;
			break;
		case Hard:
			MAX_DISTANCE_FROM_LINE = Constants.GEAR_SHIFT_MINIGAME_MAX_DISTANCE_FROM_LINE_HARD;
			break;
		case Extreme:
			MAX_DISTANCE_FROM_LINE = Constants.GEAR_SHIFT_MINIGAME_MAX_DISTANCE_FROM_LINE_EXTREME;
			break;
		default:
			//TODO assert
			break;
		}
		return;
	}

	
	private enum States {
		BEGINNING_STATE, DRIVING_STATE, FINISH_STATE;
	}
}
