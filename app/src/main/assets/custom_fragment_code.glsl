#version 300 es
precision mediump float;

in vec3 ourColor;
in vec2 texCoord;

uniform sampler2D texture0;
uniform sampler2D texture1;
uniform vec4 color;

out vec4 outColor;

void main() {
    vec4 mask = texture(texture0, texCoord);
    float luma = dot(mask.rgb, color.xyz);
    outColor = vec4(vec3(luma), 1.0);
    //    vColor = texture(texture0, texCoord);
}
