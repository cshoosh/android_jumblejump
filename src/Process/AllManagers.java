package Process;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import Entity.Supportables.Characters;
import LevelDesign.StageManager;
import Menu.Menu;
import Process.SoundManager.SoundType;
import Resources.BackgroundManager;
import Resources.ResourceManager;
import android.app.Activity;
import android.content.Context;

import com.zerotwoone.highjump.R;

public class AllManagers {
	private Activity mActivity;
	private EntityManager mEntityManager;
	private InAppStore mInAppStore;
	private BackgroundManager mBackgroundManager;
	private PhysixManager mPhysixManager;
	private SoundManager mSoundManager;
	private PlayServiceManager mPlayServiceManager;
	private VibrateManager mVibrateManager;
	private StageManager mStageManager;
	private ResourceManager mResourceManager;
	private Menu mMainMenu;
	private GameLoop mLoop;
	
	public AllManagers(Activity activity) throws IOException, ParserConfigurationException, 
			SAXException {
		
		mActivity = activity;
		mResourceManager = new ResourceManager(mActivity, Characters.Bandar);
		mBackgroundManager = new BackgroundManager(mActivity);
		mEntityManager = new EntityManager(this);
		mInAppStore = new InAppStore(mActivity);
		mSoundManager = new SoundManager(mActivity);
		mStageManager = new StageManager();
		mPhysixManager = new PhysixManager(mActivity, mStageManager);
		mPlayServiceManager = new PlayServiceManager(mActivity);
		mVibrateManager = new VibrateManager(mActivity);
		mMainMenu = (Menu) activity.findViewById(R.id.menu);
		
		mStageManager.init(this);
	}
	
	public void reset (){
		mEntityManager.reset(this);
		mSoundManager.resetSoundPool();
		mStageManager.reset();
		PhysixManager.reset();
	}
	
	public void destroy(){
		mPhysixManager.pausePhysix();
		mInAppStore.destroy();
		mSoundManager.destroy();
		mPlayServiceManager.pause();
	}
	public void pause(){
		mSoundManager.pause(SoundType.Game);
		mPhysixManager.pausePhysix();
	}
	
	public void resume(){
		mSoundManager.resume(SoundType.Game);
		mPhysixManager.resumePhysix();
	}
	
	public void setGameLoop(GameLoop loop){
		mLoop = loop;
	}
	
	public Context getContext(){
		return mActivity;
	}
	
	public GameLoop getGameLoop(){
		return mLoop;
	}
	public ResourceManager getResourceManager(){
		return mResourceManager;
	}
	public EntityManager getEntityManager(){
		return mEntityManager;
	}
	public InAppStore getInAppStore(){
		return mInAppStore;
	}
	public BackgroundManager getBackgroundManager(){
		return mBackgroundManager;
	}
	public PhysixManager getPhysixManager(){
		return mPhysixManager;
	}
	public SoundManager getSoundManager(){
		return mSoundManager;
	}
	public PlayServiceManager getPlayServiceManager(){
		return mPlayServiceManager;
	}
	public VibrateManager getVibrateManager(){
		return mVibrateManager;
	}
	public StageManager getStageManager(){
		return mStageManager;
	}
	public Menu getMainMenu(){
		return mMainMenu;
	}
}
