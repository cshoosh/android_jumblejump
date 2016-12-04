package Process;

import Resources.ResourceManager;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.preference.PreferenceManager;
import android.util.SparseIntArray;

import com.zerotwoone.highjump.R;

public class SoundManager {

	// Constants
	private static int VOLUME = 1;
	private static final int SIMULTANEOUS = 5;

	private static final int[] GameSounds = { R.raw.coin, R.raw.crack,
			R.raw.doublejump, R.raw.drop, R.raw.fire, R.raw.jump,
			R.raw.player_toasted, R.raw.balloon, R.raw.die, R.raw.shield };

	private SoundPool pool;

	private MediaPlayer mMainMenuMusic, mGamePlayMusic;

	private boolean isFXEnabled;
	private boolean isMusicEnabled;

	private Context mContext;

	private SparseIntArray soundData = new SparseIntArray();
	private SparseIntArray mPlayRecord = new SparseIntArray();

	public SoundManager(Context context) {
		init(context);
	}

	public void init(Context context) {
		// Initialization

		AudioManager manager = (AudioManager) context
				.getSystemService(Context.AUDIO_SERVICE);
		mContext = context;
		VOLUME = manager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);

		// Music Initialization
		mMainMenuMusic = MediaPlayer.create(context, R.raw.munu01);
		mMainMenuMusic.setLooping(true);

		mGamePlayMusic = MediaPlayer.create(context, R.raw.gameplay2);
		mGamePlayMusic.setLooping(true);

		// Check Initialization
		checkSoundEnable(context);
	}

	public void resetSoundPool() {

		releaseSoundPool();
		pool = new SoundPool(SIMULTANEOUS, AudioManager.STREAM_MUSIC, 0);
		// Setting sounds
		for (int i = 0; i < GameSounds.length; i++)
			soundData.put(GameSounds[i], pool.load(mContext, GameSounds[i], 1));

	}

	public void checkSoundEnable(Context context) {

		isFXEnabled = PreferenceManager
				.getDefaultSharedPreferences(context)
				.getBoolean(
						ResourceManager
								.translateID(R.string.key_sound, context),
						true);

		isMusicEnabled = PreferenceManager
				.getDefaultSharedPreferences(context)
				.getBoolean(
						ResourceManager
								.translateID(R.string.key_music, context),
						true);

	}

	public int play(int soundID) {
		if (pool != null && isFXEnabled) {
			// Get PlayID from hash map
			int id = soundData.get(soundID);

			// Check if sound is already played and get stream id
			if (mPlayRecord.get(soundID) != 0)
				pool.stop(mPlayRecord.get(soundID));

			int ret = pool.play(id, getSoundVolume(soundID),
					getSoundVolume(soundID), 0, 0, 1);

			// Replaces previous entry
			mPlayRecord.put(soundID, ret);
			return ret;
		}
		return 0;
	}

	public void pause(SoundType type) {
		switch (type) {
		case Game:
			if (pool != null)
				pool.autoPause();
			if (mGamePlayMusic != null)
				mGamePlayMusic.pause();
			break;
		case Menu:
			if (mMainMenuMusic != null)
				mMainMenuMusic.pause();
			break;
		default:
			break;
		}
	}

	public void resume(SoundType type) {

		switch (type) {
		case Game:
			if (pool != null && isFXEnabled)
				pool.autoResume();
			if (mGamePlayMusic != null && isMusicEnabled)
				mGamePlayMusic.start();
			break;
		case Menu:
			if (mMainMenuMusic != null && isMusicEnabled)
				mMainMenuMusic.start();
			break;
		
		default:
			break;
		}

	}

	public void setMusicEnabled(boolean value) {
		isMusicEnabled = value;
	}

	public void setSoundEnabled(boolean value) {
		isFXEnabled = value;
	}

	public void destroy() {
		if (mMainMenuMusic != null) {
			mMainMenuMusic.release();
			mMainMenuMusic = null;
		}
		if (mGamePlayMusic != null) {
			mGamePlayMusic.release();
			mGamePlayMusic = null;
		}
		releaseSoundPool();
	}

	public void releaseSoundPool() {
		if (pool != null) {
			pool.release();
			pool = null;
		}
	}
	
	public enum SoundType {
		Game, Menu
	}

	public static float getSoundVolume(int id) {
		switch (id) {
		case R.raw.player_toasted:
			return 0.4f;
		case R.raw.coin:
			return 0.25f;
		case R.raw.gameplay2:
			return 0.75f;
		case R.raw.fire:
			return 0.5f;
		case R.raw.jump:
			return 0.5f;

		default:
			return VOLUME;
		}
	}
}