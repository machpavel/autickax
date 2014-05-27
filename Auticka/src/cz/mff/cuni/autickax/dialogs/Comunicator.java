package cz.mff.cuni.autickax.dialogs;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ScalingViewport;

import cz.mff.cuni.autickax.constants.Constants;
import cz.mff.cuni.autickax.gamelogic.SubLevel;
import cz.mff.cuni.autickax.input.Input;
import cz.mff.cuni.autickax.scene.GameScreen;
import cz.mff.cuni.autickax.scene.ScreenInputListener;

public abstract class Comunicator extends SubLevel {
	protected NinePatchDrawable backgroundTexture;
	protected DialogAbstractStatus status;
	protected SubLevel parent;

	public Comunicator(GameScreen gameScreen, SubLevel sublevel) {
		super(gameScreen);

		this.parent = sublevel;
		this.status = DialogAbstractStatus.IN_PROGRESS;
		this.stage = new Stage(new ScalingViewport(Scaling.stretch, Constants.WORLD_WIDTH,
				Constants.WORLD_HEIGHT), this.level.getBatch());
		this.stage.addListener(new ScreenInputListener(this.level));
		takeFocus();
	}

	@Override
	public void update(float delta) {
		stage.act(delta);
		// scene1 onDialogEnded.
	}

	public void draw(SpriteBatch batch) {
		batch.begin();
		this.backgroundTexture.draw(batch, 0, 0, Constants.WORLD_WIDTH, Constants.WORLD_HEIGHT);
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
