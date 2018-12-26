name=bone_shader
---
#version 330
#define MAX_BONES 128

uniform mat4 _mvpMat;
uniform mat4 _bones[MAX_BONES];

layout(location = 0) in vec3 _vertPos;
layout(location = 1) in vec3 _normal;
layout(location = 2) in vec4 _boneData0;
layout(location = 3) in vec4 _boneData1;

out vec3 normal;

void main()
{
	mat4 boneTransform = _bones[uint(_boneData0.x)] * _boneData0.z;
	boneTransform += _bones[uint(_boneData0.y)] * _boneData0.w;
	boneTransform += _bones[uint(_boneData1.x)] * _boneData1.z;
	boneTransform += _bones[uint(_boneData1.y)] * _boneData1.w;
	
	vec4 pos = boneTransform * vec4(_vertPos, 1.0);
	gl_Position = _mvpMat * pos;
	
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