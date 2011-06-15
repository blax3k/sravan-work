package es2.common;

/**
 * vector class for various vector operations
 * @author sravan
 *
 */
public class Vec3 {
	float[] xyz;
	/**
	 * default constructor, creates float array of size 3
	 */
	public Vec3()
	{
		xyz = new float[3];		
	}
	/**
	 * copies values from src
	 * @param src, array to copy values from
	 */
	public void set(float[] src)
	{
		xyz = src.clone();
	}
	/**
	 * 
	 * @return float array of values
	 */
	public float[] getAsArray()
	{
		return xyz;
	}
	/**
	 * sets x,y, and z values to vector
	 * @param x
	 * @param y
	 * @param z
	 */
	public void set(float x, float y, float z)
	{
		xyz[0] = x;
		xyz[1] = y;
		xyz[2] = z;
	}
	
	/**
	 * normalizes this vector
	 * @return, normalized vector
	 */
	public Vec3 normalize()
	{
		Vec3 normal = new Vec3();;
		float x,y,z;
		x = xyz[0];
		y = xyz[1];
		z = xyz[2];
		
		float len = (float) Math.sqrt(x*x + y*y + z*z);
		if (len == 0.0f)
		{
			normal.set(0f, 0f, 0f);
		} else if (len == 1.0f){
			normal.set(x, y, z);
		} else {
			normal.set(x/len, y/len, z/len);
		}		
		return normal;
	}
	
	/**
	 * 
	 * @return x values
	 */
	public float getX()
	{
		return xyz[0];
	}
	
	/**
	 * 
	 * @return y values
	 */
	public float getY()
	{
		return xyz[1];
	}
	
	/**
	 * 
	 * @return z value
	 */
	public float getZ()
	{
		return xyz[2];
	}
	
	/**
	 * static function for performing cross
	 * @param src1
	 * @param src2
	 * @return, cross product of src1 and src2
	 */
	public static Vec3 cross(Vec3 src1, Vec3 src2)
	{
		Vec3 dest = new Vec3();
		float x1 = src1.getX();
		float y1 = src1.getY();
		float z1 = src1.getZ();
		
		float x2 = src2.getX();
		float y2 = src2.getY();
		float z2 = src2.getZ();
		
		dest.set((y1*z2 - z1*y2), (z1*x2 - x1*z2), (x1*y2 - y1*x2));
		
		return dest;
	}
	
	/**
	 * 
	 * @return length of vector
	 */
	public float length()
	{
		return (float) Math.sqrt((xyz[0]*xyz[0]) + (xyz[1]*xyz[1]) + (xyz[2]*xyz[2]));
	}
	
	/**
	 * static function for dot product 
	 * @param src1
	 * @param src2
	 * @return, dot product of src1 and src2
	 */
	public static float dot(Vec3 src1, Vec3 src2)
	{
		return (src1.getX()*src2.getX()) + (src1.getY()*src2.getY()) + (src1.getZ()*src2.getZ());
	}
	
	/**
	 * computes cross product
	 * @param src
	 * @return cross product with src
	 */
	public Vec3 cross(Vec3 src)
	{
		Vec3 dest = new Vec3();
		float x1 = xyz[0];
		float y1 = xyz[1];
		float z1 = xyz[2];
		
		float x2 = src.getX();
		float y2 = src.getY();
		float z2 = src.getZ();
		
		dest.set((y1*z2 - z1*y2), (z1*x2 - x1*z2), (x1*y2 - y1*x2));
		return dest;
	}
	
	/**
	 * computes dot product
	 * @param src
	 * @return dot product with src
	 */
	public float dot(Vec3 src)
	{
		return (getX()*src.getX()) + (getY()*src.getY()) + (getZ()*src.getZ());
	}
	
	/**
	 * performs addition operation with src 
	 * @param src
	 */
	public void add(Vec3 src)
	{
		xyz[0] += src.getX();
		xyz[1] += src.getY();
		xyz[2] += src.getZ();
	}
	
	/**
	 * performs addition operation between two vectors
	 * @param src1
	 * @param src2
	 * @return, addition result
	 */
	public static Vec3 add(Vec3 src1, Vec3 src2)
	{
		Vec3 dest = new Vec3();
		dest.set(src1.getX()+src2.getX(), src1.getY()+src2.getY(), src1.getZ()+src2.getZ());
		return dest;
	}
	
	/**
	 * performs subtract operation with src
	 * @param src
	 */
	public void subtract(Vec3 src)
	{
		xyz[0] -= src.getX();
		xyz[1] -= src.getY();
		xyz[2] -= src.getZ();
	}
	
	/**
	 * performs subtract operation between two vectors
	 * @param src1
	 * @param src2
	 * @return result of subtract operation
	 */
	public static Vec3 subtract(Vec3 src1, Vec3 src2)
	{
		Vec3 dest = new Vec3();
		dest.set(src1.getX()-src2.getX(), src1.getY()-src2.getY(), src1.getZ()-src2.getZ());
		return dest;
	}
	
	/**
	 * multiplies vector with scalar values
	 * @param value, scalar value to multiply
	 */
	public void scale(float value)
	{
		xyz[0] *= value;
		xyz[1] *= value;
		xyz[2] *= value;
	}
	
	/**
	 * finds direction vector to dest
	 * @param dest, destination vector 
	 * @return, direction vector
	 */
	public Vec3 direction(Vec3 dest)
	{
		Vec3 dir = new Vec3();
		float dx = xyz[0] - dest.getX();
		float dy = xyz[1] - dest.getY();
		float dz = xyz[2] - dest.getZ();
		
		float len = (float) Math.sqrt((dx*dx) + (dy*dy) + (dz*dz));
		if (len == 0.0f)
		{
			dir.set(0, 0, 0);
		} else {			
			dir.set(dx/len, dy/len, dz/len);
		}
		return dir;
	}
	
	/**
	 * performs linear interpolation with src2
	 * @param src2
	 * @param amount
	 * @return interpolated vector
	 */
	public Vec3 LinearInterpolate(Vec3 src2, float amount)
	{
		Vec3 dest = new Vec3();
		float tx = xyz[0] + amount*(src2.getX() - xyz[0]);
		float ty = xyz[1] + amount*(src2.getY() - xyz[1]);
		float tz = xyz[2] + amount*(src2.getZ() - xyz[2]);
		dest.set(tx, ty, tz);
		return dest;
	}
}
