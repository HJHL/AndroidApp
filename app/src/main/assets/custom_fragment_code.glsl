#version 300 es
precision mediump float;

in vec3 ourColor;
in vec2 texCoord;

out vec4 vColor;

uniform sampler2D texture0;
uniform sampler2D texture1;

void main() {
    vColor = mix(texture(texture0, texCoord), texture(texture1, texCoord), 1.0) * vec4(ourColor, 1.0);
}
