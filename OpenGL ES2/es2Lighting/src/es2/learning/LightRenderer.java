package es2.learning;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import es2.common.Mat3;
import es2.common.Mesh;
import es2.common.Utils;

import android.opengl.GLES20;
import android.opengl.Matrix;
import android.opengl.GLSurfaceView.Renderer;

public class LightRenderer implements Renderer {
	
	int iPogAxisId;
	int iAxisPos;
	int iAxisColor;
	
	int iProgId;
	int iProgIdPP;
	int iProgIdPV;
	int iPosition;
	int iLightColor;
	int iLightDirection;
	int iVPMatrix;
	int iTexId;
	int iTexLoc;
	int iNormals;	
	int iVNormMat;
	int iTexCoords;
	
	public float xAngle = 0;
	public float yAngle = 0;
	
	float[] m_fProjMatrix = new float[16];
	float[] m_fViewMatrix = new float[16];
	float[] m_fIdentity = new float[16];
	float[] m_fVPMatrix = new float[16];
	
	Mat3 normalMat;
	
	float[] m_fLightDir = {0, 0, -5};//light direction
	float[] m_fNormalMat = new float[16];//transposed projection matrix
	float[] m_fLightColor = {0.8f,0.6f,0.4f};//light color
	
	ES2SurfaceView curView;
	
	
	float[] lines = {
			0,0,0, 100,0,0, //+x-axis
			0,0,0, -100,0,0, //-x-axis
			0,0,0, 0,100,0,//+y
			0,0,0, 0,-100,0,//-y
			0,0,0, 0,0,100,
			0,0,0, 0,0,-100
		};
	FloatBuffer fb;
	float[] axiscolors = {
			1,0,0, 1,0,0,
			0,1,0, 0,1,0,
			0,0,1, 0,0,1,
			1,1,0, 1,1,0,
			1,0,1, 1,0,1,
			0,1,1, 0,1,1,			
		};
	FloatBuffer axisColorBuf;
	
	FloatBuffer cubeBuffer = null;
	FloatBuffer normalsBuffer = null;
	ShortBuffer indexBuffer = null;
	FloatBuffer texBuffer = null;

	Mesh sphere;
		
	public LightRenderer(ES2SurfaceView view) {
		sphere = new Mesh();
		sphere.Sphere(4, 10);
//		sphere.Cube(4);
		curView = view;
		normalMat = new Mat3();
		cubeBuffer = sphere.getVertexBuffer();
		normalsBuffer = sphere.getNormalsBuffer();
		indexBuffer = sphere.getIndecesBuffer();
		texBuffer = sphere.getTextureBuffer();
		
		fb = ByteBuffer.allocateDirect(lines.length * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
		fb.put(lines).position(0);
		
		axisColorBuf = ByteBuffer.allocateDirect(axiscolors.length * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
		axisColorBuf.put(axiscolors).position(0);
	}

	@Override
	public void onDrawFrame(GL10 gl) {
		GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
		GLES20.glUseProgram(iProgId);
		
		cubeBuffer.position(0);
		GLES20.glVertexAttribPointer(iPosition, 3, GLES20.GL_FLOAT, false, 0, cubeBuffer);
		GLES20.glEnableVertexAttribArray(iPosition);
		
		texBuffer.position(0);
		GLES20.glVertexAttribPointer(iTexCoords, 2, GLES20.GL_FLOAT, false, 0,texBuffer);
		GLES20.glEnableVertexAttribArray(iTexCoords);
		
		normalsBuffer.position(0);
		GLES20.glVertexAttribPointer(iNormals, 3, GLES20.GL_FLOAT, false, 0, normalsBuffer);
		GLES20.glEnableVertexAttribArray(iNormals);
		
		GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, iTexId);
		GLES20.glUniform1i(iTexLoc, 0);
		
		GLES20.glUniform3fv(iLightColor, 1, m_fLightColor, 0);
		GLES20.glUniform3fv(iLightDirection, 1, m_fLightDir, 0);
		
		
		Matrix.setIdentityM(m_fIdentity, 0);
		Matrix.rotateM(m_fIdentity, 0, -xAngle, 0, 1, 0);
		Matrix.rotateM(m_fIdentity, 0, -yAngle, 1, 0, 0);
		
		Matrix.multiplyMM(m_fVPMatrix, 0, m_fViewMatrix, 0, m_fIdentity, 0);
		Matrix.multiplyMM(m_fVPMatrix, 0, m_fProjMatrix, 0, m_fVPMatrix, 0);
		
		normalMat.SetFrom4X4(m_fVPMatrix);
		normalMat.invert();
		normalMat.transpose();
		GLES20.glUniformMatrix3fv(iVNormMat, 1, false, normalMat.values, 0);
		
		GLES20.glUniformMatrix4fv(iVPMatrix, 1, false, m_fVPMatrix, 0);
		
		GLES20.glDrawElements(GLES20.GL_TRIANGLES, sphere.m_nIndeces, GLES20.GL_UNSIGNED_SHORT, indexBuffer);

	}

	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {
		GLES20.glViewport(0, 0, width, height);
//		Matrix.frustumM(m_fProjMatrix, 0, -4, 4, -4, 4, 1, 20);
		float ratio = (float)width/(float)height;
		Matrix.orthoM(m_fProjMatrix, 0, -10f*ratio, 10f*ratio, -10, 10, 1, 20);
	}

	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		
		GLES20.glClearColor(0, 0, 0, 1);
		GLES20.glEnable(GLES20.GL_DEPTH_TEST);
		GLES20.glDepthFunc(GLES20.GL_LEQUAL);
		GLES20.glFrontFace(GLES20.GL_CCW);
		GLES20.glEnable(GLES20.GL_CULL_FACE);
		GLES20.glCullFace(GLES20.GL_BACK);
		
		Matrix.setLookAtM(m_fViewMatrix, 0, 0, 0, 5, 0, 0, 0, 0, 1, 0);
		
		String strVShaderPP = "attribute vec4 a_position;" +
				"attribute vec3 a_normals;" +
				"attribute vec2 a_texCoords;" +
				"uniform mat4 u_ModelViewMatrix;" +
				"uniform mat3 u_MVNormalsMatrix;" +
				"varying vec3 u_Normals;" +
				"varying vec2 v_texCoords;" +
				"void main()" +
				"{" +
					"v_texCoords = a_texCoords;" +
					"u_Normals = u_MVNormalsMatrix * a_normals;" +
					"gl_Position = u_ModelViewMatrix * a_position;" +
				"}";
		String strFShaderPP = "precision mediump float;" +
				"uniform vec3 u_LightDir;" +
				"uniform vec3 u_LightColor;" +				
				"uniform sampler2D u_texId;" +
				"varying vec2 v_texCoords;" +
				"varying vec3 u_Normals;" +
				"void main()" +
				"{" +
					"vec3 LNorm = normalize(u_LightDir);" +
					"vec3 normal = normalize(u_Normals);" +
					"float intensity = max(dot(LNorm, normal),0.0);" +
					"vec4 texColor = texture2D(u_texId, v_texCoords);" +
					"vec3 calcColor = vec3(0.2,0.2,0.2) + u_LightColor * intensity;" +
					"gl_FragColor = vec4(texColor.rgb * calcColor, texColor.a);" +
				"}";
		
		String strVShaderPV = 
			"attribute vec4 a_position;" +
			"attribute vec3 a_normals;" +
			"attribute vec2 a_texCoords;" +
			"uniform mat4 u_ModelViewMatrix;" +
			"uniform mat3 u_MVNormalsMatrix;" +
			"uniform vec3 u_LightDir;" +
			"uniform vec3 u_LightColor;" +
			"varying vec3 v_colorWeight;" +
			"varying vec2 v_texCoords;" +
			"void main()" +
			"{" +
				"gl_Position = u_ModelViewMatrix * a_position;" +
				"v_texCoords = a_texCoords;" +
				"vec3 normal = normalize(u_MVNormalsMatrix * a_normals);" +
				"vec3 lightNorm = normalize(u_LightDir);" +
				"float lightWeight = max(dot(normal,lightNorm),0.0);" +
				"v_colorWeight = vec3(0.2,0.2,0.2) + (lightWeight * u_LightColor);" +
			"}";
	String strFShaderPV = 
			"precision mediump float;" +
			"varying vec3 v_colorWeight;" +
			"varying vec2 v_texCoords;" +
			"uniform sampler2D u_texId;" +
			"void main()" +
			"{" +
				"vec4 texColor = texture2D(u_texId, v_texCoords);" +
				"gl_FragColor = vec4(texColor.rgb * v_colorWeight, texColor.a);" +
			"}";
		
		iProgIdPP = Utils.LoadProgram(strVShaderPP, strFShaderPP);
		iProgIdPV = Utils.LoadProgram(strVShaderPV, strFShaderPV);
		
		iProgId = iProgIdPV;
		
		iPosition = GLES20.glGetAttribLocation(iProgId, "a_position");
		iNormals = GLES20.glGetAttribLocation(iProgId, "a_normals");
		iVPMatrix = GLES20.glGetUniformLocation(iProgId, "u_ModelViewMatrix");
		iVNormMat = GLES20.glGetUniformLocation(iProgId, "u_MVNormalsMatrix");
		iLightColor = GLES20.glGetUniformLocation(iProgId, "u_LightColor");
		iLightDirection = GLES20.glGetUniformLocation(iProgId, "u_LightDir");
		iTexLoc = GLES20.glGetUniformLocation(iProgId, "u_texId");
		iTexCoords = GLES20.glGetAttribLocation(iProgId, "a_texCoords");
		iTexId = Utils.LoadTexture(curView, R.raw.wlt01);
		
//		LoadAxisShaders();
	}
	
	public void LoadAxisShaders()
	{
		String strVShader =
			"attribute vec4 a_position;" +
			"attribute vec4 a_color;" +
			"varying vec4 v_color;" +
			"void main()" +
			"{" +
				"v_color = a_color;" +
				"gl_PointSize = 5.0;" +
				"gl_Position = a_position;" +
			"}";	
		String strFShader = "precision mediump float;" +
			"varying vec4 v_color;" +
			"void main()" +
			"{" +
				"gl_FragColor = v_color;" +
			"}";
		iPogAxisId = Utils.LoadProgram(strVShader, strFShader);
		iAxisColor= GLES20.glGetAttribLocation(iPogAxisId, "a_color");
		iAxisPos = GLES20.glGetAttribLocation(iPogAxisId, "a_position");
	}
	
	public void DrawAxis()
	{
		GLES20.glUseProgram(iPogAxisId);
		GLES20.glVertexAttribPointer(iAxisPos, 3, GLES20.GL_FLOAT, false, 0, fb);
		GLES20.glEnableVertexAttribArray(iAxisPos);
		
		GLES20.glVertexAttribPointer(iAxisColor, 3, GLES20.GL_FLOAT, false, 0, axisColorBuf);
		GLES20.glEnableVertexAttribArray(iAxisColor);
		
		GLES20.glDrawArrays(GLES20.GL_LINES, 0, 12);
		
		GLES20.glUseProgram(0);
		
	}
	public void LoadProgram(int id)
	{
		if (id == 0)
			iProgId = iProgIdPV;
		else 
			iProgId = iProgIdPP;
	}
}
