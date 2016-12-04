package Entity.Player;

import Entity.Interfaces.OnAnimationDraw;
import EntityClasses.Entity;
import Process.AllManagers;
import Resources.ResourceName;
import Resources.ResourcePlayer;
import android.graphics.Canvas;
import android.os.Bundle;


public class PlayerShield extends Entity implements OnAnimationDraw{

	private int mInterval, mFrame;
	public PlayerShield(AllManagers manager) {
		super(manager);
	}
	
	public void draw (Canvas canvas, float left, float top, float offsetx, float offsety){
		offsetTo(left, top);
		offset(-offsetx, -offsety);
		super.draw(canvas);
	}

	@Override
	protected void init() {
		addDrawable(ResourceName.Player, ResourcePlayer.PlayerShield.name());
	}

	@Override
	protected void writeIntoParcel(Bundle bundle) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void readFromParcel(Bundle bundle) {
		// TODO Auto-generated method stub

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
