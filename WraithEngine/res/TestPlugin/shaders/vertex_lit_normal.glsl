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
	vec3 normal;
	mat3 TBN;
	vec2 uv;
} o;

void main()
{
	gl_Position = _mvpMat * vec4(_vertPos, 1.0);


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

uniform sampler2D _diffuse;
uniform sampler2D _normal;
uniform vec3 _sunDir = vec3(0.466084958468, -0.847427197214, 0.254228159164);

in VS_OUT
{
	vec3 normal;
	mat3 TBN;
	vec2 uv;
} i;

out vec4 color;

void main()
{
	vec3 tex = texture(_diffuse, i.uv).rgb;
	vec3 nor = texture(_normal, i.uv).xyz;
	nor = normalize(i.TBN * nor);

	float light = dot(-nor, _sunDir) * 0.5 + 0.5;

	color = vec4(tex * light, 1.0);
}