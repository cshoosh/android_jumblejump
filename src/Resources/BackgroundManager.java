package Resources;

import Entity.Interfaces.OnAnimationDraw;
import Entity.Player.Player;
import Entity.Supportables.DrawableObject;
import LevelDesign.Screen;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.os.Parcel;
import android.os.Parcelable;

import com.zerotwoone.highjump.R;

public final class BackgroundManager implements Parcelable{
	private DrawableObject mBackGroundTexture, mStripLeft, mStripRight, mCoin,
								mCloud, mCloudDepth;
	private int stripcount;
	private float offset;
	private RectF[] mCloudPos;
	private float mTextHeight;

	private Paint mScorePaint;
	private OnAnimationDraw mCoinAnim;

	public BackgroundManager(Context context) {

		initText(context.getResources().getDimension(R.dimen.fontsize));
		mCoinAnim = getCoinAnim();
		// Textures
		mBackGroundTexture = ResourceBackground.getResource(context, ResourceBackground.
				Background);

		mStripLeft = ResourceBackground.getResource(context, ResourceBackground.SideStripLeft);
		mStripRight = ResourceBackground.getResource(context, ResourceBackground.SideStripRight);
		
		//Coin
		mCoin = ResourceItem.getResource(context, ResourceItem.Coin);
		
		//Clouds
		mCloud = ResourceBackground.getResource(context, ResourceBackground.Cloud);
		mCloudDepth = ResourceBackground.getResource(context, ResourceBackground.CloudDepth);
		
		mCloudPos = new RectF[3];
		
		mCloudPos[0] = new RectF(0, 0, mCloud.getWidth(), mCloud.getHeight());
		mCloudPos[1] = new RectF(0, 0, mCloudDepth.getWidth(), mCloudDepth.getHeight());
		mCloudPos[2] = new RectF(0, 0, mCloud.getWidth(), mCloud.getHeight());
		
		mCloudPos[0].offsetTo(Screen.SCREEN_WIDTH * 0.05f, Screen.SCREEN_HEIGHT * 0.03f);
		mCloudPos[1].offsetTo(Screen.SCREEN_WIDTH * 0.45f, Screen.SCREEN_HEIGHT * 0.34f);
		mCloudPos[2].offsetTo(Screen.SCREEN_WIDTH * .7f, Screen.SCREEN_HEIGHT * 0.07f);
		
		stripcount = (int) (Screen.SCREEN_HEIGHT / mStripLeft.getHeight() + 2);
	}
	
	public BackgroundManager (Parcel source){
		mBackGroundTexture = source.readParcelable(null);
		mCloud = source.readParcelable(null);
		mCloudDepth = source.readParcelable(null);
		mCloudPos = (RectF[]) source.readParcelableArray(null);
		mCoin = source.readParcelable(null);
		
		mCoinAnim = getCoinAnim();
		mCoinAnim.setFrame(source.readInt());
		mCoinAnim.setInterval(source.readInt());
		
		initText(source.readFloat());
		mStripLeft = source.readParcelable(null);
		mStripRight = source.readParcelable(null);
		offset = source.readFloat();
		stripcount = source.readInt();
	}

	private void initText(float size){
		// Score Paint
				mScorePaint = new Paint();
				mScorePaint.setColor(0xFFFFFFFF);
				mScorePaint.setStyle(Style.FILL);
				mScorePaint.setStrokeWidth(1f);
				mScorePaint.setTypeface(ResourceManager.GOODTIMES);
				mScorePaint.setTextAlign(Align.LEFT);
				mScorePaint.setTextSize(size);

				mTextHeight = -mScorePaint.ascent() + mScorePaint.descent();
	}
	public void preDraw(Canvas canvas) {
		mBackGroundTexture.draw(canvas, 0, 0, null, null);
		
		//Draw Clouds
		mCloud.draw(canvas, mCloudPos[0].left, mCloudPos[0].top,null,null);
		mCloudDepth.draw(canvas, mCloudPos[1].left, mCloudPos[1].top,null,null);
		mCloud.draw(canvas, mCloudPos[2].left,mCloudPos[2].top,null,null);
		//Calculate Positions
		for (RectF r:mCloudPos){
			if (r.left <= -r.width())
				r.offsetTo(Screen.SCREEN_WIDTH + r.width(), r.top);
			r.offset(-0.3f, 0);
		}
		mCloudPos[1].offset(-0.3f, 0);
	}

	public void postDraw(Canvas canvas) {
		for (int i = -3; i < stripcount; i++) {
			float top = i * mStripLeft.getHeight() + offset;
			if (i % 2 == 0)
				mStripLeft.draw(canvas, -mStripLeft.getWidth() / 2f, top, null,
						null);
			else
				mStripRight.draw(canvas,
						Screen.SCREEN_WIDTH - mStripRight.getWidth() / 2f, top,
						null, null);
		}
	}

	public void drawHud(Canvas canvas, Player player) {
		// SCORE
		canvas.drawText(String.valueOf(player.getScore()), 10,
				10 + mTextHeight, mScorePaint);
		
		/*String coins = String.valueOf(player.getCoins());
		float scoreW = mScorePaint.measureText(coins);
		mCoin.draw(canvas, Screen.SCREEN_WIDTH - 20 - scoreW - mCoin.getWidth(),
				10 + mTextHeight - mCoin.getHeight(), null, mCoinAnim);
		canvas.drawText(coins, Screen.SCREEN_WIDTH - 10 - scoreW,
				10 + mTextHeight, mScorePaint);*/
	}

	public void offset(float value) {
		offset += value;
		if (offset >= mStripLeft.getHeight() * 2)
			offset = 0;
	}
	
	private OnAnimationDraw getCoinAnim(){
		return new OnAnimationDraw() {
			int mInterval, mFrame;
			@Override
			public void setInterval(int interval) {
				mInterval = interval;
			}
			
			@Override
			public void setFrame(int frame) {
				mFrame = frame;
			}
			
			@Override
			public int getInterval() {
				return mInterval;
			}
			
			@Override
			public int getFrame() {
				return mFrame;
			}
		};
	}

	public static final Creator<BackgroundManager> CREATOR = new Creator<BackgroundManager>() {
		
		@Override
		public BackgroundManager[] newArray(int size) {
			return new BackgroundManager[size];
		}
		
		@Override
		public BackgroundManager createFromParcel(Parcel source) {
			return new BackgroundManager(source);
		}
	};
	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeParcelable((Parcelable) mBackGroundTexture, 0);
		dest.writeParcelable((Parcelable) mCloud, 0);
		dest.writeParcelable((Parcelable) mCloudDepth, 0);
		dest.writeParcelableArray(mCloudPos, 0);
		dest.writeParcelable((Parcelable) mCoin, 0);
		dest.writeFloat(mScorePaint.getTextSize());
		dest.writeParcelable((Parcelable) mStripLeft, 0);
		dest.writeParcelable((Parcelable) mStripRight, 0);
		dest.writeFloat(offset);
		dest.writeInt(stripcount);
	}
	
}