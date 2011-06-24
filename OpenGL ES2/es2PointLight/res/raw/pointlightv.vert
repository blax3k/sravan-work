attribute vec4 a_position;
attribute vec4 a_normals;
attribute vec2 a_texCoords;
uniform mat4 u_ModelViewMatrix;
uniform mat3 u_NormalsMatrix;
uniform vec3 u_LightPosition;
uniform vec4 u_LightColor;
varying vec3 v_colorWeight;
varying vec2 v_texCoords;
void main()
{
	gl_Position = u_ModelViewMatrix * a_position;
	v_texCoords = a_texCoords;
	
	vec3 normals = normalize(u_NormalsMatrix * a_normals);
	
	vec3 lightDirection = normalize(u_LightPosition - gl_Position.xyz);
	float lightWeight = max(dot(normals, lightDirection),0.0);
	v_colorWeight = vec3(0.2,0.2,0.2) + (lightWeight * u_LightColor);
}