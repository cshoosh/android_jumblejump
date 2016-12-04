package Entity.Platforms;

import Entity.Interfaces.OnAnimationDraw;
import Entity.Player.Player;
import Entity.Supportables.EntityInfo;
import EntityClasses.Entity;
import EntityClasses.Platform;
import Process.AllManagers;
import Resources.ResourceName;
import Resources.ResourcePlatform;
import android.os.Bundle;


public class ElectricPlatform extends Platform implements OnAnimationDraw {

	private int mInterval, mFrame;//, mSoundStreamId;

	public ElectricPlatform(EntityInfo info, AllManagers context) {
		super(info, context);
	}

	@Override
	public void CollideTop(Entity who) {

	}

	@Override
	public void CollideInside(Entity who) {
		if (who instanceof Player && !((Player) who).isDead())
			((Player) who).hit(this);
		
	}

	@Override
	public void CollideBottom(Entity who) {

	}

	@Override
	protected void init() {
		addDrawable(ResourceName.Platform, ResourcePlatform.Electric.name());
	}

	@Override
	public void die() {
		super.die();
		//GameLoop.getSound().stopLoopableSound(mSoundStreamId, R.raw.teslacoil_02);
	}

	/*@Override
	public void update() {
		super.update();
		if (top > - Screen.SCREEN_HEIGHT/2f) 
			if (!GameLoop.getSound().isTeslaPlaying())
				mSoundStreamId = GameLoop.getSound().play(R.raw.teslacoil_02, true);		
	}*/

	@Override
	protected void writeIntoParcel(Bundle bundle) {

	}

	@Override
	protected void readFromParcel(Bundle bundle) {

	}

	@Override
	public int getFrame() {
		// TODO Auto-generated method stub
		return mFrame;
	}

	@Override
	public void setFrame(int frame) {
		mFrame = frame;
	}

	@Override
	public int getInterval() {
		return mInterval;
	}

	@Override
	public void setInterval(int interval) {
		mInterval = interval;
	}

	@Override
	protected void finalize() throws Throwable {
		super.finalize();
	}

}
