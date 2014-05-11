package cz.mff.cuni.autickax.dialogs;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

import cz.mff.cuni.autickax.constants.Constants;
import cz.mff.cuni.autickax.gamelogic.SubLevel;
import cz.mff.cuni.autickax.input.Input;
import cz.mff.cuni.autickax.scene.GameScreen;
import cz.mff.cuni.autickax.scene.ScreenInputListener;

public abstract class Comunicator extends SubLevel {
	protected TextureRegionDrawable backgroundTexture;
	protected DialogAbstractStatus status;
	protected SubLevel parent;

	public Comunicator(GameScreen gameScreen, SubLevel sublevel) {
		super(gameScreen);
		this.parent = sublevel;
		this.status = DialogAbstractStatus.IN_PROGRESS;
		this.stage = new Stage(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		this.stage.addListener(new ScreenInputListener(this.level));
		takeFocus();
	}


	@Override
	public void update(float delta) {
		stage.act(delta);
		// scene1 onDialogEnded.
	}

	@Override
	public void draw(SpriteBatch batch) {
		batch.begin();
		this.backgroundTexture.draw(batch, 0, 0, Constants.WORLD_WIDTH * Input.xStretchFactorInv,
				Constants.WORLD_HEIGHT * Input.yStretchFactorInv);
		batch.end();
		stage.draw();
	};

	public DialogAbstractStatus getStatus() {
		return this.status;
	}

	public void endCommunication() {
		this.status = DialogAbstractStatus.FINISHED;
		this.dispose();
	}

	public boolean isInProgress() {
		return getStatus() == DialogAbstractStatus.IN_PROGRESS;
	}

	public enum DialogAbstractStatus {
		IN_PROGRESS, FINISHED
	}
}
