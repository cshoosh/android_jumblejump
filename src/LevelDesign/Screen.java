package LevelDesign;

import Entity.Supportables.EntityInfo;
import EntityClasses.Entity;
import Process.EntityManager;
import android.graphics.Point;
import android.graphics.RectF;

public class Screen{
	public static int SCREEN_WIDTH;
	public static int SCREEN_HEIGHT;

	
	private static final int NO_SCREENS = 4;
	private static final RectF mBounds = new RectF();

	//private int count;

	public Screen(StageManager manager) {
		mParser = new StageParser();
		mStageManager = manager;
		
		

	}
	
	private boolean isParsed;
	private StageParser mParser;
	private StageManager mStageManager;
	
	public void init() {
		float Y = -SCREEN_HEIGHT * (NO_SCREENS - 1);

		mBounds.set(0, Y, SCREEN_WIDTH,	SCREEN_HEIGHT);
		mStageManager.ParseFromXML(mParser);

		for (int i = 0; i < mParser.getEntityCount(); i++) {
			Entity e = EntityManager.createEntity(mParser.getInfo(i), mStageManager.mManagers);
			e.offset(0, Y);

			mStageManager.mManagers.getEntityManager().addEntity(e);
		}
	}

	public void offsetScreen(float dx, float dy) {
		for (Entity e : mStageManager.mManagers.getEntityManager().getEntities())
			e.offset(dx, dy);

		mStageManager.mManagers.getEntityManager().getPlayer().offset(dx, dy);
		mStageManager.mManagers.getEntityManager().getPlayer().increaseScore((int)dy);
		
		mStageManager.mManagers.getBackgroundManager().offset(dy/4f);
		
		mBounds.offset(dx, dy);

		if (mBounds.top >= -SCREEN_HEIGHT * 2) {
			if (!isParsed) {
				isParsed = true;
				
				new Thread(new Runnable() {
					
					@Override
					public void run() {
						repositionScreen();
						/*count++;
						if (count % 4 == 0){
							GameLoop.getSound().resetLoopables(mInitContext);
						}*/
						isParsed = false;

					}
				},"ParsingStage").start();				
			}
		}
	}

	public void setBounds(RectF bounds){
		mBounds.set(bounds);
	}
	
	public RectF getBounds(){
		return new RectF(mBounds);
	}
	private synchronized void repositionScreen() {
		System.gc();
		mStageManager.ParseFromXML(mParser);	
		float offsetY = -SCREEN_HEIGHT * (NO_SCREENS);
		
		for (int i = 0; i < mParser.getEntityCount(); i++) {
			EntityInfo info = mParser.getInfo(i);
			info.offset(0, mBounds.top);
			info.offset(0, offsetY);
			mStageManager.mManagers.getEntityManager().addEntity(info, mStageManager.mManagers);
		}
		mBounds.offset(0, offsetY);
	}

	public static final void setScreenDimension(Point dimen) {
		SCREEN_WIDTH = dimen.x;
		SCREEN_HEIGHT = dimen.y;
	}
}