precision mediump float;
varying vec3 v_colorWeight;
uniform sampler2D u_texId;
varying vec2 v_texCoords;
void main()
{
	vec4 texColor = texture2D(u_texId, v_texCoords);
	gl_FragColor = vec4(texColor.rgb * v_colorWeight, texColor.a);
}
