package cz.mff.cuni.autickax.entities;

import java.io.IOException;

import com.badlogic.gdx.utils.XmlReader.Element;
import com.badlogic.gdx.utils.XmlWriter;

import cz.mff.cuni.autickax.scene.GameScreen;

public final class Tree extends GameObject {


	public Tree(float x, float y, int width, int height, GameScreen gameScreen, String textureName){
		super(x, y, width, height, gameScreen, textureName);		
	}
	
	public Tree(Element element, GameScreen gameScreen) {
		super(element, gameScreen);
	}

	@Override
	public void update(float delta) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getName() {
		return "tree";
	}

	@Override
	void aditionalsToXml(XmlWriter writer) throws IOException {
		// TODO Auto-generated method stub
		
	}

}
