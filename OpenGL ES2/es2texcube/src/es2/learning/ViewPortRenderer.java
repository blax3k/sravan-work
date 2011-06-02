package es2.learning;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView.Renderer;
import android.opengl.Matrix;

public class ViewPortRenderer implements Renderer {

	ES2SurfaceView curView;
	int iProgId;
	int iPosition;
	int iColor;
	int iVPMatrix;
	int iTexId;
	int iTexLoc;
	int iTexCoords;
	public float xAngle = 0;
	public float yAngle = 0;
	
	float[] m_fProjMatrix = new float[16];
	float[] m_fViewMatrix = new float[16];
	float[] m_fIdentity = new float[16];
	float[] m_fVPMatrix = new float[16];
	
	float[] cube = {
			-2, -2, -2,
            2, -2, -2,
            2,  2, -2,
            -2, 2, -2,
            -2, -2,  2,
            2, -2,  2,
            2,  2,  2,
            -2,  2,  2
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
			0, 4, 5, 
			0, 5, 1,
			
            1, 5, 6,
            1, 6, 2,
            
            2, 6, 7,
            2, 7, 3,
            
            3, 7, 4, 
            3, 4, 0,
            
            4, 7, 6,
            4, 6, 5,
            
            3, 0, 1, 
            3, 1, 2
			};
	
	float[] tex = { 0,0, 0,1, 1,1, 1,0,
			0,0, 0,1, 1,1, 1,0,
			0,0, 0,1, 1,1, 1,0,
			0,0, 0,1, 1,1, 1,0,
			0,0, 0,1, 1,1, 1,0,
			0,0, 0,1, 1,1, 1,0,
			};
			
	
	FloatBuffer cubeBuffer = null;
	FloatBuffer colorBuffer = null;
	ShortBuffer indexBuffer = null;
	FloatBuffer texBuffer = null;
	
	public ViewPortRenderer(ES2SurfaceView view)
	{
		curView = view;
		cubeBuffer = ByteBuffer.allocateDirect(cube.length * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
		cubeBuffer.put(cube).position(0);
		
		colorBuffer = ByteBuffer.allocateDirect(colors.length * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
		colorBuffer.put(colors).position(0);
		
		indexBuffer = ByteBuffer.allocateDirect(indeces.length * 4).order(ByteOrder.nativeOrder()).asShortBuffer();
		indexBuffer.put(indeces).position(0);
		
		texBuffer = ByteBuffer.allocateDirect(tex.length * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
		texBuffer.put(tex).position(0);
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
		
		texBuffer.position(0);
		GLES20.glVertexAttribPointer(iTexCoords, 2, GLES20.GL_FLOAT, false, 0, texBuffer);
		GLES20.glEnableVertexAttribArray(iTexCoords);
		
		GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
		GLES20.glBindTexture(GLES20.GL_TEXTURE_CUBE_MAP, iTexId);
		GLES20.glUniform1i(iTexLoc, 0);
		
		Matrix.setIdentityM(m_fIdentity, 0);
		Matrix.rotateM(m_fIdentity, 0, -xAngle, 0, 1, 0);
		Matrix.rotateM(m_fIdentity, 0, -yAngle, 1, 0, 0);
		Matrix.multiplyMM(m_fVPMatrix, 0, m_fViewMatrix, 0, m_fIdentity, 0);
		Matrix.multiplyMM(m_fVPMatrix, 0, m_fProjMatrix, 0, m_fVPMatrix, 0);
//		Matrix.translateM(m_fVPMatrix, 0, 0, 0, 1);
		GLES20.glUniformMatrix4fv(iVPMatrix, 1, false, m_fVPMatrix, 0);
		
		GLES20.glDrawElements(GLES20.GL_TRIANGLES, 36, GLES20.GL_UNSIGNED_SHORT, indexBuffer);
		
	}

	@Override
	public void onSurfaceChanged(GL10 arg0, int width, int height) {
		GLES20.glViewport(0, 0, width, height);
		Matrix.frustumM(m_fProjMatrix, 0, -2, 2, -2, 2, 1, 10);
	}

	@Override
	public void onSurfaceCreated(GL10 arg0, EGLConfig arg1) {
		GLES20.glClearColor(0, 0, 0, 1);
		GLES20.glEnable(GLES20.GL_DEPTH_TEST);
		GLES20.glDepthFunc(GLES20.GL_LEQUAL);
		GLES20.glFrontFace(GLES20.GL_CW);
		Matrix.setLookAtM(m_fViewMatrix, 0, 0, 0, -5, 0, 0, 0, 0, 1, 0);
		
		String strVShader = "attribute vec4 a_position;" +
				"attribute vec4 a_color;" +
				"uniform mat4 u_VPMatrix;" +
				"varying vec4 v_color;" +
				"attribute vec3 a_texCoords;" +
				"varying vec3 v_texCoords;" +
				"void main()" +
				"{" +
					"v_color = a_color;" +
					"v_texCoords = a_texCoords;" +
					"gl_Position = u_VPMatrix * a_position;" +
				"}";
		
		String strFShader = "precision mediump float;" +
				"varying vec4 v_color;" +
				"uniform samplerCube u_texId;" +
				"varying vec3 v_texCoords;" +
				"void main()" +
				"{" +
					"gl_FragColor = textureCube(u_texId,v_texCoords);" +
				"}";
		iProgId = Utils.LoadProgram(strVShader, strFShader);
		iColor = GLES20.glGetAttribLocation(iProgId, "a_color");
		iPosition = GLES20.glGetAttribLocation(iProgId, "a_position");
		iVPMatrix = GLES20.glGetUniformLocation(iProgId, "u_VPMatrix");
		iTexLoc = GLES20.glGetUniformLocation(iProgId, "u_texId");
		iTexCoords = GLES20.glGetAttribLocation(iProgId, "a_texCoords");
		iTexId = CreateCubeTexture();//Utils.LoadTexture(curView, R.drawable.texture);
	}
	
	public int CreateCubeTexture()
	{
//		float[] faceColors = { 1,0,0, 0,1,0, 0,0,1, 1,0,1, 1,1,0, 0,1,1};
		ByteBuffer fcbuffer = null;//ByteBuffer.allocateDirect(faceColors.length*4).order(ByteOrder.nativeOrder()).asFloatBuffer();
//		fcbuffer.put(faceColors).position(0);
		
		int[] cubeTex = new int[1];
		
		GLES20.glGenTextures(1, cubeTex, 0);
//		int tex = cubeTex[0];
		GLES20.glBindTexture(GLES20.GL_TEXTURE_CUBE_MAP,cubeTex[0]);
		
		Bitmap img = null;
		img = BitmapFactory.decodeResource(curView.getResources(), R.drawable.brick1);
		fcbuffer = ByteBuffer.allocateDirect(img.getHeight() * img.getWidth() * 4);
		
		img.copyPixelsToBuffer(fcbuffer);
		fcbuffer.position(0);
		GLES20.glTexImage2D(GLES20.GL_TEXTURE_CUBE_MAP_POSITIVE_X, 0, GLES20.GL_RGBA, 1, 1, 0,GLES20.GL_RGBA ,GLES20.GL_UNSIGNED_BYTE, fcbuffer);
		fcbuffer = null;
		img.recycle();
		
		img = BitmapFactory.decodeResource(curView.getResources(), R.drawable.brick2);
		fcbuffer = ByteBuffer.allocateDirect(img.getHeight() * img.getWidth() * 4);
		img.copyPixelsToBuffer(fcbuffer);
		fcbuffer.position(0);
		GLES20.glTexImage2D(GLES20.GL_TEXTURE_CUBE_MAP_NEGATIVE_X, 0, GLES20.GL_RGBA, 1, 1, 0,GLES20.GL_RGBA ,GLES20.GL_UNSIGNED_BYTE, fcbuffer);
		fcbuffer = null;
		img.recycle();
		
		img = BitmapFactory.decodeResource(curView.getResources(), R.drawable.brick3);
		fcbuffer = ByteBuffer.allocateDirect(img.getHeight() * img.getWidth() * 4);
		img.copyPixelsToBuffer(fcbuffer);
		fcbuffer.position(0);
		GLES20.glTexImage2D(GLES20.GL_TEXTURE_CUBE_MAP_POSITIVE_Y, 0, GLES20.GL_RGBA, 1, 1, 0,GLES20.GL_RGBA ,GLES20.GL_UNSIGNED_BYTE, fcbuffer);
		fcbuffer = null;
		img.recycle();
		
		
		img = BitmapFactory.decodeResource(curView.getResources(), R.drawable.brick4);
		fcbuffer = ByteBuffer.allocateDirect(img.getHeight() * img.getWidth() * 4);
		img.copyPixelsToBuffer(fcbuffer);
		fcbuffer.position(0);
		GLES20.glTexImage2D(GLES20.GL_TEXTURE_CUBE_MAP_NEGATIVE_Y, 0, GLES20.GL_RGBA, 1, 1, 0,GLES20.GL_RGBA ,GLES20.GL_UNSIGNED_BYTE, fcbuffer);
		fcbuffer = null;
		img.recycle();
		
		img = BitmapFactory.decodeResource(curView.getResources(), R.drawable.brick5);
		fcbuffer = ByteBuffer.allocateDirect(img.getHeight() * img.getWidth() * 4);
		img.copyPixelsToBuffer(fcbuffer);
		fcbuffer.position(0);
		GLES20.glTexImage2D(GLES20.GL_TEXTURE_CUBE_MAP_POSITIVE_Z, 0, GLES20.GL_RGBA, 1, 1, 0,GLES20.GL_RGBA ,GLES20.GL_UNSIGNED_BYTE, fcbuffer);
		fcbuffer = null;
		img.recycle();
		
		img = BitmapFactory.decodeResource(curView.getResources(), R.drawable.brick6);
		fcbuffer = ByteBuffer.allocateDirect(img.getHeight() * img.getWidth() * 4);
		img.copyPixelsToBuffer(fcbuffer);
		fcbuffer.position(0);
		GLES20.glTexImage2D(GLES20.GL_TEXTURE_CUBE_MAP_NEGATIVE_Z, 0, GLES20.GL_RGBA, 1, 1, 0,GLES20.GL_RGBA ,GLES20.GL_UNSIGNED_BYTE, fcbuffer);
		fcbuffer = null;
		img.recycle();
		
		GLES20.glGenerateMipmap(GLES20.GL_TEXTURE_CUBE_MAP);
		GLES20.glTexParameteri(GLES20.GL_TEXTURE_CUBE_MAP, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST);
		GLES20.glTexParameteri(GLES20.GL_TEXTURE_CUBE_MAP, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_NEAREST);
		GLES20.glTexParameteri(GLES20.GL_TEXTURE_CUBE_MAP, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE);
		GLES20.glTexParameteri(GLES20.GL_TEXTURE_CUBE_MAP, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE);
		return cubeTex[0];
	}
}
