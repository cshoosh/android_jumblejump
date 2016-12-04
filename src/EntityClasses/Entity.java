package EntityClasses;

import java.util.ArrayList;

import Entity.Interfaces.Collidable;
import Entity.Interfaces.Fallable;
import Entity.Interfaces.OnOffsetChangeListener;
import Entity.Player.Player.Fireball;
import Entity.Supportables.DrawableObject;
import Entity.Supportables.EntityInfo;
import LevelDesign.Screen;
import Process.AllManagers;
import Process.EntityManager;
import Process.PhysixManager;
import Resources.ResourceName;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

import com.zerotwoone.highjump.MainMenuActivity;

public abstract class Entity extends RectF implements Parcelable {

	protected static final String ANIM_FRAME_KEY = "animFrameKey";
	protected static final String ANIM_INTERVAL_KEY = "animIntervalKey";

	private static final String ENTITY_INFO_KEY = "entityInfoKey";
	private static final RectF mCollidable = new RectF();
	private static final RectF mColliding = new RectF();

	private float mVelocityX, mVelocityY;
	private int mLevel;
	private EntityInfo mInfo;
	private OnOffsetChangeListener mOffsetListener;

	protected ArrayList<DrawableObject> mDrawables = new ArrayList<DrawableObject>();
	protected Paint mPaint;

	protected AllManagers mManagers;
	
	public Entity(AllManagers manager) {
		mManagers = manager;
		init();
	}

	protected void setLevel(int level) {
		mLevel = level;
	}

	protected void setWidth(float width) {
		this.right = this.left + width;
	}
	
	protected void setHeight(float height) {
		this.bottom = this.top + height;
	}

	protected void addDrawable(ResourceName name, String sub) {
		DrawableObject objAdd = mManagers.getResourceManager().getResource(name, sub);
		
		setWidth(objAdd.getWidth());
		setHeight(objAdd.getHeight());
		mDrawables.add(objAdd);
	}

	protected void addDrawable(DrawableObject obj) {
		setWidth(obj.getWidth());
		setHeight(obj.getHeight());
		mDrawables.add(obj);
	}
	private void checkDies() {
		if (top >= Screen.SCREEN_HEIGHT)
			die();
	}

	public void setInfo(EntityInfo info) {
		mInfo = info;
	}

	public void increaseXVelocity(float vel) {
		mVelocityX += vel;
	}

	public void increaseYVelocity(float vel) {
		mVelocityY += vel;
	}

	public void setVelocityX(float vel) {
		mVelocityX = vel;
	}

	public void setVelocityY(float vel) {
		mVelocityY = vel;
	}

	public Paint getPaint() {
		return mPaint;
	}

	public void draw(Canvas canvas) {
		if (this.top > -height())
			mDrawables.get(mLevel).draw(canvas, left, top, getPaint(), this);
	}

	public void die() {
		mManagers.getEntityManager().removeEntity(this);
	}

	public void update() {
		if (this instanceof Fallable && ((Fallable) this).isFalling())
			increaseYVelocity(PhysixManager.GRAVITY);

		if (this instanceof Collidable)
			isCollided((Collidable) this, mManagers.getEntityManager().getPlayer());

		if (this instanceof Fireball) {
			for (Entity e : mManagers.getEntityManager().getEntities())
				if (e instanceof Collidable && e instanceof Enemy)
					isCollided((Collidable) e, this);
		}

		offset(getVelocityX(), getVelocityY());
		
		checkDies();
	}

	@Override
	public void offset(float dx, float dy) {
		super.offset(dx, dy);

		if (mOffsetListener != null)
			mOffsetListener.OnOffsetChanged(this);
	}

	@Override
	public void offsetTo(float newLeft, float newTop) {
		super.offsetTo(newLeft, newTop);

		if (mOffsetListener != null)
			mOffsetListener.OnOffsetChanged(this);
	}

	public void setOnOffsetChangeListener(OnOffsetChangeListener listener) {
		this.mOffsetListener = listener;
	}

	public float getVelocityX() {
		return mVelocityX;
	}

	public float getVelocityY() {
		return Math.max(Math.min(mVelocityY, 30), -30);
	}

	protected abstract void init();
	protected abstract void writeIntoParcel(Bundle bundle);
	protected abstract void readFromParcel(Bundle bundle);
	

	protected static final void writeAnimation(Bundle bundle, int frame,
			int interval) {
		bundle.putInt(ANIM_FRAME_KEY, frame);
		bundle.putInt(ANIM_INTERVAL_KEY, interval);
	}

	@Override
	public void writeToParcel(Parcel out, int flags) {

		Bundle mBundle = new Bundle();
		mBundle.putParcelable(ENTITY_INFO_KEY, mInfo);
		
		writeIntoParcel(mBundle);
		
		out.writeBundle(mBundle);
		out.writeFloat(left);
		out.writeFloat(top);
		out.writeFloat(right);
		out.writeFloat(bottom);
		out.writeFloat(mVelocityX);
		out.writeFloat(mVelocityY);
		out.writeInt(mLevel);
	}

	public static final Creator<Entity> CREATOR = new Creator<Entity>() {

		@Override
		public Entity[] newArray(int size) {
			return new Entity[size];
		}

		@Override
		public Entity createFromParcel(Parcel source) {
			Bundle mBundle = source.readBundle();
			EntityInfo info = mBundle.getParcelable(ENTITY_INFO_KEY);

			Entity e = EntityManager.createEntity(info, MainMenuActivity.getManager());
			
			e.readFromParcel(mBundle);
			e.left = source.readFloat();
			e.top = source.readFloat();
			e.right = source.readFloat();
			e.bottom = source.readFloat();
			e.mVelocityX = source.readFloat();
			e.mVelocityY = source.readFloat();
			e.mLevel = source.readInt();

			return e;
		}
	};

	protected static final void isCollided(Collidable c, Entity colliding) {
		mCollidable.set((RectF) c);
		mColliding.set(colliding);

		mColliding.inset(colliding.width() / 4f, 0);

		if (RectF.intersects(mCollidable, mColliding))
			c.CollideInside(colliding);

		if (colliding.getVelocityY() > 0) {
			mCollidable.set(mCollidable.left, mCollidable.top,
					mCollidable.right, mCollidable.top + 5);
			mColliding.set(mColliding.left, mColliding.bottom,
					mColliding.right,
					mColliding.bottom + colliding.getVelocityY());

			if (RectF.intersects(mCollidable, mColliding))
				c.CollideTop(colliding);
		} else {
			mCollidable.set(mCollidable.left, mCollidable.bottom - 5,
					mCollidable.right, mCollidable.bottom);
			mColliding.set(mColliding.left,
					mColliding.top + colliding.getVelocityY(),
					mColliding.right, mColliding.top);

			if (RectF.intersects(mCollidable, mColliding))
				c.CollideBottom(colliding);
		}

	}
}