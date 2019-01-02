name=normal_shader
---
#version 330
#define MAX_BONES 128

uniform mat4 _mvpMat;

layout(location = 0) in vec3 _vertPos;
layout(location = 1) in vec3 _normal;
layout(location = 2) in vec2 _uv;

out vec3 normal;
out vec2 uv;

void main()
{
	gl_Position = _mvpMat * vec4(_vertPos, 1.0);
	
	normal = _normal;
	uv = _uv;
}
---
---
#version 330 core

uniform sampler2D _diffuse;

in vec3 normal;
in vec2 uv;

out vec4 color;

void main()
{
	vec4 tex = texture(_diffuse, uv);
	color = vec4(tex.rgb, 1.0);
}