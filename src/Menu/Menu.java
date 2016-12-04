package Menu;

import java.io.IOException;

import Entity.Supportables.BitmapDrawableObject;
import Entity.Supportables.DrawableObject;
import LevelDesign.Screen;
import Menu.TouchableDrawable.TouchableTypes;
import Process.AllManagers;
import Process.SoundManager;
import Process.SoundManager.SoundType;
import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceView;

import com.zerotwoone.highjump.R;

public class Menu extends SurfaceView implements Runnable {

	// Supportive Loop variables
	private static final int MAX_FPS = 50;
	private static final float FPS_COUNT = 1000f / MAX_FPS;

	private boolean isAlive = true;
	private boolean isPaused;
	private LoadingScreen mLoadingScreen;
	
	// Characters
	private CharacterSelection slct;

	// Background Constants
	private DrawableObject mBackground, mGrass;
	
	private ViewHandler mViewHandler;
	private TouchableDrawable mNewGame, mOptions, mAchieve, mHighScore,
			mRemoveAds;

	public Menu(Context context) {
		super(context);
		init(context);
	}

	public Menu(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	public Menu(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	private void init(Context context) {

		mLoadingScreen = new LoadingScreen.Builder(context).build();
		//Character Selection
		try {
			slct = new CharacterSelection(context);
		} catch (IOException e) {
			e.printStackTrace();
		}
		// Backround Drawable
		mBackground = new BitmapDrawableObject(context, R.drawable.menu_bg);
		mBackground.setBounds(0, 0, Screen.SCREEN_WIDTH, Screen.SCREEN_HEIGHT);

		mGrass = new BitmapDrawableObject(context, R.drawable.menu_grass);
		mGrass.setBounds(0, 0, Screen.SCREEN_WIDTH,
				Screen.SCREEN_HEIGHT * 0.203125f);

		// Buttons And Touchables
		mViewHandler = new ViewHandler();

		float buttonWidth = 0.46805f * Screen.SCREEN_WIDTH;
		float buttonHeight = 0.14765f * Screen.SCREEN_HEIGHT;

		float adsWidth = Screen.SCREEN_WIDTH;
		float adsHeight = 0.0734375f * Screen.SCREEN_HEIGHT;

		mNewGame = new TouchableDrawable(R.drawable.new_press,
				R.drawable.new_unpressed, context, TouchableTypes.NewGame);
		mHighScore = new TouchableDrawable(R.drawable.highscore_press,
				R.drawable.highscore, context, TouchableTypes.HighScore);
		mAchieve = new TouchableDrawable(R.drawable.achieve_press,
				R.drawable.achieve, context, TouchableTypes.Achievement);
		mOptions = new TouchableDrawable(R.drawable.options_press,
				R.drawable.options, context, TouchableTypes.Options);
		mRemoveAds = new TouchableDrawable(R.drawable.removeads_pressed,
				R.drawable.removeads_unpressed, context,
				TouchableTypes.RemoveAds);

		mNewGame.setBounds(0, 0, buttonWidth, buttonHeight);
		mHighScore.setBounds(0, 0, buttonWidth, buttonHeight);
		mAchieve.setBounds(0, 0, buttonWidth, buttonHeight);
		mOptions.setBounds(0, 0, buttonWidth, buttonHeight);
		mRemoveAds.setBounds(0, 0, adsWidth, adsHeight);

		mViewHandler.addTouchable(mNewGame, 0.0473f * Screen.SCREEN_WIDTH,
				0.4429f * Screen.SCREEN_HEIGHT);
		mViewHandler.addTouchable(mAchieve, 0.5098f * Screen.SCREEN_WIDTH,
				0.6711f * Screen.SCREEN_HEIGHT);
		mViewHandler.addTouchable(mOptions, 0.0445f * Screen.SCREEN_WIDTH,
				0.6117f * Screen.SCREEN_HEIGHT);
		mViewHandler.addTouchable(mHighScore, 0.5111f * Screen.SCREEN_WIDTH,
				0.4828f * Screen.SCREEN_HEIGHT);
		mViewHandler.addTouchable(mRemoveAds, 0, 0.8359f * Screen.SCREEN_HEIGHT);

	}

	@Override
	public void run() {

		while (isAlive) {
			long mPrevTime;
			long diff;
			long sleepTime;

			while (isAlive) {

				if (!isPaused) {
					Canvas c = null;
					try {
						c = getHolder().lockCanvas();
						mPrevTime = System.currentTimeMillis();
						Update(c);
						diff = System.currentTimeMillis() - mPrevTime;
						sleepTime = (long) (FPS_COUNT - diff);

						if (sleepTime >= 0) {
							try {
								Thread.sleep(sleepTime);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}
					} catch (NullPointerException e) {
						e.printStackTrace();
					} finally {
						if (c != null)
							getHolder().unlockCanvasAndPost(c);
					}
				} else {
					try {
						synchronized (this) {
							wait(1000);
						}
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}

			}
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (mLoadingScreen.isLoaded()) 			
			return slct.onTouch(this, event) || mViewHandler.OnTouch(event);
		
		return false;
	}

	public void setLoaded(AllManagers manager) {		
		if (manager != null) 
			manager.getSoundManager().resume(SoundType.Menu);
		
		mLoadingScreen.setLoaded();
	}

	private void Update(Canvas c) throws NullPointerException {
		if (c != null) {
			if (mLoadingScreen.isLoaded()) {

				mBackground.draw(c, 0, 0, null, null);
				mViewHandler.draw(c);
				mGrass.draw(c, 0, Screen.SCREEN_HEIGHT - mGrass.getHeight(),
						null, null);
				slct.draw(c);
			} else {
				mLoadingScreen.draw(c);
			}

		}
	}

	public void destroy() {
		isAlive = false;
		synchronized (this) {
			notify();
		}
	}

	public void pauseMenu(SoundManager manager) {
		isPaused = true;
		
		if (manager != null)
			manager.pause(SoundType.Menu);
		
	}

	public void resumeMenu(SoundManager manager) {
		isPaused = false;

		if (manager != null)
			manager.resume(SoundType.Menu);
		
		synchronized (this) {
			notifyAll();
		}

	}

	public CharacterSelection getCharacterSelection() {
		return slct;
	}
	public ViewHandler getViewHandler(){
		return mViewHandler;
	}
}
