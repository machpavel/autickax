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
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
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
import cz.mff.cuni.autickax.entities.Booster;
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
import cz.mff.cuni.autickax.myInputListener.MyTextNumberListener;
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
	private LevelBackground background = new LevelBackground();

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

	public EditorScreen() {
		super();

		camera = new OrthographicCamera();
		camera.setToOrtho(false, Constants.WORLD_WIDTH, Constants.WORLD_HEIGHT);

		batch = new SpriteBatch();
		shapeRenderer = new ShapeRenderer();

		this.font = game.assets.getFont();

		Sprite backgroundColor = new Sprite();
		backgroundColor.setColor(Color.GREEN);
		ColorDrawable textButtonBackground = new ColorDrawable(Color.GREEN);
		this.textButtonStyle = new TextButtonStyle(textButtonBackground,
				textButtonBackground, textButtonBackground, this.font);
		this.textFieldStyle = new TextFieldStyle(this.font, new Color(1, 0, 0, 1), new ColorDrawable(Color.GREEN), new ColorDrawable(Color.WHITE), new ColorDrawable(Color.BLUE));

		restart();
	}

	public void restart() {
		background.SetType(1);

		stage.clear();

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
		this.background.SetType(level.getBackgroundType());

		for (GameObject gameObject : this.gameObjects) {
			gameObject.setTexture();

			Button button = new ImageButton(new TextureRegionDrawable(
					gameObject.getTexture()), new TextureRegionDrawable(
					gameObject.getTexture()));

			button.setPosition(
					gameObject.getPosition().x - gameObject.getWidth() / 2,
					gameObject.getPosition().y - gameObject.getHeight() / 2);

			button.addListener(new PlacedObjectsInputListener(gameObject,
					button, this));
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
			if (x > 0 && x < Constants.WORLD_WIDTH && y > 0
					&& y < Constants.WORLD_HEIGHT) {
				Vector2 point = new Vector2(Gdx.input.getX(), stageHeight
						- Gdx.input.getY());
				pathway.getControlPoints().add(point);
			}
		}
	}

	@Override
	public void render(float delta) {
		stage.act(delta); // don't forget to advance the stage ( input + actions

		if (anyButtonTouched) {
			SetAnyButtonTouched(false);
		} else
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
					this.draggedButton.setPosition(Gdx.input.getX()
							- this.draggedButton.getWidth() / 2,
							Constants.WORLD_HEIGHT - Gdx.input.getY()
									- this.draggedButton.getHeight() / 2);
					
					
				}
			} else {
				float x = Gdx.input.getX();
				float y = Constants.WORLD_HEIGHT - Gdx.input.getY();

				if (this.newObjectIsDragging && x > 0
						&& x < Constants.WORLD_WIDTH && y > 0
						&& y < Constants.WORLD_HEIGHT) {

					Button button = new ImageButton(new TextureRegionDrawable(
							this.draggedObject.getTexture()),
							new TextureRegionDrawable(this.draggedObject
									.getTexture()));

					button.setPosition(this.draggedObject.getPosition().x
							- this.draggedObject.getWidth() / 2,
							this.draggedObject.getPosition().y
									- this.draggedObject.getHeight() / 2);

					this.gameObjects.add(this.draggedObject.copy());

					button.addListener(new PlacedObjectsInputListener(
							draggedObject, button, this));
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

		if (pathway != null && pathway.getDistanceMap() != null) {
			// Renders the distance map
			// See LevelPath.createBitmap()
			Gdx.gl.glEnable(GL10.GL_BLEND);
			Gdx.gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
			Gdx.gl10.glPointSize(1);
			shapeRenderer.begin(ShapeType.Point);

			for (int row = 0; row < Constants.WORLD_HEIGHT; ++row) {
				for (int column = 0; column < Constants.WORLD_WIDTH; ++column) {
					float distance = pathway.getDistanceMap().At(column, row);

					if (distance < difficulty.getMaxDistanceFromSurface()) {
						shapeRenderer.setColor(196.f / 255, 154.f / 255,
								108.f / 255, 1);
						shapeRenderer.point(column, row, 0);
					} else if (distance < difficulty
							.getMaxDistanceFromSurface()) {
						// cause this is where the transparency begin
						distance -= Constants.misc.MAX_SURFACE_DISTANCE_FROM_PATHWAY_DEFAULT;
						float alpha = (difficulty.getMaxDistanceFromSurface() - distance)
								/ difficulty.getMaxDistanceFromSurface();
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
			shapeRenderer.setColor(1f, 0f, 0f, 1);
			shapeRenderer.point(point.x, point.y, 0);
		}
		shapeRenderer.end();

		stage.draw(); // and also display it :)

		batch.begin();

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

	public void createGenerateButton() {
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
				generateStartAndFinish();
			}
		});
	}

	public void createRestartButton() {
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

	public void createSaveButton() {
		buttonSave = new TextButton("Save", this.textButtonStyle);
		buttonSave.setPosition(Constants.WORLD_WIDTH, buttonRestart.getY()
				+ buttonRestart.getHeight());
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

					}

				}
			}
		});
	}

	public void createLoadButton() {
		buttonLoad = new TextButton("Load", this.textButtonStyle);
		buttonLoad.setPosition(
				Constants.WORLD_WIDTH + 20 + buttonSave.getWidth(),
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
						LoadLevel(new FileHandle(fileToBeLoad));
					} catch (Exception e) {
						JOptionPane.showMessageDialog(null,
								"Unable to load the file", "Warning: ",
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
		for (int i = 1; i <= Constants.misc.LEVEL_BACKGROUND_TEXTURE_TYPES_COUNT; i++) {
			Button button = new EditorBackgroundButton(
					game.assets.getGraphics(LevelBackground.GetTextureName(i)),
					i, this);
			button.setPosition(Constants.WORLD_WIDTH + 5 + widthOffset,
					Constants.WORLD_HEIGHT - heightOffset - height);
			button.addListener(new MyInputListenerForBackground(i, this));
			stage.addActor(button);

			widthOffset += width;
			if (widthOffset > EditorConstants.CONTROL_PANEL_WIDTH - width) {
				widthOffset = 0;
				heightOffset += height;
			}
		}
	}

	public enum TypeOfGameObjectButton {
		HOLE, MUD, STONE, TREE, BOOSTER
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
			createGameObjectButtons(trd, TypeOfGameObjectButton.HOLE, i,
					offsetOnScreen, maxValue);
		}

		// Muds
		for (int i = 1; i <= Constants.gameObjects.MUD_TYPES_COUNT; i++) {
			trd = new TextureRegionDrawable(game.assets.getGraphics(Mud
					.GetTextureName(i)));
			createGameObjectButtons(trd, TypeOfGameObjectButton.MUD, i,
					offsetOnScreen, maxValue);
		}

		// Stones
		for (int i = 1; i <= Constants.gameObjects.STONE_TYPES_COUNT; i++) {
			trd = new TextureRegionDrawable(game.assets.getGraphics(Stone
					.GetTextureName(i)));
			createGameObjectButtons(trd, TypeOfGameObjectButton.STONE, i,
					offsetOnScreen, maxValue);
		}

		// Trees
		for (int i = 1; i <= Constants.gameObjects.TREE_TYPES_COUNT; i++) {
			trd = new TextureRegionDrawable(game.assets.getGraphics(Tree
					.GetTextureName(i)));
			createGameObjectButtons(trd, TypeOfGameObjectButton.TREE, i,
					offsetOnScreen, maxValue);
		}

		// Boosts
		for (int i = 1; i <= Constants.gameObjects.BOOSTER_TYPES_COUNT; i++) {
			trd = new TextureRegionDrawable(game.assets.getGraphics(Booster
					.GetTextureName(i)));
			createGameObjectButtons(trd, TypeOfGameObjectButton.BOOSTER, i,
					offsetOnScreen, maxValue);
		}
	}

	private void createGameObjectButtons(TextureRegionDrawable trd,
			TypeOfGameObjectButton typeOfClass, int type,
			Vector2i offsetOnScreen, Vector2i maxValue) {

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

		button.setPosition(Constants.WORLD_WIDTH + 5 + offsetOnScreen.x,
				Constants.WORLD_HEIGHT - HEIGHT_OFFSET - offsetOnScreen.y);
		button.addListener(new MyInputListenerForGameObjects(typeOfClass, type,
				this));
		stage.addActor(button);

		offsetOnScreen.x += objectWidth;
	}

	public void createDifficultyButtons() {
		int BUTTONS_OFFSET = 10;
		Button buttonDif1 = new TextButton("1", this.textButtonStyle);
		buttonDif1.setPosition(Constants.WORLD_WIDTH + BUTTONS_OFFSET,
				buttonSave.getY() + buttonSave.getHeight() + BUTTONS_OFFSET);
		stage.addActor(buttonDif1);

		// Creates listener
		buttonDif1.addListener(new MyInputListener(this) {
			public void touchUp(InputEvent event, float x, float y,
					int pointer, int button) {
				difficulty = Difficulty.Kiddie;
			}
		});
		Button buttonDif2 = new TextButton("2", this.textButtonStyle);
		buttonDif2.setPosition(buttonDif1.getX() + buttonDif1.getWidth()
				+ BUTTONS_OFFSET, buttonSave.getY() + buttonSave.getHeight()
				+ BUTTONS_OFFSET);
		stage.addActor(buttonDif2);

		// Creates listener
		buttonDif2.addListener(new MyInputListener(this) {
			public void touchUp(InputEvent event, float x, float y,
					int pointer, int button) {
				difficulty = Difficulty.Beginner;
			}
		});
		Button buttonDif3 = new TextButton("3", this.textButtonStyle);
		buttonDif3.setPosition(buttonDif2.getX() + buttonDif2.getWidth()
				+ BUTTONS_OFFSET, buttonSave.getY() + buttonSave.getHeight()
				+ BUTTONS_OFFSET);
		stage.addActor(buttonDif3);

		// Creates listener
		buttonDif3.addListener(new MyInputListener(this) {
			public void touchUp(InputEvent event, float x, float y,
					int pointer, int button) {
				difficulty = Difficulty.Normal;
			}
		});

		Button buttonDif4 = new TextButton("4", this.textButtonStyle);
		buttonDif4.setPosition(buttonDif3.getX() + buttonDif3.getWidth()
				+ BUTTONS_OFFSET, buttonSave.getY() + buttonSave.getHeight()
				+ BUTTONS_OFFSET);
		stage.addActor(buttonDif4);

		// Creates listener
		buttonDif4.addListener(new MyInputListener(this) {
			public void touchUp(InputEvent event, float x, float y,
					int pointer, int button) {
				difficulty = Difficulty.Hard;
			}
		});

		Button buttonDif5 = new TextButton("5", this.textButtonStyle);
		buttonDif5.setPosition(buttonDif4.getX() + buttonDif4.getWidth()
				+ BUTTONS_OFFSET, buttonSave.getY() + buttonSave.getHeight()
				+ BUTTONS_OFFSET);
		stage.addActor(buttonDif5);

		// Creates listener
		buttonDif5.addListener(new MyInputListener(this) {
			public void touchUp(InputEvent event, float x, float y,
					int pointer, int button) {
				difficulty = Difficulty.Extreme;
			}
		});
	}
	
	private void createTextField() {
		timeTextField = new TextField(new DecimalFormat().format(timeLimit), this.textFieldStyle);
		timeTextField.setPosition(buttonRestart.getX() + buttonRestart.getWidth() + 20, buttonRestart.getHeight());
		timeTextField.setTextFieldListener(new MyTextNumberListener(this));
		timeTextField.addListener(new InputListener() {
			@Override
			public boolean keyTyped(InputEvent event, char character) {
				if(event.getCharacter() == '+' || event.getKeyCode() == 19 || event.getKeyCode() == 22){
					timeLimit++;
					if(timeLimit > 99)
						timeLimit = 99;
					timeTextField.setText(new DecimalFormat().format(timeLimit));
				}
				else if(event.getCharacter() == '-' || event.getKeyCode() == 20 || event.getKeyCode() == 21){
					timeLimit--;
					if(timeLimit < 0)
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
