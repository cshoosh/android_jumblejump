package Entity.Supportables;

import java.util.ArrayList;
import java.util.Random;

import org.w3c.dom.Element;

import LevelDesign.Screen;
import android.os.Parcel;
import android.os.Parcelable;


public class EntityInfo implements Parcelable {
	private float mX, mY;
	private ArrayList<TypeAttribute> mAttributeList = new ArrayList<TypeAttribute>();

	public EntityInfo() {
	}

	public EntityInfo(float x, float y, EntityType type) {
		mX = x;
		mY = y;
		mAttributeList.add(new TypeAttribute(TypeAttributeList.type, type.toString()));
	}

	public EntityInfo(Parcel out) {

	}

	public float getX() {
		return mX;
	}

	public void setX(float mX) {
		this.mX = mX;
	}

	public float getY() {
		return mY;
	}

	public void setY(float mY) {
		this.mY = mY;
	}

	public void offset(float dx, float dy) {
		mX += dx;
		mY += dy;
	}

	public void offsetTo(float x, float y) {
		mX = x;
		mY = y;
	}

	public String getAttributeValue(TypeAttributeList name) {
		for (TypeAttribute attr : mAttributeList)
			if (attr.getName().equals(name))
				return attr.getValue();

		return "";
	}

	public void addAttributes(Element Type) {
		
		try{
			for (TypeAttributeList list : TypeAttributeList.values()) 
				if (Type.hasAttribute(list.toString()))
					mAttributeList.add(new TypeAttribute(list, Type
							.getAttribute(list.toString())));
		}catch (Exception e){
			e.printStackTrace();
		}
	}

	


	public static final EntityType translateType(String type) {
		for (EntityType types:EntityType.values())
			if (type.equals(types.toString()))
				return types;

		return null;
	}

	public static final EntityInfo getPlayerInfo() {

		return new EntityInfo(Screen.SCREEN_WIDTH * new Random().nextFloat(),
				Screen.SCREEN_HEIGHT, EntityType.Player);
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {

	}

	public static final Creator<EntityInfo> CREATOR = new Creator<EntityInfo>() {

		@Override
		public EntityInfo[] newArray(int size) {
			return new EntityInfo[size];
		}

		@Override
		public EntityInfo createFromParcel(Parcel source) {
			return new EntityInfo(source);
		}
	};
}