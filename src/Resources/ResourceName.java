package Resources;

import android.content.Context;

import com.zerotwoone.highjump.R;

public enum ResourceName {
		Player, Enemy,
		Platform,
		Item,
		Fire;
	
		public static final float getDefaultWidth(ResourceName name, Context context){
			switch (name) {
			case Player:
				return context.getResources().getDimension(R.dimen.playerWidth);
			case Enemy:
				return context.getResources().getDimension(R.dimen.playerWidth);
			case Fire:
				return context.getResources().getDimension(R.dimen.fireWidth);
			case Item:
				return context.getResources().getDimension(R.dimen.fireWidth);
			case Platform:
				return context.getResources().getDimension(R.dimen.platformWidth);
			default:
				return 0;
			}
		}
		
		public static final float getDefaultHeight(ResourceName name, Context context){
			switch (name) {
			case Player:
				return context.getResources().getDimension(R.dimen.playerHeight);
			case Enemy:
				return context.getResources().getDimension(R.dimen.playerHeight);
			case Fire:
				return context.getResources().getDimension(R.dimen.fireHeight);
			case Item:
				return context.getResources().getDimension(R.dimen.fireHeight);
			case Platform:
				return context.getResources().getDimension(R.dimen.platformHeight);
			default:
				return 0;
			}
		}
}
