package test.particle;

import android.content.Context;
import android.opengl.GLSurfaceView;

public class ParticleView extends GLSurfaceView {

	public ParticleView(Context context) {
		super(context);
		CParticleManager pm = new CParticleManager(this);
		pm.CreateParticles(400);
		pm.setup(0, 0);
		setRenderer(new ParticleRenderer(pm));
	}
}
