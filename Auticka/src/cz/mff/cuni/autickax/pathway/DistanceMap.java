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

import cz.mff.cuni.autickax.Debug;
import cz.mff.cuni.autickax.Difficulty;
import cz.mff.cuni.autickax.constants.Constants;

/**
 * Class for representing 2-dimensional field of the closest distances of a
 * curve.
 */

public class DistanceMap {

	private int[][] map;
	private static int sqrtOfTwo = 7;
	private static int one = 5;
	private int height;
	private int width;

	// Maximal count of nodes used in create distancemap
	private float nodesCount = 1;

	public float progress = 0;

	private static final int distanceModifier = one;
	private static final int maxDistanceFromPathway = Constants.misc.MAX_DISTANCE_FROM_PATHWAY
			* distanceModifier;
	private static final int circleRadius = Constants.misc.PATHWAY_START_AND_FINISH_CIRCLE_RADIUS;

	/**
	 * Get a distance according to xy pixel. 0 means that the curve was hit. >0
	 * means that we are "value" pixels far from the curve.
	 * 
	 * @param x
	 * @param y
	 * @return
	 */
	public float At(int x, int y) {
		return (float) map[x][y] / distanceModifier;
	}

	public float At(Vector2 position) {
		return (float) map[(int) position.x][(int) position.y] / distanceModifier;
	}

	public DistanceMap(int height, int width) {
		this(height, width, 1);
	}

	public DistanceMap(int height, int width, float nodesCount) {
		this.height = height;
		this.width = width;
		this.map = new int[width][height];
		this.nodesCount = nodesCount;
		ClearMap();
	}

	public float getNodesCount() {
		return this.nodesCount;
	}

	private void ClearMap() {
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				map[x][y] = Integer.MAX_VALUE;
			}
		}
	}

	public void deleteMap() {
		this.map = null;
	}

	private boolean isInWorld(int x, int y) {
		return (x >= 0) && (x < width) && (y >= 0) && (y < height);
	}

	// Creates the main distances structure for given control points.
	public void CreateDistances(ArrayList<Vector2> controlPoints, Pathway.PathwayType pathwayType,
			Splines.TypeOfInterpolation typeOfInterpolation) {
		if (controlPoints.size() < 4)
			return;

		long time = System.currentTimeMillis();
		long lastTime = time;
		progress = 0;
		ClearMap();		
		progress = 5;

		time = System.currentTimeMillis();
		Debug.Log("Clearing map: " + Long.toString(time - lastTime));
		lastTime = time;

		// Set line position to zero
		int totalLines = controlPoints.size() * Constants.misc.LINE_SEGMENTATION;
		Vector2 point;
		for (float i = 0; i <= totalLines; i++) {
			float part = i / totalLines;
			point = Splines.GetPoint(controlPoints, part, typeOfInterpolation,
					pathwayType);
			progress = 5 + part * 10;
			if (point.x >= 0 && point.y > 0 && point.x <= width && point.y <= height)
				this.map[(int) point.x][(int) point.y] = 0;
		}

		time = System.currentTimeMillis();
		Debug.Log("Line to zero: " + Long.toString(time - lastTime));
		lastTime = time;
		progress = 15;

		// Start and finish circle positions to zero
		int CIRCLE_RADIUS_SQR = circleRadius * circleRadius;
		int minusCircleRadius = -circleRadius;
		Vector2 startF = Splines.GetPoint(controlPoints, 0, typeOfInterpolation, pathwayType);
		Vector2 finishF = Splines.GetPoint(controlPoints, 1, typeOfInterpolation, pathwayType);
		Vector2i start = new Vector2i((int) startF.x, (int) startF.y);
		Vector2i finish = new Vector2i((int) finishF.x, (int) finishF.y);
		int xSqr, ySqr;
		for (int x = minusCircleRadius; x < circleRadius; x++) {
			for (int y = minusCircleRadius; y < circleRadius; y++) {
				xSqr = x * x;
				ySqr = y * y;

				if (xSqr + ySqr < CIRCLE_RADIUS_SQR) {
					if (isInWorld(start.x + x, start.y + y)) {
						map[start.x + x][start.y + y] = 0;
					}

					if (isInWorld(finish.x + x, finish.y + y)) {
						map[finish.x + x][finish.y + y] = 0;
					}
				}
			}
		}

		time = System.currentTimeMillis();
		Debug.Log("Start and finish: " + Long.toString(time - lastTime));
		lastTime = time;
		progress = 20;

		float surface = width * height;
		float foo = 0;
		// Prepares for BFS by adding line positions
		Queue<Vector2i> nodesToSearch = new LinkedList<Vector2i>();
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				progress = 20 + foo / surface * 10;
				foo++;
				if (map[x][y] == 0) {
					nodesToSearch.add(new Vector2i(x, y));
				}
			}
		}

		time = System.currentTimeMillis();
		Debug.Log("Preparing for BFS: " + Long.toString(time - lastTime));
		lastTime = time;
		progress = 30;

		// Counting distances with BFS
		float offset = progress;
		float range = 100 - offset;
		float nodeNumber = 0;
		Vector2i currentPoint = nodesToSearch.poll();
		progress = offset + nodeNumber / nodesCount * range;
		while (currentPoint != null) {
			// progress = offset + (nodeNumber / maxNodesCount) * range;
			for (int x = -1; x <= 1; x++) {
				for (int y = -1; y <= 1; y++) {
					if ((x == 0 && y == 0) || !isInWorld(currentPoint.x + x, currentPoint.y + y)
							|| map[currentPoint.x][currentPoint.y] > maxDistanceFromPathway) {
						continue;
					}

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
						if (map[currentPoint.x + x][currentPoint.y + y] > map[currentPoint.x][currentPoint.y]
								+ one) {
							map[currentPoint.x + x][currentPoint.y + y] = map[currentPoint.x][currentPoint.y]
									+ one;
							nodesToSearch.add(new Vector2i(currentPoint.x + x, currentPoint.y + y));
						}
					}
				}
			}

			progress = offset + nodeNumber / nodesCount * range;
			nodeNumber++;
			currentPoint = nodesToSearch.poll();
		}

		nodesCount = nodeNumber;

		time = System.currentTimeMillis();
		Debug.Log("BFS: " + Long.toString(time - lastTime));
	}

	/**
	 * Generates texture. Saves the copy of it to disk for loading it again
	 * after application resume(), because of loosing GL context.
	 * 
	 * @param difficulty
	 * @return
	 */
	public TextureRegion generateTexture(Difficulty difficulty) {
		long timeAnchor = System.currentTimeMillis();

		Pixmap pixmap = new Pixmap(1024, 512, Pixmap.Format.RGBA8888);
		Pixmap.setBlending(Pixmap.Blending.None);
		int maxDistanceFromSurface = difficulty.getMaxDistanceFromSurface() * distanceModifier;
		int borderBlendDistance = Constants.misc.PATHWAY_BORDER_BLEND_DISTANCE * distanceModifier;

		for (int row = 0; row < Constants.WORLD_HEIGHT; ++row) {
			for (int column = 0; column < Constants.WORLD_WIDTH; ++column) { //
				int distance = map[column][Constants.WORLD_HEIGHT - row - 1];

				if (distance < maxDistanceFromSurface) {
					pixmap.setColor(Constants.misc.PATHWAY_COLOR);
					pixmap.drawPixel(column, row);
				} else if (distance < maxDistanceFromSurface + borderBlendDistance) {
					float alpha = 1.f - ((float) distance - maxDistanceFromSurface)
							/ borderBlendDistance;
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
			Debug.Log("Texture of pathway was saved into local memory.");
		} else {
			textureFile = Gdx.files.internal(Constants.misc.TEMPORARY_PATHWAY_TEXTURE_STORAGE_NAME
					+ ".cim");
			Debug.Log("Texture of pathway was saved into internal memory.");
		}
		PixmapIO.writeCIM(textureFile, pixmap);

		// Texture from pixmap
		Texture texture = new Texture(pixmap);
		pixmap.dispose();

		Debug.Log("Texture created: " + Long.toString(System.currentTimeMillis() - timeAnchor));
		return new TextureRegion(texture, Constants.WORLD_WIDTH, Constants.WORLD_HEIGHT);
	}

	/*
	 * Returns progress of creating distance map as a number from 0 to 1
	 */
	public float getProgress() {
		return this.progress / 100;
	}
}