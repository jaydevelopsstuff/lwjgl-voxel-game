#version 410
layout (location = 0) in vec3 position;

out vec3 texCoord;

uniform mat4 viewMatrix;
uniform mat4 projectionMatrix;

void main() {
    texCoord = position;
    gl_Position = projectionMatrix * viewMatrix * vec4(position, 1.0);
}