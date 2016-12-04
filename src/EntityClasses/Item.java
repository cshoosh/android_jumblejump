package EntityClasses;

import Entity.Interfaces.Collidable;
import Entity.Interfaces.OnOffsetChangeListener;
import Entity.Items.Balloon;
import Entity.Items.Coin;
import Entity.Items.Shield;
import Entity.Items.Switch;
import Entity.Player.Player;
import Entity.Supportables.EntityInfo;
import Entity.Supportables.ItemList;
import Entity.Supportables.TypeAttributeList;
import Process.AllManagers;
import android.os.Bundle;


public abstract class Item extends Entity implements Collidable, OnOffsetChangeListener {
	public Item(AllManagers context) {
		super(context);
	}

	//Alignment Codes
	public static final int LEFT = 0;
	public static final int CENTER = 1;
	public static final int RIGHT = 2;
	
	protected int mAlignment;
	public static final String ALIGNMENT_KEY = "alignmentKey";		

	protected abstract void consume(Entity who);
	
	@Override
	public void OnOffsetChanged(Entity parent) {
		float top = parent.top - height();
		switch (mAlignment) {
		case 0:
			offsetTo(parent.left, top);
			break;
		case 1:
			offsetTo(parent.centerX() - width()/2f, top);
			break;
		case 2:
			offsetTo(parent.right - width(), top);
			break;
		default:
			break;
		}
	}
	
	@Override
	public void CollideTop(Entity who) {
		
	}
	
	@Override
	public void CollideBottom(Entity who) {
		
	}
	
	@Override
	public void CollideInside(Entity who) {
		if (who instanceof Player && !((Player) who).isDead()){
			consume(who);
			this.die();
		}
	}
	
	public void setAlignMent(int align){
		mAlignment = align;
	}	
	
	@Override
	protected void readFromParcel(Bundle bundle) {
		mAlignment = bundle.getInt(ALIGNMENT_KEY, 0);
	}
	
	@Override
	protected void writeIntoParcel(Bundle bundle) {
		bundle.putInt(ALIGNMENT_KEY, mAlignment);
	}
	
	public static final Item getItemInstance(EntityInfo info, AllManagers context) {
		
		String itemName = info.getAttributeValue(TypeAttributeList.item);
		ItemList item = translate(itemName);
		Item ret = null;
		
		if (item != null)
			switch (item) {
			case Coin:
				ret = new Coin(context);
				break;
			case Balloon:
				ret = new Balloon(context);
				break;
			case Shield:
				ret = new Shield(context);
				break;
			case Switch:
				ret = new Switch(context);
				break;
			default:
				break;
			}
		
		if (ret != null)
			ret.setAlignMent(translateAlignment(
					info.getAttributeValue(TypeAttributeList.align)));
		
		return ret;
	}
	
	public static final int translateAlignment(String align){
		if (align.equals("RIGHT"))
			return RIGHT;
		else if (align.equals("CENTER"))
			return CENTER;
		else 
			return LEFT;		
	}
	
	public static final ItemList translate(String item) {
		for (ItemList list:ItemList.values()){
			if(item.equals(list.getCode()))
				return list;
		}

		return null;
	}
	
}