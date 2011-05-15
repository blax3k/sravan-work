package my.test;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView.Renderer;

public class PointRenderer implements Renderer {

//	pointview curView;
	Context context;
	int iProgId;
	int iPosition;
	float[] vertices = {
		0f,0f,0f	
	};
	/*float[] indeces = {
			0,1,2,0,2,3
	};*/
	
	FloatBuffer vertexBuf;
	FloatBuffer idxBuf;
	
	public PointRenderer(Context pv)
	{
		context = pv;
//		curView = pv;
		
		
		vertexBuf = ByteBuffer.allocateDirect(vertices.length * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
		vertexBuf.put(vertices).position(0);
		
		/*idxBuf = ByteBuffer.allocateDirect(indeces.length * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
		idxBuf.put(indeces).position(0);*/
	}
	@Override
	public void onDrawFrame(GL10 gl) {

		GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
		GLES20.glUseProgram(iProgId);
		GLES20.glVertexAttribPointer(iPosition, 3, GLES20.GL_FLOAT, false, 0, vertexBuf);
		GLES20.glEnableVertexAttribArray(iPosition);
		GLES20.glDrawArrays(GLES20.GL_POINTS, 0, 1);
	}

	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {
		GLES20.glViewport(0, 0, width, height);
	}

	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		GLES20.glClearColor(0, 0, 0, 1);
		String strVShader =		
		                    "attribute vec4 a_position;\n"+
							"void main()\n" +
							"{\n" +
							    "gl_PointSize = 45.0;\n" +
							    "gl_Position = a_position;\n"+							    
							"}";
		String strFShader = "precision mediump float;" +
				"void main() " +
				"{" +
				    "gl_FragColor = vec4(1,0,0,1);" +
				"}";
		iProgId = Utils.LoadProgram(strVShader, strFShader);
		iPosition = GLES20.glGetAttribLocation(iProgId, "a_position");
	}
}
