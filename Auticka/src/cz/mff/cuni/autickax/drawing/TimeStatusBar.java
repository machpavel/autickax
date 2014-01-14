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
	private final Label timeLimitIntLabel;
	private final Label timeIntLabel;
	private final Label timeLimitStrLabel;
	private final TextureRegion line;

	private final int xStringLabels;
	private final int yLine;
	
	public TimeStatusBar(GameScreen level, float timeLimit)
	{
		bigfont.setScale(0.46527f);
		smallfont.setScale(0.3f);
		
		
		this.stage = new Stage(Constants.WORLD_WIDTH, Constants.WORLD_HEIGHT);
		
		timeStrLabel = new Label("time:",new LabelStyle(smallfont, Color.GRAY));
		timeLimitStrLabel = new Label("limit:",new LabelStyle(smallfont, Color.GRAY));
		timeLimitIntLabel = new Label(String.format("%1$,.1f", timeLimit) ,new LabelStyle(bigfont, Color.GRAY));
		timeIntLabel = new Label(String.format("%1$,.1f", 0.0f) ,new LabelStyle(bigfont, Color.GRAY));
		
		this.stage.addActor(timeStrLabel);
		this.stage.addActor(timeLimitIntLabel);
		this.stage.addActor(timeIntLabel);
		this.stage.addActor(timeLimitStrLabel);
		
		int distUpDown = 20;
		int distAdd = 75;
		yLine = Constants.WORLD_HEIGHT - 60;
		xStringLabels = Constants.WORLD_WIDTH - 150;
		timeStrLabel.setPosition(xStringLabels, yLine + distUpDown);
		timeLimitStrLabel.setPosition(xStringLabels,yLine - distUpDown);
		
		timeIntLabel.setPosition(xStringLabels + distAdd, yLine + distUpDown-5);
		timeLimitIntLabel.setPosition(xStringLabels + distAdd, yLine - distUpDown-5);
		
		line = level.getGame().assets.getGraphics("cara-88");
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
