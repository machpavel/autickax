package cz.mff.cuni.autickax.drawing;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;

import cz.mff.cuni.autickax.Autickax;
import cz.mff.cuni.autickax.constants.Constants;
import cz.mff.cuni.autickax.input.Input;
import cz.mff.cuni.autickax.scene.GameScreen;

public class TimeStatusBar {
 
	private final Stage stage;
	private final BitmapFont bigfont = Autickax.getInstance().assets.getTimeIntFont();
	private final BitmapFont smallfont = Autickax.getInstance().assets.getTimeStringFont();
	private final Label timeStrLabel;
	private  Label timeLimitIntLabel;
	private final Label timeIntLabel;
	private  Label timeLimitStrLabel;
	//private final TextureRegion line;

	private final int xStringLabels;
	private final int yLine;
	private ShapeRenderer renderer = new ShapeRenderer();
	private final Color black90 = new Color(0.1f, 0.1f, 0.1f, 1f);
	private final Color black90alpha = new Color(0.1f, 0.1f, 0.1f, 0.1f);
	
	public TimeStatusBar(GameScreen level, float timeLimit, boolean showLimit)
	{
		bigfont.setScale(0.46527f);
		smallfont.setScale(0.3f);
		
		
		this.stage = new Stage(Constants.WORLD_WIDTH, Constants.WORLD_HEIGHT);
		
		timeStrLabel = new Label("time:",new LabelStyle(smallfont, black90));

		timeIntLabel = new Label(String.format("%1$,.1f", 0.0f) ,new LabelStyle(bigfont, black90));
		
		int distUpDown = 20;
		int distAdd = 75;
		yLine = Constants.WORLD_HEIGHT - 60;
		xStringLabels = Constants.WORLD_WIDTH - 150;
		timeStrLabel.setPosition(xStringLabels, yLine + distUpDown);

		
		timeIntLabel.setPosition(xStringLabels + distAdd, yLine + distUpDown-5);
		
		this.stage.addActor(timeStrLabel);
		this.stage.addActor(timeIntLabel);
		if (showLimit)
		{
			timeLimitStrLabel = new Label("limit:",new LabelStyle(smallfont, black90));
			timeLimitIntLabel = new Label(String.format("%1$,.1f", timeLimit) ,new LabelStyle(bigfont,black90));
			this.stage.addActor(timeLimitIntLabel);
			this.stage.addActor(timeLimitStrLabel);
			timeLimitStrLabel.setPosition(xStringLabels,yLine - distUpDown);
			timeLimitIntLabel.setPosition(xStringLabels + distAdd, yLine - distUpDown-5);
		}
		

	}
	
	public TimeStatusBar(GameScreen level)
	{
		this(level, 0, false);
	}
	
	public void update(float elapsed)
	{
		stage.act();
		timeIntLabel.setText(String.format("%1$,.1f", elapsed));
	}
	
	public void draw(SpriteBatch batch)
	{
		 
		Gdx.gl.glEnable(GL10.GL_BLEND);
		Gdx.gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		renderer.begin(ShapeType.Filled);
		renderer.setColor(black90alpha);
		renderer.rect((xStringLabels - 10)*Input.xStretchFactorInv, (yLine - 40)*Input.yStretchFactorInv, 200*Input.xStretchFactorInv, 200*Input.yStretchFactorInv);
		renderer.setColor(black90);
		renderer.rect(xStringLabels* Input.xStretchFactorInv, (yLine + 10)* Input.yStretchFactorInv,150*Input.xStretchFactorInv,3*Input.yStretchFactorInv);
		renderer.end();
		Gdx.gl.glDisable(GL10.GL_BLEND);

		stage.draw();

	}
}
