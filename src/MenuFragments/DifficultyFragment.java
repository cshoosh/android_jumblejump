package MenuFragments;

import Entity.Supportables.Characters;
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

import com.zerotwoone.highjump.MainMenuActivity;
import com.zerotwoone.highjump.R;

public class DifficultyFragment extends DialogFragment implements
		OnClickListener {

	Characters selected;
	int difficulty = -1;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setStyle(STYLE_NO_TITLE, getTheme());
		selected = Characters.valueOf(getArguments().getString("Character"));
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View ret = inflater.inflate(R.layout.difficulty, container);

		((TextView)ret.findViewById(R.id.text_difficulty)).setTypeface(ResourceManager.IMPACT);
		Button[] buttons = { (Button) ret.findViewById(R.id.button_easy),
				(Button) ret.findViewById(R.id.button_med),
				(Button) ret.findViewById(R.id.button_hard), };

		for (Button but : buttons) {
			but.setTypeface(ResourceManager.IMPACT);
			but.setOnClickListener(this);
		}
		return ret;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.button_easy:
			difficulty = 0;
			break;
		case R.id.button_med:
			difficulty = 1;
			break;
		case R.id.button_hard:
			difficulty = 2;
			break;
		default:
			break;
		}

		dismiss();
	}

	@Override
	public void onDismiss(DialogInterface dialog) {
		super.onDismiss(dialog);
		((MainMenuActivity) getActivity()).resume();
		if (difficulty != -1)
			((MainMenuActivity) getActivity()).startgame(difficulty, selected);
		
		
	}
}
