package cz.cuni.mff.xcars.gamelogic;

import java.util.Stack;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;

import cz.cuni.mff.xcars.Xcars;
import cz.cuni.mff.xcars.dialogs.Dialog;
import cz.cuni.mff.xcars.dialogs.PauseDialog;
import cz.cuni.mff.xcars.miniGames.Minigame;
import cz.cuni.mff.xcars.scene.GameScreen;
import cz.cuni.mff.xcars.sfx.SoundAndMusicManager;

public abstract class SubLevel{
	protected GameScreen level;
	protected Stage stage;
	protected Stack<Dialog> dialogStack;
	protected Minigame miniGame = null;
	protected TyreTracks tyreTracks;
	protected SoundAndMusicManager soundsManager;

	public SubLevel(GameScreen gameScreen) {
		this.level = gameScreen;
		this.stage = gameScreen.getStage();
		setDialogStack(new Stack<Dialog>());
		this.soundsManager = Xcars.getInstance().assets.soundAndMusicManager;
	}

	public void setDialog(Dialog dialog) {
		this.getDialogStack().push(dialog);
	}

	public void takeFocus() {
		Gdx.input.setInputProcessor(this.stage);
		Gdx.input.setCatchBackKey(true);
	}

	protected void eraseDialog() {
		if (this.getDialogStack().size() > 1) {
			this.getDialogStack().pop();
			this.getDialogStack().peek().takeFocus();
		} else if (this.getDialogStack().size() == 1) {
			this.getDialogStack().pop();
			if (this.getMiniGame() != null)
				this.getMiniGame().takeFocus();
			else
				takeFocus();
		} else
			takeFocus();

	}

	protected void eraseMinigame() {
		if (this.getMiniGame() != null) {
			this.setMiniGame(null);
		}
		takeFocus();
	}

	public abstract void update(float delta);

	public void onDialogEnded() {
	};

	public void onMinigameEnded() {
	};

	public void pause() {
		if (!isPaused())
			this.getDialogStack().push(new PauseDialog(this.level, this));
	}

	public boolean isPaused() {
		if (!this.getDialogStack().isEmpty() && this.getDialogStack().peek() instanceof PauseDialog)
			return true;
		else
			return false;
	}

	public void resume() {
		if (isPaused())
			this.getDialogStack().peek().endCommunication();
	}

	public boolean isDialogStackEmpty() {
		return this.getDialogStack().isEmpty();
	}

	public void takeDialogFocus() {
		if (!getDialogStack().isEmpty()) {
			getDialogStack().peek().takeFocus();
		}
	}

	public boolean isMinigameRunning() {
		return getMiniGame() != null;
	}

	public void takeMinigameFocus() {
		if (getMiniGame() != null) {
			this.getMiniGame().takeFocus();
		}
	}

	/**
	 * Disposes necessary objects. For instance stages.
	 * 
	 * @param keepTheMainStage
	 *            determines if the main stage should be kept. Because in
	 *            sublebel1 and sublevel2 is shared stage with gameScreen
	 */
	public void dispose(boolean keepTheMainStage) {
		// Disposes everything. It causes exceptions if something is
		// already disposed, so it is necessary to have it in try-catch block.
		if (getDialogStack() != null)
			for (Dialog dialog : getDialogStack()) {
				try {
					dialog.dispose();
				} catch (Exception e) {
				}
			}
		if (getMiniGame() != null)
			try {
				getMiniGame().dispose();
			} catch (Exception e) {
			}
		if (!keepTheMainStage)
			try {
				this.stage.dispose();
			} catch (Exception e) {
			}
	}

	public void dispose() {
		dispose(false);
	}

	public Stack<Dialog> getDialogStack() {
		return dialogStack;
	}

	public void setDialogStack(Stack<Dialog> dialogStack) {
		this.dialogStack = dialogStack;
	}

	public Minigame getMiniGame() {
		return miniGame;
	}

	public void setMiniGame(Minigame miniGame) {
		this.miniGame = miniGame;
	}
}
