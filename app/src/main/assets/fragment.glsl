#version 300 es

#ifdef GL_ES
precision highp float;
#endif

in vec3 ourColor;
in vec2 texCoord;

out vec4 outColor;

void main() {
    outColor = vec4(0.3, 0.4, 0.5, 1.0);
}
