package test.particle;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.opengl.GLES20;
import android.opengl.GLU;
import android.opengl.GLSurfaceView.Renderer;
import android.util.Log;

public class ParticleRenderer implements Renderer {
	CParticleManager pm;
	public ParticleRenderer(CParticleManager p1)
	{
		pm = p1;
	}
	@Override
	public void onDrawFrame(GL10 gl) {
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT/*|GL10.GL_DEPTH_BUFFER_BIT*/);
		gl.glLoadIdentity();
		GLU.gluLookAt(gl, 0, 0, 5, 0f, 0f, 0f, 0, 1, 0);
		pm.Draw(gl);
	}

	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {
		if (height <= 0 ) {
			height = 1;
		}
		Log.d("test", "w:"+width+"h:"+height);
		gl.glViewport(0, 0, width, height);
		gl.glMatrixMode(GL10.GL_PROJECTION);
		gl.glLoadIdentity();
		gl.glFrustumf(-5, 5, -5, 5, 1, 100);
		gl.glMatrixMode(GL10.GL_MODELVIEW);
		gl.glLoadIdentity();

	}

	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		gl.glShadeModel(GL10.GL_SMOOTH);
		
		gl.glClearColor(0f, 0f, 0f, 1.0f);
		gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_NICEST);
		gl.glBlendFunc(/*GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA*/ GL10.GL_SRC_ALPHA,  GL10.GL_ONE );
		gl.glDisable(GL10.GL_DEPTH_TEST);
		gl.glEnable(GL10.GL_BLEND);
		gl.glEnable(GL10.GL_TEXTURE_2D);
		pm.LoadTexture(gl);

	}

}
