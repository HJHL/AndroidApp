#version 300 es

precision highp float;

in vec3 ourColor;
in vec2 texCoord;

uniform sampler2D texture0;

out vec4 fragColor;

const vec3 M = vec3(0.299, 0.587, 0.114);

void main() {
    vec4 mask = texture(texture0, texCoord);
    float lum = dot(mask.rgb, M);
    fragColor = vec4(vec3(lum), 1.0);
}
