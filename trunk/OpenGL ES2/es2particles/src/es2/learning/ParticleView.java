package es2.learning;

import android.content.Context;
import android.opengl.GLSurfaceView;

public class ParticleView extends GLSurfaceView {

	public ParticleManager mgr;
	public ParticleView(Context context) {
		super(context);
		
		mgr = new ParticleManager(this);
		mgr.Setup(0, 0, 0);
	}

}
