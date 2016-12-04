package MenuFragments;

import Resources.ResourceManager;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.zerotwoone.highjump.GameActivity;
import com.zerotwoone.highjump.R;

public class PauseMenuFragment extends DialogFragment implements
		OnClickListener {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setStyle(STYLE_NO_TITLE, getTheme());
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View menu = inflater.inflate(R.layout.pause_menu, container);

		menu.findViewById(R.id.button_close_pause).setOnClickListener(this);
		((TextView) menu.findViewById(R.id.text_gamepaused))
				.setTypeface(ResourceManager.IMPACT);

		((Button) menu.findViewById(R.id.button_resume))
				.setTypeface(ResourceManager.IMPACT);
		((Button) menu.findViewById(R.id.button_quit_pause))
				.setTypeface(ResourceManager.IMPACT);

		menu.findViewById(R.id.button_resume).setOnClickListener(this);
		menu.findViewById(R.id.button_quit_pause).setOnClickListener(this);
		return menu;
	}

	@Override
	public void onDismiss(DialogInterface dialog) {
		super.onDismiss(dialog);
		((GameActivity)getActivity()).resume();
		//	GameActivity.getGameLooper().resumeGame();
		
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.button_resume:
		case R.id.button_close_pause:
			dismiss();
			break;
		case R.id.button_quit_pause:
			((GameActivity)getActivity()).quit();
			dismiss();
			break;
		default:
			break;
		}
	}
}
