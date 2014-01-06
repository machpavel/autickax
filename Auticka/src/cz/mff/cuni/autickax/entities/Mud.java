package cz.mff.cuni.autickax.entities;

import java.io.IOException;

import com.badlogic.gdx.utils.XmlWriter;
import com.badlogic.gdx.utils.XmlReader.Element;

import cz.mff.cuni.autickax.scene.GameScreen;

public final class Mud extends GameObject{

	public Mud(float x, float y, GameScreen gameScreen, String textureName) {
		super(x, y, 10, 10, gameScreen, textureName);		
	}
	
	public Mud(Element element, GameScreen gameScreen) {
		super(element.getFloat("X"), element.getFloat("Y"), 10, 10, gameScreen, element.getAttribute("textureName"));
	}

	@Override
	public void update(float delta) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getName() { 
		return "mud";
	}

	@Override
	void aditionalsToXml(XmlWriter writer) throws IOException {
		// TODO Auto-generated method stub
		
	}

}
