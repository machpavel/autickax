package cz.mff.cuni.autickax.constants;

public final class Constants {

	public static final Menu menu  = new Menu(); 
	public static final Sounds sounds = new Sounds();
	public static final Serialization serialization = new Serialization();
	public static final Dialog dialog = new Dialog();
	public static final Minigames minigames = new Minigames();
	public static final Strings strings = new Strings(); 
	public static final GameObjects gameObjects = new GameObjects();
	public static final Misc misc = new Misc();
	public static final TyreTracksConstants tyreTracksConstants = new TyreTracksConstants();
	public static final TimeStatusBarConsts tsb = new TimeStatusBarConsts();
	
	public static final String levelBackgroundsDirectory = "backgrounds";
	
	
	/**
	 * Determines ideal world width. If the resolution is different, coordinates
	 * are stretched.
	 */
	public static final int WORLD_WIDTH = 800;
	/**
	 * Determines ideal world height. If the resolution is different,
	 * coordinates are stretched.
	 */
	public static final int WORLD_HEIGHT = 480;
}
