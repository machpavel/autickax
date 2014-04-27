package cz.cuni.mff.autickax.colorDrawable;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.utils.BaseDrawable;

public class ColorDrawable extends BaseDrawable {

	// private final Color color;
	private final Texture texture;

	public ColorDrawable(Color color) {
		// this.color = color;
		Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
		pixmap.setColor(color);
		pixmap.drawPixel(1, 1);
		texture = new Texture(pixmap);
		System.out.println(pixmap.getPixel(1, 1));
	}

	@Override
	public void draw(SpriteBatch batch, float x, float y, float width, float height) {
		batch.draw(texture, x, y, width, height);
		// ShapeRenderer renderer = new ShapeRenderer();
		// renderer.setColor(this.color);
		// renderer.begin(ShapeType.Filled);
		// renderer.rect(x, y, width, height);
		// renderer.end();
	}

}
