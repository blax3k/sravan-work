package es2.learning;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView.Renderer;

public class ParticleRenderer implements Renderer {
	int iProgId;
	int iPosition;
	int iTexture;
	int iColor;
	int iTexId;
	ParticleView curView;
	float[] fVertex = {0,0,0};
	FloatBuffer vertexBuffer;
	public ParticleRenderer(ParticleView view) {
		curView = view;
		vertexBuffer = ByteBuffer.allocateDirect(fVertex.length * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
		vertexBuffer.put(fVertex).position(0);
	}

	@Override
	public void onDrawFrame(GL10 gl) {
		GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
		GLES20.glUseProgram(iProgId);
		
		/*GLES20.glVertexAttribPointer(iPosition, 3, GLES20.GL_FLOAT, false, 0, vertexBuffer);
		GLES20.glEnableVertexAttribArray(iPosition);*/
		
		GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, iTexId);
		
		GLES20.glUniform1i(iTexture, 0);
		GLES20.glUniform4f(iColor, 0.5f, 1f, 0.5f, 1f);
		
		curView.mgr.draw(iPosition);
		
//		GLES20.glDrawArrays(GLES20.GL_POINTS, 0, 1);
	}

	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {
		GLES20.glViewport(0, 0, width, height);
	}

	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		
		GLES20.glClearColor(0, 0, 0, 1);
		GLES20.glEnable(GLES20.GL_BLEND);
		GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE);
		
		String strVShader = 
			"attribute vec4 a_Position;" +
			"void main()" +
			"{" +
				"gl_PointSize = 20.0;" +
				"gl_Position = a_Position;" +
			"}";
		
		String strFShader = 
			"precision mediump float;" +
			"uniform sampler2D u_texture;" +
			"uniform vec4 u_color;" +
			"void main()" +
			"{" +
				"vec4 tex = texture2D(u_texture, gl_PointCoord);" +
				"gl_FragColor = u_color * tex;" +
			"}";
		iProgId = Utils.LoadProgram(strVShader, strFShader);
		iPosition = GLES20.glGetAttribLocation(iProgId, "a_Position");
		iTexture = GLES20.glGetUniformLocation(iProgId, "u_texture");
		iColor = GLES20.glGetUniformLocation(iProgId, "u_color");
		iTexId = Utils.LoadTexture(curView, R.drawable.particle);
		
	}

}
