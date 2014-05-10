package cz.mff.cuni.autickax.gamelogic;

import java.util.Stack;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;

import cz.mff.cuni.autickax.dialogs.Dialog;
import cz.mff.cuni.autickax.dialogs.PauseDialog;
import cz.mff.cuni.autickax.miniGames.Minigame;
import cz.mff.cuni.autickax.scene.GameScreen;

public abstract class SubLevel {
	protected GameScreen level;
	protected Stage stage;
	protected ShapeRenderer shapeRenderer;
	protected Stack<Dialog> dialogStack;
	protected Minigame miniGame = null;

	public SubLevel(GameScreen gameScreen) {
		this.level = gameScreen;
		this.stage = gameScreen.getStage();
		shapeRenderer = new ShapeRenderer();
		dialogStack = new Stack<Dialog>();
	}

	public void setDialog(Dialog dialog) {
		this.dialogStack.push(dialog);
	}

	public void takeFocus() {
		Gdx.input.setInputProcessor(this.stage);
		Gdx.input.setCatchBackKey(true);
	}

	protected void eraseDialog() {
		if (this.dialogStack.size() > 1) {
			this.dialogStack.pop();
			this.dialogStack.peek().takeFocus();
		} else if (this.dialogStack.size() == 1) {
			this.dialogStack.pop();
			if (this.miniGame != null)
				this.miniGame.takeFocus();
			else
				takeFocus();
		} else
			takeFocus();

	}

	protected void eraseMinigame() {
		this.miniGame = null;
		takeFocus();
	}

	public void update(float delta) {
	};

	public void draw(SpriteBatch batch) {
	};

	public void render() {
	};

	public void onDialogEnded() {
	};

	public void onMinigameEnded() {
	};

	public void pause() {
		if (!isPaused())
			this.dialogStack.push(new PauseDialog(this.level, this));
	}

	public boolean isPaused() {
		if (!this.dialogStack.isEmpty() && this.dialogStack.peek() instanceof PauseDialog)
			return true;
		else
			return false;
	}

	public void resume() {
		if (isPaused())
			this.dialogStack.pop();
	}

	public boolean isDialogStackEmpty() {
		return this.dialogStack.isEmpty();
	}

	public void takeDialogFocus() {
		if (!dialogStack.isEmpty()) {
			dialogStack.peek().takeFocus();
		}
	}

	public boolean isMinigameRunning() {
		return miniGame != null;
	}

	public void takeMinigameFocus() {
		if (miniGame != null) {
			this.miniGame.takeFocus();
		}
	}
}
