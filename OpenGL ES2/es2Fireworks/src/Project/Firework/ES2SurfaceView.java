package Project.Firework;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.view.MotionEvent;

public class ES2SurfaceView extends GLSurfaceView {

	float touchedX = 0;
	float touchedY = 0;
	FireworksRenderer renderer;
	public ES2SurfaceView(Context context) {
		super(context);
		setEGLContextClientVersion(2);
        setRenderer(renderer = new FireworksRenderer(this));
	}

	@Override
	public boolean onTouchEvent(MotionEvent event)
	{
		/*if (event.getAction() == MotionEvent.ACTION_DOWN)
		{
			touchedX = event.getX();
			touchedY = event.getY();
		} else if (event.getAction() == MotionEvent.ACTION_MOVE)
		{
			renderer.dx += (touchedX - event.getX())/100f;
			renderer.dy += (touchedY - event.getY())/100f;
			
			touchedX = event.getX();
			touchedY = event.getY();
		}*/
		return true;
		
	}
	
	public void LoadProgram(int id)
	{
//		renderer.LoadProgram(id);
	}
}
