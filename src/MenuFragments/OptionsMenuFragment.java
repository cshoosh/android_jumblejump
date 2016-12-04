package MenuFragments;

import Process.AllManagers;
import Process.SoundManager.SoundType;
import Resources.ResourceManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.zerotwoone.highjump.MainMenuActivity;
import com.zerotwoone.highjump.R;


public class OptionsMenuFragment extends DialogFragment implements OnClickListener {

	AllManagers mManager;
		
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mManager = MainMenuActivity.getManager();
		setStyle(STYLE_NO_TITLE, getTheme());
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		ViewGroup main = (ViewGroup) inflater.inflate(R.layout.options,
				container);

		main.findViewById(R.id.button_close_options).setOnClickListener(this);
		if (mManager.getPlayServiceManager().isConnected())
			main.findViewById(R.id.button_signout).setEnabled(true);
		else
			main.findViewById(R.id.button_signout).setEnabled(false);

		boolean musicKey = PreferenceManager.getDefaultSharedPreferences(
				getActivity()).getBoolean(
				ResourceManager.translateID(R.string.key_music, getActivity()),
				true);
		boolean soundKey = PreferenceManager.getDefaultSharedPreferences(
				getActivity()).getBoolean(
				ResourceManager.translateID(R.string.key_sound, getActivity()),
				true);

		((CheckBox) main.findViewById(R.id.check_music)).setChecked(musicKey);
		((CheckBox) main.findViewById(R.id.check_sound)).setChecked(soundKey);

		((TextView) main.findViewById(R.id.text_options_title))
				.setTypeface(ResourceManager.IMPACT);
		((TextView) main.findViewById(R.id.text_sound))
				.setTypeface(ResourceManager.IMPACT);
		((TextView) main.findViewById(R.id.text_music))
				.setTypeface(ResourceManager.IMPACT);

		((Button) main.findViewById(R.id.button_rate))
				.setTypeface(ResourceManager.IMPACT);
		((Button) main.findViewById(R.id.button_signout))
				.setTypeface(ResourceManager.IMPACT);

		main.findViewById(R.id.button_signout).setOnClickListener(this);
		main.findViewById(R.id.button_rate).setOnClickListener(this);
		main.findViewById(R.id.check_music).setOnClickListener(this);
		main.findViewById(R.id.check_sound).setOnClickListener(this);

		return main;
	}

	@Override
	public void onClick(View v) {
		boolean isChecked = false;
		if (v instanceof CheckBox)
			isChecked = ((CheckBox) v).isChecked();
		switch (v.getId()) {
		case R.id.button_signout:
			mManager.getPlayServiceManager().SignOut();
			v.setEnabled(false);
			break;
		case R.id.button_rate:
			Intent rateIntent = new Intent(
					Intent.ACTION_VIEW,
					Uri.parse("http://play.google.com/store/apps/details?id=" +
							"com.zerotwoone.highjump"));
			startActivity(rateIntent);
			break;
		case R.id.check_sound:
			PreferenceManager
					.getDefaultSharedPreferences(getActivity())
					.edit()
					.putBoolean(
							ResourceManager.translateID(R.string.key_sound,
									getActivity()), isChecked).commit();

			checkSoundnMusic(1, isChecked);
			break;
		case R.id.check_music:
			PreferenceManager
					.getDefaultSharedPreferences(getActivity())
					.edit()
					.putBoolean(
							ResourceManager.translateID(R.string.key_music,
									getActivity()), isChecked).commit();

			checkSoundnMusic(0, isChecked);
			break;
		case R.id.button_close_options:
			dismiss();
			break;
		default:
			break;
		}
	}

	private void checkSoundnMusic(int type, boolean enabled) {
		if (type == 0){
			mManager.getSoundManager().setMusicEnabled(enabled);
			if (enabled)
				mManager.getSoundManager().resume(SoundType.Menu);
			else
				mManager.getSoundManager().pause(SoundType.Menu);
		}
		else
			mManager.getSoundManager().setSoundEnabled(enabled);
		
	}
}
