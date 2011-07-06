precision mediump float;

varying vec2 v_texCoords;
varying vec3 v_Normals;
varying vec4 v_position;

uniform vec3 u_LightPosition;
uniform vec3 u_LightColor;
uniform sampler2D u_texId;

uniform vec3 u_SpecLightColor;
uniform float u_Shine;

void main()
{
	vec4 texColor = texture2D(u_texId, v_texCoords);
	
	vec3 normal = normalize(v_Normals);
	vec3 halfVec = normalize(u_LightPosition - v_position.xyz);
	
	float leightWeight = max(dot(halfVec,normal),0.0);
	float specWeight = pow(leightWeight,u_Shine);
	
	float dist = length(u_LightPosition - v_position.xyz);

	float attenuation = 1.0/(1.0 + (1.2 * dist) + (1.0 * dist * dist));
	
	vec3 calcColor = vec3(0.2,0.2,0.2) +  (u_LightColor * (leightWeight * attenuation)) + (u_SpecLightColor*specWeight*attenuation);
	
	gl_FragColor = vec4(texColor.rgb * calcColor, texColor.a);
}
