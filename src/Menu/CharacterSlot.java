package Menu;


import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import Entity.Supportables.BitmapDrawableObject;
import Entity.Supportables.Characters;
import Entity.Supportables.DrawableObject;
import Entity.Supportables.Characters.CharacterAction;
import Resources.Animation;
import Resources.ResourceName;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;


public class CharacterSlot {

	private static final ArrayList<DrawableObject> mCharacterBitmap = new ArrayList<DrawableObject>(
			Characters.values().length);

	private Characters mCharacterNow;
	private static final Paint mPaint = new Paint();
	private int index;
	private float mainYjump;
	private float velocityY;
	private boolean isEnabled;
	private boolean isStopped;

	public void draw(Canvas canvas, float offsetX, float offsetY) {
		try {
			
			if (!isEnabled)
				mCharacterBitmap.get(mCharacterNow.ordinal()).draw(
					canvas,
					offsetX,
					offsetY + mainYjump, mPaint, null);
			else
				mCharacterBitmap.get(mCharacterNow.ordinal()).draw(
						canvas,
						offsetX,
						offsetY + mainYjump, null, null);
				
			
			if (isEnabled){
				if (getIndex() == CharacterSelection.CENTER_INDEX && !isStopped) {
					if (mainYjump >= 0)
						setVelocity(CharacterSelection.BOUNCE_VELOCITY);
					increaseVelocity(CharacterSelection.GRAVITY);
	
					offset(getVelocity());
				}
				else	
					mainYjump = Animation.tendToZero(mainYjump, 5);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void offset(float dy) {
		mainYjump += dy;
	}

	private float getVelocity() {
		return Math.max(Math.min(velocityY, 30), -30);
	}

	private void setVelocity(float vel) {
		velocityY = vel;
	}

	private void increaseVelocity(float increment) {
		velocityY += increment;
	}

	public CharacterSlot(Characters character, int index) {
		mCharacterNow = character;
		this.index = index;
		
		float[] src = {
				1,0,0,0,0,
				1,0,0,0,0,
				1,0,0,0,0,
				0,0,0,1,0
		};

		mPaint.setColorFilter(new ColorMatrixColorFilter(src));
		setEnabled(false);
	}
	
	public int getIndex(){
		return index;
	}
	
	public void setIndex(int index){
		this.index = index;
	}
	
	public boolean isEnabled(){
		return isEnabled;
	}
	public void setEnabled(boolean enabled){	
		isEnabled = enabled;
	}

	public void setStop(boolean stop){
		isStopped = stop;
	}
	
	public void setCharacter(Characters character) {
		mCharacterNow = character;
	}

	public Characters getCharacter() {
		return mCharacterNow;
	}

	public static final void fillCharacterArray(Context context)
			throws IOException {
		mCharacterBitmap.clear();
		float playerDimen = ResourceName.getDefaultWidth(ResourceName.Player, context) * 1.5f;
		for (Characters character : Characters.values()) {
			InputStream is = context.getAssets().open(
					Characters.getPath(CharacterAction.Idle, character));

			Bitmap bmp = BitmapFactory.decodeStream(is);
			BitmapDrawableObject obj = new BitmapDrawableObject(bmp);
			
			obj.setBounds(0, 0,playerDimen,playerDimen);
			mCharacterBitmap.add(obj);
		}
	}
}
