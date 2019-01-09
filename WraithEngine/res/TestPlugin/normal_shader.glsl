#version 330

uniform mat4 _mvpMat;

layout(location = 0) in vec3 _vertPos;
layout(location = 1) in vec3 _normal;
layout(location = 2) in vec2 _uv;

out vec3 normal;

void main()
{
	gl_Position = _mvpMat * vec4(_vertPos, 1.0);
	normal = _normal;
}
---
---
#version 330 core

in vec3 normal;

out vec4 color;

void main()
{
	color = vec4(normal * 0.5 + 0.5, 1.0);
}