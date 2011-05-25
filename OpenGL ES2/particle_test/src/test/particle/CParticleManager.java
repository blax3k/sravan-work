package test.particle;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.Random;

import javax.microedition.khronos.opengles.GL10;

public class CParticleManager {

	final float PARTICLE_SIZE = 2.0f;
	private CParticle[] particleCollection;
	private FloatBuffer coordBuffer;
	private FloatBuffer normTex;
	private float[] coords={
			0.0f, 0.0f, 0.0f,
			0.0f, PARTICLE_SIZE, 0.0f,
			PARTICLE_SIZE, 0.0f, 0.0f,
			PARTICLE_SIZE,PARTICLE_SIZE,0.0f		
	};
	private float[] normalTex={
			0f,0f,
			0f,1f,			
			
			1f,0f,
			1f,1f
			
	};
	public int texId=-1;
	Random gen = new Random(System.currentTimeMillis());
	ParticleView curView;
	ParticleUpdateThread pThread;
	final float PI = 3.141592f;
	
	public CParticleManager(ParticleView view){
		curView = view;
	}
	public void CreateParticles(int size)
	{
		particleCollection = new CParticle[size];
		
		for (int i=0;i<size;i++) {			
			CParticle pa = new CParticle(gen,0f,0f,0f);
			particleCollection[i] = pa;
		}
		
		ByteBuffer vbb = ByteBuffer.allocateDirect(coords.length * 4);
		vbb.order(ByteOrder.nativeOrder());
		coordBuffer = vbb.asFloatBuffer();
		coordBuffer.put(coords);
		coordBuffer.position(0);
		
		vbb = ByteBuffer.allocateDirect(normalTex.length * 4);
		vbb.order(ByteOrder.nativeOrder());
		normTex = vbb.asFloatBuffer();
		normTex.put(normalTex).position(0);
	}
	public void LoadTexture(GL10 gl)
	{
		texId = Utils.LoadTexture(curView, R.drawable.particle);
	}
	
	public void update()
	{
//		Log.d("update", ""+particleCollection.length);
		int i=0;
		while (i<particleCollection.length)
		{
			if (particleCollection[i].status == 1) {
				particleCollection[i].x += particleCollection[i].dx*0.4f;
				particleCollection[i].y += particleCollection[i].dy*0.4f;
				particleCollection[i].z += particleCollection[i].dz*0.1f;
				particleCollection[i].life -= particleCollection[i].age;
				if ((particleCollection[i].life <= 0f)/* || (Math.abs(particleCollection[i].x) >= Math.abs(particleCollection[i].mx))
						||(Math.abs(particleCollection[i].y) >= Math.abs(particleCollection[i].my))*/)
				{					
					particleCollection[i].x = Utils.rnd(0f,0.5f);
					particleCollection[i].y = Utils.rnd(0f,0.5f);
					particleCollection[i].z = 0f;
					particleCollection[i].status = 1;					
					particleCollection[i].life = Utils.rnd(0f, 1.0f);
//					particleCollection[i].age = rnd(0.001f,0.1f);//gen.nextInt(100);
				}
			}
			i++;
//			Log.d("update", "x::"+particleCollection[i].x);
		}
	}
	
	public void setup(float posx, float posy)
	{
		pThread = new ParticleUpdateThread(this);
		int i=0;
		float inc = 1.0f/particleCollection.length;
		float vel = 0;//.25f;
		while (i<particleCollection.length)
		{
			vel += inc;
			int angle = (int) (gen.nextFloat() * 360f);
			particleCollection[i].x = posx;
			particleCollection[i].y = posy;
			particleCollection[i].status = 1;
			particleCollection[i].dx = (float) (Math.cos(Math.toRadians(angle)));
			particleCollection[i].dy = (float) (Math.sin(Math.toRadians(angle)));
			
//			particleCollection[i].mx = particleCollection[i].dx * 5f;
//			particleCollection[i].my = particleCollection[i].dy * 5f;
			//particleCollection[i].mx = (float) (Math.cos(Math.toRadians(angle))*20f);
			//particleCollection[i].my = (float) (Math.sin(Math.toRadians(angle))*20f);
			
			
			particleCollection[i].life = Utils.rnd(0.0f, 1.0f);
			particleCollection[i].age = Utils.rnd(0.001f,0.1f);//gen.nextInt(100);
			i++;
		}
		pThread.SetRunning(true);
		pThread.start();
	}
	
	public void Draw(GL10 gl)
	{
		
//		gl.glRotatef(-curView.diffx, 0,1, 0);
//		gl.glRotatef(-curView.diffy, 1, 0, 0);
//		gl.glLoadIdentity();
		
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, coordBuffer);
//		gl.glPointSize(5.0f);
//		gl.glColor4f(0.5f,1.0f,1f,1f);
		int i=0;
		gl.glBindTexture(GL10.GL_TEXTURE_2D, texId);
		gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, normTex);
		while (i<particleCollection.length) {
			if (particleCollection[i].status == 1) {
				gl.glColor4f(particleCollection[i].r, particleCollection[i].g, particleCollection[i].b, 1f);//particleCollection[i].life);
				gl.glPushMatrix();
				gl.glTranslatef(particleCollection[i].x, particleCollection[i].y, particleCollection[i].z);
//				gl.glDrawArrays(GL10.GL_POINTS, 0, 1);
				gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, coords.length / 3);
				gl.glPopMatrix();
			}
			i++;
		}
		gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
	}
}
