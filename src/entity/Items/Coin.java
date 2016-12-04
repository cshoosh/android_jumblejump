package Entity.Items;

import Entity.Interfaces.OnAnimationDraw;
import Entity.Player.Player;
import EntityClasses.Entity;
import EntityClasses.Item;
import Process.AllManagers;
import Resources.ResourceItem;
import Resources.ResourceName;
import android.os.Bundle;

import com.zerotwoone.highjump.R;

public class Coin extends Item implements OnAnimationDraw {

	private int mFrame, mInterval;

	public Coin(AllManagers context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void init() {
		addDrawable(ResourceName.Item, ResourceItem.Coin.name());
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

	@Override
	protected void consume(Entity who) {
		mManagers.getSoundManager().play(R.raw.coin);
		((Player) who).increaseCoins();
		((Player) who).increaseScore(100);
	}
}