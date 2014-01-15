package cz.mff.cuni.autickax.miniGames;

public class MiniGameStatistics {
	private int succeeded = 0;
	private int failed = 0;
	private int collisions = 0;
	
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
	
	
}
