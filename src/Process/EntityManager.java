package Process;

import java.util.ArrayList;

import Entity.Player.Player;
import Entity.Supportables.EnemyList;
import Entity.Supportables.EntityInfo;
import Entity.Supportables.PlatformType;
import Entity.Supportables.TypeAttributeList;
import EntityClasses.Entity;
import android.graphics.Canvas;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;


public final class EntityManager implements Parcelable{

	private ArrayList<Entity> mAddQue = new ArrayList<Entity>();
	private ArrayList<Entity> mRemoveQue = new ArrayList<Entity>();
	private ArrayList<Entity> mEntityAll = new ArrayList<Entity>();
	/*private static final ArrayList<Entity> mEntityFireball = new ArrayList<Entity>();
	private static final ArrayList<Entity> mEntityEnemies = new ArrayList<Entity>();
	private static final ArrayList<Entity> mEntityItems = new ArrayList<Entity>();
	private static final ArrayList<Entity> mEntityPlatforms = new ArrayList<Entity>();*/

	private Player mPlayer;

	public EntityManager(AllManagers manager) {
		reset(manager);
	}
	
	public EntityManager(Parcel source){
		Bundle bundle = source.readBundle();
		mEntityAll = bundle.getParcelableArrayList("all");
		mAddQue = bundle.getParcelableArrayList("add");
		mRemoveQue = bundle.getParcelableArrayList("remove");
		mPlayer = bundle.getParcelable("player");
	}

	public synchronized void reset(AllManagers manager) {
		for (Entity e : mEntityAll) {
			e.die();
		}

		mAddQue.clear();
		mRemoveQue.clear();
		mEntityAll.clear();
		addEntity(EntityInfo.getPlayerInfo(), manager);
	}

	public synchronized void addEntity(EntityInfo info, AllManagers context) {
		addEntity(createEntity(info, context));
	}

	public synchronized void addEntity(Entity e) {
		if (e instanceof Player)
			mPlayer = (Player) e;
		else
			mAddQue.add(e);
	}

	public synchronized void removeEntity(Entity e) {
		mRemoveQue.add(e);
	}

	public synchronized void processAddQue() {
		for (Entity e : mAddQue) {
			/*if (e instanceof Fireball)
				mEntityFireball.add(e);
			else if (e instanceof Platform)
				mEntityPlatforms.add(e);
			else if (e instanceof Item)
				mEntityItems.add(e);
			else if (e instanceof Enemy)
				mEntityEnemies.add(e);*/

			mEntityAll.add(e);
		}

		mAddQue.clear();
	}

	public synchronized void processremoveQue() {
		for (Entity e : mRemoveQue) {
			/*if (e instanceof Fireball)
				mEntityFireball.remove(e);
			else if (e instanceof Platform)
				mEntityPlatforms.remove(e);
			else if (e instanceof Item)
				mEntityItems.remove(e);
			else if (e instanceof Enemy)
				mEntityEnemies.remove(e);*/

			mEntityAll.remove(e);
		}

		mRemoveQue.clear();
	}

	public synchronized void draw(Canvas canvas) {
		for (Entity e : getEntities())
			e.draw(canvas);
		mPlayer.draw(canvas);
	}

	public ArrayList<Entity> getEntities() {
		
		return mEntityAll;
	}

	public static final Entity createEntity(EntityInfo info, AllManagers context) {
		Entity e = null;
		switch (EntityInfo.translateType(info
				.getAttributeValue(TypeAttributeList.type))) {

		case Enemy:
			e = EnemyList.getEnemyIstance(info, context);
			break;
		case Platform:
			e = PlatformType.getInstance(info, context);
			break;
		case Player:
			e = new Player(context);
			break;

		default:
			break;
		}

		if (e != null) {
			e.offsetTo(info.getX(), info.getY());
			e.setInfo(info);
		}
		return e;
	}

	public final Player getPlayer() {
		return mPlayer;
	}

	public static final Creator<EntityManager> CREATOR = new Creator<EntityManager>() {

		@Override
		public EntityManager createFromParcel(Parcel source) {
			return new EntityManager(source);
		}

		@Override
		public EntityManager[] newArray(int size) {
			return new EntityManager[size];
		}
	};
	
	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		Bundle bundle = new Bundle();
		
		bundle.putParcelableArrayList("all", mEntityAll);
		bundle.putParcelableArrayList("add", mAddQue);
		bundle.putParcelableArrayList("remove", mRemoveQue);
		bundle.putParcelable("player", mPlayer);
		
		dest.writeBundle(bundle);
	}
}