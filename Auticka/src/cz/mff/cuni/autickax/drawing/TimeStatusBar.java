package cz.mff.cuni.autickax.drawing;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;

import cz.mff.cuni.autickax.Autickax;
import cz.mff.cuni.autickax.constants.Constants;
import cz.mff.cuni.autickax.constants.TimeStatusBarConsts;
import cz.mff.cuni.autickax.input.Input;

public class TimeStatusBar extends Actor {
	private final BitmapFont bigfont = Autickax.getInstance().assets
			.getTimeIntFont();
	private final BitmapFont smallfont = Autickax.getInstance().assets
			.getTimeStringFont();
	private final Label timeStrLabel;
	private final Label timeIntLabel;
	private float timeLimit;
	
	private TimeStatusBarConsts consts = Constants.tsb;
	private ShapeRenderer renderer = new ShapeRenderer();
	private boolean countdown = true;

	public TimeStatusBar(float timeLimit) {

		bigfont.setScale(consts.BIG_FONT_SCALE*Input.xStretchFactorInv);
		smallfont.setScale(consts.SMALL_FONT_SCALE*Input.xStretchFactorInv);
		
		this.timeLimit = timeLimit;

		timeStrLabel = new Label("time:", new LabelStyle(smallfont, consts.black90));

		timeIntLabel = new Label(String.format("%1$,.1f", timeLimit),
				new LabelStyle(bigfont, consts.black90));

		timeStrLabel.setPosition(consts.xTimeStringLabel*Input.xStretchFactorInv, consts.yTimeStringLabel*Input.yStretchFactorInv);
		timeIntLabel.setPosition(consts.xTimeIntLabel*Input.xStretchFactorInv, consts.yTimeIntLabel*Input.yStretchFactorInv);
	}

	public void update(float elapsed) {
		float time = elapsed;
		if (countdown)
		{
			time = this.timeLimit - elapsed;
			if (time < 0)
				time = 0;
		}
		setTimeLabel(time);
	}
	
	private void setCountdown(boolean countdown) {
		this.countdown = countdown;
	}
	
	public void reset()
	{
		setTimeLabel(this.timeLimit);
		setCountdown(true);
	}
	
	public void setPhase2()
	{
		setCountdown(false);
		setTimeLabel(0f);
	}
	
	private void setTimeLabel(float time)
	{
		timeIntLabel.setText(String.format("%1$,.1f", time));
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {

		this.timeIntLabel.draw(batch, parentAlpha);
		this.timeStrLabel.draw(batch, parentAlpha);



		batch.end();
		
		Gdx.gl.glEnable(GL20.GL_BLEND);
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		renderer.begin(ShapeType.Filled);
		
		renderer.setColor(consts.black90alpha);
		renderer.rect(consts.xRectangle * Input.xStretchFactorInv,
				consts.yRectangle* Input.yStretchFactorInv,
				consts.RectWidth * Input.xStretchFactorInv, consts.RectHeight * Input.yStretchFactorInv);

		renderer.end();
		Gdx.gl.glDisable(GL20.GL_BLEND);

		batch.begin();
	}
}
