package Entity.Items;

import Entity.Player.Player;
import EntityClasses.Entity;
import EntityClasses.Item;
import Process.AllManagers;
import Resources.ResourceItem;
import Resources.ResourceName;
import android.os.Bundle;

import com.zerotwoone.highjump.R;

public class Shield extends Item {

	public Shield(AllManagers context) {
		super(context);
	}

	@Override
	protected void init() {
		addDrawable(ResourceName.Item, ResourceItem.Shield.name());
	}

	@Override
	protected void writeIntoParcel(Bundle bundle) {

	}

	@Override
	protected void readFromParcel(Bundle bundle) {

	}

	@Override
	protected void consume(Entity who) {
		((Player) who).shield();
		mManagers.getSoundManager().play(R.raw.shield);
	}

}
