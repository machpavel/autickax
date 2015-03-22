package cz.cuni.mff.xcars.dialogs;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ScalingViewport;

import cz.cuni.mff.xcars.constants.Constants;
import cz.cuni.mff.xcars.debug.Debug;
import cz.cuni.mff.xcars.drawing.ShapeRendererStretched;
import cz.cuni.mff.xcars.gamelogic.SubLevel;
import cz.cuni.mff.xcars.scene.GameScreen;
import cz.cuni.mff.xcars.scene.ScreenInputListener;

public abstract class Comunicator extends SubLevel {
	protected ShapeRendererStretched shapeRenderer = new ShapeRendererStretched();
	protected DialogAbstractStatus status;
	protected SubLevel parent;

	public Comunicator(GameScreen gameScreen, SubLevel sublevel) {
		super(gameScreen);

		this.parent = sublevel;
		this.status = DialogAbstractStatus.IN_PROGRESS;
		this.stage = new Stage(new ScalingViewport(Scaling.stretch, Constants.WORLD_WIDTH, Constants.WORLD_HEIGHT),
				this.level.getBatch());
		this.stage.addListener(new ScreenInputListener(this.level));
		takeFocus();
	}

	@Override
	public void update(float delta) {
		stage.act(delta);
	}

	protected abstract void drawBackGroundTexture(SpriteBatch batch);

	public void draw(SpriteBatch batch) {
		drawBackGroundTexture(batch);
		stage.draw();

		if (Debug.DEBUG) {
			DrawDiagnostics(); // Enabled for each minigame
			if (Debug.drawMaxTouchableArea) {
				DrawMaxTouchableArea();
			}
		}
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

	// start DEBUG methods
	public void DrawDiagnostics() {
	}

	public void DrawMaxTouchableArea() {
	}
	// end of DEBUG methods
}
