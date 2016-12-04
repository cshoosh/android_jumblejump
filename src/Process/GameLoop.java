package Process;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import Entity.Supportables.Characters;
import android.app.Activity;
import android.graphics.Canvas;
import android.view.SurfaceHolder;

public final class GameLoop extends Thread {

	// Variables
	private SurfaceHolder mHolder;
	private boolean isAlive = true;
	private boolean isPaused = false;

	// Static Fields

	// Game References
	private Activity mActivity;
	private TaskHandler mTaskHandler;
	private Characters mTheme;

	// Game Managers
	private AllManagers mManager;

	// Constants
	private static final int MAX_FPS = 50;
	private static final float FPS_COUNT = 1000f / MAX_FPS;

	// Constructor
	public GameLoop(SurfaceHolder holder, Activity gameActivity,
			AllManagers manager, Characters theme)
			throws ParserConfigurationException, SAXException, IOException,
			IllegalAccessException, IllegalArgumentException {
		super("GameLoop");

		mHolder = holder;
		mTheme = theme;
		mActivity = gameActivity;
		mManager = manager;

		mTaskHandler = new TaskHandler();

		mManager.setGameLoop(GameLoop.this);
		mManager.getResourceManager().setPlayerResource(mTheme);
	}

	public void restart() throws ParserConfigurationException, SAXException,
			IOException {

		mManager.reset();
		resumeGame();
		mTaskHandler.assignTask(1000, startPlayer);
	}

	private Runnable startPlayer = new Runnable() {

		@Override
		public void run() {
			mManager.getEntityManager().getPlayer().start();
		}
	};

	@Override
	public void run() {

		long mPrevTime;
		long diff;
		long sleepTime;

		try {
			Thread.sleep(1000);
			restart();
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		while (isAlive) {

			if (!isPaused) {
				mPrevTime = System.currentTimeMillis();
				refresh();
				diff = System.currentTimeMillis() - mPrevTime;
				sleepTime = (long) (FPS_COUNT - diff);

				if (sleepTime >= 0)
					try {
						Thread.sleep(sleepTime);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
			} else {
				synchronized (this) {
					try {
						wait(1000);
						refresh();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}

		mActivity.runOnUiThread(new Runnable() {

			@Override
			public void run() {
				if (!mActivity.isFinishing())
					mActivity.finish();
			}
		});
	}

	private void refresh() {
		Canvas c = null;
		try {
			c = mHolder.lockCanvas();
			Update(c);
		} catch (Exception e) {
			e.printStackTrace();
			KillGame();
		} finally {

			if (c != null)
				mHolder.unlockCanvasAndPost(c);
		}
	}

	// Game Loop executions
	public void KillGame() {
		isAlive = false;

		mManager.reset();
		mManager.pause();
		synchronized (this) {
			notifyAll();
		}

	}

	public void pauseGame() {
		
		isPaused = true;
		mManager.pause();
	}

	public boolean isPaused(){
		return isPaused;
	}
	public void resumeGame() {
		isPaused = false;
		synchronized (this) {
			notifyAll();
		}
		mManager.resume();
	}

	private void Update(Canvas c) {
		if (c != null) {
			

			// PreProcesses
			mManager.getBackgroundManager().preDraw(c);

			// Main Process
			if (!isPaused) {
				mManager.getEntityManager().draw(c);

				mManager.getPhysixManager().calculatePhysix(
						mManager.getEntityManager());
				// Process Entity Manager
				mManager.getEntityManager().processAddQue();
				mManager.getEntityManager().processremoveQue();
			}
			// Post Processing
			mManager.getBackgroundManager().postDraw(c);

			// Draw HUD
			mManager.getBackgroundManager().drawHud(c,
					mManager.getEntityManager().getPlayer());

		}
	}

	public void startTask(final long milli, final Runnable task) {
		mTaskHandler.assignTask(milli, task);
	}
}