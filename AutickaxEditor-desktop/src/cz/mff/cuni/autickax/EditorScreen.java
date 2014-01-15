package cz.mff.cuni.autickax;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.XmlWriter;

import cz.mff.cuni.autickax.drawing.LevelBackground;
import cz.mff.cuni.autickax.entities.Car;
import cz.mff.cuni.autickax.entities.Finish;
import cz.mff.cuni.autickax.entities.GameObject;
import cz.mff.cuni.autickax.entities.Hole;
import cz.mff.cuni.autickax.entities.Mud;
import cz.mff.cuni.autickax.entities.Start;
import cz.mff.cuni.autickax.entities.Stone;
import cz.mff.cuni.autickax.entities.Tree;
import cz.mff.cuni.autickax.myInputListener.MyInputListener;
import cz.mff.cuni.autickax.myInputListener.MyInputListenerForBackground;
import cz.mff.cuni.autickax.myInputListener.MyInputListenerForGameObjects;
import cz.mff.cuni.autickax.myInputListener.PlacedObjectsInputListener;
import cz.mff.cuni.autickax.pathway.Pathway;
import cz.mff.cuni.autickax.pathway.Splines;
import cz.mff.cuni.autickax.pathway.Vector2i;

public final class EditorScreen extends BaseScreenEditor {
	// Constants
	private static final int CAR_TYPE = 1;
	private static final Pathway.PathwayType pathwayType = Pathway.PathwayType.OPENED;
	private static final Splines.TypeOfInterpolation typeOfInterpolation = Splines.TypeOfInterpolation.CUBIC_B_SPLINE;
	private static final int PATHWAY_TEXTURE_TYPE = 0;
	private static final float TIME_LIMIT = 10;
	protected static final int FINISH_TYPE = 2;
	protected static final int START_TYPE = 4;


	// Rendering
	private OrthographicCamera camera;
	private SpriteBatch batch;
	private ShapeRenderer shapeRenderer;

	// Entities
	private ArrayList<GameObject> gameObjects;
	private Car car = new Car(0, 0, null, CAR_TYPE);
	private Start start;
	private Finish finish;

	// Cached font
	private BitmapFont font;

	// Pathway
	private Pathway pathway;

	// Background
	private LevelBackground background = new LevelBackground();

	// Buttons
	Button buttonGeneratePoints;
	Button buttonRestart;
	Button buttonSave;
	ArrayList<Button> backgroundButtons = new ArrayList<Button>();
	private boolean anyButtonTouched = false;
	ArrayList<Button> gameObjectsButtons = new ArrayList<Button>();

	// Texture region for buttons
	TextureRegionDrawable trbGreenPixel;

	// Variables for dragging new object values
	public boolean objectIsDragging = false;
	public boolean newObjectIsDragging = false;
	public GameObject draggedObject = null;
	public Button draggedButton = null;

	public void SetAnyButtonTouched(boolean value) {
		System.out.println("set: " + value);
		this.anyButtonTouched = value;
	}

	public LevelBackground GetBackground() {
		return this.background;
	}

	public EditorScreen() {
		super();

		camera = new OrthographicCamera();
		camera.setToOrtho(false, Constants.WORLD_WIDTH, Constants.WORLD_HEIGHT);

		batch = new SpriteBatch();
		shapeRenderer = new ShapeRenderer();

		this.font = game.assets.getFont();
		trbGreenPixel = new TextureRegionDrawable(
				game.assets.getGraphics("greenpixel"));

		// This order has to be kept.
		createGenerateButton();
		createRestartButton();
		createSaveButton();
		createBackgroundButtons();
		createGameObjectsButtons();

		restart();
	}

	public void restart() {
		background.SetType(1);

		this.gameObjects = new ArrayList<GameObject>();

		// Pathway
		pathway = new Pathway(pathwayType, typeOfInterpolation);
		start = null;
		finish = null;
	}

	private void update(float delta) {
		// Checks if user created a new point
		if (Gdx.input.justTouched()) {
			Vector2 point = new Vector2(Gdx.input.getX(), stageHeight
					- Gdx.input.getY());
			pathway.getControlPoints().add(point);
			game.assets.playSound(Constants.SOUND_EDITOR, Constants.SOUND_GAME_OBJECT_INTERACTION_DEFAULT_VOLUME);
		}
	}

	@Override
	public void render(float delta) {
		stage.act(delta); // don't forget to advance the stage ( input + actions

		if (anyButtonTouched){
			SetAnyButtonTouched(false);
		}			
		else
			update(delta);

		renderScene();

		if (objectIsDragging) {
			if (Gdx.input.isTouched()) {
				if (this.draggedObject != null) {
					// dragging new object
					draggedObject.setPosition(new Vector2(Gdx.input.getX(),
							Constants.WORLD_HEIGHT - Gdx.input.getY()));
					batch.begin();
					draggedObject.draw(batch);
					batch.end();
				} else {
					// dragging placed object
					this.draggedButton.setPosition (
							Gdx.input.getX() - this.draggedButton.getWidth() / 2,
							Constants.WORLD_HEIGHT - Gdx.input.getY() - this.draggedButton.getHeight() / 2
						);
				}
			} else {
				float x = Gdx.input.getX();
				float y = Constants.WORLD_HEIGHT - Gdx.input.getY();

				if (this.newObjectIsDragging &&
						x > 0 && x < Constants.WORLD_WIDTH &&
						y > 0 && y < Constants.WORLD_HEIGHT) {
					
							Button button = new ImageButton (
									new TextureRegionDrawable(this.draggedObject.getTexture()),
									new TextureRegionDrawable(this.draggedObject.getTexture())
								);
							
							button.setPosition (
									this.draggedObject.getPosition().x - this.draggedObject.getWidth() / 2,
									this.draggedObject.getPosition().y - this.draggedObject.getHeight() / 2);
							
							this.gameObjects.add(this.draggedObject.copy());
							
							button.addListener(new PlacedObjectsInputListener(draggedObject, button, this));
							stage.addActor(button);
				}

				objectIsDragging = false;
				draggedObject = null;
			}

		}

	}

	private void renderScene() {
		// Clears stage - unnecessary step because everything will be rewritten
		// by the distance map
		Gdx.gl.glClearColor(0.5f, 0.5f, 0.5f, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

		batch.begin();
		this.background.draw(batch, Constants.WORLD_WIDTH,
				Constants.WORLD_HEIGHT);
		batch.end();

		if(pathway != null && pathway.getDistanceMap() != null){
			// Renders the distance map
			// See LevelPath.createBitmap()
			Gdx.gl.glEnable(GL10.GL_BLEND);
			Gdx.gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
			Gdx.gl10.glPointSize(1);
			shapeRenderer.begin(ShapeType.Point);
			for (int row = 0; row < Constants.WORLD_HEIGHT; ++row) {
				for (int column = 0; column < Constants.WORLD_WIDTH; ++column) {
					// distance map is flipped to the bitmap
					float distance = pathway.getDistanceMap().At(column, row);
	
					if (distance < Constants.MAX_SURFACE_DISTANCE_FROM_PATHWAY_DEFAULT) {
						shapeRenderer.setColor(196.f / 255, 154.f / 255,
								108.f / 255, 1);
						shapeRenderer.point(column, row, 0);
					} else if (distance < Constants.MAX_DISTANCE_FROM_PATHWAY) {
						// cause this is where the transparency begin
						distance -= Constants.MAX_SURFACE_DISTANCE_FROM_PATHWAY_DEFAULT;
						float alpha = (Constants.MAX_DISTANCE_FROM_PATHWAY - distance)
								/ Constants.MAX_DISTANCE_FROM_PATHWAY;
						shapeRenderer.setColor(196.f / 255, 154.f / 255,
								108.f / 255, alpha);
						shapeRenderer.point(column, row, 0);
					}
				}
			}
			shapeRenderer.end();
			Gdx.gl.glDisable(GL10.GL_BLEND);
		}

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
//		for (GameObject gameObject : gameObjects) {
//			gameObject.draw(batch);
//
//		}
		// car.draw(batch);
		if (start != null) {
			start.draw(batch);
		}
		if (finish != null) {
			finish.draw(batch);
		}
		batch.end();
	}

	public void generateXml(File file) {
		FileWriter fileWriter = null;
		try {
			fileWriter = new FileWriter(file);
			StringWriter stringWriter = new StringWriter();
			XmlWriter xml = new XmlWriter(stringWriter);

			xml.element("AutickaX");

			xml.element("pathway");
			xml.attribute("pathwayType", pathway.getType().toString());
			xml.attribute("typeOfInterpolation", pathway
					.getTypeOfInterpolation().toString());
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
			if (fileWriter != null) {
				try {
					fileWriter.close();
				} catch (Exception e2) {
					System.out.println("Writing to XML unsuccesfull:");
					e.printStackTrace();
				}
			}
		}
	}

	public void createGenerateButton() {
		buttonGeneratePoints = new TextButton("Generate", new TextButtonStyle(
				trbGreenPixel, trbGreenPixel, trbGreenPixel, font));
		buttonGeneratePoints.setPosition(Constants.WORLD_WIDTH, 0);
		stage.addActor(buttonGeneratePoints);

		// Creates listener
		buttonGeneratePoints.addListener(new MyInputListener(this) {
			public void touchUp(InputEvent event, float x, float y,
					int pointer, int button) {
				if (pathway.getControlPoints().size() < 4)
				{
					JOptionPane.showMessageDialog(null, "You have to set at least 4 points to generate the pathway.", "Warning: ", JOptionPane.INFORMATION_MESSAGE );
					return;
				}
					
				pathway.CreateDistances();
				start = new Start(
						pathway.GetPosition(Constants.START_POSITION_IN_CURVE).x,
						pathway.GetPosition(Constants.START_POSITION_IN_CURVE).y,
						null, START_TYPE);
				start.setTexture();
				
				finish = new Finish(
						pathway.GetPosition(Constants.FINISH_POSITION_IN_CURVE).x,
						pathway.GetPosition(Constants.FINISH_POSITION_IN_CURVE).y,
						null, FINISH_TYPE);
				finish.setTexture();
			}
		});
	}

	public void createRestartButton() {
		buttonRestart = new TextButton("Restart", new TextButtonStyle(
				trbGreenPixel, trbGreenPixel, trbGreenPixel, font));
		buttonRestart.setPosition(Constants.WORLD_WIDTH,
				buttonGeneratePoints.getY() + buttonGeneratePoints.getHeight());
		stage.addActor(buttonRestart);

		// Creates listener
		buttonRestart.addListener(new MyInputListener(this) {
			public void touchUp(InputEvent event, float x, float y,
					int pointer, int button) {
				if(JOptionPane.showConfirmDialog(null, "Are you sure that you want to reset this level?", "Question: ", JOptionPane.INFORMATION_MESSAGE ) == JOptionPane.YES_OPTION)
					restart();
			}
		});
	}

	private boolean isReadyForSaving(){
		if(pathway.getControlPoints().size() < 4)
			return false;
		if(finish == null)
			return false;
		if(start == null)
			return false;
		if(car == null)
			return false;		
		return true;
	}
	public void createSaveButton() {
		buttonSave = new TextButton("Save", new TextButtonStyle(trbGreenPixel,
				trbGreenPixel, trbGreenPixel, font));
		buttonSave.setPosition(Constants.WORLD_WIDTH, buttonRestart.getY()
				+ buttonRestart.getHeight());
		stage.addActor(buttonSave);

		// Creates listener
		buttonSave.addListener(new MyInputListener(this) {
			public void touchUp(InputEvent event, float x, float y,
					int pointer, int button) {

				if(!isReadyForSaving()){
					JOptionPane.showMessageDialog(null, "Level is not ready for saving", "Warning: ", JOptionPane.INFORMATION_MESSAGE );
					return;
				}
				
				JFileChooser chooser = new JFileChooser();
				chooser.setCurrentDirectory(new java.io.File("."));
				chooser.setDialogTitle("choosertitle");
				chooser.setDialogType(JFileChooser.SAVE_DIALOG);
				chooser.setAcceptAllFileFilterUsed(false);

				if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
					File fileToBeSaved;
					if (!chooser.getSelectedFile().getAbsolutePath()
							.endsWith(".xml"))
						fileToBeSaved = new File(chooser.getSelectedFile()
								+ ".xml");
					else
						fileToBeSaved = chooser.getSelectedFile();

					try {
						generateXml(fileToBeSaved);
					} catch (Exception e) {												
						JOptionPane.showMessageDialog(null, "Unable to save the file", "Warning: ", JOptionPane.INFORMATION_MESSAGE );
						
					}

				}
			}
		});
	}

	private void createBackgroundButtons() {
		for (int i = 1; i <= Constants.LEVEL_BACKGROUND_TEXTURE_TYPES_COUNT; i++) {
			TextureRegionDrawable trd = new TextureRegionDrawable(
					game.assets.getGraphics(LevelBackground.GetSmallTextureName(i)));
			Button button = new ImageButton(trd);
			button.setPosition(Constants.WORLD_WIDTH + 5 + (i - 1) * 30,
					Constants.WORLD_HEIGHT - 18);
			button.addListener(new MyInputListenerForBackground(i, this));
			stage.addActor(button);
		}
	}

	public enum TypeOfGameObjectButton {
		HOLE, MUD, STONE, TREE
	}

	private void createGameObjectsButtons() {
		// gameObjects.add(new Tree(300,300,null, 4));
		TextureRegionDrawable trd;
		Vector2i offsetOnScreen = new Vector2i(0, 0);
		Vector2i maxValue = new Vector2i(0, 0);

		// Holes
		for (int i = 1; i <= Constants.HOLE_TYPES_COUNT; i++) {
			trd = new TextureRegionDrawable(game.assets.getGraphics(Hole
					.GetTextureName(i)));
			createGameObjectButtons(trd, TypeOfGameObjectButton.HOLE, i,
					offsetOnScreen,
					maxValue);
		}

		// Muds
		for (int i = 1; i <= Constants.MUD_TYPES_COUNT; i++) {
			trd = new TextureRegionDrawable(game.assets.getGraphics(Mud
					.GetTextureName(i)));
			createGameObjectButtons(trd, TypeOfGameObjectButton.MUD, i,
					offsetOnScreen, maxValue);
		}

		// Stones
		for (int i = 1; i <= Constants.STONE_TYPES_COUNT; i++) {
			trd = new TextureRegionDrawable(game.assets.getGraphics(Stone
					.GetTextureName(i)));
			createGameObjectButtons(trd, TypeOfGameObjectButton.STONE, i,
					offsetOnScreen,
					maxValue);
		}

		// Trees
		for (int i = 1; i <= Constants.TREE_TYPES_COUNT; i++) {
			trd = new TextureRegionDrawable(game.assets.getGraphics(Tree
					.GetTextureName(i)));
			createGameObjectButtons(trd, TypeOfGameObjectButton.TREE, i,
					offsetOnScreen,
					maxValue);
		}
	}

	private void createGameObjectButtons(TextureRegionDrawable trd,
			TypeOfGameObjectButton typeOfClass, int type,
			Vector2i offsetOnScreen,
			Vector2i maxValue) {

		Button button = new ImageButton(trd);
		float objectWidth = button.getWidth();
		float objectHeight = button.getHeight();
		
		if (offsetOnScreen.x + objectWidth > EditorConstants.CONTROL_PANEL_WIDTH) {
			offsetOnScreen.x = 0;
			maxValue.y = 0;
		}
		if (maxValue.y < objectHeight) {
			offsetOnScreen.y += objectHeight - maxValue.y;
			maxValue.y = (int)objectHeight;
		}
		
		button.setPosition(Constants.WORLD_WIDTH + 5 + offsetOnScreen.x,
				Constants.WORLD_HEIGHT - 30 - offsetOnScreen.y);
		button.addListener(new MyInputListenerForGameObjects(typeOfClass, type,
				this));
		stage.addActor(button);

		offsetOnScreen.x += objectWidth;
	}
}
