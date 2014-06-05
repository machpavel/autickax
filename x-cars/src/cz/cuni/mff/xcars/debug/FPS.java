package cz.cuni.mff.xcars.debug;

import java.text.DecimalFormat;

import com.badlogic.gdx.graphics.g2d.Batch;

import cz.cuni.mff.xcars.screenObjects.ScreenAdaptiveLabel;

public class FPS {
	private static final float xLabelPosition = 0;
	private static final float yLabelPosition = 450;

	private float framesCount = 0;
	private float elapsedTime = 0;
	private ScreenAdaptiveLabel label;
	private static DecimalFormat decimalFormat = new DecimalFormat("#.#");

	public void render(Batch batch, float delta) {
		framesCount++;
		elapsedTime += delta;

		if (elapsedTime >= 1) {
			framesCount /= elapsedTime;

			// This late initialization is due to not loaded assets in
			// loading screen.
			if (label == null) {
				try {
					label = ScreenAdaptiveLabel.getMenuLabel("Fps:");
					label.setPosition(xLabelPosition, yLabelPosition);
				} catch (Exception e) {
					label = null;
					return;
				}
			}

			label.setText("Fps: " + decimalFormat.format(framesCount));
			elapsedTime = 0;
			framesCount = 0;
		}

		if (label != null)
			label.draw(batch, 1);
	}
}
