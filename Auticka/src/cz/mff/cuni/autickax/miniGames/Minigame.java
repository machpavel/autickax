package cz.mff.cuni.autickax.miniGames;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import cz.mff.cuni.autickax.dialogs.Comunicator;
import cz.mff.cuni.autickax.gamelogic.SubLevel;
import cz.mff.cuni.autickax.scene.GameScreen;

public abstract class Minigame extends Comunicator {

	protected ResultType result;
	protected float resultValue;
	
	public Minigame(GameScreen gameScreen, SubLevel parent) {
		super(gameScreen, parent);
		// TODO Auto-generated constructor stub
	}

	@Override
	abstract public void update(float delta);

	@Override
	abstract public void draw(SpriteBatch batch);

	@Override
	abstract public void render();
	
	
	public ResultType getResult(){
		return this.result;
	}
	public float getResultValue(){
		return this.resultValue;
	}
	
	public enum ResultType{
		FAILED, PROCEEDED_WITH_VALUE, PROCEEDED;
	}
}
