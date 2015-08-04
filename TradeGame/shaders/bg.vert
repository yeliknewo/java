#version 450 core

layout (location = 0) out vec4 color;
layout (location = 1) in vec2 texCoord;

uniform sampler2D uTex;

void main()
{
	color = texture(uTex, texCoord);
}