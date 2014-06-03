package cz.cuni.mff.xcars;

import java.text.DecimalFormat;
import java.util.LinkedList;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import cz.cuni.mff.xcars.screenObjects.ScreenAdaptiveLabel;

public final class Debug {
	public static boolean DEBUG = true;

	private static Log messageLabel;
	private static Log valueLabel;
	private static LinkedList<Log> logger = new LinkedList<Log>();
	private static DecimalFormat decimalFormat = new DecimalFormat("#.##");

	private static float ROW_HEIGHT = 30;
	private static float X0 = 0, X1 = 350, X2 = 700;

	private static float maxLogTime = 7000;

	public static void SetMessage(String message) {
		if (DEBUG) {
			ScreenAdaptiveLabel label = ScreenAdaptiveLabel.getMenuLabel(message);
			label.setPosition(X1, 0);
			messageLabel = new Log(label, System.currentTimeMillis());
		}
	}

	public static void Log(String message) {
		if (DEBUG) {
			for (Log log : logger) {
				log.label.setPosition(log.label.getX(), log.label.getY() + ROW_HEIGHT);
			}

			ScreenAdaptiveLabel label = ScreenAdaptiveLabel.getMenuLabel(message);
			label.setPosition(X0, 0);
			logger.addLast(new Log(ScreenAdaptiveLabel.getMenuLabel(message), System
					.currentTimeMillis()));
		}
	}

	public static void SetValue(String value) {
		if (DEBUG) {
			ScreenAdaptiveLabel label = ScreenAdaptiveLabel.getMenuLabel(value);
			label.setPosition(X2, 0);
			valueLabel = new Log(label, System.currentTimeMillis());
		}
	}

	public static void SetValue(int value) {
		if (DEBUG) {
			SetValue(decimalFormat.format(value));
		}
	}

	public static void SetValue(float value) {
		if (DEBUG) {
			SetValue(decimalFormat.format(value));
		}
	}

	public static void draw(SpriteBatch batch) {
		if (DEBUG) {
			long currentTime = System.currentTimeMillis();

			// Draws the message label
			if (messageLabel != null) {
				float logLifeTime = (float) (currentTime - messageLabel.timeStamp);
				if (logLifeTime <= maxLogTime) {
					float alpha = 1 - logLifeTime / maxLogTime;
					if (alpha < 0) {
						alpha = 0;
					}
					messageLabel.label.draw(batch, alpha);
				} else {
					messageLabel = null;
				}
			}

			// Draws the value label
			if (valueLabel != null) {
				float logLifeTime = (float) (currentTime - valueLabel.timeStamp);
				if (logLifeTime <= maxLogTime) {
					float alpha = 1 - logLifeTime / maxLogTime;
					if (alpha < 0) {
						alpha = 0;
					}
					valueLabel.label.draw(batch, alpha);
				} else {
					valueLabel = null;
				}
			}

			// Draws the logger label
			Log firstLog = logger.peekFirst();
			if (firstLog != null) {
				float logLifeTime = (float) (currentTime - firstLog.timeStamp);
				if (logLifeTime > maxLogTime)
					logger.pollFirst();

			}
			for (Log log : logger) {
				float logLifeTime = (float) (currentTime - log.timeStamp);
				float alpha = 1 - logLifeTime / maxLogTime;
				if (alpha < 0) {
					alpha = 0;
				}
				log.label.draw(batch, alpha);
			}
		}
	}

	private static class Log {
		public ScreenAdaptiveLabel label;
		public long timeStamp;

		Log(ScreenAdaptiveLabel label, long timeStamp) {
			this.label = label;
			this.timeStamp = timeStamp;
		}
	}
}
