package es2.learning;

import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import es2.common.Mat3;
import es2.common.Mesh;
import es2.common.Utils;

import android.app.Application;
import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView.Renderer;
import android.opengl.Matrix;

public class PerVertexRenderer implements Renderer {

	ES2SurfaceView curView;
	public float dx=0f;
	public float dy=0f;
	int iProgId;
	int iPosition;
	int iNormals;
	int iMVMatrix;
	int iNormMatrix;
	int iLightPos;
	int iLightColor;
	int iTexLoc;
	int iTexCoords;
	int iTexId;
	
	float[] m_fViewMatrix = new float[16];
	float[] m_fProjMatric = new float[16];
	float[] m_fIdentity = new float[16];
	float[] m_fMVMatrix = new float[16];
	
	Mat3 m_NormalsMat;
	
	FloatBuffer vertexBuffer;
	FloatBuffer normalBuffer;
	ShortBuffer indexBuffer;
	FloatBuffer textureBuffer;
	
	Mesh mesh;
	
	float[] fLightPos = {0f,0f,-2.1f};
	float[] fLightColor = {0.8f,0.2f,0.8f};
	
	public PerVertexRenderer(ES2SurfaceView es2SurfaceView) {
		curView = es2SurfaceView;
		mesh = new Mesh();
		mesh.Sphere(4, 10);
		m_NormalsMat = new Mat3();
		vertexBuffer = mesh.getVertexBuffer();
		normalBuffer = mesh.getNormalsBuffer();
		indexBuffer = mesh.getIndecesBuffer();
		textureBuffer = mesh.getTextureBuffer();
	}

	@Override
	public void onDrawFrame(GL10 arg0) {
		GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT|GLES20.GL_DEPTH_BUFFER_BIT);
		
		GLES20.glUseProgram(iProgId);
		
		vertexBuffer.position(0);
		GLES20.glVertexAttribPointer(iPosition, 3, GLES20.GL_FLOAT, false, 0, vertexBuffer);
		GLES20.glEnableVertexAttribArray(iPosition);
		
		normalBuffer.position(0);
		GLES20.glVertexAttribPointer(iNormals, 3, GLES20.GL_FLOAT, false, 0, normalBuffer);
		GLES20.glEnableVertexAttribArray(iNormals);
		
		textureBuffer.position(0);
		GLES20.glVertexAttribPointer(iTexCoords, 2, GLES20.GL_FLOAT, false, 0, textureBuffer);
		GLES20.glEnableVertexAttribArray(iTexCoords);
		
		GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
		GLES20.glBindTexture(GLES20.GL_TEXTURE0, iTexId);
		GLES20.glUniform1i(iTexLoc, 0);
		
		GLES20.glUniform3fv(iLightPos, 1, fLightPos, 0);
		GLES20.glUniform3fv(iLightColor, 1, fLightColor, 0);
		
		Matrix.setIdentityM(m_fIdentity, 0);
		Matrix.translateM(m_fIdentity, 0, dx, dy, 0);
		Matrix.multiplyMM(m_fMVMatrix, 0, m_fViewMatrix, 0, m_fIdentity, 0);
		Matrix.multiplyMM(m_fMVMatrix, 0, m_fProjMatric, 0, m_fMVMatrix, 0);
		
		m_NormalsMat.SetFrom4X4(m_fMVMatrix);
		m_NormalsMat.invert();
		m_NormalsMat.transpose();
		
		GLES20.glUniformMatrix3fv(iNormMatrix, 1, false, m_NormalsMat.values, 0);
		
		GLES20.glUniformMatrix4fv(iMVMatrix, 1, false, m_fMVMatrix, 0);
		
		GLES20.glDrawElements(GLES20.GL_TRIANGLES, mesh.m_nIndeces, GLES20.GL_UNSIGNED_SHORT, indexBuffer);

	}

	@Override
	public void onSurfaceChanged(GL10 arg0, int width, int height) {
		GLES20.glViewport(0, 0, width, height);
		float ratio = (float)width/(float)height;
		Matrix.orthoM(m_fProjMatric, 0, -10f*ratio, 10f*ratio, -10f, 10f, 1, 10);
	}

	@Override
	public void onSurfaceCreated(GL10 arg0, EGLConfig arg1) {
		GLES20.glClearColor(0, 0, 0, 1);
		GLES20.glEnable(GLES20.GL_DEPTH_TEST);
		GLES20.glDepthFunc(GLES20.GL_LEQUAL);
		GLES20.glFrontFace(GLES20.GL_CCW);
		GLES20.glEnable(GLES20.GL_CULL_FACE);
		GLES20.glCullFace(GLES20.GL_BACK);
		
		Matrix.setLookAtM(m_fViewMatrix, 0, 0, 0, 5, 0, 0, 0, 0, 1, 0);
		
		iProgId = Utils.LoadProgram(curView.getContext(), R.raw.vert_perpixel, R.raw.frag_perpixel);
		iTexId = Utils.LoadTexture(curView, R.raw.wlt01);
		
		iPosition = GLES20.glGetAttribLocation(iProgId, "a_position");
		iNormals = GLES20.glGetAttribLocation(iProgId, "a_normals");
		iTexCoords = GLES20.glGetAttribLocation(iProgId, "a_texCoords");
		
		iTexLoc = GLES20.glGetUniformLocation(iProgId, "u_texId");
		iMVMatrix = GLES20.glGetUniformLocation(iProgId, "u_ModelViewMatrix");
		iNormMatrix = GLES20.glGetUniformLocation(iProgId, "u_NormalsMatrix");
		iLightPos = GLES20.glGetUniformLocation(iProgId, "u_LightPosition");
		iLightColor = GLES20.glGetUniformLocation(iProgId, "u_LightColor");
		

	}

}
