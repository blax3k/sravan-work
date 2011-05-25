package test.particle;

import java.util.Random;

import android.util.Log;

public class CParticle {
	float life;
	float angle;
	public float x;
	public float y;
	public float z;
	public float dx,dy,dz;
	public float r,g,b;
	public int status = 0;
	public float age = 0; 
	public float mx,my,mz;
	public CParticle(Random gen)
	{
		life = 1.0f;
		angle = 0;
		
		x = gen.nextFloat()*20f-10f;
		y = gen.nextFloat()*20f-10f;
		z = gen.nextFloat();
		
		r = gen.nextFloat();
		g = gen.nextFloat();
		b = gen.nextFloat();
		
		Log.d("XYZ", "x:"+x+"\ty:"+y+"\tz:"+z);
	}
	public CParticle(Random gen, float fx, float fy, float fz)
	{
		x = fx;
		y = fy;
		z = fz;
		
		r = gen.nextFloat();
		g = gen.nextFloat();
		b = gen.nextFloat();
		
		dx = gen.nextFloat()*2f-1f;
		dy = gen.nextFloat()*2f-1f;
		dz = gen.nextFloat()*1f-0.5f;
		
		life = gen.nextFloat();
	}
	
}
