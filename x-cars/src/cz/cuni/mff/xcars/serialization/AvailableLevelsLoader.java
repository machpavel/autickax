package cz.cuni.mff.xcars.serialization;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.AsynchronousAssetLoader;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;

import cz.cuni.mff.xcars.AvailableLevels;

public class AvailableLevelsLoader extends AsynchronousAssetLoader<AvailableLevels, AvailableLevelsLoader.AvailableLevelsParameter> {

	AvailableLevels levels;
	
	public AvailableLevelsLoader(FileHandleResolver resolver) {
		super(resolver);
	}


	@Override
	public void loadAsync(AssetManager manager, String fileName,
			FileHandle file, AvailableLevelsParameter parameter) {
		try {
			InputStream fileIn = file.read();
	        ObjectInputStream in = new ObjectInputStream(fileIn);
	        levels = (AvailableLevels)in.readObject();
	        //in.close();
	        fileIn.close();
	    } catch (IOException i) {
	    	System.out.println(i.getMessage());
	        i.printStackTrace();
	    } catch (ClassNotFoundException c) {
	    	System.out.println("AvailableLevels class not found");
	        c.printStackTrace();
	    }
	}


	@Override
	public AvailableLevels loadSync(AssetManager manager, String fileName,
			FileHandle file, AvailableLevelsParameter parameter) {
		return this.levels;
	}


	@SuppressWarnings("rawtypes")
	@Override
	public Array<AssetDescriptor> getDependencies(String fileName,
			FileHandle file, AvailableLevelsParameter parameter) {
		return null;
	}

	
	static public class AvailableLevelsParameter extends AssetLoaderParameters<AvailableLevels> {
    }
}
