package MenuFragments;

import Resources.ResourceManager;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zerotwoone.highjump.GameActivity;
import com.zerotwoone.highjump.R;

public class HowToPlayFragment extends DialogFragment implements
		OnClickListener {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setStyle(STYLE_NO_TITLE, getTheme());
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		ViewGroup ret = (ViewGroup) inflater
				.inflate(R.layout.how_to, container);

		ret.findViewById(R.id.button_howto_gotit).setOnClickListener(this);
		ret.findViewById(R.id.button_close_howto).setOnClickListener(this);

		((TextView) ret.findViewById(R.id.text_howto_01))
				.setTypeface(ResourceManager.IMPACT);
		((TextView) ret.findViewById(R.id.text_howto_02))
				.setTypeface(ResourceManager.IMPACT);
		((TextView) ret.findViewById(R.id.text_howto_title))
				.setTypeface(ResourceManager.IMPACT);
		((TextView) ret.findViewById(R.id.button_howto_gotit))
				.setTypeface(ResourceManager.IMPACT);
		return ret;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.button_howto_gotit:
		case R.id.button_close_howto:
			dismiss();
			break;
		default:
			break;
		}

	}

	@Override
	public void onDismiss(DialogInterface dialog) {
		super.onDismiss(dialog);
		((GameActivity)getActivity()).resume();
	}
}
