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
/**
 * This is the screen where the game happens
 * @author Ondrej Paska
 */

public class Level1 extends BaseScreen {
	// Textures
	private TextureRegion skyTexture;

	// Rendering
	protected OrthographicCamera camera;
	protected SpriteBatch batch;

	// Entities
	private Car car;

	// Score
	private int score = 0;
	// Time
	private float timeElapsed = 0;
	
	// Cached font
	private BitmapFont font;
	
	public Level1() {
		super();

		camera = new OrthographicCamera();
		camera.setToOrtho(false, 800, 480);

		batch = new SpriteBatch();


		Assets assets = MyGdxGame.getInstance().assets;
		this.skyTexture = assets.getGraphics("sky");
		
		// Init entities
		this.car = new Car(stageWidth * 0.25f, 70, this);
		
		font = game.assets.getFont();
		
		// Start Music!
		game.assets.music.setLooping(true);
		game.assets.music.setVolume(0.3f);
		game.assets.music.play();
	}
	
	public void unproject(Vector3 vector) {
		this.camera.unproject(vector);
	}

	private void update(float delta) {
		
		timeElapsed += delta;
		
		this.car.update(delta);
	}

	@Override
	public void render(float delta) {
		
		update(delta); //Update our world
		
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT); // This cryptic line clears the screen

		camera.update();
		batch.setProjectionMatrix(camera.combined);  // see https://github.com/libgdx/libgdx/wiki/Orthographic-camera
		
		batch.begin();
		
		batch.disableBlending(); //performance boost
		
		//SKY
		batch.draw(skyTexture, 0, 0, stageWidth, stageHeight);

		batch.enableBlending(); //don't forget to enabled this for alpha channel

		this.car.draw(batch, delta);
		
		// Draw score
		font.draw(batch, "score: "+score, 10, (int)stageHeight-32);
		// Draw time
		font.draw(batch, "time: "+ ( (int) timeElapsed ), (int) stageWidth/2, (int)stageHeight-32);
		
		batch.end();
	}

}
