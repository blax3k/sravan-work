package es2.common;

/**
 * A 3X3 matrix
 * @author sravan
 *
 */
public class Mat3 {
	public float[] values = null;
	
	/**
	 * default constructor, allocated array of size 9
	 */
	public Mat3()
	{
		values = new float[9];
	}
	/**
	 * in-place copy
	 * @param vals
	 */
	public Mat3(float[] vals)
	{
		values = vals;
	}
	
	/**
	 * set matrix to identity
	 */
	public void SetIdentity()
	{
		if (values == null)
		{
			values = new float[9];
		}
		for (int i=0; i<9; i++) {
			values[i] = 0;
		}
		values[0] = 1f;
		values[4] = 1f;
		values[8] = 1f;
	}
	/**
	 * top-left 3X3 matrix will be copied
	 * @param mat4, 4X4 matrix array
	 */
	public void SetFrom4X4(float[] mat4)
	{
		if (mat4.length != 16)
		{
			//not a 4X4 matrix
			return;
		}
		values[0] = mat4[0];
		values[1] = mat4[1];
		values[2] = mat4[2];
		
		values[3] = mat4[4];
		values[4] = mat4[5];
		values[5] = mat4[6];
		
		values[6] = mat4[8];
		values[7] = mat4[9];
		values[8] = mat4[10];
	}
	
	/**
	 * copies from array
	 * @param mat
	 */
	public void Set(float[] mat)
	{
		if (mat.length == 16)
		{
			SetFrom4X4(mat);
		} else if (mat.length == 9){
			values = mat.clone();
		} else {
			//nothing
		}
	}
	
	
	public void SetInPlace(float[] mat)
	{
		values = mat;
	}
	
	/**
	 * return 4X4 float array
	 * @return
	 */
	public float[] toMat4()
	{
		float[] mat4 = new float[16];
		mat4[0] = values[0];
		mat4[1] = values[1];
		mat4[2] = values[2];
		mat4[3] = 0f;
		
		mat4[4] = values[3];
		mat4[5] = values[4];
		mat4[6] = values[5];
		mat4[7] = 0f;
		
		mat4[8] = values[6];
		mat4[9] = values[7];
		mat4[10] = values[8];
		mat4[11] = 0f;
		
		mat4[12] = 0f;
		mat4[13] = 0f;
		mat4[14] = 0f;
		mat4[15] = 1f;
		
		return mat4;
	}
	
	/**
	 * inverts and result will be stored in dest
	 * @param dest, for result
	 * @return, true, if successful, else false
	 */
	public boolean invert(Mat3 dest)
	{
		float a00 = values[0];
		float a01 = values[1];
		float a02 = values[2];
		
		float a10 = values[3];
		float a11 = values[4];
		float a12 = values[5];
		
		float a20 = values[6];
		float a21 = values[7];
		float a22 = values[8];
		
		float b01 = a11*a22 - a12*a21;
		float b02 = a12*a20 - a10*a22;
		float b03 = a10*a21 - a20*a11;
		
		float det = a00*b01 + a01*b02 + a02*b03;
		if (det == 0.0f) {
//			dest = null;
			return false;
		}
		float invdet = 1.0f/det;
		
		float[] destArray = new float[9];
		if (dest == null)
			dest = new Mat3();
		destArray[0] = b01 * invdet;
		destArray[1] = (a02*a21-a22*a01)*invdet;
		destArray[2] = (a12*a01 - a02*a11)*invdet;
		destArray[3] = b02*invdet;
		destArray[4] = (a22*a00 - a02*a20)*invdet;
		destArray[5] = (a02*a10 - a12*a00)*invdet;
		destArray[6] = b03*invdet;
		destArray[7] = (a01*a20-a21*a00)*invdet;
		destArray[8] = (a11*a00 - a01*a10)*invdet;

		dest.SetInPlace(destArray);
		return true;
	}
	
	/**
	 * inverts matrix
	 * @return
	 */
	public boolean invert()
	{
		float a00 = values[0];
		float a01 = values[1];
		float a02 = values[2];
		
		float a10 = values[3];
		float a11 = values[4];
		float a12 = values[5];
		
		float a20 = values[6];
		float a21 = values[7];
		float a22 = values[8];
		
		float b01 = a11*a22 - a12*a21;
		float b02 = a12*a20 - a10*a22;
		float b03 = a10*a21 - a20*a11;
		
		float det = a00*b01 + a01*b02 + a02*b03;
		if (det == 0.0f) {			
			return false;
		}
		float invdet = 1.0f/det;
		
		values[0] = b01 * invdet;
		values[1] = (a02*a21-a22*a01)*invdet;
		values[2] = (a12*a01 - a02*a11)*invdet;
		values[3] = b02*invdet;
		values[4] = (a22*a00 - a02*a20)*invdet;
		values[5] = (a02*a10 - a12*a00)*invdet;
		values[6] = b03*invdet;
		values[7] = (a01*a20-a21*a00)*invdet;
		values[8] = (a11*a00 - a01*a10)*invdet;
		
		return true;
	}
	
	/**
	 * transpose matrix
	 */
	public void transpose()
	{
		float a01 = values[1];
		float a02 = values[2];
		float a12 = values[5];
		
		values[1] = values[3];
        values[2] = values[6];
        values[3] = a01;
        values[5] = values[7];
        values[6] = a02;
        values[7] = a12;
	}
}
