precision mediump float;

varying vec2 v_texCoords;
varying vec3 v_Normals;
varying vec4 v_position;

uniform vec3 u_LightPosition;
uniform vec3 u_LightColor;
uniform sampler2D u_texId;



void main()
{
	vec4 texColor = texture2D(u_texId, v_texCoords);
	
	vec3 normal = normalize(v_Normals);
	vec3 lightDirection = normalize(u_LightPosition - v_position.xyz);
	
	float leightWeight = max(dot(lightDirection,normal),0.0);
	vec3 calcColor = vec3(0.2,0.2,0.2) +  (u_LightColor * leightWeight);
	
	gl_FragColor = vec4(texColor.rgb * calcColor, texColor.a);
}
