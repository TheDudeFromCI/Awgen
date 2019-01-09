#version 330

uniform mat4 _mvpMat;
uniform vec3 _sunDir = vec3(0.466084958468, -0.847427197214, 0.254228159164);

layout(location = 0) in vec3 _vertPos;
layout(location = 1) in vec3 _normal;
layout(location = 2) in vec2 _uv;

out vec3 lightColor;
out vec2 uv;

void main()
{
	gl_Position = _mvpMat * vec4(_vertPos, 1.0);
	uv = _uv;

	float light = dot(-_normal, _sunDir) * 0.5 + 0.5;
	lightColor = vec3(light, light, light);
}
---
---
#version 330 core

uniform sampler2D _diffuse;

in vec3 lightColor;
in vec2 uv;

out vec4 color;

void main()
{
	vec4 tex = texture(_diffuse, uv);
	color = vec4(tex.rgb * lightColor, 1.0);
}