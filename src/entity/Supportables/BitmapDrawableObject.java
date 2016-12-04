package Entity.Supportables;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Parcel;
import android.os.Parcelable;

public class BitmapDrawableObject extends DrawableObject implements Parcelable{

	private Bitmap mBitmap;
	private float mWidth, mHeight;
	
	public BitmapDrawableObject(Context context, int resID) {
		mBitmap = BitmapFactory.decodeResource(context.getResources(), resID);
		
		init();
	}
	
	public BitmapDrawableObject(Bitmap bmp) {
		mBitmap = bmp;
		init();
	}
	
	public BitmapDrawableObject(Parcel source){
		mWidth = source.readFloat();
		mHeight = source.readFloat();
		mBitmap = source.readParcelable(Bitmap.class.getClassLoader());
	}
	
	private void init() {
		mWidth = mBitmap.getWidth();
		mHeight = mBitmap.getHeight();
	}

	@Override
	public void draw(Canvas canvas, float left, float top, Paint paint, Object intr) {
		canvas.save();
		try{
		canvas.translate(left, top);
		canvas.drawBitmap(mBitmap, 0, 0, paint);
		}catch (Exception e){
			e.printStackTrace();
		}
		canvas.restore();
	}

	@Override
	public void setBounds(float left, float top, float right, float bottom) {
		mBitmap = scaleBitmap(mBitmap, (int) right, (int) bottom);
		mWidth = right - left;
		mHeight = bottom - top;
	}

	@Override
	public float getWidth() {
		return mWidth;
	}

	@Override
	public float getHeight() {
		return mHeight;
	}
	
	public static final Creator<BitmapDrawableObject> CREATOR = 
			new Creator<BitmapDrawableObject>() {
		
		@Override
		public BitmapDrawableObject[] newArray(int size) {
			return new BitmapDrawableObject[size];
		}
		
		@Override
		public BitmapDrawableObject createFromParcel(Parcel source) {
			return new BitmapDrawableObject(source);
		}
	};

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeFloat(mWidth);
		dest.writeFloat(mHeight);
		dest.writeParcelable(mBitmap, 0);
	}

	@Override
	public void release() {
		if (mBitmap != null && !mBitmap.isRecycled())
			mBitmap.recycle();
	}
	
}