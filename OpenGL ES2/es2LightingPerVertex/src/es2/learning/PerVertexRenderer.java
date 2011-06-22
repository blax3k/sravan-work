package es2.learning;

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

public class PerVertexRenderer implements Renderer {
	public float xAngle = 0;
	public float yAngle = 0;
	
	int iProgId;
	int iPosition;
	int iLightColor;
	int iLightDirection;
	int iVPMatrix;
	int iTexId;
	int iTexLoc;
	int iNormals;	
	int iVNormMat;
	int iTexCoords;
	
	float[] m_fProjMatrix = new float[16];
	float[] m_fViewMatrix = new float[16];
	float[] m_fIdentity = new float[16];
	float[] m_fVPMatrix = new float[16];
	
	Mat3 normalMat;
	
	float[] m_fLightDir = {-1, 1, 0};//light direction
	float[] m_fNormalMat = new float[16];//transposed projection matrix
	float[] m_fLightColor = {0.8f,0.6f,0.4f};//light color
	
	ES2SurfaceView curView;
	
	FloatBuffer vertexBuffer = null;
	FloatBuffer normalsBuffer = null;
	ShortBuffer indexBuffer = null;
	FloatBuffer textureBuffer = null;

	Mesh sphere;
	
	public PerVertexRenderer(ES2SurfaceView es2SurfaceView) {
		sphere = new Mesh();
		sphere.Sphere(4, 20);
//		sphere.Cube(4);
		curView = es2SurfaceView;
		normalMat = new Mat3();
		vertexBuffer = sphere.getVertexBuffer();
		normalsBuffer = sphere.getNormalsBuffer();
		indexBuffer = sphere.getIndecesBuffer();
		textureBuffer = sphere.getTextureBuffer();
	}

	@Override
	public void onDrawFrame(GL10 arg0) {
		GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
		GLES20.glUseProgram(iProgId);
		
		vertexBuffer.position(0);
		GLES20.glVertexAttribPointer(iPosition, 3, GLES20.GL_FLOAT, false, 0, vertexBuffer);
		GLES20.glEnableVertexAttribArray(iPosition);
		
		textureBuffer.position(0);
		GLES20.glVertexAttribPointer(iTexCoords, 2, GLES20.GL_FLOAT, false, 0,textureBuffer);
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
		
		/*String strLight = 
				"struct LightParameters" +
				"{" +
					"vec4 ambience;" +
					"vec4 specular;" +
					"vec4 diffuse;" +
					"vec3 position;" +
					"vec3 halfVector;" +
				"};";
		String strMaterial = 
				"struct MaterialParameters" +
				"{" +
					"vec4 emission;" +
					"vec4 ambience;" +
					"vec4 specular;" +
					"vec4 diffuse;" +
					"float shininess;" +
				"};";*/
		String strVShader = 
				"attribute vec4 a_position;" +
				"attribute vec3 a_normals;" +
				"attribute vec2 a_texCoords;" +
				"uniform mat4 u_ModelViewMatrix;" +
				"uniform mat3 u_NormalsMatrix;" +
				"uniform vec3 u_LightPosition;" +
				"uniform vec3 u_LightColor;" +
				"varying vec3 v_colorWeight;" +
				"varying vec2 v_texCoords;" +
				"void main()" +
				"{" +
					"gl_Position = u_ModelViewMatrix * a_position;" +
					"v_texCoords = a_texCoords;" +
					"vec3 normal = u_NormalsMatrix * a_normals;" +
					"vec3 halfVector = normalize(gl_Position.xyz + u_LightPosition);" +
					"float lightWeight = max(dot(halfVector,normal),0.0);" +
					"v_colorWeight = vec3(0.2,0.2,0.2) + (lightWeight * u_LightColor);" +
				"}";
		String strFShader = 
				"precision mediump float;" +
				"varying vec3 v_colorWeight;" +
				"varying vec2 v_texCoords;" +
				"uniform sampler2D u_texId;" +
				"void main()" +
				"{" +
//					"vec4 texColor = vec4(0.8,0.2,0.2,1.0);" +
					"vec4 texColor = texture2D(u_texId, v_texCoords);" +
					"gl_FragColor = vec4(texColor.rgb * v_colorWeight, texColor.a);" +
				"}";
		iProgId = Utils.LoadProgram(strVShader, strFShader);
		
		iPosition = GLES20.glGetAttribLocation(iProgId, "a_position");
		iNormals = GLES20.glGetAttribLocation(iProgId, "a_normals");
		
		iVPMatrix = GLES20.glGetUniformLocation(iProgId, "u_ModelViewMatrix");
		iVNormMat = GLES20.glGetUniformLocation(iProgId, "u_NormalsMatrix");
		iLightColor = GLES20.glGetUniformLocation(iProgId, "u_LightColor");
		iLightDirection = GLES20.glGetUniformLocation(iProgId, "u_LightPosition");
		iTexLoc = GLES20.glGetUniformLocation(iProgId, "u_texId");
		iTexCoords = GLES20.glGetAttribLocation(iProgId, "a_texCoords");
		iTexId = Utils.LoadTexture(curView, R.raw.wlt01);
	}

}
