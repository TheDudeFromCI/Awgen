#version 330

uniform mat4 _mMat;
uniform mat4 _mvpMat;
uniform vec3 _sunDir = vec3(0.466084958468, -0.847427197214, 0.254228159164);

layout(location = 0) in vec3 _vertPos;
layout(location = 1) in vec3 _normal;
layout(location = 2) in vec2 _uv;

out vec3 lightColor;
out vec3 pos;
out vec3 normal;
out vec2 uv;

void main()
{
	gl_Position = _mvpMat * vec4(_vertPos, 1.0);

	pos = (_mMat * vec4(_vertPos, 1.0)).xyz;
	normal = normalize((_mMat * vec4(_normal, 0.0)).xyz);
	uv = _uv;

	float light = dot(-normal, _sunDir) * 0.5 + 0.5;
	lightColor = vec3(light, light, light);
}
---
---
#version 330 core

uniform float _textureScale = 1.0;
uniform float _triplanarBlending = 1.0;
uniform sampler2D _splat;
uniform sampler2D _diffuse1;
uniform sampler2D _diffuse2;
uniform sampler2D _diffuse3;

in vec3 lightColor;
in vec3 pos;
in vec3 normal;
in vec2 uv;

out vec4 color;

vec4 triplanar(sampler2D tex, vec3 p, vec3 n)
{
	vec4 x = texture(tex, p.zy * _textureScale);
	vec4 y = texture(tex, p.xz * _textureScale);
	vec4 z = texture(tex, p.xy * _textureScale);

	vec3 b = pow(abs(n), vec3(_triplanarBlending));
	b = b / (b.x + b.y + b.z);

	return x * b.x + y * b.y + z * b.z;
}

void main()
{
	vec3 splat = texture(_splat, uv).rgb;

	vec3 r = triplanar(_diffuse1, pos, normal).rgb;
	vec3 g = triplanar(_diffuse2, pos, normal).rgb;
	vec3 b = triplanar(_diffuse3, pos, normal).rgb;

	vec3 tex = r * splat.r + g * splat.g + b * splat.b;
	color = vec4(tex * lightColor, 1.0);
}