package cz.mff.cuni.autickax.entities;

import java.io.IOException;
import java.io.Serializable;

import com.badlogic.gdx.utils.XmlWriter;

import cz.mff.cuni.autickax.Constants;
import cz.mff.cuni.autickax.scene.GameScreen;

public final class Tree extends GameObject implements Serializable {
	private static final long serialVersionUID = 1L;

	public Tree(float x, float y, GameScreen gameScreen, int type) {	
		super(x,y,gameScreen);
		super.type = type;
		super.setMeasurements(Tree.GetWidth(type), Tree.GetHeight(type));
		this.setTexture();
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
	
	/** Gets the width according to a type*/
	public static int GetWidth(int type){
		switch (type) {
		case 1:
			return Constants.TREE_TYPE_1_WIDTH;			
		case 2:
			return Constants.TREE_TYPE_2_WIDTH;	
		case 3:
			return Constants.TREE_TYPE_3_WIDTH;	
		case 4:
			return Constants.TREE_TYPE_4_WIDTH;	
		case 5:
			return Constants.TREE_TYPE_5_WIDTH;	
		default:
			//TODO exception
			return 0;
		}
	}
	/** Gets the height according to a type*/
	public static int GetHeight(int type){
		switch (type) {
		case 1:
			return Constants.TREE_TYPE_1_HEIGHT;			
		case 2:
			return Constants.TREE_TYPE_2_HEIGHT;
		case 3:
			return Constants.TREE_TYPE_3_HEIGHT;	
		case 4:
			return Constants.TREE_TYPE_4_HEIGHT;	
		case 5:
			return Constants.TREE_TYPE_5_HEIGHT;	
		default:
			//TODO exception
			return 0;
		}
	}	
	/** Gets the texture name according to a type*/
	public static String GetTextureName(int type){
		switch (type) {
		case 1:
			return Constants.TREE_TYPE_1_TEXTURE_NAME;			
		case 2:
			return Constants.TREE_TYPE_2_TEXTURE_NAME;
		case 3:
			return Constants.TREE_TYPE_3_TEXTURE_NAME;
		case 4:
			return Constants.TREE_TYPE_4_TEXTURE_NAME;
		case 5:
			return Constants.TREE_TYPE_5_TEXTURE_NAME;
		default:
			//TODO exception
			return null;
		}
	}

	@Override
	public GameObject copy() {
		return new Tree(this);
	}

	@Override
	public void setTexture() {
		super.setTexture(Tree.GetTextureName(type));		
	}
}
