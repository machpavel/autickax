package cz.cuni.mff.xcars;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import com.google.android.gms.ads.AdView;

public class AdsHandler implements IAdsHandler {
	protected AdView banner;

	private final int SHOW_ADS = 1;
	private final int HIDE_ADS = 0;

	@SuppressLint("HandlerLeak")
	protected Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case SHOW_ADS:
				banner.setVisibility(View.VISIBLE);
				break;

			case HIDE_ADS:
				banner.setVisibility(View.GONE);
				break;
			}
		}
	};

	public AdsHandler(AdView banner) {
		this.banner = banner;
	}

	@Override
	public void showBanner(boolean show) {
		this.handler.sendEmptyMessage(show ? SHOW_ADS : HIDE_ADS);
	}
}
