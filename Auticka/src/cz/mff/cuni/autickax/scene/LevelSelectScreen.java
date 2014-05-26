package cz.mff.cuni.autickax.scene;

import java.util.Vector;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Slider.SliderStyle;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

import cz.mff.cuni.autickax.Autickax;
import cz.mff.cuni.autickax.Debug;
import cz.mff.cuni.autickax.Difficulty;
import cz.mff.cuni.autickax.Level;
import cz.mff.cuni.autickax.PlayedLevel;
import cz.mff.cuni.autickax.constants.Constants;
import cz.mff.cuni.autickax.input.Input;
import cz.mff.cuni.autickax.screenObjects.ScreenAdaptiveTextButton;

public class LevelSelectScreen extends BaseScreen {

	private final Difficulty difficulty;

	private static final int buttonsStartXPosition = 20;
	private static final int buttonsStartYPosition = 330;
	private static final int buttonsMaxXPosition = 750;
	private static final int buttonsPageMaximalCount = Constants.menu.DISPLAYED_LEVELS_MAX_COUNT;
	private static final int buttonsXShift = 150;
	private static final int buttonsYShift = 150;

	// Variable to disable buttons action (loading new level)
	private boolean wasPanned = false;
	private float lastButtonsShift = 0;
	private float buttonsShift = 0;
	private boolean autoListButtons = false;
	private float flingVelocity;
	private float actualPage;

	private int maximumPage;
	ScreenAdaptiveTextButton[] buttons;

	Slider slider;
	private boolean sliderIsOperating = false;

	public LevelSelectScreen(final Difficulty difficulty) {
		this(difficulty, 0);
	}

	static float sliderXOffset = 100;

	public LevelSelectScreen(final Difficulty difficulty, final int startingPage) {
		this.difficulty = difficulty;
		this.actualPage = startingPage;

		Vector<Level> levels = this.difficulty.getAvailableLevels();
		Vector<PlayedLevel> playedLevels = this.difficulty.getPlayedLevels();

		int x = buttonsStartXPosition;
		int y = buttonsStartYPosition;
		int page = 0;
		int numberOfButtons = levels.size();
		this.buttons = new ScreenAdaptiveTextButton[numberOfButtons];
		for (int i = 0; i < numberOfButtons; ++i) {
			final int levelIndex = i;

			String buttonTexture = Constants.menu.BUTTON_MENU_LEVEL_NO_STAR;
			String buttonTextureHover = Constants.menu.BUTTON_MENU_LEVEL_NO_STAR_HOVER;

			if (i < playedLevels.size()) {
				switch (playedLevels.get(i).starsNumber) {
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
					Integer.toString(i + 1),
					Autickax.getInstance().assets.getGraphics(buttonTexture),
					Autickax.getInstance().assets.getGraphics(buttonTextureHover),
					Autickax.getInstance().assets
							.getGraphics(Constants.menu.BUTTON_MENU_LEVEL_DISABLED),
					Autickax.getInstance().assets.getLevelNumberFont(), i < playedLevels.size()) {
				@Override
				public void action() {
					if (!wasPanned) {
						Autickax.getInstance().assets.soundAndMusicManager.pauseMenuMusic();
						Autickax.getInstance().assets.soundAndMusicManager.playSound(
								Constants.sounds.SOUND_MENU_OPEN,
								Constants.sounds.SOUND_DEFAULT_VOLUME);

						if (Autickax.levelLoadingScreen != null) {
							Autickax.levelLoadingScreen.dispose();
							Autickax.levelLoadingScreen = null;
						}

						Autickax.levelLoadingScreen = new LevelLoadingScreen(levelIndex, difficulty);
						Autickax.getInstance().setScreen(Autickax.levelLoadingScreen);
					}
				}
			};

			levelButton.setPosition(x, y);
			stage.addActor(levelButton);
			buttons[i] = levelButton;

			// Disables unplayed levels
			if (i >= playedLevels.size()) {
				levelButton.setDisabled(true);
			}

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
		this.maximumPage = page;

		for (ScreenAdaptiveTextButton levelButton : this.buttons) {
			levelButton.setX(levelButton.getX() - this.actualPage * Gdx.graphics.getWidth());
		}

		this.createSlider(this.maximumPage, this.actualPage);
	}

	private void createSlider(float maxPages, float actualPage) {
		SliderStyle style = new SliderStyle();
		// TODO Figure out how to show the progress
		style.background = new TextureRegionDrawable(
				Autickax.getInstance().assets
						.getGraphics(Constants.minigames.SWITCHING_MINIGAME_SLIDER_BACKGROUND_TEXTURE));
		style.knob = new TextureRegionDrawable(
				Autickax.getInstance().assets
						.getGraphics(Constants.minigames.SWITCHING_MINIGAME_SLIDER_KNOB_TEXTURE));
		this.slider = new Slider(0, 100, 0.001f, false, style);
		this.slider.setWidth(maxPages * sliderXOffset);
		this.slider.setPosition(400 * Input.xStretchFactorInv - this.slider.getWidth() / 2,
				20 * Input.yStretchFactorInv);
		this.slider.setValue(50);
		this.slider.addListener(new InputListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				sliderIsOperating = true;
				return true;
			}

			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				sliderIsOperating = false;
			}
		});
		//this.stage.addActor(slider);
	}

	@Override
	protected void onBackKeyPressed() {
		Autickax.getInstance().assets.soundAndMusicManager.playSound(
				Constants.sounds.SOUND_MENU_CLOSE, Constants.sounds.SOUND_DEFAULT_VOLUME);
		Autickax.difficultySelectScreen.dispose();
		Autickax.difficultySelectScreen = new DifficultySelectScreen();
		Autickax.getInstance().setScreen(Autickax.difficultySelectScreen);
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
				if (!sliderIsOperating) {
					// Enables stage buttons to load level
					wasPanned = false;
					// Stops moving of buttons
					autoListButtons = false;
				}
				return false;
			}

			@Override
			public boolean fling(float velocityX, float velocityY, int button) {
				if (!sliderIsOperating) {
					// Sets the speed of momentum
					flingVelocity = velocityX;
				}
				return false;
			}

			@Override
			public boolean pan(float x, float y, float deltaX, float deltaY) {
				if (!sliderIsOperating) {
					// Shifts buttons and disables level loading
					wasPanned = true;
					for (ScreenAdaptiveTextButton levelButton : buttons) {
						levelButton.setX(levelButton.getX() + deltaX);
					}
					buttonsShift += deltaX;
				}
				return true;
			}
		}) {
			@Override
			public boolean touchUp(int x, int y, int pointer, int button) {
				if (!sliderIsOperating) {
					// Starts buttons animation
					autoListButtons = true;
					lastButtonsShift = buttonsShift;
				}
				return super.touchUp(x, y, pointer, button);
			}
		};
		return new InputMultiplexer(gestureDetector, this.stage);
	}

	@Override
	public void render(float delta) {
		Debug.SetValue(this.buttonsShift);
		autoMoveButtons(delta);
		super.render(delta);
	}

	private void autoMoveButtons(float delta) {
		boolean isGoingRight = this.flingVelocity < 0;
		boolean isGoingLeft = this.flingVelocity > 0;
		if (this.autoListButtons) {
			// More pages
			if (this.maximumPage > 0) {
				if ((this.actualPage == 0 && isGoingLeft)
						|| (this.actualPage == this.maximumPage && isGoingRight))
					this.flingVelocity = -this.flingVelocity;

				boolean zeroCrossing = Math.signum(lastButtonsShift) != Math.signum(buttonsShift);
				boolean limitCrossing = Math.abs(this.buttonsShift) > Gdx.graphics.getWidth();
				if (!zeroCrossing && !limitCrossing) {
					float shift = delta * this.flingVelocity;
					for (ScreenAdaptiveTextButton levelButton : this.buttons) {
						levelButton.setX(levelButton.getX() + shift);
					}
					this.lastButtonsShift = this.buttonsShift;
					this.buttonsShift += shift;
				} else {
					for (ScreenAdaptiveTextButton levelButton : this.buttons) {
						levelButton.setX(levelButton.getX() - this.buttonsShift
								% Gdx.graphics.getWidth());
					}

					if (!zeroCrossing)
						this.actualPage -= Math.signum(this.flingVelocity);

					this.buttonsShift = 0;
					this.autoListButtons = false;
				}
			}
			// Only one page
			else {
				boolean zeroCrossing = Math.signum(lastButtonsShift) != Math.signum(buttonsShift);
				if (!zeroCrossing) {
					float shift = -delta * Math.signum(this.buttonsShift)
							* Math.abs(this.flingVelocity);
					for (ScreenAdaptiveTextButton levelButton : this.buttons) {
						levelButton.setX(levelButton.getX() + shift);
					}
					this.lastButtonsShift = this.buttonsShift;
					this.buttonsShift += shift;
				} else {
					for (ScreenAdaptiveTextButton levelButton : this.buttons) {
						levelButton.setX(levelButton.getX() - this.buttonsShift
								% Gdx.graphics.getWidth());
					}
					this.buttonsShift = 0;
					this.autoListButtons = false;
				}
			}
		}
	}
}
