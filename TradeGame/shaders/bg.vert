#version 450 core

layout (location = 0) out vec4 color;
layout (location = 1) in vec2 texCoord;

uniform sampler2D u_tex;

void main()
{
	color = texture(u_tex, texCoord);
}