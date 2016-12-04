package EntityClasses;

import Entity.Enemies.Boss;
import Entity.Enemies.Knight;
import Entity.Enemies.Pawn;
import Entity.Interfaces.Collidable;
import Entity.Interfaces.Fallable;
import Entity.Interfaces.OnAnimationDraw;
import Entity.Player.Player;
import Entity.Player.Player.Fireball;
import Entity.Supportables.EntityInfo;
import Entity.Supportables.TypeAttributeList;
import Process.AllManagers;
import android.os.Bundle;



public abstract class Enemy extends Entity implements Collidable, Fallable,
		OnAnimationDraw {

	public static final String HP_KEY = "hpKey";

	// For future compatibility
	//private static final int ENEMY_HIT = 1;

	private boolean isFalling;
	protected int hp = 1;
	private int mFrame, mInterval;

	public Enemy(EntityInfo info, AllManagers context) {
		super(context);
		try {
			String hpString = info.getAttributeValue(TypeAttributeList.hp);
			this.hp = Integer.valueOf(hpString);
		} catch (NumberFormatException e) {
			e.printStackTrace();
			this.hp = 1;
		}
	}

	@Override
	protected void writeIntoParcel(Bundle bundle) {
		bundle.putBoolean(FALL_KEY, isFalling);
		bundle.putInt(HP_KEY, hp);
	}

	@Override
	protected void readFromParcel(Bundle bundle) {
		isFalling = bundle.getBoolean(FALL_KEY, false);
		hp = bundle.getInt(HP_KEY, 1);
	}

	@Override
	public void CollideTop(Entity who) {
		if (who instanceof Player) {
			if (!(this instanceof Boss))
				setFalling(true);
			else {
				this.hp -= 2;
				if (this.hp <= 0)
					setFalling(true);
			}
			((Player) who).jump(this);
		}
	}

	@Override
	public void CollideInside(Entity who) {
		if (who instanceof Player) {

			if (!this.isFalling())
				((Player) who).hit(this);
			if (((Player) who).isShielded() && !this.isFalling)
				((Player) who).rammed();
		}

		else if (who instanceof Fireball && hp > 0) {
			--hp;
			who.die();
			if (this.hp <= 0) {
				setFalling(true);
				score();
				
				if (this instanceof Pawn)
					mManagers.getEntityManager().getPlayer().increaseSpiderKill();
				if (this instanceof Knight)
					mManagers.getEntityManager().getPlayer().increaseSnakeKill();
			}
		}
	}

	@Override
	public void CollideBottom(Entity who) {

	}

	@Override
	public boolean isFalling() {
		return isFalling;
	}

	@Override
	public void setFalling(boolean falling) {
		isFalling = falling;
	}

	@Override
	public void setFrame(int frame) {
		mFrame = frame;
	}

	@Override
	public int getFrame() {
		return mFrame;
	}

	@Override
	public void setInterval(int interval) {
		mInterval = interval;
	}

	@Override
	public int getInterval() {
		return mInterval;
	}

	protected abstract void score();

}