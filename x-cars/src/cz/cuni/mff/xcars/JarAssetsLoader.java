package cz.cuni.mff.xcars;

import java.net.URL;
import java.security.CodeSource;
import java.util.LinkedList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

public class JarAssetsLoader {
	public JarAssetsLoader()
	{
		
	}
	
	public LinkedList<FileHandle> LoadFromJar(String path)
	{
		
		LinkedList<FileHandle> fileHandles = new LinkedList<FileHandle>();
		try
		{
		CodeSource src = Assets.class.getProtectionDomain().getCodeSource();
		if (src != null) {
		  URL jar = src.getLocation();
		  ZipInputStream zip = new ZipInputStream(jar.openStream());
		  while(true) {
		    ZipEntry e = zip.getNextEntry();
		    if (e == null)
		      break;
		    String name = e.getName();
		    if (name.startsWith(path) && !e.isDirectory()) {
			    fileHandles.add(Gdx.files.internal(name));
		    }
		  }
		} 
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return fileHandles;
	}
}
