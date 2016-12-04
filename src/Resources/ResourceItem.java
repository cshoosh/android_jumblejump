package Resources;

import Entity.Supportables.AnimatedDrawable;
import Entity.Supportables.BitmapDrawableObject;
import Entity.Supportables.DrawableObject;
import android.content.Context;

import com.zerotwoone.highjump.R;

public enum ResourceItem {
	Coin, Balloon, Shield;
	
	public static final DrawableObject getResource(Context context, ResourceItem res){
		DrawableObject ret = null;
		switch (res) {
		
		case Coin:
			ret = new AnimatedDrawable(R.array.coinAnim, context);
			break;
		case Balloon:
			ret = new BitmapDrawableObject(context, R.drawable.balloon_01);
			break;
		case Shield:
			ret = new BitmapDrawableObject(context, R.drawable.shield);
			break;
		default:
			break;
		}
		
		if (res == Balloon)
			ret.setBounds(0, 0, ResourceName.getDefaultWidth(ResourceName.Item, context), 
					ResourceName.getDefaultHeight(ResourceName.Item, context));
		else
			ret.setBounds(0, 0, ResourceName.getDefaultWidth(ResourceName.Item, context), 
				ResourceName.getDefaultHeight(ResourceName.Item, context));
		return ret;
	}
}
