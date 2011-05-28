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
	int iMove;
	int iTimes;
	int iLife, iAge;
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
//		GLES20.glUniform4f(iColor, 0.5f, 1f, 0.5f, 1f);
		
		curView.mgr.draw(iPosition, iMove, iTimes, iColor, iLife, iAge);
		
//		GLES20.glDrawArrays(GLES20.GL_POINTS, 0, 1);
	}

	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {
		GLES20.glViewport(0, 0, width, height);
	}

	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		
		GLES20.glClearColor(0, 0, 0, 1);
		GLES20.glDisable(GLES20.GL_DEPTH_TEST);
		GLES20.glEnable(GLES20.GL_BLEND);
		GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE);
		
		String strVShader = 
			"precision mediump float;" +
			"attribute vec4 a_Position;" +
			"attribute vec4 a_move;" +
			"uniform float a_time;" +
			"attribute vec4 a_color;" +
			"varying vec4 v_color;" +
			"attribute float a_life;" +
			"attribute float a_age;" +
			"varying float alpha;" +
			"float time;" +
			"void main()" +
			"{" +
				"alpha = a_life - (a_time * 10.0 * a_age);" +
				"time = a_time;" +
				"if (alpha < 0.0)" +
				"{" +
					"float td = a_life/a_age;" +
					"td /= 10.0;" +
					"float df = a_time/td;" +
					"int div = int(df);" +
					"df = float(div);" +
					"td *= df;" +
					"time = a_time - td;" +
					"alpha = a_life - (time * 10.0 * a_age);" +
				"}" +
				"gl_PointSize = 10.0;" +
				"v_color = a_color;" +
				"gl_Position = a_Position;" +				
				"gl_Position += (time * a_move * 0.5);" +
				"gl_Position.w = 1.0;" +
			"}";
		
		String strFShader = 
			"precision mediump float;" +
			"uniform sampler2D u_texture;" +
			"varying vec4 v_color;" +
			"varying float alpha;" +
			"void main()" +
			"{" +				
				"vec4 tex = texture2D(u_texture, gl_PointCoord);" +
				"gl_FragColor = v_color * tex;" +
				"gl_FragColor.w = alpha;" +
			"}";
		iProgId = Utils.LoadProgram(strVShader, strFShader);
		iTexId = Utils.LoadTexture(curView, R.drawable.particle);
		
		iPosition = GLES20.glGetAttribLocation(iProgId, "a_Position");
		iTexture = GLES20.glGetUniformLocation(iProgId, "u_texture");
		iColor = GLES20.glGetAttribLocation(iProgId, "a_color");
		iMove = GLES20.glGetAttribLocation(iProgId, "a_move");
		iTimes = GLES20.glGetUniformLocation(iProgId, "a_time"); 
		iLife = GLES20.glGetAttribLocation(iProgId, "a_life");
		iAge = GLES20.glGetAttribLocation(iProgId, "a_age");
	}
	
}
