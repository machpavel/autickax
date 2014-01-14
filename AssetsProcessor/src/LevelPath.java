import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import cz.mff.cuni.autickax.Constants;
import cz.mff.cuni.autickax.LevelLoading;
import cz.mff.cuni.autickax.pathway.DistanceMap;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;

public class LevelPath {
	private final static int TRANSPARENT_PATHWAY = Constants.MAX_DISTANCE_FROM_PATHWAY
			- Constants.MAX_SURFACE_DISTANCE_FROM_PATHWAY;

	private final File file;
	private final String directory;
	
	public LevelLoading level;

	public LevelPath(File file, String directory) {
		this.file = file;
		this.directory = directory; 
	}
	
	public void parseLevel() {
		this.level = new LevelLoading();
		try {
			level.parseLevel(null, new FileHandle(this.file));
		} catch (Exception e) {
			System.out.println("Unable to parse file " + this.file.getName()
					+ ". Details: " + e.getMessage());
			e.printStackTrace();
		}
	}
	
	public LevelLoading getLevel() {
		return this.level;
	}

	public void createBitmap(int width, int height, String inputPath,
			String outputPath) {
		BufferedImage texture = null;
		try {
			String inputTextureFileName = "";
			if (level.getPathwayTextureType() == 0)
				inputTextureFileName = Constants.PATHWAY_TEXTURE_TYPE_1;
			else
				System.out.println("Pathway background file not found.");

			texture = ImageIO.read(new File(inputPath + "/"
					+ inputTextureFileName + ".png"));
		} catch (IOException e) {
			System.out.println("Loading pathway texture error.");
		}

		DistanceMap distanceMap = level.getPathway().getDistanceMap();

		BufferedImage image = new BufferedImage(width, height,
				BufferedImage.TYPE_4BYTE_ABGR);

		for (int row = 0; row < Constants.WORLD_HEIGHT; ++row) {
			for (int column = 0; column < Constants.WORLD_WIDTH; ++column) {

				// distance map is flipped to the bitmap
				float distance = distanceMap.At(column, Constants.WORLD_HEIGHT
						- row - 1);

				if (distance < Constants.MAX_SURFACE_DISTANCE_FROM_PATHWAY) {
					image.setRGB(column, row, texture.getRGB(column, row));
				} else if (distance < Constants.MAX_DISTANCE_FROM_PATHWAY) {
					// cause this is where the transparency begin
					distance -= Constants.MAX_SURFACE_DISTANCE_FROM_PATHWAY;

					int alpha = (int) (((TRANSPARENT_PATHWAY - distance) / TRANSPARENT_PATHWAY) * 255);
					int color = texture.getRGB(column, row);
					int mc = ((byte) alpha << 24) | 0x00ffffff;
					int newcolor = color & mc;
					image.setRGB(column, row, newcolor);
				}
			}
		}
		
		File levelsImagesDirectory = new File(outputPath + "\\" + this.directory);
    	if (!levelsImagesDirectory.exists()) {
    		System.out.println("creating directory: " + outputPath + "\\" + this.directory);
    		levelsImagesDirectory.mkdir();
    	}

		File output = new File(outputPath + "\\" + this.directory + "\\"
				+ this.file.getName().replace("xml", "png"));
		try {
			ImageIO.write(image, "png", output);
		} catch (IOException e) {
			System.out.println("Unable to write file " + output.getName()
					+ ". Details: " + e.getMessage());
			e.printStackTrace();
		}
	}
}
