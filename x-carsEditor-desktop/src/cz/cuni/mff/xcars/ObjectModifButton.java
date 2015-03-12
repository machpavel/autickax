package cz.cuni.mff.xcars;

import java.util.concurrent.atomic.AtomicBoolean;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

public class ObjectModifButton extends TextButton{

	AtomicBoolean modifFlag;
	public ObjectModifButton(String text, Skin skin, AtomicBoolean modifFlag) {
		super(text, skin);
		this.modifFlag = modifFlag;				
		
		this.addListener(new InputListener(){
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				ObjectModifButton.this.modifFlag.set(true);
				return true;
			}
			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				ObjectModifButton.this.modifFlag.set(false);
				super.touchUp(event, x, y, pointer, button);
			}
		});
	}

}
