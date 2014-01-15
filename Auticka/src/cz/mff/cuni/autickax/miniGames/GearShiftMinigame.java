package cz.mff.cuni.autickax.miniGames;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

import cz.mff.cuni.autickax.Autickax;
import cz.mff.cuni.autickax.Constants;
import cz.mff.cuni.autickax.dialogs.MessageDialog;
import cz.mff.cuni.autickax.gamelogic.SubLevel;
import cz.mff.cuni.autickax.scene.GameScreen;

public final class GearShiftMinigame extends Minigame {

	public GearShiftMinigame(GameScreen screen, SubLevel parent) {
		super(screen, parent);
		this.backgrountTexture = new TextureRegionDrawable(Autickax.getInstance().assets.getGraphics(Constants.GEAR_SHIFT_MINIGAME_BACKGROUND_TEXTURE));
				
		if(Autickax.showTooltips)
			this.parent.setDialog(new MessageDialog(screen, parent, Constants.TOOLTIP_MINIGAME_GEAR_SHIFT_WHAT_TO_DO));
	}

	@Override
	public void update(float delta) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void draw(SpriteBatch batch) {
		super.draw(batch);			
	}

	@Override
	public void onDialogEnded() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onMinigameEnded() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void render() {
		// TODO Auto-generated method stub
		
	}
	
}
