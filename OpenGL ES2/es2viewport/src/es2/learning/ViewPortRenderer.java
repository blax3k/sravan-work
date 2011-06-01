package es2.learning;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView.Renderer;
import android.opengl.Matrix;

public class ViewPortRenderer implements Renderer {

	int iProgId;
	int iPosition;
	int iColor;
	int iVPMatrix;
	
	public float xAngle = 0;
	public float yAngle = 0;
	
	float[] m_fProjMatrix = new float[16];
	float[] m_fViewMatrix = new float[16];
	float[] m_fIdentity = new float[16];
	float[] m_fVPMatrix = new float[16];
	
	float[] cube = {
		2,2,0,
		-2,2,0,
		-2,-2,0,
		2,-2,0,
		2,2,-2,
		-2,2,-2,
		-2,-2,-2,
		2,-2,-2
	};
	
	float[] colors = {
		1,0,0,
		0,1,0,
		0,0,1,
		1,1,0,
		1,0,1,
		0,1,1,
		1,1,1,
		0,0,0
	};
	
	short[] indeces = {
			0,1,2,
			0,2,3,//back
			
			0,4,7,//right
			0,7,3,
			
			7,6,2,//bottom
			7,2,3,
			
			4,5,6,//front
			4,6,7,
			
			5,1,2,//left
			5,2,6,
			
			0,1,5,
			0,5,4//top
			};
	
	FloatBuffer cubeBuffer = null;
	FloatBuffer colorBuffer = null;
	ShortBuffer indexBuffer = null;
	
	
	public ViewPortRenderer()
	{
		cubeBuffer = ByteBuffer.allocateDirect(cube.length * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
		cubeBuffer.put(cube).position(0);
		
		colorBuffer = ByteBuffer.allocateDirect(colors.length * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
		colorBuffer.put(colors).position(0);
		
		indexBuffer = ByteBuffer.allocateDirect(indeces.length * 4).order(ByteOrder.nativeOrder()).asShortBuffer();
		indexBuffer.put(indeces).position(0);
	}
	
	@Override
	public void onDrawFrame(GL10 arg0) {
		GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
		GLES20.glUseProgram(iProgId);
		cubeBuffer.position(0);
		GLES20.glVertexAttribPointer(iPosition, 3, GLES20.GL_FLOAT, false, 0, cubeBuffer);
		GLES20.glEnableVertexAttribArray(iPosition);
		
		colorBuffer.position(0);
		GLES20.glVertexAttribPointer(iColor, 3, GLES20.GL_FLOAT, false, 0, colorBuffer);
		GLES20.glEnableVertexAttribArray(iColor);
		
		Matrix.setIdentityM(m_fIdentity, 0);
		Matrix.rotateM(m_fIdentity, 0, -xAngle, 0, 1, 0);
		Matrix.rotateM(m_fIdentity, 0, -yAngle, 1, 0, 0);
		Matrix.multiplyMM(m_fVPMatrix, 0, m_fViewMatrix, 0, m_fIdentity, 0);
		Matrix.multiplyMM(m_fVPMatrix, 0, m_fProjMatrix, 0, m_fVPMatrix, 0);
		Matrix.translateM(m_fVPMatrix, 0, 0, 0, 1);
		GLES20.glUniformMatrix4fv(iVPMatrix, 1, false, m_fVPMatrix, 0);
		
		GLES20.glDrawElements(GLES20.GL_TRIANGLES, 36, GLES20.GL_UNSIGNED_SHORT, indexBuffer);
		
	}

	@Override
	public void onSurfaceChanged(GL10 arg0, int width, int height) {
		GLES20.glViewport(0, 0, width, height);
		Matrix.frustumM(m_fProjMatrix, 0, -4, 4, -4, 4, 1, 10);
	}

	@Override
	public void onSurfaceCreated(GL10 arg0, EGLConfig arg1) {
		GLES20.glClearColor(0, 0, 0, 1);
		GLES20.glEnable(GLES20.GL_DEPTH_TEST);
		GLES20.glFrontFace(GLES20.GL_CCW);
		
		Matrix.setLookAtM(m_fViewMatrix, 0, 0, 0, 5, 0, 0, 0, 0, 1, 0);
		
		String strVShader = "attribute vec4 a_position;" +
				"attribute vec4 a_color;" +
				"uniform mat4 u_VPMatrix;" +
				"varying vec4 v_color;" +
				"void main()" +
				"{" +
					"v_color = a_color;" +
					"gl_Position = u_VPMatrix * a_position;" +
				"}";
		
		String strFShader = "precision mediump float;" +
				"varying vec4 v_color;" +
				"void main()" +
				"{" +
					"gl_FragColor = v_color;" +
				"}";
		iProgId = Utils.LoadProgram(strVShader, strFShader);
		iColor = GLES20.glGetAttribLocation(iProgId, "a_color");
		iPosition = GLES20.glGetAttribLocation(iProgId, "a_position");
		iVPMatrix = GLES20.glGetUniformLocation(iProgId, "u_VPMatrix");
	}

}
