package Process;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.Vibrator;

public class VibrateManager {

	private Vibrator mVibrator;

	public VibrateManager(Context context) {
		mVibrator = (Vibrator) context
				.getSystemService(Context.VIBRATOR_SERVICE);
	}

	public void vibrate(VibrateAction action) {
		if (canVibrate()) {
			switch (action) {
			case PlayerDead:
				mVibrator.vibrate(50);
				break;
			case PlayerShocked:
				long[] pattern = {0,50,50,100};
				mVibrator.vibrate(pattern, -1);
				break;
			default:
				break;
			}
		}
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	public boolean canVibrate() {

		if (mVibrator != null)
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
				return mVibrator.hasVibrator();
			else
				return true;

		return false;
	}

	public enum VibrateAction {
		PlayerDead, PlayerShocked;
	}
}
