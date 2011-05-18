package es2.learning;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.GLSurfaceView.Renderer;

public class TriangleRenderer implements Renderer {

	GLSurfaceView curView;
	int iProgId;
	int iPosition;
	float[] fVertices = {0,0.5f,0,
			-0.5f,-0.5f,0,
			0.5f,-0.5f,0};
	FloatBuffer vertexBuffer;
	public TriangleRenderer(GLSurfaceView view) {
		curView = view;
		
		vertexBuffer = ByteBuffer.allocateDirect(fVertices.length *4).order(ByteOrder.nativeOrder()).asFloatBuffer();
		vertexBuffer.put(fVertices).position(0);
	}

	@Override
	public void onDrawFrame(GL10 gl) {
		GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
		GLES20.glUseProgram(iProgId);
		GLES20.glVertexAttribPointer(iPosition, 3, GLES20.GL_FLOAT, false, 0, vertexBuffer);
		GLES20.glEnableVertexAttribArray(iPosition);
		GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 3);
	}

	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {
		GLES20.glViewport(0, 0, width, height);
	}

	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		GLES20.glClearColor(0, 0, 0, 1);
		
		String strVShader = 
			    "attribute vec4 a_position;" +
				"void main()" +
				"{" +
				    "gl_Position = a_position;" +
				"}";
		String strFShader = 
				"precision mediump float;" +
				"void main()" +
				"{" +
					"gl_FragColor = vec4(1,0,0,1);" +
				"}";

		iProgId = Utils.LoadProgram(strVShader, strFShader);
		iPosition = GLES20.glGetAttribLocation(iProgId, "a_position");
	}

}
