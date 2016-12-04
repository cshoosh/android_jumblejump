package Menu;

import java.io.IOException;
import java.io.InputStream;
import java.util.Random;

import Entity.Supportables.BitmapDrawableObject;
import Entity.Supportables.Characters;
import Entity.Supportables.Characters.CharacterAction;
import Entity.Supportables.DrawableObject;
import LevelDesign.Screen;
import Process.PhysixManager;
import Resources.ResourceName;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;

import com.zerotwoone.highjump.R;

public interface LoadingScreen {
	public abstract void draw(Canvas c);

	public abstract boolean isLoaded();

	public abstract void setLoaded();

	public class Builder {
		private LoadingScreen screen;
		private Drawable mGradient;
		private float mPlatformX, mPlatformY, mMonkeyX, mMonkeyY,
				mMonkeyVelocity;
		private DrawableObject mMonkey, mPlatform, mGrass;
		private boolean isLoaded = false;
		private Paint mPaintLoading = new Paint();

		public Builder(Context context) {
			// Loading Initialization
			mPaintLoading.setColor(0xFF3f1600);
			mPaintLoading.setShadowLayer(5f, 0, 0, 0xFF000000);
			mPaintLoading.setStyle(Style.STROKE);
			mPaintLoading.setTextSize(48f);
			mPaintLoading.setTextAlign(Align.CENTER);
			mPaintLoading.setTypeface(Typeface.createFromAsset(context.getAssets(),
					"font/goodtimes.ttf"));

			// Init Monkey and platform
			// Platform
			mPlatform = new BitmapDrawableObject(context, R.drawable.menu_platform);
			mPlatform.setBounds(0, 0,
					ResourceName.getDefaultWidth(ResourceName.Platform, context),
					ResourceName.getDefaultHeight(ResourceName.Platform, context));

			// Monkey
			InputStream bandarIO = null;
			try {
				Characters[] values = Characters.values();
				int random = new Random().nextInt(values.length - 1);
				Characters character = values[random];

				bandarIO = context.getResources().getAssets()
						.open(Characters.getPath(CharacterAction.Idle, character));
			} catch (IOException e) {
				e.printStackTrace();
			}

			float playerDimen = ResourceName.getDefaultWidth(ResourceName.Player,
					context);
			
			Bitmap bandar = BitmapFactory.decodeStream(bandarIO);
			mMonkey = new BitmapDrawableObject(bandar);
			mMonkey.setBounds(0, 0, playerDimen, playerDimen);

			mPlatformX = Screen.SCREEN_WIDTH / 2f - mPlatform.getWidth() / 2f;
			mPlatformY = Screen.SCREEN_HEIGHT / 2f - mPlatform.getHeight() / 2f;

			mMonkeyX = Screen.SCREEN_WIDTH / 2f - mMonkey.getWidth() / 2f;
			mMonkeyY = mPlatformY - mMonkey.getHeight();

			mGradient = context.getResources().getDrawable(
					R.drawable.menu_gradient_bg);
			mGradient.setBounds(0, 0, Screen.SCREEN_WIDTH, Screen.SCREEN_HEIGHT);

			mGrass = new BitmapDrawableObject(context, R.drawable.menu_grass);
			mGrass.setBounds(0, 0, Screen.SCREEN_WIDTH,
					Screen.SCREEN_HEIGHT * 0.203125f);
			
			screen = new LoadingScreen() {

				@Override
				public void draw(Canvas c) {
					if (!isLoaded){
					mGradient.draw(c);
					mPlatform.draw(c, mPlatformX, mPlatformY, null, null);
					mMonkey.draw(c, mMonkeyX, mMonkeyY, null, null);
					c.drawText("Loading ...", Screen.SCREEN_WIDTH / 2f,
							Screen.SCREEN_HEIGHT * 0.7f, mPaintLoading);
					
					mGrass.draw(c, 0, Screen.SCREEN_HEIGHT - mGrass.getHeight(), null, null);
					// Calculate Character Position
					mMonkeyY += mMonkeyVelocity;
					mMonkeyVelocity += PhysixManager.GRAVITY;

					if (mMonkeyY >= mPlatformY - mMonkey.getHeight()) {
						mMonkeyVelocity = PhysixManager.BOUNCE_VELOCITY;
						mMonkeyY = mPlatformY - mMonkey.getHeight();
					}
				}
				}

				@Override
				public boolean isLoaded() {
					return isLoaded;
				}

				@Override
				public void setLoaded() {
					isLoaded = true;
				}
			};
		}

		public LoadingScreen build() {
			return screen;
		}
	}
}
