package cz.mff.cuni.autickax.gamelogic;

import java.util.LinkedList;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import cz.mff.cuni.autickax.scene.GameScreen;

public class SubLevel2 extends SubLevel {

	private LinkedList<CheckPoint> checkpoints;
	private double pathFollowingAccuracy = 0;
	private double pathFollowingTime = 0;

	public SubLevel2(GameScreen gameScreen, LinkedList<CheckPoint> checkpoints, double pathFollowingAccuracy, double pathFollowingTime) {
		super(gameScreen);

		this.checkpoints = checkpoints;
		this.pathFollowingAccuracy = pathFollowingAccuracy;
		this.pathFollowingTime = pathFollowingTime;
	}

	@Override
	public void update(float time) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void draw(SpriteBatch sprite) {
		// TODO Auto-generated method stub
		
	}

}
