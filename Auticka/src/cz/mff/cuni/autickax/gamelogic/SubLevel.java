package cz.mff.cuni.autickax.gamelogic;

import java.util.Stack;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;

import cz.mff.cuni.autickax.Autickax;
import cz.mff.cuni.autickax.constants.Constants;
import cz.mff.cuni.autickax.dialogs.Dialog;
import cz.mff.cuni.autickax.dialogs.PauseDialog;
import cz.mff.cuni.autickax.entities.GameObject;
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
	
	public void setDialog(Dialog dialog){
		this.dialogStack.push(dialog);
	}
	
	public void takeFocus(){
		Gdx.input.setInputProcessor(this.stage);
		Gdx.input.setCatchBackKey(true);
	}
	
	protected void eraseDialog(){
		if(this.dialogStack.size() > 1){
			this.dialogStack.pop();
			this.dialogStack.peek().takeFocus();
		}
		else if (this.dialogStack.size() == 1){
			this.dialogStack.pop();
			if(this.miniGame != null)
				this.miniGame.takeFocus();
			else
				takeFocus();
		}
		else
			takeFocus();
		
	}
	protected void playSound(GameObject collisionOrigin)
	{
		if (Autickax.settings.playSounds) {
			String soundName = collisionOrigin.getSoundName();
			if (!soundName.equals(Constants.sounds.SOUND_NO_SOUND))
			{
				this.level.getGame().assets.soundAndMusicManager.playSound(soundName, Constants.sounds.SOUND_GAME_OBJECT_INTERACTION_DEFAULT_VOLUME);
			}
		}
	}
	
	protected void eraseMinigame(){
		this.miniGame= null;
		takeFocus();
	}
	
	public abstract void update(float delta);
	
	public abstract void draw(SpriteBatch batch);
	public abstract void render();
	
	public abstract void onDialogEnded();
	public abstract void onMinigameEnded();
	
	public void onPause(){
		if(this.dialogStack.isEmpty())
			this.dialogStack.push(new PauseDialog(this.level, this));
	}
}
