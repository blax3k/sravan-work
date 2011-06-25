attribute vec4 a_position;
attribute vec3 a_normals;
attribute vec2 a_texCoords;

uniform mat4 u_ModelViewMatrix;
uniform mat3 u_NormalsMatrix;

varying vec2 v_texCoords;
varying vec3 v_Normals;
varying vec4 v_position;
void main()
{
	gl_Position = u_ModelViewMatrix * a_position;
	v_position = gl_Position;
	v_Normals = normalize(u_NormalsMatrix * a_normals);
	v_texCoords = a_texCoords; 
}
