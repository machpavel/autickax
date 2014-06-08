package cz.cuni.mff.xcars;

import android.content.Context;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;

public class XcarsAndroid extends AndroidApplication {
	private static final String AD_UNIT_ID = "ca-app-pub-7037996113279198/2020565061";
	protected AdView adView;
	protected View gameView;
	private AdsHandler adsHandler;

	private static String ShabbyhoMobil = "708BD4F8D8C72F57AE155EA12EB4E6C8";

	// @Override
	// public void onCreate(Bundle savedInstanceState) {
	// super.onCreate(savedInstanceState);
	//
	// AndroidApplicationConfiguration cfg = new
	// AndroidApplicationConfiguration();
	// initialize(new Xcars(), cfg);
	// getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
	// }
	//
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		AndroidApplicationConfiguration cfg = new AndroidApplicationConfiguration();

		// Does the stuff that initialize() would do for you
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		getWindow().clearFlags(
				WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

		RelativeLayout layout = new RelativeLayout(this);
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.MATCH_PARENT,
				RelativeLayout.LayoutParams.MATCH_PARENT);
		layout.setLayoutParams(params);

		this.adView = createAdView();
		this.gameView = createGameView(cfg, this.adView);
		layout.addView(this.gameView);
		layout.addView(this.adView);
		setContentView(layout);

		boolean isOnline = isNetworkAvailable();
		this.adsHandler.setIsOnline(isOnline);
		Gdx.app.log("Ads", "Is online: " + isOnline);
		startAdvertising(this.adView);
	}

	private AdView createAdView() {
		AdView adView = new AdView(this);
		adView.setAdSize(AdSize.SMART_BANNER);
		adView.setAdUnitId(AD_UNIT_ID);
		RelativeLayout.LayoutParams adParams = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		adParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		adParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
		adView.setLayoutParams(adParams);
		adView.setBackgroundColor(Color.BLACK);
		return adView;
	}

	private View createGameView(AndroidApplicationConfiguration cfg,
			AdView adView) {
		this.adsHandler = new AdsHandler(adView);
		View gameView = initializeForView(new Xcars(this.adsHandler), cfg);
		return gameView;
	}

	private void startAdvertising(AdView adView) {
		AdRequest adRequest = new AdRequest.Builder()
				.addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
				.addTestDevice(ShabbyhoMobil).build();
		adView.loadAd(adRequest);
	}

	@Override
	public void onResume() {
		super.onResume();
		if (adView != null)
			adView.resume();
		if (this.adsHandler != null)
			this.adsHandler.setIsOnline(isNetworkAvailable());
	}

	@Override
	public void onPause() {
		if (adView != null)
			adView.pause();
		super.onPause();
	}

	@Override
	public void onDestroy() {
		if (adView != null)
			adView.destroy();
		super.onDestroy();
	}

	private boolean isNetworkAvailable() {
		ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetworkInfo = connectivityManager
				.getActiveNetworkInfo();
		return activeNetworkInfo != null && activeNetworkInfo.isConnected();
	}

}