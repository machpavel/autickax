import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import cz.mff.cuni.autickax.Constants;
import cz.mff.cuni.autickax.Level;
import cz.mff.cuni.autickax.pathway.DistanceMap;






import com.badlogic.gdx.files.FileHandle;


public class LevelPath {
	private final static int MAX_DRAWN_DISTANCE = 10;
	private final static int TRANSPARENTLY_DRAWN_DISTANCE = 15;
	private final static int TRANSPARENT_PATHWAY = Constants.MAX_DISTANCE_FROM_PATHWAY - Constants.MAX_SURFACE_DISTANCE_FROM_PATHWAY;
	
	private final File file;
	
	public LevelPath(File file) {
		this.file = file;
	}
	
	public void createBitmap(int width, int height, String outputPath) {
		
		Level level = new Level(new FileHandle(this.file));
		try {
			level.parseLevel(null);
		} catch (Exception e) {
			System.out.println("Unable to parse file " + this.file.getName() + ". Details: " + e.getMessage());
			e.printStackTrace();
		}
		
		DistanceMap distanceMap = level.getPathway().getDistanceMap();
		
		BufferedImage image = new BufferedImage(width, height,  BufferedImage.TYPE_4BYTE_ABGR);
		
		for (int row = 0; row < Constants.WORLD_HEIGHT; ++row) {
			for (int column = 0; column < Constants.WORLD_WIDTH; ++column) {
				
				// distance map is flipped to the bitmap
				float distance = distanceMap.At(column, Constants.WORLD_HEIGHT - row - 1);
				
				if (distance < Constants.MAX_SURFACE_DISTANCE_FROM_PATHWAY) {	
					Color pathwayColor = new Color(0, 0, 0, 255);
					image.setRGB(column, row, pathwayColor.getRGB());
				}
				else if (distance < Constants.MAX_DISTANCE_FROM_PATHWAY) {
					distance -= Constants.MAX_SURFACE_DISTANCE_FROM_PATHWAY; // cause this is where the transparency begin
					
					int alpha = (int)(((TRANSPARENT_PATHWAY - distance) / TRANSPARENT_PATHWAY) * 255);
					Color pathwayColor = new Color(0, 0, 0, alpha);
					image.setRGB(column, row, pathwayColor.getRGB());
				}
			}
		}
		
		File output = new File(outputPath + "\\" + this.file.getName().replace("xml", "png"));
		try {
			ImageIO.write(image, "png", output);
		} catch (IOException e) {
			System.out.println("Unable to write file " + output.getName() + ". Details: " + e.getMessage());
			e.printStackTrace();
		}
	}
}
