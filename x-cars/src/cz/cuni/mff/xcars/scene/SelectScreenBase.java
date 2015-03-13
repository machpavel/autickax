package cz.cuni.mff.xcars.scene;

import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Slider.SliderStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

import cz.cuni.mff.xcars.Xcars;
import cz.cuni.mff.xcars.constants.Constants;
import cz.cuni.mff.xcars.debug.Debug;
import cz.cuni.mff.xcars.input.Input;
import cz.cuni.mff.xcars.screenObjects.ScreenAdaptiveImage;
import cz.cuni.mff.xcars.screenObjects.ScreenAdaptiveTextButton;

public abstract class SelectScreenBase extends BaseScreen {
	// Buttons bounds
	protected float buttonsMinXPosition = 20;
	private float buttonsMaxXPosition = Constants.WORLD_WIDTH - buttonsMinXPosition;
	protected float buttonsMinYPosition = 50;
	protected float buttonsMaxYPosition = 450;
	// Spacing between buttons
	protected int buttonsXOffset = 90;
	protected int buttonsYOffset = 90;

	// Slider constants
	static private float sliderXOffset = 100;
	static private float sliderYPosition = 20;
	static private float defaultFlingVelocity = 600;

	// Starting buttons offset on page
	private float buttonsStartYPosition;
	private float buttonsStartXPosition;
	// How many buttons are in page
	private int buttonsYCountPerPage;
	private int buttonsXCountPerPage;
	private int buttonsPerPage;
	// Pages
	private int pagesCount;
	private float actualPage;

	// Variable to disable buttons action (loading new level)
	protected boolean wasPanned = false;
	private float lastButtonsShift = 0;
	private float buttonsShift = 0;
	private boolean isAnimationInProgress = false;
	private float flingVelocity;

	private ScreenAdaptiveTextButton[] buttons;

	Slider slider;
	private boolean isSliderOperating = false;

	protected void RegisterButtons(final int buttonIndex, ScreenAdaptiveTextButton[] buttons) {
		this.buttonsPerPage = setButtonsCountsPerPage(buttons);
		this.pagesCount = buttons.length % this.buttonsPerPage == 0 ? buttons.length / this.buttonsPerPage
				: (buttons.length / this.buttonsPerPage) + 1;
		this.actualPage = Math.min(buttonIndex / this.buttonsPerPage, this.pagesCount - 1);
		

		this.buttons = buttons;
		for (int i = 0; i < buttons.length; i++) {
			this.stage.addActor(buttons[i]);
		}
		setButtonsPosition(this.actualPage, 0);

		createAnchors(this.pagesCount);
		this.slider = createSlider(this.pagesCount, this.actualPage);
		this.stage.addActor(this.slider);
	}

	/**
	 * Set count of buttons per page in x and y direction. Set starting button
	 * offsets
	 * 
	 * @param buttons
	 * @return Total number of buttons
	 */
	private int setButtonsCountsPerPage(ScreenAdaptiveTextButton[] buttons) {
		float y = buttonsMaxYPosition - buttons[0].getHeight();
		int attemp = 0;

		float x = buttonsMinXPosition;
		while (attemp < Short.MAX_VALUE) {
			if (x + buttons[0].getWidth() + this.buttonsXOffset > this.buttonsMaxXPosition) {
				// Centers the buttons
				this.buttonsStartXPosition = this.buttonsMinXPosition
						+ (this.buttonsMaxXPosition - (x - this.buttonsXOffset)) / 2;
				this.buttonsXCountPerPage = attemp;
				break;
			}

			x += buttons[0].getWidth() + this.buttonsXOffset;
			++attemp;
		}

		attemp = 1;
		while (attemp < Short.MAX_VALUE) {
			if (y - this.buttonsYOffset - buttons[0].getHeight() < this.buttonsMinYPosition) {
				this.buttonsStartYPosition = this.buttonsMaxYPosition - (y - this.buttonsMinYPosition) / 2
						- buttons[0].getHeight();
				this.buttonsYCountPerPage = attemp;
				break;
			}

			y -= this.buttonsYOffset + buttons[0].getHeight();
			++attemp;
		}

		// To be sure, it ends
		return this.buttonsXCountPerPage * this.buttonsYCountPerPage;
	}

	protected void setButtonsPosition(float pageNumber, float offset) {
		// float x = buttonsMinXPosition;
		// float y = buttonsMaxYPosition - this.buttons[0].getHeight();
		// int page = 0;

		int buttonIndex = 0;
		for (int page = 0; page < this.pagesCount; page++)
			for (int yIndex = 0; yIndex < this.buttonsYCountPerPage; yIndex++)
				for (int xIndex = 0; xIndex < this.buttonsXCountPerPage; xIndex++) {
					if (buttonIndex < this.buttons.length) {
						float x = xIndex * (this.buttons[0].getWidth() + this.buttonsXOffset)
								+ this.buttonsStartXPosition + (page - pageNumber) * Constants.WORLD_WIDTH;
						float y = this.buttonsStartYPosition - yIndex
								* (this.buttonsYOffset + this.buttons[0].getHeight());
						this.buttons[buttonIndex].setPosition(x + offset, y);
						++buttonIndex;
					} else {
						return;
					}
				}
	}

	private void createAnchors(float maxPages) {
		float xOffset = Constants.WORLD_WIDTH / 2 - ((int) (SelectScreenBase.sliderXOffset * maxPages) - sliderXOffset)
				/ 2;
		for (int i = 0; i < maxPages; i++) {

			ScreenAdaptiveImage image = new ScreenAdaptiveImage(
					Xcars.getInstance().assets.getGraphics(Constants.menu.SLIDER_MENU_LEVEL_ANCHOR));
			image.setCenterPosition(xOffset + i * SelectScreenBase.sliderXOffset, SelectScreenBase.sliderYPosition);
			this.stage.addActor(image);
		}
	}

	private Slider createSlider(float maxPages, float actualPage) {
		SliderStyle style = new SliderStyle();
		style.background = new TextureRegionDrawable(
				Xcars.getInstance().assets.getGraphics(Constants.menu.SLIDER_MENU_LEVEL_BACKGROUND));
		style.knob = new TextureRegionDrawable(
				Xcars.getInstance().assets.getGraphics(Constants.menu.SLIDER_MENU_LEVEL_KNOB));
		Slider slider = new Slider(0, 1, 0.001f, false, style);
		slider.setWidth((maxPages - 1) * SelectScreenBase.sliderXOffset + style.knob.getMinWidth());
		slider.setPosition(Constants.WORLD_WIDTH / 2 - slider.getWidth() / 2,
				SelectScreenBase.sliderYPosition - slider.getHeight() / 2);
		if (maxPages > 1)
			slider.setValue(actualPage / (maxPages - 1));
		slider.addListener(new InputListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				SelectScreenBase.this.isSliderOperating = true;
				SelectScreenBase.this.flingVelocity = SelectScreenBase.defaultFlingVelocity;
				SelectScreenBase.this.doSliderChange();
				return true;
			}

			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				SelectScreenBase.this.startAnimation();
				SelectScreenBase.this.isSliderOperating = false;
			}
		});
		slider.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				if (SelectScreenBase.this.isSliderOperating) {
					SelectScreenBase.this.doSliderChange();
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
	protected InputProcessor createInputProcessor() {
		GestureDetector gestureDetector = new GestureDetector(new MenuGestureListener() {
			@Override
			public boolean touchDown(float x, float y, int pointer, int button) {
				if (!SelectScreenBase.this.isSliderOperating) {
					// Enables stage buttons to load level
					SelectScreenBase.this.wasPanned = false;
					// Stops moving of buttons
					SelectScreenBase.this.isAnimationInProgress = false;
				}
				return false;
			}

			@Override
			public boolean fling(float velocityX, float velocityY, int button) {
				if (!SelectScreenBase.this.isSliderOperating) {
					SelectScreenBase.this.flingVelocity = velocityX * Input.xStretchFactor;
				}
				return false;
			}

			@Override
			public boolean pan(float x, float y, float deltaX, float deltaY) {
				if (!SelectScreenBase.this.isSliderOperating) {
					// Shifts buttons and disables level loading
					SelectScreenBase.this.wasPanned = true;
					SelectScreenBase.this.buttonsShift += deltaX * Input.xStretchFactor;
					SelectScreenBase.this.setButtonsPosition(actualPage, buttonsShift);
					SelectScreenBase.this.moveSlider();

				}
				return false;
			}
		}) {
			@Override
			public boolean touchUp(int x, int y, int pointer, int button) {
				if (!SelectScreenBase.this.isSliderOperating) {
					SelectScreenBase.this.startAnimation();
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
		if (Debug.DEBUG && Debug.drawSelectScreenButtonsArea) {
			Vector2[] points = { new Vector2(buttonsMinXPosition, buttonsMaxYPosition),
					new Vector2(buttonsMaxXPosition, buttonsMaxYPosition),
					new Vector2(buttonsMaxXPosition, buttonsMinYPosition),
					new Vector2(buttonsMinXPosition, buttonsMinYPosition) };
			Debug.drawPolygon(points, Color.RED, 2);
		}
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
					} else if (this.actualPage == this.pagesCount - 1 && isGoingRight && areButtonsLeft)
						this.flingVelocity = Math.abs(this.flingVelocity);
				} else {
					this.flingVelocity = -Math.abs(this.flingVelocity) * Math.signum(this.buttonsShift);
				}

				boolean zeroCrossing = Math.signum(this.lastButtonsShift) != Math.signum(this.buttonsShift);
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
			this.slider.setValue(actualPage / (this.pagesCount - 1) - this.buttonsShift / Constants.WORLD_WIDTH
					/ (this.pagesCount - 1));
	}

	public float GetActualPage() {
		return this.actualPage;
	}

	public float GetButtonsPerPage() {
		return this.buttonsPerPage;
	}
}
