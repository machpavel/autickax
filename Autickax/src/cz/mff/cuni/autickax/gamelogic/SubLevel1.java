package cz.mff.cuni.autickax.gamelogic;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import cz.mff.cuni.autickax.scene.GameScreen;

public class SubLevel1 extends SubLevel {

	// Score
	private int score = 0;
	// Time
	private float timeElapsed = 0;
	
	public SubLevel1(GameScreen gameScreen) {
		super(gameScreen);
	}

	@Override
	public void update(float delta) {
		timeElapsed += delta;
		this.Level.getCar().update(delta);
	}

	@Override
	public void draw(SpriteBatch batch) {
		BitmapFont font = this.Level.getFont();
		float stageHeight = this.Level.getStageHeight();
		float stageWidth = this.Level.getStageWidth();
		
		this.Level.getCar().draw(batch);
		
		// Draw score
		font.draw(batch, "score: " + score, 10, (int)stageHeight-32);
		// Draw time
		font.draw(batch, "time: "+ ( (int) timeElapsed ), (int) stageWidth/2, (int)stageHeight-32);
		
	}

}
