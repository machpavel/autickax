package cz.mff.cuni.autickax;

import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.XmlWriter;

import cz.mff.cuni.autickax.Assets;
import cz.mff.cuni.autickax.drawing.Background;
import cz.mff.cuni.autickax.entities.Car;
import cz.mff.cuni.autickax.entities.Finish;
import cz.mff.cuni.autickax.entities.GameObject;
import cz.mff.cuni.autickax.entities.Mud;
import cz.mff.cuni.autickax.entities.Start;
import cz.mff.cuni.autickax.entities.Stone;
import cz.mff.cuni.autickax.entities.Tree;
import cz.mff.cuni.autickax.pathway.Pathway;
import cz.mff.cuni.autickax.pathway.Splines;

public final class EditorScreen extends BaseScreenEditor {
	// Constants
	private static final int CAR_TYPE = 0;		
	private static final Pathway.PathwayType pathwayType = Pathway.PathwayType.OPENED;
	private static final Splines.TypeOfInterpolation typeOfInterpolation = Splines.TypeOfInterpolation.CUBIC_B_SPLINE;
	private static final int PATHWAY_TEXTURE_TYPE = 0;
	private static final float TIME_LIMIT = 5;	
	
	// Rendering
	private OrthographicCamera camera;
	private SpriteBatch batch;
	private ShapeRenderer shapeRenderer;

	// Entities
	private ArrayList<GameObject> gameObjects;
	private Car car;
	private Start start = new Start(0,0,null,1);
	private Finish finish = new Finish(0,0,null,1);

	// Cached font
	private BitmapFont font;

	// Pathway
	private Pathway pathway;
	
	// Background
	private Background background = new Background();
	

	// Buttons
	Button buttonGeneratePoints;
	Button buttonRestart;	
	ArrayList<Button> backgroundButtons = new ArrayList<Button>();
	private boolean anyButtonTouched = false;
	
	// Texture region for buttons
	TextureRegionDrawable trbGreenPixel;

	public EditorScreen() {
		super();

		camera = new OrthographicCamera();
		camera.setToOrtho(false, Constants.WORLD_WIDTH, Constants.WORLD_HEIGHT);

		batch = new SpriteBatch();
		shapeRenderer = new ShapeRenderer();						

		this.font = game.assets.getFont();
		trbGreenPixel = new TextureRegionDrawable(game.assets.getGraphics("greenpixel"));		
		
		createGenerateButton();
		createRestartButton();
		createBackgroundButtons();	
					
		// Car
		car = new Car(0, 0, null, CAR_TYPE);	
		
		restart();
	}
	
	public void restart()
	{
		background.SetType(1);
		
		// dummy code ------------------------>
		this.gameObjects = new ArrayList<GameObject>();
//		gameObjects.add(new Mud(600,100,null, 1));
//		gameObjects.add(new Stone(200,200,null, 1));
//		gameObjects.add(new Tree(300,300,null, 4));
		// <------------------------ dummy code

		// Pathway
		pathway = new Pathway(pathwayType, typeOfInterpolation);	
		
	}

	private void update(float delta) {
		// Checks if user created a new point
		if (Gdx.input.justTouched()) {
			Vector2 point = new Vector2(Gdx.input.getX(), stageHeight - Gdx.input.getY());
			pathway.getControlPoints().add(point);
			game.assets.getSound(Constants.SOUND_HIT).play();			
		}
	}

	@Override
	public void render(float delta) {
		stage.act(delta); // don't forget to advance the stage ( input + actions
		if (anyButtonTouched){
			anyButtonTouched = false;
			return;
		}
			

		update(delta);

		// Clears stage - unnecessary step because everything will be rewritten
		// by the distance map
		Gdx.gl.glClearColor(0.5f, 0.5f, 0.5f, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

		
		batch.begin();
		this.background.draw(batch, Constants.WORLD_WIDTH, Constants.WORLD_HEIGHT);
		batch.end();

		// Renders the distance map
		// See LevelPath.createBitmap()
		Gdx.gl.glEnable(GL10.GL_BLEND);
		Gdx.gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10. GL_ONE_MINUS_SRC_ALPHA);
		Gdx.gl10.glPointSize(1);
		shapeRenderer.begin(ShapeType.Point);
		for (int row = 0; row < Constants.WORLD_HEIGHT; ++row) {
			for (int column = 0; column < Constants.WORLD_WIDTH; ++column) {
				// distance map is flipped to the bitmap
				float distance = pathway.getDistanceMap().At(column, row);

				if (distance < Constants.MAX_SURFACE_DISTANCE_FROM_PATHWAY) {
					shapeRenderer.setColor(196.f/255, 154.f/255, 108.f/255, 1);
					shapeRenderer.point(column, row, 0);
				} else if (distance < Constants.MAX_DISTANCE_FROM_PATHWAY) {
					// cause this is where the transparency begin
					distance -= Constants.MAX_SURFACE_DISTANCE_FROM_PATHWAY;
					float alpha = (Constants.MAX_DISTANCE_FROM_PATHWAY - distance) / Constants.MAX_DISTANCE_FROM_PATHWAY;
					shapeRenderer.setColor(196.f/255, 154.f/255, 108.f/255, alpha);
					shapeRenderer.point(column, row, 0);
				}
			}
		}
		shapeRenderer.end();
		Gdx.gl.glDisable(GL10.GL_BLEND);
		

		// Renders control points
		Gdx.gl10.glPointSize(4);
		shapeRenderer.begin(ShapeType.Point);
		for (Vector2 point : pathway.getControlPoints()) {
			shapeRenderer.setColor(0.5f, 1f, 0.5f, 1);
			shapeRenderer.point(point.x, point.y, 0);
		}
		shapeRenderer.end();

		stage.draw(); // and also display it :)
		
		batch.begin();
		for (GameObject gameObject : gameObjects) {
			gameObject.draw(batch);
			
		}
		//car.draw(batch);
		batch.end();
	}

	public void generateXml(String fileName) {
		try {
			FileWriter fileWriter = new FileWriter("../Assets/levels/"
					+ fileName);
			StringWriter stringWriter = new StringWriter();
			XmlWriter xml = new XmlWriter(stringWriter);

			xml.element("AutickaX");

			xml.element("pathway");
			xml.attribute("pathwayType", pathway.getType().toString());
			xml.attribute("typeOfInterpolation", pathway.getTypeOfInterpolation().toString());
			xml.attribute("textureType", PATHWAY_TEXTURE_TYPE);
			xml.element("controlPoints");
			for (Vector2 point : pathway.getControlPoints()) {
				xml.element("point");
				xml.attribute("X", point.x);
				xml.attribute("Y", point.y);
				xml.pop();
			}
			xml.pop();
			xml.pop();

			xml.element("entities");
			for (GameObject gameObject : gameObjects) {
				gameObject.toXml(xml);
			}
			xml.pop();

			car.toXml(xml);
			start.toXml(xml);
			finish.toXml(xml);

			xml.element("levelBackgroundType", this.background.GetType());
			xml.element("timeLimit", TIME_LIMIT);

			xml.pop();
			xml.close();

			fileWriter.write(stringWriter.toString());
			fileWriter.close();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("Writing to XML unsuccesfull:");
			e.printStackTrace();
		}
	}


	public void createGenerateButton() {
		buttonGeneratePoints = new TextButton("Generate",
				new TextButtonStyle(trbGreenPixel,trbGreenPixel,trbGreenPixel, font));

		buttonGeneratePoints.setPosition(Constants.WORLD_WIDTH, 0);
		stage.addActor(buttonGeneratePoints);

		// User Input for Play Button
		buttonGeneratePoints.addListener(new InputListener() {
			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int button) {
				anyButtonTouched = true;
				return true;
			}

			public void touchUp(InputEvent event, float x, float y,
					int pointer, int button) {	
				if(pathway.getControlPoints().size() < 4) return;
				pathway.CreateDistances();
				start = new Start(pathway.GetPosition(Constants.START_POSITION_IN_CURVE).x, pathway.GetPosition(Constants.START_POSITION_IN_CURVE).y, null, 1);
				finish = new Finish(pathway.GetPosition(Constants.FINISH_POSITION_IN_CURVE).x, pathway.GetPosition(Constants.FINISH_POSITION_IN_CURVE).y, null, 1);
				generateXml("level.xml");
			}
		});
	}


	public void createRestartButton() {
		buttonRestart = new TextButton("Restart",
				new TextButtonStyle(new TextureRegionDrawable(
						game.assets.getGraphics("greenpixel")),
						new TextureRegionDrawable(game.assets
								.getGraphics("greenpixel")),
						new TextureRegionDrawable(game.assets
								.getGraphics("greenpixel")),
						game.assets.getFont()));

		buttonRestart.setPosition(Constants.WORLD_WIDTH, buttonGeneratePoints.getY()
				+ buttonGeneratePoints.getHeight());
		stage.addActor(buttonRestart);

		// User Input for Play Button
		buttonRestart.addListener(new InputListener() {
			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int button) {
				anyButtonTouched = true;
				return true;
			}

			public void touchUp(InputEvent event, float x, float y,
					int pointer, int button) {
				restart();
			}
		});
	}
	
	
	private void createBackgroundButtons() {		
		for (int i = 1; i <= 4; i++) {			
			TextureRegionDrawable trd = new TextureRegionDrawable(game.assets.getGraphics("levelBackground"+(i)+"small"));
			Button button =  new ImageButton(trd);
			button.setPosition(Constants.WORLD_WIDTH + (i - 1) * 30, Constants.WORLD_HEIGHT - 18);
			stage.addActor(button);
			
			// User Input for Play Button
			button.addListener(new MyInputListener(i) {				
				public boolean touchDown(InputEvent event, float x, float y,
						int pointer, int button) {
					anyButtonTouched = true;
					return true;
				}

				public void touchUp(InputEvent event, float x, float y,
						int pointer, int button) {
					background.SetType(i);
				}
			});
		}								
	}

}
