package Resources;

import java.io.IOException;
import java.util.ArrayList;

import Entity.Supportables.BitmapDrawableObject;
import Entity.Supportables.Characters;
import Entity.Supportables.DrawableObject;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Parcel;
import android.os.Parcelable;

import com.zerotwoone.highjump.R;

public class ResourceManager implements Parcelable{

	public static Typeface IMPACT;
	public static Typeface GOODTIMES;
	
	private Characters mTheme;
	private Context mContext;

	private static final ArrayList<ResourceObject> mResourceList = new ArrayList<ResourceObject>();

	public ResourceManager(Context context, Characters theme)
			throws IOException {
		mTheme = theme;
		init(context);
	}
	
	public ResourceManager(Parcel source){
		mTheme = Characters.values()[source.readInt()];
	}

	public Characters getTheme() {
		return mTheme;
	}

	public void init (Context context) throws IOException{
		mContext = context;
		mResourceList.clear();


		ResourceName[] names = ResourceName.values();

		for (ResourceName name : names)
			mResourceList.add(addResource(name));
		
		GOODTIMES = Typeface.createFromAsset(context.getAssets(),
				"font/goodtimes.ttf");
		IMPACT = Typeface.createFromAsset(context.getAssets(),
				"font/impact.ttf");
	}
	private ResourceObject addResource(ResourceName name) throws IOException {
		ResourceObject item = new ResourceObject(name);
		switch (name) {
		case Platform:
			for (ResourcePlatform res : ResourcePlatform.values()) {
				item.addObject(res.name(),
						ResourcePlatform.getResource(mContext, res));
			}
			break;
		case Enemy:
			for (ResourceEnemy res : ResourceEnemy.values()) {
				item.addObject(res.name(),
						ResourceEnemy.getResource(mContext, res));
			}
			break;
		case Fire:
			DrawableObject objFire = null;

			objFire = new BitmapDrawableObject(mContext, R.drawable.rock);
			objFire.setBounds(0, 0,
					ResourceName.getDefaultWidth(name, mContext),
					ResourceName.getDefaultHeight(name, mContext));
			item.addObject("fire", objFire);
			break;
		case Player:
			for (ResourcePlayer res : ResourcePlayer.values()) {
				item.addObject(res.name(),
						ResourcePlayer.getResource(res, mContext, mTheme));
			}
			break;
		case Item:
			for (ResourceItem res : ResourceItem.values()) {
				item.addObject(res.name(),
						ResourceItem.getResource(mContext, res));
			}
			break;
		/*case Background:
			for (ResourceBackground res : ResourceBackground.values()) {
				item.addObject(res.name(),
						ResourceBackground.getResource(mContext, res));
			}
			break;*/
		default:
			break;
		}
		return item;
	}

	public void setPlayerResource(Characters theme) throws IOException {
		ResourceObject item = getResourceObject(ResourceName.Player);
		mTheme = theme;
		
		for (ResourcePlayer res : ResourcePlayer.values()) {
			item.replaceObject(res.name(),
					ResourcePlayer.getResource(res, mContext, mTheme));
		}
	}

	private ResourceObject getResourceObject(ResourceName name) {
		for (ResourceObject obj : mResourceList) {
			if (obj.getName() == name)
				return obj;
		}
		return null;
	}

	public DrawableObject getResource(ResourceName name, String sub) {
		for (ResourceObject items : mResourceList)
			if (items.getName().equals(name))
				return items.getObject(sub);
		return null;
	}

	public static String translateID(int id, Context context) {
		return context.getResources().getString(id);
	}

	public static final Creator<ResourceManager> CREATOR = new Creator<ResourceManager>() {
		
		@Override
		public ResourceManager[] newArray(int size) {
			return new ResourceManager[size];
		}
		
		@Override
		public ResourceManager createFromParcel(Parcel source) {
			return new ResourceManager(source);
		}
	};
	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(mTheme.ordinal());
	}
}
