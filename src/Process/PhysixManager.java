package Process;

import java.util.List;

import Entity.Player.Player;
import EntityClasses.Entity;
import LevelDesign.Screen;
import LevelDesign.StageManager;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;


public final class PhysixManager implements SensorEventListener {
	
	public static float BOUNCE_VELOCITY;
	public static float GRAVITY;
	
	public static int DIFFICULTY = 1;
	private static float SCREEN_OFFSET;
	private static float PLAYER_TOP_LIMIT;

	private Context mContext;
	private float mSensi = 6f;
	private StageManager mStageManager;
	
	private static final float[] mSensorValues = new float[3];

	public PhysixManager(Context context, StageManager manager) {
		mContext = context;
		mStageManager = manager;
		reset();
	}

	public static void reset() {
		// Physix
		//GRAVITY = Screen.SCREEN_HEIGHT * 0.001f;
		//BOUNCE_VELOCITY = -Screen.SCREEN_HEIGHT * 0.025f;

		BOUNCE_VELOCITY = -Screen.SCREEN_HEIGHT * 0.025f;
		
		switch (DIFFICULTY) {
		case 0:
			BOUNCE_VELOCITY *= 0.75f;
			break;
		case 2:
			BOUNCE_VELOCITY *= 1.5f;
			break;
		default:
			break;
		}
		
		// Constants
		GRAVITY = getGravity(BOUNCE_VELOCITY);
		PLAYER_TOP_LIMIT = Screen.SCREEN_HEIGHT * 0.3f;
		SCREEN_OFFSET = Screen.SCREEN_HEIGHT * 0.5f;
	}
	
	public static float getGravity (float bounce){
		bounce *= -1;
		float frame = (Screen.SCREEN_HEIGHT * 0.6375f) / bounce;
		return bounce/frame;		
	}

	public void calculatePhysix(EntityManager manager) {

		try {
			for (Entity e : manager.getEntities())
				e.update();
			calculatePlayerPhysix(manager.getPlayer());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void calculatePlayerPhysix(Player player) {
		if (!player.isFreeze()) {
			player.setVelocityX(-mSensorValues[0] * mSensi);

			if (player.centerY() <= SCREEN_OFFSET) {
				float offsetAmnt = 0;
				offsetAmnt = Math.abs((SCREEN_OFFSET - player.centerY()) / 5f);

				mStageManager.getScreen()
						.offsetScreen(0, offsetAmnt);
			}

			if (player.left < -player.width())
				player.offsetTo(Screen.SCREEN_WIDTH, player.top);
			if (player.left > Screen.SCREEN_WIDTH)
				player.offsetTo(-player.width(), player.top);

			if (player.top < PLAYER_TOP_LIMIT)
				player.offsetTo(player.left, PLAYER_TOP_LIMIT);
			player.update();
		}
	}

	public void pausePhysix() {
		SensorManager sensorManager = ((SensorManager) mContext
				.getSystemService(Context.SENSOR_SERVICE));
		List<Sensor> list = sensorManager
				.getSensorList(Sensor.TYPE_ACCELEROMETER);
		Sensor mSensor = list.get(0);

		sensorManager.unregisterListener(PhysixManager.this, mSensor);
	}

	public void resumePhysix() {
		SensorManager sensorManager = ((SensorManager) mContext
				.getSystemService(Context.SENSOR_SERVICE));
		List<Sensor> list = sensorManager
				.getSensorList(Sensor.TYPE_ACCELEROMETER);
		Sensor mSensor = list.get(0);

		sensorManager.registerListener(PhysixManager.this, mSensor,
				SensorManager.SENSOR_DELAY_GAME);
	}

	public void setSensi(float value){
		mSensi = value;
	}
	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {

	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		switch (event.sensor.getType()) {
		case Sensor.TYPE_ACCELEROMETER:
			System.arraycopy(event.values, 0, mSensorValues, 0, 3);
			break;
		default:
			break;
		}
	}
}