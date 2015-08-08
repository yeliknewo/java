#version 450 core

layout ( location = 0 ) in vec4 position;
layout ( location = 1 ) in vec2 texCoord;

uniform mat4 u_pr_matrix;
uniform mat4 u_vw_matrix;
uniform mat4 u_ml_matrix;

out Data{
	vec2 texCoord;
} DataOut;

void main()
{
	DataOut.texCoord = texCoord;
	
	gl_Position = u_pr_matrix * u_vw_matrix * u_ml_matrix * position;
}