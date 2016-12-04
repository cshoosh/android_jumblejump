package Resources;

import Entity.Supportables.AnimatedDrawable;
import Entity.Supportables.BitmapDrawableObject;
import Entity.Supportables.DrawableObject;
import android.content.Context;

import com.zerotwoone.highjump.R;

public enum ResourceEnemy {
	Spider,
	Snake,
	SnakeHit,
	Croc;

	public static final DrawableObject getResource(Context context,
			ResourceEnemy res) {
		
		DrawableObject ret = null;
		switch (res) {
		case Spider:
			ret = new AnimatedDrawable(R.array.spiderAnim, context);
			break;
		case Croc:
			ret = new AnimatedDrawable(R.array.crocAnim, context);
			break;
		case Snake:
			ret = new AnimatedDrawable(R.array.snakeAnim, context);
			break;
		case SnakeHit:
			ret = new BitmapDrawableObject(context, R.drawable.snake_bonga);
			break;
		default:
			break;
		}
		
		if (res == Croc)
			ret.setBounds(0, 0, ResourceName.getDefaultWidth(ResourceName.Enemy, context) * 1.5f
					,ResourceName.getDefaultHeight(ResourceName.Enemy, context) * 1.5f);
		else
		ret.setBounds(0, 0, ResourceName.getDefaultWidth(ResourceName.Enemy, context)
				,ResourceName.getDefaultHeight(ResourceName.Enemy, context));
		
		return ret;
	}
}
