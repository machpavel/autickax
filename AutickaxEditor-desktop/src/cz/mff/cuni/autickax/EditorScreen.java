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
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.XmlWriter;

import cz.mff.cuni.autickax.Assets;
import cz.mff.cuni.autickax.entities.Car;
import cz.mff.cuni.autickax.entities.GameObject;
import cz.mff.cuni.autickax.entities.Mud;
import cz.mff.cuni.autickax.entities.Stone;
import cz.mff.cuni.autickax.entities.Tree;
import cz.mff.cuni.autickax.pathway.Pathway;
import cz.mff.cuni.autickax.pathway.Splines;

public class EditorScreen extends BaseScreenEditor {
	// Constants	
	static Pathway.PathwayType pathwayType = Pathway.PathwayType.OPENED;
	static Splines.TypeOfInterpolation typeOfInterpolation = Splines.TypeOfInterpolation.CUBIC_B_SPLINE;
	
	
	// Textures
	private TextureRegion backgroundTexture;
	private String backGroundTextureString = "sky";

	// Rendering
	private OrthographicCamera camera;
	protected SpriteBatch batch;
	protected ShapeRenderer shapeRenderer;

	// Entities
	private ArrayList<GameObject> gameObjects;
	private Car car;

	// Cached font
	private BitmapFont font;

	// Pathway
	private Pathway pathway;
	

	// Buttons
	Button buttonGeneratePoints;
	Button buttonRestart;

	public EditorScreen() {
		super();

		camera = new OrthographicCamera();
		camera.setToOrtho(false, Constants.WORLD_WIDTH, Constants.WORLD_HEIGHT);

		batch = new SpriteBatch();
		shapeRenderer = new ShapeRenderer();

		Assets assets = game.assets;
		this.backgroundTexture = assets.getGraphics(backGroundTextureString);

		
		// dummy code ------------------------>

		this.gameObjects = new ArrayList<GameObject>();

		// <------------------------ dummy code

		this.font = game.assets.getFont();

		// Pathway
		pathway = new Pathway(pathwayType, typeOfInterpolation);		
		
		game.assets.music.stop();
		

		// Car
		car = new Car(0, 0, null, 0);		

		// Start Music!
		game.assets.music.setLooping(true);
		game.assets.music.setVolume(0.3f);
		game.assets.music.play();
		game.assets.music.stop();

		createGenerateButton();
		createRestartButton();
		
		gameObjects.add(new Mud(600,100,null, 0));
		gameObjects.add(new Stone(200,200,null, 0));
		gameObjects.add(new Tree(300,300,null, 0));
	}

	private void update(float delta) {
		// Checks if user created a new point
		if (Gdx.input.justTouched()) {
			Vector2 point = new Vector2(Gdx.input.getX(), stageHeight - Gdx.input.getY());
			pathway.getControlPoints().add(point);
			game.assets.getSound(Constants.SOUND_HIT).play();
			generateXml("level.xml");
		}
	}

	@Override
	public void render(float delta) {
		stage.act(delta); // don't forget to advance the stage ( input + actions
		if (buttonGeneratePoints.isPressed() | buttonRestart.isPressed())
			return;

		update(delta);

		// Clears stage - unnecessary step because everything will be rewritten
		// by the distance map
		Gdx.gl.glClearColor(0f, 0f, 0f, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

		// Renders the distance map
		Gdx.gl10.glPointSize(1);
		shapeRenderer.begin(ShapeType.Point);
		for (int x = 0; x < (int) stageWidth; x++) {
			for (int y = 0; y < (int) stageHeight; y++) {
				float colorIntensity = 1 - (pathway.getDistanceMap().At(x, y) / Constants.MAX_DISTANCE_FROM_PATHWAY);
				shapeRenderer.setColor(colorIntensity, 0, 0, 1);
				shapeRenderer.point(x, y, 0);

			}
		}
		shapeRenderer.end();

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
		car.draw(batch);
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

			xml.element("backgroundTexture").attribute("textureName", backGroundTextureString).pop();

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
				new TextButtonStyle(new TextureRegionDrawable(
						game.assets.getGraphics("greenpixel")),
						new TextureRegionDrawable(game.assets
								.getGraphics("greenpixel")),
						new TextureRegionDrawable(game.assets
								.getGraphics("greenpixel")),
						game.assets.getFont()));

		buttonGeneratePoints.setPosition(0, 0);
		stage.addActor(buttonGeneratePoints);

		// User Input for Play Button
		buttonGeneratePoints.addListener(new InputListener() {
			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int button) {
				return true;
			}

			public void touchUp(InputEvent event, float x, float y,
					int pointer, int button) {				
				pathway.CreateDistances();
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

		buttonRestart.setPosition(0, buttonGeneratePoints.getY()
				+ buttonGeneratePoints.getHeight());
		stage.addActor(buttonRestart);

		// User Input for Play Button
		buttonRestart.addListener(new InputListener() {
			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int button) {
				return true;
			}

			public void touchUp(InputEvent event, float x, float y,
					int pointer, int button) {
				pathway = new Pathway(pathwayType, typeOfInterpolation);
			}
		});
	}
}
