attribute vec4 a_position; 
attribute vec2 a_texCoords; 
uniform mat4 u_ModelViewMatrix; 
varying vec2 v_texCoords; 
void main() 
{ 
	gl_Position = u_ModelViewMatrix * a_position; 
	v_texCoords = a_texCoords; 
}