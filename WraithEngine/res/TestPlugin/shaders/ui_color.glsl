#version 330

uniform mat4 _mvpMat;

layout(location = 0) in vec2 _vertPos;

out vec2 uv;

void main()
{
	gl_Position = _mvpMat * vec4(_vertPos, 0.0, 1.0);
}
---
---
#version 330 core

uniform vec4 _color;

out vec4 color;

void main()
{
	color = _color;
}