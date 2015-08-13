#version 450 core

layout (location = 0) out vec4 color;

uniform sampler2D u_tex;

in Data {
	vec2 texCoord;
} DataIn;

void main()
{
	color = texture(u_tex, DataIn.texCoord);
}