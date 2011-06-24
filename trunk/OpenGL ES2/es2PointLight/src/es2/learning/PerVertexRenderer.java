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
		// TODO Auto-generated method stub

	}

	@Override
	public void onSurfaceChanged(GL10 arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub

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
		
		iProgId = Utils.LoadProgram(curView.getContext(), R.raw.pointlightv, R.raw.pointlightf);

	}

}
