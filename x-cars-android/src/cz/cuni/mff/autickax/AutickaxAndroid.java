package cz.cuni.mff.autickax;

import android.os.Bundle;
import android.view.WindowManager;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;

import cz.cuni.mff.autickax.Autickax;

public class AutickaxAndroid extends AndroidApplication {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		AndroidApplicationConfiguration cfg = new AndroidApplicationConfiguration();
		initialize(new Autickax(), cfg);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
	}
}