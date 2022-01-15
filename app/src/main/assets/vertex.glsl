#version 300 es

layout(location = 0) in vec3 vPos;
layout(location = 1) in vec3 vColor;
layout(location = 2) in vec2 vTexCoord;

out vec3 ourColor;
out vec2 texCoord;

void main() {
    gl_Position = vec4(vPos, 1.0f);
    ourColor = vColor;
    texCoord = vTexCoord;
}
