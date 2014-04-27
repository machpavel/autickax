package cz.mff.cuni.autickax;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.text.DecimalFormat;
import java.util.ArrayList;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
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
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldStyle;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.XmlWriter;

import cz.cuni.mff.autickax.colorDrawable.ColorDrawable;
import cz.mff.cuni.autickax.constants.Constants;
import cz.mff.cuni.autickax.drawing.LevelBackground;
import cz.mff.cuni.autickax.drawing.LevelConstantBackground;
import cz.mff.cuni.autickax.entities.Booster;
import cz.mff.cuni.autickax.entities.Car;
import cz.mff.cuni.autickax.entities.Fence;
import cz.mff.cuni.autickax.entities.Finish;
import cz.mff.cuni.autickax.entities.GameObject;
import cz.mff.cuni.autickax.entities.Hill;
import cz.mff.cuni.autickax.entities.Hole;
import cz.mff.cuni.autickax.entities.House;
import cz.mff.cuni.autickax.entities.Mud;
import cz.mff.cuni.autickax.entities.ParkingCar;
import cz.mff.cuni.autickax.entities.Start;
import cz.mff.cuni.autickax.entities.Stone;
import cz.mff.cuni.autickax.entities.Tree;
import cz.mff.cuni.autickax.entities.Wall;
import cz.mff.cuni.autickax.myInputListener.ColorBackgroundInputListener;
import cz.mff.cuni.autickax.myInputListener.TexturedBackgroundInputListener;
import cz.mff.cuni.autickax.myInputListener.MyInputListener;
import cz.mff.cuni.autickax.myInputListener.MyInputListenerForGameObjects;
import cz.mff.cuni.autickax.myInputListener.DigitsTextFieldInputListener;
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

	protected static final int FINISH_TYPE = 2;
	protected static final int START_TYPE = 4;
	protected static final float TIME_LIMIT_DEFAULT = 10;

	private final TextButtonStyle textButtonStyle;
	private final TextFieldStyle textFieldStyle;

	private float timeLimit = TIME_LIMIT_DEFAULT;

	// Rendering
	private OrthographicCamera camera;
	private SpriteBatch batch;
	private ShapeRenderer shapeRenderer;

	// Entities
	private ArrayList<GameObject> gameObjects;
	private Car car = new Car(0, 0, CAR_TYPE);
	private Start start;
	private Finish finish;

	// Cached font

	private BitmapFont font;
	// Pathway
	private Pathway pathway;

	// Background
	private LevelBackground background;

	// Pathway texture
	private TextureRegion pathwayTexture;

	// Buttons
	Button buttonGeneratePoints;
	Button buttonRestart;
	Button buttonSave;
	Button buttonLoad;
	TextField timeTextField;
	ArrayList<Button> backgroundButtons = new ArrayList<Button>();
	private boolean anyButtonTouched = false;

	public Difficulty difficulty = Difficulty.Normal;

	// Variables for dragging new object values
	public boolean objectIsDragging = false;
	public boolean newObjectIsDragging = false;
	public GameObject draggedObject = null;
	public Button draggedButton = null;

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

		camera = new OrthographicCamera();
		camera.setToOrtho(false, Constants.WORLD_WIDTH, Constants.WORLD_HEIGHT);

		batch = new SpriteBatch();
		shapeRenderer = new ShapeRenderer();

		this.font = game.assets.getFont();

		ColorDrawable textButtonBackground = new ColorDrawable(Color.GREEN);
		this.textButtonStyle = new TextButtonStyle(textButtonBackground, textButtonBackground,
				textButtonBackground, this.font);
		this.textFieldStyle = new TextFieldStyle(this.font, new Color(1, 0, 0, 1),
				new ColorDrawable(Color.GREEN), new ColorDrawable(Color.WHITE), new ColorDrawable(
						Color.BLUE));

		restart();
	}

	public void restart() {
		stage.clear();
		background = new LevelConstantBackground(169, 207, 56);

		// This order has to be kept.
		createGenerateButton();
		createRestartButton();
		createSaveButton();
		createLoadButton();
		createBackgroundButtons();
		createGameObjectsButtons();
		createDifficultyButtons();
		createTextField();

		this.gameObjects = new ArrayList<GameObject>();

		// Pathway
		pathway = new Pathway(pathwayType, typeOfInterpolation);
		start = null;
		finish = null;

		objectIsDragging = false;
		draggedObject = null;
		timeLimit = TIME_LIMIT_DEFAULT;
		timeTextField.setText(new DecimalFormat().format(timeLimit));
		stage.setKeyboardFocus(timeTextField);
	}

	public void LoadLevel(FileHandle file) throws Exception {
		restart();
		Level level = new Level();
		level.parseLevel(file);
		level.calculateDistanceMap();

		this.car = level.getCar();
		this.finish = level.getFinish();
		this.gameObjects = level.getGameObjects();
		this.pathway = level.getPathway();
		this.start = level.getStart();
		timeLimit = level.getTimeLimit();
		timeTextField.setText(new DecimalFormat().format(timeLimit));

		this.background = level.getLevelBackground();

		for (GameObject gameObject : this.gameObjects) {
			gameObject.setTexture();

			Button button = new ImageButton(new TextureRegionDrawable(gameObject.getTexture()),
					new TextureRegionDrawable(gameObject.getTexture()));

			button.setPosition(gameObject.getPosition().x - gameObject.getWidth() / 2,
					gameObject.getPosition().y - gameObject.getHeight() / 2);

			button.addListener(new PlacedObjectsInputListener(gameObject, button, this));
			stage.addActor(button);
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
			if (x > 0 && x < Constants.WORLD_WIDTH && y > 0 && y < Constants.WORLD_HEIGHT) {
				Vector2 point = new Vector2(Gdx.input.getX(), stageHeight - Gdx.input.getY());
				pathway.getControlPoints().add(point);
			}
		}
	}

	@Override
	public void render(float delta) {
		stage.act(delta);

		if (anyButtonTouched) {
			SetAnyButtonTouched(false);
		} else {
			update(delta);
		}

		renderScene();

		if (objectIsDragging) {
			if (Gdx.input.isTouched()) {
				if (this.draggedObject != null) {
					// dragging new object
					draggedObject.setPosition(new Vector2(Gdx.input.getX(), Constants.WORLD_HEIGHT
							- Gdx.input.getY()));
					batch.begin();
					draggedObject.draw(batch);
					batch.end();
				} else {
					// dragging placed object
					this.draggedButton.setPosition(
							Gdx.input.getX() - this.draggedButton.getWidth() / 2,
							Constants.WORLD_HEIGHT - Gdx.input.getY()
									- this.draggedButton.getHeight() / 2);

				}
			} else {
				float x = Gdx.input.getX();
				float y = Constants.WORLD_HEIGHT - Gdx.input.getY();

				if (this.newObjectIsDragging && x > 0 && x < Constants.WORLD_WIDTH && y > 0
						&& y < Constants.WORLD_HEIGHT) {

					Button button = new ImageButton(new TextureRegionDrawable(
							this.draggedObject.getTexture()), new TextureRegionDrawable(
							this.draggedObject.getTexture()));

					button.setPosition(
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
		// Gdx.gl.glClearColor(0.5f, 0.5f, 0.5f, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

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
		shapeRenderer.rect(Constants.WORLD_WIDTH, 0, EditorConstants.CONTROL_PANEL_WIDTH,
				Constants.WORLD_HEIGHT);
		shapeRenderer.end();

		// Renders control points
		Gdx.gl10.glPointSize(4);
		shapeRenderer.begin(ShapeType.Point);
		for (Vector2 point : pathway.getControlPoints()) {
			shapeRenderer.setColor(1f, 0f, 0f, 1);
			shapeRenderer.point(point.x, point.y, 0);
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

	private void createGenerateButton() {
		buttonGeneratePoints = new TextButton("Generate", this.textButtonStyle);
		buttonGeneratePoints.setPosition(Constants.WORLD_WIDTH, 0);
		stage.addActor(buttonGeneratePoints);

		// Creates listener
		buttonGeneratePoints.addListener(new MyInputListener(this) {
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				if (pathway.getControlPoints().size() < 4) {
					JOptionPane.showMessageDialog(null,
							"You have to set at least 4 points to generate the pathway.",
							"Warning: ", JOptionPane.INFORMATION_MESSAGE);
					return;
				}

				pathway.CreateDistances();
				pathwayTexture = pathway.getDistanceMap().generateTexture(difficulty);
				generateStartAndFinish();
			}
		});
	}

	private void createRestartButton() {
		buttonRestart = new TextButton("Restart", this.textButtonStyle);
		buttonRestart.setPosition(Constants.WORLD_WIDTH, buttonGeneratePoints.getY()
				+ buttonGeneratePoints.getHeight());
		stage.addActor(buttonRestart);

		// Creates listener
		buttonRestart.addListener(new MyInputListener(this) {
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				if (JOptionPane.showConfirmDialog(null,
						"Are you sure that you want to reset this level?", "Question: ",
						JOptionPane.INFORMATION_MESSAGE) == JOptionPane.YES_OPTION)
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
		buttonSave.setPosition(Constants.WORLD_WIDTH,
				buttonRestart.getY() + buttonRestart.getHeight());
		stage.addActor(buttonSave);

		// Creates listener
		buttonSave.addListener(new MyInputListener(this) {
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {

				if (!isReadyForSaving()) {
					JOptionPane.showMessageDialog(null, "Level is not ready for saving",
							"Warning: ", JOptionPane.INFORMATION_MESSAGE);
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

					}

				}
			}
		});
	}

	private void createLoadButton() {
		buttonLoad = new TextButton("Load", this.textButtonStyle);
		buttonLoad.setPosition(Constants.WORLD_WIDTH + 20 + buttonSave.getWidth(),
				buttonSave.getY());
		stage.addActor(buttonLoad);

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
						LoadLevel(new FileHandle(fileToBeLoad));
					} catch (Exception e) {
						JOptionPane.showMessageDialog(null, "Unable to load the file", "Warning: ",
								JOptionPane.INFORMATION_MESSAGE);
						restart();
					}

				}
			}
		});
	}

	private void createBackgroundButtons() {
		int widthOffset = 0;
		int heightOffset = 0;
		int width = 30;
		int height = 18;

		Button colorButton = new Button(new ColorDrawable(new Color(1, 0, 0, 0.5f)));
		colorButton.setPosition(Constants.WORLD_WIDTH + 5 + widthOffset, Constants.WORLD_HEIGHT
				- heightOffset - height);
		colorButton.setWidth(2 * width);
		;
		colorButton.setHeight(height);
		colorButton.addListener(new ColorBackgroundInputListener(this));
		stage.addActor(colorButton);
		widthOffset += 2 * width;

		for (int i = 1; i <= EditorConstants.LEVEL_BACKGROUND_TEXTURE_TYPES_COUNT; i++) {
			String name = EditorConstants.LEVEL_BACKGROUND_TEXTURE_PREFIX + Integer.toString(i);
			Button button = new Button(new TextureRegionDrawable(
					AutickaxEditor.getInstance().assets.getGraphics(name)));
			button.setPosition(Constants.WORLD_WIDTH + 5 + widthOffset, Constants.WORLD_HEIGHT
					- heightOffset - height);
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

	public enum TypeOfGameObjectButton {
		HOLE, MUD, STONE, TREE, BOOSTER, FENCE, PARKING_CAR, HOUSE, WALL, HILL
	}

	private void createGameObjectsButtons() {
		// gameObjects.add(new Tree(300,300,null, 4));
		TextureRegionDrawable trd;
		Vector2i offsetOnScreen = new Vector2i(0, 0);
		Vector2i maxValue = new Vector2i(0, 0);

		// Holes
		for (int i = 1; i <= Constants.gameObjects.HOLE_TYPES_COUNT; i++) {
			trd = new TextureRegionDrawable(game.assets.getGraphics(Hole.GetTextureName(i)));
			createGameObjectButtons(trd, TypeOfGameObjectButton.HOLE, i, offsetOnScreen, maxValue);
		}

		// Muds
		for (int i = 1; i <= Constants.gameObjects.MUD_TYPES_COUNT; i++) {
			trd = new TextureRegionDrawable(game.assets.getGraphics(Mud.GetTextureName(i)));
			createGameObjectButtons(trd, TypeOfGameObjectButton.MUD, i, offsetOnScreen, maxValue);
		}

		// Stones
		for (int i = 1; i <= Constants.gameObjects.STONE_TYPES_COUNT; i++) {
			trd = new TextureRegionDrawable(game.assets.getGraphics(Stone.GetTextureName(i)));
			createGameObjectButtons(trd, TypeOfGameObjectButton.STONE, i, offsetOnScreen, maxValue);
		}

		// Trees
		for (int i = 1; i <= Constants.gameObjects.TREE_TYPES_COUNT; i++) {
			trd = new TextureRegionDrawable(game.assets.getGraphics(Tree.GetStaticTextureName(i)));
			createGameObjectButtons(trd, TypeOfGameObjectButton.TREE, i, offsetOnScreen, maxValue);
		}

		// Boosts
		for (int i = 1; i <= Constants.gameObjects.BOOSTER_TYPES_COUNT; i++) {
			trd = new TextureRegionDrawable(game.assets.getGraphics(Booster.GetTextureName(i)));
			createGameObjectButtons(trd, TypeOfGameObjectButton.BOOSTER, i, offsetOnScreen,
					maxValue);
		}

		// Fences
		for (int i = 1; i <= Constants.gameObjects.FENCE_TYPES_COUNT; i++) {
			trd = new TextureRegionDrawable(game.assets.getGraphics(Fence.GetStaticTextureName(i)));
			createGameObjectButtons(trd, TypeOfGameObjectButton.FENCE, i, offsetOnScreen, maxValue);
		}

		// Houses
		for (int i = 1; i <= Constants.gameObjects.HOUSE_TYPES_COUNT; i++) {
			trd = new TextureRegionDrawable(game.assets.getGraphics(House.GetStaticTextureName(i)));
			createGameObjectButtons(trd, TypeOfGameObjectButton.HOUSE, i, offsetOnScreen, maxValue);
		}
		// Parking cars
		for (int i = 1; i <= Constants.gameObjects.PARKING_CAR_TYPES_COUNT; i++) {
			trd = new TextureRegionDrawable(game.assets.getGraphics(ParkingCar
					.GetStaticTextureName(i)));
			createGameObjectButtons(trd, TypeOfGameObjectButton.PARKING_CAR, i, offsetOnScreen,
					maxValue);
		}
		// Walls
		for (int i = 1; i <= Constants.gameObjects.WALL_TYPES_COUNT; i++) {
			trd = new TextureRegionDrawable(game.assets.getGraphics(Wall.GetStaticTextureName(i)));
			createGameObjectButtons(trd, TypeOfGameObjectButton.WALL, i, offsetOnScreen, maxValue);
		}
		// Hills
		for (int i = 1; i <= Constants.gameObjects.HILL_TYPES_COUNT; i++) {
			trd = new TextureRegionDrawable(game.assets.getGraphics(Hill.GetTextureName(i)));
			createGameObjectButtons(trd, TypeOfGameObjectButton.HILL, i, offsetOnScreen, maxValue);
		}
	}

	private void createGameObjectButtons(TextureRegionDrawable trd,
			TypeOfGameObjectButton typeOfClass, int type, Vector2i offsetOnScreen, Vector2i maxValue) {

		int HEIGHT_OFFSET = 50;
		Button button = new ImageButton(trd);
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

		button.setPosition(Constants.WORLD_WIDTH + 5 + offsetOnScreen.x, Constants.WORLD_HEIGHT
				- HEIGHT_OFFSET - offsetOnScreen.y);
		button.addListener(new MyInputListenerForGameObjects(typeOfClass, type, this));
		stage.addActor(button);

		offsetOnScreen.x += objectWidth;
	}

	private void createDifficultyButtons() {
		int BUTTONS_OFFSET = 10;
		Button buttonDif1 = new TextButton("1", this.textButtonStyle);
		buttonDif1.setPosition(Constants.WORLD_WIDTH + BUTTONS_OFFSET, buttonSave.getY()
				+ buttonSave.getHeight() + BUTTONS_OFFSET);
		stage.addActor(buttonDif1);

		// Creates listener
		buttonDif1.addListener(new MyInputListener(this) {
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				difficulty = Difficulty.Kiddie;
				pathwayTexture = pathway.getDistanceMap().generateTexture(difficulty);
			}
		});
		Button buttonDif2 = new TextButton("2", this.textButtonStyle);
		buttonDif2.setPosition(buttonDif1.getX() + buttonDif1.getWidth() + BUTTONS_OFFSET,
				buttonSave.getY() + buttonSave.getHeight() + BUTTONS_OFFSET);
		stage.addActor(buttonDif2);

		// Creates listener
		buttonDif2.addListener(new MyInputListener(this) {
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				difficulty = Difficulty.Beginner;
				pathwayTexture = pathway.getDistanceMap().generateTexture(difficulty);
			}
		});
		Button buttonDif3 = new TextButton("3", this.textButtonStyle);
		buttonDif3.setPosition(buttonDif2.getX() + buttonDif2.getWidth() + BUTTONS_OFFSET,
				buttonSave.getY() + buttonSave.getHeight() + BUTTONS_OFFSET);
		stage.addActor(buttonDif3);

		// Creates listener
		buttonDif3.addListener(new MyInputListener(this) {
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				difficulty = Difficulty.Normal;
				pathwayTexture = pathway.getDistanceMap().generateTexture(difficulty);
			}
		});

		Button buttonDif4 = new TextButton("4", this.textButtonStyle);
		buttonDif4.setPosition(buttonDif3.getX() + buttonDif3.getWidth() + BUTTONS_OFFSET,
				buttonSave.getY() + buttonSave.getHeight() + BUTTONS_OFFSET);
		stage.addActor(buttonDif4);

		// Creates listener
		buttonDif4.addListener(new MyInputListener(this) {
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				difficulty = Difficulty.Hard;
				pathwayTexture = pathway.getDistanceMap().generateTexture(difficulty);
			}
		});

		Button buttonDif5 = new TextButton("5", this.textButtonStyle);
		buttonDif5.setPosition(buttonDif4.getX() + buttonDif4.getWidth() + BUTTONS_OFFSET,
				buttonSave.getY() + buttonSave.getHeight() + BUTTONS_OFFSET);
		stage.addActor(buttonDif5);

		// Creates listener
		buttonDif5.addListener(new MyInputListener(this) {
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				difficulty = Difficulty.Extreme;
				pathwayTexture = pathway.getDistanceMap().generateTexture(difficulty);
			}
		});
	}

	private void createTextField() {
		timeTextField = new TextField(new DecimalFormat().format(timeLimit), this.textFieldStyle);
		timeTextField.setPosition(buttonRestart.getX() + buttonRestart.getWidth() + 20,
				buttonRestart.getHeight());
		timeTextField.setTextFieldListener(new DigitsTextFieldInputListener(this));
		timeTextField.addListener(new InputListener() {
			@Override
			public boolean keyTyped(InputEvent event, char character) {
				if (event.getCharacter() == '+' || event.getKeyCode() == 19
						|| event.getKeyCode() == 22) {
					timeLimit++;
					if (timeLimit > 99)
						timeLimit = 99;
					timeTextField.setText(new DecimalFormat().format(timeLimit));
				} else if (event.getCharacter() == '-' || event.getKeyCode() == 20
						|| event.getKeyCode() == 21) {
					timeLimit--;
					if (timeLimit < 0)
						timeLimit = 0;
					timeTextField.setText(new DecimalFormat().format(timeLimit));
				}
				timeTextField.setCursorPosition(timeTextField.getText().length());
				return super.keyTyped(event, character);
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

}
