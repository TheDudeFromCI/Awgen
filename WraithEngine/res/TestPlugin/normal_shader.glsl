name=normal_shader
---
#version 330
#define MAX_BONES 128

uniform mat4 _mvpMat;

layout(location = 0) in vec3 _vertPos;
layout(location = 1) in vec3 _normal;

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