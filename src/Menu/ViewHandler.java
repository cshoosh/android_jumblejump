package Menu;


import java.util.ArrayList;

import Menu.TouchableDrawable.TouchableTypes;
import android.graphics.Canvas;
import android.view.MotionEvent;

public class ViewHandler {
	private ArrayList<TouchableDrawable> mArray = new ArrayList<TouchableDrawable>();
	
	public ViewHandler() {
		mArray.clear();
	}
	
	public synchronized TouchableDrawable getTouchable(TouchableTypes type){
		for (TouchableDrawable d:mArray)
			if (d.getID() == type)
				return d;
		
		return null;						
	}
	public synchronized void draw (Canvas canvas){
		for (TouchableDrawable draw:mArray){
			draw.draw(canvas, 0, 0, null, null);
		}
	}
	
	public void addTouchable (TouchableDrawable drawable, float x, float y){
		drawable.offsetTo(x, y);
		mArray.add(drawable);
	}
	
	
	public synchronized boolean OnTouch(MotionEvent event){
		
		for(TouchableDrawable drawable:mArray)
			if (drawable.OnTouch(event))
				return true;
		
		return false;
	}
}
