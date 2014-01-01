package cz.mff.cuni.autickax.scene;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;

import cz.mff.cuni.autickax.Assets;
import cz.mff.cuni.autickax.MyGdxGame;
import cz.mff.cuni.autickax.entities.Car;
import cz.mff.cuni.autickax.entities.GameObject;
import cz.mff.cuni.autickax.gamelogic.SubLevel;
import cz.mff.cuni.autickax.gamelogic.SubLevel1;
import cz.mff.cuni.autickax.gamelogic.SubLevel2;
/**
 * This is the screen where the game happens
 * @author Ondrej Paska
 */

public class GameScreen extends BaseScreen {
	// Textures
	private TextureRegion backgroundTexture;

	// Rendering
	protected OrthographicCamera camera;
	protected SpriteBatch batch;
	
	// Levels
	private SubLevel currentPhase;

	// Entities
	private GameObject[] gameObjects;
	
	// Cached font
	private BitmapFont font;
	public BitmapFont getFont() {
		// TODO: consider loading font elsewhere, once for the whole game
		return font;
	}
	
	public GameScreen() {
		super();

		camera = new OrthographicCamera();
		camera.setToOrtho(false, 800, 480);

		batch = new SpriteBatch();
		
		this.currentPhase = new SubLevel1(this);

		Assets assets = MyGdxGame.getInstance().assets;
		this.backgroundTexture = assets.getGraphics("sky");
		
		// dummy code ------------------------>
		
		this.gameObjects = new GameObject[1];
		this.gameObjects[0] = new Car(stageWidth * 0.25f, 70, this);
		
		// <------------------------ dummy code
		
		this.font = game.assets.getFont();
		
		// Start Music!
		game.assets.music.setLooping(true);
		game.assets.music.setVolume(0.3f);
		game.assets.music.play();
	}
	
	public void switchToPhase2() {
		this.currentPhase = new SubLevel2(this);
	}
	
	public void reset() {
		// TODO: reseting of all game objects
	}
	
	public void unproject(Vector3 vector) {
		this.camera.unproject(vector);
	}

	private void update(float delta) {		
		// TODO: Consider moving this into sublevels
		for (int i = 0; i < this.gameObjects.length; ++i) {
			this.gameObjects[i].update(delta);
		}
		
		this.currentPhase.update(delta);
	}

	@Override
	public void render(float delta) {
		
		update(delta); //Update our world
		
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT); // This cryptic line clears the screen

		camera.update();
		batch.setProjectionMatrix(camera.combined);  // see https://github.com/libgdx/libgdx/wiki/Orthographic-camera
		
		batch.begin();
		
		batch.disableBlending(); //performance boost
		
		// background
		batch.draw(backgroundTexture, 0, 0, stageWidth, stageHeight);

		batch.enableBlending(); //don't forget to enabled this for alpha channel

		// TODO: Consider moving this into sublevels
		for (int i = 0; i < this.gameObjects.length; ++i) {
			this.gameObjects[i].draw(this.batch, delta);
		}
		
		batch.end();
	}

}
