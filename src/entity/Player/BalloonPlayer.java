package Entity.Player;

import EntityClasses.Entity;
import Process.AllManagers;
import Resources.ResourceName;
import Resources.ResourcePlayer;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.os.Bundle;


public class BalloonPlayer extends Entity {

	private boolean isBalloonReleased, isBallooned;
	private Player mPlayer;
	
	public BalloonPlayer(AllManagers manager,Player player) {
		super(manager);
		mPlayer = player;
	}

	@Override
	protected void init() {
		addDrawable(ResourceName.Player, ResourcePlayer.Balloon.name());
	}

	public void draw(Canvas canvas, RectF sender) {
		if (isBalloonned()){
		offsetTo(sender.centerX() - width() / 2f, sender.top - height()
				+ (sender.height() * 0.3243243243243243f));
		}
		
		if (isBalloonReleased()){
			offset(0, -10);
			if (this.top < -height()){
				isBalloonReleased = false;
				mPlayer.isShielded = false;
			}
		}
		
		super.draw(canvas);
	}
	
	public void ballooned(){
		isBallooned = true;
	}
	
	public void release(){
		isBallooned = false;
		isBalloonReleased = true;
	}

	@Override
	protected void writeIntoParcel(Bundle bundle) {

	}

	@Override
	protected void readFromParcel(Bundle bundle) {

	}
	
	public boolean isBalloonned(){
		return isBallooned;
	}
	
	public boolean isBalloonReleased(){
		return isBalloonReleased;
	}

}
