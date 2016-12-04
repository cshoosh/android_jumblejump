package Entity.Platforms;

import Entity.Player.Player;
import Entity.Supportables.EntityInfo;
import EntityClasses.Entity;
import EntityClasses.Item;
import EntityClasses.Platform;
import Process.AllManagers;
import Resources.ResourceName;
import Resources.ResourcePlatform;
import android.os.Bundle;


public class NormalPlatform extends Platform {

	private Item mItem;
	public NormalPlatform(EntityInfo info, AllManagers context) {
		super(info, context);

		Item item = Item.getItemInstance(info, context);
		mItem = item;
		if (item != null){
			setOnOffsetChangeListener(item);
			mManagers.getEntityManager().addEntity(item);
		}
	}

	@Override
	public void CollideTop(Entity who) {
		if (who instanceof Player)
			((Player) who).jump(this);

	}

	@Override
	public void CollideInside(Entity who) {

	}

	@Override
	public void CollideBottom(Entity who) {

	}

	@Override
	public void die() {
		super.die();
		if (mItem != null)
			mItem.die();
	}
	@Override
	protected void init() {
		addDrawable(ResourceName.Platform, ResourcePlatform.DEFAULT.name());
	}

	@Override
	protected void writeIntoParcel(Bundle bundle) {

	}

	@Override
	protected void readFromParcel(Bundle bundle) {

	}

}