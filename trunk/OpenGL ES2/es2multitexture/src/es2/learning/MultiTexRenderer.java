package es2.learning;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView.Renderer;

public class MultiTexRenderer implements Renderer {

	int iProgId;
	int iPosition;
	int[] iTexIds = new int[2];
	
	float[] coords = {
		0.3f,0.3f,0f, -0.3f,0.3f,0f, -0.3f,-0.3f,0f, 0.3f,-0.3f,0f	
	};
	short[] index = {
		0,1,2, 0,2,3	
	};
	float[] tex = {
		1,1, 1,0, 0,0, 0,1	
	};
	
	FloatBuffer vertexBuffer = null;
	FloatBuffer texBuffer = null;
	ShortBuffer indexBuffer = null;
	public MultiTexRenderer(){
		vertexBuffer = ByteBuffer.allocateDirect(coords.length * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
		vertexBuffer.put(coords).position(0);
		
		texBuffer = ByteBuffer.allocateDirect(tex.length * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
		texBuffer.put(tex).position(0);
		
		indexBuffer = ByteBuffer.allocateDirect(index.length * 4).order(ByteOrder.nativeOrder()).asShortBuffer();
		indexBuffer.put(index).position(0);
	}
	@Override
	public void onDrawFrame(GL10 gl) {
		GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
		GLES20.glUseProgram(iProgId);
		
		GLES20.glVertexAttribPointer(iPosition, 3, GLES20.GL_FLOAT, false, 0, vertexBuffer);
		GLES20.glEnableVertexAttribArray(iPosition);
		
		GLES20.glDrawArrays(GLES20.GL_TRIANGLE_FAN, 0, 4);
//		GLES20.glDrawElements(GLES20.GL_TRIANGLES, 6, GLES20.GL_FLOAT, indexBuffer);
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
				"gl_FragColor = vec4(1.0,0.0,0.0,1.0);" +
			"}";
		iProgId = Utils.LoadProgram(strVShader, strFShader);
		iPosition = GLES20.glGetAttribLocation(iProgId, "a_position");
		
	}

}
