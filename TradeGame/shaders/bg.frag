#version 450 core

layout ( location = 0 ) in vec4 position;

uniform mat4 u_pr_matrix;
uniform mat4 u_vw_matrix;
uniform mat4 u_ml_matrix;

void main()
{
	gl_Position = u_pr_matrix * u_vw_matrix * u_ml_matrix * position;
}