package Entity.Platforms;

import java.util.Random;

import Entity.Player.Player;
import Entity.Supportables.EntityInfo;
import EntityClasses.Entity;
import LevelDesign.Screen;
import Process.AllManagers;
import Resources.ResourceName;
import Resources.ResourcePlatform;
import android.graphics.Canvas;
import android.os.Bundle;

import com.zerotwoone.highjump.R;

public class Removable extends NormalPlatform {

	Leaf[] mLeafArray = new Leaf[8];
	boolean isReleased;
	
	private boolean consumed;
	public Removable(EntityInfo info, AllManagers managers) {
		super(info, managers);
		
		for (int i = 0; i <mLeafArray.length;i++){
			mLeafArray[i] = new Leaf(managers, this);			
		}
	}

	@Override
	public void CollideTop(Entity who) {
		if (!consumed)
			if (who instanceof Player){
				((Player) who).jump(this);
				mManagers.getSoundManager().play(R.raw.drop);
			}
		
		consumed = true;
		
		for (Leaf leaf:mLeafArray)
			leaf.destroy();
	}

	
	@Override
	public void draw(Canvas canvas) {
		for (int i = 0;i < mLeafArray.length;i++){
			float portion = width() / mLeafArray.length;
			if (i % 2 == 0)
				mLeafArray[i].draw(canvas, this.left + (portion * i), this.top);
			else
				mLeafArray[i].draw(canvas, this.left + (portion * i), this.top + height() /2f);

		}
		if (isReleased){
			mLeafArray = null;
			this.die();
		}
	}

	@Override
	protected void init() {
		setWidth(ResourceName.getDefaultWidth(ResourceName.Platform, mManagers.getContext()));
	}

	@Override
	protected void writeIntoParcel(Bundle bundle) {

	}

	@Override
	protected void readFromParcel(Bundle bundle) {

	}
	
	public void release(){
		isReleased = true;
	}
	
	private class Leaf extends Entity {

		private float mRotation;
		private float mXBlast, mYBlast;
		private boolean isDestroyed;
		private int counter;
		private Entity parent;
		
		public Leaf(AllManagers manager,  Entity parent) {
			super(manager);
			this.parent = parent;
		}

		@Override
		protected void init() {
			Random rand = new Random();
			
			int leafRand = rand.nextInt(2);
			switch (leafRand) {
			case 0:
				addDrawable(ResourceName.Platform, ResourcePlatform.Leaf1.name());
				break;
			case 1:
				addDrawable(ResourceName.Platform, ResourcePlatform.Leaf2.name());
				break;
			
			default:
				break;
			}
			
			
			mRotation = rand.nextInt(360);
			mXBlast = rand.nextInt((int) (Screen.SCREEN_WIDTH * 0.015f));
			mYBlast = rand.nextInt((int) (Screen.SCREEN_HEIGHT * .01f));
		}
		
		public void destroy(){
			isDestroyed = true;
		}
		
		public void draw(Canvas canvas, float left, float top) {
			canvas.save();
			offsetTo(left, top);
			if (isDestroyed){
				offset(mXBlast * counter, mYBlast * counter);
				mRotation += 10;
				if (counter == 15){
					if (parent instanceof Removable)
						((Removable)parent).release();					
				}
				counter++;
			}
			
			canvas.rotate(mRotation, Leaf.this.centerX(), Leaf.this.centerY());
			super.draw(canvas);
			canvas.restore();
		}

		@Override
		protected void writeIntoParcel(Bundle bundle) {
			
		}

		@Override
		protected void readFromParcel(Bundle bundle) {
			
		}

	}

}
