package es2.learning;

import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import es2.common.Mat3;
import es2.common.Mesh;

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
	
	float[] m_fLightDir = {0, 0, -2};//light direction
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
		sphere.Sphere(4, 4);
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
		// TODO Auto-generated method stub

	}

	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {
		GLES20.glViewport(0, 0, width, height);
//		Matrix.frustumM(m_fProjMatrix, 0, -4, 4, -4, 4, 1, 20);
		Matrix.orthoM(m_fProjMatrix, 0, -10, 10, -10, 10, 1, 20);

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
		
		

	}

}
