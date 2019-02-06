#version 330

uniform mat4 _mMat;
uniform mat4 _mvMat;
uniform mat4 _mvpMat;

layout(location = 0) in vec3 _vertPos;
layout(location = 1) in vec3 _normal;
layout(location = 2) in vec3 _tangent;
layout(location = 3) in vec3 _bitangent;
layout(location = 4) in vec2 _uv;

out VS_OUT
{
	vec3 pos;
	vec3 normal;
	mat3 TBN;
	vec2 uv;
} o;

void main()
{
	gl_Position = _mvpMat * vec4(_vertPos, 1.0);

	o.pos = (_mMat * vec4(_vertPos, 1.0)).xyz;
	o.normal = normalize((_mMat * vec4(_normal, 0.0)).xyz);
	o.uv = _uv;

	vec3 T = normalize(vec3(_mMat * vec4(_tangent, 0.0)));
	vec3 B = normalize(vec3(_mMat * vec4(_bitangent, 0.0)));
	vec3 N = normalize(vec3(_mMat * vec4(_normal, 0.0)));
	o.TBN = mat3(T, B, N);
}
---
---
#version 330 core

uniform vec3 _sunDir = vec3(0.466084958468, -0.847427197214, 0.254228159164);
uniform float _textureScale = 1.0;
uniform float _triplanarBlending = 1.0;
uniform sampler2D _splat;
uniform sampler2D _diffuse1;
uniform sampler2D _diffuse2;
uniform sampler2D _diffuse3;
uniform sampler2D _normal1;
uniform sampler2D _normal2;
uniform sampler2D _normal3;

in VS_OUT
{
	vec3 pos;
	vec3 normal;
	mat3 TBN;
	vec2 uv;
} i;

out vec4 color;

vec4 triplanar(sampler2D tex)
{
	vec4 x = texture(tex, i.pos.zy * _textureScale);
	vec4 y = texture(tex, i.pos.xz * _textureScale);
	vec4 z = texture(tex, i.pos.xy * _textureScale);

	vec3 b = pow(abs(i.normal), vec3(_triplanarBlending));
	b = b / (b.x + b.y + b.z);

	return x * b.x + y * b.y + z * b.z;
}

vec3 fullSample(sampler2D dif, sampler2D nor)
{
	vec3 matDiffuse = triplanar(dif).rgb;
	vec3 matNormal = normalize(triplanar(nor).xyz * 2.0 - 1.0);
	matNormal = normalize(i.TBN * matNormal);

	float light = dot(-matNormal, _sunDir) * 0.5 + 0.5;
	return matDiffuse * vec3(light);
}

void main()
{
	vec3 splat = texture(_splat, i.uv).rgb;

	vec3 r = fullSample(_diffuse1, _normal1).rgb;
	vec3 g = fullSample(_diffuse2, _normal2).rgb;
	vec3 b = fullSample(_diffuse3, _normal3).rgb;

	vec3 tex = r * splat.r + g * splat.g + b * splat.b;
	color = vec4(tex, 1.0);
}