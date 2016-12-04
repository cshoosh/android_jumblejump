package Entity.Platforms;

import Entity.Supportables.EntityInfo;
import Entity.Supportables.TypeAttributeList;
import LevelDesign.Screen;
import Process.AllManagers;
import android.os.Bundle;


public class MovablePlatform extends NormalPlatform {

	//Default Value Constants..
	private static final int MOVE_LIMIT_CONST = 50;
	private static final float VELOCITY_CONST = 2;
	private static final int INTERVAL_CONST = 0;
	private static final boolean IS_VERTICAL_CONST = false;
	private static final boolean IS_POSITIVE_CONST = true;
	
	//Key Constants
	private static final String MOVE_LIMIT_KEY = "moveLimitKey";
	private static final String VELOCITY_KEY = "velKey";
	private static final String INTERVAL_KEY = "intervalKey";
	private static final String ISVERTICAL_KEY = "isVertKey";
	private static final String ISPOSITIVE_KEY = "isPositiveKey";
	private static final String INTERVAL_COUNT_KEY = "intervalCountKey";
	private static final String COUNTER_KEY = "countKey";
	
	//Variables
	private int MOVE_LIMIT;
	private int counter, intervalCounter;
	private boolean isGoingPositive = true,isVertical;
	private float mVelocity;
	private int mInterval;

	
	public MovablePlatform(EntityInfo info, AllManagers context) {
		super(info, context);
		try {
			MOVE_LIMIT = Integer.valueOf(info
					.getAttributeValue(TypeAttributeList.movableDis));
		}
		catch(Exception e){
			MOVE_LIMIT = MOVE_LIMIT_CONST;
		}
		try{
			mVelocity = Float.valueOf(info
					.getAttributeValue(TypeAttributeList.movableVel));
		}catch(Exception e){
			mVelocity = VELOCITY_CONST;
		}
		try{
			mInterval = Integer.valueOf(info
					.getAttributeValue(TypeAttributeList.movableInt));
		}catch(Exception e){
			mInterval = INTERVAL_CONST;
		}
		try{
			isVertical = Boolean.valueOf(info.getAttributeValue(TypeAttributeList.movableVer));
			if (isVertical)
				isGoingPositive = false;
		} catch (Exception e) {
			isVertical = IS_VERTICAL_CONST;
		}
		if (isVertical)
			MOVE_LIMIT = (int) ((MOVE_LIMIT /100f) * Screen.SCREEN_HEIGHT);
		else
			MOVE_LIMIT = (int) ((MOVE_LIMIT /100f) * Screen.SCREEN_WIDTH);
	}

	
	@Override
	public void update() {
		super.update();
		if (intervalCounter >= mInterval) {
			if (isGoingPositive)
				if (!isVertical)
					offset(mVelocity, 0);
				else
					offset(0, mVelocity);
			else
				if (!isVertical)
					offset(-mVelocity, 0);
				else
					offset(0, -mVelocity);

			counter += mVelocity;

			if (counter >= MOVE_LIMIT) {
				counter = 0;
				isGoingPositive = !isGoingPositive;
			}
		} else
			intervalCounter++;
	}

	@Override
	protected void writeIntoParcel(Bundle bundle) {
		super.writeIntoParcel(bundle);
		bundle.putFloat(VELOCITY_KEY, mVelocity);
		bundle.putInt(INTERVAL_KEY, mInterval);
		bundle.putBoolean(ISVERTICAL_KEY, isVertical);
		bundle.putInt(MOVE_LIMIT_KEY, MOVE_LIMIT);
		bundle.putBoolean(ISPOSITIVE_KEY, isGoingPositive);
		bundle.putInt(INTERVAL_COUNT_KEY, intervalCounter);
		bundle.putInt(COUNTER_KEY, counter);
	}

	@Override
	protected void readFromParcel(Bundle bundle) {
		super.readFromParcel(bundle);
		mVelocity = bundle.getFloat(VELOCITY_KEY, VELOCITY_CONST);
		mInterval = bundle.getInt(INTERVAL_KEY, INTERVAL_CONST);
		isVertical = bundle.getBoolean(ISVERTICAL_KEY, IS_VERTICAL_CONST);
		MOVE_LIMIT = bundle.getInt(MOVE_LIMIT_KEY, MOVE_LIMIT_CONST);
		isGoingPositive = bundle.getBoolean(ISPOSITIVE_KEY, IS_POSITIVE_CONST);
		
		intervalCounter = bundle.getInt(INTERVAL_COUNT_KEY, 0);
		counter = bundle.getInt(COUNTER_KEY, 0);
	}

}