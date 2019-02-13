#version 330

uniform mat4 _mvpMat;

layout(location = 0) in vec2 _vertPos;
layout(location = 1) in vec2 _uv;

out vec2 uv;

void main()
{
	gl_Position = _mvpMat * vec4(_vertPos, 0.0, 1.0);
	//gl_Position = vec4(_vertPos, 0.0, 1.0);
	uv = _uv;
}
---
---
#version 330 core

uniform sampler2D _texture;

in vec2 uv;

out vec4 color;

void main()
{
	color = texture(_texture, uv);
}