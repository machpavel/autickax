import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import cz.mff.cuni.autickax.Level;
import cz.mff.cuni.autickax.pathway.DistanceMap;





import com.badlogic.gdx.files.FileHandle;


public class LevelPath {
	private final static int REFERENCE_LEVEL_WIDTH = 800;
	private final static int REFERENCE_LEVEL_HEIGH = 480;
	private final static int MAX_DRAWN_DISTANCE = 10;
	private final static int TRANSPARENTLY_DRAWN_DISTANCE = 15;
	
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
		
		for (int row = 0; row < REFERENCE_LEVEL_HEIGH; ++row) {
			for (int column = 0; column < REFERENCE_LEVEL_WIDTH; ++column) {
				
				float distance = distanceMap.At(column, row);
				if (distance < LevelPath.MAX_DRAWN_DISTANCE) {	
					Color pathwayColor = new Color(0, 0, 0, 255);
					image.setRGB(column, row, pathwayColor.getRGB());
				}
				else if (distance < LevelPath.TRANSPARENTLY_DRAWN_DISTANCE) {
					distance -= LevelPath.MAX_DRAWN_DISTANCE; // cause this is where the transparency begin
					
					int alpha = (int)(((5 - distance) / 5) * 255);
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
