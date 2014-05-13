package cz.mff.cuni.autickax.drawing;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;

import cz.mff.cuni.autickax.Autickax;
import cz.mff.cuni.autickax.constants.Constants;
import cz.mff.cuni.autickax.input.Input;

public class TimeStatusBar extends Actor {
	private final BitmapFont bigfont = Autickax.getInstance().assets
			.getTimeIntFont();
	private final BitmapFont smallfont = Autickax.getInstance().assets
			.getTimeStringFont();
	private final Label timeStrLabel;
	private Label timeLimitIntLabel;
	private final Label timeIntLabel;
	private Label timeLimitStrLabel;
	// private final TextureRegion line;

	private final int xStringLabels = Constants.WORLD_WIDTH - 150;
	private final int yLine = Constants.WORLD_HEIGHT - 70;
	private ShapeRenderer renderer = new ShapeRenderer();
	private static final Color black90 = new Color(0.1f, 0.1f, 0.1f, 1f);
	private static final Color black90alpha = new Color(0.1f, 0.1f, 0.1f, 0.1f);

	private boolean showTimeLimit = true;

	public TimeStatusBar(float timeLimit) {

		bigfont.setScale(0.46527f);
		smallfont.setScale(0.3f);

		timeStrLabel = new Label("time:", new LabelStyle(smallfont, black90));

		timeIntLabel = new Label(String.format("%1$,.1f", 0.0f),
				new LabelStyle(bigfont, black90));

		int distUpDown = 20;
		int distAdd = 75;
		
		timeStrLabel.setPosition(xStringLabels, yLine + distUpDown);

		timeIntLabel.setPosition(xStringLabels + distAdd, yLine + distUpDown
				- 5);

		timeLimitStrLabel = new Label("limit:", new LabelStyle(smallfont,
				black90));
		
		timeLimitIntLabel = new Label(String.format("%1$,.1f", timeLimit),
				new LabelStyle(bigfont, black90));
		timeLimitStrLabel.setPosition(xStringLabels, yLine - distUpDown);
		timeLimitIntLabel.setPosition(xStringLabels + distAdd, yLine
				- distUpDown - 5);
	}

	public void update(float elapsed) {
		timeIntLabel.setText(String.format("%1$,.1f", elapsed));
	}
	
	public void setShowTimeLimit(boolean showTimeLimit) {
		this.showTimeLimit = showTimeLimit;
	}

	@Override
	public void draw(SpriteBatch batch, float parentAlpha) {

		this.timeIntLabel.draw(batch, parentAlpha);
		this.timeStrLabel.draw(batch, parentAlpha);

		if (this.showTimeLimit) {
			this.timeLimitIntLabel.draw(batch, parentAlpha);
			this.timeLimitStrLabel.draw(batch, parentAlpha);
		}

		batch.end();
		
		Gdx.gl.glEnable(GL10.GL_BLEND);
		Gdx.gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		renderer.begin(ShapeType.Filled);
		
		renderer.setColor(black90alpha);
		renderer.rect((xStringLabels - 10) * Input.xStretchFactorInv,
				(yLine - 15) * Input.yStretchFactorInv,
				200 * Input.xStretchFactorInv, 200 * Input.yStretchFactorInv);
		
		renderer.setColor(black90);
		renderer.rect(xStringLabels * Input.xStretchFactorInv, (yLine + 28)
				* Input.yStretchFactorInv, 150 * Input.xStretchFactorInv,
				3 * Input.yStretchFactorInv);
		renderer.end();
		Gdx.gl.glDisable(GL10.GL_BLEND);

		batch.begin();
	}
}
