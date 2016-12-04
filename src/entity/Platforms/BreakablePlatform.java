package Entity.Platforms;

import Entity.Player.Player;
import Entity.Supportables.EntityInfo;
import EntityClasses.Entity;
import EntityClasses.Platform;
import Process.AllManagers;
import Resources.ResourceName;
import Resources.ResourcePlatform;
import android.graphics.Canvas;
import android.os.Bundle;

import com.zerotwoone.highjump.R;

public class BreakablePlatform extends Platform{
	public BreakablePlatform(EntityInfo info, AllManagers context) {
		super(info, context);
	}

	private static final String ROTATION_KEY = "breakPlatformRotationKey";
	private static final String IS_ROTATION_KEY = "breakPlatformIsRotationKey";
	
	private float mRotation;
	private boolean isRotating;

	@Override
	public void CollideTop(Entity who) {
		if (who instanceof Player){
			if (!isRotating)
				mManagers.getSoundManager().play(R.raw.crack);
			isRotating = true;
		}
	}

	@Override
	public void CollideInside(Entity who) {
		
	}

	@Override
	public void CollideBottom(Entity who) {
		
	}
	
	@Override
	public void update() {
		super.update();
		if (isRotating){
			mRotation += 6;
			if (mRotation >= 85)
				mRotation = 85;
		}
	}

	@Override
	public void draw(Canvas canvas) {
		//Draw Left Side
		setLevel(0);
		canvas.save();
		canvas.rotate(mRotation, left, centerY());
		super.draw(canvas);
		canvas.restore();
		
		//Draw Right Side
		setLevel(1);
		canvas.save();
		canvas.rotate(-mRotation, right, centerY());
		super.draw(canvas);
		canvas.restore();
	}
	@Override
	protected void init() {
		addDrawable(ResourceName.Platform, ResourcePlatform.BreakA.name());
		addDrawable(ResourceName.Platform, ResourcePlatform.BreakB.name());
	}

	@Override
	protected void writeIntoParcel(Bundle bundle) {
		bundle.putFloat(ROTATION_KEY, mRotation);
		bundle.putBoolean(IS_ROTATION_KEY, isRotating);
	}

	@Override
	protected void readFromParcel(Bundle bundle) {
		mRotation = bundle.getFloat(ROTATION_KEY, 0);
		isRotating = bundle.getBoolean(IS_ROTATION_KEY, false);
	}

}