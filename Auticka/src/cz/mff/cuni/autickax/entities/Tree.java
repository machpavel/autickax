package cz.mff.cuni.autickax.entities;

import java.io.IOException;

import com.badlogic.gdx.utils.XmlReader.Element;
import com.badlogic.gdx.utils.XmlWriter;

import cz.mff.cuni.autickax.scene.GameScreen;

public final class Tree extends GameObject {


	public Tree(float x, float y, GameScreen gameScreen, String textureName) {
		super(x, y, 10, 10, gameScreen, textureName);		
	}
	
	public Tree(Element element, GameScreen gameScreen) {
		super(element.getFloat("X"), element.getFloat("Y"), 10, 10, gameScreen, element.getAttribute("textureName"));
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
