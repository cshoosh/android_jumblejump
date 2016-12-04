package Resources;

import Entity.Supportables.AnimatedDrawable;
import Entity.Supportables.BitmapDrawableObject;
import Entity.Supportables.DrawableObject;
import android.content.Context;

import com.zerotwoone.highjump.R;

public enum ResourcePlatform {
	DoubleJumpIdle, DoubleJumpAnim, BreakA, BreakB, Electric, Leaf1, Leaf2, DEFAULT;

	public static final DrawableObject getResource(Context context,
			ResourcePlatform res) {
		DrawableObject ret = null;
		switch (res) {
		case BreakA:
			ret = new BitmapDrawableObject(context,
					R.drawable.platform_break_01a);
			break;
		case BreakB:
			ret = new BitmapDrawableObject(context,
					R.drawable.platform_break_01b);
			break;
		case DoubleJumpIdle:
			ret = new BitmapDrawableObject(context, R.drawable.jump_platform_00);
			break;
		case DoubleJumpAnim:
			ret = new AnimatedDrawable(R.array.doubleJumpAnim, context);
			break;
		case Electric:
			ret = new AnimatedDrawable(R.array.teslaAnim, context);
			break;
		case Leaf1:
			ret = new BitmapDrawableObject(context, R.drawable.leaf01);
			break;
		case Leaf2:
			ret = new BitmapDrawableObject(context, R.drawable.leaf02);
			break;
		
		case DEFAULT:
			ret = new BitmapDrawableObject(context, R.drawable.platform_wood);
			break;
		default:
			break;
		}

		float defWidth = ResourceName.getDefaultWidth(ResourceName.Platform,
				context);
		float defHeight = ResourceName.getDefaultHeight(ResourceName.Platform,
				context);

		switch (res) {

		case DoubleJumpAnim:
		case DoubleJumpIdle:
			ret.setBounds(0, 0, defWidth, defHeight * 1.83f);
			break;

		case Leaf1: case Leaf2:
			ret.setBounds(0,0, ResourceName.getDefaultWidth(ResourceName.Fire, context),
					ResourceName.getDefaultHeight(ResourceName.Fire, context));
			break;
		default:
			ret.setBounds(0, 0, defWidth, defHeight);
			break;
		}

		return ret;
	}

}
