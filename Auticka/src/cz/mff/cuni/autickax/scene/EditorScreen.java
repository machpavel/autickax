package cz.mff.cuni.autickax.scene;

import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.XmlWriter;

import cz.mff.cuni.autickax.entities.GameObject;
import cz.mff.cuni.autickax.pathway.DistanceMap;
import cz.mff.cuni.autickax.pathway.Pathway;

public class EditorScreen extends GameScreen {

	Button buttonGeneratePoints;
	Button buttonRestart;

	public EditorScreen() {
		super();
		pathway = new Pathway();
		
		game.assets.music.stop();
		

		createGenerateButton();
		createRestartButton();

	}

	
	private void update(float delta) {
		// Checks if user created a new point
		if (Gdx.input.justTouched()) {
			pathway.getControlPoints().add(
					new Vector2(Gdx.input.getX(), stageHeight
							- Gdx.input.getY()));			
			game.assets.getSound("hit").play();
			generateXml("level.xml");
		}
	}

	@Override
	public void render(float delta) {		
		stage.act(delta); // don't forget to advance the stage ( input + actions					
		if(buttonGeneratePoints.isPressed() | buttonRestart.isPressed())
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
				float colorIntensity = 1 - (pathway.getDistanceMap().At(x, y) / DistanceMap.MAXIMUM_DISTANCE);
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
	}

	public void generateXml(String fileName) {
		try {
			FileWriter fileWriter = new FileWriter("../Assets/levels/"
					+ fileName);
			StringWriter stringWriter = new StringWriter();
			XmlWriter xml = new XmlWriter(stringWriter);

			xml.element("AutickaX");

			xml.element("pathway");
			xml.element("controlPoints");
			for (Vector2 point : pathway.getControlPoints()) {
				xml.element("point");
				xml.attribute("X", point.x);
				xml.attribute("Y", point.y);
				xml.pop();
			}
			xml.pop();
			xml.pop();

			gameObjects.add(new GameObject(12.f, 13.f) {
				@Override
				public void update(float delta) {
				}

				@Override
				public String getName() {
					return "object";
				}
			});

			xml.element("entities");
			for (GameObject gameObject : gameObjects) {
				gameObject.toXml(xml);
			}
			xml.pop();

			car.toXml(xml);

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

	public void createGenerateButton(){
				buttonGeneratePoints = new TextButton("Generate", new TextButtonStyle(
						new TextureRegionDrawable(game.assets.getGraphics("greenpixel")),
						new TextureRegionDrawable(game.assets.getGraphics("greenpixel")),
						new TextureRegionDrawable(game.assets.getGraphics("greenpixel")),
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
	
	public void createRestartButton(){
		buttonRestart = new TextButton("Restart", new TextButtonStyle(
				new TextureRegionDrawable(game.assets.getGraphics("greenpixel")),
				new TextureRegionDrawable(game.assets.getGraphics("greenpixel")),
				new TextureRegionDrawable(game.assets.getGraphics("greenpixel")),
				game.assets.getFont()));

		buttonRestart.setPosition(0, buttonGeneratePoints.getY() + buttonGeneratePoints.getHeight());
		stage.addActor(buttonRestart);

		// User Input for Play Button
		buttonRestart.addListener(new InputListener() {
			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int button) {
				return true;
			}

			public void touchUp(InputEvent event, float x, float y,
					int pointer, int button) {
				pathway = new Pathway();
			}
		});
 }
}
