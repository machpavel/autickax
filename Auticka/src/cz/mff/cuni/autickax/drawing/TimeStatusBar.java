package cz.mff.cuni.autickax.drawing;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;

import cz.mff.cuni.autickax.Autickax;
import cz.mff.cuni.autickax.Constants;
import cz.mff.cuni.autickax.scene.GameScreen;

public class TimeStatusBar {
 
	private final Stage stage;
	private final BitmapFont bigfont = Autickax.getInstance().assets.getTimeIntFont();
	private final BitmapFont smallfont = Autickax.getInstance().assets.getTimeStringFont();
	private final Label timeStrLabel;
	private  Label timeLimitIntLabel;
	private final Label timeIntLabel;
	private  Label timeLimitStrLabel;
	private final TextureRegion line;

	private final int xStringLabels;
	private final int yLine;
	
	public TimeStatusBar(GameScreen level, float timeLimit, boolean showLimit)
	{
		bigfont.setScale(0.46527f);
		smallfont.setScale(0.3f);
		
		
		this.stage = new Stage(Constants.WORLD_WIDTH, Constants.WORLD_HEIGHT);
		
		timeStrLabel = new Label("time:",new LabelStyle(smallfont, Color.GRAY));

		timeIntLabel = new Label(String.format("%1$,.1f", 0.0f) ,new LabelStyle(bigfont, Color.GRAY));
		
		int distUpDown = 20;
		int distAdd = 75;
		yLine = Constants.WORLD_HEIGHT - 60;
		xStringLabels = Constants.WORLD_WIDTH - 150;
		timeStrLabel.setPosition(xStringLabels, yLine + distUpDown);

		
		timeIntLabel.setPosition(xStringLabels + distAdd, yLine + distUpDown-5);

		
		line = level.getGame().assets.getGraphics("cara-88");
		
		this.stage.addActor(timeStrLabel);
		this.stage.addActor(timeIntLabel);
		if (showLimit)
		{
			timeLimitStrLabel = new Label("limit:",new LabelStyle(smallfont, Color.GRAY));
			timeLimitIntLabel = new Label(String.format("%1$,.1f", timeLimit) ,new LabelStyle(bigfont, Color.GRAY));
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
		stage.draw();
		batch.begin();
		batch.draw(line, xStringLabels, yLine + 10,150,line.getRegionY());
		batch.end();
	}
}
