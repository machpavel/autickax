package cz.mff.cuni.autickax.pathway;

import java.util.ArrayList;
import java.util.LinkedList;



import java.util.Queue;
import com.badlogic.gdx.math.Vector2;

 
/**
 * @author Shabby
 * Class for representind 2-dimensional field of the closest distances of a curve.
 */

public class DistanceMap {
	public static float MAXIMUM_DISTANCE = 30;
	private static Splines.TypeOfInterpolation typeOfInterpolation = Splines.TypeOfInterpolation.CUBIC_SPLINE;
	private static int LINE_SEGMENTATION = 100; //amount of parts used between two points
	
	private float [][] map;	
	private static float sqrtOfTwo = (float)Math.sqrt(2);	
	private int height;
	private int width;
	
	
	// Get a distance according to xy pixel. 0 means that the curve was hit. >0 means that we are "value" pixels far from the curve.
	// TODO: change to relative coordinates
	public float At(int x, int y){
		return map[x][y];
	}
		
	public DistanceMap(int height, int width) {
		this.height = height;
		this.width = width;
		this.map = new float[width][height];	
		ClearMap();
	}
	

	private void ClearMap(){		
			for (int x = 0; x < width; x++) {
				for (int y = 0; y < height; y++) {				
					map[x][y] = Float.MAX_VALUE;				
				}
			}
	}

	// Creates the main distances structure for given control points.  
	public void CreateDistances(ArrayList<Vector2> controlPoints){
		if(controlPoints.size() < 4) return;
		
		ClearMap();

		
		//Set line position to zero
		int totalLines = controlPoints.size() * LINE_SEGMENTATION;				
		Vector2 point;				
		for (float i = 0; i <= totalLines; i++) {
			point = Splines.GetPoint(controlPoints, i / totalLines, getTypeOfInterpolation());
			if(point.x >=0 && point.y > 0 && point.x <= width && point.y <= height)
				this.map[(int)point.x][(int)point.y] = 0f;					
		}
		
		//Prepares for BFS by adding line positions
		Queue<Vector2i> nodesToSearch = new LinkedList<Vector2i>();
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				if (map[x][y] == 0) {
					nodesToSearch.add(new Vector2i(x,y));
				}
			}
		}
		
		//Counting distances with BFS
		Vector2i currentPoint = nodesToSearch.poll();
		while (currentPoint!= null) {			
			for (int x = -1; x <= 1; x++) {
				for (int y = -1; y <= 1; y++) {
					if((x==0 && y==0)||
					(currentPoint.x + x < 0)||
					(currentPoint.x + x >= width)||
					(currentPoint.y + y < 0)||
					(currentPoint.y + y >= height)||
					map[currentPoint.x][currentPoint.y] >= MAXIMUM_DISTANCE)
						continue;
					
					
					///System.out.println(map[currentPoint.x][currentPoint.y] + " " + map[currentPoint.x + x][currentPoint.y + y]);
					if(Math.abs(x) == Math.abs(y)){
						if(map[currentPoint.x + x][currentPoint.y + y] > map[currentPoint.x][currentPoint.y] + sqrtOfTwo){
							//diagonal distance
							map[currentPoint.x + x][currentPoint.y + y] = map[currentPoint.x][currentPoint.y] + sqrtOfTwo;
							nodesToSearch.add(new Vector2i(currentPoint.x + x, currentPoint.y + y));
						}
					}
					else{
						//horizontal or vertical distance
						if(map[currentPoint.x + x][currentPoint.y + y] > map[currentPoint.x][currentPoint.y] + 1){
							map[currentPoint.x + x][currentPoint.y + y] = map[currentPoint.x][currentPoint.y] + 1;
							nodesToSearch.add(new Vector2i(currentPoint.x + x, currentPoint.y + y));
						}
					}															
				}
			}
			currentPoint = nodesToSearch.poll();			
		}									
	}
		
	 public static Splines.TypeOfInterpolation getTypeOfInterpolation() {
		return typeOfInterpolation;
	}

	public static void setTypeOfInterpolation(Splines.TypeOfInterpolation typeOfInterpolation) {
		DistanceMap.typeOfInterpolation = typeOfInterpolation;
	}

	class Vector2i{
		public int x,y;
		public Vector2i(int x, int y){
			this.x = x;
			this.y = y;
		}
	}

}
