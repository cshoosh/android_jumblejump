package Menu;

import Entity.Interfaces.OnMenuButtonClickListener;
import Entity.Supportables.DrawableObject;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;

import com.zerotwoone.highjump.MainMenuActivity;

public class TouchableDrawable extends DrawableObject implements
		OnGestureListener {

	private TouchableTypes id;
	private OnMenuButtonClickListener mClick;
	private GestureDetector mGesture;
	private Bitmap mBitmapPressed, mBitmapUnPressed;
	private boolean isPressed, isEnabled;

	private RectF mBounds = new RectF();

	public TouchableDrawable(int resPressed, int unPressed, Context context,
			TouchableTypes type) {
		mBitmapPressed = BitmapFactory.decodeResource(context.getResources(),
				resPressed);
		mBitmapUnPressed = BitmapFactory.decodeResource(context.getResources(),
				unPressed);
		id = type;
		init(context);
	}

	private void init(Context context) {
		mBounds.set(0, 0, mBitmapPressed.getWidth(), mBitmapPressed.getHeight());
		mGesture = new GestureDetector(context, this);
		mClick = (MainMenuActivity) context;
		isEnabled = true;
	}

	public void offsetTo(float x, float y) {
		mBounds.offsetTo(x, y);
	}

	public RectF getBounds() {
		return mBounds;
	}

	public boolean OnTouch(MotionEvent event) {
		if (isEnabled) {
			if (mBounds.contains(event.getX(), event.getY())) {
				mGesture.onTouchEvent(event);

				if (event.getAction() == MotionEvent.ACTION_DOWN)
					isPressed = true;
				else if (event.getAction() == MotionEvent.ACTION_CANCEL
						|| event.getAction() == MotionEvent.ACTION_UP)
					isPressed = false;
				return true;
			}
		}
		isPressed = false;
		return false;
	}

	@Override
	public void draw(Canvas canvas, float left, float top, Paint paint,
			Object intr) {
		if (isEnabled) {
			if (isPressed)
				canvas.drawBitmap(mBitmapPressed, mBounds.left, mBounds.top,
						paint);
			else
				canvas.drawBitmap(mBitmapUnPressed, mBounds.left, mBounds.top,
						paint);
		}
	}

	@Override
	public void setBounds(float left, float top, float right, float bottom) {

		mBounds.set(left, top, right, bottom);
		mBitmapPressed = scaleBitmap(mBitmapPressed, (int) getWidth(),
				(int) getHeight());
		mBitmapUnPressed = scaleBitmap(mBitmapUnPressed, (int) getWidth(),
				(int) getHeight());
	}

	@Override
	public float getWidth() {
		return mBounds.width();
	}

	@Override
	public float getHeight() {
		return mBounds.height();
	}

	@Override
	public boolean onDown(MotionEvent e) {
		return true;
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		return false;
	}

	@Override
	public void onLongPress(MotionEvent e) {

	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		return false;
	}

	@Override
	public void onShowPress(MotionEvent e) {

	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		if (mClick != null) {
			mClick.OnMenuButtonClicked(this);
			return true;
		}

		return false;
	}

	public void setEnabled(boolean value) {
		isEnabled = value;
	}

	public TouchableTypes getID() {
		return id;
	}

	public enum TouchableTypes {
		NewGame, Options, Achievement, HighScore, RemoveAds
	}

	@Override
	public void release() {
		if (mBitmapPressed != null && !mBitmapPressed.isRecycled())
			mBitmapPressed.recycle();
		if (mBitmapUnPressed != null && !mBitmapUnPressed.isRecycled())
			mBitmapUnPressed.recycle();
	}
	
}
