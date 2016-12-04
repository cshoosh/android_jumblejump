package Entity.Items;

import Entity.Player.Player;
import EntityClasses.Entity;
import EntityClasses.Item;
import Process.AllManagers;
import Process.PhysixManager;
import Resources.ResourceItem;
import Resources.ResourceName;
import android.os.Bundle;

import com.zerotwoone.highjump.R;


public class Balloon extends Item {

	public Balloon(AllManagers context) {
		super(context);
	}

	@Override
	protected void init() {
		addDrawable(ResourceName.Item, ResourceItem.Balloon.name());		
	}

	@Override
	protected void writeIntoParcel(Bundle bundle) {

	}

	@Override
	protected void readFromParcel(Bundle bundle) {

	}

	@Override
	protected void consume(Entity who) {
		((Player) who).balloon();
		who.setVelocityY(PhysixManager.BOUNCE_VELOCITY);
		mManagers.getSoundManager().play(R.raw.balloon);
	}

}
