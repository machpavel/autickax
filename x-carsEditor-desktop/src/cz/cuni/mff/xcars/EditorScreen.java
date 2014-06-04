package cz.cuni.mff.xcars;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.text.DecimalFormat;
import java.util.ArrayList;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldStyle;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.XmlWriter;

import cz.cuni.mff.xcars.Debug;
import cz.cuni.mff.xcars.Difficulty;
import cz.cuni.mff.xcars.Level;
import cz.cuni.mff.xcars.colorDrawable.ColorDrawable;
import cz.cuni.mff.xcars.constants.Constants;
import cz.cuni.mff.xcars.drawing.LevelBackground;
import cz.cuni.mff.xcars.drawing.LevelConstantBackground;
import cz.cuni.mff.xcars.entities.Booster;
import cz.cuni.mff.xcars.entities.Car;
import cz.cuni.mff.xcars.entities.Fence;
import cz.cuni.mff.xcars.entities.Finish;
import cz.cuni.mff.xcars.entities.GameObject;
import cz.cuni.mff.xcars.entities.Hill;
import cz.cuni.mff.xcars.entities.Hole;
import cz.cuni.mff.xcars.entities.House;
import cz.cuni.mff.xcars.entities.Mud;
import cz.cuni.mff.xcars.entities.ParkingCar;
import cz.cuni.mff.xcars.entities.Pneu;
import cz.cuni.mff.xcars.entities.RacingCar;
import cz.cuni.mff.xcars.entities.Start;
import cz.cuni.mff.xcars.entities.Stone;
import cz.cuni.mff.xcars.entities.Tornado;
import cz.cuni.mff.xcars.entities.Tree;
import cz.cuni.mff.xcars.entities.UniversalGameObject;
import cz.cuni.mff.xcars.entities.Wall;
import cz.cuni.mff.xcars.myInputListener.ColorBackgroundInputListener;
import cz.cuni.mff.xcars.myInputListener.DifficultyChangeListerner;
import cz.cuni.mff.xcars.myInputListener.DigitsTextFieldInputListener;
import cz.cuni.mff.xcars.myInputListener.MyInputListener;
import cz.cuni.mff.xcars.myInputListener.MyInputListenerForGameObjects;
import cz.cuni.mff.xcars.myInputListener.PlacedObjectsInputListener;
import cz.cuni.mff.xcars.myInputListener.TexturedBackgroundInputListener;
import cz.cuni.mff.xcars.pathway.Arrow;
import cz.cuni.mff.xcars.pathway.Pathway;
import cz.cuni.mff.xcars.pathway.Splines;
import cz.cuni.mff.xcars.pathway.Vector2i;

public final class EditorScreen extends BaseScreenEditor {
	// ***********************************************
	// Constants
	// ***********************************************
	private static final int CAR_TYPE = 1;
	private static final Pathway.PathwayType pathwayType = Pathway.PathwayType.OPENED;
	private static final Splines.TypeOfInterpolation typeOfInterpolation = Splines.TypeOfInterpolation.CUBIC_B_SPLINE;
	private static final int PATHWAY_TEXTURE_TYPE = 0;

	protected static final int FINISH_TYPE = 2;
	protected static final int START_TYPE = 4;
	protected static final float TIME_LIMIT_DEFAULT = 10;

	private static final float rotationSpeed = 60;
	private static final float prolongingSpeed = 40;
	// ***********************************************

	private final TextButtonStyle textButtonStyle;
	private final TextFieldStyle textFieldStyle;

	private float timeLimit = TIME_LIMIT_DEFAULT;

	// Rendering
	private OrthographicCamera camera;
	private SpriteBatch batch;
	private ShapeRenderer shapeRenderer;

	// Entities
	private ArrayList<GameObject> gameObjects;
	private ArrayList<GameObject> universalObjects;
	private Car car = new Car(0, 0, CAR_TYPE);
	private Start start;
	private Finish finish;

	// Cached font

	private BitmapFont font;
	// Pathway
	private Pathway pathway;
	private ArrayList<Arrow> arrows;

	// Background
	private LevelBackground background;

	// Pathway texture
	private TextureRegion pathwayTexture;

	// Buttons
	Button buttonGeneratePoints;
	Button buttonRestart;
	Button buttonSave;
	Button buttonLoad;
	Button buttonUnpoint;
	TextField timeTextField;
	ArrayList<Button> backgroundButtons = new ArrayList<Button>();
	private boolean anyButtonTouched = false;

	public Difficulty difficulty = Difficulty.Normal;

	// Variables for dragging new object values
	public boolean draggedNewObject = false;
	public Object draggedObject = null;
	public Arrow lastArrowMoved = null;

	public void SetAnyButtonTouched(boolean value) {
		this.anyButtonTouched = value;
	}

	public LevelBackground GetBackground() {
		return this.background;
	}

	public void SetBackground(LevelBackground value) {
		this.background = value;
	}

	public EditorScreen() {
		super();
		stage = new Stage() {
			@Override
			public boolean keyDown(int keyCode) {
				if (keyCode == Keys.F1)
					printHepl();

				if (keyCode == Keys.Q)
					changeArrowType(false);

				if (keyCode == Keys.E)
					changeArrowType(true);

				return super.keyDown(keyCode);
			}
		};
		Gdx.input.setInputProcessor(stage);

		camera = new OrthographicCamera();
		camera.setToOrtho(false, Constants.WORLD_WIDTH, Constants.WORLD_HEIGHT);

		batch = new SpriteBatch();
		shapeRenderer = new ShapeRenderer();

		this.font = game.assets.getMenuFont();

		ColorDrawable textButtonBackground = new ColorDrawable(Color.GREEN);
		this.textButtonStyle = new TextButtonStyle(textButtonBackground,
				textButtonBackground, textButtonBackground, this.font);
		this.textFieldStyle = new TextFieldStyle(this.font, new Color(1, 0, 0,
				1), new ColorDrawable(Color.GREEN), new ColorDrawable(
				Color.WHITE), new ColorDrawable(Color.BLUE));
		restart();
	}

	public void restart() {
		stage.clear();
		background = new LevelConstantBackground(169, 207, 56);

		// This order has to be kept.
		createGenerateButton();
		createDifficultyButtons();
		createTextField();

		createRestartButton();
		createSaveButton();
		createLoadButton();
		createUnpointButton();

		createBackgroundButtons();
		createGameObjectsButtons();
		createArrowController();

		this.gameObjects = new ArrayList<GameObject>();
		this.universalObjects = new ArrayList<GameObject>();
		this.arrows = new ArrayList<Arrow>();

		// Pathway
		pathway = new Pathway(pathwayType, typeOfInterpolation);
		start = null;
		finish = null;

		draggedNewObject = false;
		draggedObject = null;
		timeLimit = TIME_LIMIT_DEFAULT;
		timeTextField.setText(new DecimalFormat().format(timeLimit));
		stage.setKeyboardFocus(timeTextField);
	}

	public void loadLevel(FileHandle file) throws Exception {
		restart();
		Level level = new Level();
		level.parseLevel(file);
		level.getPathway().CreateDistances();

		this.car = level.getCar();
		this.finish = level.getFinish();
		this.gameObjects = level.getGameObjects();
		this.universalObjects = level.getUniversalObjects();
		this.arrows = level.getArrows();
		this.pathway = level.getPathway();
		this.pathwayTexture = this.pathway.getDistanceMap().generateTexture(
				this.difficulty);
		this.start = level.getStart();
		timeLimit = level.getTimeLimit();
		timeTextField.setText(new DecimalFormat().format(timeLimit));

		this.background = level.getLevelBackground();

		for (GameObject universalObject : this.universalObjects) {
			universalObject.setTexture();
			universalObject.setPosition(universalObject.getX(),
					universalObject.getY());
			universalObject.addListener(new PlacedObjectsInputListener(
					universalObject, this));
			stage.addActor(universalObject);
		}

		for (GameObject gameObject : this.gameObjects) {
			gameObject.setTexture();
			gameObject.setPosition(gameObject.getX(), gameObject.getY());
			gameObject.addListener(new PlacedObjectsInputListener(gameObject,
					this));
			stage.addActor(gameObject);
		}

		for (Arrow arrow : this.arrows) {
			arrow.setTexture();
			arrow.setPosition(arrow.getX(), arrow.getY());
			arrow.addListener(new PlacedObjectsInputListener(arrow, this));
			stage.addActor(arrow);
		}

		this.start.setTexture();
		this.finish.setTexture();
		generateStartAndFinish();
	}

	private void update(float delta) {
		// Checks if user created a new point
		if (Gdx.input.justTouched()) {
			int x = Gdx.input.getX();
			int y = Gdx.input.getY();
			if (x > 0 && x < Constants.WORLD_WIDTH && y > 0
					&& y < Constants.WORLD_HEIGHT) {
				Vector2 point = new Vector2(Gdx.input.getX(), stageHeight
						- Gdx.input.getY());
				pathway.getControlPoints().add(point);
			}
		}
	}

	private void doArrowKayboardModification(float delta) {
		if (this.lastArrowMoved != null) {
			if (Gdx.input.isKeyPressed(Keys.A)) {
				this.lastArrowMoved.setRotation(this.lastArrowMoved
						.getRotation() + rotationSpeed * delta);
				// Debug.SetValue(this.lastArrowMoved.getRotation());
			}
			if (Gdx.input.isKeyPressed(Keys.D)) {
				this.lastArrowMoved.setRotation(this.lastArrowMoved
						.getRotation() - rotationSpeed * delta);
				// Debug.SetValue(this.lastArrowMoved.getRotation());
			}
			if (Gdx.input.isKeyPressed(Keys.W)) {
				this.lastArrowMoved.setLength(this.lastArrowMoved.getLength()
						+ prolongingSpeed * delta);
				// Debug.SetValue(this.lastArrowMoved.getLength());
			}
			if (Gdx.input.isKeyPressed(Keys.S)) {
				this.lastArrowMoved.setLength(this.lastArrowMoved.getLength()
						- prolongingSpeed * delta);
				// Debug.SetValue(this.lastArrowMoved.getLength());
			}
		}
	}

	public void printHepl() {
		Debug.clear();
		Debug.Log("Keys:");
		Debug.Log("A, D: Rotate an arrow image");
		Debug.Log("W, S: Resize an arrow image");
		Debug.Log("Q, E: Change an arrow type");
		Debug.Log("Numbers: Modify time limit");
		Debug.Log("Arrows, +, -: Modify time limit");
		Debug.Log("");
		Debug.Log("How to draw a pathway:");
		Debug.Log("By clicking into scene add points.");
		Debug.Log("Then click on generate button.");
	}

	@Override
	public void render(float delta) {

		stage.act(delta);
		if (anyButtonTouched) {
			anyButtonTouched = false;
		} else {
			update(delta);
		}

		renderScene();
		dragObject();

		doArrowKayboardModification(delta);

		batch.begin();
		Debug.draw(batch);
		batch.end();
	}

	private void dragObject() {
		boolean objectIsDragging = this.draggedObject != null;
		if (objectIsDragging) {
			if (Gdx.input.isTouched()) {
				if (this.draggedNewObject) {
					// dragging new object
					if (draggedObject instanceof GameObject) {
						GameObject draggedGameObject = (GameObject) draggedObject;
						draggedGameObject.setPosition(Gdx.input.getX(),
								Constants.WORLD_HEIGHT - Gdx.input.getY());
						batch.begin();
						draggedGameObject.draw(batch, 1);
						batch.end();
					} else if (draggedObject instanceof Arrow) {
						Arrow draggedGameObject = (Arrow) draggedObject;
						draggedGameObject.setCenterPosition(Gdx.input.getX(),
								Constants.WORLD_HEIGHT - Gdx.input.getY());
						batch.begin();
						draggedGameObject.draw(batch, 1);
						batch.end();
					}

				} else {
					// dragging placed object
					if (this.draggedObject instanceof GameObject) {
						GameObject draggedGameObject = (GameObject) this.draggedObject;
						draggedGameObject.setPosition(Gdx.input.getX(),
								Constants.WORLD_HEIGHT - Gdx.input.getY());
					} else if (this.draggedObject instanceof Arrow) {
						Arrow draggedArrow = (Arrow) this.draggedObject;
						draggedArrow.setCenterPosition(Gdx.input.getX(),
								Constants.WORLD_HEIGHT - Gdx.input.getY());
					}
				}
			} else {
				// Just released
				if (this.draggedNewObject) {
					if (this.draggedObject instanceof GameObject) {
						GameObject draggedGameObject = (GameObject) this.draggedObject;
						if (isInWorld(draggedGameObject)) {
							if (draggedGameObject instanceof UniversalGameObject) {
								draggedGameObject
										.addListener(new PlacedObjectsInputListener(
												draggedGameObject, this));
								this.universalObjects.add(draggedGameObject);
								this.stage.addActor(draggedGameObject);
							} else {
								draggedGameObject
										.addListener(new PlacedObjectsInputListener(
												draggedGameObject, this));
								this.gameObjects.add(draggedGameObject);
								this.stage.addActor(draggedGameObject);
							}
						}
					} else if (this.draggedObject instanceof Arrow) {
						Arrow arrow = (Arrow) this.draggedObject;
						if (isInWorld(arrow)) {
							arrow.addListener(new PlacedObjectsInputListener(
									arrow, this));
							this.arrows.add(arrow);
							this.stage.addActor(arrow);
						}
					}
				}
				this.draggedObject = null;
				this.draggedNewObject = false;
			}

		}
	}

	public boolean isInWorld(GameObject gameObject) {
		float x = gameObject.getX();
		float y = gameObject.getY();
		float bounding = gameObject.getBoundingRadius();
		return x > -bounding && x < Constants.WORLD_WIDTH + bounding
				&& y > -bounding && y < Constants.WORLD_HEIGHT + bounding;
	}

	public boolean isInWorld(Arrow arrow) {
		float x = arrow.getX();
		float y = arrow.getY();
		float widthBounding = arrow.getWidth() / 2;
		float heightBounding = arrow.getHeight() / 2;
		return x > -widthBounding && x < Constants.WORLD_WIDTH + widthBounding
				&& y > -heightBounding
				&& y < Constants.WORLD_HEIGHT + heightBounding;
	}

	private void renderScene() {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		// Background and pathway
		batch.begin();
		batch.disableBlending();
		this.background.draw(batch, Constants.WORLD_WIDTH,
				Constants.WORLD_HEIGHT);
		batch.enableBlending();
		if (pathway != null && pathway.getDistanceMap() != null)
			batch.draw(this.pathwayTexture, 0, 0, Constants.WORLD_WIDTH,
					Constants.WORLD_HEIGHT);
		batch.end();

		// Control panel
		shapeRenderer.begin(ShapeType.Filled);
		shapeRenderer.setColor(0.5f, 0.5f, 0.5f, 1);
		shapeRenderer.rect(Constants.WORLD_WIDTH, 0,
				EditorConstants.CONTROL_PANEL_WIDTH, Constants.WORLD_HEIGHT);
		shapeRenderer.end();

		// Renders control points
		shapeRenderer.begin(ShapeType.Filled);
		for (Vector2 point : pathway.getControlPoints()) {
			shapeRenderer.setColor(1f, 0f, 0f, 1);
			shapeRenderer.circle(point.x, point.y, 4);
		}
		shapeRenderer.end();

		batch.begin();
		if (start != null)
			start.draw(batch);

		if (finish != null)
			finish.draw(batch);
		batch.end();
		stage.draw();
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
			xml.element("distanceMap");
			xml.attribute("nodesCount", pathway.getDistanceMap()
					.getNodesCount());
			xml.pop();
			xml.pop();

			xml.element("arrows");
			for (Arrow arrow : arrows) {
				arrow.toXml(xml);
			}
			xml.pop();

			xml.element("entities");
			for (GameObject gameObject : gameObjects) {
				gameObject.toXml(xml);
			}
			xml.pop();

			xml.element("drawings");
			for (GameObject universalObject : universalObjects) {
				universalObject.toXml(xml);
			}
			xml.pop();

			car.toXml(xml);
			start.toXml(xml);
			finish.toXml(xml);

			xml.element("background");
			background.toXml(xml);
			xml.pop();

			xml.element("timeLimit", getTimeLimit());

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

	public void generateStartAndFinish() {
		float delta = 0.01f;
		start = new Start(
				pathway.GetPosition(Constants.misc.START_POSITION_IN_CURVE).x,
				pathway.GetPosition(Constants.misc.START_POSITION_IN_CURVE).y,
				START_TYPE);
		start.setTexture();
		Vector2 startTo = pathway.GetPosition(delta);
		Vector2 startFrom = pathway.GetPosition(0);
		float startAngle = startTo.sub(startFrom).angle();
		start.setRotation((startAngle + 270) % 360);

		finish = new Finish(
				pathway.GetPosition(Constants.misc.FINISH_POSITION_IN_CURVE).x,
				pathway.GetPosition(Constants.misc.FINISH_POSITION_IN_CURVE).y,
				FINISH_TYPE);
		finish.setTexture();
		Vector2 finishTo = pathway.GetPosition(1 - delta);
		Vector2 finishFrom = pathway.GetPosition(1);
		float finishAngle = finishTo.sub(finishFrom).angle();
		finish.setRotation((finishAngle + 90) % 360);
	}

	private void createGenerateButton() {
		buttonGeneratePoints = new TextButton("Generate", this.textButtonStyle);
		buttonGeneratePoints.setPosition(Constants.WORLD_WIDTH, 0);
		stage.addActor(buttonGeneratePoints);

		// Creates listener
		buttonGeneratePoints.addListener(new MyInputListener(this) {
			public void touchUp(InputEvent event, float x, float y,
					int pointer, int button) {
				if (pathway.getControlPoints().size() < 4) {
					JOptionPane
							.showMessageDialog(
									null,
									"You have to set at least 4 points to generate the pathway.",
									"Warning: ",
									JOptionPane.INFORMATION_MESSAGE);
					return;
				}

				pathway.CreateDistances();
				pathwayTexture = pathway.getDistanceMap().generateTexture(
						difficulty);
				generateStartAndFinish();
			}
		});
	}

	private void createRestartButton() {
		buttonRestart = new TextButton("Restart", this.textButtonStyle);
		buttonRestart.setPosition(Constants.WORLD_WIDTH,
				buttonGeneratePoints.getY() + buttonGeneratePoints.getHeight());
		stage.addActor(buttonRestart);

		// Creates listener
		buttonRestart.addListener(new MyInputListener(this) {
			public void touchUp(InputEvent event, float x, float y,
					int pointer, int button) {
				if (JOptionPane.showConfirmDialog(null,
						"Are you sure that you want to reset this level?",
						"Question: ", JOptionPane.INFORMATION_MESSAGE) == JOptionPane.YES_OPTION)
					restart();
			}
		});
	}

	private boolean isReadyForSaving() {
		if (pathway.getControlPoints().size() < 4)
			return false;
		if (finish == null)
			return false;
		if (start == null)
			return false;
		if (car == null)
			return false;
		return true;
	}

	private void createSaveButton() {
		buttonSave = new TextButton("Save", this.textButtonStyle);
		buttonSave.setPosition(
				20 + buttonRestart.getX() + buttonRestart.getWidth(),
				buttonRestart.getY());
		stage.addActor(buttonSave);

		// Creates listener
		buttonSave.addListener(new MyInputListener(this) {
			public void touchUp(InputEvent event, float x, float y,
					int pointer, int button) {

				if (!isReadyForSaving()) {
					JOptionPane.showMessageDialog(null,
							"Level is not ready for saving", "Warning: ",
							JOptionPane.INFORMATION_MESSAGE);
					return;
				}

				JFileChooser chooser = new JFileChooser();
				chooser.setCurrentDirectory(new java.io.File("."));
				chooser.setDialogTitle("Save file");
				chooser.setDialogType(JFileChooser.SAVE_DIALOG);
				chooser.setAcceptAllFileFilterUsed(false);

				if (chooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
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
						JOptionPane.showMessageDialog(null,
								"Unable to save the file", "Warning: ",
								JOptionPane.INFORMATION_MESSAGE);
						e.printStackTrace();
					}

				}
			}
		});
	}

	private void createLoadButton() {
		buttonLoad = new TextButton("Load", this.textButtonStyle);
		buttonLoad.setPosition(buttonSave.getX() + 20 + buttonSave.getWidth(),
				buttonSave.getY());
		stage.addActor(buttonLoad);

		// Creates listener
		buttonLoad.addListener(new MyInputListener(this) {
			public void touchUp(InputEvent event, float x, float y,
					int pointer, int button) {

				JFileChooser chooser = new JFileChooser();
				chooser.setCurrentDirectory(new java.io.File("."));
				chooser.setDialogTitle("Load file");
				chooser.setDialogType(JFileChooser.OPEN_DIALOG);
				chooser.setAcceptAllFileFilterUsed(false);

				if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
					File fileToBeLoad;
					if (!chooser.getSelectedFile().getAbsolutePath()
							.endsWith(".xml"))
						fileToBeLoad = new File(chooser.getSelectedFile()
								+ ".xml");
					else
						fileToBeLoad = chooser.getSelectedFile();

					try {
						loadLevel(new FileHandle(fileToBeLoad));
					} catch (Exception e) {
						JOptionPane.showMessageDialog(null,
								"Unable to load the file", "Warning: ",
								JOptionPane.INFORMATION_MESSAGE);
						System.out.println(e.getMessage());
						restart();
					}

				}
			}
		});
	}

	private void createUnpointButton() {
		buttonUnpoint = new TextButton("Unpoint", this.textButtonStyle);
		buttonUnpoint.setPosition(
				buttonLoad.getX() + 20 + buttonLoad.getWidth(),
				buttonLoad.getY());
		stage.addActor(buttonUnpoint);

		// Creates listener
		buttonUnpoint.addListener(new MyInputListener(this) {
			public void touchUp(InputEvent event, float x, float y,
					int pointer, int button) {
				if (pathway.getControlPoints().size() > 0)
					pathway.getControlPoints().remove(
							pathway.getControlPoints().size() - 1);
			}
		});
	}

	private void createBackgroundButtons() {
		int widthOffset = 0;
		int heightOffset = 0;
		int width = 30;
		int height = 18;

		Button colorButton = new Button(new ColorDrawable(new Color(1, 0, 0,
				0.5f)));
		colorButton.setPosition(Constants.WORLD_WIDTH + 5 + widthOffset,
				Constants.WORLD_HEIGHT - heightOffset - height);
		colorButton.setWidth(2 * width);
		;
		colorButton.setHeight(height);
		colorButton.addListener(new ColorBackgroundInputListener(this));
		stage.addActor(colorButton);
		widthOffset += 2 * width;

		for (int i = 1; i <= EditorConstants.LEVEL_BACKGROUND_TEXTURE_TYPES_COUNT; i++) {
			String name = EditorConstants.LEVEL_BACKGROUND_TEXTURE_PREFIX
					+ Integer.toString(i);
			Button button = new Button(new TextureRegionDrawable(
					XcarsEditor.getInstance().assets.getGraphics(name)));
			button.setPosition(Constants.WORLD_WIDTH + 5 + widthOffset,
					Constants.WORLD_HEIGHT - heightOffset - height);
			button.setWidth(width);
			button.setHeight(height);
			button.addListener(new TexturedBackgroundInputListener(name, this));
			stage.addActor(button);

			widthOffset += width;
			if (widthOffset > EditorConstants.CONTROL_PANEL_WIDTH - width) {
				widthOffset = 0;
				heightOffset += height;
			}
		}
	}

	public enum TypeOfObjectToDrag {
		HOLE, MUD, STONE, TREE, BOOSTER, FENCE, PARKING_CAR, HOUSE, WALL, HILL, TORNADO, RACING_CAR, PNEU, ARROW, UNIVERSAL
	}

	private void createArrowController() {
		Arrow arrow = new Arrow();
		arrow.setTexture();
		Button button = new ImageButton(arrow.getDrawable());

		button.setPosition(Constants.WORLD_WIDTH + 300, 80);
		button.addListener(new MyInputListenerForGameObjects(
				TypeOfObjectToDrag.ARROW, 1, this));
		stage.addActor(button);

	}

	private void createGameObjectsButtons() {
		// gameObjects.add(new Tree(300,300,null, 4));
		TextureRegionDrawable trd;
		Vector2i offsetOnScreen = new Vector2i(0, 0);
		Vector2i maxValue = new Vector2i(0, 0);

		// Holes
		for (int i = 1; i <= Constants.gameObjects.HOLE_TYPES_COUNT; i++) {
			trd = new TextureRegionDrawable(game.assets.getGraphics(Hole
					.GetTextureName(i)));
			createGameObjectButtons(trd, TypeOfObjectToDrag.HOLE, i,
					offsetOnScreen, maxValue);
		}

		// Muds
		for (int i = 1; i <= Constants.gameObjects.MUD_TYPES_COUNT; i++) {
			trd = new TextureRegionDrawable(game.assets.getGraphics(Mud
					.GetTextureName(i)));
			createGameObjectButtons(trd, TypeOfObjectToDrag.MUD, i,
					offsetOnScreen, maxValue);
		}

		// Stones
		for (int i = 1; i <= Constants.gameObjects.STONE_TYPES_COUNT; i++) {
			trd = new TextureRegionDrawable(game.assets.getGraphics(Stone
					.GetTextureName(i)));
			createGameObjectButtons(trd, TypeOfObjectToDrag.STONE, i,
					offsetOnScreen, maxValue);
		}

		// Trees
		for (int i = 1; i <= Constants.gameObjects.TREE_TYPES_COUNT; i++) {
			trd = new TextureRegionDrawable(game.assets.getGraphics(Tree
					.GetStaticTextureName(i)));
			createGameObjectButtons(trd, TypeOfObjectToDrag.TREE, i,
					offsetOnScreen, maxValue);
		}

		// Boosts
		for (int i = 1; i <= Constants.gameObjects.BOOSTER_TYPES_COUNT; i++) {
			trd = new TextureRegionDrawable(game.assets.getGraphics(Booster
					.GetTextureName(i)));
			createGameObjectButtons(trd, TypeOfObjectToDrag.BOOSTER, i,
					offsetOnScreen, maxValue);
		}

		// Fences
		for (int i = 1; i <= Constants.gameObjects.FENCE_TYPES_COUNT; i++) {
			trd = new TextureRegionDrawable(game.assets.getGraphics(Fence
					.GetStaticTextureName(i)));
			createGameObjectButtons(trd, TypeOfObjectToDrag.FENCE, i,
					offsetOnScreen, maxValue);
		}

		// Houses
		for (int i = 1; i <= Constants.gameObjects.HOUSE_TYPES_COUNT; i++) {
			trd = new TextureRegionDrawable(game.assets.getGraphics(House
					.GetStaticTextureName(i)));
			createGameObjectButtons(trd, TypeOfObjectToDrag.HOUSE, i,
					offsetOnScreen, maxValue);
		}
		// Parking cars
		for (int i = 1; i <= Constants.gameObjects.PARKING_CAR_TYPES_COUNT; i++) {
			trd = new TextureRegionDrawable(game.assets.getGraphics(ParkingCar
					.GetStaticTextureName(i)));
			createGameObjectButtons(trd, TypeOfObjectToDrag.PARKING_CAR, i,
					offsetOnScreen, maxValue);
		}
		// Walls
		for (int i = 1; i <= Constants.gameObjects.WALL_TYPES_COUNT; i++) {
			trd = new TextureRegionDrawable(game.assets.getGraphics(Wall
					.GetStaticTextureName(i)));
			createGameObjectButtons(trd, TypeOfObjectToDrag.WALL, i,
					offsetOnScreen, maxValue);
		}
		// Hills
		for (int i = 1; i <= Constants.gameObjects.HILL_TYPES_COUNT; i++) {
			trd = new TextureRegionDrawable(game.assets.getGraphics(Hill
					.GetTextureName(i)));
			createGameObjectButtons(trd, TypeOfObjectToDrag.HILL, i,
					offsetOnScreen, maxValue);
		}

		// Tornados
		for (int i = 1; i <= Constants.gameObjects.TORNADO_TYPES_COUNT; i++) {
			trd = new TextureRegionDrawable(game.assets.getGraphics(Tornado
					.GetTextureName(i)));
			createGameObjectButtons(trd, TypeOfObjectToDrag.TORNADO, i,
					offsetOnScreen, maxValue);
		}

		// Racing cars
		for (int i = 1; i <= Constants.gameObjects.RACING_CAR_TYPES_COUNT; i++) {
			trd = new TextureRegionDrawable(game.assets.getGraphics(RacingCar
					.GetStaticTextureName(i)));
			createGameObjectButtons(trd, TypeOfObjectToDrag.RACING_CAR, i,
					offsetOnScreen, maxValue);
		}

		// Pneu
		for (int i = 1; i <= Constants.gameObjects.PNEU_TYPES_COUNT; i++) {
			trd = new TextureRegionDrawable(game.assets.getGraphics(Pneu
					.GetTextureName(i)));
			createGameObjectButtons(trd, TypeOfObjectToDrag.PNEU, i,
					offsetOnScreen, maxValue);
		}

		// Universal
		for (int i = 1; i <= Constants.gameObjects.UNIVERSAL_TYPES_COUNT; i++) {
			trd = new TextureRegionDrawable(
					game.assets.getGraphics(UniversalGameObject
							.GetTextureName(i)));
			createGameObjectButtons(trd, TypeOfObjectToDrag.UNIVERSAL, i,
					offsetOnScreen, maxValue);
		}
	}

	private void createGameObjectButtons(TextureRegionDrawable trd,
			TypeOfObjectToDrag typeOfClass, int type, Vector2i offsetOnScreen,
			Vector2i maxValue) {

		int HEIGHT_OFFSET = 50;
		Button button = new ImageButton(trd);
		button.setWidth(30);
		button.setHeight(30);
		float objectWidth = button.getWidth();
		float objectHeight = button.getHeight();

		if (offsetOnScreen.x + objectWidth > EditorConstants.CONTROL_PANEL_WIDTH) {
			offsetOnScreen.x = 0;
			maxValue.y = 0;
		}
		if (maxValue.y < objectHeight) {
			offsetOnScreen.y += objectHeight - maxValue.y;
			maxValue.y = (int) objectHeight;
		}

		button.setPosition(Constants.WORLD_WIDTH + 5 + offsetOnScreen.x,
				Constants.WORLD_HEIGHT - HEIGHT_OFFSET - offsetOnScreen.y);
		button.addListener(new MyInputListenerForGameObjects(typeOfClass, type,
				this));
		stage.addActor(button);

		offsetOnScreen.x += objectWidth;
	}

	private void createDifficultyButtons() {
		int BUTTONS_OFFSET = 25;

		for (int i = 0; i < Difficulty.values().length; i++) {
			Button button = new TextButton(Integer.toString(i + 1),
					this.textButtonStyle);
			button.setPosition(Constants.WORLD_WIDTH + i * BUTTONS_OFFSET
					+ buttonGeneratePoints.getWidth() + BUTTONS_OFFSET,
					buttonGeneratePoints.getY());
			stage.addActor(button);

			// Creates listener
			button.addListener(new DifficultyChangeListerner(this, i));
		}
	}

	private void createTextField() {
		timeTextField = new TextField(new DecimalFormat().format(timeLimit),
				this.textFieldStyle);
		timeTextField.setPosition(buttonGeneratePoints.getX()
				+ buttonGeneratePoints.getWidth() + 170,
				buttonGeneratePoints.getY());
		timeTextField.setTextFieldListener(new DigitsTextFieldInputListener(
				this));
		timeTextField.addListener(new InputListener() {
			@Override
			public boolean keyTyped(InputEvent event, char character) {
				if (event.getCharacter() == '+' || event.getKeyCode() == 19
						|| event.getKeyCode() == 22) {
					timeLimit++;
					if (timeLimit > 99)
						timeLimit = 99;
					timeTextField.setText(new DecimalFormat().format(timeLimit));
				} else if (event.getCharacter() == '-'
						|| event.getKeyCode() == 20 || event.getKeyCode() == 21) {
					timeLimit--;
					if (timeLimit < 0)
						timeLimit = 0;
					timeTextField.setText(new DecimalFormat().format(timeLimit));
				}
				timeTextField.setCursorPosition(timeTextField.getText()
						.length());
				return true;
			}
		});
		stage.addActor(timeTextField);
	}

	public float getTimeLimit() {
		return timeLimit;
	}

	public void setTimeLimit(float timeLimit) {
		this.timeLimit = timeLimit;
	}

	public Pathway getPathway() {
		return this.pathway;
	}

	public void setPathwayTexture(TextureRegion texture) {
		this.pathwayTexture = texture;
	}

	protected void changeArrowType(boolean toRight) {
		if (this.lastArrowMoved != null) {
			int type = this.lastArrowMoved.getType();
			type = toRight ? type + 1 : type - 1;
			if (type > Constants.misc.ARROW_TYPE_COUNT)
				type = 1;
			else if (type < 1)
				type = Constants.misc.ARROW_TYPE_COUNT;
			this.lastArrowMoved.setType(type);
		}
	}
}
