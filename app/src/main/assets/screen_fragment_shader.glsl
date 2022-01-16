#version 300 es

precision highp float;

in vec3 ourColor;
in vec2 texCoord;

uniform sampler2D screenTexture;

out vec4 fragColor;

void main() {
//    vec2 coord = vec2(1.0f, 1.0f);
    vec3 color = texture(screenTexture, texCoord).rgb;
    fragColor = vec4(color, 1.0);
}
