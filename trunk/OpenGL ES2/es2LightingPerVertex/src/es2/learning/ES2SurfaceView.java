package es2.learning;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.view.MotionEvent;

public class ES2SurfaceView extends GLSurfaceView {

	float touchedX = 0;
	float touchedY = 0;
	PerVertexRenderer renderer;
	public ES2SurfaceView(Context context) {
		super(context);
		setEGLContextClientVersion(2);
        setRenderer(renderer = new PerVertexRenderer(this));
	}

	@Override
	public boolean onTouchEvent(MotionEvent event)
	{
		if (event.getAction() == MotionEvent.ACTION_DOWN)
		{
			touchedX = event.getX();
			touchedY = event.getY();
		} else if (event.getAction() == MotionEvent.ACTION_MOVE)
		{
			renderer.xAngle += (touchedX - event.getX())/2f;
			renderer.yAngle += (touchedY - event.getY())/2f;
			
			touchedX = event.getX();
			touchedY = event.getY();
		}
		return true;
		
	}
}
