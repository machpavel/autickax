package cz.mff.cuni.autickax.entities;

import java.io.IOException;
import java.io.Serializable;

import com.badlogic.gdx.utils.XmlWriter;

import cz.mff.cuni.autickax.constants.Constants;
import cz.mff.cuni.autickax.gamelogic.SubLevel;
import cz.mff.cuni.autickax.miniGames.Crash;
import cz.mff.cuni.autickax.miniGames.Minigame;
import cz.mff.cuni.autickax.scene.GameScreen;

public final class Tree extends GameObject implements Serializable {
	private static final long serialVersionUID = 1L;

	public Tree(float x, float y, GameScreen gameScreen, int type) {	
		super(x,y,gameScreen, type);
	}
	
	public Tree(GameObject object){
		super(object);		
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
		
	/** Gets the texture name according to a type*/
	public static String GetTextureName(int type){
		return Constants.gameObjects.TREE_TEXTURE_PREFIX + type;
	}

	@Override
	public GameObject copy() {
		return new Tree(this);
	}

	@Override
	public void setTexture(int type) {
		super.setTexture(Tree.GetTextureName(type));		
	}

	@Override
	public Minigame getMinigame(GameScreen gameScreen, SubLevel parent) {
		return new Crash(gameScreen, parent);
	}
	
	@Override
	public String getSoundName() {
		return Constants.sounds.SOUND_TREE;
	}
}
