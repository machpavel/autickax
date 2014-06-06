package cz.cuni.mff.xcars.scene;

import java.util.Vector;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Slider.SliderStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

import cz.cuni.mff.xcars.Xcars;
import cz.cuni.mff.xcars.Difficulty;
import cz.cuni.mff.xcars.PlayedLevel;
import cz.cuni.mff.xcars.constants.Constants;
import cz.cuni.mff.xcars.input.Input;
import cz.cuni.mff.xcars.screenObjects.ScreenAdaptiveImage;
import cz.cuni.mff.xcars.screenObjects.ScreenAdaptiveTextButton;

public class LevelSelectScreen extends BaseScreen {

	private final Difficulty difficulty;

	private static final int buttonsStartXPosition = 20;
	private static final int buttonsStartYPosition = 330;
	private static final int buttonsMaxXPosition = 750;
	private static final int buttonsPageMaximalCount = Constants.menu.DISPLAYED_LEVELS_MAX_COUNT;
	private static final int buttonsXShift = 150;
	private static final int buttonsYShift = 150;

	static float sliderXOffset = 100;
	static float sliderYPosition = 20;
	static float defaultFlingVelocity = 600;

	// Variable to disable buttons action (loading new level)
	private boolean wasPanned = false;
	private float lastButtonsShift = 0;
	private float buttonsShift = 0;
	private boolean isAnimationInProgress = false;
	private float flingVelocity;
	private float actualPage;

	private final int pagesCount;
	ScreenAdaptiveTextButton[] buttons;

	Slider slider;
	private boolean isSliderOperating = false;

	public LevelSelectScreen(final Difficulty difficulty) {
		this(difficulty, 0);
		if(Xcars.adsHandler != null){
			Xcars.adsHandler.showBanner(false);
		}
	}

	public LevelSelectScreen(final Difficulty difficulty, final int startingPage) {
		this.difficulty = difficulty;
		this.actualPage = startingPage;
		int numberOfButtons = this.difficulty.getAvailableLevels().size();
		this.buttons = new ScreenAdaptiveTextButton[numberOfButtons];
		for (int i = 0; i < numberOfButtons; ++i) {
			ScreenAdaptiveTextButton levelButton = createButton(i,
					this.difficulty.getPlayedLevels());
			stage.addActor(levelButton);
			buttons[i] = levelButton;
		}
		this.pagesCount = numberOfButtons / buttonsPageMaximalCount + 1;
		setButtonsPosition(this.actualPage, 0);

		createAnchors(this.pagesCount);
		this.slider = createSlider(this.pagesCount, this.actualPage);
		this.stage.addActor(this.slider);
	}

	/**
	 * Creates a text button with according start level handler.
	 * 
	 * @param levelIndex
	 *            Number of level
	 * @param playedLevels
	 *            List of played levels
	 * @return
	 */
	private ScreenAdaptiveTextButton createButton(final int levelIndex,
			Vector<PlayedLevel> playedLevels) {

		String buttonTexture = Constants.menu.BUTTON_MENU_LEVEL_NO_STAR;
		String buttonTextureHover = Constants.menu.BUTTON_MENU_LEVEL_NO_STAR_HOVER;

		if (levelIndex < playedLevels.size()) {
			switch (playedLevels.get(levelIndex).starsNumber) {
			case 0:
				buttonTexture = Constants.menu.BUTTON_MENU_LEVEL_NO_STAR;
				buttonTextureHover = Constants.menu.BUTTON_MENU_LEVEL_NO_STAR_HOVER;
				break;
			case 1:
				buttonTexture = Constants.menu.BUTTON_MENU_LEVEL_ONE_STAR;
				buttonTextureHover = Constants.menu.BUTTON_MENU_LEVEL_ONE_STAR_HOVER;
				break;
			case 2:
				buttonTexture = Constants.menu.BUTTON_MENU_LEVEL_TWO_STARS;
				buttonTextureHover = Constants.menu.BUTTON_MENU_LEVEL_TWO_STARS_HOVER;
				break;
			case 3:
				buttonTexture = Constants.menu.BUTTON_MENU_LEVEL_THREE_STARS;
				buttonTextureHover = Constants.menu.BUTTON_MENU_LEVEL_THREE_STARS_HOVER;
				break;
			default:
				throw new RuntimeException("Stars can't be more then three");
			}
		}
		ScreenAdaptiveTextButton levelButton = new ScreenAdaptiveTextButton(
				Integer.toString(levelIndex + 1),
				Xcars.getInstance().assets.getGraphics(buttonTexture),
				Xcars.getInstance().assets.getGraphics(buttonTextureHover),
				Xcars.getInstance().assets
						.getGraphics(Constants.menu.BUTTON_MENU_LEVEL_DISABLED),
				Xcars.getInstance().assets.getLevelNumberFont(),
				levelIndex < playedLevels.size()) {
			@Override
			public void action() {
				if (!wasPanned) {
					Xcars.getInstance().assets.soundAndMusicManager.pauseMenuMusic();
					Xcars.getInstance().assets.soundAndMusicManager
							.playSound(Constants.sounds.SOUND_MENU_OPEN,
									Constants.sounds.SOUND_DEFAULT_VOLUME);
					if (Xcars.levelLoadingScreen != null) {
						Xcars.levelLoadingScreen.dispose();
						Xcars.levelLoadingScreen = null;
					}
					Xcars.levelLoadingScreen = new LevelLoadingScreen(levelIndex, difficulty);
					Xcars.getInstance().setScreen(Xcars.levelLoadingScreen);
				}
			}
		};
		// Disables a button with unplayed level
		if (levelIndex >= playedLevels.size()) {
			levelButton.setDisabled(true);
		}
		return levelButton;
	}

	private void setButtonsPosition(float pageNumber, float offset) {
		ScreenAdaptiveTextButton levelButton;
		int x = buttonsStartXPosition;
		int y = buttonsStartYPosition;
		int page = 0;

		for (int i = 0; i < this.buttons.length; i++) {
			levelButton = this.buttons[i];
			levelButton.setPosition(x - pageNumber * Constants.WORLD_WIDTH + offset, y);

			// Moves onto new page
			if (i + 1 < LevelSelectScreen.buttonsPageMaximalCount * (page + 1)) {
				// Positioning on a page
				if (x + LevelSelectScreen.buttonsXShift < LevelSelectScreen.buttonsMaxXPosition
						+ page * Constants.WORLD_WIDTH) {
					x += LevelSelectScreen.buttonsXShift;
				} else {

					x = LevelSelectScreen.buttonsStartXPosition + page * Constants.WORLD_WIDTH;
					y -= LevelSelectScreen.buttonsYShift;
				}
			} else {
				page++;
				x = LevelSelectScreen.buttonsStartXPosition + page * Constants.WORLD_WIDTH;
				y = LevelSelectScreen.buttonsStartYPosition;
			}
		}
	}

	private void createAnchors(float maxPages) {
		float xOffset = Constants.WORLD_WIDTH / 2
				- ((int) (LevelSelectScreen.sliderXOffset * maxPages) - sliderXOffset) / 2;
		for (int i = 0; i < maxPages; i++) {

			ScreenAdaptiveImage image = new ScreenAdaptiveImage(
					Xcars.getInstance().assets
							.getGraphics(Constants.menu.SLIDER_MENU_LEVEL_ANCHOR));
			image.setCenterPosition(xOffset + i * LevelSelectScreen.sliderXOffset,
					LevelSelectScreen.sliderYPosition);
			this.stage.addActor(image);
		}
	}

	private Slider createSlider(float maxPages, float actualPage) {
		SliderStyle style = new SliderStyle();
		style.background = new TextureRegionDrawable(
				Xcars.getInstance().assets
						.getGraphics(Constants.menu.SLIDER_MENU_LEVEL_BACKGROUND));
		style.knob = new TextureRegionDrawable(
				Xcars.getInstance().assets.getGraphics(Constants.menu.SLIDER_MENU_LEVEL_KNOB));
		Slider slider = new Slider(0, 1, 0.001f, false, style);
		slider.setWidth((maxPages - 1) * LevelSelectScreen.sliderXOffset + style.knob.getMinWidth());
		slider.setPosition(Constants.WORLD_WIDTH / 2 - slider.getWidth() / 2,
				LevelSelectScreen.sliderYPosition - slider.getHeight() / 2);
		if (maxPages > 1)
			slider.setValue(actualPage / (maxPages - 1));
		slider.addListener(new InputListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				LevelSelectScreen.this.isSliderOperating = true;
				LevelSelectScreen.this.flingVelocity = LevelSelectScreen.defaultFlingVelocity;
				LevelSelectScreen.this.doSliderChange();
				return true;
			}

			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				LevelSelectScreen.this.startAnimation();
				LevelSelectScreen.this.isSliderOperating = false;
			}
		});
		slider.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				if (LevelSelectScreen.this.isSliderOperating) {
					LevelSelectScreen.this.doSliderChange();
				}
			}
		});
		return slider;
	}

	private void doSliderChange() {
		float scaledValue = this.slider.getValue() * (this.pagesCount - 1);
		float pageNumber = Math.round(scaledValue);
		float offset = -(scaledValue - pageNumber) * Constants.WORLD_WIDTH;
		this.actualPage = pageNumber;
		this.buttonsShift = offset;
		setButtonsPosition(pageNumber, offset);
		if (offset != 0)
			this.flingVelocity = -Math.abs(this.flingVelocity) * Math.signum(offset);
	}

	@Override
	protected void onBackKeyPressed() {
		Xcars.getInstance().assets.soundAndMusicManager.playSound(
				Constants.sounds.SOUND_MENU_CLOSE, Constants.sounds.SOUND_DEFAULT_VOLUME);
		Xcars.difficultySelectScreen.dispose();
		Xcars.difficultySelectScreen = new DifficultySelectScreen();
		Xcars.getInstance().setScreen(Xcars.difficultySelectScreen);
	}

	@Override
	protected void clearScreenWithColor() {
		Gdx.gl.glClearColor(Constants.menu.LEVELS_MENU_RED, Constants.menu.LEVELS_MENU_GREEN,
				Constants.menu.LEVELS_MENU_BLUE, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
	}

	@Override
	protected InputProcessor createInputProcessor() {
		GestureDetector gestureDetector = new GestureDetector(new MenuGestureListener() {
			@Override
			public boolean touchDown(float x, float y, int pointer, int button) {
				if (!LevelSelectScreen.this.isSliderOperating) {
					// Enables stage buttons to load level
					LevelSelectScreen.this.wasPanned = false;
					// Stops moving of buttons
					LevelSelectScreen.this.isAnimationInProgress = false;
				}
				return false;
			}

			@Override
			public boolean fling(float velocityX, float velocityY, int button) {
				if (!LevelSelectScreen.this.isSliderOperating) {
					LevelSelectScreen.this.flingVelocity = velocityX * Input.xStretchFactor;
				}
				return false;
			}

			@Override
			public boolean pan(float x, float y, float deltaX, float deltaY) {
				if (!LevelSelectScreen.this.isSliderOperating) {
					// Shifts buttons and disables level loading
					LevelSelectScreen.this.wasPanned = true;
					LevelSelectScreen.this.buttonsShift += deltaX * Input.xStretchFactor;
					LevelSelectScreen.this.setButtonsPosition(actualPage, buttonsShift);
					LevelSelectScreen.this.moveSlider();

				}
				return false;
			}
		}) {
			@Override
			public boolean touchUp(int x, int y, int pointer, int button) {
				if (!LevelSelectScreen.this.isSliderOperating) {
					LevelSelectScreen.this.startAnimation();
				}
				return super.touchUp(x, y, pointer, button);
			}
		};
		return new InputMultiplexer(gestureDetector, this.stage);
	}

	@Override
	public void render(float delta) {
		updateAnimation(delta);
		super.render(delta);
	}

	private void startAnimation() {
		this.isAnimationInProgress = true;
		this.lastButtonsShift = this.buttonsShift;
	}

	private void updateAnimation(float delta) {
		if (this.isAnimationInProgress) {
			if (this.buttonsShift != 0) {
				boolean isGoingRight = this.flingVelocity < 0;
				boolean isGoingLeft = this.flingVelocity > 0;
				boolean areButtonsRight = this.buttonsShift > 0;
				boolean areButtonsLeft = this.buttonsShift < 0;

				if (this.pagesCount > 1) {
					if (this.actualPage == 0 && isGoingLeft && areButtonsRight) {
						this.flingVelocity = -Math.abs(this.flingVelocity);
					} else if (this.actualPage == this.pagesCount - 1 && isGoingRight
							&& areButtonsLeft)
						this.flingVelocity = Math.abs(this.flingVelocity);
				} else {
					this.flingVelocity = -Math.abs(this.flingVelocity)
							* Math.signum(this.buttonsShift);
				}

				boolean zeroCrossing = Math.signum(this.lastButtonsShift) != Math
						.signum(this.buttonsShift);
				boolean limitCrossing = Math.abs(this.buttonsShift) > Constants.WORLD_WIDTH;

				if (!zeroCrossing && !limitCrossing) {
					this.lastButtonsShift = this.buttonsShift;
					this.buttonsShift += delta * this.flingVelocity;
				} else {
					if (!zeroCrossing) {
						this.actualPage -= Math.signum(this.flingVelocity);
					}
					this.buttonsShift = 0;
					this.isAnimationInProgress = false;
				}
				setButtonsPosition(this.actualPage, this.buttonsShift);
				moveSlider();
			} else {
				this.isAnimationInProgress = false;
			}
		}
	}

	private void moveSlider() {
		if (this.pagesCount > 1)
			this.slider.setValue(actualPage / (this.pagesCount - 1) - this.buttonsShift
					/ Constants.WORLD_WIDTH / (this.pagesCount - 1));
	}
}
