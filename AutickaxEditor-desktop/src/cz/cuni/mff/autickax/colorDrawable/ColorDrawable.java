package cz.cuni.mff.autickax.colorDrawable;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.scenes.scene2d.utils.BaseDrawable;

public class ColorDrawable extends BaseDrawable {
	
	private final Color color;
	
	public ColorDrawable(Color color) {
		this.color = color;
	}
	
	@Override
	public void draw(SpriteBatch batch, float x, float y, float width,
			float height) {
		
		ShapeRenderer renderer = new ShapeRenderer();
		renderer.setColor(this.color);
		renderer.begin(ShapeType.Filled);
		renderer.rect(0, 0, 1, 1);
		renderer.end();
		
		
		
		// TODO Auto-generated method stub
		//super.draw(batch, x, y, width, height);
	}

}
