package cz.mff.cuni.autickax.gamelogic;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import cz.mff.cuni.autickax.dialogs.Dialog;
import cz.mff.cuni.autickax.miniGames.Minigame;
import cz.mff.cuni.autickax.scene.GameScreen;

public abstract class SubLevel {
	protected GameScreen level;
	protected ShapeRenderer shapeRenderer;
	protected Dialog dialog = null;
	protected Minigame miniGame = null;
	
	public SubLevel(GameScreen gameScreen) {
		this.level = gameScreen;
		shapeRenderer = new ShapeRenderer();		
	}
	
	public void setDialog(Dialog dialog){
		this.dialog = dialog;
	}
	
	public void takeFocus(){
		Gdx.input.setInputProcessor(this.level.getStage());
		Gdx.input.setCatchBackKey(true);
	}
	
	protected void eraseDialog(){
		this.dialog = null;
		takeFocus();
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
}
