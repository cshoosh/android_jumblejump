package Resources;

import Entity.Supportables.BitmapDrawableObject;
import Entity.Supportables.DrawableObject;
import LevelDesign.Screen;
import android.content.Context;

import com.zerotwoone.highjump.R;

public enum ResourceBackground {
	Background,
	SideStripLeft,
	SideStripRight,
	Cloud,
	CloudDepth;
	
	public static DrawableObject getResource(Context context, ResourceBackground bg){
		DrawableObject ret = null;
		float density = context.getResources().getDisplayMetrics().density;
		
		switch (bg) {
		case Background:
			ret = new BitmapDrawableObject(context, R.drawable.bg);
			ret.setBounds(0, 0, Screen.SCREEN_WIDTH, Screen.SCREEN_HEIGHT);
			break;
		case SideStripLeft:
			ret = new BitmapDrawableObject(context, R.drawable.sidestrip);
			break;
		case  SideStripRight:
			ret = new BitmapDrawableObject(context, R.drawable.sidestripright);
			break;
		case Cloud:
			ret = new BitmapDrawableObject(context, R.drawable.cloud02);
			break;
		case CloudDepth:
			ret = new BitmapDrawableObject(context, R.drawable.cloud01);
			break;
		default:
			break;
		}
		
		if (bg == SideStripLeft || bg == SideStripRight)
			ret.setBounds(0, 0, ResourceName.getDefaultHeight(ResourceName.Platform, context) * 2.5f,
					ResourceName.getDefaultWidth(ResourceName.Platform, context) * 1.5f);
		else if (bg == Cloud || bg == CloudDepth)
			ret.setBounds(0, 0, density * 85, density * 85);
		return ret;
	}
}
