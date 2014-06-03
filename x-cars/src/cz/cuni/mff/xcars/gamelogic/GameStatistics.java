package cz.cuni.mff.xcars.gamelogic;

import cz.cuni.mff.xcars.Difficulty;
import cz.cuni.mff.xcars.constants.Constants;

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
	
	private byte getNumberOfStars(float timeElapsed, float timeLimit)
	{
		float timeLimitAfterGlobalRegulation = timeLimit / Constants.misc.GLOBAL_SPEED_REGULATOR;
		
		float [] starsThresholds = {
				Constants.misc.STARS_THREE_TIME_THRESHOLD,
				Constants.misc.STARS_TWO_TIME_THRESHOLD,
				Constants.misc.STARS_ONE_TIME_THRESHOLD
			};
		
		byte res = 0;
		for (byte i = 0; i < starsThresholds.length && res == 0; i++)
		{
			if (timeElapsed <= timeLimitAfterGlobalRegulation *  starsThresholds[i])
			{
				res = (byte)(Constants.misc.STARS_MAX - i);
			}
		}
		return res;
	}
	
	public byte getNumberOfStars() {
		return this.getNumberOfStars(this.phase2ElapsedTime, this.phase1TimeLimit);
	}
	
	public int getScoreFromTime()
	{
		float normalizedElapsed = phase2ElapsedTime / Constants.misc.GLOBAL_SPEED_REGULATOR;
		int score = (int)(Constants.misc.SCORE_MULTIPLIER / normalizedElapsed);
		return score;
	}
	
	public float getTimeFromScore(int score)
	{
		float normalizedTime = (Constants.misc.SCORE_MULTIPLIER / (float)score);
		float resultTime = normalizedTime * Constants.misc.GLOBAL_SPEED_REGULATOR;
		return resultTime;
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
