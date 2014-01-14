package cz.mff.cuni.autickax.pathway;


import java.util.ArrayList;
import java.util.LinkedList;



import java.util.Queue;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import cz.mff.cuni.autickax.Constants;
import cz.mff.cuni.autickax.Difficulty;

 
/**
 * @author Shabby
 * Class for representing 2-dimensional field of the closest distances of a curve.
 */

public class DistanceMap implements java.io.Serializable {
        private static final long serialVersionUID = 1L;
        
        private float [][] map;        
        private static float sqrtOfTwo = (float)Math.sqrt(2);        
        private int height;
        private int width;
        
        
         
        /**
         * Get a distance according to xy pixel. 0 means that the curve was hit. >0 means that we are "value" pixels far from the curve.
         * @param x
         * @param y
         * @return
         */
        public float At(int x, int y){
                return map[x][y];
        }
        
        public float At(Vector2 position){
                return map[(int)position.x][(int)position.y];
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
        
        public void deleteMap() {
                this.map = null;
        }
        
        public int getWidth()
        {
                return width;
        }
        
        public int getHeight()
        {
                return height;
        }
        
        

        // Creates the main distances structure for given control points.  
        public void CreateDistances(ArrayList<Vector2> controlPoints, Pathway.PathwayType pathwayType, Splines.TypeOfInterpolation typeOfInterpolation){
                if(controlPoints.size() < 4) return;
                
                ClearMap();

                
                //Set line position to zero
                int totalLines = controlPoints.size() * Constants.LINE_SEGMENTATION;                                
                Vector2 point;                                
                for (float i = 0; i <= totalLines; i++) {
                        point = Splines.GetPoint(controlPoints, i / totalLines, typeOfInterpolation, pathwayType);
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
                                        map[currentPoint.x][currentPoint.y] >= Constants.MAX_DISTANCE_FROM_PATHWAY)
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
        
        public TextureRegion generateTexture(Difficulty difficulty) {
                Pixmap pixmap = new Pixmap(1024, 512, Format.RGBA8888);
                pixmap.setColor( 0.75f, 0.7f, 0.6f, 0.9f );
                for (int row = 0; row < Constants.WORLD_HEIGHT; ++row) {
                        for (int column = 0; column < Constants.WORLD_WIDTH; ++column) {

                                // distance map is flipped to the bitmap
                                float distance = this.At(column, Constants.WORLD_HEIGHT
                                                - row - 1);

                                if (distance < difficulty.getMaxDistanceFromSurface()) {
                                        pixmap.drawPixel(column, row);
                                }
                        }
                }
                
                Texture texture = new Texture(pixmap);
                
                TextureRegion region = new TextureRegion(texture, Constants.WORLD_WIDTH, Constants.WORLD_HEIGHT);
                
                return region;
        }
}