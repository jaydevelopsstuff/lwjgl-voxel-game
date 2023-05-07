#version 330 core
layout(location = 0) in vec3 position;
layout(location = 1) in vec2 texCoordIn;

out vec2 texCoord;

uniform mat4 projectionMatrix;

void main() {
    gl_Position = projectionMatrix * vec4(position, 1.0);
    texCoord = texCoordIn;
}