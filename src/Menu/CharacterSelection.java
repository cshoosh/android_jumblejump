package Menu;

import java.io.IOException;
import java.util.ArrayList;

import Entity.Supportables.BitmapDrawableObject;
import Entity.Supportables.Characters;
import Entity.Supportables.DrawableObject;
import LevelDesign.Screen;
import Resources.Animation;
import Resources.ResourceName;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

import com.zerotwoone.highjump.R;

public class CharacterSelection implements OnTouchListener, OnGestureListener {

	private static final ArrayList<CharacterSlot> mSlotArray = new ArrayList<CharacterSlot>(
			Characters.values().length);

	private RectF mBounds = new RectF();

	private GestureDetector mDetector;
	private float xOffset;
	//private Context mContext;

	public static float GRAVITY;
	public static float BOUNCE_VELOCITY;
	public static int CENTER_INDEX;

	private DrawableObject mPlatform;

	private boolean isScrolling;
	private float x, y, playerDimen;

	public CharacterSelection(Context context) throws IOException {
		mDetector = new GestureDetector(context, this);

	//	mContext = context;
		mSlotArray.clear();
		for (Characters character : Characters.values())
			mSlotArray.add(new CharacterSlot(character, character.ordinal()));

		CENTER_INDEX = mSlotArray.size() / 2;

		GRAVITY = Screen.SCREEN_HEIGHT * 0.001f;
		BOUNCE_VELOCITY = -Screen.SCREEN_HEIGHT * 0.020f;

		CharacterSlot.fillCharacterArray(context);

		playerDimen = ResourceName.getDefaultWidth(ResourceName.Player, context)
				* 1.5f;
		float width = mSlotArray.size() * playerDimen;
		float height = playerDimen;

		x = -width / 2f + Screen.SCREEN_WIDTH / 2f;
		y = Screen.SCREEN_HEIGHT * 0.421f - playerDimen;

		mBounds.set(x, y, x + width, y + height);

		mPlatform = new BitmapDrawableObject(context, R.drawable.menu_platform);
		mPlatform.setBounds(0, 0,
				context.getResources().getDimension(R.dimen.platformWidth),
				context.getResources().getDimension(R.dimen.platformHeight));

		for (CharacterSlot slot : mSlotArray) {
				if (slot.getCharacter() == Characters.Bandar)
					slot.setEnabled(true);
				if (slot.getCharacter() == Characters.Bhed)
					slot.setEnabled(true);
		}
	}

	public synchronized ArrayList<CharacterSlot> getArray() {
		return mSlotArray;
	}

	public synchronized void draw(Canvas canvas) {
		canvas.save();
		canvas.translate(x, y);
		for (CharacterSlot slot : mSlotArray) {

			slot.setStop(isScrolling);
			
			float xtranslate = slot.getIndex()
					* playerDimen;
			if (slot.getIndex() == CharacterSelection.CENTER_INDEX) {
				slot.draw(canvas, xtranslate + xOffset, 0);
				mPlatform.draw(canvas, xtranslate, playerDimen, null, null);
			} else {
				
				slot.draw(canvas, xtranslate + xOffset, 0);
			}

		}
		canvas.restore();
		if (!isScrolling)
			xOffset = Animation.tendToZero(xOffset, 3f);

	}

	private void cycleCharacters(boolean clockwise) {
		if (clockwise) {
			int indexFirst = mSlotArray.get(0).getIndex();
			for (int i = 1; i < mSlotArray.size(); i++) {

				CharacterSlot prev = mSlotArray.get(i - 1);
				CharacterSlot now = mSlotArray.get(i);

				prev.setIndex(now.getIndex());
			}

			CharacterSlot last = mSlotArray.get(mSlotArray.size() - 1);
			last.setIndex(indexFirst);
		} else {
			int indexLast = mSlotArray.get(mSlotArray.size() - 1).getIndex();

			for (int i = mSlotArray.size() - 2; i >= 0; i--) {

				CharacterSlot next = mSlotArray.get(i + 1);
				CharacterSlot now = mSlotArray.get(i);

				next.setIndex(now.getIndex());
			}

			CharacterSlot first = mSlotArray.get(0);
			first.setIndex(indexLast);
		}
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_CANCEL
				|| event.getAction() == MotionEvent.ACTION_UP)
			isScrolling = false;

		if (mBounds.contains(event.getX(), event.getY())) {
			return mDetector.onTouchEvent(event);
		}
		return false;
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
		xOffset -= distanceX * 1.5f;
		isScrolling = true;
		if (Math.abs(xOffset) >= playerDimen) {
			if (xOffset > 0)
				cycleCharacters(true);
			else
				cycleCharacters(false);

			xOffset = 0;
		}

		return true;
	}

	@Override
	public void onShowPress(MotionEvent e) {

	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		return false;
	}
}
