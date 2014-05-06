package cz.mff.cuni.autickax.pathway;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.PixmapIO;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import cz.mff.cuni.autickax.Difficulty;
import cz.mff.cuni.autickax.constants.Constants;

/**
 * @author Shabby Class for representing 2-dimensional field of the closest
 *         distances of a curve.
 */

public class DistanceMap {

	private float[][] map;
	private static float sqrtOfTwo = (float) Math.sqrt(2);
	private int height;
	private int width;

	private float progress = 0;

	/**
	 * Get a distance according to xy pixel. 0 means that the curve was hit. >0
	 * means that we are "value" pixels far from the curve.
	 * 
	 * @param x
	 * @param y
	 * @return
	 */
	public float At(int x, int y) {
		return map[x][y];
	}

	public float At(Vector2 position) {
		return map[(int) position.x][(int) position.y];
	}

	public DistanceMap(int height, int width) {
		this.height = height;
		this.width = width;
		this.map = new float[width][height];
		ClearMap();
	}

	private void ClearMap() {
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				map[x][y] = Float.MAX_VALUE;
			}
		}
	}

	public void deleteMap() {
		this.map = null;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	private boolean isInWorld(int x, int y) {
		return (x >= 0) && (x < width) && (y >= 0) && (y < height);
	}

	// Creates the main distances structure for given control points.
	public void CreateDistances(ArrayList<Vector2> controlPoints, Pathway.PathwayType pathwayType,
			Splines.TypeOfInterpolation typeOfInterpolation) {
		if (controlPoints.size() < 4)
			return;

		ClearMap();

		progress = 0;

		// Set line position to zero
		int totalLines = controlPoints.size() * Constants.misc.LINE_SEGMENTATION;
		Vector2 point;
		for (float i = 0; i <= totalLines; i++) {
			point = Splines.GetPoint(controlPoints, i / totalLines, typeOfInterpolation,
					pathwayType);
			if (point.x >= 0 && point.y > 0 && point.x <= width && point.y <= height)
				this.map[(int) point.x][(int) point.y] = 0.f;
		}

		progress = 10;

		// Start and finish circle positions to zero
		int CIRCLE_RADIUS = Constants.misc.PATHWAY_START_AND_FINISH_CIRCLE_RADIUS;
		int CIRCLE_RADIUS_SQR = CIRCLE_RADIUS * CIRCLE_RADIUS;
		Vector2 startF = Splines.GetPoint(controlPoints, 0, typeOfInterpolation, pathwayType);
		Vector2 finishF = Splines.GetPoint(controlPoints, 1, typeOfInterpolation, pathwayType);
		Vector2i start = new Vector2i((int) startF.x, (int) startF.y);
		Vector2i finish = new Vector2i((int) finishF.x, (int) finishF.y);
		float xSqr, ySqr;
		for (int x = -CIRCLE_RADIUS; x < CIRCLE_RADIUS; x++) {
			for (int y = -CIRCLE_RADIUS; y < CIRCLE_RADIUS; y++) {
				xSqr = x * x;
				ySqr = y * y;

				if (xSqr + ySqr < CIRCLE_RADIUS_SQR) {
					if (isInWorld(start.x + x, start.y + y)) {
						map[start.x + x][start.y + y] = 0.f;
					}

					if (isInWorld(finish.x + x, finish.y + y)) {
						map[finish.x + x][finish.y + y] = 0.f;
					}
				}
			}
		}

		progress = 30;

		// Prepares for BFS by adding line positions
		Queue<Vector2i> nodesToSearch = new LinkedList<Vector2i>();
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				if (map[x][y] == 0) {
					nodesToSearch.add(new Vector2i(x, y));
				}
			}
		}

		progress = 35;

		float estimatedSurface = 1;// 3.f / 4;
		float offset = progress;
		float range = 100 - offset;
		float maxNodesCount = width * height / estimatedSurface;
		float nodeNumber = 0;
		// Counting distances with BFS
		Vector2i currentPoint = nodesToSearch.poll();
		while (currentPoint != null) {
			progress = offset + (nodeNumber / maxNodesCount) * range;
			for (int x = -1; x <= 1; x++) {
				for (int y = -1; y <= 1; y++) {
					if ((x == 0 && y == 0)
							|| !isInWorld(currentPoint.x + x, currentPoint.y + y)
							|| map[currentPoint.x][currentPoint.y] > Constants.misc.MAX_DISTANCE_FROM_PATHWAY) {
						continue;
					}

					// /System.out.println(map[currentPoint.x][currentPoint.y] +
					// " " + map[currentPoint.x + x][currentPoint.y + y]);
					if (Math.abs(x) == Math.abs(y)) {
						if (map[currentPoint.x + x][currentPoint.y + y] > map[currentPoint.x][currentPoint.y]
								+ sqrtOfTwo) {
							// diagonal distance
							map[currentPoint.x + x][currentPoint.y + y] = map[currentPoint.x][currentPoint.y]
									+ sqrtOfTwo;
							nodesToSearch.add(new Vector2i(currentPoint.x + x, currentPoint.y + y));
						}
					} else {
						// horizontal or vertical distance
						if (map[currentPoint.x + x][currentPoint.y + y] > map[currentPoint.x][currentPoint.y] + 1) {
							map[currentPoint.x + x][currentPoint.y + y] = map[currentPoint.x][currentPoint.y] + 1;
							nodesToSearch.add(new Vector2i(currentPoint.x + x, currentPoint.y + y));
						}
					}
				}
			}
			currentPoint = nodesToSearch.poll();
			nodeNumber++;
		}
		progress = 100;
	}

	/**
	 * Generates texture. Saves the copy of it to disk for loading it again
	 * after application resume(), because of loosing GL context.
	 * 
	 * @param difficulty
	 * @return
	 */
	public TextureRegion generateTexture(Difficulty difficulty) {
		Pixmap pixmap = new Pixmap(1024, 512, Pixmap.Format.RGBA8888);
		Pixmap.setBlending(Pixmap.Blending.None);

		for (int row = 0; row < Constants.WORLD_HEIGHT; ++row) {
			for (int column = 0; column < Constants.WORLD_WIDTH; ++column) {

				// distance map is flipped to the bitmap
				float distance = this.At(column, Constants.WORLD_HEIGHT - row - 1);

				if (distance < difficulty.getMaxDistanceFromSurface()) {
					pixmap.setColor(Constants.misc.PATHWAY_COLOR);
					pixmap.drawPixel(column, row);
				} else if (distance < difficulty.getMaxDistanceFromSurface()
						+ Constants.misc.PATHWAY_BORDER_BLEND_DISTANCE) {
					float alpha = 1 - (distance - difficulty.getMaxDistanceFromSurface())
							/ Constants.misc.PATHWAY_BORDER_BLEND_DISTANCE;
					Color color = new Color(Constants.misc.PATHWAY_COLOR.r,
							Constants.misc.PATHWAY_COLOR.g, Constants.misc.PATHWAY_COLOR.b, alpha);

					pixmap.setColor(color);
					pixmap.drawPixel(column, row);
				}
			}
		}

		// Save pixmap to temporary file
		FileHandle textureFile = null;
		if (Gdx.files.isLocalStorageAvailable()) {
			textureFile = Gdx.files.local(Constants.misc.TEMPORARY_PATHWAY_TEXTURE_STORAGE_NAME
					+ ".cim");
			// System.out.println("Texture of pathway was saved into local memory.");
		} else {
			textureFile = Gdx.files.internal(Constants.misc.TEMPORARY_PATHWAY_TEXTURE_STORAGE_NAME
					+ ".cim");
			// System.out.println("Texture of pathway was saved into internal memory.");
		}
		PixmapIO.writeCIM(textureFile, pixmap);

		// Texture from pixmap
		Texture texture = new Texture(pixmap);
		pixmap.dispose();
		return new TextureRegion(texture, Constants.WORLD_WIDTH, Constants.WORLD_HEIGHT);
	}

	/*
	 * Returns progress of creating distance map as a number from 0 to 1  
	 */
	public float getProgress() {
		return this.progress / 100;
	}

}