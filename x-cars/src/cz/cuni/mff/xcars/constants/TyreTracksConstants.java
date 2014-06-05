package cz.cuni.mff.xcars.constants;

import com.badlogic.gdx.graphics.Color;



public class TyreTracksConstants {
	public final float WIDTH_HALF = 10f;
	public final int TYRE_DIST_FROM_CENTER = 30;
	public final int FADE_LIMIT = 400; //duration of tracks in ms
	public final float LINE_WIDTH = 4.5f;
	public final float MAX_SEGMENT_LENGTH =  5;
	public final float MIN_DISTANCE_BETWEEN_POINTS = 1f;
	public final float MAX_DIST_SQUARED = WIDTH_HALF*WIDTH_HALF + TYRE_DIST_FROM_CENTER*TYRE_DIST_FROM_CENTER;
	public final Color TYRE_TRACK_COLOR = new Color(0.1f, 0.1f, 0.12f,1);
	
}
