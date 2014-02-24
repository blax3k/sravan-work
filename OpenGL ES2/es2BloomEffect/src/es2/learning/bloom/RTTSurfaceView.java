package es2.learning.bloom;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class RTTSurfaceView extends GLSurfaceView {

	RTTRenderer renderer;
	float touchedX = 0;
	float touchedY = 0;
	
	public RTTSurfaceView(Context context) {
		super(context);
		setEGLContextClientVersion(2);
		renderer = new RTTRenderer(this);
		setRenderer(renderer);
	}

	public RTTSurfaceView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
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
			
			/*if(renderer.yAngle > 70)
				renderer.yAngle = 70;
			
			if(renderer.xAngle > 70)
				renderer.xAngle = 70;
			
			if(renderer.xAngle < -70)
				renderer.xAngle = -70;
			
			if(renderer.yAngle < -70)
				renderer.yAngle = -70;
			
			Log.d("RTT", "renderer.xAngle:"+renderer.xAngle+" renderer.yAngle:"+renderer.xAngle);*/
			
			touchedX = event.getX();
			touchedY = event.getY();
		}
		return true;
		
	}

}
