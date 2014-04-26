package cz.mff.cuni.autickax.miniGames;

import cz.mff.cuni.autickax.dialogs.Comunicator;
import cz.mff.cuni.autickax.gamelogic.SubLevel;
import cz.mff.cuni.autickax.scene.GameScreen;

public abstract class Minigame extends Comunicator {

	protected ResultType result = ResultType.UNASIGNED;
	protected float resultValue = -1;
	protected String resultMessage = null;
	
	public Minigame(GameScreen gameScreen, SubLevel parent) {
		super(gameScreen, parent);
	}
	
	public ResultType getResult(){
		return this.result;
	}
	public float getResultValue(){
		return this.resultValue;
	}
	
	public String getResultMessage(){
		return resultMessage;
	}
	
	public enum ResultType{
		FAILED, FAILED_WITH_VALUE, PROCEEDED_WITH_VALUE, PROCEEDED, UNASIGNED;
	}
}
