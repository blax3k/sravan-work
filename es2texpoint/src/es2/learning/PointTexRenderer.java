package es2.learning;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.GLSurfaceView.Renderer;

public class PointTexRenderer implements Renderer {

	int texId = -1;
	int iProgId;
	int iPosition;
	//int iTexCoords;
	int iBaseMap;
	GLSurfaceView curView;
	float[] fVertices = {0f,0f,0f};
	float[] fTex = {0,0};
	FloatBuffer vertBuffer;
	FloatBuffer texBuffer;
	public PointTexRenderer(GLSurfaceView view)
	{
		curView = view;
		
		vertBuffer = ByteBuffer.allocateDirect(fVertices.length * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
		vertBuffer.put(fVertices).position(0);
		
		texBuffer = ByteBuffer.allocateDirect(fTex.length * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
		texBuffer.put(fTex).position(0);
	}
	@Override
	public void onDrawFrame(GL10 gl) {
		GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
		GLES20.glUseProgram(iProgId);
		GLES20.glVertexAttribPointer(iPosition, 3, GLES20.GL_FLOAT, false, 0, vertBuffer);
		//GLES20.glVertexAttribPointer(iTexCoords, 2, GLES20.GL_FLOAT, false, 0, texBuffer);
		
		GLES20.glEnableVertexAttribArray(iPosition);
		//GLES20.glEnableVertexAttribArray(iTexCoords);
		
		GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, texId);
		
		GLES20.glUniform1i(iBaseMap, 0);
		
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
			    "attribute vec4 a_position;" +
			  //  "attribute vec2 a_texCoords;" +
			    //"varying vec2 v_texCoords;" +
				"void main()" +
				"{" +
					"gl_PointSize = 45.0;" +
					"gl_Position = a_position;" +
				//	"v_texCoords = a_texCoords;" +
				"}";
		String strFShader = 
			    "precision mediump float;" +
			  //  "varying vec2 v_texCoords;" +
			    "uniform sampler2D u_baseMap;" +
				"void main()" +
				"{" +
					"vec4 color;" +
					"color = texture2D(u_baseMap, gl_PointCoord);" +
					"gl_FragColor = color;" +
					//"gl_FragColor = vec4(0.5,1,0.5,0.5);" +
				"}";
		iProgId = Utils.LoadProgram(strVShader, strFShader);
		iPosition = GLES20.glGetAttribLocation(iProgId, "a_position");
		//iTexCoords = GLES20.glGetAttribLocation(iProgId, "a_texCoords");
		iBaseMap = GLES20.glGetUniformLocation(iProgId, "u_baseMap");
		texId = Utils.LoadTexture(curView, R.drawable.b6);
	}
	
}
