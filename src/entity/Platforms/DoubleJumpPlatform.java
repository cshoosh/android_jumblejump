package Entity.Platforms;

import Entity.Interfaces.OnAnimationDraw;
import Entity.Interfaces.OnAnimationListener;
import Entity.Player.Player;
import Entity.Supportables.EntityInfo;
import Entity.Supportables.TypeAttributeList;
import EntityClasses.Entity;
import EntityClasses.Platform;
import Process.AllManagers;
import Resources.ResourceName;
import Resources.ResourcePlatform;
import android.os.Bundle;


public class DoubleJumpPlatform extends Platform implements OnAnimationDraw, OnAnimationListener{
	
	public DoubleJumpPlatform(EntityInfo info, AllManagers context) {
		super(info, context);
		try{
			mJumpIntensity = Float.valueOf(info.getAttributeValue(TypeAttributeList.doubleInt));
		}catch(Exception e){
			mJumpIntensity = 2;
			e.printStackTrace();
		}
	}
	//Key Constants
	private static final String DOUBLEJUMP_INTENSITY_KEY = "dblJumpIntensityKey";
	
	//Animation Constants
	private static final int IDLE_LEVEL = 0;
	private static final int ANIM_LEVEL = 1;
	
	private int mInterval,mFrame;
	private float mJumpIntensity;
	
	@Override
	public void CollideTop(Entity who) {
		if (who instanceof Player){
			((Player) who).setFalling(false);
			who.setVelocityY(0);
			who.offsetTo(who.left, this.top + height() * 0.352f - who.height());
			setLevel(ANIM_LEVEL);
			
			if (((Player) who).isDead())
				((Player) who).cliffHanged();
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
		addDrawable(ResourceName.Platform, ResourcePlatform.DoubleJumpIdle.name());
		addDrawable(ResourceName.Platform, ResourcePlatform.DoubleJumpAnim.name());		
	}

	@Override
	protected void writeIntoParcel(Bundle bundle) {
		writeAnimation(bundle, mFrame, mInterval);
		bundle.putFloat(DOUBLEJUMP_INTENSITY_KEY, mJumpIntensity);
	}

	@Override
	protected void readFromParcel(Bundle bundle) {
		mFrame = bundle.getInt(ANIM_FRAME_KEY, 0);
		mInterval = bundle.getInt(ANIM_INTERVAL_KEY, 0);
		mJumpIntensity = bundle.getFloat(DOUBLEJUMP_INTENSITY_KEY, 2f);
	}

	@Override
	public void OnAnimationComplete() {
		setLevel(IDLE_LEVEL);
		mManagers.getEntityManager().getPlayer().jump(this, mJumpIntensity);
		mManagers.getEntityManager().getPlayer().setFalling(true);
	}

	@Override
	public void OnAnimationFrame(int frame) {
		Player player = mManagers.getEntityManager().getPlayer();
		switch (frame) {
		case 0:
			player.offsetTo(player.left, this.top + height() * 0.6f - player.height());
			break;
		case 1:
			player.offsetTo(player.left, this.top + height() * 0.809f - player.height());
			break;
		case 2:
			player.offsetTo(player.left, this.top + height() * 0.6f - player.height());
			break;
		default:
			player.offsetTo(player.left, this.top + height() * 0.352f - player.height());
			break;
		}
		
	}

	@Override
	public int getFrame() {
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
}