package cz.mff.cuni.autickax.gamelogic;

import cz.mff.cuni.autickax.Constants;
import cz.mff.cuni.autickax.Difficulty;

public class GameStatistics {
	private int succeeded = 0;
	private int failed = 0;
	private int collisions = 0;
	private float phase1TimeLimit = 0;
	private float phase1ElapsedTime = 0;
	private float phase2ElapsedTime = 0;
	private Difficulty difficulty = null;
	
	GameStatistics(Difficulty difficulty, float phase1TimeLimit){
		this.difficulty = difficulty;
		this.phase1TimeLimit = phase1TimeLimit;
	}
	
	public void increaseSucceeded()
	{
		succeeded++;
	}
	
	public void increaseFailed()
	{
		failed++;
	}
	
	public void increaseCollisions()
	{
		collisions++;
	}
	
	public void increasePhase1ElapsedTime(float delta){
		this.phase1ElapsedTime += delta;
	}
	
	public void increasePhase2ElapsedTime(float delta){
		this.phase2ElapsedTime += delta;
	}
	
	public int getSucceeded()
	{
		return succeeded;
	}
	
	public int getFailed()
	{
		return failed;
	}
	
	public int getCollisions()
	{
		return collisions;
	}
	
	public int getNOfStars(float timeElapsed, float timeLimit)
	{
		float timeLimitAfterGlobalRegulation = timeLimit / Constants.GLOBAL_SPEED_REGULATOR;
		float [] starsThresholds = {Constants.STARS_THREE_TIME_THRESHOLD, Constants.STARS_TWO_TIME_THRESHOLD, Constants.STARS_ONE_TIME_THRESHOLD};
		int res = 0;
		for (int i = 0; i < starsThresholds.length && res == 0; i++)
		{
			if (timeElapsed <= timeLimitAfterGlobalRegulation *  starsThresholds[i])
			{
				res = Constants.STARS_MAX - i;
			}
		}
		return res;
	}

	public float getPhase1TimeLimit() {
		return phase1TimeLimit;
	}

	public float getPhase1ElapsedTime() {
		return phase1ElapsedTime;
	}

	public float getPhase2ElapsedTime() {
		return phase2ElapsedTime;
	}

	public Difficulty getDifficulty() {
		return difficulty;
	}

	public void reset() {
		succeeded = 0;
		failed = 0;
		collisions = 0;		
		phase1ElapsedTime = 0;
		phase2ElapsedTime = 0;		
	}
	
}
