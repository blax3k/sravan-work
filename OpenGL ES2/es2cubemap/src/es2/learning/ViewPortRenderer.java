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
import android.util.Log;

public class ViewPortRenderer implements Renderer {

	ES2SurfaceView curView;
	int iProgId;
	int iPosition;
	int iColor;
	int iVPMatrix;
	int iTexId;
	int iTexLoc;
	int iTexCoords;
	int iNormal;
	public float xAngle = 0;
	public float yAngle = 0;
	
	float[] m_fProjMatrix = new float[16];
	float[] m_fViewMatrix = new float[16];
	float[] m_fIdentity = new float[16];
	float[] m_fVPMatrix = new float[16];
	
	float[] cube = {
		2,2,2, -2,2,2, -2,-2,2, 2,-2,2, //0-1-2-3 front
		2,2,2, 2,-2,2,  2,-2,-2, 2,2,-2,//0-3-4-5 right
		2,-2,-2, -2,-2,-2, -2,2,-2, 2,2,-2,//4-7-6-5 back
		-2,2,2, -2,2,-2, -2,-2,-2, -2,-2,2,//1-6-7-2 left
		2,2,2, 2,2,-2, -2,2,-2, -2,2,2, //top
		2,-2,2, -2,-2,2, -2,-2,-2, 2,-2,-2,//bottom
	};
	
	float[] colors = {
		1,0,0,
		1,0,0,
		1,0,0,
		1,0,0,
		1,0,0,
		1,0,0,
		1,0,0,
		1,0,0
	};
	
	short[] indeces = {
			0,1,2, 0,2,3,
			4,5,6, 4,6,7,
			8,9,10, 8,10,11,
			12,13,14, 12,14,15,
			16,17,18, 16,18,19,
			20,21,22, 20,22,23,
			
			};
	
	float[] tex = {
			1,0, 0,0, 0,1, 1,1, 
			0,0, 0,1, 1,1, 1,0,
			1,1, 0,1, 0,0, 1,0,
			0,0, 1,0, 1,1, 0,1,
			0,1, 0,0, 1,0, 1,1,
			0,0, 1,0, 1,1, 0,1,
			
			};
	 float[] normals = {
		          0, 0, 1,   0, 0, 1,   0, 0, 1,   0, 0, 1,     //front
		           1, 0, 0,   1, 0, 0,   1, 0, 0,   1, 0, 0,     // right
		           0, 0,-1,   0, 0,-1,   0, 0,-1,   0, 0,-1,     //back
		           -1, 0, 0,  -1, 0, 0,  -1, 0, 0,  -1, 0, 0,     // left
		           0, 1, 0,   0, 1, 0,   0, 1, 0,   0, 1, 0,     //  top		          
		           0,-1, 0,   0,-1, 0,   0,-1, 0,   0,-1, 0,     // bottom
		           
	 };	
	
	FloatBuffer cubeBuffer = null;
	FloatBuffer colorBuffer = null;
	ShortBuffer indexBuffer = null;
	FloatBuffer texBuffer = null;
	FloatBuffer normBuffer = null;
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
		
		normBuffer= ByteBuffer.allocateDirect(normals.length * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
		normBuffer.put(normals).position(0);
	}
	
	@Override
	public void onDrawFrame(GL10 arg0) {
		GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
		GLES20.glUseProgram(iProgId);
		cubeBuffer.position(0);
		GLES20.glVertexAttribPointer(iPosition, 3, GLES20.GL_FLOAT, false, 0, cubeBuffer);
		GLES20.glEnableVertexAttribArray(iPosition);
		
		/*colorBuffer.position(0);
		GLES20.glVertexAttribPointer(iColor, 3, GLES20.GL_FLOAT, false, 0, colorBuffer);
		GLES20.glEnableVertexAttribArray(iColor);
		
		texBuffer.position(0);
		GLES20.glVertexAttribPointer(iTexCoords, 2, GLES20.GL_FLOAT, false, 0, texBuffer);
		GLES20.glEnableVertexAttribArray(iTexCoords);*/
		
		GLES20.glVertexAttribPointer(iNormal, 3, GLES20.GL_FLOAT, false, 0, normBuffer);
		GLES20.glEnableVertexAttribArray(iNormal);
		
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
		Matrix.frustumM(m_fProjMatrix, 0, -4, 4, -4, 4, 1, 20);
	}

	@Override
	public void onSurfaceCreated(GL10 arg0, EGLConfig arg1) {
		GLES20.glClearColor(0, 0, 0, 1);
//		GLES20.glEnable(GLES20.GL_DEPTH_TEST);
//		GLES20.glDepthFunc(GLES20.GL_LEQUAL);
		GLES20.glFrontFace(GLES20.GL_CCW);
		GLES20.glEnable(GLES20.GL_CULL_FACE);
		GLES20.glCullFace(GLES20.GL_BACK);
		
		Matrix.setLookAtM(m_fViewMatrix, 0, 0, 0, 5, 0, 0, 0, 0, 1, 0);
		
		String strVShader = "attribute vec4 a_position;" +
				//"attribute vec4 a_color;" +
				"uniform mat4 u_VPMatrix;" +
				//"varying vec4 v_color;" +
				//"attribute vec3 a_texCoords;" +
				//"varying mediump vec3 v_texCoords;" +
				"attribute vec3 a_normal;" +
				"varying vec3 v_normal;" +
				"void main()" +
				"{" +
				//	"v_color = a_color;" +
					//"v_texCoords = a_texCoords;" +
					"gl_Position = u_VPMatrix * a_position;" +
					"v_normal = a_normal;" +
				//	"v_texCoords = normalize(a_position.xyz);" +
//					"gl_TexCoord[0].xyz = gl_Vertex.xyz;" +
//					"gl_TexCoord[0].xyz = gl_Position;" +
				"}";
		
		String strFShader = "precision mediump float;" +
			//	"varying vec4 v_color;" +
				"uniform samplerCube u_texId;" +
			//	"varying vec3 v_texCoords;" +
				"varying vec3 v_normal;" +
				"void main()" +
				"{" +
//					"vec3 cube = vec3(textureCube(u_texId, gl_TexCoord[0].xyz));" +
//					"gl_FragColor = vec4(cube, 1.0);" +
					"gl_FragColor = textureCube(u_texId, v_normal);" +
				"}";
		iProgId = Utils.LoadProgram(strVShader, strFShader);
//		iColor = GLES20.glGetAttribLocation(iProgId, "a_color");
		iPosition = GLES20.glGetAttribLocation(iProgId, "a_position");
		iVPMatrix = GLES20.glGetUniformLocation(iProgId, "u_VPMatrix");
		iTexLoc = GLES20.glGetUniformLocation(iProgId, "u_texId");
//		iTexCoords = GLES20.glGetAttribLocation(iProgId, "a_texCoords");
		iNormal = GLES20.glGetAttribLocation(iProgId, "a_normal");
		iTexId = CreateCubeTexture();//Utils.LoadTexture(curView, R.raw.brick5);
	}
	 private int createSimpleTextureCubemap( )
	    {
	        int[] textureId = new int[1];

	        // Face 0 - Red        
	        byte[] cubePixels0 = { 127, 0, 0 };
	        // Face 1 - Green
	        byte[] cubePixels1 = { 0, 127, 0 }; 
	        // Face 2 - Blue
	        byte[] cubePixels2 = { 0, 0, 127 };
	        // Face 3 - Yellow
	        byte[] cubePixels3 = { 127, 0, 0 };
	        // Face 4 - Purple
	        byte[] cubePixels4 = { 0, 0, 127 };
	        // Face 5 - White
	        byte[] cubePixels5 = { 127, 0, 127 };
	                
	        ByteBuffer cubePixels = ByteBuffer.allocateDirect(3);
	    
	        // Generate a texture object
	        GLES20.glGenTextures ( 1, textureId, 0 );

	        // Bind the texture object
	        GLES20.glBindTexture ( GLES20.GL_TEXTURE_CUBE_MAP, textureId[0] );
	    
	        // Load the cube face - Positive X
	        cubePixels.put(cubePixels0).position(0);
	        GLES20.glTexImage2D ( GLES20.GL_TEXTURE_CUBE_MAP_POSITIVE_X, 0, GLES20.GL_RGB, 1, 1, 0, 
	                              GLES20.GL_RGB, GLES20.GL_UNSIGNED_BYTE, cubePixels );

	        // Load the cube face - Negative X
	        cubePixels.put(cubePixels1).position(0);
	        GLES20.glTexImage2D ( GLES20.GL_TEXTURE_CUBE_MAP_NEGATIVE_X, 0, GLES20.GL_RGB, 1, 1, 0, 
	                              GLES20.GL_RGB, GLES20.GL_UNSIGNED_BYTE, cubePixels );

	        // Load the cube face - Positive Y
	        cubePixels.put(cubePixels2).position(0);        
	        GLES20.glTexImage2D ( GLES20.GL_TEXTURE_CUBE_MAP_POSITIVE_Y, 0, GLES20.GL_RGB, 1, 1, 0, 
	                              GLES20.GL_RGB, GLES20.GL_UNSIGNED_BYTE, cubePixels );

	        // Load the cube face - Negative Y
	        cubePixels.put(cubePixels3).position(0);
	        GLES20.glTexImage2D ( GLES20.GL_TEXTURE_CUBE_MAP_NEGATIVE_Y, 0, GLES20.GL_RGB, 1, 1, 0, 
	                              GLES20.GL_RGB, GLES20.GL_UNSIGNED_BYTE, cubePixels );

	        // Load the cube face - Positive Z
	        cubePixels.put(cubePixels4).position(0);        
	        GLES20.glTexImage2D ( GLES20.GL_TEXTURE_CUBE_MAP_POSITIVE_Z, 0, GLES20.GL_RGB, 1, 1, 0, 
	                              GLES20.GL_RGB, GLES20.GL_UNSIGNED_BYTE, cubePixels );

	        // Load the cube face - Negative Z
	        cubePixels.put(cubePixels5).position(0);
	        GLES20.glTexImage2D ( GLES20.GL_TEXTURE_CUBE_MAP_NEGATIVE_Z, 0, GLES20.GL_RGB, 1, 1, 0, 
	                              GLES20.GL_RGB, GLES20.GL_UNSIGNED_BYTE, cubePixels );

	        // Set the filtering mode
	        GLES20.glTexParameteri ( GLES20.GL_TEXTURE_CUBE_MAP, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST );
	        GLES20.glTexParameteri ( GLES20.GL_TEXTURE_CUBE_MAP, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_NEAREST );

	        return textureId[0];
	    }
	public int CreateCubeTexture()
    {
//          float[] faceColors = { 1,0,0, 0,1,0, 0,0,1, 1,0,1, 1,1,0, 0,1,1};
            ByteBuffer fcbuffer = null;//ByteBuffer.allocateDirect(faceColors.length*4).order(ByteOrder.nativeOrder()).asFloatBuffer();
//          fcbuffer.put(faceColors).position(0);
            
            int[] cubeTex = new int[1];
            
            GLES20.glGenTextures(1, cubeTex, 0);
//          int tex = cubeTex[0];
            GLES20.glBindTexture(GLES20.GL_TEXTURE_CUBE_MAP,cubeTex[0]);
            
            Bitmap img = null;
            img = BitmapFactory.decodeResource(curView.getResources(), R.raw.brick1);
            fcbuffer = ByteBuffer.allocateDirect(img.getHeight() * img.getWidth() * 4);
            
            img.copyPixelsToBuffer(fcbuffer);
            fcbuffer.position(0);
            Log.d("alpha",""+img.hasAlpha());
            GLES20.glTexImage2D(GLES20.GL_TEXTURE_CUBE_MAP_POSITIVE_X, 0, GLES20.GL_RGBA, img.getWidth(),img.getHeight() , 0,GLES20.GL_RGBA ,GLES20.GL_UNSIGNED_BYTE, fcbuffer);
            fcbuffer = null;
            img.recycle();
            
            img = BitmapFactory.decodeResource(curView.getResources(), R.raw.brick2);
            fcbuffer = ByteBuffer.allocateDirect(img.getHeight() * img.getWidth() * 4);
            img.copyPixelsToBuffer(fcbuffer);
            fcbuffer.position(0);
            GLES20.glTexImage2D(GLES20.GL_TEXTURE_CUBE_MAP_NEGATIVE_X, 0, GLES20.GL_RGBA, img.getWidth(),img.getHeight(), 0,GLES20.GL_RGBA ,GLES20.GL_UNSIGNED_BYTE, fcbuffer);
            fcbuffer = null;
            img.recycle();
            
            img = BitmapFactory.decodeResource(curView.getResources(), R.raw.brick3);
            fcbuffer = ByteBuffer.allocateDirect(img.getHeight() * img.getWidth() * 4);
            img.copyPixelsToBuffer(fcbuffer);
            fcbuffer.position(0);
            GLES20.glTexImage2D(GLES20.GL_TEXTURE_CUBE_MAP_POSITIVE_Y, 0, GLES20.GL_RGBA, img.getWidth(),img.getHeight(), 0,GLES20.GL_RGBA ,GLES20.GL_UNSIGNED_BYTE, fcbuffer);
            fcbuffer = null;
            img.recycle();
            
            
            img = BitmapFactory.decodeResource(curView.getResources(), R.raw.brick4);
            fcbuffer = ByteBuffer.allocateDirect(img.getHeight() * img.getWidth() * 4);
            img.copyPixelsToBuffer(fcbuffer);
            fcbuffer.position(0);
            GLES20.glTexImage2D(GLES20.GL_TEXTURE_CUBE_MAP_NEGATIVE_Y, 0, GLES20.GL_RGBA, img.getWidth(),img.getHeight(), 0,GLES20.GL_RGBA ,GLES20.GL_UNSIGNED_BYTE, fcbuffer);
            fcbuffer = null;
            img.recycle();
            
            img = BitmapFactory.decodeResource(curView.getResources(), R.raw.brick5);
            fcbuffer = ByteBuffer.allocateDirect(img.getHeight() * img.getWidth() * 4);
            img.copyPixelsToBuffer(fcbuffer);
            fcbuffer.position(0);
            GLES20.glTexImage2D(GLES20.GL_TEXTURE_CUBE_MAP_POSITIVE_Z, 0, GLES20.GL_RGBA,img.getWidth(),img.getHeight(), 0,GLES20.GL_RGBA ,GLES20.GL_UNSIGNED_BYTE, fcbuffer);
            fcbuffer = null;
            img.recycle();
            
            img = BitmapFactory.decodeResource(curView.getResources(), R.raw.brick6);
            fcbuffer = ByteBuffer.allocateDirect(img.getHeight() * img.getWidth() * 4);
            img.copyPixelsToBuffer(fcbuffer);
            fcbuffer.position(0);
            GLES20.glTexImage2D(GLES20.GL_TEXTURE_CUBE_MAP_NEGATIVE_Z, 0, GLES20.GL_RGBA, img.getWidth(),img.getHeight(), 0,GLES20.GL_RGBA ,GLES20.GL_UNSIGNED_BYTE, fcbuffer);
            fcbuffer = null;
            img.recycle();
            
//            GLES20.glGenerateMipmap(GLES20.GL_TEXTURE_CUBE_MAP);
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_CUBE_MAP, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST);
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_CUBE_MAP, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_NEAREST);
//            GLES20.glTexParameteri(GLES20.GL_TEXTURE_CUBE_MAP, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE);
//            GLES20.glTexParameteri(GLES20.GL_TEXTURE_CUBE_MAP, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE);
            return cubeTex[0];
    }
	
}
