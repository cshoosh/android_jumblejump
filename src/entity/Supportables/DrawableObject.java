package Entity.Supportables;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

public abstract class DrawableObject{
	
	public abstract void draw (Canvas canvas, float left, float top, Paint paint, Object intr);
	public abstract void setBounds(float left, float top, float right, float bottom);
	public abstract float getWidth();
	public abstract float getHeight();
			
	public abstract void release();
	
	public static final Bitmap scaleBitmap (Bitmap bmp, int width, int height){
		Bitmap bmpScaled = Bitmap.createScaledBitmap(bmp,
				width, height, false);
		//bmp.recycle();
		
		return bmpScaled;
	}
		
}