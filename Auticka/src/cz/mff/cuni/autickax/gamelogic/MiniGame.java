package cz.mff.cuni.autickax.gamelogic;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import cz.mff.cuni.autickax.dialogs.DialogAbstract;
import cz.mff.cuni.autickax.scene.GameScreen;

public abstract class MiniGame extends DialogAbstract {

	public MiniGame(GameScreen gameScreen, SubLevel parent) {
		super(gameScreen, parent);
		// TODO Auto-generated constructor stub
	}

	@Override
	abstract public void update(float delta);

	@Override
	abstract public void draw(SpriteBatch batch);

	@Override
	abstract public void render();
}
