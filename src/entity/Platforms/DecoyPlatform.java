package Entity.Platforms;

import java.util.Random;

import Entity.Interfaces.Fallable;
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

public class DecoyPlatform extends Platform implements Fallable{
	
	private static final int RANDOMNESS = 8;
	private static final Random mRandom = new Random();
	private boolean isFalling = false;
	
	public DecoyPlatform(EntityInfo info, AllManagers context) {
		super(info, context);
	}
	@Override
	public void draw(Canvas canvas) {
		canvas.save();
		if (!isFalling())
			canvas.translate(mRandom.nextInt(RANDOMNESS), mRandom.nextInt(RANDOMNESS));
		super.draw(canvas);
		canvas.restore();
	}
	
	@Override
	public void CollideTop(Entity who) {
		if (who instanceof Player){
			
			if (!isFalling())
				mManagers.getSoundManager().play(R.raw.drop);
			setFalling(true);
		}
	}

	@Override
	public void CollideInside(Entity who) {
		
	}

	@Override
	public void CollideBottom(Entity who) {
		
	}

	@Override
	protected void init() {
		addDrawable(ResourceName.Platform, ResourcePlatform.DEFAULT.name());
	}

	@Override
	protected void writeIntoParcel(Bundle bundle) {
		bundle.putBoolean(FALL_KEY, isFalling);
	}

	@Override
	protected void readFromParcel(Bundle bundle) {
		isFalling = bundle.getBoolean(FALL_KEY, false);
	}

	@Override
	public boolean isFalling() {
		return isFalling;
	}

	@Override
	public void setFalling(boolean falling) {
		isFalling = falling;
	}
	
}