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
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.Tree.Node;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.XmlWriter;
import com.badlogic.gdx.utils.viewport.ScalingViewport;

import cz.cuni.mff.xcars.colorDrawable.ColorDrawable;
import cz.cuni.mff.xcars.constants.Constants;
import cz.cuni.mff.xcars.debug.Debug;
import cz.cuni.mff.xcars.drawing.LevelBackground;
import cz.cuni.mff.xcars.drawing.LevelConstantBackground;
import cz.cuni.mff.xcars.entities.Arrow;
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
import cz.cuni.mff.xcars.myInputListener.DigitsTextFieldInputListener;
import cz.cuni.mff.xcars.myInputListener.MyInputListener;
import cz.cuni.mff.xcars.myInputListener.MyInputListenerForGameObjects;
import cz.cuni.mff.xcars.myInputListener.PlacedObjectsInputListener;
import cz.cuni.mff.xcars.myInputListener.TexturedBackgroundInputListener;
import cz.cuni.mff.xcars.pathway.Pathway;
import cz.cuni.mff.xcars.pathway.Splines;

public final class EditorScreen extends BaseScreenEditor {
	// ***********************************************
	// Constants
	// ***********************************************
	private static final int CAR_TYPE = 1;
	private static final Pathway.PathwayType pathwayType = Pathway.PathwayType.OPENED;
	private static final Splines.TypeOfInterpolation typeOfInterpolation = Splines.TypeOfInterpolation.CUBIC_B_SPLINE;
	private static final int PATHWAY_TEXTURE_TYPE = 0;

	private static final int FINISH_TYPE = 2;
	private static final int START_TYPE = 4;
	private static final float TIME_LIMIT_DEFAULT = 10;

	private static final float rotationSpeed = 60;
	private static final float prolongingSpeed = 30;
	// ***********************************************

	// Rendering
	private OrthographicCamera camera;
	private SpriteBatch batch;
	private ShapeRenderer shapeRenderer;
	private Skin skin;

	// Entities
	private ArrayList<GameObject> gameObjects;
	private ArrayList<GameObject> universalObjects;
	private Car car = new Car(0, 0, CAR_TYPE);
	private Start start;
	private Finish finish;

	// Pathway
	private Pathway pathway;
	private TextureRegion pathwayTexture;

	// Background
	private LevelBackground background;

	// Time limit
	private float timeLimit;
	private TextField timeTextField;

	// Difficulty
	public Difficulty difficulty = Difficulty.Normal;

	// Variables for dragging new object values
	public boolean draggingNewObject = false;
	public GameObject draggedObject = null;
	public GameObject lastObjectMoved = null;

	// Disables creating new pathway point when UI is pressed
	private boolean anyButtonTouched = false;

	public EditorScreen() {
		super();
		Debug.DRAW_FPS = false;

		this.batch = new SpriteBatch();
		this.shapeRenderer = new ShapeRenderer();
		this.skin = new Skin(Gdx.files.internal("UISkin/uiskin.json"));

		// Camera
		this.camera = new OrthographicCamera();
		this.camera.setToOrtho(false, this.stageWidth, this.stageHeight);

		// Stage
		this.stage = new Stage(new ScalingViewport(Scaling.stretch, this.stageWidth, this.stageHeight, this.camera),
				batch) {
			@Override
			public boolean keyDown(int keyCode) {
				if (keyCode == Keys.F1)
					printHepl();
				return super.keyDown(keyCode);
			}
		};
		Gdx.input.setInputProcessor(stage);
		if (Debug.DEBUG) {
			if (Debug.debugEditor) {
				this.stage.setDebugAll(true);
			}
		}
		restart();
	}

	public void restart() {
		this.stage.clear();
		this.background = new LevelConstantBackground(169, 207, 56);
		this.gameObjects = new ArrayList<GameObject>();
		this.universalObjects = new ArrayList<GameObject>();
		this.pathway = new Pathway(pathwayType, typeOfInterpolation);
		this.start = null;
		this.finish = null;
		this.draggingNewObject = false;
		this.draggedObject = null;
		this.timeLimit = TIME_LIMIT_DEFAULT;

		// Must be called last
		createUI();
	}

	public void createUI() {
		Table table = new Table(skin);

		table.setFillParent(true);
		stage.addActor(table);

		table.add(new Container<Label>(new Label("1", skin)).right().bottom()).width(Constants.WORLD_WIDTH)
				.height(Constants.WORLD_HEIGHT);

		Table controlPanel = new Table();
		table.add(controlPanel).expand().fill();

		// Background buttons
		controlPanel.add(createBackgroundButtons()).left();
		controlPanel.row();

		// Game objects tree
		controlPanel.add(createGameObjectsButtons()).expand().fill();
		controlPanel.row();

		// Additional controllers
		controlPanel.add(createAdditionalControllers()).left();
		controlPanel.row();

		// Controlling buttons
		HorizontalGroup buttons = new HorizontalGroup();
		controlPanel.add(buttons).left();
		buttons.addActor(createGenerateButton());
		buttons.addActor(createRestartButton());
		buttons.addActor(createSaveButton());
		buttons.addActor(createLoadButton());
		buttons.addActor(createUnpointButton());
		controlPanel.row();
	}

	@Override
	public void render(float delta) {
		// Updating
		stage.act(delta);
		if (anyButtonTouched) {
			anyButtonTouched = false;
		} else {
			update(delta);
		}
		doArrowKayboardModification(delta);
		dragObject();

		// Rendering
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		renderScene();
		batch.begin();
		Debug.render(batch, delta);
		batch.end();
	}

	private void update(float delta) {
		// Checks if user created a new point
		if (Gdx.input.justTouched()) {
			int x = Gdx.input.getX();
			int y = Gdx.input.getY();
			if (x > 0 && x < Constants.WORLD_WIDTH && y > 0 && y < Constants.WORLD_HEIGHT) {
				Vector2 point = new Vector2(Gdx.input.getX(), stageHeight - Gdx.input.getY());
				pathway.getControlPoints().add(point);
			}
		}
	}

	private void doArrowKayboardModification(float delta) {
		if (this.lastObjectMoved != null) {
			if (this.lastObjectMoved instanceof UniversalGameObject) {
				UniversalGameObject scalableObject = (UniversalGameObject) this.lastObjectMoved;

				if (Gdx.input.isKeyPressed(Keys.W)) {
					scalableObject.setScaleX(scalableObject.getScaleX() + prolongingSpeed * delta);
				}
				if (Gdx.input.isKeyPressed(Keys.S)) {
					scalableObject.setScaleX(scalableObject.getScaleX() - prolongingSpeed * delta);
				}
			}

			if (Gdx.input.isKeyPressed(Keys.A)) {
				this.lastObjectMoved.setRotation(this.lastObjectMoved.getRotation() + rotationSpeed * delta);
			}
			if (Gdx.input.isKeyPressed(Keys.D)) {
				this.lastObjectMoved.setRotation(this.lastObjectMoved.getRotation() - rotationSpeed * delta);
			}
		}
	}

	public void printHepl() {
		Debug.clear();
		Debug.Log("Keys:");
		Debug.Log("A, D: Rotate an object");
		Debug.Log("W, S: Resize an scalable object");
		Debug.Log("Numbers: Modify time limit");
		Debug.Log("Arrows, +, -: Modify time limit");
		Debug.Log("");
		Debug.Log("How to draw a pathway:");
		Debug.Log("By clicking into scene add points.");
		Debug.Log("Then click on generate button.");
	}

	private void dragObject() {
		boolean objectIsDragging = this.draggedObject != null;
		if (objectIsDragging) {
			if (Gdx.input.isTouched()) {
				if (this.draggingNewObject) {
					// dragging new object
					GameObject draggedGameObject = this.draggedObject;
					draggedGameObject.setPosition(Gdx.input.getX(), Constants.WORLD_HEIGHT - Gdx.input.getY());
				} else {
					// dragging placed object
					GameObject draggedGameObject = this.draggedObject;
					draggedGameObject.setPosition(Gdx.input.getX(), Constants.WORLD_HEIGHT - Gdx.input.getY());
				}
			} else {
				// Just released
				if (this.draggingNewObject) {
					GameObject draggedGameObject = this.draggedObject;
					if (isInWorld(draggedGameObject)) {
						if (draggedGameObject instanceof UniversalGameObject) {
							draggedGameObject.addListener(new PlacedObjectsInputListener(draggedGameObject, this));
							this.universalObjects.add(draggedGameObject);
						} else {
							draggedGameObject.addListener(new PlacedObjectsInputListener(draggedGameObject, this));
							this.gameObjects.add(draggedGameObject);
						}
						this.stage.addActor(draggedGameObject);

					}
				}
				this.draggedObject = null;
				this.draggingNewObject = false;
			}
		}
	}

	public boolean isInWorld(Object object) {
		if (object instanceof GameObject) {
			GameObject gameObject = (GameObject) object;
			float x = gameObject.getX();
			float y = gameObject.getY();
			float bounding = gameObject.getBoundingRadius();
			return x > -bounding && x < Constants.WORLD_WIDTH + bounding && y > -bounding
					&& y < Constants.WORLD_HEIGHT + bounding;
		} else
			return false;

	}

	private void renderScene() {
		// Background and pathway
		batch.begin();
		batch.disableBlending();
		this.background.draw(batch, Constants.WORLD_WIDTH, Constants.WORLD_HEIGHT);
		batch.enableBlending();
		if (pathway != null && pathway.getDistanceMap() != null)
			batch.draw(this.pathwayTexture, 0, 0, Constants.WORLD_WIDTH, Constants.WORLD_HEIGHT);
		batch.end();

		// Control panel
		shapeRenderer.begin(ShapeType.Filled);
		shapeRenderer.setColor(0.5f, 0.5f, 0.5f, 1);
		shapeRenderer.rect(Constants.WORLD_WIDTH, 0, EditorConstants.CONTROL_PANEL_WIDTH, Constants.WORLD_HEIGHT);
		shapeRenderer.end();

		// Renders control points
		shapeRenderer.begin(ShapeType.Filled);
		for (Vector2 point : pathway.getControlPoints()) {
			shapeRenderer.setColor(1f, 0f, 0f, 1);
			shapeRenderer.circle(point.x, point.y, 4);
		}
		shapeRenderer.end();

		stage.draw();

		batch.begin();
		if (start != null)
			start.draw(batch);

		if (finish != null)
			finish.draw(batch);

		if (this.draggingNewObject) {
			this.draggedObject.setPosition(Gdx.input.getX(), Constants.WORLD_HEIGHT - Gdx.input.getY());
			this.draggedObject.draw(batch, 1);
		}
		batch.end();
	}

	public void loadLevel(FileHandle file) throws Exception {
		restart();
		Level level = new Level();
		level.parseLevel(file);
		level.getPathway().CreateDistances();

		this.gameObjects = level.getGameObjects();
		this.universalObjects = level.getUniversalObjects();
		this.pathway = level.getPathway();
		this.pathwayTexture = this.pathway.getDistanceMap().generateTexture(this.difficulty);
		this.car = level.getCar();
		this.start = level.getStart();
		this.start.setTexture();
		this.finish = level.getFinish();
		this.finish.setTexture();
		this.timeLimit = level.getTimeLimit();
		this.timeTextField.setText(new DecimalFormat().format(timeLimit));
		this.background = level.getLevelBackground();
		this.difficulty = level.getDifficulty();

		for (GameObject universalObject : this.universalObjects) {
			universalObject.setTexture();
			universalObject.setPosition(universalObject.getX(), universalObject.getY());
			universalObject.addListener(new PlacedObjectsInputListener(universalObject, this));
			stage.addActor(universalObject);
		}

		for (GameObject gameObject : this.gameObjects) {
			gameObject.setTexture();
			gameObject.setPosition(gameObject.getX(), gameObject.getY());
			gameObject.addListener(new PlacedObjectsInputListener(gameObject, this));
			stage.addActor(gameObject);
		}
	}

	public void generateXml(File file) {
		FileWriter fileWriter = null;
		try {
			fileWriter = new FileWriter(file);
			StringWriter stringWriter = new StringWriter();
			XmlWriter xml = new XmlWriter(stringWriter);

			xml.element("x-cars");

			xml.element("pathway");
			xml.attribute("pathwayType", pathway.getType().toString());
			xml.attribute("typeOfInterpolation", pathway.getTypeOfInterpolation().toString());
			xml.attribute("textureType", PATHWAY_TEXTURE_TYPE);
			xml.element("controlPoints");
			for (Vector2 point : pathway.getControlPoints()) {
				xml.element("point");
				xml.attribute("X", point.x);
				xml.attribute("Y", point.y);
				xml.pop(); // point
			}
			xml.pop(); // controlPoints
			xml.element("distanceMap");
			xml.attribute("nodesCount", pathway.getDistanceMap().getNodesCount());
			xml.pop(); // distanceMap
			xml.pop(); // pathway

			xml.element("entities");
			for (GameObject gameObject : gameObjects) {
				gameObject.toXml(xml);
			}
			xml.pop(); // entities

			xml.element("drawings");
			for (GameObject universalObject : universalObjects) {
				universalObject.toXml(xml);
			}
			xml.pop(); // drawings

			car.toXml(xml);
			start.toXml(xml);
			finish.toXml(xml);

			xml.element("background");
			background.toXml(xml);
			xml.pop(); // background

			xml.element("timeLimit", getTimeLimit());
			xml.element("difficulty", difficulty.toString());

			xml.pop(); // x-cars
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
		start = new Start(pathway.GetPosition(Constants.misc.START_POSITION_IN_CURVE).x,
				pathway.GetPosition(Constants.misc.START_POSITION_IN_CURVE).y, START_TYPE);
		start.setTexture();
		Vector2 startTo = pathway.GetPosition(delta);
		Vector2 startFrom = pathway.GetPosition(0);
		float startAngle = startTo.sub(startFrom).angle();
		start.setRotation((startAngle + 270) % 360);

		finish = new Finish(pathway.GetPosition(Constants.misc.FINISH_POSITION_IN_CURVE).x,
				pathway.GetPosition(Constants.misc.FINISH_POSITION_IN_CURVE).y, FINISH_TYPE);
		finish.setTexture();
		Vector2 finishTo = pathway.GetPosition(1 - delta);
		Vector2 finishFrom = pathway.GetPosition(1);
		float finishAngle = finishTo.sub(finishFrom).angle();
		finish.setRotation((finishAngle + 90) % 360);
	}

	private TextButton createGenerateButton() {
		TextButton buttonGeneratePoints = new TextButton("Generate", this.skin);

		// Creates listener
		buttonGeneratePoints.addListener(new MyInputListener(this) {
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				if (pathway.getControlPoints().size() < 4) {
					JOptionPane.showMessageDialog(null, "You have to set at least 4 points to generate the pathway.",
							"Warning: ", JOptionPane.INFORMATION_MESSAGE);
					return;
				}

				pathway.CreateDistances();
				pathwayTexture = pathway.getDistanceMap().generateTexture(difficulty);
				generateStartAndFinish();
			}
		});
		return buttonGeneratePoints;
	}

	private TextButton createRestartButton() {
		TextButton buttonRestart = new TextButton("Restart", this.skin);

		// Creates listener
		buttonRestart.addListener(new MyInputListener(this) {
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				if (JOptionPane.showConfirmDialog(null, "Are you sure that you want to reset this level?",
						"Question: ", JOptionPane.INFORMATION_MESSAGE) == JOptionPane.YES_OPTION)
					restart();
			}
		});
		return buttonRestart;
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

	private TextButton createSaveButton() {
		TextButton buttonSave = new TextButton("Save", this.skin);

		// Creates listener
		buttonSave.addListener(new MyInputListener(this) {
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {

				if (!isReadyForSaving()) {
					JOptionPane.showMessageDialog(null, "Level is not ready for saving", "Warning: ",
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
					if (!chooser.getSelectedFile().getAbsolutePath().endsWith(".xml"))
						fileToBeSaved = new File(chooser.getSelectedFile() + ".xml");
					else
						fileToBeSaved = chooser.getSelectedFile();

					try {
						generateXml(fileToBeSaved);
					} catch (Exception e) {
						JOptionPane.showMessageDialog(null, "Unable to save the file", "Warning: ",
								JOptionPane.INFORMATION_MESSAGE);
						e.printStackTrace();
					}

				}
			}
		});
		return buttonSave;
	}

	private TextButton createLoadButton() {
		TextButton buttonLoad = new TextButton("Load", this.skin);

		// Creates listener
		buttonLoad.addListener(new MyInputListener(this) {
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {

				JFileChooser chooser = new JFileChooser();
				chooser.setCurrentDirectory(new java.io.File("."));
				chooser.setDialogTitle("Load file");
				chooser.setDialogType(JFileChooser.OPEN_DIALOG);
				chooser.setAcceptAllFileFilterUsed(false);

				if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
					File fileToBeLoad;
					if (!chooser.getSelectedFile().getAbsolutePath().endsWith(".xml"))
						fileToBeLoad = new File(chooser.getSelectedFile() + ".xml");
					else
						fileToBeLoad = chooser.getSelectedFile();

					try {
						loadLevel(new FileHandle(fileToBeLoad));
					} catch (Exception e) {
						JOptionPane.showMessageDialog(null, "Unable to load the file", "Warning: ",
								JOptionPane.INFORMATION_MESSAGE);
						System.out.println(e.getMessage());
						restart();
					}

				}
			}
		});
		return buttonLoad;
	}

	private TextButton createUnpointButton() {
		TextButton buttonUnpoint = new TextButton("Unpoint", this.skin);

		// Creates listener
		buttonUnpoint.addListener(new MyInputListener(this) {
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				if (pathway.getControlPoints().size() > 0)
					pathway.getControlPoints().remove(pathway.getControlPoints().size() - 1);
			}
		});
		return buttonUnpoint;
	}

	private ScrollPane createBackgroundButtons() {
		int width = 30;
		int height = 45;

		Table group = new Table();

		ScrollPane backgroundButtons = new ScrollPane(group, this.skin);
		backgroundButtons.setFlickScroll(false);
		backgroundButtons.setFadeScrollBars(false);
		backgroundButtons.setScrollbarsOnTop(false);
		backgroundButtons.setScrollingDisabled(false, true);
		Button colorButton = new Button(new ColorDrawable(new Color(1, 0, 0, 0.5f)));
		colorButton.addListener(new ColorBackgroundInputListener(this));
		group.add(colorButton).width(2 * width).height(height);

		for (int i = 1; i <= EditorConstants.LEVEL_BACKGROUND_TEXTURE_TYPES_COUNT; i++) {
			String name = EditorConstants.LEVEL_BACKGROUND_TEXTURE_PREFIX + Integer.toString(i);
			Button button = new Button(new TextureRegionDrawable(XcarsEditor.getInstance().assets.getGraphics(name)));
			button.addListener(new TexturedBackgroundInputListener(name, this));
			group.add(button).width(width).height(height);
		}
		return backgroundButtons;
	}

	public enum TypeOfObjectToDrag {
		HOLE, MUD, STONE, TREE, BOOSTER, FENCE, PARKING_CAR, HOUSE, WALL, HILL, TORNADO, RACING_CAR, PNEU, ARROW, UNIVERSAL
	}

	private ScrollPane createGameObjectsButtons() {
		com.badlogic.gdx.scenes.scene2d.ui.Tree objectsTree = new com.badlogic.gdx.scenes.scene2d.ui.Tree(this.skin);

		final int maxColumnSize = 8;

		// Holes
		final Node holesNode = new Node(new Label("Holes", this.skin));
		objectsTree.add(holesNode);
		Table holesTable = new Table();
		holesNode.add(new Node(holesTable));
		int holeIndex = 1;
		while (game.assets.graphicExist(Hole.GetTextureName(holeIndex))) {
			TextureRegionDrawable trd = new TextureRegionDrawable(game.assets.getGraphics(Hole
					.GetTextureName(holeIndex)));
			createGameObjectButtons(holesTable, trd, TypeOfObjectToDrag.HOLE, holeIndex, maxColumnSize);
			++holeIndex;
		}

		// Muds
		Node mudsNode = new Node(new Label("Muds", this.skin));
		objectsTree.add(mudsNode);
		Table mudsTable = new Table();
		mudsNode.add(new Node(mudsTable));
		int mudIndex = 1;
		while (game.assets.graphicExist(Mud.GetTextureName(mudIndex))) {
			TextureRegionDrawable trd = new TextureRegionDrawable(game.assets.getGraphics(Mud.GetTextureName(mudIndex)));
			createGameObjectButtons(mudsTable, trd, TypeOfObjectToDrag.MUD, mudIndex, maxColumnSize);
			++mudIndex;
		}

		// Stones
		Node stonesNode = new Node(new Label("Stones", this.skin));
		objectsTree.add(stonesNode);
		Table stonesTable = new Table();
		stonesNode.add(new Node(stonesTable));
		int stoneIndex = 1;
		while (game.assets.graphicExist(Stone.GetTextureName(stoneIndex))) {
			TextureRegionDrawable trd = new TextureRegionDrawable(game.assets.getGraphics(Stone
					.GetTextureName(stoneIndex)));
			createGameObjectButtons(stonesTable, trd, TypeOfObjectToDrag.STONE, stoneIndex, maxColumnSize);
			++stoneIndex;
		}

		// Trees
		Node treesNode = new Node(new Label("Trees", this.skin));
		objectsTree.add(treesNode);
		Table treesTable = new Table();
		treesNode.add(new Node(treesTable));
		int treeIndex = 1;
		while (game.assets.graphicExist(Tree.GetStaticTextureName(treeIndex))) {
			TextureRegionDrawable trd = new TextureRegionDrawable(game.assets.getGraphics(Tree
					.GetStaticTextureName(treeIndex)));
			createGameObjectButtons(treesTable, trd, TypeOfObjectToDrag.TREE, treeIndex, maxColumnSize);
			++treeIndex;
		}

		// Boosts
		Node boostsNode = new Node(new Label("Boosts", this.skin));
		objectsTree.add(boostsNode);
		Table boostsTable = new Table();
		boostsNode.add(new Node(boostsTable));
		int boostIndex = 1;
		while (game.assets.graphicExist(Booster.GetTextureName(boostIndex))) {
			TextureRegionDrawable trd = new TextureRegionDrawable(game.assets.getGraphics(Booster
					.GetTextureName(boostIndex)));
			createGameObjectButtons(boostsTable, trd, TypeOfObjectToDrag.BOOSTER, boostIndex, maxColumnSize);
			++boostIndex;
		}

		// Fences
		Node fencesNode = new Node(new Label("Fences", this.skin));
		objectsTree.add(fencesNode);
		Table fencesTable = new Table();
		fencesNode.add(new Node(fencesTable));
		int fenceIndex = 1;
		while (game.assets.graphicExist(Fence.GetStaticTextureName(fenceIndex))) {
			TextureRegionDrawable trd = new TextureRegionDrawable(game.assets.getGraphics(Fence
					.GetStaticTextureName(fenceIndex)));
			createGameObjectButtons(fencesTable, trd, TypeOfObjectToDrag.FENCE, fenceIndex, maxColumnSize);
			++fenceIndex;
		}

		// Houses
		Node housesNode = new Node(new Label("Houses", this.skin));
		objectsTree.add(housesNode);
		Table housesTable = new Table();
		housesNode.add(new Node(housesTable));
		int houseIndex = 1;
		while (game.assets.graphicExist(House.GetStaticTextureName(houseIndex))) {
			TextureRegionDrawable trd = new TextureRegionDrawable(game.assets.getGraphics(House
					.GetStaticTextureName(houseIndex)));
			createGameObjectButtons(housesTable, trd, TypeOfObjectToDrag.HOUSE, houseIndex, maxColumnSize);
			++houseIndex;
		}

		// Parking cars
		Node parkingCarsNode = new Node(new Label("Parking Cars", this.skin));
		objectsTree.add(parkingCarsNode);
		Table parkingCarsTable = new Table();
		parkingCarsNode.add(new Node(parkingCarsTable));
		int parkingCarIndex = 1;
		while (game.assets.graphicExist(ParkingCar.GetStaticTextureName(parkingCarIndex))) {
			TextureRegionDrawable trd = new TextureRegionDrawable(game.assets.getGraphics(ParkingCar
					.GetStaticTextureName(parkingCarIndex)));
			createGameObjectButtons(parkingCarsTable, trd, TypeOfObjectToDrag.PARKING_CAR, parkingCarIndex,
					maxColumnSize);
			++parkingCarIndex;
		}

		// Walls
		Node wallsNode = new Node(new Label("Walls", this.skin));
		objectsTree.add(wallsNode);
		Table wallsTable = new Table();
		wallsNode.add(new Node(wallsTable));
		int wallIndex = 1;
		while (game.assets.graphicExist(Wall.GetStaticTextureName(wallIndex))) {
			TextureRegionDrawable trd = new TextureRegionDrawable(game.assets.getGraphics(Wall
					.GetStaticTextureName(wallIndex)));
			createGameObjectButtons(wallsTable, trd, TypeOfObjectToDrag.WALL, wallIndex, maxColumnSize);
			++wallIndex;
		}

		// Hills
		Node hillsNode = new Node(new Label("Hills", this.skin));
		objectsTree.add(hillsNode);
		Table hillsTable = new Table();
		hillsNode.add(new Node(hillsTable));
		int hillIndex = 1;
		while (game.assets.graphicExist(Hill.GetTextureName(hillIndex))) {
			TextureRegionDrawable trd = new TextureRegionDrawable(game.assets.getGraphics(Hill
					.GetTextureName(hillIndex)));
			createGameObjectButtons(hillsTable, trd, TypeOfObjectToDrag.HILL, hillIndex, maxColumnSize);
			++hillIndex;
		}

		// Tornados
		Node tornadosNode = new Node(new Label("Tornados", this.skin));
		objectsTree.add(tornadosNode);
		Table tornadosTable = new Table();
		tornadosNode.add(new Node(tornadosTable));
		int tornadoIndex = 1;
		while (game.assets.graphicExist(Tornado.GetTextureName(tornadoIndex))) {
			TextureRegionDrawable trd = new TextureRegionDrawable(game.assets.getGraphics(Tornado
					.GetTextureName(tornadoIndex)));
			createGameObjectButtons(tornadosTable, trd, TypeOfObjectToDrag.TORNADO, tornadoIndex, maxColumnSize);
			++tornadoIndex;
		}

		// Racing cars
		Node racingCarsNode = new Node(new Label("Racing Cars", this.skin));
		objectsTree.add(racingCarsNode);
		Table racingCarsTable = new Table();
		racingCarsNode.add(new Node(racingCarsTable));
		int racingCarIndex = 1;
		while (game.assets.graphicExist(RacingCar.GetStaticTextureName(racingCarIndex))) {
			TextureRegionDrawable trd = new TextureRegionDrawable(game.assets.getGraphics(RacingCar
					.GetStaticTextureName(racingCarIndex)));
			createGameObjectButtons(racingCarsTable, trd, TypeOfObjectToDrag.RACING_CAR, racingCarIndex, maxColumnSize);
			++racingCarIndex;
		}

		// Pneu
		Node pneuNode = new Node(new Label("Pneu", this.skin));
		objectsTree.add(pneuNode);
		Table pneuTable = new Table();
		pneuNode.add(new Node(pneuTable));
		int pneuIndex = 1;
		while (game.assets.graphicExist(Pneu.GetTextureName(pneuIndex))) {
			TextureRegionDrawable trd = new TextureRegionDrawable(game.assets.getGraphics(Pneu
					.GetTextureName(pneuIndex)));
			createGameObjectButtons(pneuTable, trd, TypeOfObjectToDrag.PNEU, pneuIndex, maxColumnSize);
			++pneuIndex;
		}

		// Universal
		Node universalNode = new Node(new Label("Universal", this.skin));
		objectsTree.add(universalNode);
		Table universalTable = new Table();
		universalNode.add(new Node(universalTable));
		int universalIndex = 1;
		while (game.assets.graphicExist(UniversalGameObject.GetTextureName(universalIndex))) {
			TextureRegionDrawable trd = new TextureRegionDrawable(game.assets.getGraphics(UniversalGameObject
					.GetTextureName(universalIndex)));
			createGameObjectButtons(universalTable, trd, TypeOfObjectToDrag.UNIVERSAL, universalIndex, maxColumnSize);
			++universalIndex;
		}

		// Arrows
		Node arrowsNode = new Node(new Label("Arrows", this.skin));
		objectsTree.add(arrowsNode);
		Table arrowsTable = new Table();
		arrowsNode.add(new Node(arrowsTable));
		int arrowsIndex = 1;
		while (game.assets.graphicExist(Arrow.GetTextureName(arrowsIndex))) {
			TextureRegionDrawable trd = new TextureRegionDrawable(game.assets.getGraphics(Arrow
					.GetTextureName(arrowsIndex)));
			createGameObjectButtons(arrowsTable, trd, TypeOfObjectToDrag.ARROW, arrowsIndex, maxColumnSize);
			++arrowsIndex;
		}

		ScrollPane objectsScrollPane = new ScrollPane(objectsTree, this.skin);
		objectsScrollPane.setFlickScroll(false);
		return objectsScrollPane;
	}

	private void createGameObjectButtons(Table table, TextureRegionDrawable trd, TypeOfObjectToDrag typeOfClass,
			int type, int maxColumnSize) {
		if ((type - 1) % maxColumnSize == 0) {
			table.row();
		}
		Button button = new ImageButton(trd);
		button.addListener(new MyInputListenerForGameObjects(typeOfClass, type, this));
		table.add(button).height(40).width(40);
	}

	private Table createAdditionalControllers() {
		Table group = new Table();

		// Difficulty
		group.add(new Label("Difficulty: ", this.skin));

		Array<String> listChoises = new Array<String>();
		for (int i = 0; i < Difficulty.values().length; i++) {
			listChoises.add(Difficulty.values()[i].toString());
		}
		SelectBox<String> selectBox = new SelectBox<String>(this.skin);
		group.add(selectBox);
		selectBox.setItems(listChoises);
		;
		selectBox.addListener(new ChangeListener() {
			@SuppressWarnings("unchecked")
			@Override
			public void changed(ChangeEvent event, Actor actor) {

				Difficulty difficulty = Difficulty.valueOf(((SelectBox<String>) actor).getSelected());
				EditorScreen screen = EditorScreen.this;
				screen.difficulty = difficulty;
				if (screen.getPathway() != null && screen.getPathway().getDistanceMap() != null)
					screen.setPathwayTexture(screen.getPathway().getDistanceMap().generateTexture(difficulty));
			}
		});
		selectBox.setSelected(this.difficulty.toString());

		// Time limit
		group.add(new Label("Time limit: ", this.skin)).padLeft(30);

		TextField timeTextField = new TextField(new DecimalFormat().format(EditorScreen.this.timeLimit), this.skin);
		this.timeTextField = timeTextField;
		timeTextField.setAlignment(Align.right);
		group.add(timeTextField).width(25).right();
		timeTextField.setTextFieldListener(new DigitsTextFieldInputListener(this));
		timeTextField.addListener(new InputListener() {
			@Override
			public boolean keyTyped(InputEvent event, char character) {
				if (event.getCharacter() == '+' || event.getKeyCode() == 19 || event.getKeyCode() == 22) {
					EditorScreen.this.timeLimit++;
					if (EditorScreen.this.timeLimit > 99)
						EditorScreen.this.timeLimit = 99;
					EditorScreen.this.timeTextField.setText(new DecimalFormat().format(EditorScreen.this.timeLimit));
				} else if (event.getCharacter() == '-' || event.getKeyCode() == 20 || event.getKeyCode() == 21) {
					EditorScreen.this.timeLimit--;
					if (EditorScreen.this.timeLimit < 0)
						EditorScreen.this.timeLimit = 0;
					EditorScreen.this.timeTextField.setText(new DecimalFormat().format(EditorScreen.this.timeLimit));
				}
				EditorScreen.this.timeTextField.setCursorPosition(EditorScreen.this.timeTextField.getText().length());
				return true;
			}
		});
		this.stage.setKeyboardFocus(timeTextField);
		return group;
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

	public void removeObject(Object object) {
		if (object instanceof GameObject) {
			GameObject gameObject = (GameObject) object;
			if (gameObject instanceof UniversalGameObject) {
				universalObjects.remove(gameObject);
			} else {
				gameObjects.remove(gameObject);
			}
			gameObject.remove();
		}
	}

	public void SetAnyButtonTouched(boolean value) {
		this.anyButtonTouched = value;
	}

	public void SetBackground(LevelBackground value) {
		this.background = value;
	}
}
