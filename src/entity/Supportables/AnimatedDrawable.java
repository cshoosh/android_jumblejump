package Entity.Supportables;


import Entity.Interfaces.OnAnimationDraw;
import Entity.Interfaces.OnAnimationListener;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Parcel;
import android.os.Parcelable;

public class AnimatedDrawable extends DrawableObject implements OnAnimationDraw 
					,Parcelable{

	private Bitmap[] mBitmapArray;
	private OnAnimationListener mAnimationListener;
	private OnAnimationDraw mDrawImplementation;
	
	private int mInterval, mFrameCount;
	private float mWidth, mHeight;
	
	public AnimatedDrawable(Parcel source) {
		mWidth = source.readFloat();
		mHeight = source.readFloat();
		mInterval = source.readInt();
		mFrameCount = source.readInt();
		mBitmapArray = (Bitmap[]) source.readParcelableArray(Bitmap.class.getClassLoader());
	}

	public AnimatedDrawable(int resID, Context context) {
		
		TypedArray array = context.getResources()
				.obtainTypedArray(resID);

		mFrameCount = array.length() - 1;
		
		mBitmapArray = new Bitmap[mFrameCount];
		for (int i = 0; i < mFrameCount; i++)
			mBitmapArray[i] = BitmapFactory.decodeResource(
					array.getResources(), array.getResourceId(i, resID));
		
		mInterval = Integer.valueOf(array.getString(mFrameCount)); 

		array.recycle();
		
		setWidthHeight();			
	}

	public AnimatedDrawable(Bitmap[] bmps, int interval) {
		mInterval = interval;
		mFrameCount = bmps.length;
		mBitmapArray = bmps;
		
		setWidthHeight();
	}
	

	@Override
	public void draw(Canvas canvas, float left, float top, Paint paint,
			Object intr) {
		if (intr instanceof OnAnimationDraw) {
			mDrawImplementation = (OnAnimationDraw) intr;
			canvas.save();
			try{
			canvas.translate(left, top);
			canvas.drawBitmap(mBitmapArray[getFrame()], 0, 0, paint);
			}catch(Exception e){
				e.printStackTrace();
			}
			canvas.restore();

			if (intr instanceof OnAnimationListener)
				mAnimationListener = (OnAnimationListener) intr;
			increaseFrame();
			
			mDrawImplementation = null;
			mAnimationListener = null;

		} else
			throw new IllegalArgumentException(
					"Passing argument is not a valid interface");

	}

	private void increaseFrame() {
		if (mInterval == getInterval()) {
			if (getFrame() == mFrameCount - 1){
				setFrame(0);
				if (mAnimationListener != null)
					mAnimationListener.OnAnimationComplete();
				return;
			}
			if (mAnimationListener != null)
				mAnimationListener.OnAnimationFrame(getFrame());
			setFrame(getFrame() + 1);
			setInterval(0);
			return;
		}
		setInterval(getInterval() + 1);
	}

	@Override
	public void setBounds(float left, float top, float right, float bottom) {
		for (int i = 0; i < mBitmapArray.length; i++) 
			mBitmapArray[i] = scaleBitmap(mBitmapArray[i], (int)right, (int)bottom);
		
		setWidthHeight();
	}

	public void setWidthHeight(){
		if (mBitmapArray != null && mBitmapArray.length > 0){
			mWidth = mBitmapArray[0].getWidth();
			mHeight = mBitmapArray[0].getHeight();
		}
	}
	@Override
	public int getFrame() {
		if (mDrawImplementation != null)
			return mDrawImplementation.getFrame();
		return 0;
	}

	@Override
	public void setFrame(int frame) {
		if (mDrawImplementation != null)
			mDrawImplementation.setFrame(frame);
	}

	@Override
	public int getInterval() {
		if (mDrawImplementation != null)
			return mDrawImplementation.getInterval();
		return 0;
	}

	@Override
	public void setInterval(int interval) {
		if (mDrawImplementation != null)
			mDrawImplementation.setInterval(interval);
	}

	@Override
	public float getWidth() {
		return mWidth;
	}

	@Override
	public float getHeight() {
		return mHeight;
	}
	
	public static final Creator<AnimatedDrawable> CREATOR = new Creator<AnimatedDrawable>() {
		
		@Override
		public AnimatedDrawable[] newArray(int size) {
			return new AnimatedDrawable[size];
		}
		
		@Override
		public AnimatedDrawable createFromParcel(Parcel source) {
			return null;
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
		dest.writeInt(mInterval);
		dest.writeInt(mFrameCount);
		dest.writeParcelableArray(mBitmapArray, 0);
	}

	@Override
	public void release() {
		for (Bitmap bmp:mBitmapArray)
			if(bmp != null && !bmp.isRecycled())
				bmp.recycle();
		
	}

}