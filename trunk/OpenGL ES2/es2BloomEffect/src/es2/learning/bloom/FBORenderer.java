package es2.learning.bloom;

import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import es2.common.Mesh;
import es2.common.Utils;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;

public class FBORenderer {

	GLSurfaceView curView;	
	int fboWidth = 512;
	int fboHeight = 512;
	
	int fboId;
	int fboTex; 
	int renderBufferId;
	
	int fboIdStep1;
	int fboTexStep1; 
	int renderBufferIdStep1;
	
	int fboIdStep2;
	int fboTexStep2; 
	int renderBufferIdStep2;
	
	int iProgIdRTT;
	int iPositionRTT;
	int iTexCoordsRTT;
	int iTexLocRTT;
	int iTexIdRTT;
	int iVPMatrix;
	
	int iOffset;
	int iWeight;
	
	int iProgIdBlur, iProgIdStep2; 
	
	public float xAngle = 50;
	public float yAngle = 0;
	
	int iTexId1;
	
	Mesh sphere;
	FloatBuffer vertexBuffer, normalBuffer, texBuffer;
	FloatBuffer texBuffer1, vertexBuffer2;
	ShortBuffer indexBuffer;
	
	//keep dimensions in sync with projection dimensions
	//since rect should of full screen to map fbo
	final float[] COORDS1 = {
			-5f, -5f, //bottom - left
			5f, -5f, //bottom - right
			-5f, 5f, //top - left
			5f, 5f //top - right
		};
	
	final float[] TEX_COORDS = {
		0, 1, //bottom - left
		1, 1, // bottom - right		
		0, 0, // top - left
		1, 0 // top - right
	};
	
	float[] m_fViewMatrix = new float[16];
	float[] m_fProjMatrix = new float[16];
	float[] m_fVPMatrix = new float[16];
	float[] m_fModel = new float[16];
	float[] m_fMVPMatrix = new float[16];
	
	
	int iDirection, iBlurAmount;
	int iBlurScale, iBlurStrength;
	
	public FBORenderer(GLSurfaceView view) {
		curView = view;
		
		sphere = new Mesh();
		sphere.Sphere(4, 10);
		vertexBuffer = sphere.getVertexBuffer();
		indexBuffer = sphere.getIndecesBuffer();
		texBuffer = sphere.getTextureBuffer();
		normalBuffer = sphere.getNormalsBuffer();
		
		vertexBuffer2 = Utils.CreateVertexArray(COORDS1);
		texBuffer1 = Utils.CreateVertexArray(TEX_COORDS);
		
	}
	
	public void LoadShaders()
	{
		String strVShader = 
			    "attribute vec4 a_position;" +
			    "attribute vec2 a_texCoords;" +
			    "uniform mat4 u_ModelViewMatrix;" +
			    "varying vec2 v_texCoords;" +
				"void main()" +
				"{" +
				    "gl_Position = u_ModelViewMatrix * a_position;" +
				    "v_texCoords = a_texCoords;" +
				"}";
		String strFShaderRTT = 
				"precision mediump float;" +
				"varying vec2 v_texCoords;" +
				"uniform sampler2D u_texId;" +				
				"void main()" +
				"{" +
					"gl_FragColor = texture2D(u_texId, v_texCoords);"+
				"}";
		
					
		iProgIdRTT = Utils.LoadProgram(strVShader, strFShaderRTT);
		
		iProgIdBlur = Utils.LoadProgram(curView.getContext(), "vertex.vsh","gaussianblur.fsh");
		
		
		iTexId1 = Utils.LoadTexture(curView, "pattern.png");
	}
	
	public int[] CreateFrameBuffers()
	{
		Matrix.setLookAtM(m_fViewMatrix, 0, 0, 0, 5, 0, 0, 0, 0, 1, 0);
		
		int []ret = new int[2];
		
		int[] temp = new int[1];
		GLES20.glGenFramebuffers(1, temp, 0);
		fboId = temp[0];
		GLES20.glGenTextures(1, temp, 0);
		fboTex = temp[0];
		GLES20.glGenRenderbuffers(1, temp, 0);
		renderBufferId = temp[0];
		
		int rtt = InitiateFrameBuffer(fboId, fboTex, renderBufferId);
		
		GLES20.glGenFramebuffers(1, temp, 0);
		fboIdStep1 = temp[0];
		GLES20.glGenTextures(1, temp, 0);
		fboTexStep1 = temp[0];
		GLES20.glGenRenderbuffers(1, temp, 0);
		renderBufferIdStep1 = temp[0];

		InitiateFrameBuffer(fboIdStep1, fboTexStep1,renderBufferIdStep1);
		
		GLES20.glGenFramebuffers(1, temp, 0);
		fboIdStep2 = temp[0];
		GLES20.glGenTextures(1, temp, 0);
		fboTexStep2 = temp[0];
		GLES20.glGenRenderbuffers(1, temp, 0);
		renderBufferIdStep2 = temp[0];
		
		int finaltex = InitiateFrameBuffer(fboIdStep2, fboTexStep2,renderBufferIdStep2);
		
		ret[0] = rtt;
		ret[1] = finaltex;
		
		return ret;
	}
		
	public int InitiateFrameBuffer(int fbo, int tex, int rid)
	{
		//Bind Frame buffer 
		GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, fbo);
		//Bind texture
		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, tex);
		//Define texture parameters 
		GLES20.glTexImage2D(GLES20.GL_TEXTURE_2D, 0, GLES20.GL_RGBA, fboWidth, fboHeight, 0, GLES20.GL_RGBA, GLES20.GL_UNSIGNED_BYTE, null);
		
		GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE);
		GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE);
		GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
		GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
		//Bind render buffer and define buffer dimension
		GLES20.glBindRenderbuffer(GLES20.GL_RENDERBUFFER, rid);
		GLES20.glRenderbufferStorage(GLES20.GL_RENDERBUFFER, GLES20.GL_DEPTH_COMPONENT16, fboWidth, fboHeight);
		//Attach texture FBO color attachment
		GLES20.glFramebufferTexture2D(GLES20.GL_FRAMEBUFFER, GLES20.GL_COLOR_ATTACHMENT0, GLES20.GL_TEXTURE_2D, tex, 0);
		//Attach render buffer to depth attachment
		GLES20.glFramebufferRenderbuffer(GLES20.GL_FRAMEBUFFER, GLES20.GL_DEPTH_ATTACHMENT, GLES20.GL_RENDERBUFFER, rid);
		//we are done, reset
		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);
		GLES20.glBindRenderbuffer(GLES20.GL_RENDERBUFFER, 0);
		GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, 0);
		return tex;
	}
	
	public void RenderGaussianBlur()
	{
		GLES20.glViewport(0, 0, fboWidth, fboHeight);
		float ratio = (float)fboWidth/(float)fboHeight;
		float a = 5f;
		Matrix.orthoM(m_fProjMatrix, 0, -a*ratio, a*ratio, -a*ratio, a*ratio, 1, 10);
		Matrix.multiplyMM(m_fVPMatrix, 0, m_fProjMatrix, 0, m_fViewMatrix, 0);
		//render scene to texture fboTex
		RenderToTexture();
		
		Blur();
	}
	
	public void RenderToTexture()
	{
		GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, fboId);
		
		iPositionRTT = GLES20.glGetAttribLocation(iProgIdRTT, "a_position");
		iTexCoordsRTT = GLES20.glGetAttribLocation(iProgIdRTT, "a_texCoords");
		iTexLocRTT = GLES20.glGetUniformLocation(iProgIdRTT, "u_texId");
		iVPMatrix = GLES20.glGetUniformLocation(iProgIdRTT, "u_ModelViewMatrix");
		
		GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT|GLES20.GL_DEPTH_BUFFER_BIT);
		
		GLES20.glUseProgram(iProgIdRTT);
		
		vertexBuffer.position(0);
		GLES20.glVertexAttribPointer(iPositionRTT, 3, GLES20.GL_FLOAT, false, 0, vertexBuffer);
		GLES20.glEnableVertexAttribArray(iPositionRTT);
		
		texBuffer.position(0);
		GLES20.glVertexAttribPointer(iTexCoordsRTT, 2, GLES20.GL_FLOAT, false, 0, texBuffer);
		GLES20.glEnableVertexAttribArray(iTexCoordsRTT);
		
		GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, iTexId1);
		GLES20.glUniform1i(iTexLocRTT, 0);
		
		Matrix.setIdentityM(m_fModel, 0);
		xAngle += 1f;
		yAngle += 2f;
		Matrix.rotateM(m_fModel, 0, -xAngle, 0, 1, 0);
		Matrix.rotateM(m_fModel, 0, -yAngle, 1, 0, 0);
		
		Matrix.multiplyMM(m_fMVPMatrix, 0, m_fVPMatrix, 0, m_fModel, 0);
		
		GLES20.glUniformMatrix4fv(iVPMatrix, 1, false, m_fMVPMatrix, 0);
		
		//GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4);
		GLES20.glDrawElements(GLES20.GL_TRIANGLES, sphere.m_nIndeces, GLES20.GL_UNSIGNED_SHORT, indexBuffer);
		
		GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, 0);
	}
	
	/***
	 * using Gaussian blur with linear approximation
	 * Step1: Apply Vertical Blur on image/texture
	 * Step2: apply horizontal blur on Step1
	 */
	
	public void Blur()
	{
		iPositionRTT = GLES20.glGetAttribLocation(iProgIdBlur, "a_position");
		iTexCoordsRTT = GLES20.glGetAttribLocation(iProgIdBlur, "a_texCoords");		
		iTexLocRTT = GLES20.glGetUniformLocation(iProgIdBlur, "u_texId");
		iVPMatrix = GLES20.glGetUniformLocation(iProgIdBlur, "u_ModelViewMatrix");
		
		iDirection = GLES20.glGetUniformLocation(iProgIdBlur, "direction");
		iBlurScale = GLES20.glGetUniformLocation(iProgIdBlur, "blurScale");
		iBlurAmount = GLES20.glGetUniformLocation(iProgIdBlur, "blurAmount");
		iBlurStrength = GLES20.glGetUniformLocation(iProgIdBlur, "blurStrength");
		
		GLES20.glUseProgram(iProgIdBlur);
		
		//apply horizontal blur on fboTex store result in fboTexStep1
		BlurStep(1);
		//apply horizontal blur on fboTex store result in fboTexStep2
		BlurStep(2);
	}
	
	public void BlurStep(int step)
	{
		if (step == 1)
			GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, fboIdStep1); 
		else if (step == 2)
			GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, fboIdStep2);
		
		GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT|GLES20.GL_DEPTH_BUFFER_BIT);
		
		vertexBuffer.position(0);
		GLES20.glVertexAttribPointer(iPositionRTT, 2, GLES20.GL_FLOAT, false, 0, vertexBuffer2);
		GLES20.glEnableVertexAttribArray(iPositionRTT);
		
		texBuffer.position(0);
		GLES20.glVertexAttribPointer(iTexCoordsRTT, 2, GLES20.GL_FLOAT, false, 0, texBuffer1);
		GLES20.glEnableVertexAttribArray(iTexCoordsRTT);
		
		GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
		if (step == 1)
			GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, fboTex);
		else if (step == 2)
			GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, fboTexStep1);
		
		GLES20.glUniform1i(iTexLocRTT, 0);
		
		Matrix.setIdentityM(m_fModel, 0);		
		
		Matrix.multiplyMM(m_fMVPMatrix, 0, m_fVPMatrix, 0, m_fModel, 0);
		
		GLES20.glUniformMatrix4fv(iVPMatrix, 1, false, m_fMVPMatrix, 0);
		
		if (step == 1)
			GLES20.glUniform1i(iDirection, 0);
		else if (step == 2)
			GLES20.glUniform1i(iDirection, 1);
		
		GLES20.glUniform1f(iBlurScale,  1.0f);
		
		GLES20.glUniform1f(iBlurAmount, 20f);		
		GLES20.glUniform1f(iBlurStrength, 0.5f);
			
		GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4);
		
		GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, 0);
	}
	
	/*public void BlurStep2()
	{
		GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, fboIdStep2);
		
		iPositionRTT = GLES20.glGetAttribLocation(iProgIdBlur, "a_position");
		iTexCoordsRTT = GLES20.glGetAttribLocation(iProgIdBlur, "a_texCoords");		
		iTexLocRTT = GLES20.glGetUniformLocation(iProgIdBlur, "u_texId");
		iVPMatrix = GLES20.glGetUniformLocation(iProgIdBlur, "u_ModelViewMatrix");
		
		iDirection = GLES20.glGetUniformLocation(iProgIdBlur, "direction");
		iBlurScale = GLES20.glGetUniformLocation(iProgIdBlur, "blurScale");
		iBlurAmount = GLES20.glGetUniformLocation(iProgIdBlur, "blurAmount");
		iBlurStrength = GLES20.glGetUniformLocation(iProgIdBlur, "blurStrength");
		
		GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT|GLES20.GL_DEPTH_BUFFER_BIT);
		
		
		GLES20.glUseProgram(iProgIdBlur);
		
		vertexBuffer.position(0);
		GLES20.glVertexAttribPointer(iPositionRTT, 2, GLES20.GL_FLOAT, false, 0, vertexBuffer2);
		GLES20.glEnableVertexAttribArray(iPositionRTT);
		
		texBuffer.position(0);
		GLES20.glVertexAttribPointer(iTexCoordsRTT, 2, GLES20.GL_FLOAT, false, 0, texBuffer1);
		GLES20.glEnableVertexAttribArray(iTexCoordsRTT);
		
		GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, fboTexStep1);
		GLES20.glUniform1i(iTexLocRTT, 0);
		
		Matrix.setIdentityM(m_fModel, 0);		
		
		Matrix.multiplyMM(m_fMVPMatrix, 0, m_fVPMatrix, 0, m_fModel, 0);
		
		GLES20.glUniformMatrix4fv(iVPMatrix, 1, false, m_fMVPMatrix, 0);
		
		GLES20.glUniform1i(iDirection, 1);
		
		GLES20.glUniform1f(iBlurScale,  1.0f);
		
		GLES20.glUniform1f(iBlurAmount, 20f);		
		GLES20.glUniform1f(iBlurStrength, 0.5f);
		
		GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4);		
		
		GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, 0);
	}*/

}
