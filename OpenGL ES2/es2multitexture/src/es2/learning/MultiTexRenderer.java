package es2.learning;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.GLSurfaceView.Renderer;

public class MultiTexRenderer implements Renderer {
	
	GLSurfaceView curView;
	int iProgId;
	int iPosition;
	int iTex1,iTex2;
	int iTexCoords1;
	int[] iTexIds = new int[2];
	
	
	float[] coords = {1,1,0, -1,1,0, -1,-1,0, 1,-1,0};
	
	short[] index = {0,1,2, 0,2,3};
	float[] tex = {1,1, 1,0, 0,0, 0,1};

	FloatBuffer vertexBuffer = null;
	ShortBuffer indecesBuffer = null;
	FloatBuffer texBuffer = null;

	public MultiTexRenderer(GLSurfaceView view)
	{
		curView = view;
		vertexBuffer = ByteBuffer.allocateDirect(coords.length * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
		vertexBuffer.put(coords).position(0);
		
		indecesBuffer= ByteBuffer.allocateDirect(index.length * 2).order(ByteOrder.nativeOrder()).asShortBuffer();
		indecesBuffer.put(index).position(0);
		
		texBuffer = ByteBuffer.allocateDirect(tex.length * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
		texBuffer.put(tex).position(0);
	}
	@Override
	public void onDrawFrame(GL10 gl) {
		GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
		GLES20.glUseProgram(iProgId);
		
		GLES20.glVertexAttribPointer(iPosition, 3, GLES20.GL_FLOAT, false, 0, vertexBuffer);
		GLES20.glEnableVertexAttribArray(iPosition);
		
		GLES20.glVertexAttribPointer(iTexCoords1, 2, GLES20.GL_FLOAT, false, 0, texBuffer);
		GLES20.glEnableVertexAttribArray(iTexCoords1);
		
		GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, iTexIds[0]);
		GLES20.glUniform1i(iTex1, 0);
		
		GLES20.glActiveTexture(GLES20.GL_TEXTURE1);
		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, iTexIds[1]);
		GLES20.glUniform1i(iTex2, 1);
		
//		GLES20.glDrawArrays(GLES20.GL_TRIANGLE_FAN, 0, 4);
		GLES20.glDrawElements(GLES20.GL_TRIANGLES, 6, GLES20.GL_UNSIGNED_SHORT, indecesBuffer);

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
			"attribute vec2 a_texCoords;" +
			"varying vec2 v_texCoords;" +
			"void main()" +
			"{" +
				"v_texCoords = a_texCoords;" +
				"gl_Position = a_position;" +
			"}";
		String strFShader = 
			"precision mediump float;" +
            "varying vec2 v_texCoords;" +
            "uniform sampler2D u_texId1;" +
            "uniform sampler2D u_texId2;" +
            "void main()" +
            "{" +
            	"vec4 color1 = texture2D(u_texId1, v_texCoords);" +
            	"vec4 color2 = texture2D(u_texId2, v_texCoords);" +
            	"gl_FragColor = color2 * color1;" +
            "}";

		
		iProgId = Utils.LoadProgram(strVShader, strFShader);
		iPosition = GLES20.glGetAttribLocation(iProgId, "a_position");
		iTexIds[0] = Utils.LoadTexture(curView, R.raw.tex1);
		iTexIds[1] = Utils.LoadTexture(curView, R.raw.tex2);
		iTex1 = GLES20.glGetUniformLocation(iProgId, "u_texId1");
		iTex2 = GLES20.glGetUniformLocation(iProgId, "u_texId2");
		iTexCoords1 = GLES20.glGetAttribLocation(iProgId, "a_texCoords");
	}

}
