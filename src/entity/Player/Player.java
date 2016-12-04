package Entity.Player;

import Entity.Interfaces.Fallable;
import Entity.Interfaces.OnAnimationDraw;
import Entity.Interfaces.OnAnimationListener;
import Entity.Platforms.DoubleJumpPlatform;
import Entity.Platforms.ElectricPlatform;
import Entity.Platforms.NormalPlatform;
import EntityClasses.Entity;
import LevelDesign.Screen;
import MenuFragments.DeadScreenFragment;
import Process.AllManagers;
import Process.PhysixManager;
import Process.VibrateManager.VibrateAction;
import Resources.ResourceManager;
import Resources.ResourceName;
import Resources.ResourcePlayer;
import android.annotation.TargetApi;
import android.graphics.Canvas;
import android.os.Build;
import android.os.Bundle;

import com.zerotwoone.highjump.GameActivity;
import com.zerotwoone.highjump.R;

public class Player extends Entity implements Fallable, OnAnimationDraw,
		OnAnimationListener {

	private static final int PLAYER_IDLE = 0;
	private static final int PLAYER_FIRE = 1;
	private static final int PLAYER_HANDSUP = 2;

	public Player(AllManagers manager) {
		super(manager);
	}

	// Static Member
	private static final String SHIELD_KEY = "shieldKey";

	boolean isFalling = true, isShielded, isShieldedDraw, isDead,
			isFreeze = true;
	private int mInterval, mFrameCount, mDeadRotation;

	private float offsetShieldX, offsetShieldY;
	private BalloonPlayer mPlayerBalloon;
	private PlayerShield mPlayerShield;

	// Achievement Variables
	int coins, score, spiderCount, snakeCount;
	boolean cliffhanger, toasted, rammer, bigboss;

	public boolean isDead() {
		return isDead;
	}

	public int getScore() {
		return score;
	}

	@Override
	public boolean isFalling() {
		return isFalling;
	}

	public void increaseCoins() {
		coins++;
	}

	public void increaseScore(int score) {
		this.score += score;
	}

	public void increaseSpiderKill() {
		spiderCount++;
	}

	public void increaseSnakeKill() {
		snakeCount++;
	}

	public void cliffHanged() {
		cliffhanger = true;
	}

	public void rammed() {
		rammer = true;
	}

	public void toasted() {
		mManagers.getVibrateManager().vibrate(VibrateAction.PlayerShocked);
		toasted = true;
	}

	public void bigboss() {
		bigboss = true;
	}

	public boolean isFreeze() {
		return isFreeze;
	}

	public int getCoins() {
		return coins;
	}

	public void reInit() {
		init();
	}

	@Override
	protected void init() {
		mDrawables.clear();
		addDrawable(ResourceName.Player, ResourcePlayer.PlayerIdle.name());
		addDrawable(ResourceName.Player, ResourcePlayer.PlayerFire.name());
		addDrawable(ResourceName.Player, ResourcePlayer.PlayerBalloon.name());

		float shieldWidth = mManagers
				.getResourceManager()
				.getResource(ResourceName.Player,
						ResourcePlayer.PlayerShield.name()).getWidth();

		float shieldHeight = mManagers
				.getResourceManager()
				.getResource(ResourceName.Player,
						ResourcePlayer.PlayerShield.name()).getHeight();

		offsetShieldX = shieldWidth / 2f - width() / 2f;
		offsetShieldY = shieldHeight / 2f - height() / 2f;

		mPlayerBalloon = new BalloonPlayer(mManagers, this);
		mPlayerShield = new PlayerShield(mManagers);
	}

	public void start() {
		isFreeze = false;
		jump(this);
	}

	public void jump(Entity from) {
		jump(from, 1);
	}

	public void jump(Entity from, float mul) {
		if (!isDead) {
			offsetTo(left, from.top - height());
			setVelocityY(PhysixManager.BOUNCE_VELOCITY * mul);

			if (from instanceof NormalPlatform)
				mManagers.getSoundManager().play(R.raw.jump);
			else if (from instanceof DoubleJumpPlatform)
				mManagers.getSoundManager().play(R.raw.doublejump);
		}
	}

	public void balloon() {
		isFalling = false;
		isShielded = true;
		mPlayerBalloon.ballooned();
		setLevel(PLAYER_HANDSUP);
		mManagers.getGameLoop().startTask(
				Integer.valueOf(mManagers.getContext().getResources()
						.getString(R.string.BalloonInt)), setFalling);

	}

	public void shield() {
		isShielded = true;
		isShieldedDraw = true;
		mManagers.getGameLoop().startTask(
				Integer.valueOf(mManagers.getContext().getResources()
						.getString(R.string.ShieldInt)), setShielded);

	}

	public boolean isShielded() {
		return isShielded;
	}

	public void hit(Entity entity) {
		if (!isShielded()) {
			if (entity instanceof ElectricPlatform) {
				toasted();
				mManagers.getSoundManager().play(R.raw.player_toasted);
			}
			die();
		}
	}

	@Override
	public void die() {
		super.die();

		if (!isDead) {
			isDead = true;
			mManagers.getSoundManager().play(R.raw.die);
			mManagers.getVibrateManager().vibrate(VibrateAction.PlayerDead);
		}

	}

	public void fire(float angle) {
		if (!isDead) {
			setLevel(PLAYER_FIRE);
			mManagers.getEntityManager().addEntity(new Fireball(angle));
		}
	}

	private Runnable setShielded = new Runnable() {

		@Override
		public void run() {
			Player.this.isShielded = false;
			Player.this.isShieldedDraw = false;
		}
	};

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	public void draw(Canvas canvas) {
		canvas.save();
		if (isDead) {
			mDeadRotation += 10;

			if (mDeadRotation >= 180)
				mDeadRotation = 180;

			canvas.rotate(mDeadRotation, centerX(), centerY());

			if (top > Screen.SCREEN_HEIGHT) {
				final Bundle mAchievementBundle = new Bundle();

				mAchievementBundle.putInt(ResourceManager.translateID(
						R.string.achievement_scorer, mManagers.getContext()),
						score);
				mAchievementBundle.putInt(ResourceManager.translateID(
						R.string.achievement_saver, mManagers.getContext()),
						coins);
				mAchievementBundle.putInt(ResourceManager.translateID(
						R.string.achievement_creepy_spiders,
						mManagers.getContext()), spiderCount);
				mAchievementBundle.putInt(ResourceManager.translateID(
						R.string.achievement_snakes_in_the_jungle,
						mManagers.getContext()), snakeCount);

				mAchievementBundle.putBoolean(ResourceManager.translateID(
						R.string.achievement_big_boss, mManagers.getContext()),
						bigboss);
				mAchievementBundle.putBoolean(ResourceManager.translateID(
						R.string.achievement_cliffhanger,
						mManagers.getContext()), cliffhanger);
				mAchievementBundle.putBoolean(ResourceManager.translateID(
						R.string.achievement_rammer, mManagers.getContext()),
						rammer);
				mAchievementBundle.putBoolean(ResourceManager.translateID(
						R.string.achievement_toasted, mManagers.getContext()),
						toasted);

				mManagers.getGameLoop().pauseGame();

				DeadScreenFragment menu = new DeadScreenFragment();
				GameActivity gameactivity = (GameActivity) GameActivity.getActivity();
				gameactivity.pause(GameActivity.PAUSE_DEAD);
				menu.setArguments(mAchievementBundle);
				menu.show(gameactivity.getSupportFragmentManager(),
						GameActivity.TAG_DEAD_MENU);
			}

		}

		super.draw(canvas);
		canvas.restore();

		if (!isDead) {
			if (isShieldedDraw)
				mPlayerShield.draw(canvas, this.left, this.top, offsetShieldX,
						offsetShieldY);

			if (mPlayerBalloon.isBalloonned()
					|| mPlayerBalloon.isBalloonReleased())
				mPlayerBalloon.draw(canvas, this);
		}

	};

	private Runnable setFalling = new Runnable() {

		@Override
		public void run() {
			Player.this.mPlayerBalloon.release();
			Player.this.isFalling = true;
			// Player.this.isShielded = false;
			setLevel(PLAYER_IDLE);
		}
	};

	@Override
	protected void writeIntoParcel(Bundle bundle) {
		bundle.putBoolean(FALL_KEY, isFalling);
		bundle.putBoolean(SHIELD_KEY, isShielded);
		writeAnimation(bundle, mFrameCount, mInterval);
	}

	@Override
	protected void readFromParcel(Bundle bundle) {
		isShielded = bundle.getBoolean(SHIELD_KEY, false);
		isFalling = bundle.getBoolean(FALL_KEY, true);
		mFrameCount = bundle.getInt(ANIM_FRAME_KEY, 0);
		mInterval = bundle.getInt(ANIM_INTERVAL_KEY, 0);
	}

	@Override
	public void setFalling(boolean falling) {
		isFalling = falling;
	}

	@Override
	public int getFrame() {
		return mFrameCount;
	}

	@Override
	public void setFrame(int frame) {
		mFrameCount = frame;
	}

	@Override
	public int getInterval() {
		return mInterval;
	}

	@Override
	public void setInterval(int interval) {
		mInterval = interval;
	}

	@Override
	public void OnAnimationComplete() {
		if (mPlayerBalloon.isBalloonned())
			setLevel(PLAYER_HANDSUP);
		else
			setLevel(PLAYER_IDLE);
	}

	@Override
	public void OnAnimationFrame(int frame) {

	}

	public class Fireball extends Entity implements Fallable {

		private static final String FIRE_ROTATION_KEY = "fireRotation";
		private float mRotation;

		public Fireball(float angle) {
			super(Player.this.mManagers);

			offsetTo(Player.this.centerX(), Player.this.top);
			float xVelocity = 20 * angle;
			setVelocityX(xVelocity);
			setVelocityY(PhysixManager.BOUNCE_VELOCITY * 1.2f);

			mManagers.getSoundManager().play(R.raw.fire);
		}

		@Override
		public void draw(Canvas canvas) {
			canvas.save();
			canvas.rotate(mRotation, this.centerX(), this.centerY());
			super.draw(canvas);
			canvas.restore();
		}

		@Override
		public void update() {
			super.update();
			mRotation += 20;
			if (mRotation >= 360)
				mRotation = 0;
		}

		@Override
		public boolean isFalling() {
			return true;
		}

		@Override
		protected void init() {
			addDrawable(ResourceName.Fire, "fire");
		}

		@Override
		protected void writeIntoParcel(Bundle bundle) {
			bundle.putFloat(FIRE_ROTATION_KEY, mRotation);
		}

		@Override
		protected void readFromParcel(Bundle bundle) {
			mRotation = bundle.getFloat(FIRE_ROTATION_KEY, 0);
		}

		@Override
		public void setFalling(boolean falling) {

		}

	}
}