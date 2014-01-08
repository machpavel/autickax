package cz.mff.cuni.autickax.gamelogic;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import cz.mff.cuni.autickax.scene.GameScreen;

public abstract class SubLevel {
	protected GameScreen Level;
	protected ShapeRenderer shapeRenderer;
	public SubLevel(GameScreen gameScreen) {
		this.Level = gameScreen;
		shapeRenderer = new ShapeRenderer();
	}
	
	public abstract void update(float delta);
	
	public abstract void draw(SpriteBatch batch);
	public abstract void render();
}
