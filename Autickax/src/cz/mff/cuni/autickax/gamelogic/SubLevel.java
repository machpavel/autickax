package cz.mff.cuni.autickax.gamelogic;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import cz.mff.cuni.autickax.scene.GameScreen;

public abstract class SubLevel {
	
	protected GameScreen Level;
	
	public SubLevel(GameScreen gameScreen) {
		this.Level = gameScreen;
	}
	
	public abstract void update(float delta);
	
	public abstract void draw(SpriteBatch batch);
}
