package com.zerotwoone.highjump;

import Entity.Player.Player;
import Entity.Supportables.Characters;
import LevelDesign.Screen;
import MenuFragments.HowToPlayFragment;
import MenuFragments.PauseMenuFragment;
import Process.GameLoop;
import Process.PhysixManager;
import Resources.ResourceManager;
import android.app.Activity;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.Window;
import android.view.WindowManager;

public class GameActivity extends FragmentActivity {

	public static final String TAG_DEAD_MENU = "Dead Menu";
	public static final String TAG_PAUSE_MENU = "Pause";
	public static final String TAG_OPTIONS_MENU = "Options";
	public static final String TAG_HOWTO = "HowTo";
	public static final String TAG_DIFFICULTY = "Difficulty";

	public static final int PAUSE_HOWTO = 1;
	public static final int PAUSE_DEAD = 2;
	public static final int PAUSE_GAME = 0;

	// static final String AD_UNIT_ID =
	// "ca-app-pub-4477487517732949/2978001509";
	public static final String AD_UNIT_ID_INT = "ca-app-pub-4477487517732949/7480423103";

	private static Activity mActivity;
	private GameLoop loop;

	// private AdView mAdView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().addFlags(
				WindowManager.LayoutParams.FLAG_FULLSCREEN
						| WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

		final SurfaceView view = new SurfaceView(this);
		setContentView(view);

		final Characters theme = Characters.valueOf(getIntent().getExtras()
				.getString("Theme"));

		PhysixManager.DIFFICULTY = getIntent().getExtras().getInt("Difficulty",
				1);
		mActivity = this;
		try {

			loop = new GameLoop(view.getHolder(), mActivity,
					MainMenuActivity.getManager(), theme);
			loop.start();
			if (! PreferenceManager.getDefaultSharedPreferences(this)
					.getBoolean(
							ResourceManager.translateID(
									R.string.key_first_play, this), false)) {

				new HowToPlayFragment().show(getSupportFragmentManager(),
						TAG_HOWTO);
				PreferenceManager
						.getDefaultSharedPreferences(this)
						.edit()
						.putBoolean(
								ResourceManager.translateID(
										R.string.key_first_play, this), true)
						.commit();
				pause(PAUSE_HOWTO);
			}
		} catch (Exception e) {
			e.printStackTrace();
			quit();
			return;
		}

	}

	public void pause(int reason) {

		if (!loop.isPaused()) {
			try {

				loop.pauseGame();

				if (!isFinishing() && reason == PAUSE_GAME)
					new PauseMenuFragment().show(getSupportFragmentManager(),
							TAG_PAUSE_MENU);
			} catch (Exception e) {
				e.printStackTrace();
				quit();
			}
		}
	}

	public void resume() {
		try {
			loop.resumeGame();
		} catch (Exception e) {
			e.printStackTrace();
			quit();
		}
	}

	public void quit() {
		try {
			loop.KillGame();
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (!isFinishing())
			finish();
	}

	@Override
	protected void onPause() {
		super.onPause();
		pause(PAUSE_GAME);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		quit();
	}

	public void restartGame() {
		try {
			loop.restart();
			loop.resumeGame();
		} catch (Exception e) {
			e.printStackTrace();
			quit();
		}

	}

	@Override
	public void onBackPressed() {
		pause(PAUSE_GAME);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			Player player = MainMenuActivity.getManager().getEntityManager()
					.getPlayer();
			if (player != null && !player.isFreeze()) {
				float centerScreen = Screen.SCREEN_WIDTH / 2f;
				player.fire((event.getX() - centerScreen) / centerScreen);
			}
		}

		/*
		 * if (event.getAction() == MotionEvent.ACTION_MOVE){
		 * GameLoop.getStageManager().getScreen().offsetScreen(0, mY -
		 * event.getY()); }
		 * 
		 * mY = event.getY();
		 */
		return super.onTouchEvent(event);

	}

	// float mY;

	public static final Activity getActivity() {
		return mActivity;
	}

}
