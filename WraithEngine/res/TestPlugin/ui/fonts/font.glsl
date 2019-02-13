#version 330

uniform mat4 _mvpMat;

layout(location = 0) in vec2 _vertPos;
layout(location = 1) in vec2 _uv;

out vec2 uv;

void main()
{
	gl_Position = _mvpMat * vec4(_vertPos, 0.0, 1.0);
	uv = _uv;
}
---
---
#version 330 core

uniform sampler2D _font;

in vec2 uv;

out vec4 color;

const float smoothing = 1.0/32.0;
void main()
{
	float distance = texture(_font, uv).a;
	float alpha = smoothstep(0.5 - 0, 0.5 + smoothing, distance);
	color = vec4(1.0, 0.0, 0.0, alpha);
}