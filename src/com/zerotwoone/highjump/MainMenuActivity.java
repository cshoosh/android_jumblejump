package com.zerotwoone.highjump;

import Entity.Interfaces.OnMenuButtonClickListener;
import Entity.Supportables.Characters;
import LevelDesign.Screen;
import Menu.CharacterSelection;
import Menu.CharacterSlot;
import Menu.Menu;
import Menu.TouchableDrawable;
import MenuFragments.DifficultyFragment;
import MenuFragments.OptionsMenuFragment;
import Process.AllManagers;
import Process.InAppStore;
import Process.PhysixManager;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Point;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.games.GamesActivityResultCodes;

public class MainMenuActivity extends FragmentActivity implements
		OnMenuButtonClickListener, OnClickListener {

	private Menu menu;

	public static final int REQUEST_NEW_GAME = 1;
	public static final int REQUEST_ACHIEVEMENT = 2;
	public static final int REQUEST_LEADERBOARD = 3;
	public static final int REQUEST_RESOLUTION = 4;
	public static final int REQUEST_MAIN_LAUNCH = 1005;

	private static AllManagers manager;
	private static InterstitialAd mAd;

	@SuppressWarnings("deprecation")
	@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

		WindowManager mWindowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);

		Point outSize = new Point();
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2)
			mWindowManager.getDefaultDisplay().getSize(outSize);
		else {
			outSize.x = mWindowManager.getDefaultDisplay().getWidth();
			outSize.y = mWindowManager.getDefaultDisplay().getHeight();
		}

		Screen.setScreenDimension(outSize);
		PhysixManager.reset();
		setContentView(R.layout.selection);
		menu = (Menu) findViewById(R.id.menu);

		new Thread(new Runnable() {

			@Override
			public void run() {
				new Thread(menu, "MainMenu").start();

				findViewById(R.id.button_facebook).setOnClickListener(
						MainMenuActivity.this);
				findViewById(R.id.button_twitter).setOnClickListener(
						MainMenuActivity.this);

				try {
					manager = new AllManagers(MainMenuActivity.this);
					manager.getInAppStore().setUpgraded(
							PreferenceManager.getDefaultSharedPreferences(
									MainMenuActivity.this).getBoolean(
									InAppStore.UPGRADE, false));
				} catch (final Exception e) {
					e.printStackTrace();

					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							Toast.makeText(
									MainMenuActivity.this,
									e.getMessage()
											+ " : Application crashed"
											+ "\nPlease contact developer for support",
									Toast.LENGTH_LONG).show();
							finish();
						}
					});

					return;
				}

				((SignInButton) findViewById(R.id.sign_in_button)).setOnClickListener(MainMenuActivity.this);

				runOnUiThread(mSetup);
			}
		}, "Initialization").start();

	}

	@Override
	protected void onPause() {
		super.onPause();
		if (manager != null)
			menu.pauseMenu(manager.getSoundManager());
	}

	@Override
	protected void onResume() {
		super.onResume();
		resume();
	}
	
	public void resume(){
		if (manager != null)
			menu.resumeMenu(manager.getSoundManager());
	}

	@Override
	protected void onDestroy() {

		super.onDestroy();

		// Release Resources
		menu.destroy();
		if (manager != null)
			manager.destroy();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (manager != null && manager.getInAppStore() != null)
			manager.getInAppStore().onActivityResult(requestCode, resultCode,
					data);
		switch (resultCode) {
		case RESULT_OK:
			switch (requestCode) {
			case REQUEST_RESOLUTION:
				manager.getPlayServiceManager().ConnectToService();
				break;

			default:
				break;
			}
			break;
		case GamesActivityResultCodes.RESULT_SIGN_IN_FAILED:
			Toast.makeText(getApplicationContext(),
					"Sign In failed, check network " + "connection",
					Toast.LENGTH_SHORT).show();

			break;
		case GamesActivityResultCodes.RESULT_RECONNECT_REQUIRED:
			manager.getPlayServiceManager().getClient().reconnect();
			break;

		default:
			break;
		}
	}

	@Override
	public void OnMenuButtonClicked(TouchableDrawable drawable) {
		switch (drawable.getID()) {
		case NewGame:
			Characters main = Characters.Bandar;

			for (CharacterSlot slot : menu.getCharacterSelection().getArray()) {
				if (slot.getIndex() == CharacterSelection.CENTER_INDEX) {
					main = slot.getCharacter();
					if (!slot.isEnabled()) {
						new AlertDialog.Builder(this).setMessage("Character not available!!\nRemove Ads to unlock..")
							.setTitle("Warning").setPositiveButton("OK", new DialogInterface.OnClickListener() {
								
								@Override
								public void onClick(DialogInterface dialog, int which) {
									dialog.dismiss();
								}
							}).create().show();
						return;
					}
				}
			}
			
			if(getSupportFragmentManager().findFragmentByTag(GameActivity.TAG_DIFFICULTY) == null){
				DifficultyFragment diff = new DifficultyFragment();
				Bundle arg = new Bundle();
				arg.putString("Character", main.toString());
				diff.setArguments(arg);
				diff.show(getSupportFragmentManager(), GameActivity.TAG_DIFFICULTY);
			}
			//startgame(1, main);
			
			break;
		case Achievement:
			manager.getPlayServiceManager().startAchievement();
			break;
		case HighScore:
			manager.getPlayServiceManager().startLeaderBoard();
			break;
		case Options:
			Fragment menu = getSupportFragmentManager().findFragmentByTag(
					GameActivity.TAG_OPTIONS_MENU);
			if (menu == null)
				new OptionsMenuFragment().show(getSupportFragmentManager(),
						GameActivity.TAG_OPTIONS_MENU);
			break;
		case RemoveAds:
			manager.getInAppStore().makePurchase(InAppStore.UPGRADE,
					InAppStore.TYPE_INAPP);
			break;
		default:
			break;
		}
	}

	public static InterstitialAd getAd() {
		return mAd;
	}

	public static void reloadAd(Context context) {
		if (mAd != null && !manager.getInAppStore().isUpgraded())
			mAd.loadAd(new AdRequest.Builder().addTestDevice(
					"883921C29483F7D030D5A5E27455B425").build());
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.button_facebook:
			Intent myIntent = new Intent(Intent.ACTION_VIEW,
					Uri.parse("http://www.facebook.com/jumblejump"));
			startActivity(myIntent);
			break;
		case R.id.button_twitter:
			Intent myIntentTwitter = new Intent(Intent.ACTION_VIEW,
					Uri.parse("http://twitter.com/021labs"));
			startActivity(myIntentTwitter);
			break;
		case R.id.sign_in_button:
			if (manager != null && manager.getPlayServiceManager() != null)
				manager.getPlayServiceManager().SignIn();
			break;
		default:
			break;
		}
	}

	private Runnable mSetup = new Runnable() {

		@Override
		public void run() {
			findViewById(R.id.sign_in_button).setVisibility(View.VISIBLE);
			findViewById(R.id.button_facebook).setVisibility(View.VISIBLE);
			findViewById(R.id.button_twitter).setVisibility(View.VISIBLE);

			// Init Google Play Service Manager..

			manager.getPlayServiceManager().ConnectToService();
			if (GooglePlayServicesUtil
					.isGooglePlayServicesAvailable(getApplicationContext()) == ConnectionResult.SUCCESS) {
				mAd = new InterstitialAd(getApplicationContext());
				mAd.setAdUnitId(GameActivity.AD_UNIT_ID_INT);

				reloadAd(getApplicationContext());
			} else
				GooglePlayServicesUtil
						.getErrorDialog(
								GooglePlayServicesUtil
										.isGooglePlayServicesAvailable(getApplicationContext()),
								MainMenuActivity.this, 1);
			menu.setLoaded(manager);
		}
	};

	public void startgame(int difficulty, Characters main){
		menu.pauseMenu(manager.getSoundManager());		
		Intent intent = new Intent(this, GameActivity.class);
		intent.putExtra("Theme", main.toString());
		intent.putExtra("Difficulty", difficulty);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivityForResult(intent, REQUEST_NEW_GAME);
	}
	public static AllManagers getManager() {
		return manager;
	}

}
