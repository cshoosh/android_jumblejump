package MenuFragments;

import Process.PhysixManager;
import Resources.ResourceManager;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.plus.Plus;
import com.zerotwoone.highjump.GameActivity;
import com.zerotwoone.highjump.MainMenuActivity;
import com.zerotwoone.highjump.R;

public class DeadScreenFragment extends DialogFragment implements
		OnClickListener {

	private static final String KEY_PLAYER_NAME = "nameKey";
	private boolean isError;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setStyle(STYLE_NO_TITLE, getTheme());
		setCancelable(false);
	}

	@Override
	public void onStart() {
		super.onStart();
		if (MainMenuActivity.getAd() != null)
			if (MainMenuActivity.getAd().isLoaded())
				//if (!MainMenuActivity.getManager().getInAppStore().isUpgraded())
					MainMenuActivity.getAd().show();
		if (isError)
			dismiss();

	}

	@Override
	public View onCreateView(final LayoutInflater inflater,
			ViewGroup container, Bundle savedInstanceState) {
		final View menu = inflater.inflate(R.layout.dead_menu, container);

		try {
			((Button) menu.findViewById(R.id.button_quit_dead))
					.setTypeface(ResourceManager.IMPACT);
			((Button) menu.findViewById(R.id.button_replay))
					.setTypeface(ResourceManager.IMPACT);
			((TextView) menu.findViewById(R.id.text_score))
					.setTypeface(ResourceManager.IMPACT);
			((TextView) menu.findViewById(R.id.text_name))
					.setTypeface(ResourceManager.IMPACT);

			menu.findViewById(R.id.button_quit_dead).setOnClickListener(this);
			menu.findViewById(R.id.button_replay).setOnClickListener(this);

			TextView scoreView = ((TextView) menu.findViewById(R.id.text_score));
			TextView nameView = (TextView) menu.findViewById(R.id.text_name);

			scoreView.setText(String.valueOf(MainMenuActivity.getManager()
					.getEntityManager().getPlayer().getScore()));

			if (MainMenuActivity.getManager().getPlayServiceManager()
					.isConnected()) {
				nameView.setEnabled(false);

				String name = Plus.PeopleApi.getCurrentPerson(
						MainMenuActivity.getManager().getPlayServiceManager()
								.getClient()).getDisplayName();
				if (name.length() > 12)
					name = name.substring(0, 12);
				nameView.setText(name);
			} else {
				nameView.setOnClickListener(this);
				nameView.setEnabled(true);
				nameView.setText(PreferenceManager.getDefaultSharedPreferences(
						getActivity()).getString(KEY_PLAYER_NAME, "Player"));
			}
		} catch (NullPointerException e) {
			e.printStackTrace();
			isError = true;
		}
		return menu;
	}

	@Override
	public void onDismiss(DialogInterface dialog) {
		super.onDismiss(dialog);

		try {
			MainMenuActivity.reloadAd(getActivity());
			new Thread(new Runnable() {

				@Override
				public void run() {
					if (getArguments() != null)
						MainMenuActivity.getManager().getPlayServiceManager()
								.submitAchievements(getArguments(), PhysixManager.DIFFICULTY);
				}
			}, "AchievementSubmit").start();
			
		} catch (NullPointerException e) {
			e.printStackTrace();
			isError = true;
		}

		if (isError)
			((GameActivity)getActivity()).quit();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.button_quit_dead:
			((GameActivity)getActivity()).quit();
			dismiss();
			break;
		case R.id.button_replay:
			GameActivity activity = (GameActivity) getActivity();
			activity.restartGame();
			dismiss();
			break;
		case R.id.text_name:
			getTextDialog(getActivity()).show();
			break;
		default:
			break;
		}
	}

	private AlertDialog getTextDialog(final Context context) {
		final EditText input = new EditText(context);

		return new AlertDialog.Builder(context)
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						TextView nameView = (TextView) getView().findViewById(
								R.id.text_name);
						String value = input.getText().toString();
						if (value.length() > 12)
							value = value.substring(0, 12);
						nameView.setText(value);

						PreferenceManager.getDefaultSharedPreferences(context)
								.edit().putString(KEY_PLAYER_NAME, value)
								.commit();
						dialog.dismiss();
					}
				})
				.setNegativeButton("Cancel",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								dialog.cancel();
							}
						}).setView(input).setMessage("Enter Name").create();
	}
}
