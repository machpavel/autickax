package cz.mff.cuni.autickax.dialogs;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

import cz.mff.cuni.autickax.Constants;
import cz.mff.cuni.autickax.gamelogic.SubLevel;
import cz.mff.cuni.autickax.input.Input;
import cz.mff.cuni.autickax.scene.GameScreen;
import cz.mff.cuni.autickax.scene.ScreenInputListener;

public abstract class DialogAbstract extends SubLevel{
	protected TextureRegionDrawable backgrountTexture;
	protected Stage stage;
	protected DialogAbstractStatus status;
	
	
	
	public DialogAbstract(GameScreen gameScreen) {
		super(gameScreen);
		this.status = DialogAbstractStatus.IN_PROGRESS;
		stage = new Stage(Constants.WORLD_WIDTH, Constants.WORLD_HEIGHT);
		this.stage.addListener(new ScreenInputListener(this.Level));
		Gdx.input.setInputProcessor(stage);
		Gdx.input.setCatchBackKey(false);
	}
	
	@Override
	public void update(float delta){
		stage.act(delta);
	}

	@Override
	public void draw(SpriteBatch batch){
		batch.begin();
		this.backgrountTexture.draw(batch, (Constants.WORLD_WIDTH - Constants.DIALOG_WORLD_WIDTH) / 2, (Constants.WORLD_HEIGHT - Constants.DIALOG_WORLD_HEIGHT) / 2, Constants.DIALOG_WORLD_WIDTH* Input.xStretchFactorInv, Constants.DIALOG_WORLD_HEIGHT * Input.yStretchFactorInv);
		batch.end();
		stage.draw();
	};

	@Override
	abstract public void render();
	
	public DialogAbstractStatus getStatus(){
		return this.status;
	}
		
	
	public boolean isInProgress(){
		return getStatus() == DialogAbstractStatus.IN_PROGRESS;
	}
	
	public enum DialogAbstractStatus{
		IN_PROGRESS,
		FINISHED
	}
}
