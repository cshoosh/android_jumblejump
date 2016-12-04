package Process;

import android.os.Handler;

public class TaskHandler{
	
	private Handler mHandler;
	public TaskHandler() {
		mHandler = new Handler();
	}
	
	
	public void assignTask(long milli, Runnable task){
		
		mHandler.removeCallbacks(task);
		mHandler.postDelayed(task, milli);
	}
	
	
}
