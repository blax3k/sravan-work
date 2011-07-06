attribute vec4 a_position;
attribute vec3 a_normals;
attribute vec2 a_texCoords;

uniform mat4 u_ModelViewMatrix;
uniform mat3 u_NormalsMatrix;
uniform vec3 u_LightPosition;
uniform vec3 u_LightColor;

uniform vec3 u_SpecLightColor;
uniform float u_Shine;

varying vec3 v_colorWeight;
varying vec2 v_texCoords;
void main()
{
	gl_Position = u_ModelViewMatrix * a_position;
	v_texCoords = a_texCoords;
	
	vec3 normals = normalize(u_NormalsMatrix * a_normals);
	
	vec3 halfVec = normalize(u_LightPosition - gl_Position.xyz);
	float lightWeight = max(dot(normals, halfVec),0.0);
	float specWeight = pow(lightWeight,u_Shine);
	
	
	float dist = length(u_LightPosition - gl_Position.xyz);
	
	
	//f = 1/(C + Ld + Qd2)
	float attenuation = 1.0/(1.0 + (0.2 * dist) + (0.08 * dist * dist));
	
	
	
	v_colorWeight = vec3(0.2,0.2,0.2) + (u_LightColor*lightWeight * attenuation) + (u_SpecLightColor*specWeight*attenuation);
}